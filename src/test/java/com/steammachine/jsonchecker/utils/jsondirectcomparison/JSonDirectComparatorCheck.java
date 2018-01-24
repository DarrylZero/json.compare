package com.steammachine.jsonchecker.utils.jsondirectcomparison;

import com.steammachine.jsonchecker.defaults.MonkeyPathRepresentation;
import com.steammachine.jsonchecker.types.NodeCheckResult;
import com.steammachine.jsonchecker.types.PathRepresentation;
import com.steammachine.jsonchecker.types.exceptions.WrongDataFormat;
import com.steammachine.jsonchecker.utils.JSonDirectComparator;
import com.steammachine.common.utils.ResourceUtils;

import com.steammachine.org.junit5.extensions.dynamictests.dynamictestparam.DynamicTestParams;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Stream;

import static com.steammachine.common.utils.commonutils.CommonUtils.*;
import static com.steammachine.jsonchecker.utils.jsondirectcomparison.CompareJSons.jsonFormPath;


/**
 * Created by Vladimir Bogoduhov on 01.12.2017.
 *
 * @author Vladimir Bogoduhov
 */
class JSonDirectComparatorCheck {


    private static final Charset CHARSET = Charset.forName("UTF-8");

    @Test
    void testName() {
        Assertions.assertEquals("com.steammachine.jsonchecker.utils.JSonDirectComparator",
                JSonDirectComparator.class.getName());
    }

    /* --------------------------------------------- comapreJSonStrings -------------------------------------------  */


    @SuppressWarnings("WeakerAccess")
    private static class ComapreJSonStringsParam {
        private static final ComapreJSonStringsParam SKIPPED =
                new ComapreJSonStringsParam("SKIPPED", null, null, null, false, false);

        private final Class<? extends Throwable> expectedError;
        private final String data1;
        private final String data2;
        private final boolean expectedResult;
        private final boolean used;
        private final String testName;
        private final PathRepresentation representation = MonkeyPathRepresentation.REPRESENTATION;

        public ComapreJSonStringsParam(
                String testName,
                Class<? extends Throwable> expectedError,
                String data1,
                String data2,
                boolean expectedResult,
                boolean used) {
            this.testName = Objects.requireNonNull(testName);
            this.expectedError = expectedError;
            this.data1 = data1;
            this.data2 = data2;
            this.expectedResult = expectedResult;
            this.used = used;
        }

        public ComapreJSonStringsParam(
                String testName,
                Class<? extends Throwable> expectedError,
                String data1,
                String data2,
                boolean expectedResult) {
            this(testName, expectedError, data1, data2, expectedResult, true);
        }

        public static ComapreJSonStringsParam delimetr(String... data) {
            return SKIPPED;
        }

        public Class<? extends Throwable> getExpectedError() {
            return expectedError;
        }

        public String getData1() {
            return data1;
        }

        public String getData2() {
            return data2;
        }

        public PathRepresentation representation() {
            return representation;
        }

        public boolean expectedResult() {
            return expectedResult;
        }

        public boolean used() {
            return used;
        }

        public String testName() {
            return testName;
        }

        @Override
        public String toString() {
            return "ComapreJSonStringsParam{" +
                    "expectedError=" + expectedError +
                    ", data1='" + data1 + '\'' +
                    ", data2='" + data2 + '\'' +
                    ", expectedResult=" + expectedResult +
                    ", used=" + used +
                    '}';
        }

        public static ComapreJSonStringsParam expectError(String testName, String data1, String data2, Class<? extends Throwable> expectedError) {
            return new ComapreJSonStringsParam(testName, expectedError, data1, data2, false);
        }

        public static ComapreJSonStringsParam comaprison(String testName, String data1, String data2, boolean result) {
            return new ComapreJSonStringsParam(testName, null, data1, data2, result);
        }
    }

    private static Executable compareJSonStringsExecutable(ComapreJSonStringsParam param) {
        return () -> {
            Boolean result;
            try {
                result = JSonDirectComparator.comapreJSonStrings(param.getData1(), param.getData2(),
                        param.representation()); /* <--- Это тестируемый метод <--- */
                if (param.getExpectedError() != null) {
                    Assertions.assertEquals(null, param.getExpectedError());
                }
            } catch (Throwable throwable) {
                if (param.getExpectedError() == null) {
                    Assertions.assertEquals(param.getExpectedError(), throwable.getClass());
                } else if (!param.getExpectedError().isAssignableFrom(throwable.getClass())) {
                    Assertions.assertEquals(param.getExpectedError(), throwable.getClass());
                }
                return;
            }
            Objects.requireNonNull(result);
            Assertions.assertEquals(param.expectedResult(), (boolean) result);
        };
    }

    private static String string(String resourcePath) {
        return suppress(() -> ResourceUtils.
                loadResourceAsString(JSonDirectComparatorCheck.class, resourcePath, CHARSET));
    }

    private static JSONObject jsonFromPath(String resourcePath) {
        return suppress(() -> (JSONObject) new JSONParser().parse(
                suppress(() -> ResourceUtils.loadResourceAsString(JSonDirectComparatorCheck.class, resourcePath, CHARSET))));

    }


