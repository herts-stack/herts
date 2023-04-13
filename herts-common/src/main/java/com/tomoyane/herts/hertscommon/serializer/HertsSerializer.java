package com.tomoyane.herts.hertscommon.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.msgpack.jackson.dataformat.MessagePackFactory;

import java.io.IOException;
import java.io.Reader;

/**
 * Herts message serializer
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsSerializer {
    private static final ObjectMapper msgPackMapper = new ObjectMapper(new MessagePackFactory());
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private HertsSerializeType serializeType = HertsSerializeType.MessagePack;

    public HertsSerializer() {
    }

    /**
     * Constructor
     * @param serializeType Supported type. Json or MessagePack
     */
    public HertsSerializer(HertsSerializeType serializeType) {
        this.serializeType = serializeType;
    }

    /**
     * Serialize from Class to byte array
     * @param messageClass Class tyoe
     * @return Byte array
     * @param <T> Generics
     * @throws JsonProcessingException Invalid message
     */
    public <T> byte[] serialize(T messageClass) throws JsonProcessingException {
        if (this.serializeType == HertsSerializeType.Json) {
            return objectMapper.writeValueAsBytes(messageClass);
        } else {
            return msgPackMapper.writeValueAsBytes(messageClass);
        }
    }

    /**
     * Serialize from Object to String
     * @param value Oject
     * @return String data
     * @throws JsonProcessingException Invalid message
     */
    public String serializeAsStr(Object value) throws JsonProcessingException {
        if (this.serializeType == HertsSerializeType.Json) {
            return objectMapper.writeValueAsString(value);
        } else {
            return msgPackMapper.writeValueAsString(value);
        }
    }

    /**
     * Deserialize from byte array to Class type
     * @param message Message byte
     * @param classType Class type
     * @return Deserialize class type
     * @param <T> Generics
     * @throws IOException Invalid message
     */
    public <T> T deserialize(byte[] message, Class<T> classType) throws IOException {
        if (this.serializeType == HertsSerializeType.Json) {
            return objectMapper.readValue(message, classType);
        } else {
            return msgPackMapper.readValue(message, classType);
        }
    }

    /**
     * Deserialize from byte array
     * @param message Message byte
     * @param typeReference TypeReference
     * @return Deserialize class type
     * @param <T> Generics
     * @throws IOException Invalid message
     */
    public <T> T deserialize(byte[] message, TypeReference typeReference) throws IOException {
        if (this.serializeType == HertsSerializeType.Json) {
            return objectMapper.readValue(message, typeReference);
        } else {
            return msgPackMapper.readValue(message, typeReference);
        }
    }

    /**
     * Deserialize from Reader
     * @param src Reader data
     * @param classType Class type
     * @return Deserialize class type
     * @param <T> Generics
     * @throws IOException Invalid message
     */
    public <T> T deserialize(Reader src, Class<T> classType) throws IOException {
        if (this.serializeType == HertsSerializeType.Json) {
            return objectMapper.readValue(src, classType);
        } else {
            return msgPackMapper.readValue(src, classType);
        }
    }

    /**
     * Convert from Object to Class type
     * @param message Message object
     * @param classType Class type
     * @return Converted data
     * @param <T> Generics
     */
    public <T> T convert(Object message, Class<T> classType) {
        if (this.serializeType == HertsSerializeType.Json) {
            return objectMapper.convertValue(message, classType);
        } else {
            return msgPackMapper.convertValue(message, classType);
        }
    }
}
