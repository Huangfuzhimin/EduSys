package org.itheima.edu.tutorials.web.service.impl;

import org.itheima.edu.jcompiler.JCompilerUtils;
import org.itheima.edu.tutorials.utils.JsonUtils;
import org.itheima.edu.tutorials.utils.StreamUtils;
import org.itheima.edu.tutorials.web.ResponseCode;
import org.itheima.edu.tutorials.web.service.RunService;
import org.springframework.beans.factory.annotation.Value;
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
public class RunServiceLocalImpl implements RunService{

    // 数据源目录
    @Value("${config.dir.source}")
    String sourceFolder;
    //	private static final String rootPath = "/root/newstrap";
    @Value("${config.dir.root}")
    String rootPath;

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
                Process p = Runtime.getRuntime().exec(command);
                try {
                    String parseCmdStream = StreamUtils.parseCmdStream(p.getInputStream());
//                System.out.println(parseCmdStream);
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

                responseStr = JsonUtils.toWrapperJson(map);
            } else {
                responseStr = JsonUtils.toWrapperJson(ResponseCode.ExamError.RUN_EXEC_FAILD, result.message);
            }
            return responseStr;
    }
}
