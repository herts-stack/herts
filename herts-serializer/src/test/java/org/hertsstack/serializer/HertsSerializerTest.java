package org.hertsstack.serializer;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HertsSerializerTest {
    public final MessageSerializer hertsJsonSerializer;
    public final MessageSerializer hertsMsgSerializer;

    public HertsSerializerTest() {
        this.hertsJsonSerializer = new MessageSerializer(MessageSerializeType.Json);
        this.hertsMsgSerializer = new MessageSerializer(MessageSerializeType.MessagePack);
    }

    @Test
    public void serializeAsStr() throws IOException {
        List<TestMsg> payloads = new ArrayList<>();
        TestMsg payload = new TestMsg();
        float value = 0.01f;
        payload.setClassInfo(float.class.getName());
        payload.setValue(value);
        payload.setKeyName("key");
        payloads.add(payload);

        TestRequest hertsHttpRequest = new TestRequest();
        hertsHttpRequest.setPayloads(payloads);

        String serializedData = this.hertsJsonSerializer.serializeAsStr(hertsHttpRequest);
        String expect = "{\"payloads\":[{\"keyName\":\"key\",\"value\":0.01,\"classInfo\":\"float\"}]}";
        assertEquals(expect, serializedData);
    }
}
