package org.hertsstack.core.descriptor;

import io.grpc.MethodDescriptor;
import org.hertsstack.core.context.HertsType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HertsGrpcDescriptorTest {

    @Test
    public void generateMethodDescriptor() {
        HertsType coreType = HertsType.Unary;
        String serviceName = "testRpcService";
        String methodName = "getFoo";
        MethodDescriptor<byte[], byte[]> descriptor = CustomGrpcDescriptor.generateMethodDescriptor(coreType, serviceName, methodName);

        assertEquals(serviceName + "/" + methodName, descriptor.getFullMethodName());
    }

    @Test
    public void generateStreamingMethodDescriptor() {
        HertsType coreType = HertsType.Unary;
        String serviceName = "testRpcService";
        String methodName = "getFoo";
        MethodDescriptor<Object, Object> descriptor = CustomGrpcDescriptor.generateStramingMethodDescriptor(coreType, serviceName, methodName);

        assertEquals(serviceName + "/" + methodName, descriptor.getFullMethodName());
    }
}