    @TestFactory
    Stream<DynamicTest> comapreJSonStrings() {
        return DynamicTestParams.of(
                ComapreJSonStringsParam.expectError("expectError10", null, "", NullPointerException.class),
                ComapreJSonStringsParam.expectError("expectError20", "", null, NullPointerException.class),
                ComapreJSonStringsParam.expectError("expectError30", "dasda", "", WrongDataFormat.class),
                ComapreJSonStringsParam.expectError("expectError40", "", "dasda", WrongDataFormat.class),
                ComapreJSonStringsParam.comaprison("comaprison10", "{              }", "{}", true),
                ComapreJSonStringsParam.comaprison("comaprison20", "{}", "{}", true),
                ComapreJSonStringsParam.comaprison("comaprison30", "{}", "{\"q\" : 12}", false),
                ComapreJSonStringsParam.comaprison("comaprison40", "{\"q\":       13}", "{\"q\" : 13}", true),
                ComapreJSonStringsParam.comaprison("comaprison50", "{\"q\": 14}", "             {\"q\" : 14}", true),
                ComapreJSonStringsParam.comaprison("comaprison60", "{\"q\": 15}", "             {\"q\" : 15}", true),
                ComapreJSonStringsParam.comaprison("comaprison70", "{\"q\": 16}", "             {\"q\" : \"16\"}", false),
                ComapreJSonStringsParam.comaprison("comaprison80", "{ \"a\": 17 }", "{ \"a\": 17 }", true),
                ComapreJSonStringsParam.comaprison("comaprison90", "{ \"a\": 18 }", "{ \"a\": 18 }", true),
                ComapreJSonStringsParam.comaprison("comaprison100", "{ \"a\": \"19\" }", "{ \"a\": 19 }", false),
                ComapreJSonStringsParam.comaprison("comaprison110", string("res/resource_big_json.json"), string("res/resource_big_json.json"), true),
                ComapreJSonStringsParam.comaprison("comaprison120", string("res/resource_well_formed_json.json"), string("res/resource_well_formed_json.json"), true),
                ComapreJSonStringsParam.comaprison("comaprison130", string("res/resource_well_formed_json.json"), string("res/resource_big_json.json"), false),
                ComapreJSonStringsParam.comaprison("comaprison140", string("res/resource_big_json.json"), string("res/resource_well_formed_json.json"), false),
                ComapreJSonStringsParam.comaprison("comaprison150", string("res/resource_well_formed_json.json"), string("res/resource_well_formed_json2.json"), true),
                ComapreJSonStringsParam.comaprison("comaprison160", "{\"desc\": \"w\",  \"defaultclassalias\": \"TestType\"}",
                        "{\"defaultclassalias\": \"TestType\", \"desc\": \"w\"}", true),
                ComapreJSonStringsParam.delimetr()
        ).
                lock().dynamicTests(ComapreJSonStringsParam::testName, JSonDirectComparatorCheck::compareJSonStringsExecutable,
                DynamicTest::dynamicTest, ComapreJSonStringsParam::used);
    }

    @TestFactory
    @Tag("DebugRun")
    Stream<DynamicTest> comapreJSonStrings_Debug() {
        return DynamicTestParams.of(
                ComapreJSonStringsParam.delimetr(),

                ComapreJSonStringsParam.comaprison("comaprison10", "{              }", "{}", true),

                ComapreJSonStringsParam.delimetr()).
                lock().dynamicTests(ComapreJSonStringsParam::testName, JSonDirectComparatorCheck::compareJSonStringsExecutable,
                DynamicTest::dynamicTest, ComapreJSonStringsParam::used);
    }


    @Test
    void comapreJSonStringsSpeedTest() throws Exception {
        String bigString = string("res/resource_big_json.json");
        long measuredTime = measureTime(() -> JSonDirectComparator.comapreJSonStrings(bigString, bigString,
                MonkeyPathRepresentation.REPRESENTATION), 10);
        Assertions.assertTrue(measuredTime < 10000);
    }

    @Test
    void comapreJSonStrings10() {
        Assertions.assertFalse(JSonDirectComparator.comapreJSonStrings(string("res/resource_to_compare1.json"),
                string("res/resource_to_compare2.json"), MonkeyPathRepresentation.REPRESENTATION));
    }

    @Test
    void comapreJSonStrings30() {
        Assertions.assertFalse(JSonDirectComparator.comapreJSonStrings(
                string("res/resource_to_compare1.json"),
                string("res/resource_to_compare3.json"), MonkeyPathRepresentation.REPRESENTATION));
    }

    @Test
    void comapreJSonStrings40() {
        Assertions.assertFalse(JSonDirectComparator.compareJSonsObjects(
                jsonFromPath("res/resource_to_compare1.json"),
                jsonFromPath("res/resource_to_compare3.json"),
                MonkeyPathRepresentation.REPRESENTATION).isSuccessful());
    }

    @Test
    void comapreJSonStrings50() {
        NodeCheckResult result = JSonDirectComparator.compareJSonsObjects(
                jsonFromPath("res/resource_to_compare1.json"),
                jsonFromPath("res/resource_to_compare3.json"),
                MonkeyPathRepresentation.REPRESENTATION);
        Assertions.assertFalse(result.isSuccessful());

    }

    @Test
    void comapreJSonStrings60() {
        NodeCheckResult result = JSonDirectComparator.compareJSonsObjects(
                jsonFromPath("res/resource_to_compare1.json"),
                jsonFromPath("res/resource_to_compare4.json"),
                MonkeyPathRepresentation.REPRESENTATION);
        Assertions.assertTrue(result.isSuccessful());

    }

    /* --------------------------------------------- comapreJSonStream -------------------------------------------  */


    @SuppressWarnings("WeakerAccess")
    private static class ComapreJSonStreamParam {
        private static final ComapreJSonStreamParam SKIPPED =
                new ComapreJSonStreamParam("SKIPPED", null, null, null, false, false);

