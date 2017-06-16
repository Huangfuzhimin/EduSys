package org.itheima.edu.tutorials.web.controller;

import org.itheima.edu.tutorials.bean.ChapterDTO;
import org.itheima.edu.tutorials.bean.QuestionDTO;
import org.itheima.edu.tutorials.utils.FileUtils;
import org.itheima.edu.tutorials.utils.JsonUtils;
import org.itheima.edu.tutorials.web.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;


/**
 * Created by Poplar on 2017/6/5.
 */
@Controller
@RequestMapping(produces = "application/json;charset=UTF-8")
public class MainController {


    @Autowired
    MainService mainService;

    /**
     * 获取章节目录列表
     *
     * @param request
     * @param type    0:java 1:kotlin 2:python
     * @return
     */
    // 获取章节目录
    @RequestMapping("/chapters")
    @ResponseBody
    public String chapters(HttpServletRequest request,
                           @RequestParam(required = false, defaultValue = "0") int type) {

        List<ChapterDTO> dtoList = mainService.getChaptersByType(type);

        return JsonUtils.toWrapperJson(0, dtoList);
    }

    /**
     * 获取指定章节所有题目
     *
     * @param request
     * @param chapter 章节名称
     * @return
     */
    @RequestMapping(value = "/chapter")
    @ResponseBody
    public String list(HttpServletRequest request, String chapter) {

        try {
            request.setCharacterEncoding("UTF-8");
            chapter = new String(chapter.getBytes("iso-8859-1"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        List<QuestionDTO> dtos = mainService.getQuestionsByChapter(chapter);

        return JsonUtils.toWrapperJson(dtos);
    }



    @RequestMapping(value = "/desc")
    public void getQuestionDesc(HttpServletRequest request, HttpServletResponse response) {
//        LogUtils.printRequest(request);
        String chapter = request.getParameter("chapter");
        String questionid = request.getParameter("questionid");
        try {
            chapter = new String(chapter.getBytes("iso-8859-1"), "utf-8");
            questionid = new String(questionid.getBytes("iso-8859-1"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = mainService.getQuestionDesc(chapter, questionid);

        try {
            response.getOutputStream().write(result.getBytes("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
