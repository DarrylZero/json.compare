package com.steammachine.jsonchecker.types.exceptions;

/**
 * Специально выделенный тип исключения  - обозначающий неверную структуру документа.
 *
 * Используется в тех случаях когда требуется указать недопустимые несоответствия структуры.
 * Исключение НЕ предполагает использования в случае неверного json документа (not well formed)
 *
 *
 * 30.12.2017 10:21:46
 * @author Vladimir Bogodukhov
 **/
public class MalformedDocument extends JSonParseException {

    public MalformedDocument() {
    }

    public MalformedDocument(String message) {
        super(message);
    }

    public MalformedDocument(String message, Throwable cause) {
        super(message, cause);
    }

    public MalformedDocument(Throwable cause) {
        super(cause);
    }

    public MalformedDocument(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
