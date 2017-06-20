package org.itheima.edu.tutorials.web.service.impl;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.LogStream;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.*;
import org.apache.commons.lang.StringUtils;
import org.itheima.edu.jcompiler.JCompilerUtils;
import org.itheima.edu.tutorials.utils.JsonUtils;
import org.itheima.edu.tutorials.utils.PathUtil;
import org.itheima.edu.tutorials.utils.RedisUtil;
import org.itheima.edu.tutorials.utils.StreamUtils;
import org.itheima.edu.tutorials.utils.cmd.Commander;
import org.itheima.edu.tutorials.web.ResponseCode;
import org.itheima.edu.tutorials.web.ResponseUtils;
import org.itheima.edu.tutorials.web.service.RunService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Value("${config.dir.root}")
    String rootPath;

    @Value("${docker.uri}")
    String docker_uri;

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
        try {
            docker = DefaultDockerClient.fromEnv().uri(docker_uri).build();
        } catch (DockerCertificateException e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("I'm  destory method  using  @PreDestroy.....");
    }

    public boolean runDocker(String[] command, String reportPath) {
        String id = null;
        try {

            final HostConfig hostConfig = HostConfig.builder()
                    .appendBinds(HostConfig.Bind.from(rootPath).to(rootPath).readOnly(true).build())
                    .appendBinds(HostConfig.Bind.from(reportPath).to(reportPath).readOnly(false).build())
                    .build();
            // Create container with exposed ports
            final ContainerConfig containerConfig = ContainerConfig.builder().hostConfig(hostConfig)
                    .image("hub.c.163.com/library/java:8").tty(true).attachStderr(true).attachStdin(true)
                    .attachStdout(true).build();

            final ContainerCreation creation = docker.createContainer(containerConfig);
            id = creation.id();

            // Inspect container
            final ContainerInfo info = docker.inspectContainer(id);
            System.out.println("ContainerInfo: " + info);

            // Start container
            docker.startContainer(id);

            final ExecCreation execCreation = docker.execCreate(id, command,
                    DockerClient.ExecCreateParam.attachStdout(), DockerClient.ExecCreateParam.attachStderr());
            final LogStream output = docker.execStart(execCreation.id());
            System.out.println("ready go ");
            closeContainerDelay(id);

            final String execOutput = output.readFully();
            System.out.println("execOutput: " + execOutput);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if(!StringUtils.isEmpty(id)){
                try {
                    // Kill container
                    docker.killContainer(id);
                    System.out.println("killed :" + id);
                    // Remove container
                    docker.removeContainer(id, true);
                    // Close the docker client
                    //docker.close();
                } catch (DockerException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void closeContainerDelay(String id) {
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
        }, 15 * 1000);
    }



    Logger logger = LoggerFactory.getLogger(RunServiceDockerImpl.class);
    @Autowired
    RedisUtil redisUtil;


    @Override
    public String run(String username, String chapter, String questionid, String code) throws IOException {
        return run(username, chapter, questionid, code, System.currentTimeMillis());
    }

    @Override
    public String asyncRun(String username, String chapter, String questionid, String code) throws IOException {
        return null;
    }

    public String run(String username, String chapter, String questionid, String code, long currentTime) throws IOException {

        // /root/newstrap/result/aaa/Array-1/lucky13/865757798789/src
        // /root/newstrap/result/aaa/Array-1/lucky13/865757798789/bin
        // /root/newstrap/result/aaa/Array-1/lucky13/865757798789/report
        PathUtil.RunDir runDir = PathUtil.newRunDir(username, chapter, questionid, currentTime);

        File srcDir = runDir.getChildDir("src");
        File binDir = runDir.getChildDir("bin");
        File reportDir = runDir.getChildDir("report");
        File questionDir = PathUtil.questionDir(chapter, questionid);
//        String cacheKey = EncryptUtils.SHA1(username + "_" + chapter + "_" + questionid + "_" + currentTime);
        String cacheKey = String.format("%s_%s_%s", username, chapter, questionid);
        logger.info("cacheKey: " + cacheKey + " reportDir: " + reportDir.getAbsolutePath());

        // 1. 写出src到文件
        File ItheimaJava = writeSrc(code, srcDir);

        // 2. 生成class到bin目录
        JCompilerUtils.Result result = compileSrc(ItheimaJava, binDir);

        String[] command = Commander.java("TestMain")
                .args(new String[]{reportDir.getAbsolutePath()})   // 报表输出路径
                .classpath(questionDir.getAbsolutePath())          // 题库根目录
                .classpath(binDir.getAbsolutePath())               // 编译结果目录
                .extDir(questionDir.getAbsolutePath() + File.separator + "libs")   // 依赖库目录
                .separator(":")
                .createArr();

        String finalCommand = StringUtils.join(command, " ");
        logger.info("finalCommand: " + finalCommand);

        // 3. 测试class, 把结果放到report目录  E:\cms\newstrap\result\vvv\minCat\1496717064235\report\test.html
        boolean isSuccess;
        if (result.code == 200) {
            isSuccess = runDocker(command, reportDir.getAbsolutePath());
//            isSuccess = runTest(command);
        } else {
            isSuccess = false;
        }


        // 4. 得到测试结果, 返回并将成功信息写入缓存
        String responseStr;
        if (isSuccess) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("username", username);
            map.put("chapter", chapter);
            map.put("questionid", questionid);
            map.put("currentTime", currentTime);

            responseStr = JsonUtils.toWrapperJson(map);

            redisUtil.setCacheObject(cacheKey, ResponseUtils.success(0, reportDir.getAbsolutePath()));
            redisUtil.publish(cacheKey, ResponseUtils.success(0, reportDir.getAbsolutePath()));
        } else {
            responseStr = JsonUtils.toWrapperJson(ResponseCode.ExamError.RUN_EXEC_FAILD, result.message);

            redisUtil.setCacheObject(cacheKey, ResponseUtils.error(ResponseCode.ExamError.RUN_EXEC_FAILD, result.message));
            redisUtil.publish(cacheKey, ResponseUtils.error(ResponseCode.ExamError.RUN_EXEC_FAILD, result.message));
        }
        System.out.println("写出完毕->" + cacheKey);

        return responseStr;
    }

    private boolean runTest(String command) throws IOException {
        System.out.println(command);
        Process p = Runtime.getRuntime().exec(command);
        try {
            String parseCmdStream = StreamUtils.parseCmdStream(p.getInputStream());
//            System.out.println(parseCmdStream);
            p.waitFor();
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    @NotNull
    private JCompilerUtils.Result compileSrc(File itheimaJava, File binDir) {
        return JCompilerUtils.doMagic(binDir.getAbsolutePath(), new String[]{itheimaJava.getAbsolutePath()});
    }

    @NotNull
    private File writeSrc(String code, File srcDir) throws IOException {
        File ItheimaJava = new File(srcDir, "Itheima.java");
        FileOutputStream fos = new FileOutputStream(ItheimaJava);
        fos.write(code.getBytes("utf-8"));
        StreamUtils.closeIO(fos);
        return ItheimaJava;
    }

//    @Override
//    public String asyncRun(String username, String chapter, String questionid, String code) throws IOException {
//        // 生成时间
//        long currentTime = System.currentTimeMillis();
//        // 异步执行
//        async(username, chapter, questionid, code, currentTime);
//
//        // 返回缓存key (SHA1数字摘要)
//        String cacheKey = EncryptUtils.SHA1(username + "_" + chapter + "_" + questionid + "_" + currentTime);
//        System.out.println("asyncRun -> cacheKey: " + cacheKey);
//        return ResponseUtils.success(cacheKey);
//    }

    @Override
    public void async(String username, String chapter, String questionid, String code, long currentTime) throws IOException {
        run(username, chapter, questionid, code, currentTime);
    }
}
