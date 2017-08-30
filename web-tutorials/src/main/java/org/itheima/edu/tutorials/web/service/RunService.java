package org.itheima.edu.tutorials.web.service;

import org.springframework.scheduling.annotation.Async;

import java.io.IOException;

/**
 * Created by Poplar on 2017/6/16.
 */
public interface RunService {

    String run(String type, String username, String chapter, String questionid, String code,String cacheKey) throws IOException;

    @Async
    void async(String type, String username, String chapter, String questionid, String code,String cacheKey, long currentTime) throws IOException;
}
