package com.tomoyane.herts.hertscommon.service;

import com.tomoyane.herts.hertscommon.context.HertsCoreType;

import io.grpc.MethodDescriptor;

public interface HertsService {
    HertsCoreType getHertsCoreType();

    MethodDescriptor.MethodType getGrpcMethodType();

    String[] getConnections();
}
