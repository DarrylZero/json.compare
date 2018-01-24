package com.steammachine.jsonchecker.utils.compatibletypescomparator.ver2;

/**
 *
 * Вспомогательный тип для сравнения элементов
 * 30.12.2017 10:21:46
 * @author Vladimir Bogodukhov
 **/
public interface CompareTo<T1, T2> {
    /**
     * @return Первый тип объекта сравнения
     */
    Class<T1> type1();

    /**
     * @return Второй объекта сравнения
     */
    Class<T2> type2();

    /**
     * Метод сравнения
     *
     * @param value1 -
     * @param value2 -
     * @return -
     */
    boolean isEqualTo(T1 value1, T2 value2);
}
