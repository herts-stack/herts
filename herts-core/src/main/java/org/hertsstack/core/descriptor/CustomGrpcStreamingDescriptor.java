package org.hertsstack.core.descriptor;

import io.grpc.MethodDescriptor;
import io.grpc.ServiceDescriptor;

import java.util.List;

/**
 * Herts gRPC streaming descriptor.
 *
 * @author Herts Contributer
 */
public class CustomGrpcStreamingDescriptor {
    private ServiceDescriptor serviceDescriptor;
    private List<MethodDescriptor<Object, Object>> methodDescriptors;

    public CustomGrpcStreamingDescriptor() {
    }

    private CustomGrpcStreamingDescriptor(ServiceDescriptor serviceDescriptor, List<MethodDescriptor<Object, Object>> methodDescriptors) {
        this.serviceDescriptor = serviceDescriptor;
        this.methodDescriptors = methodDescriptors;
    }

    /**
     * Factory method.
     *
     * @param serviceDescriptor HertsService descriptor
     * @param methodDescriptors HertsService method descriptor
     * @return HertsStreamingDescriptor
     */
    public static CustomGrpcStreamingDescriptor createGrpcDescriptor(ServiceDescriptor serviceDescriptor, List<MethodDescriptor<Object, Object>> methodDescriptors) {
        return new CustomGrpcStreamingDescriptor(serviceDescriptor, methodDescriptors);
    }

    public ServiceDescriptor getServiceDescriptor() {
        return serviceDescriptor;
    }

    public void setServiceDescriptor(ServiceDescriptor serviceDescriptor) {
        this.serviceDescriptor = serviceDescriptor;
    }

    public List<MethodDescriptor<Object, Object>> getMethodDescriptors() {
        return methodDescriptors;
    }

    public void setMethodDescriptors(List<MethodDescriptor<Object, Object>> methodDescriptors) {
        this.methodDescriptors = methodDescriptors;
    }
}
