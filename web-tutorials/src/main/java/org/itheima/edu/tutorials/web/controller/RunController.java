package org.itheima.edu.tutorials.web.controller;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.LogStream;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.messages.*;
import org.itheima.edu.jcompiler.JCompilerUtils;
import org.itheima.edu.tutorials.utils.JsonUtils;
import org.itheima.edu.tutorials.utils.StreamUtils;
import org.itheima.edu.tutorials.web.ResponseCode;
import org.itheima.edu.tutorials.web.ResponseUtils;
import org.itheima.edu.tutorials.web.service.RunService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
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

    @Resource(name = "runServiceLocalImpl")
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
        chapter = new String(chapter.getBytes("iso8859-1"), "utf-8");

        return runService.run(request, username, chapter, questionid, code);
    }



}
