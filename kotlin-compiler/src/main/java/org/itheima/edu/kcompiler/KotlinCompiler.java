package org.itheima.edu.kcompiler;

import org.jetbrains.kotlin.cli.common.CLICompiler;
import org.jetbrains.kotlin.cli.common.ExitCode;
import org.jetbrains.kotlin.cli.jvm.K2JVMCompiler;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/9.
 */
public class KotlinCompiler {

    public static boolean compile(PrintStream stream, String... ktFiles) {
        CLICompiler compiler = new K2JVMCompiler();

        List<String> commands = new ArrayList<>();
   /*     commands.add("-g");
        commands.add("-encoding");
        commands.add("utf-8");*/
        commands.add("-d");
        commands.add("gaga.jar");

        for (String file : ktFiles) {
            commands.add(file);
        }

        String[] args = commands.toArray(new String[commands.size()]);

        ExitCode exec = compiler.exec(stream, args);
        return exec == ExitCode.OK;
    }

}
