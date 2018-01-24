package com.steammachine.jsonchecker.utils.jsondirectcomparison;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.steammachine.jsonchecker.impl.flatter2.ValueKey;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Vladimir Bogodukhov on 01.12.2017.
 *
 * @author Vladimir Bogodukhov
 */
@SuppressWarnings("WeakerAccess")
public class ValueKeyCheck {


    @SuppressWarnings("RedundantStringConstructorCall")
    @Test
    void test() {
        String s = new String("333");
        Set<ValueKey> map = new HashSet<>();


        map.add(ValueKey.of(s, "1"));
        Assertions.assertTrue(map.contains(ValueKey.of(s, "1")));
        Assertions.assertFalse(map.contains(ValueKey.of(s, "2")));
        Assertions.assertFalse(map.contains(ValueKey.of("333", "1")));


        map.add(ValueKey.of(s, 1));
        Assertions.assertTrue(map.contains(ValueKey.of(s, 1)));
        Assertions.assertFalse(map.contains(ValueKey.of(s, 2)));
        Assertions.assertFalse(map.contains(ValueKey.of("333", 1)));

        Object o = new Object();
        map.add(ValueKey.of(o, "1"));
        Assertions.assertTrue(map.contains(ValueKey.of(o, "1")));
        Assertions.assertFalse(map.contains(ValueKey.of(o, "2")));
        Assertions.assertFalse(map.contains(ValueKey.of(new Object(), "1")));


        map.add(ValueKey.of(o, 1));
        Assertions.assertTrue(map.contains(ValueKey.of(o, 1)));
        Assertions.assertFalse(map.contains(ValueKey.of(o, 2)));
        Assertions.assertFalse(map.contains(ValueKey.of(new Object(), 1)));
    }


}