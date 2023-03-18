package com.tomoyane.herts.hertscommon.mapping;

import io.grpc.MethodDescriptor;
import io.grpc.ServiceDescriptor;

import java.util.List;

public class HertsDescriptor {
    private ServiceDescriptor serviceDescriptor;
    private List<MethodDescriptor<byte[], byte[]>> methodDescriptors;

    public HertsDescriptor() {
    }

    private HertsDescriptor(ServiceDescriptor serviceDescriptor, List<MethodDescriptor<byte[], byte[]>> methodDescriptors) {
        this.serviceDescriptor = serviceDescriptor;
        this.methodDescriptors = methodDescriptors;
    }

    public static HertsDescriptor createGrpcDescriptor(ServiceDescriptor serviceDescriptor, List<MethodDescriptor<byte[], byte[]>> methodDescriptors) {
        return new HertsDescriptor(serviceDescriptor, methodDescriptors);
    }

    public ServiceDescriptor getServiceDescriptor() {
        return serviceDescriptor;
    }

    public void setServiceDescriptor(ServiceDescriptor serviceDescriptor) {
        this.serviceDescriptor = serviceDescriptor;
    }

    public List<MethodDescriptor<byte[], byte[]>> getMethodDescriptors() {
        return methodDescriptors;
    }

    public void setMethodDescriptors(List<MethodDescriptor<byte[], byte[]>> methodDescriptors) {
        this.methodDescriptors = methodDescriptors;
    }
}