        private final String testName;
        private final Class<? extends Throwable> expectedError;
        private final InputStream data1;
        private final InputStream data2;
        private final boolean expectedResult;
        private final boolean used;
        private final PathRepresentation representation = MonkeyPathRepresentation.REPRESENTATION;

        public ComapreJSonStreamParam(
                String testName,
                Class<? extends Throwable> expectedError,
                InputStream data1,
                InputStream data2,
                boolean expectedResult,
                boolean used) {
            this.testName = Objects.requireNonNull(testName);
            this.expectedError = expectedError;
            this.data1 = data1;
            this.data2 = data2;
            this.expectedResult = expectedResult;
            this.used = used;
        }

        public ComapreJSonStreamParam(
                String testName,
                Class<? extends Throwable> expectedError,
                InputStream data1,
                InputStream data2,
                boolean expectedResult) {
            this(testName, expectedError, data1, data2, expectedResult, true);
        }

        public Class<? extends Throwable> getExpectedError() {
            return expectedError;
        }

        public InputStream data1() {
            return data1;
        }

        public InputStream data2() {
            return data2;
        }

        public PathRepresentation representation() {
            return representation;
        }

        public boolean expectedResult() {
            return expectedResult;
        }

        public boolean isUsed() {
            return used;
        }

        @Override
        public String toString() {
            return "ComapreJSonStreamParam{" +
                    "expectedError=" + expectedError +
                    ", data1=" + data1 +
                    ", data2=" + data2 +
                    ", expectedResult=" + expectedResult +
                    ", used=" + used +
                    '}';
        }

        public static ComapreJSonStreamParam expectError(
                String testName,
                InputStream data1,
                InputStream data2,
                Class<? extends Throwable> expectedError) {
            return new ComapreJSonStreamParam(testName, expectedError, data1, data2, false);
        }

        public static ComapreJSonStreamParam comaprison(
                String testName,
                InputStream data1,
                InputStream data2,
                boolean result) {
            return new ComapreJSonStreamParam(testName, null, data1, data2, result);
        }

        public String testName() {
            return testName;
        }
    }

    private static DynamicTest comapreJSonStreamParamTest(ComapreJSonStreamParam param) {
        return DynamicTest.dynamicTest(param.testName(), () -> {
            Boolean result = null;
            try {
                result = JSonDirectComparator.comapreJSonStreams(param.data1(), param.data2(),
                        param.representation()); /* <--- Это тестируемый метод <--- */
                if (param.getExpectedError() != null) {
                    Assertions.assertEquals(null, param.getExpectedError());
                }
            } catch (Throwable throwable) {
                if (param.getExpectedError() == null) {
                    Assertions.assertEquals(param.getExpectedError(), throwable.getClass());
                } else if (!param.getExpectedError().isAssignableFrom(throwable.getClass())) {
                    Assertions.assertEquals(param.getExpectedError(), throwable.getClass());
                }
                return;
            }
            Objects.requireNonNull(result);
            Assertions.assertEquals(param.expectedResult(), result);
        });
    }

    private static InputStream fromString(String data) {
        Objects.requireNonNull(data);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        suppressWOResult(() -> stream.write(data.getBytes(CHARSET)));
        suppressWOResult(stream::flush);
        return new ByteArrayInputStream(stream.toByteArray());
    }

    private static InputStream fromPath(String path) {
        Objects.requireNonNull(path);
        return ResourceUtils.loadResourceByRelativePath(JSonDirectComparatorCheck.class, path);
    }

    @TestFactory
    Stream<DynamicTest> comapreJSonStream() {
        return DynamicTestParams.of(
                ComapreJSonStreamParam.expectError("expectError10", null, fromString(""), NullPointerException.class),
                ComapreJSonStreamParam.expectError("expectError20", fromString(""), null, NullPointerException.class),
                ComapreJSonStreamParam.expectError("expectError30", fromString("dasda"), fromString(""), WrongDataFormat.class),
                ComapreJSonStreamParam.expectError("expectError40", fromString("NOT A JSON"), fromString("NOT A JSON"), WrongDataFormat.class),
                ComapreJSonStreamParam.comaprison("comaprison10", fromString("{              }"), fromString("{}"), true),
                ComapreJSonStreamParam.comaprison("comaprison20", fromString("{}"), fromString("{}"), true),
                ComapreJSonStreamParam.comaprison("comaprison30", fromString("{}"), fromString("{\"q\" : 12}"), false),
                ComapreJSonStreamParam.comaprison("comaprison40", fromString("{\"q\":       12}"), fromString("{\"q\" : 12}"), true),
                ComapreJSonStreamParam.comaprison("comaprison50", fromString("{\"q\": 12}"), fromString("             {\"q\" : 12}"), true),
                ComapreJSonStreamParam.comaprison("comaprison60", fromString("{\"q\": 12}"), fromString("             {\"q\" : 13}"), false),
                ComapreJSonStreamParam.comaprison("comaprison70", fromString("{\"q\": 12}"), fromString("             {\"q\" : \"12\"}"), false),
                ComapreJSonStreamParam.comaprison("comaprison80", fromString("{ \"a\": 17 }"), fromString("{ \"a\": 17 }"), true),
                ComapreJSonStreamParam.comaprison("comaprison90", fromString("{ \"a\": 16 }"), fromString("{ \"a\": 17 }"), false),
                ComapreJSonStreamParam.comaprison("comaprison100", fromString("{ \"a\": \"17\" }"), fromString("{ \"a\": 17 }"), false),
                ComapreJSonStreamParam.comaprison("comaprison110", fromPath("res/resource_big_json.json"), fromPath("res/resource_big_json.json"), true),
                ComapreJSonStreamParam.comaprison("comaprison120", fromPath("res/resource_well_formed_json.json"), fromPath("res/resource_well_formed_json.json"), true),
                ComapreJSonStreamParam.comaprison("comaprison130", fromPath("res/resource_well_formed_json.json"), fromPath("res/resource_big_json.json"), false),
                ComapreJSonStreamParam.comaprison("comaprison140", fromPath("res/resource_big_json.json"), fromPath("res/resource_well_formed_json.json"), false),
                ComapreJSonStreamParam.comaprison("comaprison150", fromPath("res/resource_well_formed_json.json"), fromPath("res/resource_well_formed_json2.json"), true),
                ComapreJSonStreamParam.comaprison("comaprison160", fromString("{\"desc\": \"w\",  \"defaultclassalias\": \"TestType\"}"),
                        fromString("{\"defaultclassalias\": \"TestType\", \"desc\": \"w\"}"), true),
                ComapreJSonStreamParam.SKIPPED).
                lock().dynamicTests(JSonDirectComparatorCheck::comapreJSonStreamParamTest, ComapreJSonStreamParam::isUsed);
    }

