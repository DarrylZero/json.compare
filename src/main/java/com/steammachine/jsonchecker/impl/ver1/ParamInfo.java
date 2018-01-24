package com.steammachine.jsonchecker.impl.ver1;

/**
 * 30.12.2017 10:21:46
 *
 *  Несмотря на то, что класс объявлен как public его использование не предполагается извне пакета
 *  он может быть удален в любой момент.
 *
 * @author Vladimir Bogodukhov
 **/
public interface ParamInfo {

    /**
     * @return тип параметра (всегда не null)
     */
    Class clazz();

    /**
     * @return наименование параметра (всегда не null)
     */
    String name();

    /**
     *
     * @return Значение параметра
     */
    Object value();
}
