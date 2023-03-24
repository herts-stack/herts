package com.tomoyane.herts.hertscommon.marshaller;

import com.tomoyane.herts.hertscommon.logger.HertsLogger;
import io.grpc.MethodDescriptor;

import java.io.*;
import java.util.logging.Logger;

public class HertsStramingMarshaller implements MethodDescriptor.Marshaller<Object> {
    private static final Logger logger = HertsLogger.getLogger(HertsStramingMarshaller.class.getSimpleName());
    private final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    public HertsStramingMarshaller() {
    }

    @Override
    public InputStream stream(Object value) {
        if (value == null) {
            return null;
        }

        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(this.byteArrayOutputStream);
            oos.writeObject(value);
            oos.flush();
            oos.close();
            return new ByteArrayInputStream(this.byteArrayOutputStream.toByteArray());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("ER=============", ex);
        }
    }

    @Override
    public Object parse(InputStream stream) {
        if (stream == null) {
            return null;
        }
        try {
            ObjectInputStream ois = new ObjectInputStream(stream);
            return ois.readObject();
        } catch (ClassNotFoundException e) {
            logger.warning("Failed to parse message. " + e.getMessage());
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
