package com.steammachine.jsonchecker.impl.flatter2;

import com.steammachine.jsonchecker.types.Path;

import java.util.*;

import static com.steammachine.common.utils.commonutils.CommonUtils.check;

/**
 * 30.12.2017 10:21:45
 *
 * @author Vladimir Bogodukhov
 **/
public class PathCluster {

    private final Map<String, Path> pathMap = new HashMap<>();
    private final ValueKey valueKey;
    private final Object value;

    public PathCluster(ValueKey valueKey, Object value, Map<String, Path> pathMap) {
        this.valueKey = valueKey;
        this.value = value;
        this.pathMap.putAll(pathMap);
    }

    public PathCluster(ValueKey valueKey, Object value) {
        this.valueKey = valueKey;
        this.value = value;
    }

    public Set<String> kinds() {
        return pathMap.keySet();
    }

    public Object value() {
        return value;
    }

    public static PathCluster of(ValueKey valueKey, Object value) {
        return new PathCluster(valueKey, value);
    }

    public PathCluster putPath(String name, Path path) {
        Objects.requireNonNull(path);
        Objects.requireNonNull(name);
        pathMap.put(name, path);
        return this;
    }

    public ValueKey valueKey() {
        return valueKey;
    }

    public Path path(String kind) {
        return pathMap.get(kind);
    }

    public void merge(PathCluster cluster) {
        check(() -> Objects.equals(valueKey, cluster.valueKey), IllegalStateException::new);
        check(() -> Objects.equals(value, cluster.value), IllegalStateException::new);
        this.pathMap.putAll(cluster.pathMap);
    }

    public static PathCluster merge(PathCluster pc1, PathCluster pc2) {
        Objects.requireNonNull(pc1);
        Objects.requireNonNull(pc2);
        check(() -> Objects.equals(pc1.valueKey, pc2.valueKey), IllegalStateException::new);
        check(() -> Objects.equals(pc1.value, pc2.value), IllegalStateException::new);

        Map<String, Path> map = new HashMap<>();
        map.putAll(pc1.pathMap);
        map.putAll(pc2.pathMap);

        return new PathCluster(pc1.valueKey, pc1.value, map);
    }

    public Map<String, Path> unmodifiablePathMap() {
        return Collections.unmodifiableMap(pathMap);
    }
}
