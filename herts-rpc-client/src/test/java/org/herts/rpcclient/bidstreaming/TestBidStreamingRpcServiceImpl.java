package org.herts.rpcclient.bidstreaming;

import io.grpc.stub.StreamObserver;
import org.herts.common.service.HertsBidirectionalStreamingService;
import org.herts.rpcclient.TestFoo;
import org.herts.rpcclient.TestHoo;

import java.util.HashMap;
import java.util.Map;

public class TestBidStreamingRpcServiceImpl extends HertsBidirectionalStreamingService<TestBidStreamingRpcService> implements TestBidStreamingRpcService {
    @Override
    public StreamObserver<String> test01(StreamObserver<String> responseObserver) {
        StringBuilder builder = new StringBuilder();
        return new StreamObserver<String>() {
            @Override
            public void onNext(String value) {
                if (value.equals("please_ping")) {
                    responseObserver.onNext("ping_response");
                } else {
                    builder.append(value).append("\n");
                }
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(builder.toString());
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public StreamObserver<TestFoo> test02(StreamObserver<TestHoo> responseObserver) {
        return new StreamObserver<TestFoo>() {
            @Override
            public void onNext(TestFoo value) {
                TestHoo hoo = new TestHoo();
                hoo.setA01("Received");
                responseObserver.onNext(hoo);
            }

            @Override
            public void onError(Throwable t) {
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public StreamObserver<Map<String, String>> test03(StreamObserver<Map<String, String>> responseObserver) {
        return new StreamObserver<Map<String, String>>() {
            @Override
            public void onNext(Map<String, String> value) {
                Map<String, String> data = new HashMap<>();
                data.put("key", "value_" + System.nanoTime());
                responseObserver.onNext(data);
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public StreamObserver<byte[]> error01(StreamObserver<byte[]> responseObserver) {
        return new StreamObserver<byte[]>() {
            @Override
            public void onNext(byte[] value) {
                throw new RuntimeException("unexpected");
            }

            @Override
            public void onError(Throwable t) {
            }

            @Override
            public void onCompleted() {
            }
        };
    }

    @Override
    public StreamObserver<byte[]> error02(StreamObserver<byte[]> responseObserver) {
        return new StreamObserver<byte[]>() {
            @Override
            public void onNext(byte[] value) {
                responseObserver.onNext(new byte[100]);
            }

            @Override
            public void onError(Throwable t) {
                responseObserver.onError(t);
            }

            @Override
            public void onCompleted() {
            }
        };
    }
}