    @TestFactory
    @Tag("DebugRun")
    Stream<DynamicTest> comapreJSonStreamDebug() {
        return DynamicTestParams.of(
                ComapreJSonStreamParam.comaprison("comaprison110", fromPath("res/resource_big_json.json"), fromPath("res/resource_big_json.json"), true),
                ComapreJSonStreamParam.SKIPPED).
                lock().dynamicTests(JSonDirectComparatorCheck::comapreJSonStreamParamTest, ComapreJSonStreamParam::isUsed);
    }

    @Test
    void comapreJSonStreamSpeedTest() throws Exception {
        long measuredTime = measureTime(() -> {
            try (InputStream stream1 = ResourceUtils.loadResourceByRelativePath(JSonDirectComparatorCheck.class, "res/resource_big_json.json")) {
                try (InputStream stream2 = ResourceUtils.loadResourceByRelativePath(JSonDirectComparatorCheck.class, "res/resource_big_json.json")) {
                    JSonDirectComparator.comapreJSonStreams(stream1, stream2, MonkeyPathRepresentation.REPRESENTATION);
                }
            }
        }, 10);
        Assertions.assertTrue(measuredTime < 45_000);
    }


    /* ---------------------------------------- compareJSonsObjects -------------------------------------------------------- */

    @TestFactory
    Stream<DynamicTest> compareJSons() {

        return DynamicTestParams.of(
                SimpleTest.delimer("expected errors tests"),
                SimpleTest.simpleTest("compare the same json", () -> {
                    NodeCheckResult result = JSonDirectComparator.compareJSonsObjects(
                            jsonFormPath(JSonDirectComparatorCheck.class, "res/resource_big_json.json"),
                            jsonFormPath(JSonDirectComparatorCheck.class, "res/resource_big_json.json"),
                            MonkeyPathRepresentation.REPRESENTATION);
                    Assertions.assertTrue(result.isSuccessful());
                    Assertions.assertTrue(result.messages().isEmpty());
                }),

                SimpleTest.simpleTest("compare big slightly different jsons", () -> {
                    NodeCheckResult result = JSonDirectComparator.compareJSonsObjects(
                            jsonFormPath(JSonDirectComparatorCheck.class, "res/resource_big_json.json"),
                            jsonFormPath(JSonDirectComparatorCheck.class, "res/resource_big_json2.json"),
                            MonkeyPathRepresentation.REPRESENTATION);
                    Assertions.assertFalse(result.isSuccessful());
                    Assertions.assertFalse(result.messages().isEmpty());
                    Assertions.assertEquals(
                            sortedListFromArray(
                                    "values by path /beltway_map_dlg/background/bottom_panel_3_section/coords/[0] are not equal. 251.0 != 99.0",
                                    "values by path /beltway_map_dlg/background/bottom_panel_3_section/coords/[1] are not equal. 99.0 != 251.0",
                                    "values by path /beltway_map_dlg/background/bottom_panel_3_section/type are not equal. widget != widget2"),
                            sortedList(result.messages())
                    );
                }),

                SimpleTest.simpleTest("compare different json 10", () -> {
                    NodeCheckResult result = JSonDirectComparator.compareJSonsObjects(
                            jsonFormPath(JSonDirectComparatorCheck.class, "res/resource_json_simple.json"),
                            jsonFormPath(JSonDirectComparatorCheck.class, "res/resource_empty_json.json"),
                            MonkeyPathRepresentation.REPRESENTATION);
                    Assertions.assertFalse(result.isSuccessful());
                    Assertions.assertFalse(result.messages().isEmpty());
                    Assertions.assertEquals(
                            sortedListFromArray(
                                    "value by path /ui_leaders/coords/[3] not found in document 2",
                                    "value by path /ui_leaders/monkeyId not found in document 2",
                                    "value by path /ui_leaders/value not found in document 2",
                                    "value by path /ui_leaders/type not found in document 2",
                                    "value by path /ui_leaders/ComponentType not found in document 2",
                                    "value by path /ui_leaders/coords/[0] not found in document 2",
                                    "value by path /ui_leaders/className not found in document 2",
                                    "value by path /ui_leaders/coords/[2] not found in document 2",
                                    "value by path /ui_leaders/coords/[1] not found in document 2",
                                    "value by path /ui_leaders/visible not found in document 2"),
                            sortedList(result.messages())
                    );
                }),

                SimpleTest.simpleTest("compare different json 20", () -> {
                    NodeCheckResult result = JSonDirectComparator.compareJSonsObjects(
                            jsonFormPath(JSonDirectComparatorCheck.class, "res/resource_empty_json.json"),
                            jsonFormPath(JSonDirectComparatorCheck.class, "res/resource_json_simple.json"),
                            MonkeyPathRepresentation.REPRESENTATION);

                    Assertions.assertFalse(result.isSuccessful());
                    Assertions.assertFalse(result.messages().isEmpty());
                    Assertions.assertEquals(
                            sortedListFromArray(
                                    "value by path /ui_leaders/className not found in document 1",
                                    "value by path /ui_leaders/monkeyId not found in document 1",
                                    "value by path /ui_leaders/value not found in document 1",
                                    "value by path /ui_leaders/type not found in document 1",
                                    "value by path /ui_leaders/coords/[3] not found in document 1",
                                    "value by path /ui_leaders/coords/[2] not found in document 1",
                                    "value by path /ui_leaders/ComponentType not found in document 1",
                                    "value by path /ui_leaders/coords/[1] not found in document 1",
                                    "value by path /ui_leaders/coords/[0] not found in document 1",
                                    "value by path /ui_leaders/visible not found in document 1"),
                            sortedList(result.messages())
                    );
                }),


                SimpleTest.delimer()).lock().dynamicTests(SimpleTest::dynamicTest, SimpleTest::used);

    }


