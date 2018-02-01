package com.steammachine.jsonchecker.utils.jsondirectcomparison;

import com.steammachine.jsonchecker.defaults.AddressType;
import com.steammachine.jsonchecker.defaults.MonkeyPathRepresentation;
import com.steammachine.jsonchecker.impl.directcomparison.flatterprocs.Flatters;
import com.steammachine.jsonchecker.impl.flatter2.ValRec;
import com.steammachine.jsonchecker.impl.flatter2.ValueKey;
import com.steammachine.common.utils.ResourceUtils;
import com.steammachine.org.junit5.extensions.dynamictests.dynamictestparam.DynamicTestParams;
import com.steammachine.org.junit5.extensions.expectedexceptions.Expected;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.steammachine.common.utils.ResourceUtils.loadResourceAsString;
import static com.steammachine.common.utils.commonutils.CommonUtils.*;
import static java.util.stream.Collectors.toList;


/**
 * Created by Vladimir Bogoduhov on 01.12.2017.
 *
 * @author Vladimir Bogoduhov
 */
class FlattersCheck {


    private static final Charset CHARSET = Charset.forName("UTF-8");

    @Test
    @Expected(expected = NullPointerException.class)
    void testNullFlattenType() {
        Flatters.flatten(null,
                jsonFromPath(FlattersCheck.class, "res/resource_to_compare1.json"));

    }

    @Test
    @Expected(expected = NullPointerException.class)
    void testNullFlattenTypeAndNullObject() {
        Flatters.flatten(null, null);
    }

    @Test
    @Expected(expected = NullPointerException.class)
    void testNullObject() {
        Flatters.flatten(AddressType.DIRECT.name(), null);
    }

    @Test
    @Expected(expected = IllegalStateException.class)
    void testUnknownFlattenType() {
        Flatters.flatten("unknown",
                jsonFromPath(FlattersCheck.class, "res/resource_to_compare1.json"));
    }

    @Test
    void testCorrectIds() {
        JSONObject o = jsonFromPath(FlattersCheck.class, "res/resource_to_compare1.json");
        ValueKey vk1 = Flatters.flatten(AddressType.DIRECT.name(), o).get(100).valueKey();
        ValueKey vk2 = Flatters.flatten(AddressType.DIRECT.name(), o).get(100).valueKey();
        Assertions.assertNotSame(vk1, vk2);
        Assertions.assertEquals(vk1, vk2);
    }

    @Test
    void testCorrectAllIds() {
        JSONObject o = jsonFromPath(FlattersCheck.class, "res/resource_to_compare1.json");

        List<ValueKey> l1 =
                Flatters.flatten(AddressType.DIRECT.name(), o).stream().map(ValRec::valueKey).collect(toList());
        List<ValueKey> l2 =
                Flatters.flatten(AddressType.DIRECT.name(), o).stream().map(ValRec::valueKey).collect(toList());

        Assertions.assertEquals(l1, l2);
    }

    @Test
    void testDirect() {
        List<ValRec> values = Flatters.flatten(AddressType.DIRECT.name(),
                jsonFromPath(FlattersCheck.class, "res/resource_to_compare1.json"));

        values.get(100);
    }


    @Test
    void testMonkeyIds() {
        List<ValRec> values = Flatters.flatten(AddressType.MONKEY.name(),
                jsonFromPath(FlattersCheck.class, "res/resource_to_compare1.json"));


        values.get(100);
    }


    @Test
    void testMonkeyIdsAllIds() {
        JSONObject object = jsonFromPath(FlattersCheck.class, "res/resource_to_compare1.json");
        Assertions.assertEquals(
                Flatters.flatten(AddressType.MONKEY.name(), object).stream().map(ValRec::valueKey).collect(toList()),
                Flatters.flatten(AddressType.MONKEY.name(), object).stream().map(ValRec::valueKey).collect(toList())
        );
    }


    @Test
    void testComponentTypesLightData() {
        Flatters.flatten(AddressType.MONKEY.name(),
                jsonFromPath(FlattersCheck.class, "res/resource_to_compare_light_data.json"));
    }

