package org.herts.core.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.herts.core.exception.MessageJsonParsingException;
import org.msgpack.jackson.dataformat.MessagePackFactory;

import java.io.IOException;
import java.io.Reader;

/**
 * Herts message serializer
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class MessageSerializer {
    private static final ObjectMapper msgPackMapper = new ObjectMapper(new MessagePackFactory());
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private MessageSerializeType serializeType = MessageSerializeType.MessagePack;

    public MessageSerializer() {
    }

    /**
     * Constructor
     *
     * @param serializeType Supported type. Json or MessagePack
     */
    public MessageSerializer(MessageSerializeType serializeType) {
        this.serializeType = serializeType;
    }

    /**
     * Get HertsSerializeType
     *
     * @return HertsSerializeType
     */
    public MessageSerializeType getSerializeType() {
        return this.serializeType;
    }

    /**
     * Serialize from Class to byte array
     *
     * @param messageClass Class tyoe
     * @param <T>          Generics
     * @return Byte array
     * @throws MessageJsonParsingException Invalid message
     */
    public <T> byte[] serialize(T messageClass) throws MessageJsonParsingException {
        try {
            if (this.serializeType == MessageSerializeType.Json) {
                return objectMapper.writeValueAsBytes(messageClass);
            } else {
                return msgPackMapper.writeValueAsBytes(messageClass);
            }
        } catch (JsonProcessingException ex) {
            throw new MessageJsonParsingException(ex);
        }
    }

    /**
     * Serialize from Object to String
     *
     * @param value Oject
     * @return String data
     * @throws MessageJsonParsingException Invalid message
     */
    public String serializeAsStr(Object value) throws MessageJsonParsingException {
        try {
            if (this.serializeType == MessageSerializeType.Json) {
                return objectMapper.writeValueAsString(value);
            } else {
                return msgPackMapper.writeValueAsString(value);
            }
        } catch (JsonProcessingException ex) {
            throw new MessageJsonParsingException(ex);
        }
    }

    /**
     * Deserialize from byte array to Class type
     *
     * @param message   Message byte
     * @param classType Class type
     * @param <T>       Generics
     * @return Deserialize class type
     * @throws IOException Invalid message
     */
    public <T> T deserialize(byte[] message, Class<T> classType) throws IOException {
        if (this.serializeType == MessageSerializeType.Json) {
            return objectMapper.readValue(message, classType);
        } else {
            return msgPackMapper.readValue(message, classType);
        }
    }

    /**
     * Deserialize from byte array
     *
     * @param message       Message byte
     * @param typeReference TypeReference
     * @param <T>           Generics
     * @return Deserialize class type
     * @throws IOException Invalid message
     */
    public <T> T deserialize(byte[] message, TypeReference typeReference) throws IOException {
        if (this.serializeType == MessageSerializeType.Json) {
            return objectMapper.readValue(message, typeReference);
        } else {
            return msgPackMapper.readValue(message, typeReference);
        }
    }

    /**
     * Deserialize from Reader
     *
     * @param src       Reader data
     * @param classType Class type
     * @param <T>       Generics
     * @return Deserialize class type
     * @throws IOException Invalid message
     */
    public <T> T deserialize(Reader src, Class<T> classType) throws IOException {
        if (this.serializeType == MessageSerializeType.Json) {
            return objectMapper.readValue(src, classType);
        } else {
            return msgPackMapper.readValue(src, classType);
        }
    }

    /**
     * Convert from Object to Class type
     *
     * @param message   Message object
     * @param classType Class type
     * @param <T>       Generics
     * @return Converted data
     */
    public <T> T convert(Object message, Class<T> classType) {
        if (this.serializeType == MessageSerializeType.Json) {
            return objectMapper.convertValue(message, classType);
        } else {
            return msgPackMapper.convertValue(message, classType);
        }
    }

    /**
     * Convert to Object from value of HertsHttpRequest.Payload
     *
     * @param hertsHttpPayloadValue Value of HertsHttpRequest.Payload
     * @param aClass                Class type
     * @return Object
     * @throws IOException If fail deserialize
     */
    public Object convertFromHertHttpPayload(Object hertsHttpPayloadValue, Class<?> aClass) throws IOException {
        if (aClass.isInstance(hertsHttpPayloadValue)) {
            return aClass.cast(hertsHttpPayloadValue);
        } else {
            return deserialize(serializeAsStr(hertsHttpPayloadValue).getBytes(), aClass);
        }
    }
}
