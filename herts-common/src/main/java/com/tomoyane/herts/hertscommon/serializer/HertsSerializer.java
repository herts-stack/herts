package com.tomoyane.herts.hertscommon.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.msgpack.jackson.dataformat.MessagePackFactory;

import java.io.IOException;

public class HertsSerializer {
    private static final ObjectMapper msgPackMapper = new ObjectMapper(new MessagePackFactory());
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private HertsSerializeType serializeType = HertsSerializeType.MessagePack;

    public HertsSerializer() {
    }

    public HertsSerializer(HertsSerializeType serializeType) {
        this.serializeType = serializeType;
    }

    public <T> byte[] serialize(T messageClass) throws JsonProcessingException {
        if (this.serializeType == HertsSerializeType.Json) {
            return objectMapper.writeValueAsBytes(messageClass);
        } else {
            return msgPackMapper.writeValueAsBytes(messageClass);
        }
    }

    public <T> T deserialize(byte[] message, Class<T> classType) throws IOException {
        if (this.serializeType == HertsSerializeType.Json) {
            return objectMapper.readValue(message, classType);
        } else {
            return msgPackMapper.readValue(message, classType);
        }
    }

    public <T> T convert(Object message, Class<T> classType) {
        if (this.serializeType == HertsSerializeType.Json) {
            return objectMapper.convertValue(message, classType);
        } else {
            return msgPackMapper.convertValue(message, classType);
        }
    }
}
