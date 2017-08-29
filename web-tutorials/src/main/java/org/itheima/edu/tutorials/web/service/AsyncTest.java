package org.itheima.edu.tutorials.web.service;

import org.itheima.edu.tutorials.utils.RedisUtil;
import org.itheima.edu.tutorials.utils.SerializeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;

/**
 * Created by Poplar on 2017/6/16.
 */
@Service
public class AsyncTest {

    static private Logger logger = LoggerFactory.getLogger(SerializeUtil.class);

    @Autowired
    RedisUtil redisUtil;

    @Async
    public void asyncRun(String key) {

        int maxStep = 20;
        for (int i = 0; i < maxStep; i++) {
            try {
                Thread.sleep(500);

                System.out.println("异步放置缓存");
                //将执行进度放入缓存
                redisUtil.setCacheObject(key, (i + 1) + "/" + maxStep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        String res = "异步完成!" + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis());
        logger.info(res);
    }

    public String getProgress(String cacheKey) throws Exception {
        return (String) redisUtil.getCacheObject(cacheKey);
    }
    public void clearCache(String cacheKey) throws Exception {
      //完成后，清空缓存
        redisUtil.delete(cacheKey);
        logger.info("缓存清理完毕:" + cacheKey);
    }
}
