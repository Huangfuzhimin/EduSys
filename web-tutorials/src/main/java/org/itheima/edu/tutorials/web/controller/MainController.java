package org.itheima.edu.tutorials.web.controller;

import org.itheima.edu.tutorials.bean.ChapterDTO;
import org.itheima.edu.tutorials.bean.QuestionDTO;
import org.itheima.edu.tutorials.utils.JsonUtils;
import org.itheima.edu.tutorials.web.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


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
        List<QuestionDTO> dtos = mainService.getQuestionsByChapter(chapter);
        return JsonUtils.toWrapperJson(dtos);
    }


    /**
     * 获取问题的描述
     * @param request
     * @param response
     */
    @RequestMapping(value = "/desc")
    public void getQuestionDesc(HttpServletRequest request, HttpServletResponse response) {
//        LogUtils.printRequest(request);
        String username = request.getParameter("username");
        String chapter = request.getParameter("chapter");
        String questionid = request.getParameter("questionid");

        String cacheKey = String.format("%s_%s_%s_%s", username, chapter, questionid, UUID.randomUUID());

        String result = mainService.getQuestionDesc(chapter, questionid);
        try {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("result", result);
            map.put("cacheKey", cacheKey);
            String json = JsonUtils.toJson(map);

            response.getOutputStream().write(json.getBytes("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/question/tree")
    @ResponseBody
    public String getTree(String chapter, String questionid) {
        return mainService.getQuestionTree(chapter,questionid);
    }

    @RequestMapping("/index")
    public String index() {

        return "index";
    }

    @RequestMapping("/chapter/list")
    public String getChapterList(String name, String type) {

        return "chapter/list";
    }

    @RequestMapping("/chapter/question")
    public String getQuestionDetail(String chapter, String title, String name) {

        return "chapter/question";
    }


}
