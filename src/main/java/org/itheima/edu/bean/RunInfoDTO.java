package org.itheima.edu.bean;

import java.util.List;

/**
 * Created by Poplar on 2017/2/17.
 */
public class RunInfoDTO {

    private String folder;
    private String type;
    private String pkgName;
    private List<RunFile> files;

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public List<RunFile> getFiles() {
        return files;
    }

    public void setFiles(List<RunFile> files) {
        this.files = files;
    }

    public class RunFile{
        private String fileName;
        private String content;

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