    @TestFactory
    @Tag("DebugRun")
    Stream<DynamicTest> compareJSonsDebug2() {
        return DynamicTestParams.of(
                SimpleTest.delimer("expected errors tests"),

                SimpleTest.simpleTest("compare big slightly different jsons", () -> {
                    NodeCheckResult result = JSonDirectComparator.compareJSonsObjects(
                            jsonFormPath(JSonDirectComparatorCheck.class, "res/resource_big_json.json"),
                            jsonFormPath(JSonDirectComparatorCheck.class, "res/resource_big_json2.json"),
                            MonkeyPathRepresentation.REPRESENTATION);

                    Assertions.assertFalse(result.isSuccessful());
                    Assertions.assertFalse(result.messages().isEmpty());
                    Assertions.assertEquals(
                            sortedListFromArray(
                                    "values by path /beltway_map_dlg/background/bottom_panel_3_section/coords/[0] are not equal. 251.0 != 99.0",
                                    "values by path /beltway_map_dlg/background/bottom_panel_3_section/coords/[1] are not equal. 99.0 != 251.0",
                                    "values by path /beltway_map_dlg/background/bottom_panel_3_section/type are not equal. widget != widget2"),
                            sortedList(result.messages())
                    );
                }),

                SimpleTest.simpleTest("compare different json 10", () -> {
                    NodeCheckResult result = JSonDirectComparator.compareJSonsObjects(
                            jsonFormPath(JSonDirectComparatorCheck.class, "res/resource_json_simple.json"),
                            jsonFormPath(JSonDirectComparatorCheck.class, "res/resource_empty_json.json"),
                            MonkeyPathRepresentation.REPRESENTATION);
                    Assertions.assertFalse(result.isSuccessful());
                    Assertions.assertFalse(result.messages().isEmpty());
                    Assertions.assertEquals(
                            sortedListFromArray(
                                    "value by path /ui_leaders/coords/[3] not found in document 2",
                                    "value by path /ui_leaders/monkeyId not found in document 2",
                                    "value by path /ui_leaders/value not found in document 2",
                                    "value by path /ui_leaders/type not found in document 2",
                                    "value by path /ui_leaders/ComponentType not found in document 2",
                                    "value by path /ui_leaders/coords/[0] not found in document 2",
                                    "value by path /ui_leaders/className not found in document 2",
                                    "value by path /ui_leaders/coords/[2] not found in document 2",
                                    "value by path /ui_leaders/coords/[1] not found in document 2",
                                    "value by path /ui_leaders/visible not found in document 2"),
                            sortedList(result.messages())
                    );
                }),

                SimpleTest.simpleTest("compare different json 20", () -> {
                    NodeCheckResult result = JSonDirectComparator.compareJSonsObjects(
                            jsonFormPath(JSonDirectComparatorCheck.class, "res/resource_empty_json.json"),
                            jsonFormPath(JSonDirectComparatorCheck.class, "res/resource_json_simple.json"),
                            MonkeyPathRepresentation.REPRESENTATION);

                    Assertions.assertFalse(result.isSuccessful());
                    Assertions.assertFalse(result.messages().isEmpty());
                    Assertions.assertEquals(
                            sortedListFromArray(
                                    "value by path /ui_leaders/className not found in document 1",
                                    "value by path /ui_leaders/monkeyId not found in document 1",
                                    "value by path /ui_leaders/value not found in document 1",
                                    "value by path /ui_leaders/type not found in document 1",
                                    "value by path /ui_leaders/coords/[3] not found in document 1",
                                    "value by path /ui_leaders/coords/[2] not found in document 1",
                                    "value by path /ui_leaders/ComponentType not found in document 1",
                                    "value by path /ui_leaders/coords/[1] not found in document 1",
                                    "value by path /ui_leaders/coords/[0] not found in document 1",
                                    "value by path /ui_leaders/visible not found in document 1"),
                            sortedList(result.messages())
                    );
                }),
                SimpleTest.delimer()).lock().dynamicTests(SimpleTest::dynamicTest, SimpleTest::used);
    }

