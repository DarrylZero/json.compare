package com.steammachine.jsonchecker.impl.flatter2;

import java.util.Objects;

/**
 * Идентификатор Узла в родительском элементе.
 * Объект неменяемый - после создания
 * <p>
 * 30.12.2017 10:21:45
 *  *
 * @author Vladimir Bogodukhov
 *
 **/
public final class Id {
    private final IdentifierType type;
    private final String id;
    private final Integer index;

    private Id(String id, Integer index, IdentifierType type) {
        Objects.requireNonNull(type);
        switch (type) {
            case notArrayItem:
                Objects.requireNonNull(id);
                break;
            case arrayItem:
                Objects.requireNonNull(index);
                break;
            case anyArrayItem:
                break;
            default: {
                throw new IllegalStateException();
            }
        }

        this.id = id;
        this.index = index;
        this.type = type;
    }

    /**
     * Обозначение элемента - не элемента массива (объекта)
     *
     * @param name - имя
     * @return новый объект идентификатора
     */
    public static Id obj(String name) {
        return new Id(name, null, IdentifierType.notArrayItem);
    }

    /**
     * Обозначение элемента - элемента массива
     *
     * @param index индекс в массиве
     * @return новый объект идентификатора
     */
    public static Id arr(int index) {
        return new Id(null, index, IdentifierType.arrayItem);
    }

    /**
     * Обозначение элемента - любого элемента массива
     *
     * @return новый объект идентификатора
     */
    public static Id anyArr() {
        return new Id(null, null, IdentifierType.anyArrayItem);
    }

    public String id() {
        return id;
    }

    public Integer index() {
        return index;
    }

    /**
     * @return Всегда не null
     */
    public IdentifierType type() {
        return type;
    }

    @Override
    public String toString() {
        switch (type) {
            case notArrayItem:
                return "Id(" + id + ")";

            case arrayItem:
                return "Id [" + index() + "]";

            case anyArrayItem:
                return "Id [*]";

            default: {
                throw new IllegalStateException("illegal type " + type);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Id)) return false;

        Id id1 = (Id) o;

        if (type != id1.type) return false;
        if (id != null ? !id.equals(id1.id) : id1.id != null) return false;
        return index != null ? index.equals(id1.index) : id1.index == null;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (index != null ? index.hashCode() : 0);
        return result;
    }
}

