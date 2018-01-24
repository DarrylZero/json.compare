package com.steammachine.jsonchecker.types.exceptions;

/**
 * Исключение выбрасываемое если формат используемых данных неверен. - Не является json (not well - formed json)
 *
 * 30.12.2017 10:21:46
 * @author Vladimir Bogodukhov
 *         {@link WrongDataFormat}
 **/
public class WrongDataFormat extends JSonParseException {
    public WrongDataFormat() {
    }

    public WrongDataFormat(String message) {
        super(message);
    }

    public WrongDataFormat(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongDataFormat(Throwable cause) {
        super(cause);
    }

    public WrongDataFormat(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
