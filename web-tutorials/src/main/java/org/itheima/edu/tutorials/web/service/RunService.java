package org.itheima.edu.tutorials.web.service;

import org.springframework.scheduling.annotation.Async;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by Poplar on 2017/6/16.
 */
public interface RunService {

    String run(String username, String chapter, String questionid, String code) throws IOException;

    String asyncRun(String username, String chapter, String questionid, String code) throws IOException;

    @Async
    void async(String username, String chapter, String questionid, String code, long currentTime);
}
