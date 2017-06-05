package org.itheima.edu.util;

import java.io.*;

/**
 * Created by Poplar on 2016/12/29.
 */
public class FileUtils {

    public static boolean writeFile(File file, String content) {
        FileWriter writer = null;
        try {
            File parentFile = file.getParentFile();
            parentFile.mkdirs();

            writer = new FileWriter(file);
            writer.write(content);
            writer.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if(writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static String readFileToString(File file) {
        if(file == null || !file.exists()){
            return null;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        FileInputStream fos = null;
        try {
            fos = new FileInputStream(file);
            int len;
            byte[] buffer = new byte[1024 * 8];
            while((len = fos.read(buffer)) != -1){
                baos.write(buffer, 0, len);
            }
            return baos.toString("utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;

    }

    public static String readFile(File file) {
        if(file == null || !file.exists()){
            return null;
        }
        // 会以默认编码 (GBK?) 从系统文件读取数据.
        FileReader fileReader = null;
        BufferedReader br = null;
        try {
            fileReader = new FileReader(file);
            br = new BufferedReader(fileReader);
            StringBuffer sb = new StringBuffer();
            String line;
            while((line = br.readLine()) != null){
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
