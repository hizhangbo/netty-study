package io.github.hizhangbo.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Bob
 * @date 2019-12-18 21:14
 */
public class NioClient {

    ExecutorService executorService = Executors.newSingleThreadExecutor();

    private void start() throws IOException {
        // 1. 连接
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8000));

        // 2. 接收服务端响应
        receive(socketChannel);

        // 3. 发送
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String request = scanner.nextLine();
            if (request != null && request.length() > 0) {
                socketChannel.write(StandardCharsets.UTF_8.encode(request));
            }
        }
    }

    private void receive(SocketChannel socketChannel) throws IOException {
        Selector selector = Selector.open();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        executorService.submit(() -> {
            for (; ; ) {
                // 获取注册就绪的channel数量
                int readyChannels = 0;
                try {
                    readyChannels = selector.select();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (readyChannels == 0) {
                    continue;
                }

                // 获取具体事件
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();

                    iterator.remove();

                    // 7. 根据就绪状态，调用对应方法处理
                    if (selectionKey.isReadable()) {
                        try {
                            readHandler(selectionKey, selector);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    /**
     * 可读事件处理器
     */
    private void readHandler(SelectionKey selectionKey, Selector selector) throws IOException {
        // 1. 获取可读事件对应的channel
        SocketChannel channel = (SocketChannel) selectionKey.channel();

        // 2. 创建Buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        // 3. 循环读取服务器端响应
        StringBuilder response = new StringBuilder();
        while (channel.read(byteBuffer) > 0) {
            byteBuffer.flip();
            response.append(StandardCharsets.UTF_8.decode(byteBuffer));
        }

        // 4. 将channel再次注册到selector上，持续监听可读事件
        channel.register(selector, SelectionKey.OP_READ);

        // 5. 打印服务器响应信息
        if (response.length() > 0) {
            System.out.println(response);
            System.out.println("client connected,send message:");
        }
    }

    public static void main(String[] args) throws IOException {
        new NioClient().start();
    }
}
