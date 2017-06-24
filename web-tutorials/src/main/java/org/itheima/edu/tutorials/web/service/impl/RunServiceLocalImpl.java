package org.itheima.edu.tutorials.web.service.impl;

import org.itheima.edu.jcompiler.JCompilerUtils;
import org.itheima.edu.kcompiler.KotlinCompiler;
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

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Poplar on 2017/6/16.
 */
@Service
public class RunServiceLocalImpl implements RunService {

    Logger logger = LoggerFactory.getLogger(RunServiceLocalImpl.class);
    @Autowired
    RedisUtil redisUtil;

    @Value("${kotlin.home}")
    String kotlinHome;

    static Map<String, String> fileTypeMap = new HashMap<String, String>(){{
        put("java", "java");
        put("kotlin", "kt");
    }};

    @Override
    public String run(String type, String username, String chapter, String questionid, String code) throws IOException {
        return run(type, username, chapter, questionid, code, System.currentTimeMillis());
    }

    public String run(String type, String username, String chapter, String questionid, String code, long currentTime) throws IOException {

        // /root/newstrap/result/aaa/Array-1/lucky13/865757798789/src
        // /root/newstrap/result/aaa/Array-1/lucky13/865757798789/bin
        // /root/newstrap/result/aaa/Array-1/lucky13/865757798789/report
        PathUtil.RunDir runDir = PathUtil.newRunDir(username, chapter, questionid, currentTime);

        File srcDir = runDir.getChildDir("src");
        File binDir = runDir.getChildDir("bin");
        File reportDir = runDir.getChildDir("report");
        File questionDir = PathUtil.questionDir(chapter, questionid);
//        String cacheKey = EncryptUtils.SHA1(username + "_" + chapter + "_" + questionid + "_" + currentTime);
        String cacheKey = String.format("%s_%s_%s_%s", type, username, chapter, questionid);
        logger.info("cacheKey: " + cacheKey + " reportDir: " + reportDir.getAbsolutePath());

        // 1. 写出src到文件
        File srcFile = writeSrc(type, code, srcDir);

        // 2. 生成class到bin目录
        Object[] compileResult = compileSrc(type, srcFile, binDir);

        // 3. 创建测试命令
        String command = generateCommand(type, binDir, reportDir, questionDir);
//        System.out.println("command:" + command);

        // 4. 测试class, 把结果放到report目录  E:\cms\newstrap\result\vvv\minCat\1496717064235\report\test.html
        boolean compileSuccess = (Integer) compileResult[0] == 200;
        boolean testResult = false;
        if (compileSuccess) {
            testResult = runTest(command);
        }

        // 4. 得到测试结果, 返回并将成功信息写入缓存
        String responseStr;
        if (compileSuccess) { // 编译成功
            HashMap<String, Object> map = new HashMap<>();
            map.put("username", username);
            map.put("chapter", chapter);
            map.put("questionid", questionid);
            map.put("currentTime", currentTime);

            responseStr = JsonUtils.toWrapperJson(map);

            redisUtil.setCacheObject(cacheKey, ResponseUtils.success(0, reportDir.getAbsolutePath()));
            redisUtil.publish(cacheKey, ResponseUtils.success(0, reportDir.getAbsolutePath()));
        } else {
            String errorMsg = (String) compileResult[1];
            errorMsg = "编译出错: " + errorMsg;
            responseStr = JsonUtils.toWrapperJson(ResponseCode.ExamError.RUN_EXEC_FAILD, errorMsg);
            redisUtil.setCacheObject(cacheKey, ResponseUtils.error(ResponseCode.ExamError.RUN_EXEC_FAILD, errorMsg));
            redisUtil.publish(cacheKey, ResponseUtils.error(ResponseCode.ExamError.RUN_EXEC_FAILD, errorMsg));
        }
        System.out.println("写出完毕->" + cacheKey);

        return responseStr;
    }



    private String generateCommand(String type, File binDir, File reportDir, File questionDir) {
        if("kotlin".equals(type)){
            return Commander.kotlin("TestMain")
                    .args(new String[]{reportDir.getAbsolutePath()})   // 报表输出路径
                    .classpath(questionDir.getAbsolutePath())          // 题库根目录
                    .classpath(binDir.getAbsolutePath() + File.separator + "Itheima.jar")               // 编译结果目录
                    .extDir(questionDir.getAbsolutePath() + File.separator + "libs")   // TestNG依赖库目录
                    .extDir(PathUtil.rootDir() + "kotlin" + File.separator + "libs")   // Kotlin依赖库目录
                    .create();
        }

        return Commander.java("TestMain")
                    .args(new String[]{reportDir.getAbsolutePath()})   // 报表输出路径
                    .classpath(questionDir.getAbsolutePath())          // 题库根目录
                    .classpath(binDir.getAbsolutePath())               // 编译结果目录
                    .extDir(questionDir.getAbsolutePath() + File.separator + "libs")   // 依赖库目录
                    .create();
    }

    private boolean runTest(String command) throws IOException {
        System.out.println(command);
        Process p = Runtime.getRuntime().exec(command);
        try {
            String parseCmdStream = StreamUtils.parseCmdStream(p.getInputStream());
            System.out.println(parseCmdStream);
            p.waitFor();
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 编译源码
     * @param type
     * @param src
     * @param binDir
     * @return
     */
    @NotNull
    private Object[] compileSrc(String type, File src, File binDir) {
        Object[] result = new Object[2];
        if("kotlin".equals(type)){
            KotlinCompiler.setKotlinHome(kotlinHome);
            result = KotlinCompiler.compile(binDir.getAbsolutePath(), new String[]{src.getAbsolutePath()});
            if((Integer)result[0] == 0) result[0] = 200;
        } else {
            result = JCompilerUtils.doMagic(binDir.getAbsolutePath(), new String[]{src.getAbsolutePath()});
        }
        return result;
    }

    @NotNull
    private File writeSrc(String type, String code, File srcDir) throws IOException {

        String postFix = fileTypeMap.get(type);

        File ItheimaJava = new File(srcDir, "Itheima." + postFix);
        FileOutputStream fos = new FileOutputStream(ItheimaJava);
        fos.write(code.getBytes("utf-8"));
        StreamUtils.closeIO(fos);
        return ItheimaJava;
    }


    @Override
    public void async(String type, String username, String chapter, String questionid, String code, long currentTime) throws IOException {
        run(type, username, chapter, questionid, code, currentTime);
    }
}
