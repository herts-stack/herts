package org.hertsstack.core.marshaller;

import com.google.common.io.ByteStreams;

import org.hertsstack.core.exception.StreamParsingException;
import org.hertsstack.core.logger.Logging;

import io.grpc.MethodDescriptor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Herts grpc marshaller
 *
 * @author Herts Contributer
 */
public class CustomGrpcMarshaller implements MethodDescriptor.Marshaller<byte[]> {
    private static final java.util.logging.Logger logger = Logging.getLogger(CustomGrpcMarshaller.class.getSimpleName());

    public CustomGrpcMarshaller() {
    }

    @Override
    public InputStream stream(byte[] value) {
        if (value == null) {
            return null;
        }
        return new ByteArrayInputStream(value);
    }

    @Override
    public byte[] parse(InputStream stream) {
        if (stream == null) {
            return null;
        }
        try {
            return ByteStreams.toByteArray(stream);
        } catch (IOException ex) {
            throw new StreamParsingException(ex);
        }
    }
}
