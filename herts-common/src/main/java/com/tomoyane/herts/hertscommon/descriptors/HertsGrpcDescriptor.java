package com.tomoyane.herts.hertscommon.descriptors;

import com.tomoyane.herts.hertscommon.mapping.*;

import io.grpc.MethodDescriptor;
import io.grpc.ServiceDescriptor;

import java.util.ArrayList;
import java.util.List;

import static io.grpc.MethodDescriptor.generateFullMethodName;

public class HertsGrpcDescriptor {
    private static final MethodDescriptor.Marshaller<byte[]> reqMessageMarshaller = new HertsMarshaller();
    private static final MethodDescriptor.Marshaller<byte[]> resMessageMarshaller = new HertsMarshaller();

    public static MethodDescriptor<byte[], byte[]> generateMethodDescriptor(
            MethodDescriptor.MethodType methodType,
            String serviceName,
            String methodName) {

        MethodDescriptor.Builder<byte[], byte[]> builder = MethodDescriptor.<byte[], byte[]>newBuilder()
                .setType(methodType)
                .setFullMethodName(generateFullMethodName(serviceName, methodName))
                .setSampledToLocalTracing(true)
                .setRequestMarshaller(reqMessageMarshaller)
                .setResponseMarshaller(resMessageMarshaller);

        return builder.build();
    }

    public static <T> ServiceDescriptor getServiceDescriptor(MethodInfo methodInfo) {
//        ServiceDescriptor.Builder builder = ServiceDescriptor.newBuilder(methodInfo.getServiceName());
//        // .setSchemaDescriptor(new GreeterFileDescriptorSupplier())
//
//        int index = 0;
//        for (var methodName : methodInfo.getMethodNames()) {
//            Class<?> returnType = methodInfo.getMethodReturnTypes()[index];
//            MethodDescriptor<HertsMessage, T> method = generateMethodDescriptor(
//                    methodInfo.getMethodType(), methodInfo.getServiceName(), methodName);
//
//            builder.addMethod(method);
//        }
//        return builder.build();
        return null;
    }

    public static HertsDescriptor getGrpcDescriptor(String serviceName, List<HertsMethod> hertsMethods) {
        ServiceDescriptor.Builder builder = ServiceDescriptor.newBuilder(serviceName);
        List<MethodDescriptor<byte[], byte[]>> methodDescriptors = new ArrayList<>();

        for (var hertsMethod : hertsMethods) {
            MethodDescriptor<byte[], byte[]> method = generateMethodDescriptor(
                    hertsMethod.getMethodType(), serviceName, hertsMethod.getMethodName());

            methodDescriptors.add(method);
            builder.addMethod(method);
        }

        return HertsDescriptor.createGrpcDescriptor(builder.build(), methodDescriptors);
    }

    public static Class<?> convertToClass(String classType) {
        return String.class;
    }
}
