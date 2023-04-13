package com.tomoyane.herts.hertscommon.service;

import io.grpc.stub.StreamObserver;

public interface HertsClientService {
    <T> void received(StreamObserver<T> response, Object obj);
}
