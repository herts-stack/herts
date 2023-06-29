# Herts

[![build](https://github.com/herts-stack/herts/actions/workflows/build.yaml/badge.svg)](https://github.com/herts-stack/herts/actions/workflows/build.yaml)
[![Apache License](https://img.shields.io/badge/license-Apatch-blue.svg?style=flat)](https://github.com/herts-stack/herts/blob/master/LICENSE)

Unified gRPC/HTTP Realtime API framework for Java.

<img width="194" alt="herts" src="https://github.com/herts-stack/herts/assets/9509132/aad29304-3ed2-4651-bdea-a1c910cbb2ca">

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

![img09](https://github.com/herts-stack/herts/assets/9509132/775c4866-b9cd-4319-b64f-91a3bcc67586)

#### *Support Type*

* gRPC Unary
* gRPC Client Streaming 
* gRPC Server Streaming
* gRPC Bidirectional Streaming
* gRPC Herts Steaming
* HTTP API

## Requirements

Server-side
* Java 11+
* `org.herts`, `io.grpc` packages

Client-side  
* Java 11+
* `org.herts`, `io.grpc` packages

## Getting Started
gRPC Unary Sample code.

Dependency.
```bash
dependencies {
    implementation 'org.herts.core:herts-core:1.0.0'
    implementation 'org.herts.rpc:herts-rpc:1.0.0'
    implementation 'org.herts.rpcclient:herts-rpc-client:1.0.0'
}
```

Definition Interface.  
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

Implementation class.  
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

Payload definition.  
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

Server side.
```java
public class Main {
  
    public static void main(String[] args) {
        UnaryService service = new UnaryServiceImpl();
        HertsRpcServerEngine engine = HertsRpcServerEngineBuilder.builder()
                .registerHertsRpcService(service)
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
                .registerHertsRpcServiceInterface(UnaryService.class)
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

### More Examples
* [Herts document - Getting Started - gRPC Unary](https://herts-framework.herts-stack.org/getting-started/grpc_unary/)
* [Herts document - Getting Started - gRPC Server Streaming](https://herts-framework.herts-stack.org/getting-started/grpc_serverstreaming/)
* [Herts document - Getting Started - gRPC Client Streaming](https://herts-framework.herts-stack.org/getting-started/grpc_clientstreaming/)
* [Herts document - Getting Started - gRPC Bid Streaming](https://herts-framework.herts-stack.org/getting-started/grpc_bidstreaming/)
* [Herts document - Getting Started - gRPC Reactive Streaming](https://herts-framework.herts-stack.org/getting-started/grpc_reactivestreaming/)
* [Herts document - Getting Started - gRPC Http Service](https://herts-framework.herts-stack.org/getting-started/http/)

## License

[Apache-2.0](https://github.com/herts-stack/herts/blob/master/LICENSE)
