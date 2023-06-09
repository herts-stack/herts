package org.herts.core.context;

import io.grpc.MethodDescriptor;
import org.herts.core.exception.TypeInvalidException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class HertsTypeTest {
    @Test
    public void convertToMethodType() {
        HertsType hertsType = HertsType.Unary;
        MethodDescriptor.MethodType methodType = hertsType.convertToMethodType();
        assertEquals(MethodDescriptor.MethodType.UNARY, methodType);

        hertsType = HertsType.ClientStreaming;
        methodType = hertsType.convertToMethodType();
        assertEquals(MethodDescriptor.MethodType.CLIENT_STREAMING, methodType);

        hertsType = HertsType.ServerStreaming;
        methodType = hertsType.convertToMethodType();
        assertEquals(MethodDescriptor.MethodType.SERVER_STREAMING, methodType);

        hertsType = HertsType.BidirectionalStreaming;
        methodType = hertsType.convertToMethodType();
        assertEquals(MethodDescriptor.MethodType.BIDI_STREAMING, methodType);

        hertsType = HertsType.Http;
        HertsType finalHertsType = hertsType;
        assertThrows(TypeInvalidException.class, finalHertsType::convertToMethodType);
    }
}
