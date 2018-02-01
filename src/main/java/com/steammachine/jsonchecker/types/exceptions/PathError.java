package com.steammachine.jsonchecker.types.exceptions;

/**
 * Ошибка неверного пути до ноды - предполагает использование в случае если в формате данных пути.
 * 30.12.2017 10:21:46
 *
 * @author Vladimir Bogodukhov
 *         {@link com.steammachine.jsonchecker.types.exceptions.PathError}
 *
 *         com.steammachine.jsonchecker.types.exceptions.PathError
 *
 **/
public class PathError extends JSonParseException {

    public PathError() {
    }

    public PathError(Object message) {
        super("" + message);
    }

    public PathError(String message, Throwable cause) {
        super(message, cause);
    }

    public PathError(Throwable cause) {
        super(cause);
    }

    public PathError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
