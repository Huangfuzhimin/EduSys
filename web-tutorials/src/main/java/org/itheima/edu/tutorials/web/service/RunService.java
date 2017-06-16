package org.itheima.edu.tutorials.web.service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by Poplar on 2017/6/16.
 */
public interface RunService {

    String run(HttpServletRequest request, String username, String chapter, String questionid, String code) throws IOException;

}
