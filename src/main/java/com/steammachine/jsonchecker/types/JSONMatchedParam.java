package com.steammachine.jsonchecker.types;

/**
 * Интерфейс параметра, используемый для передачи в методы сравнения документов JSON
 *
 * @author Vladimir Bogodukhov
 */
@FunctionalInterface
public interface JSONMatchedParam {

    /**
     * Метод сравнения параметра со значением.
     *
     * @param value значение сравниваемое на соответствие.
     * @return {@code true} если параметр соответствует переданному значению.
     */
    boolean matches(Object value);
}
