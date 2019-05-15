package com.martin6699.toolKit.util.sftp;

/**
 * 参考GaoFeiGit的github工具箱sftp工具类改进，
 * GaoFeiGit github地址为：
 * https://github.com/GaoFeiGit/xutils/tree/master/src/org/xdemo/app/xutils/ext/sftp
 */
public class SFTPFile {
    private boolean isDir;
    private boolean isFile;
    private Long size;
    private String name;
    private String parent;
    private String pathname;
    private Long modifyTime;
    public boolean isDir() {
        return isDir;
    }
    public void setDir(boolean isDir) {
        this.isDir = isDir;
    }
    public boolean isFile() {
        return isFile;
    }
    public void setFile(boolean isFile) {
        this.isFile = isFile;
    }
    public Long getSize() {
        return size;
    }
    public void setSize(Long size) {
        this.size = size;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getParent() {
        return parent;
    }
    public void setParent(String parent) {
        this.parent = parent;
    }
    public String getPathName() {
        return pathname;
    }
    public void setPathName(String pathname) {
        this.pathname = pathname;
    }
    public Long getModifyTime() {
        return modifyTime;
    }
    public void setModifyTime(Long modifyTime) {
        this.modifyTime = modifyTime;
    }
    @Override
    public String toString() {
        return "SFTPFile [isDir=" + isDir + ", isFile=" + isFile + ", size=" + size + ", name=" + name + ", parent=" + parent + ", pathname=" + pathname + ", modifyTime=" + modifyTime + "]";
    }
    /**
     * @param isDir
     * @param isFile
     * @param size
     * @param name
     * @param parent
     * @param pathname
     * @param modifyTime
     */
    public SFTPFile(boolean isDir, boolean isFile, Long size, String name, String parent, String pathname, Long modifyTime) {
        super();
        this.isDir = isDir;
        this.isFile = isFile;
        this.size = size;
        this.name = name;
        this.parent = parent;
        this.pathname = pathname;
        this.modifyTime = modifyTime;
    }

}
