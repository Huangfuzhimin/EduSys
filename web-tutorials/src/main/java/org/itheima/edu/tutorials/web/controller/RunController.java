package org.itheima.edu.tutorials.web.controller;

import org.itheima.edu.tutorials.utils.EncryptUtils;
import org.itheima.edu.tutorials.utils.JsonUtils;
import org.itheima.edu.tutorials.web.ResponseUtils;
import org.itheima.edu.tutorials.web.service.AsyncTest;
import org.itheima.edu.tutorials.web.service.RunService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Created by Poplar on 2017/6/6.
 */
@Controller
@RequestMapping(produces = "application/json;charset=UTF-8")
public class RunController {

    @Resource(name = "runServiceDockerImpl")    // docker编译
//    @Resource(name = "runServiceLocalImpl")   // 本地直接编译
    RunService runService;

    // 运行指定题目代码
    @RequestMapping(value = "/run")
    @ResponseBody
    public void run(HttpServletRequest req,
                    HttpServletResponse resp) {
        try {
//            String resStr = executeRequest(req, resp);
            resp.setContentType("text/html;charset=UTF-8");
            String type = req.getParameter("type");
            String username = req.getParameter("username");
            String chapter = req.getParameter("chapter");
            String questionid = req.getParameter("questionid");
            String code = req.getParameter("code");

//        return runService.asyncRun(username, chapter, questionid, code);

            // 生成时间
            long currentTime = System.currentTimeMillis();
            // 返回缓存key (SHA1数字摘要)
//            String cacheKey = EncryptUtils.SHA1(username + "_" + chapter + "_" + questionid + "_" + currentTime);
            String cacheKey = type + "_"+ username + "_" + chapter + "_" + questionid;
            System.out.println("asyncRun -> cacheKey: " + cacheKey);
            ResponseUtils.outputJson(resp, ResponseUtils.success(cacheKey));

            // 异步执行
            runService.async(type, username, chapter, questionid, code, currentTime);

        } catch (IOException e) {
            ResponseUtils.error(100, "运行错误", resp);

            e.printStackTrace();
        }

    }

    @Autowired
    AsyncTest asyncTest;

    @RequestMapping("async/run/{key}")
    public void runAsync(HttpServletRequest request, HttpServletResponse response, @PathVariable String key){
        System.out.println("asyncRun -> " + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()));
        asyncTest.asyncRun(key);
        System.out.println("asyncRun <- " + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()));

        try {
            response.getOutputStream().write("start!".getBytes("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("async/progress/{key}")
    @ResponseBody
    public String getProgress(HttpServletResponse resp, @PathVariable String key) throws Exception {
        String progress = asyncTest.getProgress(key);
//        ResponseUtils.outputJson(resp, JsonUtils.toWrapperJson(progress));
        return JsonUtils.toWrapperJson(0, progress);
    }

    @RequestMapping("async/clear/{key}")
    public void clearCache(HttpServletResponse resp, @PathVariable String key) throws Exception {
        asyncTest.clearCache(key);
    }

    private String executeRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        String username = request.getParameter("username");
        String chapter = request.getParameter("chapter");
        String questionid = request.getParameter("questionid");
        String code = request.getParameter("code");

//        return runService.asyncRun(username, chapter, questionid, code);

        // 生成时间
        long currentTime = System.currentTimeMillis();
        // 异步执行
//        runService.async(type, username, chapter, questionid, code, currentTime);

        // 返回缓存key (SHA1数字摘要)
        String cacheKey = EncryptUtils.SHA1(username + "_" + chapter + "_" + questionid + "_" + currentTime);
        System.out.println("asyncRun -> cacheKey: " + cacheKey);
        return ResponseUtils.success(cacheKey);
//        return runService.run(username, chapter, questionid, code);
    }

}
