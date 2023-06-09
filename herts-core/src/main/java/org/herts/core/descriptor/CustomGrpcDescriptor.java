package org.herts.core.descriptor;

import org.herts.core.marshaller.CustomGrpcMarshaller;
import org.herts.core.context.HertsType;
import org.herts.core.modelx.RegisteredMethod;
import org.herts.core.marshaller.CustomGrpcStramingMarshaller;

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
public class CustomGrpcDescriptor {
    private static final MethodDescriptor.Marshaller<byte[]> reqMessageMarshaller = new CustomGrpcMarshaller();
    private static final MethodDescriptor.Marshaller<byte[]> resMessageMarshaller = new CustomGrpcMarshaller();
    private static final MethodDescriptor.Marshaller<Object> resStreamingMessageMarshaller = new CustomGrpcStramingMarshaller();

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
    public static CustomGrpcUnaryDescriptor generateGrpcDescriptor(String serviceName, List<RegisteredMethod> hertsMethods) {
        ServiceDescriptor.Builder builder = ServiceDescriptor.newBuilder(serviceName);
        List<MethodDescriptor<byte[], byte[]>> methodDescriptors = new ArrayList<>();

        for (RegisteredMethod hertsMethod : hertsMethods) {
            MethodDescriptor<byte[], byte[]> method = generateMethodDescriptor(
                    hertsMethod.getHertsCoreType(), serviceName, hertsMethod.getMethodName());

            methodDescriptors.add(method);
            builder.addMethod(method);
        }

        return CustomGrpcUnaryDescriptor.createGrpcDescriptor(builder.build(), methodDescriptors);
    }

    /**
     * Generate gRPC client, server, bidirectional streaming descriptor.
     *
     * @param serviceName  Interface service name
     * @param hertsMethods HertMethod class list
     * @return HertsStreamingDescriptor
     */
    public static CustomGrpcStreamingDescriptor generateStreamingGrpcDescriptor(String serviceName, List<RegisteredMethod> hertsMethods) {
        ServiceDescriptor.Builder builder = ServiceDescriptor.newBuilder(serviceName);
        List<MethodDescriptor<Object, Object>> methodDescriptors = new ArrayList<>();

        for (RegisteredMethod hertsMethod : hertsMethods) {
            MethodDescriptor<Object, Object> method = generateStramingMethodDescriptor(
                    hertsMethod.getHertsCoreType(), serviceName, hertsMethod.getMethodName());

            methodDescriptors.add(method);
            builder.addMethod(method);
        }

        return CustomGrpcStreamingDescriptor.createGrpcDescriptor(builder.build(), methodDescriptors);
    }
}
