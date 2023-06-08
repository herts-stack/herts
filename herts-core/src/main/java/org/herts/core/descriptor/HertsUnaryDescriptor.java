package org.herts.core.descriptor;

import io.grpc.MethodDescriptor;
import io.grpc.ServiceDescriptor;

import java.util.List;

/**
 * Herts gRPC unary descriptor.
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsUnaryDescriptor {
    private ServiceDescriptor serviceDescriptor;
    private List<MethodDescriptor<byte[], byte[]>> methodDescriptors;

    public HertsUnaryDescriptor() {
    }

    private HertsUnaryDescriptor(ServiceDescriptor serviceDescriptor, List<MethodDescriptor<byte[], byte[]>> methodDescriptors) {
        this.serviceDescriptor = serviceDescriptor;
        this.methodDescriptors = methodDescriptors;
    }

    /**
     * Factory method.
     *
     * @param serviceDescriptor HertsService descriptor
     * @param methodDescriptors HertsService method descriptor
     * @return HertsUnaryDescriptor
     */
    public static HertsUnaryDescriptor createGrpcDescriptor(ServiceDescriptor serviceDescriptor, List<MethodDescriptor<byte[], byte[]>> methodDescriptors) {
        return new HertsUnaryDescriptor(serviceDescriptor, methodDescriptors);
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
