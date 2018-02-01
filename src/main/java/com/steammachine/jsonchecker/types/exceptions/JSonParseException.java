package com.steammachine.jsonchecker.types.exceptions;

/**
 *
 * Oбобщенный тип исключения для обозначения ошибок разбора документа json
 *
 *
 * 30.12.2017 10:21:46
 * @author Vladimir Bogodukhov
 **/
public class JSonParseException extends RuntimeException {

    public JSonParseException() {
    }

    public JSonParseException(String message) {
        super(message);
    }

    public JSonParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public JSonParseException(Throwable cause) {
        super(cause);
    }

    public JSonParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
