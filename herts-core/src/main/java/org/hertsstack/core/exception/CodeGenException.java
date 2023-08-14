package org.hertsstack.core.exception;

/**
 * Herts code generation exception class.
 *
 * @author Herts Contributer
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
