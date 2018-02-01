package com.steammachine.jsonchecker.executiontests.simple;

import com.steammachine.common.lazyeval.LazyEval;
import com.steammachine.common.utils.metodsutils.MethodCaller;
import com.steammachine.common.utils.metodsutils.MethodUtils.BaseMethodCaller;
import com.steammachine.jsonchecker.defaults.DefaultNodeCheckContext;
import com.steammachine.jsonchecker.defaults.DefaultWidgetNode;
import com.steammachine.jsonchecker.defaults.DefaultWidgetNodeChecker;
import com.steammachine.jsonchecker.executiontests.simple.res.types.TestType;
import com.steammachine.jsonchecker.types.LoadResult;
import com.steammachine.jsonchecker.types.NodeChecker;
import com.steammachine.jsonchecker.types.TreeNode;
import com.steammachine.jsonchecker.types.exceptions.MalformedDocument;
import com.steammachine.jsonchecker.utils.JSonMapper;
import com.steammachine.jsonchecker.utils.TreeFlatter;
import com.steammachine.org.junit5.extensions.expectedexceptions.Expected;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.io.IOException;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.steammachine.common.utils.commonutils.CommonUtils.*;
import static com.steammachine.common.utils.metodsutils.MethodUtils.Level.PRIVATE;
import static com.steammachine.common.utils.metodsutils.MethodUtils.findMethod;
import static com.steammachine.jsonchecker.utils.TreeFlatter.flatten;


/**
 * Created by Vladimir Bogodukhov on 01.12.2017.
 *
 * @author Vladimir Bogodukhov
 */
@Deprecated
class JSonMapperCheck {

    public static class DefaultWidgetNodeDirectSubClass extends DefaultWidgetNode {

    }

    @Test
    void load10() throws IOException {
        String path = getAbsoluteResourcePath(getClass(), "res/abstracts/search/resource_search_deep_tree.json");
        TreeNode root = JSonMapper.load(path, DefaultWidgetNodeDirectSubClass.class, Collections.emptyMap()).root();

        Assertions.assertEquals(
                eval(() -> {
                    DefaultWidgetNodeDirectSubClass node = new DefaultWidgetNodeDirectSubClass();
                    node.setMonkeyId("one");
                    node.setComponentType("sqc_scriptable_dialog");
                    node.setVisible(true);
                    node.setType(null);
                    node.setClassName(null);
                    node.setValue(null);
                    node.setValue("debugId", "one");
                    return node;
                }),
                root.object());

    }

    @Test
    void load20() throws IOException {
        String path = getAbsoluteResourcePath(getClass(), "res/abstracts/search/resource_search_deep_tree.json");
        TreeNode root = JSonMapper.load(path, DefaultWidgetNodeDirectSubClass.class, Collections.emptyMap()).root();

        Assertions.assertEquals(Arrays.asList(
                eval(() -> {
                    DefaultWidgetNodeDirectSubClass node = new DefaultWidgetNodeDirectSubClass();
                    node.setMonkeyId("one");
                    node.setComponentType("sqc_scriptable_dialog");
                    node.setVisible(true);
                    node.setType(null);
                    node.setClassName(null);
                    node.setValue(null);
                    node.setValue("debugId", "one");
                    return node;
                }),
                eval(() -> {
                    DefaultWidgetNodeDirectSubClass node = new DefaultWidgetNodeDirectSubClass();
                    node.setMonkeyId("two");
                    node.setComponentType("milk_bevel");
                    node.setClassName("milk_bevel");
                    node.setType("widget");
                    node.setVisible(true);
                    node.setValue("");
                    node.setValue("debugId", "two");
                    node.setValue("coords", "[250.0,98.0,780.0,470.0]");

                    return node;
                }),
                eval(() -> {
                    DefaultWidgetNodeDirectSubClass node = new DefaultWidgetNodeDirectSubClass();
                    node.setMonkeyId("three");
                    node.setComponentType("milk_bevel");
                    node.setVisible(true);
                    node.setClassName("milk_bevel");
                    node.setType("widget");
                    node.setValue("");
                    node.setValue("debugId", "three");
                    node.setValue("coords", "[250.0,98.0,780.0,470.0]");

                    return node;
                })
                , eval((Supplier<Object>) () -> {
                    DefaultWidgetNodeDirectSubClass node = new DefaultWidgetNodeDirectSubClass();
                    node.setMonkeyId("four");
                    node.setComponentType("milk_bevel");
                    node.setVisible(true);
                    node.setClassName("milk_bevel");
                    node.setType("widget");
                    node.setValue("");
                    node.setValue("debugId", "four");
                    node.setValue("coords", "[250.0,98.0,780.0,470.0]");
                    return node;
                }),
                eval(() -> {
                    DefaultWidgetNodeDirectSubClass node = new DefaultWidgetNodeDirectSubClass();
                    node.setMonkeyId("five");
                    node.setComponentType("milk_bevel");
                    node.setVisible(true);
                    node.setType(null);
                    node.setClassName(null);
                    node.setValue(null);
                    node.setValue("debugId", "five");

                    return node;
                }), eval(() -> {
                    DefaultWidgetNodeDirectSubClass node = new DefaultWidgetNodeDirectSubClass();
                    node.setMonkeyId("six");
                    node.setComponentType("milk_bevel");
                    node.setVisible(true);
                    node.setType(null);
                    node.setClassName(null);
                    node.setValue(null);
                    node.setValue("debugId", "six");

                    return node;
                }),
                eval(() -> {
                    DefaultWidgetNodeDirectSubClass node = new DefaultWidgetNodeDirectSubClass();
                    node.setMonkeyId("seven");
                    node.setComponentType("milk_bevel");
                    node.setVisible(true);
                    node.setType(null);
                    node.setClassName(null);
                    node.setValue(null);
                    node.setValue("debugId", "seven");

                    return node;
                })
                ),
                TreeFlatter.flatten(root).stream().map(TreeNode::object).
                        collect(Collectors.toList())
        );
    }


