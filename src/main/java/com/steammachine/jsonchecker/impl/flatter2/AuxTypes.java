package com.steammachine.jsonchecker.impl.flatter2;

import com.steammachine.jsonchecker.types.PathParticle;

import java.util.Collections;

/**
 * 30.12.2017 10:21:45
 *
 * @author Vladimir Bogodukhov
 **/
public class AuxTypes {

    private AuxTypes() {
    }

    public static final PathParticle DELIMETR = () -> PathParticle.ParticleType.DELIMETR;
    public static final PathParticle NO_TRESSPASSING = () -> PathParticle.ParticleType.NO_TRESPASING;
    public static final PathParticle ASTERIX = new Asterix(Collections.emptyList());


}
