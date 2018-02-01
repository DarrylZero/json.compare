package com.steammachine.jsonchecker.types.exceptions;

/**
 * Специально выделенный тип исключения - обозначающий несовпадение структур документа и проверяемого шаблона.
 * Используется в тех случаях когда требуется указать недопустимые несоответствия структуры.
 *
 *
 *
 * 30.12.2017 10:21:46
 * @author Vladimir Bogodukhov
 * {@link com.steammachine.jsonchecker.types.exceptions.StructureMismatch}
 * com.steammachine.jsonchecker.types.exceptions.StructureMismatch
 **/
public class StructureMismatch extends JSonParseException {

    public StructureMismatch() {
    }

    public StructureMismatch(String message) {
        super(message);
    }

    public StructureMismatch(String message, Throwable cause) {
        super(message, cause);
    }

    public StructureMismatch(Throwable cause) {
        super(cause);
    }

    public StructureMismatch(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
