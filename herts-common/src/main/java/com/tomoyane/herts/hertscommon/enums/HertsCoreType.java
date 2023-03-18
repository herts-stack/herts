package com.tomoyane.herts.hertscommon.enums;

import com.tomoyane.herts.hertscommon.exception.HertsCoreTypeInvalidException;
import io.grpc.MethodDescriptor;

public enum HertsCoreType {
    Unary(1),
    ServerStreaming(2),
    ClientStreaming(3),
    BidirectionalStreaming(4),
    Http(5);

    private final int id;

    HertsCoreType(final int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public MethodDescriptor.MethodType convertToMethodType() {
        MethodDescriptor.MethodType methodType;
        if (id == Unary.getId()) {
            methodType = MethodDescriptor.MethodType.UNARY;
        } else if (id == ClientStreaming.getId()) {
            methodType = MethodDescriptor.MethodType.CLIENT_STREAMING;
        } else if (id == ServerStreaming.getId()) {
            methodType = MethodDescriptor.MethodType.SERVER_STREAMING;
        } else if (id == BidirectionalStreaming.getId()) {
            methodType = MethodDescriptor.MethodType.BIDI_STREAMING;
        } else {
            throw new HertsCoreTypeInvalidException("gRPC Herts type is invalid");
        }
        return methodType;
    }

}
