## 手写RPC(Remote Procedure Call)
要实现一个RPC不算难，难的是实现一个高性能高可靠的RPC框架。

---
### 一、前言
RPC要解决的两个问题   
> 1.解决分布式系统中，服务之间的调用问题。     
2.远程调用时，要能够像本地调用一样方便，让调用者感知不到远程调用的逻辑。

完整RPC调用过程：   
![desc](https://raw.githubusercontent.com/chenxingxing6/myrpc/master/img/1.jpg)    

> 以左边的Client端为例，Application就是rpc的调用方，Client Stub就是我们上面说到的代理对象，其实内部是通过rpc方
式来进行远程调用的代理对象，至于Client Run-time Library，则是实现远程调用的工具包，比如jdk的Socket，最后通过底
层网络实现实现数据的传输。

> 这个过程中最重要的就是序列化和反序列化了，因为数据传输的数据包必须是二进制的，你直接丢一个Java对象过去，人家可不
认识，你必须把Java对象序列化为二进制格式，传给Server端，Server端接收到之后，再反序列化为Java对象。