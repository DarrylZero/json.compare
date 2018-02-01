package com.steammachine.jsonchecker.types;

/**
 *
 * Вспомогательный интерфейс - представления пути в виде строки
 * 30.12.2017 10:21:46
 * @author Vladimir Bogodukhov
 **/
@FunctionalInterface
public interface PathRepresentation {

    /**
     * Получить строковое представление пути
     *
     * @param path - путь (всегда не null)
     * @return - строковое представление пути (всегда не null)
     */
    String path(Path path);

}
