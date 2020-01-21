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
说明：NioEventLoop和selector一一对应，在创建NioEventLoopGroup时，会对每个NioEventLoop中的selector初始化。

```
## build connection
 - Boss Thread
   - NioEventLoop 中的 selector 轮询创建连接事件（OP_ACCEPT）
   - 创建 SocketChannel
   - 初始化 SocketChannel 并从 WorkerGroup 中选择一个 NioEventLoop
 - Worker Thread
   - 将 SocketChannel 注册到选择的 NioEventLoop 的 selector
   - 注册读事件（OP_READ）到 selector 上

## receive data

## service handler

## send data
