# Herts

[![build](https://github.com/herts-stack/herts/actions/workflows/build.yaml/badge.svg)](https://github.com/herts-stack/herts/actions/workflows/build.yaml)
[![Apache License](https://img.shields.io/badge/license-Apatch-blue.svg?style=flat)](https://github.com/herts-stack/herts/blob/master/LICENSE)

Unified gRPC/HTTP Realtime API framework for Java.

![logo_index](https://github.com/herts-stack/herts/assets/9509132/26d11e7b-172a-44b8-a163-17dfbf2d2dac)

**[Herts Document](https://herts-framework.herts-stack.org/)**

## About

This framework is based on gRPC Streaming, which is a fast and binary network transport for HTTP/2.  
However, unlike plain gRPC, it treats Java interfaces as a protocol schema, enabling seamless code sharing without .proto .

![img09](https://github.com/herts-stack/herts/assets/9509132/a26742d2-bf76-4d4e-9e92-168e83427032)

Support functions  

* gRPC Unary
* gRPC Client Streaming 
* gRPC Server Streaming
* gRPC Bidirectional Streaming
* gRPC Herts Steaming
* HTTP API Interface

## Requirements

Server-side
* Java 11+
* `org.herts` packages
* `io.grpc` packages

Client-side  
* Java 11+
* `org.herts` packages
* `io.grpc` packages


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
```java
import org.herts.core.annotation.HertsRpcService;
import org.herts.core.service.HertsService;

@HertsRpcService(value = HertsType.Unary)
public interface UnaryService extends HertsService {

    String helloWorld();

    Map<String, String> getUser(String id);

}
```

Implementation
```java
import org.herts.core.service.HertsServiceUnary;

public class UnaryServiceImpl extends HertsServiceUnary<UnaryService> implements UnaryService {
    
    @Override
    public String helloWorld() {
        return "hello world";
    }
    
    @Overide
    public Map<String, String> getUser(String id) {
        return Collections.singletonMap("name", "foo");
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

        var res02 = service.getUser("ID");
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
