package org.herts.common.marshaller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

public class HertsStreamingMarshallerTest {

    @Test
    public void streamIfNull() {
        var marshaller = new HertsStramingMarshaller();
        var stream = marshaller.stream(null);
        assertNull(stream);
    }

    @Test
    public void parseIfNull() {
        var marshaller = new HertsStramingMarshaller();
        var parsedData = marshaller.parse(null);
        assertNull(parsedData);
    }
}
