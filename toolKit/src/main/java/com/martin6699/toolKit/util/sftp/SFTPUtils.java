package com.martin6699.toolKit.util.sftp;

import com.jcraft.jsch.*;
import com.jcraft.jsch.ChannelSftp.LsEntry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.provider.sftp.SftpClientFactory;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Vector;

/**
 * 参考GaoFeiGit的github工具箱sftp工具类改进，
 * GaoFeiGit github地址为：
 * https://github.com/GaoFeiGit/xutils/tree/master/src/org/xdemo/app/xutils/ext/sftp
 *
 * 功能列表<br>
 * 1.多文件上传，支持自动创建目录<br>
 * 2.下载文件，支持下载多个文件、目录到本地目录，到ZIP压缩包<br>
 * 4.创建目录，支持创建多级目录<br>
 * 5.判断是否是文件夹<br>
 * 6.判断是否是文件<br>
 * 7.文件重命名<br>
 * 8.递归列出指定目录下的所有文件，包括子目录<br>
 */
public class SFTPUtils {

    private final static Logger LOG = LoggerFactory.getLogger(SFTPUtils.class);

    private static ChannelSftp cs;

    private static Session session;

    private static final String separator = "/";
    private static final String UTF_8 = "UTF-8";

    private SFTPUtils() {
    }

    /**
     * 获取SFTP实例
     *
     * @param host     主机
     * @param port     端口
     * @param user     用户
     * @param password 密码
     * @return
     * @throws FileSystemException
     * @throws JSchException
     */
    public static SFTPUtils getInstance(String host, int port, String user, String password) throws FileSystemException, JSchException {
        FileSystemOptions vfs = new FileSystemOptions();
        SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(vfs, "no");
        // 设置免输入验证信息 ，通过密码则填password; 通过公钥或密码或键盘交互等多种方式，填publickey,password,keyboard-interactive
        SftpFileSystemConfigBuilder.getInstance().setPreferredAuthentications(vfs, "password");
        SftpFileSystemConfigBuilder.getInstance().setSessionTimeoutMillis(vfs, 120 * 60 * 1000); // 2小时
        SftpFileSystemConfigBuilder.getInstance().setConnectTimeoutMillis(vfs, 120 * 1000); // 2分钟

        session = SftpClientFactory.createConnection(host, port, user.toCharArray(), password.toCharArray(), vfs);
        SftpFileSystemConfigBuilder builder = SftpFileSystemConfigBuilder.getInstance();

        Channel channel = session.openChannel("sftp");
        channel.connect();
        cs = (ChannelSftp) channel;
        return new SFTPUtils();
    }

    /**
     * 断开连接
     */
    public void close() {
        if (cs != null) {
            cs.exit();
        }
        if (session != null) {
            session.disconnect();
        }
        cs = null;
    }

    /**
     * 列出路径下的文件和目录
     *
     * @param path
     * @return
     * @throws SftpException
     */
    @SuppressWarnings("unchecked")
    public Vector<SFTPFile> list(String path) throws SftpException {
        Vector<LsEntry> v = null;
        try {
            v = cs.ls(path);
        } catch (SftpException e) {
            LOG.error("path={}, 异常信息：{}", path, e.getMessage());
            return new Vector<>(0);
        }

        if (!(path.endsWith("\\") || path.endsWith("/")))
            path += separator; // 必须为斜杆“/”，不能为反斜杠，否则路径在linux上找不到
        Vector<SFTPFile> files = new Vector<SFTPFile>();
        for (LsEntry entry : v) {
            if (entry.getFilename().equals(".") || entry.getFilename().equals("..")) continue;
            files.add(new SFTPFile(entry.getAttrs().isDir(), entry.getAttrs().isFifo(), entry.getAttrs().getSize(), entry.getFilename(), path, path + entry.getFilename(), (long) entry.getAttrs().getMTime()));
        }
        return files;
    }

