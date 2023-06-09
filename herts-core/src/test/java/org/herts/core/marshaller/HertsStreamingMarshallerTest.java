package org.herts.core.marshaller;

import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertNull;

public class HertsStreamingMarshallerTest {

    @Test
    public void streamIfNull() {
        CustomGrpcStramingMarshaller marshaller = new CustomGrpcStramingMarshaller();
        InputStream stream = marshaller.stream(null);
        assertNull(stream);
    }

    @Test
    public void parseIfNull() {
        CustomGrpcStramingMarshaller marshaller = new CustomGrpcStramingMarshaller();
        Object parsedData = marshaller.parse(null);
        assertNull(parsedData);
    }
}