    @TestFactory
    @Tag("DebugRun")
    Stream<DynamicTest> compareJSonsDebug() {
        return DynamicTestParams.of(
                SimpleTest.delimer("expected errors tests"),
                SimpleTest.delimer()).lock().dynamicTests(SimpleTest::dynamicTest, SimpleTest::used);
    }


    private static DynamicTest compareJSonsDynamicTest(CompareJSons param) {
        String testName = param.testName() != null ? param.testName() : "" + param;
        return DynamicTest.dynamicTest(testName, () -> {

            NodeCheckResult context;
            try {
                context = JSonDirectComparator.compareJSonsObjects(param.jsonObject(), param.jsonObject2(),
                        param.pathRepresentation(), param.exclusions());
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
                Assertions.assertEquals(null, param.expectedError());
            }

            if (param.resultHandler() != null) {
                param.resultHandler().accept(context);
            }
        });
    }

    private static DynamicTest compareJSonsDynamicTestWithParams(CompareJSons param) {
        String testName = param.testName() != null ? param.testName() : "" + param;
        return DynamicTest.dynamicTest(testName, () -> {

            NodeCheckResult context;
            try {
                context = JSonDirectComparator.compareJSonsObjects(
                        param.jsonObject(),
                        param.jsonObject2(),
                        param.pathRepresentation(),
                        param.params().build(),
                        param.inclusions(),
                        param.exclusions());
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
                Assertions.assertEquals(null, param.expectedError());
            }

            if (param.resultHandler() != null) {
                param.resultHandler().accept(context);
            }
        });
    }

