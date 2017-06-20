package org.itheima.edu.tutorials.web;

import com.google.gson.Gson;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ResponseUtils {

    public static String error(int code) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", code);
        return toJson(map);
    }

    public static String error(int code, String error) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", code);
        map.put("data", error);
        return toJson(map);
    }
    public static void error(int code, String error, HttpServletResponse resp) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", code);
        map.put("data", error);
        String json = toJson(map);
        outputJson(resp, json);
    }

    public static String success() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", ResponseCode.Success.DEFAULT);
        return toJson(map);
    }

    public static String success(Object data) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", ResponseCode.Success.DEFAULT);
        map.put("data", data);
        return toJson(map);
    }

    public static String success(int code, Object data) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", code);
        map.put("data", data);
        return toJson(map);
    }

    private static String toJson(Object obj) {
        return new Gson().toJson(obj);
    }

    public static void outputJson(HttpServletResponse resp, String s) {
        try {
            resp.getOutputStream().write(s.getBytes("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
