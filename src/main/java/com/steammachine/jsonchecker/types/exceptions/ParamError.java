package com.steammachine.jsonchecker.types.exceptions;

/**
 * Ошибка неверного пути до ноды - предполагает использование в случае если в формате данных пути.
 * 30.12.2017 10:21:46
 *
 * @author Vladimir Bogodukhov
 *         {@link ParamError}
 **/
public class ParamError extends JSonParseException {

    public ParamError() {
    }

    public ParamError(String message) {
        super(message);
    }

    public ParamError(String message, Throwable cause) {
        super(message, cause);
    }

    public ParamError(Throwable cause) {
        super(cause);
    }

    public ParamError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
