# Herts

[![build](https://github.com/herts-stack/herts/actions/workflows/build.yaml/badge.svg)](https://github.com/herts-stack/herts/actions/workflows/build.yaml)
[![Apache License](https://img.shields.io/badge/license-Apatch-blue.svg?style=flat)](https://github.com/herts-stack/herts/blob/master/LICENSE)
[![Doc](https://img.shields.io/badge/herts-core?logo=herts&logoColor=%23003366&label=document&color=%23000077&link=https%3A%2F%2Fherts-framework.herts-stack.org%2F
)](https://herts-framework.herts-stack.org/)

Unified gRPC/HTTP Realtime API framework for Java.

<img width="130" alt="herts" src="https://github.com/herts-stack/herts/assets/9509132/f1717514-a403-4597-949f-96c5820f9894">

---

## Document
**[Herts Document](https://herts-framework.herts-stack.org/)**

## About
#### *Code base Streaming*
This framework is based on gRPC Streaming, which is a fast and binary network transport for HTTP/2.  
However, unlike plain gRPC, it treats **Java interfaces as a protocol schema**, enabling seamless code sharing without .proto .

#### *Easy to load balancing*
Loadbalancing is easily performed with functions supported by Herts.  
There are multiple OSS to support.  

#### *Original Streaming Interface*
Herts support original bidirectional streaming.  
It enables two-way communication with a **simple interface**. Also, this communication method can be **easily load balanced**.

<kbd><img width="800" alt="herts" src="https://github.com/herts-stack/herts/assets/9509132/a6af6afb-3fea-46f0-8221-4f0ed552495e"></kbd>

#### *Support Service Type*

* gRPC Unary
* gRPC Client Streaming 
* gRPC Server Streaming
* gRPC Bidirectional Streaming
* gRPC Herts Reactive Steaming
* HTTP API

## Requirements

Server-side
* Java 11+
* `org.herts`, `io.grpc` packages

Client-side  
* Java 11+
* `org.herts`, `io.grpc` packages

## Getting Started

Dependency.
```bash
dependencies {
    implementation 'org.herts.core:herts-core:1.0.0'
    implementation 'org.herts.rpc:herts-rpc:1.0.0'
    implementation 'org.herts.rpcclient:herts-rpc-client:1.0.0'
}
```

Request Payload definition.  
Herts request and response models require `extends HertsMessage`
```java
public class Payload extends HertsMessage {
    private String hoo;

    public String getHoo() {
        return hoo;
    }
    public void setHoo(String hoo) {
        this.hoo = hoo;
    }
}
```

### **Herts Unary** sample
#### Define Service
Define a interface.  
It is used by server and client both.  
Herts interface require a `@HertsRpcService(value = HertsType.XXXX)` and `extends HertsService`.
```java
import org.herts.core.annotation.HertsRpcService;
import org.herts.core.service.HertsService;

@HertsRpcService(value = HertsType.Unary)
public interface UnaryService extends HertsService {

    String helloWorld();

    Map<String, String> getUser(Payload payload);

}
```

Implementation a class.  
Herts implementation require a `extends HertsServiceXXXX<YOUTR INTERFCE> implements YOUTR INTERFCE`.
```java
import org.herts.core.service.HertsServiceUnary;

public class UnaryServiceImpl extends HertsServiceUnary<UnaryService> implements UnaryService {
    
    @Override
    public String helloWorld() {
        return "hello world";
    }
    
    @Overide
    public Map<String, String> getUser(Payload payload) {
        System.out.println(payload);
        return Collections.singletonMap("name", "foo");
    }
}
```

#### Start Server/Client

Server side.
```java
public class Main {
  
    public static void main(String[] args) {
        UnaryService service = new UnaryServiceImpl();
        HertsRpcServerEngine engine = HertsRpcServerEngineBuilder.builder()
                .registerHertsRpcService(service) // You can register multi service
                .build();

        engine.start();
    }
}
```

Client side.
```java
public class Main {
  
    public static void main(String[] args) {
        HertsRpcClient client = HertsRpcClientBuilder
                .builder("localhost")
                .secure(false)
                .registerHertsRpcServiceInterface(UnaryService.class) // You can register multi service
                .connect();

        UnaryService service = client.createHertsRpcService(UnaryService.class);
        
        var res01 = service.helloWorld();
        System.out.println(res01);

        Payload p = new Payload();
        p.setHoo("test");
        var res02 = service.getUser(p);
        System.out.println(res02);
    }
}
```


### **Herts Reactive Streaming** sample
#### Define Server Service

Define a server service interface.  
It is used by server and client both.  
Herts service interface require a `@HertsRpcService(value = HertsType.XXXX)` and `extends HertsService`.
```java
import org.herts.core.annotation.HertsRpcService;
import org.herts.core.service.HertsReactiveService;

@HertsRpcService(value = HertsType.Reactive)
public interface ReactiveStreamingService extends HertsReactiveService {
    String publishToClient(Payload payload);
    List<String> getIds();
}
```

Implementation a class.  
Herts implementation require a `extends HertsServiceXXXX<YOUTR INTERFCE> implements YOUTR INTERFCE`.
```java
import org.herts.core.service.HertsServiceReactiveStreaming;

public class ReactiveStreamingServiceImpl extends HertsServiceReactiveStreaming<ReactiveStreamingService, ReactiveReceiver> implements ReactiveStreamingService {
    
    @Override
    public String publishToClient(Payload payload) {
        var clientId = getClientId();
        var uniqId = UUID.randomUUID().toString();
        broadcast(clientId).onReceivedData(clientId, "Published");
        return uniqId;
    }

    @Override
    public List<String> getIds() {
        return Collections.singletonList("Hello");
    }

}
```

#### Define Client Receiver

Define client receiver interface.  
It is used by server and client both.  
Herts receiver interface require a `@HertsRpcReceiver` and `extends HertsReceiver`.
```java
import org.herts.core.annotation.HertsRpcReceiver;
import org.herts.core.service.HertsReceiver;

@HertsRpcReceiver
public interface ReactiveReceiver extends HertsReceiver {
    void onReceivedData(String fromClient, String data);
}
```

Implementation class.  
```java
public class ReactiveReceiverImpl implements ReactiveReceiver {
    @Override
    public void onReceivedData(String fromClient, String data) {
        System.out.println("Client: " + fromClient + ", Data: " + data);
    }
}
```

#### Start Server/Client
Server side.
```java
public class Main {
    public static void main(String[] args) {
        var service = new ReactiveStreamingServiceImpl();

        HertsRpcServerEngine engine = HertsRpcServerEngineBuilder.builder()
                .registerHertsReactiveRpcService(service) // You can register multi service
                .build();

        engine.start();
    }
}
```

Client side.
```java
public class Main {
  
    public static void main(String[] args) {
        HertsRpcClient client = HertsRpcClientBuilder
                .builder("localhost")
                .secure(false)
                .registerHertsRpcServiceInterface(ReactiveStreamingService.class) // You can register multi service
                .registerHertsRpcReceiver(new ReactiveReceiver())                 // You can register multi receiver
                .connect();

        ReactiveStreamingService service = client.createHertsRpcService(ReactiveStreamingService.java);
        
        Payload p = new Payload();
        var res = service.publishToClient(p);
        System.out.println(res);
    }
}
```

## All Examples
* [Herts document - Getting Started - gRPC Unary](https://herts-framework.herts-stack.org/getting-started/grpc_unary/)
* [Herts document - Getting Started - gRPC Server Streaming](https://herts-framework.herts-stack.org/getting-started/grpc_serverstreaming/)
* [Herts document - Getting Started - gRPC Client Streaming](https://herts-framework.herts-stack.org/getting-started/grpc_clientstreaming/)
* [Herts document - Getting Started - gRPC Bid Streaming](https://herts-framework.herts-stack.org/getting-started/grpc_bidstreaming/)
* [Herts document - Getting Started - gRPC Reactive Streaming](https://herts-framework.herts-stack.org/getting-started/grpc_reactivestreaming/)
* [Herts document - Getting Started - gRPC Http Service](https://herts-framework.herts-stack.org/getting-started/http/)

## License

[Apache-2.0](https://github.com/herts-stack/herts/blob/master/LICENSE)
