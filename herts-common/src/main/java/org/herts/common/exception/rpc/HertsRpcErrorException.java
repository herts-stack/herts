package org.herts.common.exception.rpc;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;

/**
 * Herts prc error
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsRpcErrorException extends io.grpc.StatusRuntimeException {
    private final StatusCode statusCode;
    private final Status status;

    /**
     * Status Code enum
     * See: <a href="https://github.com/grpc/grpc/blob/master/doc/statuscodes.md">StatusCode</a>
     */
    public enum StatusCode {
        Status0("0"),
        Status1("1"),
        Status2("2"),
        Status3("3"),
        Status4("4"),
        Status5("5"),
        Status6("6"),
        Status7("7"),
        Status8("8"),
        Status9("9"),
        Status10("10"),
        Status11("11"),
        Status12("12"),
        Status13("13"),
        Status14("14"),
        Status15("15"),
        Status16("16");

        private final String value;

        StatusCode(final String value) {
            this.value = value;
        }

        public String getStringCode() {
            return this.value;
        }

        public int getIntegerCode() {
            return Integer.parseInt(this.value);
        }

        /**
         * Convert to io.grpc.Status
         *
         * @param code StatusCode
         * @return io.grpc.Status
         */
        public Status convertToGrpc(StatusCode code) {
            switch (code) {
                case Status0:
                    return Status.OK;
                case Status1:
                    return Status.CANCELLED;
                case Status2:
                    return Status.UNKNOWN;
                case Status3:
                    return Status.INVALID_ARGUMENT;
                case Status4:
                    return Status.DEADLINE_EXCEEDED;
                case Status5:
                    return Status.NOT_FOUND;
                case Status6:
                    return Status.ALREADY_EXISTS;
                case Status7:
                    return Status.PERMISSION_DENIED;
                case Status8:
                    return Status.RESOURCE_EXHAUSTED;
                case Status9:
                    return Status.FAILED_PRECONDITION;
                case Status10:
                    return Status.ABORTED;
                case Status11:
                    return Status.OUT_OF_RANGE;
                case Status12:
                    return Status.UNIMPLEMENTED;
                case Status13:
                    return Status.INTERNAL;
                case Status14:
                    return Status.UNAVAILABLE;
                case Status15:
                    return Status.DATA_LOSS;
                case Status16:
                    return Status.UNAUTHENTICATED;
                default:
                    return Status.INTERNAL;
            }
        }
    }

    /**
     * Convert to herts StatusCode.
     *
     * @param status Status
     * @return io.grpc.Status
     */
    public static StatusCode convertToHertsCode(Status status) {
        if (status.getCode() == Status.OK.getCode()) {
            return StatusCode.Status0;
        } else if (status.getCode() == Status.CANCELLED.getCode()) {
            return StatusCode.Status1;
        } else if (status.getCode() == Status.UNKNOWN.getCode()) {
            return StatusCode.Status2;
        } else if (status.getCode() == Status.INVALID_ARGUMENT.getCode()) {
            return StatusCode.Status3;
        } else if (status.getCode() == Status.DEADLINE_EXCEEDED.getCode()) {
            return StatusCode.Status4;
        } else if (status.getCode() == Status.NOT_FOUND.getCode()) {
            return StatusCode.Status5;
        } else if (status.getCode() == Status.ALREADY_EXISTS.getCode()) {
            return StatusCode.Status6;
        } else if (status.getCode() == Status.PERMISSION_DENIED.getCode()) {
            return StatusCode.Status7;
        } else if (status.getCode() == Status.RESOURCE_EXHAUSTED.getCode()) {
            return StatusCode.Status8;
        } else if (status.getCode() == Status.FAILED_PRECONDITION.getCode()) {
            return StatusCode.Status9;
        } else if (status.getCode() == Status.ABORTED.getCode()) {
            return StatusCode.Status10;
        } else if (status.getCode() == Status.OUT_OF_RANGE.getCode()) {
            return StatusCode.Status11;
        } else if (status.getCode() == Status.UNIMPLEMENTED.getCode()) {
            return StatusCode.Status12;
        } else if (status.getCode() == Status.INTERNAL.getCode()) {
            return StatusCode.Status13;
        } else if (status.getCode() == Status.UNAVAILABLE.getCode()) {
            return StatusCode.Status14;
        } else if (status.getCode() == Status.DATA_LOSS.getCode()) {
            return StatusCode.Status15;
        } else if (status.getCode() == Status.UNAUTHENTICATED.getCode()) {
            return StatusCode.Status16;
        }
        return StatusCode.Status13;
    }

    public HertsRpcErrorException(StatusRuntimeException ex) {
        super(Status.fromThrowable(ex).withDescription(ex.getMessage()));
        Status s = Status.fromThrowable(ex);
        this.statusCode = convertToHertsCode(s);
        System.out.println(this.statusCode );
        System.out.println(s);
        this.status = s;
    }

    public HertsRpcErrorException(StatusCode statusCode) {
        super(statusCode.convertToGrpc(statusCode));
        this.statusCode = statusCode;
        this.status = statusCode.convertToGrpc(statusCode);
    }

    public HertsRpcErrorException(StatusCode statusCode, String msg) {
        super(statusCode.convertToGrpc(statusCode).withDescription(msg));
        this.statusCode = statusCode;
        this.status = statusCode.convertToGrpc(statusCode).withDescription(msg);
    }

    public StatusRuntimeException createStatusException() {
        return this.status.asRuntimeException();
    }

    public String getMessage() {
        return this.status.getDescription();
    }

    public StatusCode getStatusCode() {
        return this.statusCode;
    }

    public Status getGrpcStatus() {
        return this.status;
    }
}
