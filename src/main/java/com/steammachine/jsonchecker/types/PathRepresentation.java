package com.steammachine.jsonchecker.types;

import com.steammachine.common.apilevel.Api;
import com.steammachine.common.apilevel.State;

/**
 *
 * Вспомогательный интерфейс - представления пути в виде строки
 * 30.12.2017 10:21:46
 * @author Vladimir Bogodukhov
 **/
@FunctionalInterface
@Api(State.INTERNAL)
public interface PathRepresentation {

    /**
     * Получить строковое представление пути
     *
     * @param path - путь (всегда не null)
     * @return - строковое представление пути (всегда не null)
     */
    String path(Path path);

}
