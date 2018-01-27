package com.steammachine.jsonchecker.utils.jsondirectcomparison;

import com.steammachine.org.junit5.extensions.dynamictests.dynamictestparam.DynamicTestParams;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.*;
import com.steammachine.jsonchecker.types.JSONParam;
import com.steammachine.jsonchecker.types.NodeCheckResult;
import com.steammachine.jsonchecker.types.exceptions.ParamError;
import com.steammachine.jsonchecker.types.exceptions.ParamTypeError;
import com.steammachine.jsonchecker.utils.JSonDirectComparator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/*
*/

/**
 * 30.12.2017 10:21:46
 *
 * @author Vladimir Bogodukhov
 **/
class JSonDirectComparatorParamsCheck {

    private static final JSONObject STUB = CompareJSons.jsonFormString("{ \"a\": \"11\"}");

    private static DynamicTest paramCheck(CompareJSons param) {
        String paramName = param.testName() == null ? "" + param : param.testName();
        return DynamicTest.dynamicTest(paramName, () -> {
            NodeCheckResult context;
            try {
                context = JSonDirectComparator.compareJSonsObjects(
                        param.jsonObject(),
                        param.jsonObject2(),
                        param.pathRepresentation(),
                        param.params().build(), null, param.exclusions());

            } catch (Throwable throwable) {
                if (param.expectedError() == null) {
                    throw throwable;
                }
                if (!param.expectedError().isAssignableFrom(throwable.getClass())) {
                    Assertions.assertEquals(param.expectedError(), throwable.getClass());
                }
                return;
            }
            if (param.expectedError() != null) {
                Assertions.assertEquals(param.expectedError(), null);
            }

            if (param.resultHandler() != null) {
                param.resultHandler().accept(context);
            }
        });
    }

    private static class CustomParam implements JSONParam {
        private final String name;
        private final String valueToCompare;

        CustomParam(String name, String valueToCompare) {
            this.name = Objects.requireNonNull(name);
            this.valueToCompare = valueToCompare;
        }

        public static CustomParam of(String name, String valueToCompare) {
            return new CustomParam(name, valueToCompare);
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public String paramsRep() {
            return "";
        }

        @Override
        public void checkValues() {
        }

        @Override
        public boolean matches(Object VALUE) {
            return Objects.equals(valueToCompare, VALUE);
        }

        @Override
        public boolean matchesParam(JSONParam thatParam) {
            return false;
        }
    }

