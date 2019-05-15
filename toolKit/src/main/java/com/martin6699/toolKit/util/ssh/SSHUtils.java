package com.martin6699.toolKit.util.ssh;

import com.jcraft.jsch.*;

import org.apache.commons.io.IOUtils;
import org.apache.commons.vfs2.FileSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * 通过jsch ssh隧道实现远端执行命令，类似使用ssh登录远端机器执行命令的功能
 *
 */
public class SSHUtils {

    private final static Logger LOG = LoggerFactory.getLogger(SSHUtils.class);


    private static Session session;

    private static final String UTF_8 = "UTF-8";

    private SSHUtils() {
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
    public static SSHUtils getInstance(String host, int port, String user, String password) throws JSchException {
        session = new JSch().getSession(user, host, port);
        session.setPassword(password);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        return new SSHUtils();
    }

    public void close() {
        // 它会把整个应用程序kill掉
        session.disconnect();
    }

    public String executeCommand(String command) {
        if (session == null || !session.isConnected()) {
            LOG.error("session is null");
            return null;
        }

        LOG.info("Execute command {} \r\n to {}", command, session.getHost());

        Channel tempChannel = null;
        try {
            tempChannel = session.openChannel("exec");
        } catch (JSchException e) {
            e.printStackTrace();
            return null;

        }

        ChannelExec channel = (ChannelExec) tempChannel;

        channel.setCommand(command);
        channel.setInputStream(null);
        channel.setErrStream(System.err);

        try (InputStream output = channel.getInputStream()){

            try {
                channel.connect();
                String result = IOUtils.toString(output, UTF_8);
                LOG.info("Result of command {} on {}: {}", command, session.getHost(), result);
                return result;
            } finally {
                channel.disconnect();
            }
        } catch (JSchException | IOException e) {
            LOG.error("Failed to execute command {} on {} due to {}", command, session.getHost(), e.toString());
            return null;
        }
    }
}
