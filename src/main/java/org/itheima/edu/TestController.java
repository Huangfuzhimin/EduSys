package org.itheima.edu;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * Html
 *
 * Java基础
 *
 */
@Controller
public class TestController {

    @Value("${config.dir.source}")
    String sourcePath;

    @Value("${config.dir.result}")
    String resultPath;

    @RequestMapping("/test")
    @ResponseBody
    public String test() {
        return "hi test: " + sourcePath;
    }

    @RequestMapping("/")
    @ResponseBody
    public String index(){
        return "abc";
    }

    @RequestMapping("/xxx")
    public String jsp(){
        return "a/b";
    }

}