    @Test
    void testComponentTypesLightData2() {
        Flatters.flatten(AddressType.MONKEY.name(),
                jsonFromPath(FlattersCheck.class, "res/resource_to_compare_light_data2.json"));
    }

    @Test
    void testComponentTypes() {
        List<ValRec> values = Flatters.flatten(AddressType.MONKEY.name(),
                jsonFromPath(FlattersCheck.class, "res/resource_to_compare1.json"));

        values.get(100);
    }


    @Test
    void testComponentTypesAllIds() {
        JSONObject object = jsonFromPath(FlattersCheck.class, "res/resource_to_compare1.json");
        Assertions.assertEquals(
                Flatters.flatten(AddressType.MONKEY.name(), object).stream().map(ValRec::valueKey).collect(toList()),
                Flatters.flatten(AddressType.MONKEY.name(), object).stream().map(ValRec::valueKey).collect(toList())
        );
    }

    @TestFactory
    Stream<DynamicTest> flattenJSonObject() {
        return DynamicTestParams.of(
                SimpleTest.delimer("expected errors tests"),

                SimpleTest.simpleTest("flattenTree 1", () -> Assertions.assertEquals(
                        Arrays.asList(
                                "ComponentType",
                                "className",
                                "coords/[0]",
                                "coords/[1]",
                                "coords/[2]",
                                "coords/[3]",
                                "monkeyId",
                                "type",
                                "value",
                                "visible"
                        )
                        ,

                        Flatters.flatten(AddressType.DIRECT.name(),
                                jsonFromPath(FlattersCheck.class, "res/resource_json_simple.json")).
                                stream().map(ValRec::path).map(MonkeyPathRepresentation.REPRESENTATION::path).sorted().
                                collect(Collectors.toList())
                )),

                SimpleTest.simpleTest("flattenTree 10", () -> Assertions.assertEquals(
                        Arrays.asList(
                                "a",
                                "array_a/[0]/array_b/[0]/array_c/[0]/d",
                                "array_a/[0]/array_b/[0]/c",
                                "array_a/[0]/b"
                        )
                        ,
                        Flatters.flatten(AddressType.DIRECT.name(),
                                jsonFromPath(FlattersCheck.class, "res/resource_json_simple2.json")).
                                stream().map(ValRec::path).map(MonkeyPathRepresentation.REPRESENTATION::path).sorted().
                                collect(Collectors.toList())
                )),

                SimpleTest.simpleTest("flattenTree 20", () -> {
                    Assertions.assertEquals(
                            loadList("res/resource.list.txt"),
                            /*saveList("res/resource.list.txt",  */Flatters.flatten(AddressType.DIRECT.name(), jsonFromPath(FlattersCheck.class, "res/resource_big_json.json")).
                                    stream().map(ValRec::path).map(MonkeyPathRepresentation.REPRESENTATION::path).sorted().
                                    collect(Collectors.toList()))/*)*/;
                }),

                SimpleTest.delimer()).lock().dynamicTests(SimpleTest::dynamicTest, SimpleTest::used);
    }


    private static JSONObject jsonFromPath(Class<?> startClass, String resourcePath) {
        return suppress(() ->
                (JSONObject) new JSONParser().parse(suppress(() -> loadResourceAsString(startClass, resourcePath, CHARSET))));
    }

    private static JSONObject jsonFromString(String data) {
        return suppress(() -> (JSONObject) new JSONParser().parse(data));
    }

    private static List<String> loadList(String path) throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        ResourceUtils.loadResourceByRelativePath(
                                FlattersCheck.class, path)))) {
            return reader.lines().sequential().filter(s -> !s.trim().isEmpty()).collect(Collectors.toList());
        }
    }

    @SuppressWarnings("unused")
    private static List<String> saveList(String path, List<String> strings) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(
                                getAbsoluteResourcePath(FlattersCheck.class, path))))) {
            strings.stream().sequential().forEachOrdered(i -> suppressWOResult(() -> {
                writer.newLine();
                writer.write(i);
            }));
        }
        return strings;
    }


}