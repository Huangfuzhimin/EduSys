package org.itheima.edu.controller;

import org.itheima.edu.bean.QuestionDTO;
import org.itheima.edu.util.FileUtils;
import org.itheima.edu.util.JsonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Poplar on 2017/6/5.
 */
@Controller
@RequestMapping(produces = "application/json;charset=UTF-8")
public class MainController {


    // 数据源目录
    @Value("${config.dir.source}")
    String sourceFolder;

    /**
     * 获取章节目录列表
     *
     * @param request
     * @param type    0:java 1:kotlin 2:python
     * @return
     */
    // 获取章节目录
    @RequestMapping("/chapter")
    @ResponseBody
    public String chapters(HttpServletRequest request,
                           @RequestParam(required = false, defaultValue = "0") int type) {

        File folder = new File(sourceFolder);
        System.out.println("chapters--> " + folder.getAbsolutePath());

        List<String> fileList = new ArrayList<>();
        if (folder.exists() && folder.isDirectory()) {

            File[] files = folder.listFiles();
            for (File file : files) {
                fileList.add(file.getName());
            }
        }

        return JsonUtils.toWrapperJson(0, fileList);
    }

    /**
     * 获取指定章节所有题目
     *
     * @param request
     * @param chapter 章节名称
     * @return
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public String list(HttpServletRequest request, String chapter) {
//        logic-1\blueTicket\build\question.description
//        logic-1\blueTicket\build\question.title
        List<QuestionDTO> dtos = new ArrayList<>();
        try {
            request.setCharacterEncoding("UTF-8");
            chapter = new String(chapter.getBytes("iso-8859-1"), "utf-8");

            dtos = readChapter(chapter);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String s = JsonUtils.toWrapperJson(dtos);
//        printEncoding(s);
        return s;
    }


    public List<QuestionDTO> readChapter(String chapter) throws UnsupportedEncodingException {
        List<QuestionDTO> dtos = new ArrayList<>();

        File folder = new File(sourceFolder, chapter);
        System.out.println("list--> " + folder.getAbsolutePath());
        if (folder.exists() && folder.isDirectory()) {
            String chapterName = folder.getName();

            File[] files = folder.listFiles();
            for (File file : files) {
                File descFile = new File(file, "build" + File.separator + "question.description");
                File titleFile = new File(file, "build" + File.separator + "question.title");
                String title = FileUtils.readFileToString(titleFile);
                String desc = FileUtils.readFileToString(descFile);
                dtos.add(new QuestionDTO(chapterName, title, desc));

            }
        }
        return dtos;
    }


}
