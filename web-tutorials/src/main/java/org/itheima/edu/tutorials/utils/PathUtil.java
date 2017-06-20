package org.itheima.edu.tutorials.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Created by Poplar on 2017/6/19.
 */
@Component
public class PathUtil {

    // 根目录
    public static String rootDir;

    // 题库目录
    public static String sourceDir;

    @Value("${config.dir.root}")
    public void setRootDir(String rootDir){
        this.rootDir = rootDir;
    }

    @Value("${config.dir.source}")
    public void setSourceDir(String sourceDir) {
        this.sourceDir = sourceDir;
    }


    /**
     * 题库根目录
     * @return /root/newstrap/exam/
     */
    public static String sourceDir() {
        return sourceDir;
    }
    public static String rootDir() {
        return rootDir;
    }

    /**
     * 章节根目录
     * @param chapter 章节id
     * @return /root/newstrap/exam/Array-2
     */
    public static File chapterDir(String chapter) {
        return new File(sourceDir(),chapter);
    }

    /**
     * 题目根目录
     * @param chapter   章节id
     * @param questionid    问题id
     * @return  E:\cms\newstrap\exam\Array-2\lucky13
     */
    public static File questionDir(String chapter, String questionid) {
        return new File(sourceDir(), chapter + File.separator + questionid);
    }

    public static RunDir newRunDir(String username, String chapter, String questionid, long currentTime) {

//        String tempRoot = "result/" + username + "/" + chapter + "/" + questionid + "/" + currentTime + "/";
        String result = new StringBuilder(rootDir())
                .append(File.separator).append("result")
                .append(File.separator).append(username)
                .append(File.separator).append(chapter)
                .append(File.separator).append(questionid)
                .append(File.separator).append(currentTime)
                .toString();

        return new RunDir(new File(result));
    }

    public static class RunDir {
        File runRoot;
        private File src;

        public RunDir(File runRoot) {
            this.runRoot = runRoot;
        }

        public File getSrcDir() {
            return getChildDir("src");
        }

        public File getChildDir(String dirName){
            File dir = new File(runRoot, dirName);
            if(!dir.exists()){
                dir.mkdirs();
            }
            return dir;
        }
    }
}
