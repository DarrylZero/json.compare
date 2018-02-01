package com.steammachine.jsonchecker.impl.directcomparison.flatterprocs;

import com.steammachine.jsonchecker.utils.jsondirectcomparison.SimpleTest;
import com.steammachine.org.junit5.extensions.dynamictests.dynamictestparam.DynamicTestParams;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.stream.Stream;

/**
 * Created by Vladimir Bogodukhov on 01.12.2017.
 *
 * @author Vladimir Bogodukhov
 */
class FlattersCommonCheck {

        /* ---------------------------------------- isFlatValue -------------------------------------------------------- */

    @TestFactory
    Stream<DynamicTest> isFlatValue() {
        return DynamicTestParams.of(
                SimpleTest.delimer("simple checks"),
                SimpleTest.simpleTest("isFlatValue 10", () -> Assertions.assertTrue(FlattersCommon.isFlatValue(null))),
                SimpleTest.simpleTest("isFlatValue 20", () -> Assertions.assertTrue(FlattersCommon.isFlatValue(Byte.MAX_VALUE))),
                SimpleTest.simpleTest("isFlatValue 30", () -> Assertions.assertTrue(FlattersCommon.isFlatValue(Short.MAX_VALUE))),
                SimpleTest.simpleTest("isFlatValue 40", () -> Assertions.assertTrue(FlattersCommon.isFlatValue(Character.MAX_VALUE))),
                SimpleTest.simpleTest("isFlatValue 50", () -> Assertions.assertTrue(FlattersCommon.isFlatValue(Integer.MAX_VALUE))),
                SimpleTest.simpleTest("isFlatValue 60", () -> Assertions.assertTrue(FlattersCommon.isFlatValue(Long.MAX_VALUE))),
                SimpleTest.simpleTest("isFlatValue 70", () -> Assertions.assertTrue(FlattersCommon.isFlatValue(Float.MAX_VALUE))),
                SimpleTest.simpleTest("isFlatValue 80", () -> Assertions.assertTrue(FlattersCommon.isFlatValue(Double.MAX_VALUE))),
                SimpleTest.simpleTest("isFlatValue 90", () -> Assertions.assertTrue(FlattersCommon.isFlatValue("A String"))),
                SimpleTest.simpleTest("isFlatValue 100", () -> Assertions.assertTrue(FlattersCommon.isFlatValue(false))),
                SimpleTest.simpleTest("isFlatValue 110", () -> Assertions.assertFalse(FlattersCommon.isFlatValue(new BigInteger("1")))),
                SimpleTest.simpleTest("isFlatValue 120", () -> Assertions.assertFalse(FlattersCommon.isFlatValue(new BigDecimal("1")))),
                SimpleTest.simpleTest("isFlatValue 130", () -> Assertions.assertFalse(FlattersCommon.isFlatValue(new JSONObject()))),
                SimpleTest.simpleTest("isFlatValue 140", () -> Assertions.assertFalse(FlattersCommon.isFlatValue(new JSONArray()))),
                SimpleTest.simpleTest("isFlatValue 150", () -> Assertions.assertFalse(FlattersCommon.isFlatValue(new JSONValue()))),
                SimpleTest.delimer()).lock().
                dynamicTests(SimpleTest::dynamicTest, SimpleTest::used);
    }


}