package org.herts.core.marshaller;

import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertNull;

public class HertsMarshallerTest {

    @Test
    public void streamIfNull() {
        CustomGrpcMarshaller marshaller = new CustomGrpcMarshaller();
        InputStream stream = marshaller.stream(null);
        assertNull(stream);
    }

    @Test
    public void parseIfNull() {
        CustomGrpcMarshaller marshaller = new CustomGrpcMarshaller();
        byte[] parsedData = marshaller.parse(null);
        assertNull(parsedData);
    }
}
