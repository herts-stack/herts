package org.herts.core.marshaller;

import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertNull;

public class HertsStreamingMarshallerTest {

    @Test
    public void streamIfNull() {
        HertsStramingMarshaller marshaller = new HertsStramingMarshaller();
        InputStream stream = marshaller.stream(null);
        assertNull(stream);
    }

    @Test
    public void parseIfNull() {
        HertsStramingMarshaller marshaller = new HertsStramingMarshaller();
        Object parsedData = marshaller.parse(null);
        assertNull(parsedData);
    }
}