    @SuppressWarnings("UnnecessaryBoxing")
    @TestFactory
    Stream<DynamicTest> compareJSonsObjects() {
        return DynamicTestParams.of(
                CompareJSons.delimer(" ------------------------------ errors ---------------------------------"),
                CompareJSons.delimer(),

                CompareJSons.errorTest("errors 20", null, STUB, NullPointerException.class),
                CompareJSons.errorTest("errors 30", STUB, null, NullPointerException.class),
                CompareJSons.errorTest("errors 40", null, null, NullPointerException.class),
                CompareJSons.errorTest("errors 50", STUB, STUB, null).useParam(p -> p.single("param", "VALUE")),
                CompareJSons.errorTest("errors 60", STUB, STUB, null).useParam(p -> p.single("param", 1)),
                CompareJSons.errorTest("errors 70", STUB, STUB, null).useParam(p -> p.single("param", 1L)),
                CompareJSons.errorTest("errors 80", STUB, STUB, null).useParam(p -> p.single("param", (byte) 1)),
                CompareJSons.errorTest("errors 90", STUB, STUB, null).useParam(p -> p.single("param", (float) 1)),
                CompareJSons.errorTest("errors 100", STUB, STUB, null).useParam(p -> p.single("param", (double) 1)),
                CompareJSons.errorTest("errors 110", STUB, STUB, null).useParam(p -> p.single("param", true)),
                CompareJSons.errorTest("errors 120", STUB, STUB, null).useParam(p -> p.single("param", false)),
                CompareJSons.errorTest("errors 130", STUB, STUB, ParamTypeError.class).useParam(p -> p.single("param", new Object())),
                CompareJSons.errorTest("errors 140", STUB, STUB, ParamTypeError.class).useParam(p -> p.single("param", new BigInteger("1"))),
                CompareJSons.errorTest("errors 150", STUB, STUB, ParamTypeError.class).useParam(p -> p.single("param", new BigDecimal("1"))),

                CompareJSons.delimer(),
                CompareJSons.delimer(),
                CompareJSons.delimer(" ------------------------------ check params ---------------------------------"),
                CompareJSons.delimer(),
                CompareJSons.simpleTest(
                        "check params 10",
                        CompareJSons.jsonFormString("{ \"a\": \"11\"}"),
                        CompareJSons.jsonFormString("{ \"a\": \"11\"}"),
                        r -> Assertions.assertTrue(r.isSuccessful())).
                        useParam(p -> p.single("param", null))
                ,
                CompareJSons.simpleTest(
                        "check params 20",
                        CompareJSons.jsonFormString("{ \"a\": \"11\"}"),
                        CompareJSons.jsonFormString("{ \"a\": \"11\"}"),
                        r -> Assertions.assertTrue(r.isSuccessful())).
                        useParam(p -> p.single("param", 11))
                ,
                CompareJSons.simpleTest(
                        "check params 30",
                        CompareJSons.jsonFormString("{ \"a\": null}"),
                        CompareJSons.jsonFormString("{ \"a\": \"${param}\"}"),
                        r -> Assertions.assertTrue(r.isSuccessful())).
                        useParam(p -> p.single("param", null))
                ,
                CompareJSons.simpleTest(
                        "check params 40",
                        CompareJSons.jsonFormString("{ \"a\": true}"),
                        CompareJSons.jsonFormString("{ \"a\": \"${param}\"}"),
                        r -> Assertions.assertTrue(r.isSuccessful())).
                        useParam(p -> p.single("param", true))
                ,
                CompareJSons.simpleTest(
                        "check params 50",
                        CompareJSons.jsonFormString("{ \"a\": false}"),
                        CompareJSons.jsonFormString("{ \"a\": \"${param}\"}"),
                        r -> Assertions.assertFalse(r.isSuccessful())).
                        useParam(p -> p.single("param", true))
                ,
                CompareJSons.simpleTest(
                        "check params 60",
                        CompareJSons.jsonFormString("{ \"a\": false}"),
                        CompareJSons.jsonFormString("{ \"a\": \"${param}\"}"),
                        r -> Assertions.assertTrue(r.isSuccessful())).
                        useParam(p -> p.single("param", false))
                ,
                CompareJSons.simpleTest(
                        "check params 70",
                        CompareJSons.jsonFormString("{ \"a\": [false]}"),
                        CompareJSons.jsonFormString("{ \"a\": [\"${param}\"]}"),
                        r -> Assertions.assertTrue(r.isSuccessful())).
                        useParam(p -> p.single("param", false))
                ,
                CompareJSons.simpleTest(
                        "check params 80",
                        CompareJSons.jsonFormString("{ \"a\": [true]}"),
                        CompareJSons.jsonFormString("{ \"a\": [\"${param}\"]}"),
                        r -> Assertions.assertFalse(r.isSuccessful())).
                        useParam(p -> p.single("param", false))
                ,
                CompareJSons.simpleTest(
                        "check params 90 - comparing the same param",
                        CompareJSons.jsonFormString("{ \"a\": [\"${param}\"]}"),
                        CompareJSons.jsonFormString("{ \"a\": [\"${param}\"]}"),
                        r -> Assertions.assertTrue(r.isSuccessful())).
                        useParam(p -> p.single("param", false))
                ,
                CompareJSons.simpleTest(
                        "check params 100 - comparing the same param",
                        CompareJSons.jsonFormString("{ \"a\": [\"${param}\"]}"),
                        CompareJSons.jsonFormString("{ \"a\": [\"${param}\"]}"),
                        r -> Assertions.assertTrue(r.isSuccessful())).
                        useParam(p -> p.single("param", true))
                ,
                CompareJSons.simpleTest(
                        "check params 110 - comparing the same param",
                        CompareJSons.jsonFormString("{ \"a\": [\"${param}\"]}"),
                        CompareJSons.jsonFormString("{ \"a\": [\"${param}\"]}"),
                        r -> Assertions.assertTrue(r.isSuccessful())).
                        useParam(p -> p.single("param", 111.2211321))
                ,

                CompareJSons.simpleTest(
                        "check params 120",
                        CompareJSons.jsonFormString("{ \"a\": [111]}"),
                        CompareJSons.jsonFormString("{ \"a\": [\"${param}\"]}"),
                        r -> Assertions.assertTrue(r.isSuccessful())).
                        useParam(p -> p.single("param", 111L))
                ,

                CompareJSons.simpleTest(
                        "check params 130",
                        CompareJSons.jsonFormString("{ \"a\": [111]}"),
                        CompareJSons.jsonFormString("{ \"a\": [\"${param}\"]}"),
                        r -> Assertions.assertTrue(r.isSuccessful())).
                        useParam(p -> p.single("param", 111))
                ,

                CompareJSons.simpleTest(
                        "check params 140",
                        CompareJSons.jsonFormString("{ \"a\": [111.2]}"),
                        CompareJSons.jsonFormString("{ \"a\": [\"${param}\"]}"),
                        r -> Assertions.assertFalse(r.isSuccessful())).
                        useParam(p -> p.single("param", new Float(111.2)))
                ,

                CompareJSons.simpleTest(
                        "check params 150",
                        CompareJSons.jsonFormString("{ \"a\": [111.2]}"),
                        CompareJSons.jsonFormString("{ \"a\": [\"${param}\"]}"),
                        r -> Assertions.assertTrue(r.isSuccessful())).
                        useParam(p -> p.single("param", 111.2))
                ,


                CompareJSons.delimer("_____________________ Params With The Same names ______________________________"),
                CompareJSons.simpleTest(
                        "ParamsWithTheSameNames10",
                        CompareJSons.jsonFormString("{ \"a\": [111.2]}"),
                        CompareJSons.jsonFormString("{ \"a\": [\"${param}\"]}"),
                        r -> Assertions.assertTrue(r.isSuccessful())).
                        useParam(p -> p.single("param", 11.2).single("param", 111.2))
                ,


                CompareJSons.delimer("__________________________ ChecksRegexpParams _________________________________"),
                CompareJSons.simpleTest(
                        "ChecksRegexpParams 10",
                        CompareJSons.jsonFormString("{ \"a\": [\"Hola\"]}"),
                        CompareJSons.jsonFormString("{ \"a\": [\"${param}\"]}"),
                        r -> Assertions.assertTrue(r.isSuccessful())).
                        useParam(p -> p.regexp("param", Pattern.compile("^Hola$")))
                ,
                CompareJSons.simpleTest(
                        "ChecksRegexpParams 20",
                        CompareJSons.jsonFormString("{ \"a\": [\"Hola\"]}"),
                        CompareJSons.jsonFormString("{ \"a\": [\"${param}\"]}"),
                        r -> Assertions.assertTrue(r.isSuccessful())).
                        useParam(p -> p.regexp("param", "^Hola$"))
                ,
                CompareJSons.simpleTest(
                        "ChecksRegexpParams 30",
                        CompareJSons.jsonFormString("{ \"a\": [\"11111\"]}"),
                        CompareJSons.jsonFormString("{ \"a\": [\"${param}\"]}"),
                        r -> Assertions.assertTrue(r.isSuccessful())).
                        useParam(p -> p.regexp("param", Pattern.compile("[0-9]+")))
                ,
                CompareJSons.simpleTest(
                        "ChecksRegexpParams 40",
                        CompareJSons.jsonFormString("{ \"a\": [\"11111\"]}"),
                        CompareJSons.jsonFormString("{ \"a\": [\"${param}\"]}"),
                        r -> Assertions.assertTrue(r.isSuccessful())).
                        useParam(p -> p.regexp("param", "[0-9]+"))
                ,
                CompareJSons.simpleTest(
                        "ChecksRegexpParams 50 not a String VALUE",
                        CompareJSons.jsonFormString("{ \"a\": [11111]}"),
                        CompareJSons.jsonFormString("{ \"a\": [\"${param}\"]}"),
                        r -> Assertions.assertFalse(r.isSuccessful())).
                        useParam(p -> p.regexp("param", Pattern.compile("[0-9]+")))
                ,
                CompareJSons.simpleTest(
                        "ChecksRegexpParams 60 not a String VALUE",
                        CompareJSons.jsonFormString("{ \"a\": [11111.0]}"),
                        CompareJSons.jsonFormString("{ \"a\": [\"${param}\"]}"),
                        r -> Assertions.assertFalse(r.isSuccessful())).
                        useParam(p -> p.regexp("param", Pattern.compile("[0-9]+")))
                ,
                CompareJSons.simpleTest(
                        "ChecksRegexpParams 63 not a String VALUE",
                        CompareJSons.jsonFormString("{ \"a\": [true]}"),
                        CompareJSons.jsonFormString("{ \"a\": [\"${param}\"]}"),
                        r -> Assertions.assertFalse(r.isSuccessful())).
                        useParam(p -> p.regexp("param", Pattern.compile("[0-9]+")))
                ,
                CompareJSons.simpleTest(
                        "ChecksRegexpParams 66 not a String VALUE",
                        CompareJSons.jsonFormString("{ \"a\": [null]}"),
                        CompareJSons.jsonFormString("{ \"a\": [\"${param}\"]}"),
                        r -> Assertions.assertFalse(r.isSuccessful())).
                        useParam(p -> p.regexp("param", Pattern.compile("[0-9]+")))
                ,
                CompareJSons.simpleTest(
                        "ChecksRegexpParams 70(two regexp params are not matched)",
                        CompareJSons.jsonFormString("{ \"a\": [\"${param}\"]}"),
                        CompareJSons.jsonFormString("{ \"a\": [\"${param}\"]}"),
                        r -> Assertions.assertFalse(r.isSuccessful())).
                        useParam(p -> p.regexp("param", Pattern.compile("[0-9]+")))
                ,
                CompareJSons.simpleTest(
                        "ChecksRegexpParams 80(two regexp params are not matched)",
                        CompareJSons.jsonFormString("{ \"a\": [\"${param}\"]}"),
                        CompareJSons.jsonFormString("{ \"a\": [\"${param}\"]}"),
                        r -> Assertions.assertFalse(r.isSuccessful())).
                        useParam(p -> p.regexp("param", "[0-9]+"))
                ,
                CompareJSons.simpleTest(
                        "ChecksRegexpParams 90(two regexp params are not matched)",
                        CompareJSons.jsonFormString("{ \"a\": [\"abcde1abcde\"]}"),
                        CompareJSons.jsonFormString("{ \"a\": [\"${param}\"]}"),
                        r -> Assertions.assertFalse(r.isSuccessful())).
                        useParam(p -> p.regexp("param", Pattern.compile("[^0-9]+")))
                ,
                CompareJSons.simpleTest(
                        "ChecksRegexpParams 100(two regexp params are not matched)",
                        CompareJSons.jsonFormString("{ \"a\": [\"abcde1abcde\"]}"),
                        CompareJSons.jsonFormString("{ \"a\": [\"${param}\"]}"),
                        r -> Assertions.assertFalse(r.isSuccessful())).
                        useParam(p -> p.regexp("param", "[^0-9]+"))
                ,
                CompareJSons.simpleTest(
                        "ChecksRegexpParams 110(two regexp params are not matched)",
                        CompareJSons.jsonFormString("{ \"a\": [\"abcdeabcde\"]}"),
                        CompareJSons.jsonFormString("{ \"a\": [\"${param}\"]}"),
                        r -> Assertions.assertTrue(r.isSuccessful())).
                        useParam(p -> p.regexp("param", Pattern.compile("[^0-9]+")))
                ,
                CompareJSons.simpleTest(
                        "ChecksRegexpParams 120(two regexp params are not matched)",
                        CompareJSons.jsonFormString("{ \"a\": [\"abcdeabcde\"]}"),
                        CompareJSons.jsonFormString("{ \"a\": [\"${param}\"]}"),
                        r -> Assertions.assertTrue(r.isSuccessful())).
                        useParam(p -> p.regexp("param", "[^0-9]+"))
                ,
                CompareJSons.delimer(" Custom params "),
                CompareJSons.simpleTest(
                        "Custom params 10",
                        CompareJSons.jsonFormString("{ \"a\": \"abcde1abcde\"}"),
                        CompareJSons.jsonFormString("{ \"a\": \"${param}\"}"),
                        r -> Assertions.assertTrue(r.isSuccessful())).
                        useParam(p -> p.param("param", "abcde1abcde"::equals))
                ,

                CompareJSons.delimer("------------------------- Custom params always match --------------------------"),
                CompareJSons.simpleTest(
                        "Custom params always match 10",
                        CompareJSons.jsonFormString("{ \"a\": null}"),
                        CompareJSons.jsonFormString("{ \"a\": \"${param}\"}"),
                        r -> Assertions.assertTrue(r.isSuccessful())).
                        useParam(p -> p.param("param", VALUE -> true))
                ,
                CompareJSons.simpleTest(
                        "Custom params always match 20",
                        CompareJSons.jsonFormString("{ \"a\": 11}"),
                        CompareJSons.jsonFormString("{ \"a\": \"${param}\"}"),
                        r -> Assertions.assertTrue(r.isSuccessful())).
                        useParam(p -> p.param("param", VALUE -> true))
                ,
                CompareJSons.simpleTest(
                        "Custom params always match 30",
                        CompareJSons.jsonFormString("{ \"a\": false}"),
                        CompareJSons.jsonFormString("{ \"a\": \"${param}\"}"),
                        r -> Assertions.assertTrue(r.isSuccessful())).
                        useParam(p -> p.param("param", VALUE -> true))
                ,
                CompareJSons.simpleTest(
                        "Custom params always match 40",
                        CompareJSons.jsonFormString("{ \"a\": \" \"}"),
                        CompareJSons.jsonFormString("{ \"a\": \"${param}\"}"),
                        r -> Assertions.assertTrue(r.isSuccessful())).
                        useParam(p -> p.param("param", VALUE -> true))
                ,
                CompareJSons.delimer("------------------------- Custom params never match --------------------------"),
                CompareJSons.simpleTest(
                        "Custom params never match 10",
                        CompareJSons.jsonFormString("{ \"a\": null}"),
                        CompareJSons.jsonFormString("{ \"a\": \"${param}\"}"),
                        r -> Assertions.assertFalse(r.isSuccessful())).
                        useParam(p -> p.param("param", VALUE -> false))
                ,
                CompareJSons.simpleTest(
                        "Custom params never match 20",
                        CompareJSons.jsonFormString("{ \"a\": 11}"),
                        CompareJSons.jsonFormString("{ \"a\": \"${param}\"}"),
                        r -> Assertions.assertFalse(r.isSuccessful())).
                        useParam(p -> p.param("param", VALUE -> false))
                ,
                CompareJSons.simpleTest(
                        "Custom params never match 30",
                        CompareJSons.jsonFormString("{ \"a\": false}"),
                        CompareJSons.jsonFormString("{ \"a\": \"${param}\"}"),
                        r -> Assertions.assertFalse(r.isSuccessful())).
                        useParam(p -> p.param("param", VALUE -> false))
                ,
                CompareJSons.simpleTest(
                        "Custom params never match 40",
                        CompareJSons.jsonFormString("{ \"a\": \" \"}"),
                        CompareJSons.jsonFormString("{ \"a\": \"${param}\"}"),
                        r -> Assertions.assertFalse(r.isSuccessful())).
                        useParam(p -> p.param("param", VALUE -> false))
                ,

                CompareJSons.delimer(" -------------------- Custom params of second type  -------------------------"),
                CompareJSons.simpleTest(
                        "Custom params of second type 10",
                        CompareJSons.jsonFormString("{ \"a\": \"1234\"}"),
                        CompareJSons.jsonFormString("{ \"a\": \"${param}\"}"),
                        r -> Assertions.assertTrue(r.isSuccessful())).
                        useParam(p -> p.param(CustomParam.of("param", "1234")))
                ,

                CompareJSons.simpleTest(
                        "Custom params of second type 20",
                        CompareJSons.jsonFormString("{ \"a\": \"1234\"}"),
                        CompareJSons.jsonFormString("{ \"a\": \"${param}\"}"),
                        r -> Assertions.assertFalse(r.isSuccessful())).
                        useParam(p -> p.param(CustomParam.of("param", "123")))
                ,


                CompareJSons.delimer("Tests by feedback ")
                ,

                CompareJSons.simpleTest(
                        "Tests by feedback 10",
                        CompareJSons.jsonFormPath(JSonDirectComparatorParamsCheck.class, "res/resource_by_feedback_real_data1.json"),
                        CompareJSons.jsonFormPath(JSonDirectComparatorParamsCheck.class, "res/resource_by_feedback_template1.json"),
                        r -> Assertions.assertFalse(r.isSuccessful()))
                ,

                CompareJSons.simpleTest(
                        "Tests by feedback 20",
                        CompareJSons.jsonFormPath(JSonDirectComparatorParamsCheck.class, "res/resource_by_feedback_real_data2.json"),
                        CompareJSons.jsonFormPath(JSonDirectComparatorParamsCheck.class, "res/resource_by_feedback_template2.json"),
                        r -> Assertions.assertFalse(r.isSuccessful()))
                ,

                CompareJSons.simpleTest(
                        "Tests by feedback 30",
                        CompareJSons.jsonFormPath(JSonDirectComparatorParamsCheck.class, "res/resource_by_feedback_real_data2.json"),
                        CompareJSons.jsonFormPath(JSonDirectComparatorParamsCheck.class, "res/resource_by_feedback_template2.json"),
                        r -> Assertions.assertFalse(r.isSuccessful()))
                ,


                CompareJSons.simpleTest(
                        "Tests by feedback 40",
                        CompareJSons.jsonFormString("{\"a\" : 11}"),
                        CompareJSons.jsonFormString("{\"a\" : \"${param}\"}"),
                        r -> Assertions.assertTrue(r.isSuccessful())).
                        useParam(builder -> builder.single("param", 11))
                ,

                CompareJSons.simpleTest(
                        "Tests by feedback 50",
                        CompareJSons.jsonFormString("{\"a\" : \"11\"}"),
                        CompareJSons.jsonFormString("{\"a\" : \"${param}\"}"),
                        r -> Assertions.assertFalse(r.isSuccessful())).
                        useParam(builder -> builder.single("param", new Integer(11)))
                ,

                CompareJSons.simpleTest(
                        "Tests by feedback 60",
                        CompareJSons.jsonFormString("{\"a\" : 11}"),
                        CompareJSons.jsonFormString("{\"a\" : \"${param}\"}"),
                        r -> Assertions.assertTrue(r.isSuccessful())).
                        useParam(builder -> builder.single("param", new Integer(11)))
                ,

                CompareJSons.simpleTest(
                        "Tests by feedback 70",
                        CompareJSons.jsonFormString("{\"a\" : 11}"),
                        CompareJSons.jsonFormString("{\"a\" : \"${param}\"}"),
                        r -> Assertions.assertTrue(r.isSuccessful())).
                        useParam(builder -> builder.single("param", new Long(11)))
                ,

                CompareJSons.delimer()
        ).lock().dynamicTests(JSonDirectComparatorParamsCheck::paramCheck, CompareJSons::used);
    }


    @Tag("DebugRun")
    @TestFactory
    Stream<DynamicTest> compareJSonsObjectsDebug() {

       return DynamicTestParams.of(
                CompareJSons.delimer(" ------------------------------ errors ---------------------------------"),

                CompareJSons.simpleTest(
                        "check params 150",
                        CompareJSons.jsonFormString("{ \"a\": [111.2]}"),
                        CompareJSons.jsonFormString("{ \"a\": [\"${param}\"]}"),
                        r -> Assertions.assertTrue(r.isSuccessful())).
                        useParam(p -> p.single("param", 111.2))
                ,
                CompareJSons.delimer()).lock().dynamicTests(JSonDirectComparatorParamsCheck::paramCheck, CompareJSons::used);
    }
}


