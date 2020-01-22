# netty-study
## source compile
 - groovy sdk install
   - config GROOVY_HOME
   - groovy -version
 - compile netty-common
   - mvn compile -DskipTests -Dcheckstyle.skip=true
 - h2spec-maven-plugin package
   - download from https://github.com/madgnome/h2spec-maven-plugin
   - mvn package
   
   
   
## server start
 - Main Thread
   - 创建 selector
   - 创建 ServerSocketChannel
   - 初始化 ServerSocketChannel
   - 从 BossGroup 选择一个 NioEventLoop 分配给 ServerSocketChannel
 - Boss Thread
   - 将 ServerSocketChannel 注册到分配的 NioEventLoop 的 selector # 线程切换，基于Task，此时第一次监听的事件是0，而非16（OP_ACCEPT）
   - 绑定地址启动
   - 注册接收连接事件（OP_ACCEPT）到 selector
   
```
说明：
NioEventLoop和selector一一对应，在创建NioEventLoopGroup时，会对每个NioEventLoop中的selector初始化。
```
## build connection
 - Boss Thread
   - NioEventLoop 中的 selector 轮询创建连接事件（OP_ACCEPT）
   - 创建 SocketChannel
   - 初始化 SocketChannel 并从 WorkerGroup 中选择一个 NioEventLoop
 - Worker Thread
   - 将 SocketChannel 注册到选择的 NioEventLoop 的 selector
   - 注册读事件（OP_READ）到 selector 上
   - NioServerSocketChannel.read()创建连接

## receive data
 - Worker Thread
   - 多路复用器（selector）接收到OP_READ事件
   - 处理OP_READ事件：NioSocketChannel.NioSocketChannelUnsafe.read()
     - 分配一个初始1024字节的byte buffer来接收数据
     - 从channel接收数据到byte buffer
     - 记录实际接收数据大小，调整下次分配byte buffer 大小(说明1)
     - 触发pipeline.fireChannelRead(byteBuf)把读取到的数据传播出去
     - 判断接收byte buffer是否已满：是，尝试继续读取直到没有数据或满16次；否，结束本轮读取，等待下次OP_READ事件(说明2)
```
说明：
1.自适应数据大小的分配器（AdaptiveRecvByteBufAllocator）
2.连续读（DefaultMaxMessagesPerRead/DefaultMaxMessagesRecvByteBufAllocator）
3.NioEventLoop -> unsafe.read()中channel的选择：
NioServerSocketChannel.read() -> AbstractNioMessageChannel -> OP_ACCEPT
NioSocketChannel.NioSocketChannelUnsafe.read() -> AbstractNioByteChannel -> OP_READ
```
## service handler
 - 接收数据 pipeline.fireChannelRead(byteBuf)
 - 执行Handler Pipeline
   - 实现了ChannelInboundHandler
   - 实现方法channelRead不能加注解@Skip
   - 读数据是Head -> Tail，写数据是Tail -> Head

## send data
 - Write
   - 写数据到buffer
     - ChannelOutboundBuffer#addMessage
 - Flush
   - 发送buffer里面的数据
     - AbstractChannel.AbstractUnsafe#flush
       - 准备数据：ChannelOutBoundBuffer#addFlush
       - 发送数据：NioSocketChannel#doWrite
 
 - 写数据的三种方式
   - |方式|区别|
     |---|---|
     |write|写到一个buffer|
     |flush|把buffer中的数据发送出去|
     |writeAndFlush|写到buffer，立马发送|
     |关键buffer|ChannelOutboundBuffer|




















