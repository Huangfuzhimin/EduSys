package org.itheima.edu.jcompiler;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by Administrator on 2017/6/9.
 */
public class JCompilerUtils {

    public static Object[] doMagic(String outDir, String[] javaFiles) {
        StringWriter out = new StringWriter();
        File classOutDir = new File(outDir);
        if (!(classOutDir.exists())) {
            classOutDir.mkdirs();
        }

        PrintWriter writer = new PrintWriter(out);

        String destDir = classOutDir.getAbsolutePath();

        boolean compile = JavaCompiler.compile(writer, destDir, javaFiles);
        if (compile) {
            return new Object[]{200, "ok"};
        }

        return new Object[]{404, out.toString()};
    }

    public static class Result {
        public int code;
        public String message;

        public Result(int code, String message) {
            this.code = code;
            this.message = message;
        }
    }
}
