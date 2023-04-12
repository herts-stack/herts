package com.tomoyane.herts.hertscommon.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.msgpack.jackson.dataformat.MessagePackFactory;

import java.io.IOException;
import java.io.Reader;

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

    public String serializeAsStr(Object value) throws JsonProcessingException {
        if (this.serializeType == HertsSerializeType.Json) {
            return objectMapper.writeValueAsString(value);
        } else {
            return msgPackMapper.writeValueAsString(value);
        }
    }

    public <T> T deserialize(byte[] message, Class<T> classType) throws IOException {
        if (this.serializeType == HertsSerializeType.Json) {
            return objectMapper.readValue(message, classType);
        } else {
            return msgPackMapper.readValue(message, classType);
        }
    }

    public <T> T deserialize(byte[] message, TypeReference typeReference) throws IOException {
        if (this.serializeType == HertsSerializeType.Json) {
            return objectMapper.readValue(message, typeReference);
        } else {
            return msgPackMapper.readValue(message, typeReference);
        }
    }

    public <T> T deserialize(Reader src, Class<T> classType) throws IOException {
        if (this.serializeType == HertsSerializeType.Json) {
            return objectMapper.readValue(src, classType);
        } else {
            return msgPackMapper.readValue(src, classType);
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
