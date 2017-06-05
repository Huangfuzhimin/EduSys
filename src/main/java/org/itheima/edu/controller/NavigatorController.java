package org.itheima.edu.controller;

import org.itheima.edu.bean.ExamBean;
import org.itheima.edu.bean.ExamResult;
import org.itheima.edu.bean.HomeBean;
import org.itheima.edu.dao.UserEntityDao;
import org.itheima.edu.entity.UserEntity;
import org.itheima.edu.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.sqlite.date.DateFormatUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.DecimalFormat;
import java.util.*;


/**
 * Created by Poplar on 2016/12/27.
 */
@Controller
@RequestMapping(produces = "application/json;charset=UTF-8")
public class NavigatorController {

    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    // 远程数据源目录
    @Value("${config.dir.source}")
    String sourceFolder;	// /mnt/disk/source

    // 远程服务数据结果根目录
    @Value("${config.dir.result}")
    String resultFolder; 	// /mnt/disk/result

    // 本地json数据缓存根目录
    @Value("${config.dir.cache}")
    String cacheFolder;		// /bootstrap/cache

    @Autowired
    UserEntityDao userDao;

    String[] bgStyles = new String[]{"bg-maroon", "bg-aqua", "bg-orange"};
    String[] types = new String[]{"lesson", "exercise"};
    public static DecimalFormat decimalFormat=new DecimalFormat("0.00");

    String itemHtml =
            "<div id='lesson' class='col-md-3 col-sm-6 col-xs-12' style='cursor: pointer;'>" +
                "<div class='info-box %3$s'>" +
                    "<span class='info-box-icon'><i><b id='index'>%1$s</b></i></span>" +
                    "<div class='info-box-content'>" +
                        "<span class='info-box-number-bg' id='name'>%2$s</span>" +
                        "<div class='progress'>" +
                            "<div class='progress-bar' style='width: %4$s%%'></div>" +
                        "</div>" +
                        "<span class='progress-description'>进度已完成%5$s%%，请继续加油！</span>" +
                    "</div>" +
                "</div>" +
            "</div>";

    /**
     * 获取首页的html
     * @return
     */
    @RequestMapping("/home")
    @ResponseBody
    public String home(HttpServletRequest request/*@RequestParam(value = "id",  required = false) int id*/){

        // 动态解析文件夹并获取内容
        // 需要将cms/source目录拷贝至E盘下确保E:/cms/source文件夹里有内容
        // 子文件夹中包含config.xml文件的目录, 才会被加入到列表对象

        try {

            String token = request.getHeader("token");
            UserEntity entry = userDao.findByToken(token);
            if(entry == null){
                return ResponseUtils.error(ResponseCode.LoginError.LOGIN_NEEDED, "请重新登录");
            }

            // 初始化版本信息
            HomeBean homeBean = new HomeBean(1.0f);

            // 读取数据
            List<ExamBean> exams = SourceReader.readSource(new File(sourceFolder));

            StringBuffer sb = new StringBuffer();

            long lastModified = 0;

            // 组织成字符串
            for (int i = 0; i < exams.size(); i++) {
                ExamBean exam = exams.get(i);

                // 生成序号
                int order = i + 1;
                String index =  (order < 10 ? "0" : "") + order;
                // 章节名称
                String chapterName = exam.getExamName();

                // 读取每个课程模块的进度 (注意缓存)
                ExamResult result = getChapterResult(resultFolder, entry, chapterName);

                float completePercent = 0f;
                if(result != null){
                    lastModified = Math.max(lastModified, result.getLastModified()); // 最后修改时间
                    if(result.getTotalExamCount() > 0){
                        completePercent = result.getTotalScore() * 1.0f / result.getTotalExamCount();
                    }
                }
                String progress = decimalFormat.format(completePercent);

                System.out.println(String.format("读取到[%s]的进度(%s)", chapterName, progress));

                sb.append(String.format(itemHtml, index, chapterName, bgStyles[i % 3], progress, progress));
            }
            homeBean.setContent(sb.toString());
            homeBean.setLastModified(lastModified);
            System.out.println("最后修改时间: " + DateFormatUtils.format(lastModified, DATE_PATTERN));

            //  设置进度
//            List<Integer> progresses = Arrays.asList(80, 65, 44);
//            homeBean.setProgress(progresses);

            return JsonUtils.toWrapperJson(homeBean);
        } catch (Exception e) {
            e.printStackTrace();
        }


//        String json = readFromJsonFile();
//        if (json != null) return json;

        return "error";
    }

    private ExamResult getChapterResult(String dataPath, UserEntity entry, String chapterName) {

        // 获得用户根目录 (eg. /result/aaa)
        File userRoot = new File(dataPath, entry.getAccount());
        if (userRoot.exists()) {
            HashMap<String, ExamResult> typeResults = new HashMap<>();
            for (int i = 0; i < types.length; i++) {
                String type = types[i];
                ExamResult result = ResultUtils.getResult(dataPath, cacheFolder, entry, chapterName, type);
                typeResults.put(type, result);
            }
            ExamResult result = mergeResults(typeResults);
            return result;

        }
        return null;
    }

    private static ExamResult mergeResults(HashMap<String, ExamResult> typeResults) {
        Collection<ExamResult> values = typeResults.values();
        Iterator<ExamResult> it = values.iterator();
        ExamResult result = null;
        while(it.hasNext()){
            ExamResult next = it.next();
            if(result == null){
                result = next;
                continue;
            }else {
                result.merge(next);
            }
        }
        return result;

    }


//    private String readFromJsonFile() {
//        File file = new File(dataPath);
//        System.out.println("file: " + file.getAbsolutePath());
//        try {
//            String json = StreamUtils.copyToString(new FileInputStream(dataPath + "/home.json"), Charset.forName("UTF-8"));
//            return json;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

}
