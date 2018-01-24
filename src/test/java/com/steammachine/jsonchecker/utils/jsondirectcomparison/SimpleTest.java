package com.steammachine.jsonchecker.utils.jsondirectcomparison;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.function.Executable;

/**
 * 30.12.2017 10:21:46
 *
 * @author Vladimir Bogodukhov
 **/
public class SimpleTest {

    private static final SimpleTest UNUSED = new SimpleTest(null, null, false);

    private final boolean used;
    private final String testName;
    private final Executable executable;

    public boolean used() {
        return used;
    }

    private SimpleTest(String testName, Executable executable, boolean used) {
        this.testName = testName;
        this.executable = executable;
        this.used = used;
    }

    public SimpleTest(String testName, Executable executable) {
        this(testName, executable, true);
    }

    public static SimpleTest simpleTest(String testName, Executable executable) {
        return new SimpleTest(testName, executable);
    }

    public static SimpleTest unused() {
        return UNUSED;
    }

    public SimpleTest ignore() {
        return delimer();
    }

    public static SimpleTest delimer(String ... data) {
        return UNUSED;
    }

    public static DynamicTest dynamicTest(SimpleTest i) {
        return DynamicTest.dynamicTest(i.testName, i.executable);
    }

    public Executable executable() {
        return executable;
    }

    public String testName() {
        return testName;
    }


}
