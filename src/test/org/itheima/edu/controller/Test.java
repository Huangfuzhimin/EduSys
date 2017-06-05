package org.itheima.edu.controller;

import org.itheima.edu.encrypt.SaltEncoder;

/**
 * Created by Poplar on 2016/12/27.
 */
public class Test {

    static String itemHtml =
            "<div id='lesson' class='col-md-3 col-sm-6 col-xs-12' style='cursor: pointer;'>" +
                    "<div class='info-box bg-aqua'>" +
                    "<span class='info-box-icon'><i class='fa'><b>%1$s</b></i></span>" +
                    "<div class='info-box-content'>" +
                    "<span class='info-box-number-bg'>%2$s</span>" +
                    "<div class='progress'>" + "<div class='progress-bar' style='width: 70%%'></div></div>" +
                    "<span class='progress-description'>进度已完成0%%，请继续加油！</span>" +
                    "</div><!-- /.info-box-content -->" +
                    "</div><!-- /.info-box -->" +
                    "</div>";

    public static void main(String[] args) {

//        String str = String.format("<div class='%1$s'> %2$s </div>", "col", "AAA");
//        String str = String.format(itemHtml, "3", "题目");
//        System.out.println(str);


        System.out.println(SaltEncoder.md5SaltEncode("aaa", "123")); // 812842ccfabece7d23e0808df5a1c30f
    }
}
