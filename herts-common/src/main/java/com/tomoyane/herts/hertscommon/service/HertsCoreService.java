package com.tomoyane.herts.hertscommon.service;

import com.tomoyane.herts.hertscommon.context.HertsCoreType;

import io.grpc.MethodDescriptor;
import io.grpc.stub.StreamObserver;

public interface HertsCoreService {
    HertsCoreType getHertsCoreType();

    MethodDescriptor.MethodType getGrpcMethodType();

    String[] getConnections();
}
