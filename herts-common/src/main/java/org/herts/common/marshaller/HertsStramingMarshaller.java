package org.herts.common.marshaller;

import org.herts.common.logger.HertsLogger;
import org.herts.common.exception.HertsMessageException;

import io.grpc.MethodDescriptor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Logger;

/**
 * Herts grpc steaming marshaller
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsStramingMarshaller implements MethodDescriptor.Marshaller<Object> {
    private static final Logger logger = HertsLogger.getLogger(HertsStramingMarshaller.class.getSimpleName());

    public HertsStramingMarshaller() {
    }

    @Override
    public InputStream stream(Object value) {
        if (value == null) {
            return null;
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(byteArrayOutputStream);
            oos.writeObject(value);
            oos.close();
            ByteArrayInputStream data = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            byteArrayOutputStream.close();
            return data;
        } catch (Exception ex) {
            throw new HertsMessageException(ex);
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
        } catch (ClassNotFoundException | IOException ex) {
            throw new HertsMessageException(ex);
        }
    }
}
