package org.hertsstack.core.exception;

/**
 * Herts code generation exception class.
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class CodeGenException extends RuntimeException {
    public CodeGenException() {
        super();
    }

    public CodeGenException(String msg) {
        super(msg);
    }

    public CodeGenException(Throwable cause) {
        super(cause);
    }

    public CodeGenException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
