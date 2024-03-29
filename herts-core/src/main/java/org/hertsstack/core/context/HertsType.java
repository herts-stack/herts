package org.hertsstack.core.context;

import org.hertsstack.core.exception.TypeInvalidException;

import io.grpc.MethodDescriptor;

/**
 * Herts support type
 *
 * @author Herts Contributer
 */
public enum HertsType {
    Unary(1),
    ServerStreaming(2),
    ClientStreaming(3),
    BidirectionalStreaming(4),
    Reactive(5),
    Http(6);

    private final int id;

    HertsType(final int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    /**
     * Convert from enum to io.grpc.MethodDescriptor if exist
     *
     * @return MethodDescriptor.MethodType
     */
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
        } else if (id == Reactive.getId()) {
            methodType = MethodDescriptor.MethodType.BIDI_STREAMING;
        } else {
            throw new TypeInvalidException("gRPC Herts type is invalid");
        }
        return methodType;
    }
}
