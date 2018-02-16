package com.steammachine.jsonchecker.utils;

import com.steammachine.common.map.MapBuilder;
import com.steammachine.jsonchecker.types.JSONMatchedParam;
import com.steammachine.jsonchecker.types.JSONParams;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;

class JSONParamsBuilderCheck {
    @Test
    void of() {
        JSONParamsBuilder.of();
    }

    @Test
    void fromMap() {
        JSONParamsBuilder.of().fromMap(MapBuilder.<String, Integer>of().put("11", 1000).build());
    }

    @Test
    void build() {
        JSONParams params = JSONParamsBuilder.params().
                single("a", false).
                single("b", 1).
                single("c", 1.0).
                single("d", 2).
                regexp("e", "^abc$").
                param("d", v -> String.class.isInstance(v) && "Los narcos".equals(v)).

                build();

        Assertions.assertAll(
                () -> Assertions.assertTrue(params.get("a").matches(false)),
                () -> Assertions.assertFalse(params.get("a").matches(true)),
                () -> Assertions.assertTrue(params.get("b").matches(1)),
                () -> Assertions.assertFalse(params.get("e").matches("asbcs")),
                () -> Assertions.assertTrue(params.get("e").matches("abc")),
                () -> Assertions.assertTrue(params.get("d").matches("Los narcos")),
                () -> Assertions.assertFalse(params.get("d").matches("Los arcos")),
                () -> Objects.requireNonNull(params)
        );
    }
}