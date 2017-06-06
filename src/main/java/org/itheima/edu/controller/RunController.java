package org.itheima.edu.controller;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.LogStream;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.messages.*;
import org.itheima.edu.util.JsonUtils;
import org.itheima.edu.util.ResponseCode;
import org.itheima.edu.util.StreamUtils;
import org.itheima.online.magic.CompilerUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Poplar on 2017/6/6.
 */
@Controller
@RequestMapping(produces = "application/json;charset=UTF-8")
public class RunController {


    // 数据源目录
    @Value("${config.dir.source}")
    String sourceFolder;
    //	private static final String rootPath = "/root/newstrap";
    @Value("${config.dir.root}")
    String rootPath;

    private ExecutorService threadPool;
    private DockerClient docker;


    @PostConstruct
    public void postConstruct() {
        System.out.println("I'm  init  method  using  @PostConstrut....");
        try {
            init();
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }

    public void init() throws ServletException {
        threadPool = Executors.newFixedThreadPool(20);
        try {
            docker = DefaultDockerClient.fromEnv().uri("http://localhost:2735").build();
        } catch (DockerCertificateException e) {
            e.printStackTrace();
        }
    }


    // 运行指定题目代码
    @RequestMapping(value = "/run")
    @ResponseBody
    public void run(HttpServletRequest request,
                    HttpServletResponse response) throws IOException {
//                      String chatper, String question, String content) {

//        try {
//            response.getOutputStream().write("success".getBytes("utf-8"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        executeRequest(request, response);

    }

    private void executeRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        String username = request.getParameter("username");
        String chapter = request.getParameter("chapter");
        String questionid = request.getParameter("questionid");
        String code = request.getParameter("code");

        chapter = new String(chapter.getBytes("iso8859-1"), "utf-8");

        File rootDir = new File(rootPath);
        long currentTime = System.currentTimeMillis();
        // /root/newstrap/result/aaa/Array-1/questionxxx/865757798789/src
        // /root/newstrap/result/aaa/Array-1/questionxxx/865757798789/bin
        String tempRoot = "result/" + username + "/" + chapter + "/" + questionid + "/" + currentTime + "/";
        File srcDir = new File(rootDir, tempRoot + "src");
        srcDir.mkdirs();
        File binDir = new File(rootDir, tempRoot + "bin");
        binDir.mkdirs();
        // /root/newstrap/result/aaa/Array-1/questionxxx/865757798789/report
        File reportDir = new File(rootDir,tempRoot + "report");
        reportDir.mkdirs();
        File ItheimaJava = new File(srcDir, "Itheima.java");
        FileOutputStream fos = new FileOutputStream(ItheimaJava);
        fos.write(code.getBytes("utf-8"));
        StreamUtils.closeIO(fos);
        CompilerUtils.Result result = CompilerUtils.doMagic(binDir.getAbsolutePath(), new String[]{ItheimaJava.getAbsolutePath()});
        if (result.code == 200) {
//            E:\cms\newstrap\exam\String-2\mirrorEnds\build
//            E:\cms\newstrap\exam\String-2\mirrorEnds\build\libs
            String questionPath = sourceFolder + chapter + File.separator + questionid + File.separator + "build";
            String command =
                    "java" + " -Dfile.encoding=UTF-8"
                            + " -Djava.ext.dirs=" + questionPath + "/libs"
                            + " -cp .;" + questionPath + ";" + binDir.getAbsolutePath()
                            + " TestMain " + reportDir.getAbsolutePath();
            System.out.println(command);
            Process p = Runtime.getRuntime().exec(command);
            try {
                String parseCmdStream = StreamUtils.parseCmdStream(p.getInputStream());
                System.out.println(parseCmdStream);
                p.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 在子线程中执行业务调用，并由其负责输出响应，主线程退出
//			final AsyncContext ctx = request.startAsync();
//			ctx.setTimeout(120000);
//			threadPool.execute(new Work(ctx, srcDir, binDir, reportDir, questionid));
//            E:\cms\newstrap\result\vvv\minCat\1496717064235\report\test.html
//            String resultPath = reportDir + File.separator + "test.html";
            HashMap<String, Object> map = new HashMap<>();
            map.put("username", username);
            map.put("chapter", chapter);
            map.put("questionid", questionid);
            map.put("currentTime", currentTime);

            response.getOutputStream().write(JsonUtils.toWrapperJson(map).getBytes("utf-8"));
        } else {
            response.getOutputStream().write(
                    JsonUtils.toWrapperJson(ResponseCode.ExamError.RUN_EXEC_FAILD, result.message).getBytes("utf-8"));
        }
    }

    class Work implements Runnable {
        private AsyncContext context;
        private File srcDir;
        private File binDir;
        private File reportDir;
        private String questionid;


        public Work(AsyncContext context, File srcDir, File binDir, File reportDir, String questionid) {
            this.context = context;
            this.srcDir = srcDir;
            this.binDir = binDir;
            this.reportDir = reportDir;
            this.questionid = questionid;
        }

        @Override
        public void run() {
            try {
                runDocker(srcDir, binDir, reportDir, questionid);

                File reportFile = new File(reportDir, "html/suite1_test1_results.html");
                if (reportFile.exists()) {
                    FileInputStream fis = new FileInputStream(reportFile);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while ((len = fis.read(buffer)) != -1) {
                        baos.write(buffer, 0, len);
                    }
                    fis.close();
                    baos.close();
                    context.getResponse().getOutputStream().write(baos.toByteArray());
                } else {
                    context.getResponse().getOutputStream().write("time out...".getBytes());
                }
                context.complete();
            } catch (IOException e) {

            }
        }
    }

    public void runDocker(File srcDir, File binDir, File reportDir, String questionid) {
        try {

            final HostConfig hostConfig = HostConfig.builder()
                    .appendBinds(HostConfig.Bind.from("/root/newstrap/").to("/root/newstrap/").readOnly(true).build())
                    .appendBinds(HostConfig.Bind.from(reportDir.getAbsolutePath()).to(reportDir.getAbsolutePath()).readOnly(false).build())
                    .build();
            // Create container with exposed ports
            final ContainerConfig containerConfig = ContainerConfig.builder().hostConfig(hostConfig)
                    .image("hub.c.163.com/library/java:8").tty(true).attachStderr(true).attachStdin(true)
                    .attachStdout(true).build();

            final ContainerCreation creation = docker.createContainer(containerConfig);
            final String id = creation.id();

            // Inspect container
            final ContainerInfo info = docker.inspectContainer(id);
            System.out.println(info);

            // Start container
            docker.startContainer(id);
            // " -cp + ":" + " TestMain " +
            final String[] command = {"java", "-Dfile.encoding=UTF-8",
                    "-Djava.ext.dirs=/root/newstrap/exam/" + questionid + "/lib", "-cp",
                    ".:/root/newstrap/exam/" + questionid + ":" + binDir.getAbsolutePath(), "TestMain",
                    reportDir.getAbsolutePath()};
            final ExecCreation execCreation = docker.execCreate(id, command,
                    DockerClient.ExecCreateParam.attachStdout(), DockerClient.ExecCreateParam.attachStderr());
            final LogStream output = docker.execStart(execCreation.id());
            System.out.println("ready go ");
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        //docker timer make sure docker stop after run 15seconds
                        docker.stopContainer(id, 0);
                        docker.removeContainer(id, true);
                    } catch (Exception e) {

                    }
                }
            }, 15000);
            final String execOutput = output.readFully();
            System.out.println(execOutput);

            // Kill container
            docker.killContainer(id);
            System.out.println("killed :" + id);
            // Remove container
            docker.removeContainer(id, true);
            // Close the docker client
            //docker.close();
        } catch (Exception e) {

        }
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("I'm  destory method  using  @PreDestroy.....");
    }


}
