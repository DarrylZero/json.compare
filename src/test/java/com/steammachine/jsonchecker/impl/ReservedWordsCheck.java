package com.steammachine.jsonchecker.impl;

import com.steammachine.common.utils.enumerations.EnumComparisonUtils;
import com.steammachine.jsonchecker.impl.ver1.ReservedWords;
import com.steammachine.org.junit5.extensions.dynamictests.dynamictestparam.DynamicTestParams;
import com.steammachine.org.junit5.extensions.expectedexceptions.Expected;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Created by vladimirbogoduhov on 09/01/17.
 *
 * @author Vladimir Bogodukhov
 */
class ReservedWordsCheck {


    enum ReservedWordsTemplate {
        PARENT("parent"),
        INTERFACE("interface"),
        CLASS("class"),
        CLASS_ALIAS("classalias"),
        PARAMS("params"),
        USERDATA("userdata"),
        DEFAULT("default"),
        DEFAULTCLASS("defaultclass"),
        DEFAULTCLASSALIAS("defaultclassalias"),
        TYPES("types"),
        NAME("name"),
        INSTANCE_NAME("instancename"),
        CHILDREN("children"),
        ITEMS("items");

        private final String ident;

        ReservedWordsTemplate(String ident) {
            this.ident = Objects.requireNonNull(ident);
        }

        public String ident() {
            return ident;
        }
    }


    @Test
    void testInterfaceTemplate() {
        EnumComparisonUtils.checkIfEnumsEqual(ReservedWords.class, ReservedWordsTemplate.class);
        EnumComparisonUtils.deepIfEnumsAreEqual(ReservedWords.class, ReservedWordsTemplate.class,
                (fake, fakeTemplate) -> Objects.equals(fake.ident(), fakeTemplate.ident()));
    }


    @Test
    void checkNullity() {
        Stream.of(ReservedWords.values()).map(ReservedWords::ident).forEach(Objects::requireNonNull);
    }

    /* -------------------------------------------- everyObjectExclusion -------------------------------------------- */

    @Test
    @Expected(expected = NullPointerException.class)
    void everyObjectExclusion10() {
        ReservedWords.everyObjectExclusion(null);
    }

    @TestFactory
    Stream<DynamicTest> everyObjectExclusion20() {
        return DynamicTestParams.of(
                "parent",
                "class",
                "classalias",
                "instancename",
                "children"
        ).testGroup("", (s, s2) -> "test for '" + s2 + "' ident",
                (s, s2) -> DynamicTest.dynamicTest(s, () -> ReservedWords.everyObjectExclusion(s2)), s -> true).lock().form();
    }

    @TestFactory
    Stream<DynamicTest> everyObjectExclusion30() {
        return Stream.of(ReservedWords.values()).
                map(ReservedWords::ident).
                filter((n) -> !ReservedWords.everyObjectExclusion(n)).
                map(i -> new String[]{"test for '" + i + "' ident", i}).
                map(a -> DynamicTest.dynamicTest(a[0], () -> assertFalse(ReservedWords.everyObjectExclusion(a[1]))));
    }

}