    @TestFactory
    Stream<DynamicTest> compareJSons20() {
        return DynamicTestParams.of(
                CompareJSons.delimer("expected errors tests"),

                CompareJSons.errorTest("expected error 10", null, null, NullPointerException.class),
                CompareJSons.errorTest("expected error 20", CompareJSons.jsonFormString("{}"), null,
                        NullPointerException.class),
                CompareJSons.errorTest("expected error 30", null, CompareJSons.jsonFormString("{}"),
                        NullPointerException.class),
                CompareJSons.errorTest("expected error 40", CompareJSons.jsonFormString("{}"),
                        CompareJSons.jsonFormString("{ \"A\":1 }"), null).exclusions(p -> p.add("rrr")),

                CompareJSons.delimer(" ------------------------------- simple tests -----------------------------------"),
                CompareJSons.simpleTest("simple test 10",
                        CompareJSons.jsonFormString("{}"),
                        CompareJSons.jsonFormString("{}"),
                        result -> {
                            Assertions.assertTrue(result.isSuccessful());
                            Assertions.assertTrue(result.messages().isEmpty());
                        }).exclusions(p -> p.add("rrr")),

                CompareJSons.simpleTest("simple test 20",
                        CompareJSons.jsonFormString("{  \"A\": 1    }"),
                        CompareJSons.jsonFormString("{  \"A\": 1    }"),
                        result -> {
                            Assertions.assertTrue(result.isSuccessful());
                            Assertions.assertTrue(result.messages().isEmpty());
                        }).exclusions(p -> p.add("rrr")),


                CompareJSons.simpleTest("simple test 30",
                        CompareJSons.jsonFormString("{  \"A\": 2    }"),
                        CompareJSons.jsonFormString("{  \"A\": 1    }"),
                        result -> {
                            Assertions.assertFalse(result.isSuccessful());
                            Assertions.assertFalse(result.messages().isEmpty());
                        }).exclusions(p -> p.add("rrr")),


                CompareJSons.simpleTest("simple test 40",
                        CompareJSons.jsonFormString("{ \"B\" : 2, \"A\": 1 }"),
                        CompareJSons.jsonFormString("{ \"A\" : 1, \"B\": 2 }"),
                        result -> {
                            Assertions.assertTrue(result.isSuccessful());
                            Assertions.assertTrue(result.messages().isEmpty());
                        }).exclusions(p -> p.add("rrr")),


                CompareJSons.simpleTest("simple test 50",
                        CompareJSons.jsonFormString("{  \"A\": 2    }"),
                        CompareJSons.jsonFormString("{  \"A\": 1    }"),
                        result -> {
                            Assertions.assertFalse(result.isSuccessful());
                            Assertions.assertFalse(result.messages().isEmpty());
                        }).exclusions(p -> p.add("rrr")),

                CompareJSons.simpleTest("simple test 60",
                        jsonFormPath(JSonDirectComparatorCheck.class, "res/resource_json_simple.json"),
                        jsonFormPath(JSonDirectComparatorCheck.class, "res/resource_json_simple.json"),
                        (result) -> {
                            Assertions.assertTrue(result.isSuccessful());
                            Assertions.assertTrue(result.messages().isEmpty(), () -> "" + result.messages());
                        }).exclusions(p -> p.add("rrr")),

                CompareJSons.simpleTest("simple test 70",
                        CompareJSons.jsonFormString("{\"C\": [284.0, 113.0, 711.0, 573.0] }"),
                        CompareJSons.jsonFormString("{\"C\": 573.0 }"),
                        result -> {
                            Assertions.assertFalse(result.isSuccessful());
                            Assertions.assertFalse(result.messages().isEmpty());
                        }).exclusions(p -> p.add("rrr")),

                CompareJSons.simpleTest("simple test 80",
                        CompareJSons.jsonFormString("{\"C\": [284.0, 113.0, 711.0, 573.0] }"),
                        CompareJSons.jsonFormString("{\"C\": [284.0, 113.0, 711.0, 573.0] }"),
                        result -> {
                            Assertions.assertTrue(result.isSuccessful());
                            Assertions.assertTrue(result.messages().isEmpty());
                        }).exclusions(p -> p.add("rrr")),

                CompareJSons.simpleTest("simple test 90",
                        CompareJSons.jsonFormString("{\"C\": [284.0, 113.0, 711.0, 573.0] }"),
                        CompareJSons.jsonFormString("{\"C\": [284.1, 113.0, 711.0, 573.0] }"),
                        result -> {
                            Assertions.assertFalse(result.isSuccessful());
                            Assertions.assertEquals(
                                    Arrays.asList("values by path C/[0] are not equal. 284.0 != 284.1"),
                                    result.messages()
                            );
                        }).exclusions(p -> p.add("rrr")),


                CompareJSons.simpleTest("simple test 120",
                        CompareJSons.jsonFormString("{\"C\": 11    }"),
                        CompareJSons.jsonFormString("{\"C\": \"11\" }"),
                        result -> {
                            Assertions.assertFalse(result.isSuccessful());
                            Assertions.assertEquals(
                                    Arrays.asList("values by path C have incompatible types. java.lang.Long and java.lang.String"),
                                    result.messages()
                            );
                        }).exclusions(p -> p.add("rrr")),
                CompareJSons.delimer("---------------------------- exclusions tests -------------------------------- "),
                CompareJSons.delimer(),

                CompareJSons.simpleTest("exclusions tests 10",
                        jsonFormPath(JSonDirectComparatorCheck.class, "res/resource_big_json.json"),
                        jsonFormPath(JSonDirectComparatorCheck.class, "res/resource_big_json2.json"),

                        result -> {
                            Assertions.assertTrue(result.isSuccessful());
                            Assertions.assertTrue(result.messages().isEmpty());
                        }).exclusions(p -> p.add("children[1].children[126].coords[*]", "children[1].children[126].type"))
                ,
                CompareJSons.simpleTest("exclusions tests 20",
                        jsonFormPath(JSonDirectComparatorCheck.class, "res/resource_big_json.json"),
                        jsonFormPath(JSonDirectComparatorCheck.class, "res/resource_big_json2.json"),

                        result -> {
                            Assertions.assertTrue(result.isSuccessful());
                            Assertions.assertTrue(result.messages().isEmpty());
                        }).exclusions(p -> p.add("children[*].children[*].coords[*]", "children[*].children[*].type"))
                ,

                CompareJSons.simpleTest("exclusions tests 30",
                        CompareJSons.jsonFormString("{\"C\": 11    }"),
                        CompareJSons.jsonFormString("{\"C\": \"11\" }"),
                        result -> Assertions.assertTrue(result.isSuccessful())).
                        exclusions(p -> p.add("C"))
                ,

                CompareJSons.simpleTest("exclusions tests  40",
                        CompareJSons.jsonFormString("{\"C\": 11    }"),
                        CompareJSons.jsonFormString("{\"C\": \"11\" }"),
                        result -> Assertions.assertTrue(result.isSuccessful())).
                        exclusions(p -> p.add("C"))
                ,

                CompareJSons.simpleTest("exclusions tests 50",
                        CompareJSons.jsonFormString("{\"C\": 11    }"),
                        CompareJSons.jsonFormString("{\"C\": \"11\" }"),
                        result -> Assertions.assertTrue(result.isSuccessful())).
                        exclusions(p -> p.add("*"))
                ,


                /* Всегда и везде исключать поле с путем с */
                CompareJSons.simpleTest("exclusions tests 60",
                        CompareJSons.jsonFormString("{\"a\": 11,  \"c\": 11    }"),
                        CompareJSons.jsonFormString("{\"a\": 11,  \"c\": \"11\" }"),
                        result -> Assertions.assertTrue(result.isSuccessful())).
                        exclusions(p -> p.add("**.c"))
                ,

                /* Всегда и везде исключать поле с путем excluded */
                CompareJSons.simpleTest("exclusions tests 70",
                        jsonFormPath(JSonDirectComparatorCheck.class, "res/resource_json_simple3.json"),
                        jsonFormPath(JSonDirectComparatorCheck.class, "res/resource_json_simple4.json"),
                        result -> Assertions.assertTrue(result.isSuccessful())).
                        exclusions(p -> p.add("**.excluded")),

                 /* Всегда и везде исключать поле с путем excluded */
                CompareJSons.simpleTest("exclusions tests 80",
                        jsonFormPath(JSonDirectComparatorCheck.class, "res/resource_json_simple3.json"),
                        jsonFormPath(JSonDirectComparatorCheck.class, "res/resource_json_simple4.json"),
                        result -> Assertions.assertFalse(result.isSuccessful())).exclusions(p -> p.add("rrr"))
                ,

                /* проверка сравнения элементов с разными типами */
                CompareJSons.simpleTest("exclusions tests 90",
                        jsonFormPath(JSonDirectComparatorCheck.class, "res/resource_different_types1.json"),
                        jsonFormPath(JSonDirectComparatorCheck.class, "res/resource_different_types2.json"),
                        result -> {
                            Assertions.assertFalse(result.isSuccessful());
                            Assertions.assertEquals(
                                    Arrays.asList("values by path b have incompatible types. java.lang.String and java.lang.Boolean"),
                                    result.messages()
                            );
                        }

                ).exclusions(CompareJSons.Pattern::drop).inclusions(CompareJSons.Pattern::drop)
                ,

                /* проверка сравнения элементов с разными типами */
                CompareJSons.simpleTest("exclusions tests 100",
                        jsonFormPath(JSonDirectComparatorCheck.class, "res/resource_different_types1.json"),
                        jsonFormPath(JSonDirectComparatorCheck.class, "res/resource_different_types2.json"),
                        result -> {
                            Assertions.assertTrue(result.isSuccessful());
                            Assertions.assertEquals(Collections.emptyList(), result.messages());
                        }

                ).exclusions(p -> p.add("b"))
                ,

                CompareJSons.errorTest("exclusions tests 810",
                        jsonFormPath(JSonDirectComparatorCheck.class, "res/resource_json_simple3.json"),
                        jsonFormPath(JSonDirectComparatorCheck.class, "res/resource_json_simple4.json"),
                        null).exclusions(CompareJSons.Pattern::drop),


                CompareJSons.delimer(),
                CompareJSons.delimer()).lock().dynamicTests(JSonDirectComparatorCheck::compareJSonsDynamicTest, CompareJSons::used);
    }

