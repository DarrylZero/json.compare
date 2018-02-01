package com.steammachine.jsonchecker.impl.internal;

/**
 * Created 20/01/17 16:50
 *
 * @author Vladimir Bogodukhov 
 **/
public interface Association<T> {

    /**
     * проверяет есть ли для объекта ассоциации
     *
     * @param o объект
     * @return
     */
    boolean isAssociated(Object o);

    /**
     *
     * Получить связь объекта с некоторым значением.
     *
     * @param o объект (not null)
     * @return Значение объекта
     */
    T associatedValue(Object o);
}
