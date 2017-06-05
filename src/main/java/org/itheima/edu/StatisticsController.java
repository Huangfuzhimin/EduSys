package org.itheima.edu;

import org.itheima.edu.bean.ExamBean;
import org.itheima.edu.util.SourceReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.util.List;

/**
 * Created by Poplar on 2016/12/19.
 * 需求:
 *      编译关键数据存储

        E:. 每个试题的目录如下
         │  plugin.data 插件配置
         │  submit.data 最后一次提交信息, 和storage\run_info中最新一次信息相同
         │
         ├─app  试题内容    (可复用)
         │  │  manifest.xml
         │  ├─classes   题目插件字节码
         │  ├─META-INF  元信息
         │  └─res   题目描述
         │
         ├─compile  提交文件    (只保留java)
         │  ├─classes
         │  └─src
         │         Itheima.java
         │
         ├─extra
         └─storage
             └─run_info 运行信息 保存每次编译的源码/结果/分数/时间/测试项分数
                 1480844580811
                 1480847758084

 *      数据报表定时生成
 *
 *
 *      数据报表展示
 *
 * 编译数据:
 *
 *
 */
@Controller
@RequestMapping("/data")
public class StatisticsController {

    @Value("${config.dir.source}")
    String sourcePath;

    @RequestMapping("/")
    @ResponseBody
    public String test() {
        return "hi test: ";
    }

    @RequestMapping("/home")
    public String home(Model model) {

        // 读取数据
        List<ExamBean> exams = SourceReader.readSource(new File(sourcePath));
        System.out.println("exams: " + exams.size());

        // 设置数据
        model.addAttribute("exams", exams);
        model.addAttribute("source", sourcePath);

        // 展示数据
        return "data/home";
    }
}
