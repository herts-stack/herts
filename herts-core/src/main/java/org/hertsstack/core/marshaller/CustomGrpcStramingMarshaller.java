package org.hertsstack.core.marshaller;

import org.hertsstack.core.exception.StreamParsingException;
import org.hertsstack.core.logger.Logging;

import io.grpc.MethodDescriptor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Herts grpc steaming marshaller
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class CustomGrpcStramingMarshaller implements MethodDescriptor.Marshaller<Object> {
    private static final java.util.logging.Logger logger = Logging.getLogger(CustomGrpcStramingMarshaller.class.getSimpleName());

    public CustomGrpcStramingMarshaller() {
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
            throw new StreamParsingException(ex);
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
            throw new StreamParsingException(ex);
        }
    }
}
