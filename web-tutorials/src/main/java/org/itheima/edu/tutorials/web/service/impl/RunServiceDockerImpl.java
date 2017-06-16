package org.itheima.edu.tutorials.web.service.impl;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.LogStream;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.messages.*;
import org.itheima.edu.jcompiler.JCompilerUtils;
import org.itheima.edu.tutorials.utils.JsonUtils;
import org.itheima.edu.tutorials.utils.StreamUtils;
import org.itheima.edu.tutorials.web.ResponseCode;
import org.itheima.edu.tutorials.web.service.RunService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Poplar on 2017/6/16.
 */
@Service
public class RunServiceDockerImpl implements RunService{

    public static final String URI_DOCKER = "http://192.168.1.131:2735";
//    public static final String URI_DOCKER = "http://localhost:2735";
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
            docker = DefaultDockerClient.fromEnv().uri(URI_DOCKER).build();
        } catch (DockerCertificateException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String run(HttpServletRequest request, String username, String chapter, String questionid, String code) throws IOException{

        String responseStr;File rootDir = new File(rootPath);
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
        JCompilerUtils.Result result = JCompilerUtils.doMagic(binDir.getAbsolutePath(), new String[]{ItheimaJava.getAbsolutePath()});
        if (result.code == 200) {
//            E:\cms\newstrap\exam\String-2\mirrorEnds\
//            E:\cms\newstrap\exam\String-2\mirrorEnds\libs
            String questionPath = sourceFolder + File.separator + chapter + File.separator + questionid ;
            String command =
                    "java" + " -Dfile.encoding=UTF-8"
                            + " -Djava.ext.dirs=" + questionPath + "/libs"
                            + " -cp .;" + questionPath + ";" + binDir.getAbsolutePath()
                            + " TestMain " + reportDir.getAbsolutePath();
            System.out.println(command);
//            Process p = Runtime.getRuntime().exec(command);
//            try {
//                String parseCmdStream = StreamUtils.parseCmdStream(p.getInputStream());
////                System.out.println(parseCmdStream);
//                p.waitFor();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            // 在子线程中执行业务调用，并由其负责输出响应，主线程退出
			final AsyncContext ctx = request.startAsync();
			ctx.setTimeout(120000);
			threadPool.execute(new Work(ctx, srcDir, binDir, reportDir, questionid));
//            E:\cms\newstrap\result\vvv\minCat\1496717064235\report\test.html
//            String resultPath = reportDir + File.separator + "test.html";
            HashMap<String, Object> map = new HashMap<>();
            map.put("username", username);
            map.put("chapter", chapter);
            map.put("questionid", questionid);
            map.put("currentTime", currentTime);

            responseStr = JsonUtils.toWrapperJson(map);
        } else {
            responseStr = JsonUtils.toWrapperJson(ResponseCode.ExamError.RUN_EXEC_FAILD, result.message);
        }
        return responseStr;
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
                    .appendBinds(HostConfig.Bind.from(rootPath).to(rootPath).readOnly(true).build())
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
            final String[] command = {
                    "java", "-Dfile.encoding=UTF-8",
                    "-Djava.ext.dirs="+rootPath+"/exam/" + questionid + "/lib",
                    "-cp",".:"+rootPath+"/exam/" + questionid + ":" + binDir.getAbsolutePath(),
                    "TestMain", reportDir.getAbsolutePath()};
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
