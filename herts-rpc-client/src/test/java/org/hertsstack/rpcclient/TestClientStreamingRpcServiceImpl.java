package org.hertsstack.rpcclient;

import io.grpc.stub.StreamObserver;
import org.hertsstack.core.service.HertsServiceClientStreaming;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestClientStreamingRpcServiceImpl extends HertsServiceClientStreaming<TestClientStreamingRpcService> implements TestClientStreamingRpcService {
    @Override
    public StreamObserver<String> test01(StreamObserver<String> responseObserver) {
        return new StreamObserver<String>() {
            private final List<String> data = new ArrayList<>();

            @Override
            public void onNext(String response) {
                data.add(response);
            }

            @Override
            public void onError(Throwable t) {
            }

            @Override
            public void onCompleted() {
                StringBuilder builder = new StringBuilder();
                for (String d : data) {
                    builder.append(d).append("\n");
                }
                responseObserver.onNext(builder.toString());
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public StreamObserver<TestHoo> test02(StreamObserver<TestFoo> responseObserver) {
        TestFoo foo = new TestFoo();
        return new StreamObserver<TestHoo>() {
            @Override
            public void onNext(TestHoo value) {
                String currentData = "";
                if (foo.getA01() != null) {
                    currentData = foo.getA01() + value.getA01() + "\n";
                } else {
                    currentData = value.getA01();
                }
                foo.setA01(currentData);
            }

            @Override
            public void onError(Throwable t) {
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(foo);
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public StreamObserver<Map<String, String>> test03(StreamObserver<String> responseObserver) {
        StringBuilder next = new StringBuilder();
        return new StreamObserver<Map<String, String>>() {
            @Override
            public void onNext(Map<String, String> value) {
                for (Map.Entry<String, String> entries : value.entrySet()) {
                    next.append(entries.getKey())
                            .append(':')
                            .append(entries.getValue())
                            .append("\n");
                }
            }

            @Override
            public void onError(Throwable t) {
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(next.toString());
                responseObserver.onCompleted();
            }
        };
    }
}
