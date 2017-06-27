package NioSocket;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NioServer {

    private static final int TIMEOUT = 3000; // 3秒

    private static final int PORT = 9999; // 端口

    private static final int BUF_SIZE=1024; // buf size 长度

    public static void main(String[] args) {
        selector();
    }

    public static void selector() {

        Selector selector = null;

        ServerSocketChannel ssc = null;

        try {
            ssc = ServerSocketChannel.open();
            selector = Selector.open();

            ssc.configureBlocking(false);

            ssc.bind(new InetSocketAddress(PORT));

            // 把通道 绑定到 selectors上
            ssc.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                if (selector.select(TIMEOUT) == 0) { // select()方法返回整数，代表有多少个channel处于就绪，select(TIMEOUT)代表在没超出TIMEOUT时间里有多少channel处于就绪
                    System.out.print(".");
                    continue;
                }

                Iterator<SelectionKey> iter = selector.selectedKeys().iterator();

                while (iter.hasNext()) {

                    SelectionKey selectionKey = iter.next();

                    if (selectionKey.isAcceptable()) {
                        System.out.println("before handleAccept method  ");
                        handleAccept(selectionKey);
                        System.out.println("after handleAccept method  ");
                    }

                    if (selectionKey.isReadable()){
                        System.out.println("before handleRead method  ");
                        handleRead(selectionKey);
                        System.out.println("after handleRead method  ");
                    }

                    if (selectionKey.isValid() && selectionKey.isWritable()) {
                        System.out.println("before handleWrite method  ");
                        handleWrite(selectionKey);
                        System.out.println("after handleWrite method  ");
                    }

                    if(selectionKey.isValid() && selectionKey.isConnectable()){
                        System.out.println("isConnectable = true");
                    }

                    iter.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            {
                try {
                    if (selector != null) {
                        selector.close();
                    }

                    if (ssc != null) {
                        ssc.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void handleAccept(SelectionKey key) throws IOException {
        ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
        SocketChannel sc = ssc.accept();
        sc.configureBlocking(false);
        sc.register(key.selector(), SelectionKey.OP_READ, ByteBuffer.allocateDirect(BUF_SIZE));
    }

    public static void handleRead(SelectionKey key) throws IOException {
        SocketChannel sc = (SocketChannel) key.channel();

        ByteBuffer buf = (ByteBuffer) key.attachment();



        int readLength = sc.read(buf) ;

        while (readLength > 0){

            buf.flip();

            while (buf.hasRemaining()){
                System.out.print((char) buf.get());
            }

            System.out.println();

            buf.clear();
            readLength = sc.read(buf);
        }

        if(readLength == -1){
            sc.close();
        }
    }


    public static void handleWrite(SelectionKey key) throws IOException {

        ByteBuffer buf = (ByteBuffer) key.attachment();

        buf.put("hellldlsdsdsdsd".getBytes());

        buf.flip();

        SocketChannel sc = (SocketChannel) key.channel();

        while (buf.hasRemaining()) {
            sc.write(buf);
        }

        System.out.println("sc.write is correct");

        sc.close();

        buf.compact(); // 把未操作的数据加到buf的最前边

    }
}