    @TestFactory
    Stream<DynamicTest> flatCompareWithParams() {
        return DynamicTestParams.of(
                CompareJSons.delimer(" debugs "),

                CompareJSons.simpleTest("exclusions tests 120",
                        jsonFormPath(JSonDirectComparatorCheck.class, "res/resource_addressed_nodes.json"),
                        jsonFormPath(JSonDirectComparatorCheck.class, "res/resource_addressed_nodes2.json"),
                        result -> {
                            Assertions.assertTrue(result.isSuccessful(), () -> "" + result.messages());
                            Assertions.assertEquals(Collections.emptyList(), result.messages());
                        }

                ).useParam(b -> b.single("param1", "monkeyId1").single("param2", "monkeyId2")).
                        inclusions(pattern -> pattern.add("/monkeyId1/monkeyId2/*")),

                CompareJSons.simpleTest("exclusions tests 130",
                        jsonFormPath(JSonDirectComparatorCheck.class, "res/resource_addressed_nodes.json"),
                        jsonFormPath(JSonDirectComparatorCheck.class, "res/resource_addressed_nodes3.json"),
                        result -> {
                            Assertions.assertFalse(result.isSuccessful());
                            Assertions.assertEquals(sortedList(Arrays.asList(
                                    "values by path /monkeyId1/monkeyId2/value2 are not equal. value2 != value+2",
                                    "values by path /monkeyId1/monkeyId2/value3 are not equal. value3 != value+3",
                                    "values by path /monkeyId1/monkeyId2/value are not equal. value1 != value+1"
                            )), sortedList(result.messages()));
                        }

                ).useParam(b -> b.single("param1", "monkeyId1").single("param2", "monkeyId2")).
                        inclusions(pattern -> pattern.add("/monkeyId1/monkeyId2/*"))

                ,
                CompareJSons.delimer()).checkTestNamesUniquiness(false).lock().
                dynamicTests(JSonDirectComparatorCheck::compareJSonsDynamicTestWithParams, CompareJSons::used);
    }


    @TestFactory
    @Tag("DebugRun")
    Stream<DynamicTest> flatCompareDebugged() {
        return DynamicTestParams.of(
                CompareJSons.delimer(" debugs "),

                /* проверка сравнения элементов с разными типами */
                CompareJSons.simpleTest("exclusions tests 120",
                        jsonFormPath(JSonDirectComparatorCheck.class, "res/resource_addressed_nodes.json"),
                        jsonFormPath(JSonDirectComparatorCheck.class, "res/resource_addressed_nodes2.json"),
                        result -> {
                            Assertions.assertTrue(result.isSuccessful(), () -> "" + result.messages());
                            Assertions.assertEquals(Collections.emptyList(), result.messages());
                        }

                ).useParam(b -> b.single("param1", "monkeyId1").single("param2", "monkeyId2")).
                        inclusions(pattern -> pattern.add("/monkeyId1/monkeyId2/*"))

                , CompareJSons.delimer()).lock().dynamicTests(JSonDirectComparatorCheck::compareJSonsDynamicTestWithParams, CompareJSons::used);

    }

    @TestFactory
    @Tag("DebugRun")
    Stream<DynamicTest> compareJSonsDebugged() {
        return DynamicTestParams.of(
                SimpleTest.delimer(" DEBUGS  "),
                SimpleTest.delimer()).lock().dynamicTests(SimpleTest::dynamicTest, SimpleTest::used);
    }

    @TestFactory
    @Tag("DebugRun")
    Stream<DynamicTest> objectNameDebugged() {
        return DynamicTestParams.of(
                SimpleTest.unused()).lock().dynamicTests(SimpleTest::dynamicTest, SimpleTest::used);
    }

    @SafeVarargs
    private static <T extends Comparable<T>> List<T> sortedListFromArray(T... t) {
        List<T> result = new ArrayList<>(Arrays.asList(t));
        result.sort(Comparable::compareTo);
        return result;
    }

    private static <T extends Comparable<T>> List<T> sortedList(List<T> list) {
        List<T> result = new ArrayList<>(list);
        result.sort(Comparable::compareTo);
        return result;
    }

}

