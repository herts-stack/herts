package org.herts.common.marshaller;

import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertNull;

public class HertsMarshallerTest {

    @Test
    public void streamIfNull() {
        HertsMarshaller marshaller = new HertsMarshaller();
        InputStream stream = marshaller.stream(null);
        assertNull(stream);
    }

    @Test
    public void parseIfNull() {
        HertsMarshaller marshaller = new HertsMarshaller();
        byte[] parsedData = marshaller.parse(null);
        assertNull(parsedData);
    }
}
