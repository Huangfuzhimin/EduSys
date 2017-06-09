package org.itheima.edu.jcompiler;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.itheima.edu.jcompiler.tools.javac.main.Main;
import org.itheima.edu.jcompiler.tools.javac.main.Main.Result;

public class JavaCompiler {
	private JavaCompiler() {
	}

	/**
	 * 编译java文件
	 * 
	 * @param writer
	 *            编译过程中的结果输出
	 * @param destDir
	 *            编译后class文件存放的目录
	 * @param javaFiles
	 *            要编译的java文件
	 * @return true 编译成功
	 */
	public static boolean compile(PrintWriter writer, String destDir, String... javaFiles) {
		if (javaFiles == null || javaFiles.length == 0) {
			writer.write("没有编译的java文件");
			return false;
		}

		Main main = new Main("javac", writer);

		List<String> commands = new ArrayList<>();
		commands.add("-g");
		commands.add("-encoding");
		commands.add("utf-8");
		commands.add("-d");
		commands.add(destDir);

		for (String file : javaFiles) {
			commands.add(file);
		}

		Result result = main.compile(commands.toArray(new String[commands.size()]));
		return result.exitCode == 0;
	}

}
