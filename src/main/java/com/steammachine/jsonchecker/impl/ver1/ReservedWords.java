package com.steammachine.jsonchecker.impl.ver1;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Перечисление описывает зарезервированные слова используемые в разбираемом json-документе.
 *
 *  Несмотря на то, что класс объявлен как public его использование не предполагается извне пакета
 *  он может быть удален в любой момент.
 *
 * 30.12.2017 10:21:46
 * @author Vladimir Bogodukhov
 **/
public enum ReservedWords {

    PARENT("parent"),
    INTERFACE("interface"),
    CLASS("class"),
    CLASS_ALIAS("classalias"),
    PARAMS("params"),
    USERDATA("userdata"),
    DEFAULT("default"),
    DEFAULTCLASS("defaultclass"),
    DEFAULTCLASSALIAS("defaultclassalias"),
    TYPES("types"),
    NAME("name"),
    INSTANCE_NAME("instancename"),
    CHILDREN("children"),
    ITEMS("items");

    /**
     * Набор свойств исключений для каждого элемента Json.
     *
     */
    public static final List<String> EVERY_OBJECT_EXCLUSION =
            Stream.of(
                    PARENT,
                    CLASS,
                    CLASS_ALIAS,
                    INSTANCE_NAME,
                    CHILDREN).
                    map(ReservedWords::ident).
                    collect(Collectors.toList());

    private final String ident;

    ReservedWords(String ident) {
        this.ident = Objects.requireNonNull(ident);
    }

    public String ident() {
        return ident;
    }

    /**
     * Исключение в названиях свойств - которое действует для каждого объекта в
     *
     * @param propertyName наименование элемента которое проверяется на допустимость быть свойством.
     * @return {@code true} если свойство элемент с именем рассматривается как свойство.
     */
    public static boolean everyObjectExclusion(String propertyName) {
        Objects.requireNonNull(propertyName);
        return EVERY_OBJECT_EXCLUSION.contains(propertyName);
    }

}
