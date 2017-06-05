package org.itheima.edu.controller;

import org.itheima.edu.dao.ClassEntityDao;
import org.itheima.edu.entity.ClassEntity;
import org.itheima.edu.util.ResponseCode;
import org.itheima.edu.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Poplar on 2016/12/29.
 */
@Controller
@RequestMapping("/class")
public class ClassController {

    @Autowired
    ClassEntityDao classDao;

    @RequestMapping("/create")
    @ResponseBody
    public String create(HttpServletRequest request, String className) {

        ClassEntity entity = new ClassEntity();
        entity.setComplete(false);
        entity.setName(className);
        classDao.add(entity);

        String format = String.format("班级[%s]创建成功", className);
        return ResponseUtils.success(ResponseCode.Success.DEFAULT, format);
    }

}
