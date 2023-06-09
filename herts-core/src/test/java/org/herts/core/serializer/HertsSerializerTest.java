package org.herts.core.serializer;

import org.herts.core.modelx.InternalHttpRequest;
import org.herts.core.modelx.InternalHttpMsg;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HertsSerializerTest {
    public final MessageSerializer hertsJsonSerializer;
    public final MessageSerializer hertsMsgSerializer;

    public HertsSerializerTest() {
        this.hertsJsonSerializer = new MessageSerializer(MessageSerializeType.Json);
        this.hertsMsgSerializer = new MessageSerializer(MessageSerializeType.MessagePack);
    }

    @Test
    public void serializeAsStr() throws IOException {
        List<InternalHttpMsg> payloads = new ArrayList<>();
        InternalHttpMsg payload = new InternalHttpMsg();
        float value = 0.01f;
        payload.setClassInfo(float.class.getName());
        payload.setValue(value);
        payload.setKeyName("key");
        payloads.add(payload);

        InternalHttpRequest hertsHttpRequest = new InternalHttpRequest();
        hertsHttpRequest.setPayloads(payloads);

        String serializedData = this.hertsJsonSerializer.serializeAsStr(hertsHttpRequest);
        String expect = "{\"payloads\":[{\"keyName\":\"key\",\"value\":0.01,\"classInfo\":\"float\"}]}";
        assertEquals(expect, serializedData);
    }
}
