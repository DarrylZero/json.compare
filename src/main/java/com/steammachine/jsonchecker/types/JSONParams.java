package com.steammachine.jsonchecker.types;

import com.steammachine.jsonchecker.types.exceptions.ParamNotFound;

/**
 *
 * Интерфейс параметров передаваемых в методы сравнения.
 * @author Vladimir Bogodukhov
 */
public interface JSONParams {

    /**
     * Константа затычка для интерйеса JSONParams
     *
     */
    JSONParams STUB_PARAMS = name -> {
        throw new ParamNotFound("param " + name + " not found");
    };

    /**
     * Получить параметр по имени
     *
     * @param name имя
     * @return параметр
     * @throws ParamNotFound если параметр не найден
     */
    JSONParam get(String name) throws ParamNotFound;
}
