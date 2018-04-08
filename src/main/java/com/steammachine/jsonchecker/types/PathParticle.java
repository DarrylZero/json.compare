package com.steammachine.jsonchecker.types;

import java.util.Arrays;

/**
 *
 * Path particle interface
 *
 * @author Vladimir Bogodukhov
 * 30.12.2017 10:21:46
 **/
public interface PathParticle {

    enum ParticleType {
        ELEMENT,
        ANYELEMENT,
        DELIMETR,
        NO_TRESPASING;

        public boolean in(ParticleType... types) {
            return Arrays.stream(types).anyMatch(t -> t == this);
        }

    }

    /**
     * @return тип части всегда не null
     */
    ParticleType type();


}
