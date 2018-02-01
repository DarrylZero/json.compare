package com.steammachine.jsonchecker.impl.flatter2;

import com.steammachine.jsonchecker.types.PathParticle;

import java.util.*;

/**
 * 30.12.2017 10:21:45
 *
 * @author Vladimir Bogodukhov
 **/
public class Element implements PathParticle {

    private static final Element EMPTY_ELEMENT = new Element(Collections.emptyList());
    private final List<Id> list;

    protected Element(List<Id> list) {
        this.list = Collections.unmodifiableList(list);
    }

    public static Element empty() {
        return EMPTY_ELEMENT;
    }

    public static Element of(Id identifier) {
        Objects.requireNonNull(identifier);
        return new Element(Collections.singletonList(identifier));
    }

    public List<Id> list() {
        return list;
    }

    public Element addId(Id id) {
        Objects.requireNonNull(id);
        List<Id> list = new ArrayList<>();
        list.addAll(this.list);
        list.add(id);
        return new Element(list);
    }

    @Override
    public ParticleType type() {
        return ParticleType.ELEMENT;
    }

    /**
     * @param e1 -
     * @param e2 -
     * @return - новый объект с новыми данными
     */
    @Deprecated
    public static Element merge(Element e1, Element e2) {
        Objects.requireNonNull(e1);
        Objects.requireNonNull(e2);

        List<Id> list = new ArrayList<>();
        list.addAll(e1.list);
        list.addAll(e2.list);
        return new Element(list);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Element)) return false;

        Element element = (Element) o;

        return list.equals(element.list);
    }

    @Override
    public int hashCode() {
        return list.hashCode();
    }

}
