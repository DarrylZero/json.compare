package com.steammachine.jsonchecker.types;

/**
 * Вспомогательный интерфейс установки значений, которые не имеют прямого маппинга в объект.
 * Если объект поддерживает данный интерфейс, то для каждого (устанавливаемого) записываемого
 * свойства у которого нет маппинга в объекте
 * будет вызван метод {@link #setValue(String, String)}
 * <p>
 * <p>
 * <p>
 * 30.12.2017 10:21:46
 * {@link com.steammachine.jsonchecker.types.UnmappedProperties }
 * com.steammachine.jsonchecker.types.UnmappedProperties
 *
 * @author Vladimir Bogodukhov
 **/
@FunctionalInterface
@Deprecated
public interface UnmappedProperties {

    /**
     * Установить значение свойства в объект
     *
     * @param propertyName - наименование свойства (всегда не null)
     * @param value        - значение свойства.
     */
    void setValue(String propertyName, String value);

}
