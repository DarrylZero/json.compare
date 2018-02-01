package com.steammachine.jsonchecker.types.exceptions;

/**
 *
 * Исключения - выбрасываемое если не найден параметр передаваемый в Документ
 *
 *
 * 30.12.2017 10:21:46
 * @author Vladimir Bogodukhov
 **/
public class ParamNotFound extends RuntimeException {
    public ParamNotFound(String message) {
        super(message);
    }
}