    @Test
    void speedTest() throws Exception {
        String path = getAbsoluteResourcePath(getClass(), "res/resource_beltway_map_dlg.json");
        long measureTime = measureTime(() -> JSonMapper.load(path, DefaultWidgetNode.class, Collections.emptyMap()), 100);
        Assertions.assertTrue(measureTime < 30000);
    }

    @Test
    void testCheckProcess() throws IOException {
        String dataPath = getAbsoluteResourcePath(getClass(), "res/resource_city_metro_style/resource_city_metro_style_dlg.json");
        TreeNode treeNode = JSonMapper.load(dataPath, DefaultWidgetNode.class, Collections.emptyMap()).root();
        String dataCheckersPath = getAbsoluteResourcePath(getClass(), "res/resource_city_metro_style/resource_city_metro_style_dlg_check_struct.json");
        TreeNode checkers = JSonMapper.load(dataCheckersPath, DefaultWidgetNodeChecker.class, Collections.emptyMap()).root();

        List<TreeNode> treeNodes = flatten(treeNode);
        List<TreeNode> checks = flatten(checkers);

        DefaultNodeCheckContext context = new DefaultNodeCheckContext();
        int size = treeNodes.size() < checks.size() ? treeNodes.size() : checks.size();
        Assertions.assertTrue(context.isSuccessful());
        for (int i = 0; i < size; i++) {
            Object data = treeNodes.get(i).object();
            NodeChecker nodeChecker = (NodeChecker) checks.get(i).object();

            if (nodeChecker.checks(data.getClass())) {
                nodeChecker.check(data, "", context);
            }
        }

        Assertions.assertTrue(context.isSuccessful());
    }


    /* -------------------------------------------- params  handling ----------------------------------------------- */

    @Test
    void paramsCheck10() throws IOException {
        Map<String, Object> params = new HashMap<String, Object>() {{
            put("intparam2", 10);
        }};
        String dataPath = getAbsoluteResourcePath(getClass(), "res/abstracts/params/resource_file_with_default_params.json");
        LoadResult loadResult = JSonMapper.load(dataPath, DefaultWidgetNode.class, params);
        TreeNode treeNode = loadResult.root();
        TestType obj5 = flatten(treeNode).stream().map(TreeNode::object).
                /* peek(System.out::println). */
                        map(i -> (TestType) i).
                        filter(tt -> "obj5".equals(tt.getName())).findFirst().orElse(null);

        Assertions.assertEquals(eval(() -> {
            TestType expected = new TestType();
            expected.setField1("croaton");
            expected.setField2(10);
            expected.setName("obj5");
            return expected;
        }), obj5);

        Assertions.assertNotNull(loadResult.extrainfo());
        Assertions.assertNotNull(loadResult.extrainfo().userDefinedData());
        Assertions.assertEquals(Arrays.asList(
                "exclude val/val/*/",
                "exclude Gustavo Cerrati es cantante argentino",
                "include another_one_bytes_the_dust"
        ), loadResult.extrainfo().userDefinedData());
    }

