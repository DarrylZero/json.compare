package com.steammachine.jsonchecker.executiontests.simple;

import java.util.Objects;

/**
 * 30.12.2017 10:21:46
 *
 * @author Vladimir Bogodukhov
 **/
public class PathFormatChecker {
    private final String testName;
    private final String path;
    private final boolean expected;
    private final boolean skip;

    private PathFormatChecker(String testName, String path, boolean expected, boolean skip) {
        this.testName = Objects.requireNonNull(testName);
        this.path = Objects.requireNonNull(path);
        this.expected = expected;
        this.skip = skip;
    }

    public static PathFormatChecker pathChecker(String testName, String path, boolean expected) {
        return new PathFormatChecker(testName, path, expected, false);
    }

    public static PathFormatChecker correctPathFormat(String testName, String path) {
        return new PathFormatChecker(testName, path, true, false);
    }

    public static PathFormatChecker correctPathFormat(String path) {
        return new PathFormatChecker("correct path format '" + path + "'", path, true, false);
    }

    public static PathFormatChecker incorrectPathFormat(String testName, String path) {
        return new PathFormatChecker(testName, path, false, false);
    }

    public static PathFormatChecker incorrectPathFormat(String path) {
        return new PathFormatChecker("incorrect path format '" + path + "'", path, false, false);
    }

    public static PathFormatChecker skipped() {
        return new PathFormatChecker("skipped test '", "", false, true);
    }


    public String testName() {
        return testName;
    }

    public String path() {
        return path;
    }

    public boolean expected() {
        return expected;
    }

    public boolean skip() {
        return skip;
    }

    public boolean used() {
        return !skip;
    }
}
