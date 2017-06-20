package org.itheima.edu.tutorials.web.service.impl;

import org.itheima.edu.jcompiler.JCompilerUtils;
import org.itheima.edu.tutorials.utils.*;
import org.itheima.edu.tutorials.utils.cmd.Commander;
import org.itheima.edu.tutorials.web.ResponseCode;
import org.itheima.edu.tutorials.web.ResponseUtils;
import org.itheima.edu.tutorials.web.service.RunService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Poplar on 2017/6/16.
 */
@Service
public class RunServiceLocalImpl implements RunService {

    Logger logger = LoggerFactory.getLogger(RunServiceLocalImpl.class);
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
        String cacheKey = EncryptUtils.SHA1(username + "_" + chapter + "_" + questionid + "_" + currentTime);
        logger.info("cacheKey: " + cacheKey + " reportDir: " + reportDir.getAbsolutePath());

        // 1. 写出src到文件
        File ItheimaJava = writeSrc(code, srcDir);

        // 2. 生成class到bin目录
        JCompilerUtils.Result result = compileSrc(ItheimaJava, binDir);

        String command = Commander.java("TestMain")
                .args(new String[]{reportDir.getAbsolutePath()})   // 报表输出路径
                .classpath(questionDir.getAbsolutePath())          // 题库根目录
                .classpath(binDir.getAbsolutePath())               // 编译结果目录
                .extDir(questionDir.getAbsolutePath() + File.separator + "libs")   // 依赖库目录
                .create();

        // 3. 测试class, 把结果放到report目录  E:\cms\newstrap\result\vvv\minCat\1496717064235\report\test.html
        boolean isSuccess;
        if (result.code == 200) {
            isSuccess = runTest(command);
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
        } else {
            responseStr = JsonUtils.toWrapperJson(ResponseCode.ExamError.RUN_EXEC_FAILD, result.message);

            redisUtil.setCacheObject(cacheKey, ResponseUtils.error(ResponseCode.ExamError.RUN_EXEC_FAILD));
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
    public void async(String username, String chapter, String questionid, String code, long currentTime) {
        try {
            run(username, chapter, questionid, code, currentTime);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
