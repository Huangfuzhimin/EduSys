package org.itheima.edu.tutorials.utils.cmd;

/**
 * Created by Poplar on 2017/6/19.
 */
public class Commander {
    public static JavaCmd java(String testMain) {
        return new JavaCmd(testMain);
    }

    public static KotlinCmd kotlin(String testMain) {
        return new KotlinCmd(testMain);
    }
}
