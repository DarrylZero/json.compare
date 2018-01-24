package com.steammachine.jsonchecker.impl.directcomparison.pathformats;

import com.steammachine.jsonchecker.types.Path;

/**
 * Created 02/03/17 19:33
 *
 * @author Vladimir Bogodukhov 
 **/

public class CheckPathFormat {
    private static final CheckPathFormat UNUSED = new CheckPathFormat();

    private final boolean used;
    private final String data;
    private final boolean result;
    private final Path path;

    private CheckPathFormat() {
        this.used = false;
        this.data = null;
        this.result = false;
        this.path = null;
    }

    private CheckPathFormat(String data, boolean result, Path path) {
        this.used = true;
        this.data = data;
        this.result = result;
        this.path = path;
    }


    public String data() {
        return data;
    }

    public boolean result() {
        return result;
    }

    public Path path() {
        return path;
    }

    public String testName() {
        return "checkPathFormat " + data;
    }

    public boolean used() {
        return used;
    }

    public CheckPathFormat ignore() {
        return UNUSED;
    }

    public static CheckPathFormat check(String data, boolean result) {
        return new CheckPathFormat(data, result, null);
    }

    public static CheckPathFormat check(String data, boolean result, Path path) {
        return new CheckPathFormat(data, result, path);
    }

    public static CheckPathFormat delimer(String... data) {
        return UNUSED;
    }




}