    /**
     * 列出指定路径下的文件和目录，以及所有的子目录
     *
     * @param path
     * @return
     * @throws SftpException
     */
    @SuppressWarnings("unchecked")
    public Vector<SFTPFile> listAll(String path) throws SftpException {
        Vector<LsEntry> v = null;
        try {
            v = cs.ls(path);
        } catch (SftpException e) {
            LOG.error("path={}, 异常信息：{}", path, e.getMessage());
            return new Vector<>(0);
        }

        if (!(path.endsWith("\\") || path.endsWith("/")))
            path += separator; // 必须为斜杆“/”，不能为反斜杠，否则路径在linux上找不到
        Vector<SFTPFile> files = new Vector<SFTPFile>();
        for (LsEntry entry : v) {
            if (entry.getFilename().equals(".") || entry.getFilename().equals("..")) continue;
            files.add(new SFTPFile(entry.getAttrs().isDir(), entry.getAttrs().isFifo(), entry.getAttrs().getSize(), entry.getFilename(), path, path + entry.getFilename(), (long) entry.getAttrs().getMTime()));
            if (entry.getAttrs().isDir())
                files.addAll(listAll(path + entry.getFilename()));
        }

        return files;
    }


    /**
     * 重命名文件
     *
     * @param src
     * @param target
     * @throws SftpException
     */
    public void rename(String src, String target) throws SftpException {
        cs.rename(src, target);
    }

