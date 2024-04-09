# socket.io

### step1
* 先启动server，后启动client，可以看到两边的建联日志
* 过10s，client—>server发送消息，过5s，server->client消息
* client部分也可以使用html目录下的index.html


### step2
先启动server，再打开client，即html/ack-index.html

* 页面上发送消息，发送的是ackevent1事件的、可ack的消息，所以发送之后，服务端可以`ackRequest.sendAckData`。
  * client发送消息，服务端收到消息，服务端ack，client接收到ack数据
* 服务端发送消息，发送的是ackevent2事件的、可ack的消息，发送给上面拿到的`SocketIOClient`
  * 服务端发送消息，客户端收到消息，客户端ack，服务端收到ack数据
* 最后一个只只要ack不要ack data的实现，不再赘述。


### step3
上面看到了，server是如何给**指定client**发送消息的。

* 广播时需指定namespace（命名空间），此时该namespace下的所有SocketClient都会收到消息

### step4

* 一个SocketClient一定是属于某个namespace下的某个(些)room
* 一个SocketClient可以切换房间、可以加入多个房间，但namespace不能变。


### 一些注意

* 遇到链接失败，考虑下server和client和版本问题。[LINK](https://github.com/socketio/socket.io-client-java/issues/571)
* client只能指定namespace，不能指定room...反正目前版本下实测不行

### reference
* [netty-socketio-demo](https://github.com/mrniko/netty-socketio-demo)
* [netty-socketio](https://github.com/mrniko/netty-socketio)
* [socket.io-client-java](https://github.com/socketio/socket.io-client-java)
* [socket.io-client-java-doc](https://socketio.github.io/socket.io-client-java/initialization.html)