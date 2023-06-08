package org.herts.core.descriptor;

import org.herts.core.marshaller.HertsMarshaller;
import org.herts.core.context.HertsType;
import org.herts.core.modelx.HertsMethod;
import org.herts.core.marshaller.HertsStramingMarshaller;

import io.grpc.MethodDescriptor;
import io.grpc.ServiceDescriptor;

import java.util.ArrayList;
import java.util.List;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * Herts gRPC custom static descriptor.
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsGrpcDescriptor {
    private static final MethodDescriptor.Marshaller<byte[]> reqMessageMarshaller = new HertsMarshaller();
    private static final MethodDescriptor.Marshaller<byte[]> resMessageMarshaller = new HertsMarshaller();
    private static final MethodDescriptor.Marshaller<Object> resStreamingMessageMarshaller = new HertsStramingMarshaller();

    /**
     * Generate method descriptor.
     *
     * @param coreType    HertsCoreType
     * @param serviceName Interface service name
     * @param methodName  Rpc name
     * @return MethodDescriptor
     */
    public static MethodDescriptor<byte[], byte[]> generateMethodDescriptor(
            HertsType coreType,
            String serviceName,
            String methodName) {

        MethodDescriptor.MethodType methodType = coreType.convertToMethodType();
        MethodDescriptor.Builder<byte[], byte[]> builder = MethodDescriptor.<byte[], byte[]>newBuilder()
                .setType(methodType)
                .setFullMethodName(generateFullMethodName(serviceName, methodName))
                .setSampledToLocalTracing(true)
                .setRequestMarshaller(reqMessageMarshaller)
                .setResponseMarshaller(resMessageMarshaller);

        return builder.build();
    }

    /**
     * Generate straeming method descriptor
     *
     * @param coreType    HertsType
     * @param serviceName Service name
     * @param methodName  Method name
     * @return MethodDescriptor
     */
    public static MethodDescriptor<Object, Object> generateStramingMethodDescriptor(
            HertsType coreType,
            String serviceName,
            String methodName) {

        MethodDescriptor.MethodType methodType = coreType.convertToMethodType();
        MethodDescriptor.Builder<Object, Object> builder = MethodDescriptor.<Object, Object>newBuilder()
                .setType(methodType)
                .setFullMethodName(generateFullMethodName(serviceName, methodName))
                .setSampledToLocalTracing(true)
                .setRequestMarshaller(resStreamingMessageMarshaller)
                .setResponseMarshaller(resStreamingMessageMarshaller);

        return builder.build();
    }

    /**
     * Generate gRPC unary descriptor.
     *
     * @param serviceName  Interface service name
     * @param hertsMethods HertMethod class list
     * @return HertsDescriptor
     */
    public static HertsUnaryDescriptor generateGrpcDescriptor(String serviceName, List<HertsMethod> hertsMethods) {
        ServiceDescriptor.Builder builder = ServiceDescriptor.newBuilder(serviceName);
        List<MethodDescriptor<byte[], byte[]>> methodDescriptors = new ArrayList<>();

        for (HertsMethod hertsMethod : hertsMethods) {
            MethodDescriptor<byte[], byte[]> method = generateMethodDescriptor(
                    hertsMethod.getHertsCoreType(), serviceName, hertsMethod.getMethodName());

            methodDescriptors.add(method);
            builder.addMethod(method);
        }

        return HertsUnaryDescriptor.createGrpcDescriptor(builder.build(), methodDescriptors);
    }

    /**
     * Generate gRPC client, server, bidirectional streaming descriptor.
     *
     * @param serviceName  Interface service name
     * @param hertsMethods HertMethod class list
     * @return HertsStreamingDescriptor
     */
    public static HertsStreamingDescriptor generateStreamingGrpcDescriptor(String serviceName, List<HertsMethod> hertsMethods) {
        ServiceDescriptor.Builder builder = ServiceDescriptor.newBuilder(serviceName);
        List<MethodDescriptor<Object, Object>> methodDescriptors = new ArrayList<>();

        for (HertsMethod hertsMethod : hertsMethods) {
            MethodDescriptor<Object, Object> method = generateStramingMethodDescriptor(
                    hertsMethod.getHertsCoreType(), serviceName, hertsMethod.getMethodName());

            methodDescriptors.add(method);
            builder.addMethod(method);
        }

        return HertsStreamingDescriptor.createGrpcDescriptor(builder.build(), methodDescriptors);
    }
}
