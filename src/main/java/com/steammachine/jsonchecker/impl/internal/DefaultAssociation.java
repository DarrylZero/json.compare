package com.steammachine.jsonchecker.impl.internal;

import com.steammachine.jsonchecker.impl.internal.Association;

import java.util.IdentityHashMap;
import java.util.Objects;

/**
 * Реализация интерфейса {@link Association} - ассоциаций объекта с произвольным значением.
 * ассоциируемый объект может быть абсолютно любой -
 * При ассоциации со значением не используется "классическая"
 * пара методов {@link Object#equals(Object)} и  {@link Object#hashCode()}
 * Обеспечивается однозначная ассоциация любого объекта приложения со значением.
 * <p>
 * <p>
 * <p>
 * Created 20/01/17 16:50
 *
 * @author Vladimir Bogodukhov
 * @deprecated
 **/
@Deprecated
public class DefaultAssociation<T> implements Association<T> {
    private final IdentityHashMap<Object, T> identityMap = new IdentityHashMap<>();

    @Override
    public boolean isAssociated(Object o) {
        return identityMap.containsKey(o);
    }

    public T associatedValue(Object o) {
        Objects.requireNonNull(o);
        return identityMap.get(o);
    }

    public void associate(Object o, T t) {
        identityMap.put(o, t);
    }

    public void mergeWith(DefaultAssociation<T> association) {
        identityMap.putAll(association.identityMap);
    }


}
