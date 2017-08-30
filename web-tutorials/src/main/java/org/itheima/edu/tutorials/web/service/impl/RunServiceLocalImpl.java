package org.itheima.edu.tutorials.web.service.impl;

import org.itheima.edu.tutorials.utils.StreamUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by Poplar on 2017/6/16.
 */
@Service
public class RunServiceLocalImpl extends BaseRunServiceImpl {


    @Override
    public boolean runTest(String command, String reportpath) {

        try {
            Process p = Runtime.getRuntime().exec(command);
            String parseCmdStream = StreamUtils.parseCmdStream(p.getInputStream());
            System.out.println(parseCmdStream);
            p.waitFor();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

}
