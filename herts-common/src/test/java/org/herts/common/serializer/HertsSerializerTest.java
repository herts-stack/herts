package org.herts.common.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.herts.common.context.HertsHttpRequest;
import org.herts.common.context.Payload;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HertsSerializerTest {
    public final HertsSerializer hertsJsonSerializer;
    public final HertsSerializer hertsMsgSerializer;

    public HertsSerializerTest() {
        this.hertsJsonSerializer = new HertsSerializer(HertsSerializeType.Json);
        this.hertsMsgSerializer = new HertsSerializer(HertsSerializeType.MessagePack);
    }

    @Test
    public void serializeAsStr() throws IOException {
        List<Payload> payloads = new ArrayList<>();
        Payload payload = new Payload();
        float value = 0.01f;
        payload.setClassInfo(float.class.getName());
        payload.setValue(value);
        payload.setKeyName("key");
        payloads.add(payload);

        HertsHttpRequest hertsHttpRequest = new HertsHttpRequest();
        hertsHttpRequest.setPayloads(payloads);

        var serializedData = this.hertsJsonSerializer.serializeAsStr(hertsHttpRequest);
        var expect = "{\"payloads\":[{\"keyName\":\"key\",\"value\":0.01,\"classInfo\":\"float\"}]}";
        assertEquals(expect, serializedData);
    }
}
