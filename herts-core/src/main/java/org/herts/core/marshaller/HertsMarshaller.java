package org.herts.core.marshaller;

import com.google.common.io.ByteStreams;
import org.herts.core.exception.HertsMessageException;
import org.herts.core.logger.HertsLogger;

import io.grpc.MethodDescriptor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

/**
 * Herts grpc marshaller
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsMarshaller implements MethodDescriptor.Marshaller<byte[]> {
    private static final Logger logger = HertsLogger.getLogger(HertsMarshaller.class.getSimpleName());

    public HertsMarshaller() {
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
            throw new HertsMessageException(ex);
        }
    }
}
