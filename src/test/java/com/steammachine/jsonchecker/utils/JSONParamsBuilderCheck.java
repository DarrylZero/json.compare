package com.steammachine.jsonchecker.utils;

import com.steammachine.common.map.MapBuilder;
import org.junit.jupiter.api.Test;

class JSONParamsBuilderCheck {
    @Test
    void of() {
        JSONParamsBuilder.of();
    }

    @Test
    void fromMap() {
        JSONParamsBuilder.of().fromMap(MapBuilder.<String, Integer>of().put("11", 1000).build());
    }

/*
    @Test
    void build() {
    }

    @Test
    void param() {
    }

    @Test
    void param1() {
    }

    @Test
    void single() {
    }

    @Test
    void single1() {
    }

    @Test
    void single2() {
    }

    @Test
    void single3() {
    }

    @Test
    void single4() {
    }

    @Test
    void single5() {
    }

    @Test
    void single6() {
    }

    @Test
    void regexp() {
    }

    @Test
    void regexp1() {
    }
*/

}