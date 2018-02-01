package com.steammachine.jsonchecker.impl.flatter2;

import java.util.List;

/**
 * 30.12.2017 10:21:45
 *
 * @author Vladimir Bogodukhov
 **/
public final class Asterix extends Element {

    protected Asterix(List<Id> list) {
        super(list);
    }

    @Override
    public ParticleType type() {
        return ParticleType.ANYELEMENT;
    }
}
