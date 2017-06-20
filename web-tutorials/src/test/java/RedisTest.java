import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.itheima.edu.tutorials.utils.JsonUtils;
import org.itheima.edu.tutorials.utils.RedisUtil;
import org.itheima.edu.tutorials.web.service.AsyncTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Poplar on 2017/6/17.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:META-INF/spring/servletContext.xml"})
public class RedisTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    AsyncTest asyncTest;

    @Autowired
    RedisUtil redisUtil;

    @Test
    public void startAsync(){
        String key = "testKey!";
        asyncTest.asyncRun(key);

        try {
            String progress = null;
            while(true){
                progress = asyncTest.getProgress(key);
                System.out.println("progress: " + progress);
                if(!StringUtils.isEmpty(progress)){
                    String[] split = progress.split("/");
                    if(StringUtils.equals(split[0], split[1])){
                        asyncTest.clearCache(key);
                        System.out.println("Complete!");
                        break;
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRadis(){
        redisUtil.setCacheObject("cacheKey", 123);

        Object cacheKey = redisUtil.getCacheObject("cacheKey");
        System.out.println(cacheKey);

//        redisUtil.delete("cacheKey");
//
//        cacheKey = redisUtil.getCacheObject("cacheKey");
//        System.out.println(cacheKey);

    }

}