    /**
     * 是否是文件
     *
     * @param file
     * @return
     */
    public boolean isFile(String file) {
        try (InputStream is = cs.get(file)) {
            if (is == null) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    /**
     * 是否是目录
     *
     * @param dir
     * @return
     */
    public boolean isDir(String dir) {
        try {
            cs.cd(dir);
            cs.cd("..");
            return true;
        } catch (Exception e) {
            System.out.println("检查没有该目录(" + dir + ")，信息: " + e.getMessage());
            return false;
        }
    }

    /**
     * 创建目录,支持多级创建
     *
     * @param fullPathName 全路径，如/x/y/z
     * @throws SftpException
     */
    public void createDir(String fullPathName) throws SftpException {

        if (fullPathName.startsWith("/") || fullPathName.startsWith("\\"))
            fullPathName = fullPathName.substring(1);
        if (fullPathName.endsWith("/") || fullPathName.endsWith("\\"))
            fullPathName = fullPathName.substring(0, fullPathName.lastIndexOf("/"));
        fullPathName = fullPathName.replace("\\", "/");
        fullPathName = fullPathName.replace("//", "/");

        if (!isDir(fullPathName)) {
            String[] path = fullPathName.split("/");
            String base = "/";
            for (int i = 0; i < path.length - 1; i++) {
                base += path[i] + "/";
                createDir(base, path[i + 1]);
            }
        }
    }

    /**
     * 创建目录
     *
     * @param parent
     * @param dir
     * @throws SftpException
     */
    public void createDir(String parent, String dir) throws SftpException {
        if (!isDir(parent)) {
            System.out.println("创建" + parent + "目录");
            cs.mkdir(parent);
        }

        String currentDir = parent + dir;
        if (!isDir(currentDir)) {
            System.out.println("创建" + currentDir + "目录");
            cs.mkdir(currentDir);
        }
    }


    public void uploadDir(String ftpDir, String localFileDir) throws SftpException {
        // 1. 创建远端ftpDir目录
        // 2. 遍历localFileDir 拿到所有本地目录和文件
        // 3. for创建目录和上传文件
        if (!(ftpDir.endsWith("/") || ftpDir.endsWith("\\"))) {
            ftpDir += "/";
        }

        if (!isDir(ftpDir)) {
            createDir(ftpDir);
        }

        Collection<File> localFiles = FileUtils.listFilesAndDirs(new File(localFileDir), TrueFileFilter.INSTANCE, DirectoryFileFilter.DIRECTORY);

        if (localFileDir.endsWith("/") || localFileDir.endsWith("\\")) {
            localFileDir = localFileDir.substring(0, localFileDir.length() - 1);
        }

        for (File file : localFiles) {
            String filePath = file.getAbsolutePath();
            filePath = filePath.replaceAll("\\\\", "/");
            filePath = filePath.replace(localFileDir, "");
            String createFtpPath = ftpDir + filePath;
            if (file.isDirectory()) {
                createDir(createFtpPath);
            } else {
                cs.put(file.getAbsolutePath(), createFtpPath);
            }

        }

    }

    /**
     * 上传多个文件
     *
     * @param ftpDir
     * @param files
     * @throws SftpException
     */
    public void upload(String ftpDir, String... files) throws SftpException {


        if (!isDir(ftpDir)) {
            createDir(ftpDir);
        }
        for (String file : files)
            cs.put(file, ftpDir);
    }

    /**
     * 上传单个文件
     * <p>
     *
     *
     * @param ftpFileDir 远端目录路径
     * @param localFile  本地文件路径
     */
    public void uploadOneFile(String ftpFileDir, String localFile) throws SftpException {

        ftpFileDir = ftpFileDir.replaceAll("\\\\", "/");
        if (!ftpFileDir.endsWith("/")) {
            ftpFileDir = ftpFileDir.substring(0, ftpFileDir.lastIndexOf("/"));
        }

        upload(ftpFileDir, localFile);
    }

    /**
     * 下载目录到本地目录
     *
     * @param ftpDir
     * @param localDir
     * @throws SftpException
     */
    public void downloadDir(String ftpDir, String localDir) throws SftpException {
        if (!localDir.endsWith("/") || !localDir.endsWith("\\")) {
            localDir += "/";
        }
        Vector<SFTPFile> files = listAll(ftpDir);
        for (SFTPFile file : files) {
            if (file.isDir()) {
                //如果是路径，创建目录
                new File(localDir + file.getPathName().replace(ftpDir, "")).mkdirs();
            } else {
                File path = new File(localDir + file.getPathName().replace(ftpDir, "")).getParentFile();
                if (!path.exists()) {
                    path.mkdirs();
                }
                cs.get(file.getPathName(), localDir + file.getPathName().replace(ftpDir, ""));
            }
        }
    }

    /**
     * 下载目录到ZIP文件
     *
     * @param ftpDir
     * @param localZipPathName
     * @throws SftpException
     */
    public void downloadDirToZip(String ftpDir, String localZipPathName) throws SftpException {
        byte[] buf = new byte[1024];

        Vector<SFTPFile> list = listAll(ftpDir);

        try (FileOutputStream fos = new FileOutputStream(localZipPathName);
             ZipOutputStream zipStream = new ZipOutputStream(new BufferedOutputStream(fos))) {
            zipStream.setEncoding("GBK");
            for (SFTPFile ftpFile : list) {
                String entryName = ftpFile.getPathName().replace(ftpDir, "");
                if (entryName.startsWith("/")) {
                    entryName = entryName.substring(1);
                }
                if (ftpFile.isDir()) {
                    entryName += "/";
                }
                ZipEntry entry = new ZipEntry(entryName);
                zipStream.putNextEntry(entry);
                if (ftpFile.isFile()) {
                    InputStream is = cs.get(ftpFile.getPathName());
                    if (is != null) {
                        try {
                            int readLen = -1;
                            while ((readLen = is.read(buf, 0, 1024)) != -1) {
                                zipStream.write(buf, 0, readLen);
                            }
                        } finally {
                            is.close();
                        }

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 下载多个文件到本地压缩包
     *
     * @param localZipPathName
     * @param pathnames
     * @throws IOException
     */
    public void downloadFiles(String localZipPathName, String... pathnames) throws IOException {
        byte[] buf = new byte[1024];
        try (FileOutputStream fos = new FileOutputStream(localZipPathName);
             ZipOutputStream zipStream = new ZipOutputStream(new BufferedOutputStream(fos))) {
            for (String pathname : pathnames) {
                ZipEntry entry = new ZipEntry(pathname.substring(pathname.lastIndexOf("/") + 1));
                zipStream.putNextEntry(entry);
                InputStream is = cs.get(pathname);
                if (is != null) {
                    try {
                        int readLen = -1;
                        while ((readLen = is.read(buf, 0, 1024)) != -1) {
                            zipStream.write(buf, 0, readLen);
                        }
                    } finally {
                        is.close();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 下载指定路径指定文件到本地
     *
     * @param ftpFilePath
     * @param localFile
     * @return
     * @throws SftpException
     */
    public void downloadFile(String ftpFilePath, String localFile) throws SftpException {

        File f = new File(localFile);
        if (!f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        }

        cs.get(ftpFilePath, localFile);
    }


//    public static void main(String[] args) throws Exception {
//
//        // 192.168.1.11 /martin6699/server/MartinTomcat/webapps
//        // --->
//        // 192.168.1.12 /home/martin6699/testsshdownload/test.war
//
//        SFTPUtils sftp = SFTPUtils.getInstance("192.168.1.12", 22, "root", "123456");
//
//        sftp.downloadFile("/martin6699/server/MartinTomcat/webapps/test.war", "/martin6699/server/testsshdownload/test.war");
//
//      //  sftp.downloadDir("/root/testscp/", "E:\\log\\atestscp\\");
//
//        System.out.println("成功下载到本地");
//
//    }


}
