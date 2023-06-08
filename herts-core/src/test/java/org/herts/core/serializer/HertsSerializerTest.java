package org.herts.core.serializer;

import org.herts.core.modelx.HertsHttpRequest;
import org.herts.core.modelx.HertsHttpMsg;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        List<HertsHttpMsg> payloads = new ArrayList<>();
        HertsHttpMsg payload = new HertsHttpMsg();
        float value = 0.01f;
        payload.setClassInfo(float.class.getName());
        payload.setValue(value);
        payload.setKeyName("key");
        payloads.add(payload);

        HertsHttpRequest hertsHttpRequest = new HertsHttpRequest();
        hertsHttpRequest.setPayloads(payloads);

        String serializedData = this.hertsJsonSerializer.serializeAsStr(hertsHttpRequest);
        String expect = "{\"payloads\":[{\"keyName\":\"key\",\"value\":0.01,\"classInfo\":\"float\"}]}";
        assertEquals(expect, serializedData);
    }
}
