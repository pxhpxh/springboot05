package com.example.exception;

public class BaseBusinessException extends RuntimeException {
    protected String errorCode;
    protected String message;
    protected String extFields;

    public BaseBusinessException() {
        super();
    }

    public BaseBusinessException(String message) {
        super();
        this.errorCode="-1";
        this.message = message;
    }

    public BaseBusinessException(Throwable arg0) {
        super(arg0);
    }

    public BaseBusinessException(String errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    public BaseBusinessException(String errorCode, String message) {
        super();
        this.errorCode = errorCode;
        this.message = message;
    }

    public BaseBusinessException(String errorCode, String message, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
        this.message = message;
    }

    public BaseBusinessException(String errorCode, String message, String extFields, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
        this.message = message;
        this.extFields=extFields;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getExtFields() {
        return extFields;
    }

    public void setExtFields(String extFields) {
        this.extFields = extFields;
    }
}
