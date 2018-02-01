package com.steammachine.jsonchecker.types;

import com.steammachine.common.apilevel.Api;
import com.steammachine.common.apilevel.State;
import com.steammachine.jsonchecker.impl.ver1.ReservedWords;

/**
 * 30.12.2017 10:21:46
 *
 * @author Vladimir Bogodukhov
 *         <p>
 *         {@link NodeChecker}
 *         <p>
 *         Экземпляр класса реализации должен иметь no-args конструктор.
 *         Не статические методы класса реализации, начинающиеся на set, возвращающие
 *         void и содержащие один параметр могут быть использованы для установки параметров из
 *         документа шаблона.
 *         методу
 *         void setSetting1 (long l){}
 *         <p>
 *         Будет соответствовать свойство  setting1 объявленное в шаблоне
 *         <p>
 *         Наименование должно быть идентификатором java
 *         (может содержать только символы которые используются в наименованиях методов java)
 *         Не может иметь значение описанное в списке.
 *         {@link ReservedWords#EVERY_OBJECT_EXCLUSION}
 *
 *         пример : свойcтво  'parent' или 'class' не будут установливаться.
 *         соответствующие им методы объекта setParent и setObject вызываться не будут.
 *
 **/
@Deprecated
@Api(State.INTERNAL)
public interface NodeChecker<Type> {


    /**
     * Метод проверки - может ли передаваемый элемент быть проверен данным объектом.
     *
     * @param type тип
     * @return {@code true} если передаваемый элемент может быть проверен данным объектом.
     */
    boolean checks(Class type);

    /**
     * Метод осуществляет проверку элемента типа Type
     *
     * @param item    - проверяемый элемент.
     * @param path    - строка координат элемента.
     * @param context - контекст проверки.
     */
    void check(Type item, String path, NodeCheckContext context);
}
