package org.itheima.edu.util;

import org.apache.commons.codec.Charsets;

import java.io.UnsupportedEncodingException;

/**
 * Created by Poplar on 2017/6/6.
 */
public class EncodingUtils {

    static String[] encodings = new String[]{
            Charsets.ISO_8859_1.toString(),
            Charsets.UTF_8.toString(),
            Charsets.US_ASCII.toString(),
            "gbk",
            "gb2312",
    };

    public static void printEncoding(String s) {
        try {
            int last = encodings.length - 1;
            for (int i = 0; i < last; i++) {
                String from = encodings[i];

                if(i + 1 <= last){
                    for(int k = i + 1; k <= last; k++){
                        String to = encodings[k];
                        System.out.println(from + " -> " + to + ": \t\t" + new String(s.getBytes(from), to));
                        System.out.println(to + " -> " + from + ": \t\t" + new String(s.getBytes(to), from));
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
