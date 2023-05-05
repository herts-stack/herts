package org.herts.common.descriptor;

import org.herts.common.context.HertsType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HertsGrpcDescriptorTest {

    @Test
    public void generateMethodDescriptor() {
        var coreType = HertsType.Unary;
        var serviceName = "testRpcService";
        var methodName = "getFoo";
        var descriptor = HertsGrpcDescriptor.generateMethodDescriptor(coreType, serviceName, methodName);

        assertEquals(serviceName + "/" + methodName, descriptor.getFullMethodName());
    }

    @Test
    public void generateStreamingMethodDescriptor() {
        var coreType = HertsType.Unary;
        var serviceName = "testRpcService";
        var methodName = "getFoo";
        var descriptor = HertsGrpcDescriptor.generateStramingMethodDescriptor(coreType, serviceName, methodName);

        assertEquals(serviceName + "/" + methodName, descriptor.getFullMethodName());
    }
}
