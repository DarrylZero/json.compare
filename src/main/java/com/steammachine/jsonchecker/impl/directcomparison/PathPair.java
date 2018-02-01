package com.steammachine.jsonchecker.impl.directcomparison;

import com.steammachine.jsonchecker.types.Path;

import java.util.Objects;

/**
 * 30.12.2017 10:21:45
 *
 * @author Vladimir Bogodukhov
 **/
public class PathPair {
    private final String kind;
    private final Path path;

    private PathPair(String kind, Path path) {
        this.kind = Objects.requireNonNull(kind);
        this.path = Objects.requireNonNull(path);
    }

    public static PathPair of(String kind, Path path) {
        return new PathPair(kind, path);
    }

    public String kind() {
        return kind;
    }

    public Path path() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PathPair)) return false;

        PathPair pathPair = (PathPair) o;

        if (kind != null ? !kind.equals(pathPair.kind) : pathPair.kind != null) return false;
        return path != null ? path.equals(pathPair.path) : pathPair.path == null;
    }

    @Override
    public int hashCode() {
        int result = kind != null ? kind.hashCode() : 0;
        result = 31 * result + (path != null ? path.hashCode() : 0);
        return result;
    }
}

