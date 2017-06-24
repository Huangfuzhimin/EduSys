package org.itheima.edu.kcompiler;

import org.jetbrains.kotlin.cli.common.CLICompiler;
import org.jetbrains.kotlin.cli.common.ExitCode;
import org.jetbrains.kotlin.cli.jvm.K2JVMCompiler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/9.
 */
public class KotlinCompiler {

    private static String kotlinHome;

    public static void setKotlinHome(String kotlinHome){
        KotlinCompiler.kotlinHome = kotlinHome;
    }

    public static Object[] compile(String outDir, String... ktFiles) {

        File classOutDir = new File(outDir);
        if (!(classOutDir.exists())) {
            classOutDir.mkdirs();
        }

        CLICompiler compiler = new K2JVMCompiler();

        List<String> commands = new ArrayList<>();
   /*     commands.add("-g");
        commands.add("-encoding");
        commands.add("utf-8");*/
//        commands.add("-include-runtime");
        commands.add("-nowarn");
//        commands.add("-classpath");
        if(kotlinHome != null && !"".equals(kotlinHome)){
            commands.add("-kotlin-home");
            commands.add(kotlinHome);
        }

        commands.add("-d");
        commands.add(new File(outDir, "Itheima.jar").getAbsolutePath());

        for (String file : ktFiles) {
            commands.add(file);
        }

        ByteArrayOutputStream baos = null;
        PrintStream ps = null;
        try {
            baos = new ByteArrayOutputStream();
            ps = new PrintStream(baos);
            String[] args = commands.toArray(new String[commands.size()]);
            ExitCode exec = compiler.exec(ps, args);
            return new Object[]{exec.getCode(), baos.toString()};// ExitCode.OK.getCode() 成功
        } finally {
            try {
                if( baos != null) baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if( ps != null) ps.close();
        }
    }

    public static void main(String[] args){
        KotlinCompiler.setKotlinHome("G:/JetBrains/ideaIU-2017.1.4.win/plugins/Kotlin/kotlinc");
        Object[] result = KotlinCompiler.compile("G:\\JetBrains\\ideaIU-2017.1.4.win\\plugins\\Kotlin\\kotlinc\\bin\\out",
                new String[]{"G:\\JetBrains\\ideaIU-2017.1.4.win\\plugins\\Kotlin\\kotlinc\\bin\\Itheima.kt"});

        System.out.println(result[0]);
        System.out.println(result[1]);

    }

}
