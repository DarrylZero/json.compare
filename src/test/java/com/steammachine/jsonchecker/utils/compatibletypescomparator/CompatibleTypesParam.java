package com.steammachine.jsonchecker.utils.compatibletypescomparator;

import java.util.Objects;
import java.util.function.BooleanSupplier;

/**
 * 30.12.2017 10:21:46
 *
 * @author Vladimir Bogodukhov
 **/
public class CompatibleTypesParam {

    private static final CompatibleTypesParam UNUSED = new CompatibleTypesParam();
    private static final BooleanSupplier ALWAYS_OK = () -> true;
    private boolean used = true;
    private final String name;
    private final Object obj1;
    private final Object obj2;
    private final boolean result;
    private final BooleanSupplier condition;


    private CompatibleTypesParam() {
        this.used = false;
        this.name = "";
        this.result = false;
        this.obj1 = null;
        this.obj2 = null;
        this.condition = ALWAYS_OK;
    }

    private CompatibleTypesParam(String name, Object obj1, Object obj2, boolean result, BooleanSupplier condition) {
        this.name = Objects.requireNonNull(name);
        this.result = result;
        this.obj1 = obj1;
        this.obj2 = obj2;
        this.condition = Objects.requireNonNull(condition);
    }

    public static CompatibleTypesParam delimetr(String... parts) {
        return UNUSED;
    }

    public CompatibleTypesParam _ignore() {
        return UNUSED;
    }

    public BooleanSupplier condition() {
        return condition;
    }

    public String name() {
        return name;
    }

    public boolean used() {
        return used;
    }

    public Object obj1() {
        return obj1;
    }

    public Object obj2() {
        return obj2;
    }

    public boolean result() {
        return result;
    }

    public static CompatibleTypesParam test(String name, Object obj1, Object obj2, boolean result) {
        return new CompatibleTypesParam(name, obj1, obj2, result, ALWAYS_OK);
    }

    public static CompatibleTypesParam test(String name, Object obj1, Object obj2, boolean result, BooleanSupplier condition) {
        return new CompatibleTypesParam(name, obj1, obj2, result, ALWAYS_OK);
    }

    public String dataString() {
        return "CompatibleTypesParam(" + "obj1=" + obj1 + ", obj2=" + obj2 + ", result=" + result + ')';
    }
}
