package org.itheima.edu.tutorials.web.controller;

import org.itheima.edu.tutorials.web.ResponseUtils;
import org.itheima.edu.tutorials.web.service.RunService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by Poplar on 2017/6/6.
 */
@Controller
@RequestMapping(produces = "application/json;charset=UTF-8")
public class RunController {

    @Resource(name = "runServiceLocalImpl")   // 本地直接编译
//    @Resource(name = "runServiceDockerImpl")    // docker编译
    RunService runService;

    // 运行指定题目代码
    @RequestMapping(value = "/run")
    @ResponseBody
    public void run(HttpServletRequest request,
                    HttpServletResponse response) {
        try {
            String resStr = executeRequest(request, response);
            response.getOutputStream().write(resStr.getBytes("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
            try {
                response.getOutputStream().write(ResponseUtils.error(100, "运行错误").getBytes("utf-8"));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }

    private String executeRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        String username = request.getParameter("username");
        String chapter = request.getParameter("chapter");
        String questionid = request.getParameter("questionid");
        String code = request.getParameter("code");

        return runService.run(request, username, chapter, questionid, code);
    }

}
