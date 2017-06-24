package org.itheima.edu.tutorials.utils.cmd;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Poplar on 2017/6/19.
 */
public class KotlinCmd {

    private String classMain;
    private String[] args;
    private Set<String> extDirs = new HashSet<>();
    private Set<String> classpath = new HashSet<>();
    private String encoding = "UTF-8";
    private String separator = ";";     // 参数分隔符, win为; linux为:

    public KotlinCmd(String main) {
        this.classMain = main;
    }

    public KotlinCmd args(String[] args) {
        this.args = args;
        return this;
    }

    public KotlinCmd classpath(String cp) {
        classpath.add(cp);
        return this;
    }

    public KotlinCmd extDir(String extDir) {
        extDirs.add(extDir);
        return this;
    }

    public KotlinCmd encoding(String encoding) {
        this.encoding = encoding;
        return this;
    }

    public KotlinCmd separator(String separator) {
        this.separator = separator;
        return this;
    }

    public String create() {
        return new StringBuilder("java")
                .append(" -Dfile.encoding=").append(encoding)
                .append(" -Djava.ext.dirs=").append(StringUtils.join(extDirs, separator))
                .append(" -cp").append(" ." + separator).append(StringUtils.join(classpath, separator))
                .append(" " + classMain).append(" ").append(StringUtils.join(args, " "))
                .toString();
    }

    public String[] createArr() {

//        java -Dfile.encoding=UTF-8 -Djava.ext.dirs=E:\cms\newstrap\exam\Array-1\firstLast6\libs
//                -cp .:E:\cms\newstrap\exam\Array-1\firstLast6:E:\cms\newstrap\result\aaa\Array-1\firstLast6\1497945848954\bin
//        TestMain E:\cms\newstrap\result\aaa\Array-1\firstLast6\1497945848954\report

//            final String[] command = {
//                    "java", "-Dfile.encoding=UTF-8",
//                    "-Djava.ext.dirs="+rootPath+"/exam/" + questionid + "/lib",
//                    "-cp",".:"+rootPath+"/exam/" + questionid + ":" + binDir.getAbsolutePath(),
//                    "TestMain", reportDir.getAbsolutePath()};
        ArrayList<String> cmdList = new ArrayList<String>(){{
            add("java");
            add("-Dfile.encoding="+encoding);
            add("-Djava.ext.dirs="+StringUtils.join(extDirs, separator));
            add("-cp");add("." + separator + StringUtils.join(classpath, separator));
            add(classMain);add(StringUtils.join(args, " "));
        }};

        return cmdList.toArray(new String[cmdList.size()]);
    }
}
