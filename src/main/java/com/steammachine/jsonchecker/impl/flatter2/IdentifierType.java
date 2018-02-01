package com.steammachine.jsonchecker.impl.flatter2;

import java.util.Arrays;

/**
 * 30.12.2017 10:21:45
 *
 * @author Vladimir Bogodukhov
 **/
public enum IdentifierType {
    notArrayItem,
    arrayItem,
    anyArrayItem;

    public boolean in(IdentifierType ... types) {
        return Arrays.stream(types).anyMatch(i -> i == this);
    }



}
