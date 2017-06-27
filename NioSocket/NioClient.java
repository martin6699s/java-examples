package NioSocket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;

public class NioClient {
    public NioClient() {
    }

    public static void main(String[] var0) {
        ByteBuffer var1 = ByteBuffer.allocate(1024);
        SocketChannel var2 = null;

        try {
            var2 = SocketChannel.open();
            var2.configureBlocking(false);
            var2.connect(new InetSocketAddress("10.10.10.2", 9999));
            if (var2.finishConnect()) {
                int var3 = 0;

                while (true) {
                    while (true) {
                        try {
                            TimeUnit.SECONDS.sleep(1L);
                            String var4 = "I'm " + var3++ + "-th information from client";
                            var1.clear();
                            var1.put(var4.getBytes());
                            var1.flip();

                            while (var1.hasRemaining()) {
                                System.out.println(var1);
                                var2.write(var1);
                            }
                        } catch (InterruptedException var14) {
                            var14.printStackTrace();
                        }
                    }
                }
            }
        } catch (IOException var15) {
            var15.printStackTrace();
        } finally {
            try {
                if (var2 != null) {
                    var2.close();
                }
            } catch (IOException var13) {
                var13.printStackTrace();
            }

            System.out.println("this is over && finish");
        }
    }
}
