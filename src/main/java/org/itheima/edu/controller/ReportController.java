package org.itheima.edu.controller;

import org.itheima.edu.util.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * Created by Poplar on 2017/6/6.
 */
@Controller
public class ReportController {

    @Value("${config.dir.root}")
    String rootPath;

    @RequestMapping(value = "/report", produces = "text/html;charset=UTF-8")
    public void getReport(HttpServletRequest request, HttpServletResponse response,
        String username,
        String chapter,
        String questionid,
        String time
        ) {
        System.out.println("getReport: " + username + " c: " +chapter+ " q: " + questionid + " time: " + time);


        File reportDir = new File(rootPath,
                "result/" + username +"/" +chapter + "/" + questionid + "/" + time + "/" + "report");
        if (questionid != null) {
            File descFile = new File(reportDir, "test.html");
            System.out.println("descFile: " + descFile.getAbsolutePath());
            String s = FileUtils.readFileToString(descFile);

            try {
                response.getOutputStream().write(s.getBytes("utf-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
