package org.hertsstack.core.exception;

/**
 * Herts core type invalid exception class.
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class TypeInvalidException extends RuntimeException {
    public TypeInvalidException() {
        super();
    }

    public TypeInvalidException(String msg) {
        super(msg);
    }

    public TypeInvalidException(Throwable cause) {
        super(cause);
    }

    public TypeInvalidException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