    @Test
    void paramsCheck20() throws IOException {
        int intparam2 = new Random().nextInt(1000);
        String stringparam = "" + System.nanoTime();

        Map<String, Object> params = new HashMap<String, Object>() {{
            put("intparam2", intparam2);
            put("stringparam", stringparam);
        }};

        String dataPath = getAbsoluteResourcePath(getClass(), "res/abstracts/params/resource_file_with_default_params2.json");
        TreeNode treeNode = JSonMapper.load(dataPath, TestType.class, params).root();
        List<TestType> list = flatten(treeNode).stream().
                map(TreeNode::object).
                map(i -> (TestType) i).
                collect(Collectors.toList());


        Assertions.assertEquals(
                Arrays.asList(
                        eval(() -> {
                            TestType result = new TestType();
                            result.setField1(stringparam);
                            result.setField2(1888);
                            return result;
                        }),
                        eval(() -> {
                            TestType result = new TestType();
                            result.setField1("another value");
                            result.setField2(intparam2);
                            return result;
                        })
                ),
                list);
    }


    /* ---------------------------------------------- isParamRef ---------------------------------------------------- */

    @Test
    void isParamRef10() {
        Assertions.assertFalse(isParamRef(null));
    }

    @Test
    void isParamRef20() {
        Assertions.assertFalse(isParamRef(""));
    }

    @Test
    void isParamRef30() {
        Assertions.assertTrue(isParamRef("${parametr}"));
    }

    @Test
    void isParamRef40() {
        Assertions.assertFalse(isParamRef("${parametre"));
    }

    /* ---------------------------------------------- refParamName ---------------------------------------------------- */


//    @Test
//    @Expected(expected = IllegalArgumentException.class)
//    void refParamName10() {
//        refParamName("");
//    }
//
//    @Test
//    @Expected(expected = IllegalArgumentException.class)
//    void refParamName20() {
//        refParamName("paramName");
//    }

    @Test
    void refParamName30() {
        Assertions.assertEquals("paramName", refParamName("${paramName}"));
    }


    /* ---------------------------------------------- load tipical jsons ----------------------------------------- */

    @TestFactory
    Stream<DynamicTest> loadTipicalJsons10() {
        return Stream.of(
                "resource_beltway_map_dlg",
                "resource_city_buy_construction_dlg",
                "resource_city_metro_style_dlg2",
                "resource_military_production_dlg").
                map(r -> toArray(r, getAbsoluteResourcePath(getClass(), "res/" + r + ".json"))).
                map(r -> dynamicTest(r[0], r[1], r[1]));
    }


    /* ------------------------------------------ type definition duplicates --------------------------------------- */

    @Test
    @Expected(expected = MalformedDocument.class)
    void typeDefinitionDuplicates10() throws IOException {
        String dataPath = getAbsoluteResourcePath(getClass(), "res/interfaceaware/resource_definitions_dulicates.json");
        JSonMapper.load(dataPath, DefaultWidgetNode.class, null);
    }

    @Test
    void typeDefinitionDuplicates20() throws IOException {
        String dataPath = getAbsoluteResourcePath(getClass(), "res/interfaceaware/resource_definitions_dulicates20.json");
        JSonMapper.load(dataPath, DefaultWidgetNode.class, null);
    }


    /* ---------------------------------------------- privates ---------------------------------------------------- */

    private static DynamicTest dynamicTest(String testName, String jsonPath, String jsonTemplatePath) {
        return DynamicTest.dynamicTest(testName, () -> {
            JSonMapper.load(jsonPath, DefaultWidgetNode.class, null);
            JSonMapper.load(jsonTemplatePath, DefaultWidgetNodeChecker.class, null);
        });
    }

    private static final LazyEval<MethodCaller> IS_PARAM_REF = LazyEval.eval(() ->
            new BaseMethodCaller(findMethod(PRIVATE, JSonMapper.class, "isParamRef", Boolean.TYPE,
                    String.class)));
    private static final LazyEval<MethodCaller> REF_PARAM_NAME = LazyEval.eval(() ->
            new BaseMethodCaller(findMethod(PRIVATE, JSonMapper.class, "refParamName", String.class,
                    String.class)));

    private static String refParamName(String valueCandidate) {
        return REF_PARAM_NAME.value().invoke(null, valueCandidate);
    }

    private static boolean isParamRef(String valueCandidate) {
        return IS_PARAM_REF.value().invoke(null, valueCandidate);
    }


}