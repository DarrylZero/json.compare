package com.steammachine.jsonchecker.types.exceptions;

/**
 * Ошибка неверного пути до ноды - предполагает использование в случае если в формате данных пути.
 * 30.12.2017 10:21:46
 *
 * @author Vladimir Bogodukhov
 *         {@link ParamTypeError}
 **/
public class ParamTypeError extends JSonParseException {

    public ParamTypeError() {
    }

    public ParamTypeError(String message) {
        super(message);
    }

    public ParamTypeError(String message, Throwable cause) {
        super(message, cause);
    }

    public ParamTypeError(Throwable cause) {
        super(cause);
    }

    public ParamTypeError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
