package NioSocket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;

public class NioClient {

    public static void main(String[] var0) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        SocketChannel socketChannel = null;
        try
        {
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress("10.10.10.2",9999));

                TimeUnit.SECONDS.sleep(1);
                if(socketChannel.finishConnect())
                {
                    System.out.println("finishConnet");
                    int i=0;
                    while(true)
                    {
                        String info = "I'm "+i+++"-th information from client";
                        buffer.clear();
                        buffer.put(info.getBytes());
                        buffer.flip();
                        System.out.println("buffer.hasRemaining before ");
                        while(buffer.hasRemaining()){
                            System.out.println("buffer.hasRemaining 中。。。");
                            System.out.println(buffer);
                            socketChannel.write(buffer);
                        }
                    }

                }


        }
        catch (IOException | InterruptedException e)
        {
            e.printStackTrace();
        }
        finally{
            try{
                if(socketChannel!=null){
                    socketChannel.close();
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}
