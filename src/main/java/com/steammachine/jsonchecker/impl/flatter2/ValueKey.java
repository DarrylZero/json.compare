package com.steammachine.jsonchecker.impl.flatter2;

import java.util.Objects;

/**
 *
 * 30.12.2017 10:21:45
 * @author Vladimir Bogodukhov
 **/
public class ValueKey {

    private final Object holder;
    private final String propertyName;
    private final Integer arrayIndex;

    private ValueKey(Object holder, String propertyName, Integer arrayIndex) {
        this.holder = Objects.requireNonNull(holder);
        this.propertyName = propertyName;
        this.arrayIndex = arrayIndex;
        if ((propertyName == null) == (arrayIndex == null)) {
            throw new IllegalArgumentException("propertyName or arrayIndex must be defined");
        }
    }

    /**
     * @return Объект держатель значения (всегда не null)
     */
    public Object holder () {
        return holder;
    }

    /**
     * @return наименование свойства объекта - (может быть null, если {@link #arrayIndex()} возвращает не null )
     */
    public String propertyName() {
        return propertyName;
    }

    /**
     *
     * @return индекс значения
     */
    public Integer arrayIndex() {
        return arrayIndex;
    }

    /**
     * Ключ для значения идентифицируемого объектом держателем и именем свойства.
     * @param o объект
     * @param name наименование свойства
     * @return Ключ для значения
     */
    public static ValueKey of(Object o, String name) {
        return new ValueKey(o, name, null);
    }

    /**
     * Ключ для значения идентифицируемого объектом держателем и индексом в массиве .
     * @param o объект
     * @param index индекс
     * @return Ключ для значения
     */
    public static ValueKey of(Object o, int index) {
        return new ValueKey(o, null, index);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ValueKey)) return false;

        ValueKey nodeKey = (ValueKey) o;

        if (holder != nodeKey.holder) return false;

        if (propertyName != null ? !propertyName.equals(nodeKey.propertyName) : nodeKey.propertyName != null)
            return false;

        return arrayIndex != null ? arrayIndex.equals(nodeKey.arrayIndex) : nodeKey.arrayIndex == null;
    }

    @Override
    public int hashCode() {
        int result = System.identityHashCode(holder);
        result = 31 * result + (propertyName != null ? propertyName.hashCode() : 0);
        result = 31 * result + (arrayIndex != null ? arrayIndex.hashCode() : 0);
        return result;
    }


}
