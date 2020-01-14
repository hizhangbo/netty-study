package io.github.hizhangbo.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Bob
 * @date 2019-12-18 20:50
 */
public class NioServer {

    /**
     * 服务器启动
     *
     * @throws IOException
     */
    private void start() throws IOException {
        // 1. 创建Selector
        Selector selector = Selector.open();

        // 2. 通过ServerSocketChannel建立channel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 3. 为channel绑定监听端口
        serverSocketChannel.bind(new InetSocketAddress(8000));

        // 4. 设置channel为非阻塞模式
        serverSocketChannel.configureBlocking(false);

        // 5. 将channel注册到selector上，监听连接事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("server start success");

        // 6. 循环等待新接入的连接
        for (; ; ) {
            // 获取注册就绪的channel数量
            int readyChannels = selector.select();
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
                if (selectionKey.isAcceptable()) {
                    acceptHandler(serverSocketChannel, selector);
                }

                if (selectionKey.isReadable()) {
                    readHandler(selectionKey, selector);
                }
            }
        }
    }

    /**
     * 接入事件处理器
     */
    private void acceptHandler(ServerSocketChannel serverSocketChannel, Selector selector) throws IOException {
        // 1.创建socketChannel
        SocketChannel socketChannel = serverSocketChannel.accept();// 为什么连接事件不是从selectionKey中获取SocketChannel？

        // 2. socketChannel设置为非阻塞模式
        socketChannel.configureBlocking(false);

        // 3. 将channel注册到selector上，监听可读事件
        socketChannel.register(selector, SelectionKey.OP_READ);

        // 4. 回复客户端提示信息
        socketChannel.write(StandardCharsets.UTF_8.encode("client connected"));
    }

    /**
     * 可读事件处理器
     */
    private void readHandler(SelectionKey selectionKey, Selector selector) throws IOException {
        // 1. 获取可读事件对应的channel
        SocketChannel channel = (SocketChannel) selectionKey.channel();

        // 2. 创建Buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        // 3. 循环读取客户端请求
        String request = "";
        while (channel.read(byteBuffer) > 0) {
            byteBuffer.flip();
            request += StandardCharsets.UTF_8.decode(byteBuffer);
        }

        // 4. 将channel再次注册到selector上，持续监听可读事件
        channel.register(selector, SelectionKey.OP_READ);

        // 5. 客户端消息广播
        if (request.length() > 0) {
            broadCast(selector, channel, request);
        }
    }

    private void broadCast(Selector selector, SocketChannel sourceChannel, String request) {
        Set<SelectionKey> keys = selector.keys();
        keys.forEach(selectionKey -> {
            Channel targetChannel = selectionKey.channel();

            if (targetChannel instanceof SocketChannel && targetChannel != sourceChannel) {
                try {
                    ((SocketChannel) targetChannel).write(StandardCharsets.UTF_8.encode(request));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public static void main(String[] args) throws IOException {
        new NioServer().start();
    }
}
