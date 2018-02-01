package com.steammachine.jsonchecker.impl.flatter2;

/*
 *
 * 30.12.2017 10:21:45
 * @author Vladimir Bogodukhov
 **/

import com.steammachine.jsonchecker.types.Path;

import java.util.Objects;
import java.util.Optional;

/**
 * Класс определяющий полные пути до объекта в json документе со значением.
 */
public class ValRec {
    private final Object value;
    private final Path path;
    private final ValueKey valueKey;

    private ValRec(Path path, ValueKey valueKey, Object value) {
        Objects.requireNonNull(path);
        Objects.requireNonNull(valueKey);

        this.path = Objects.requireNonNull(path);
        this.valueKey = Objects.requireNonNull(valueKey);
        this.value = value;
    }

    public static ValRec of(Path path, ValueKey valueKey, Object value) {
        return new ValRec(path, valueKey, value);
    }

    public ValueKey valueKey() {
        return valueKey;
    }

    public Path path() {
        return path;
    }

    public ValRec updatePath(Path path) {
        Objects.requireNonNull(path);
        return new ValRec(path, this.valueKey, this.value);
    }

    public Object value() {
        return value;
    }

    public Optional<Object> optional() {
        return Optional.ofNullable(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ValRec)) return false;

        ValRec valRec = (ValRec) o;

        if (value != null ? !value.equals(valRec.value) : valRec.value != null) return false;
        if (path != null ? !path.equals(valRec.path) : valRec.path != null) return false;
        return valueKey != null ? valueKey.equals(valRec.valueKey) : valRec.valueKey == null;
    }

    @Override
    public int hashCode() {
        int result = value != null ? value.hashCode() : 0;
        result = 31 * result + (path != null ? path.hashCode() : 0);
        result = 31 * result + (valueKey != null ? valueKey.hashCode() : 0);
        return result;
    }
}
