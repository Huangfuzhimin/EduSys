package org.itheima.edu;

import org.itheima.edu.bean.QuestionDTO;
import org.itheima.edu.util.FileUtils;
import org.itheima.edu.util.JsonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Poplar on 2017/6/5.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/spring/servlet-context.xml" })
public class JunitTest extends AbstractJUnit4SpringContextTests {

    // 数据源目录
    @Value("${config.dir.source}")
    String sourceFolder;

    @Test
    public void testRead(){
        try {
            System.out.println(JsonUtils.toWrapperJson(readChapter("biggerTwo")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    public List<QuestionDTO> readChapter(String chapter) throws UnsupportedEncodingException {
        List<QuestionDTO> dtos = new ArrayList<>();

        File folder = new File(sourceFolder, chapter);
        System.out.println("list--> " + folder.getAbsolutePath());
        if (folder.exists() && folder.isDirectory()) {
            String chapterName = folder.getName();
            File descFile = new File(folder, "build" + File.separator + "question.description");
            File titleFile = new File(folder, "build" + File.separator + "question.title");
            String title = FileUtils.readFile(titleFile);
            String desc = FileUtils.readFile(descFile);
            dtos.add(new QuestionDTO(chapterName, folder.getName(), title, desc));
        }
        return dtos;
    }

}
