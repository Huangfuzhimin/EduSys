package org.itheima.edu.tutorials.utils.cmd;

import org.apache.commons.lang.StringUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Poplar on 2017/6/19.
 */
public class JavaCmd {

    private String classMain;
    private String[] args;
    private Set<String> extDirs = new HashSet<>();
    private Set<String> classpath = new HashSet<>();
    private String encoding = "UTF-8";
    private String separator = ";";     // 参数分隔符, win为; linux为:

    public JavaCmd(String main) {
        this.classMain = main;
    }

    public JavaCmd args(String[] args) {
        this.args = args;
        return this;
    }

    public JavaCmd classpath(String cp) {
        classpath.add(cp);
        return this;
    }

    public JavaCmd extDir(String extDir) {
        extDirs.add(extDir);
        return this;
    }

    public JavaCmd encoding(String encoding) {
        this.encoding = encoding;
        return this;
    }

    public JavaCmd separator(String separator) {
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
}
