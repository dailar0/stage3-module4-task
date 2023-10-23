package com.mjc.school.service.exception;

public abstract class BaseException extends RuntimeException {
    public final int status;
    public final String message;

    public BaseException(ErrorCode errorCode) {
        super(errorCode.getReason());
        status = errorCode.getCode();
        message = errorCode.getReason();
    }

    public BaseException(ErrorCode errorCode, String message) {
        super(message);
        status = errorCode.getCode();
        this.message = message;
    }

    public BaseException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        status = errorCode.getCode();
        this.message = message;
    }

    public BaseException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getReason(), cause);
        status = errorCode.getCode();
        message = errorCode.getReason();
    }
}
