package org.herts.rpcclient.clientstreaming;

import io.grpc.stub.StreamObserver;
import org.herts.common.service.HertsClientStreamingService;
import org.herts.rpcclient.TestFoo;
import org.herts.rpcclient.TestHoo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestClientStreamingRpcServiceImpl extends HertsClientStreamingService<TestClientStreamingRpcService> implements TestClientStreamingRpcService {
    @Override
    public StreamObserver<String> test01(StreamObserver<String> responseObserver) {
        return new StreamObserver<>() {
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
        return new StreamObserver<TestHoo>() {
            @Override
            public void onNext(TestHoo value) {
            }

            @Override
            public void onError(Throwable t) {
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext();
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public StreamObserver<Map<String, String>> test03(StreamObserver<String> responseObserver) {
        return null;
    }
}
