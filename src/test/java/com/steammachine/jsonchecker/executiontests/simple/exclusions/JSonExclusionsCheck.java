package com.steammachine.jsonchecker.executiontests.simple.exclusions;

import com.steammachine.common.lazyeval.LazyEval;
import com.steammachine.common.utils.metodsutils.MethodCaller;
import com.steammachine.common.utils.metodsutils.MethodUtils;
import com.steammachine.jsonchecker.defaults.DefaultWidgetNode;
import com.steammachine.jsonchecker.executiontests.simple.PathFormatChecker;
import com.steammachine.jsonchecker.executiontests.simple.PathTestParam;
import com.steammachine.jsonchecker.impl.internal.Association;
import com.steammachine.jsonchecker.impl.ver1.DefaultTreeNode;
import com.steammachine.jsonchecker.types.LoadResult;
import com.steammachine.jsonchecker.types.SqNodeKeyType;
import com.steammachine.jsonchecker.types.TreeNode;
import com.steammachine.jsonchecker.types.exceptions.PathError;
import com.steammachine.jsonchecker.utils.JSonExclusions;
import com.steammachine.jsonchecker.utils.JSonMapper;
import com.steammachine.jsonchecker.utils.TreeFlatter;
import com.steammachine.org.junit5.extensions.dynamictests.dynamictestparam.DynamicTestParams;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.Executable;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.steammachine.common.utils.commonutils.CommonUtils.getAbsoluteResourcePath;
import static com.steammachine.jsonchecker.executiontests.simple.PathFormatChecker.*;
import static com.steammachine.jsonchecker.executiontests.simple.PathTestParam.param;
import static com.steammachine.jsonchecker.executiontests.simple.PathTestParam.skip;

/**
 * Created 20/01/17 11:59
 *
 * @author Vladimir Bogodukhov
 * @deprecated
 **/
@Deprecated
class JSonExclusionsCheck {
    private static final class JSonExclusionsHack {

        private static final LazyEval<MethodCaller> IS_CORRECT_PATH =
                LazyEval.eval(() -> new MethodUtils.BaseMethodCaller(MethodUtils.findMethod(MethodUtils.Level.PRIVATE,
                        JSonExclusions.class, "isCorrectPath", Boolean.TYPE, String.class)));
        private static final LazyEval<MethodCaller> IS_EXCLUSION_APPLIED =
                LazyEval.eval(() -> new MethodUtils.BaseMethodCaller(MethodUtils.findMethod(MethodUtils.Level.PRIVATE,
                        JSonExclusions.class, "isExclusionApplied", Boolean.TYPE, List.class, String.class)));
        private static final LazyEval<MethodCaller> EXTRACT_DATA =
                LazyEval.eval(() -> new MethodUtils.BaseMethodCaller(MethodUtils.findMethod(MethodUtils.Level.PRIVATE,
                        JSonExclusions.class, "extractData", List.class, String.class)));
        private static final LazyEval<MethodCaller> GET_IDENTITY_TYPE =
                LazyEval.eval(() -> new MethodUtils.BaseMethodCaller(MethodUtils.findMethod(MethodUtils.Level.PRIVATE,
                        JSonExclusions.class, "getIdentityType", SqNodeKeyType.class, String.class)));
        private static final LazyEval<MethodCaller> GET_IDENTITY =
                LazyEval.eval(() -> new MethodUtils.BaseMethodCaller(MethodUtils.findMethod(MethodUtils.Level.PRIVATE,
                        JSonExclusions.class, "getIdentity", String.class, String.class)));

        private static boolean isCorrectPath(String path) {
            return IS_CORRECT_PATH.value().invoke(null, path);
        }

        private static boolean isExclusionApplied(List<TreeNode> fullNode, String exclusion) {
            return IS_EXCLUSION_APPLIED.value().invoke(null, fullNode, exclusion);
        }

        private static List<String> extractData(String path) {
            return EXTRACT_DATA.value().invoke(null, path);
        }

        private static SqNodeKeyType getIdentityType(String pathElement) {
            return GET_IDENTITY_TYPE.value().invoke(null, pathElement);
        }


        private static String getIdentity(String pathElenent) {
            return GET_IDENTITY.value().invoke(null, pathElenent);
        }
    }

    private interface DebugId {
        String id();
    }

    public static class DefaultWidgetNodeWithDebugId extends DefaultWidgetNode implements DebugId {
        private String debugId;

        public DefaultWidgetNodeWithDebugId() {

        }

        public void setDebugId(String debugId) {
            this.debugId = debugId;
        }

        @Override
        public String id() {
            return debugId;
        }
    }



    /* ----------------------------------------------------- parsePath -------------------------------------------------- */



/*

   Схематичная струтура документа - resource_search_deep_tree.json
                                       one
                                        |
                                        |
                        -------------------------------
                        |               |             |
                        |               |             |
                        two            three         four
                                                      |
                                                      |
                                                -----------------------
                                               |          |           |
                                               |          |           |
                                               five      six      seven

 */

////
////    @TestFactory
////    Stream<DynamicTest> checkProcessed() throws IOException {
////        String path = getAbsoluteResourcePath(getClass(), "../res/abstracts/search/resource_search_deep_tree.json");
////        TreeNode root = JSonMapper.load(path, DefaultWidgetNode.class, null).root();
////
////        Association<Boolean> association = JSonExclusions.collectProcessed(root, Arrays.asList()); /* никаких исключений */
////        return TreeFlatter.flatten(root).stream().map(tn -> createTest(association, true, tn));
////    }
//
//
//    private static DynamicTest createTest(Association<Boolean> association, boolean expected, TreeNode treeNode) {
//        return DynamicTest.dynamicTest("" + treeNode,
//                () -> Assertions.assertEquals(expected, Objects.requireNonNull(association.associatedValue(treeNode.object()))));
//    }

    @TestFactory
    Stream<DynamicTest> parsePath() {
        return DynamicTestParams.of(
                param("one node excluded[10]", docPath("../res/abstracts/search/resource_search_deep_tree.json"), "one").
                        addExclusion("/one/four/seven"),
                param("one node excluded[20]", docPath("../res/abstracts/search/resource_search_deep_tree.json"), "two").
                        addExclusion("/one/four/seven"),
                param("one node excluded[30]", docPath("../res/abstracts/search/resource_search_deep_tree.json"), "three").
                        addExclusion("/one/four/seven"),
                param("one node excluded[40]", docPath("../res/abstracts/search/resource_search_deep_tree.json"), "four").
                        addExclusion("/one/four/seven"),
                param("one node excluded[50]", docPath("../res/abstracts/search/resource_search_deep_tree.json"), "five").
                        addExclusion("/one/four/seven"),
                param("one node excluded[60]", docPath("../res/abstracts/search/resource_search_deep_tree.json"), "six").
                        addExclusion("/one/four/seven"),
                param("one node excluded[70]", docPath("../res/abstracts/search/resource_search_deep_tree.json"), "seven", false).
                        addExclusion("/one/four/seven"),

                param("all wrong path[10]", docPath("../res/abstracts/search/resource_search_deep_tree.json"), "one", PathError.class).
                        addExclusion("one"),
                param("all wrong path[20]", docPath("../res/abstracts/search/resource_search_deep_tree.json"), "one", PathError.class).
                        addExclusion("/one/"),
                param("all wrong path[30]", docPath("../res/abstracts/search/resource_search_deep_tree.json"), "one", false).
                        addExclusion("/one"),
                param("all wrong path[40]", docPath("../res/abstracts/search/resource_search_deep_tree.json"), "one", true).
                        addExclusion("/doesnotexist"),

                param("excluded one path[10]", docPath("../res/abstracts/search/resource_search_deep_tree.json"), "one", false).
                        addExclusion("/one"),
                param("excluded one path[20]", docPath("../res/abstracts/search/resource_search_deep_tree.json"), "two", true).
                        addExclusion("/one"),
                param("excluded one path[30]", docPath("../res/abstracts/search/resource_search_deep_tree.json"), "three", true).
                        addExclusion("/one"),
                param("excluded one path[40]", docPath("../res/abstracts/search/resource_search_deep_tree.json"), "four", true).
                        addExclusion("/one"),
                param("excluded one path[50]", docPath("../res/abstracts/search/resource_search_deep_tree.json"), "five", true).
                        addExclusion("/one"),
                param("excluded one path[60]", docPath("../res/abstracts/search/resource_search_deep_tree.json"), "six", true).
                        addExclusion("/one"),
                param("excluded one path[70]", docPath("../res/abstracts/search/resource_search_deep_tree.json"), "seven", true).
                        addExclusion("/one"),

                param("all paths excluded [10]", docPath("../res/abstracts/search/resource_search_deep_tree.json"), "one", false).
                        addExclusion("/**"),
                param("all paths excluded [20]", docPath("../res/abstracts/search/resource_search_deep_tree.json"), "two", false).
                        addExclusion("/**"),
                param("all paths excluded [30]", docPath("../res/abstracts/search/resource_search_deep_tree.json"), "three", false).
                        addExclusion("/**"),
                param("all paths excluded [40]", docPath("../res/abstracts/search/resource_search_deep_tree.json"), "four", false).
                        addExclusion("/**"),
                param("all paths excluded [50]", docPath("../res/abstracts/search/resource_search_deep_tree.json"), "five", false).
                        addExclusion("/**"),
                param("all paths excluded [60]", docPath("../res/abstracts/search/resource_search_deep_tree.json"), "six", false).
                        addExclusion("/**"),
                param("all paths excluded [70]", docPath("../res/abstracts/search/resource_search_deep_tree.json"), "seven", false).
                        addExclusion("/**"),

                param("one level children excluded [10]", docPath("../res/abstracts/search/resource_search_deep_tree.json"), "one", true).
                        addExclusion("/one/*"),
                param("one level children excluded [20]", docPath("../res/abstracts/search/resource_search_deep_tree.json"), "two", false).
                        addExclusion("/one/*"),
                param("one level children excluded [30]", docPath("../res/abstracts/search/resource_search_deep_tree.json"), "three", false).
                        addExclusion("/one/*"),
                param("one level children excluded [40]", docPath("../res/abstracts/search/resource_search_deep_tree.json"), "four", false).
                        addExclusion("/one/*"),
                param("one level children excluded [50]", docPath("../res/abstracts/search/resource_search_deep_tree.json"), "five", false).
                        addExclusion("/one/*"),
                param("one level children excluded [60]", docPath("../res/abstracts/search/resource_search_deep_tree.json"), "six", false).
                        addExclusion("/one/*"),
                param("one level children excluded [70]", docPath("../res/abstracts/search/resource_search_deep_tree.json"), "seven", false).
                        addExclusion("/one/*"),/* для элемента "seven" применяется исключение - не обрабатывается */
                param("all wrong path[1]", docPath("../res/abstracts/search/resource_search_deep_tree.json"), "one", false).
                        addExclusion("/one"),
                param("all processed[1]", docPath("../res/abstracts/search/resource_search_deep_tree.json"), "one"),
                param("all processed[2]", docPath("../res/abstracts/search/resource_search_deep_tree.json"), "two"),
                param("all processed[3]", docPath("../res/abstracts/search/resource_search_deep_tree.json"), "three"),
                param("all processed[4]", docPath("../res/abstracts/search/resource_search_deep_tree.json"), "four"),
                param("all processed[50]", docPath("../res/abstracts/search/resource_search_deep_tree.json"), "five"),
                param("all processed[60]", docPath("../res/abstracts/search/resource_search_deep_tree.json"), "six"),
                param("all processed[70]", docPath("../res/abstracts/search/resource_search_deep_tree.json"), "seven"),
                skip()
        ).lock().dynamicTests(JSonExclusionsCheck::createPathTest, PathTestParam::used);
    }

    /* ------------------------------------------------- isCorrectPath -------------------------------------------------- */


    @TestFactory
    Stream<DynamicTest> isCorrectPath() {
        return DynamicTestParams.of(
                incorrectPathFormat("/"),
                incorrectPathFormat("string that does not start from /"),
                incorrectPathFormat("one"),
                incorrectPathFormat("/one/"),
                incorrectPathFormat("/!"),
                incorrectPathFormat("/#"),
                incorrectPathFormat("/$"),
                incorrectPathFormat("/^"),
                incorrectPathFormat("/&"),
                incorrectPathFormat("/{}"),
                incorrectPathFormat("/}"),
                incorrectPathFormat("/{"),
                incorrectPathFormat("/:"),
                incorrectPathFormat("/;"),
                incorrectPathFormat("wrong path format [' -- пустая строка --- ']", ""),
                incorrectPathFormat("/<"),
                incorrectPathFormat("/."),
                incorrectPathFormat("/one/**/"),
                incorrectPathFormat("/one/***"),
                incorrectPathFormat("/one/*/***"),
                incorrectPathFormat("/one/***/*"),
                incorrectPathFormat("/*/*/*/*/*/*/*/*/*/*/*/*/*/*/"),

                correctPathFormat("/*/*/*/*/*/*/*/*/*/*/*/*/*/*"),
                correctPathFormat("/one/*/*"),
                correctPathFormat("/one/*/*/*"),
                correctPathFormat("/one/*/**"),
                correctPathFormat("/7"),
                correctPathFormat("/*"),
                correctPathFormat("/**"),
                correctPathFormat("/one"),
                correctPathFormat("/@componenttype"),
                correctPathFormat("/one/**"),
                correctPathFormat("/one/*"),
                correctPathFormat("/@componenttype_2/monkey_id"),
                correctPathFormat("/@componenttype2/monkey_id"),
                correctPathFormat("/@componenttype2/*"),
                correctPathFormat("/@componenttype1/componenttype2"),
                correctPathFormat("/@componenttype1/componenttype2/componenttype3"),
                skipped()).lock().
                dynamicTests(i -> DynamicTest.dynamicTest(i.testName(),
                        () -> Assertions.assertEquals(i.expected(), JSonExclusionsHack.isCorrectPath(i.path()))),
                        PathFormatChecker::used);
    }

       /* ------------------------------------------ isExclusionApplied ---------------------------------------------- */

    private static class IsExclusionAppliedParam {
        private static final IsExclusionAppliedParam UNUSED =
                new IsExclusionAppliedParam("", "", "", false, false);
        private final String exclusion;
        private final String fullPath;
        private final boolean expectedResult;
        private final boolean used;
        private final String testName;

        private IsExclusionAppliedParam(String testName, String fullPath, String exclusion, boolean expectedResult, boolean used) {
            this.testName = testName;
            this.fullPath = fullPath;
            this.exclusion = exclusion;
            this.expectedResult = expectedResult;
            this.used = used;
        }

        public boolean expectedResult() {
            return expectedResult;
        }

        public String testName() {
            return testName;
        }

        public String fullPath() {
            return fullPath;
        }

        public String exclusion() {
            return exclusion;
        }

        public boolean used() {
            return used;
        }

        @Override
        public String toString() {
            return "IsExclusionAppliedParam{" +
                    "exclusion='" + exclusion + '\'' +
                    ", fullPath='" + fullPath + '\'' +
                    ", expectedResult=" + expectedResult +
                    ", used=" + used +
                    '}';
        }
    }

    private static DefaultTreeNode getTreeNodes(String i) {
        switch (JSonExclusionsHack.getIdentityType(i)) {
            case monkeyId:
                return new DefaultTreeNode(new IdForChecks(JSonExclusionsHack.getIdentity(i), "***"));
            case componentType:
                return new DefaultTreeNode(new IdForChecks("***", JSonExclusionsHack.getIdentity(i)));
            default:
                throw new IllegalStateException();
        }
    }

    public static IsExclusionAppliedParam delimetr(String... testName) {
        return IsExclusionAppliedParam.UNUSED;
    }

    public static IsExclusionAppliedParam applied(String testName, String fullPath, String exclusion) {
        return new IsExclusionAppliedParam(testName, fullPath, exclusion, true, true);
    }

    public static IsExclusionAppliedParam notApplied(String testName, String fullName, String exclusion) {
        return new IsExclusionAppliedParam(testName, fullName, exclusion, false, true);
    }

    private static void testExclusion(IsExclusionAppliedParam p) {
        List<TreeNode> node = JSonExclusionsHack.extractData(p.fullPath()).stream().map(JSonExclusionsCheck::getTreeNodes).map(i -> (TreeNode) i).
                collect(Collectors.toList());
        Assertions.assertEquals(p.expectedResult(), JSonExclusionsHack.isExclusionApplied(node, p.exclusion()));
    }

    @TestFactory
    Stream<DynamicTest> isExclusionApplied() {
        return DynamicTestParams.of(
                delimetr("--------------------------- applied ------------------------------------------"),
                applied("applied /one", "/one", "/one"),
                applied("applied /two/two", "/two/two", "/two/*"),
                applied("applied /two/two/node/node/mode/tta 1", "/two/two/node/node/mode/tta", "/two/**"),
                applied("applied /two/two 2", "/two/two", "/two/**"),
                applied("applied /two/two/node/node/mode/tta 2", "/two/two/node/node/mode/tta", "/**"),
                applied("applied /two/two/node", "/two/two/node", "/*/*/node"),
                applied("applied /two/two/node/node/mode/tta 3", "/two/two/node/node/mode/tta", "/*/**"),
                applied("applied /@componentType", "/@componentType", "/@componentType"),
                applied("applied /@two/@two/@node 1", "/@two/@two/@node", "/*/*/@node"),
                applied("applied /@two/node/@node 2", "/@two/node/@node", "/*/*/@node"),
                applied("applied /@two/node/@node 3", "/@two/node/@node", "/*/*/node"),/* родительский тэг исключения предполагает любого непосредственного child */
                applied("applied /@two/@two/@zzz", "/@two/@two/@zzz", "/*/*/@node"), /* родительский тэг исключения предполагает любого непосредственного child */
                applied("applied /two/two/zzz", "/two/two/zzz", "/*/*/node"),/* родительский тэг исключения предполагает любого непосредственного child */
                applied("applied /two/two/@node 1", "/two/two/@node", "/**"),/* родительский тэг ** исключения предполагает любого child */
                applied("applied /two/two/@node 2", "/two/two/@node", "/two/**"),
                applied("applied /one/four/six", "/one/four/six", "/one/*"),
                applied("applied /one/four/seven", "/one/four/seven", "/one/*"),
                delimetr("--------------------------- not applied ------------------------------------------"),
                notApplied("not applied /two/two/node/node/mode/tta 1", "/two/two/node/node/mode/tta", "/*/*"),
                notApplied("not applied /two", "/two", "/one"),
                notApplied("not applied /two/two/node/node/mode/tta 2", "/two/two/node/node/mode/tta", "/*/*/node"),
                notApplied("not applied /two/mode/tta", "/two/mode/tta", "/two"),
                notApplied("not applied /one", "/one", "/two"),
                notApplied("not applied /@componentTypeTwo", "/@componentTypeTwo", "/@componentType"),
                notApplied("not applied /two/two/@node", "/two/two/@node", "/one/**"),
                notApplied("not applied /one/four/six/seven", "/one/four/six/seven", "/one/*"),
                IsExclusionAppliedParam.UNUSED).lock().
                dynamicTests(IsExclusionAppliedParam::testName, p -> () -> testExclusion(p),
                        DynamicTest::dynamicTest, IsExclusionAppliedParam::used);
    }

    /* ------------------------------------------ getIdentityType ---------------------------------------------- */
    private static class GetIdentityTypeParam {

        private static final GetIdentityTypeParam UNUSED =
                new GetIdentityTypeParam("", "", SqNodeKeyType.componentType, null, true);

        private final String testName;
        private final String string;
        private final SqNodeKeyType identityType;
        private final Class<? extends Throwable> expectedError;
        private final boolean skipped;

        private GetIdentityTypeParam(String testName, String string, SqNodeKeyType identityType,
                                     Class<? extends Throwable> expectedError, boolean skipped) {
            this.testName = testName;
            this.string = string;
            this.identityType = Objects.requireNonNull(identityType);
            this.expectedError = expectedError;
            this.skipped = skipped;
        }

        public String testName() {
            return testName;
        }

        public String string() {
            return string;
        }

        public SqNodeKeyType identityType() {
            return identityType;
        }

        public Class<? extends Throwable> expectedError() {
            return expectedError;
        }

        public boolean skipped() {
            return skipped;
        }

        public boolean used() {
            return !skipped;
        }

        private static GetIdentityTypeParam param(String testName, String string, SqNodeKeyType identityType) {
            return new GetIdentityTypeParam(testName, string, identityType, null, false);
        }

        private static GetIdentityTypeParam param(String testName, String string, SqNodeKeyType identityType,
                                                  Class<? extends Throwable> expecedError) {
            return new GetIdentityTypeParam(testName, string, identityType, expecedError, false);
        }

        private static GetIdentityTypeParam delimetr(String... data) {
            return UNUSED;
        }
    }

    private static Executable testExec(GetIdentityTypeParam param) {
        return () -> {
            try {
                Assertions.assertEquals(param.identityType(), JSonExclusionsHack.getIdentityType(param.string()));
                Assertions.assertEquals(param.expectedError(), null);
            } catch (Throwable throwable) {
                if (!throwable.getClass().isAssignableFrom(param.expectedError())) {
                    Assertions.assertEquals(param.expectedError(), throwable.getClass());
                }
            }
        };
    }

    @TestFactory
    Stream<DynamicTest> getIdentityType() {
        return DynamicTestParams.of(
                GetIdentityTypeParam.param("@componentType", "@componentType", SqNodeKeyType.componentType),
                GetIdentityTypeParam.param("monkeyId", "monkeyId", SqNodeKeyType.monkeyId),
                GetIdentityTypeParam.param("SqNodeKeyType.monkeyId ", null, SqNodeKeyType.monkeyId, NullPointerException.class),
                GetIdentityTypeParam.param("SqNodeKeyType.componentType", null, SqNodeKeyType.componentType, NullPointerException.class),
                GetIdentityTypeParam.delimetr()
        ).lock().dynamicTests(GetIdentityTypeParam::testName, JSonExclusionsCheck::testExec, DynamicTest::dynamicTest,
                GetIdentityTypeParam::used);
    }

    /*  ---------------------------------- aux ---------------------------------------------------------------------- */


    private static String docPath(String documentRelativePath) {
        return getAbsoluteResourcePath(JSonExclusionsCheck.class, documentRelativePath);
    }

    private static DynamicTest createPathTest(PathTestParam param) {
        return DynamicTest.dynamicTest(param.testName(), () -> {
            LoadResult load = JSonMapper.load(param.documentPath(), DefaultWidgetNode.class, Collections.emptyMap());
            Map<String, DefaultWidgetNode> objects = TreeFlatter.flatten(load.root()).stream().map(i -> (DefaultWidgetNode) i.object()).
                    collect(Collectors.toMap(DefaultWidgetNode::monkeyId, wn -> wn));
            /* objects содержит соответствие объекта имени*/

            DefaultWidgetNode object = Objects.requireNonNull(objects.get(param.objectName()));
            try {
                Association<Boolean> association = JSonExclusions.collectProcessed(load.root(), param.exceptions());
                Assertions.assertEquals(param.processed(), Objects.requireNonNull(association.associatedValue(object)));

                if (param.expectedErrorClass() != null) {
                    Assertions.assertEquals(param.expectedErrorClass(), null);
                }
            } catch (Throwable throwable) {
                if (param.expectedErrorClass() == null) {
                    Assertions.assertEquals(param.expectedErrorClass(), throwable.getClass());
                } else if (!param.expectedErrorClass().isAssignableFrom(throwable.getClass())) {
                    Assertions.assertEquals(param.expectedErrorClass(), throwable.getClass());
                }

            }
        });
    }


    /*  ------------------------------------------- collectNodeNames  ------------------------------------------------ */

    private static class CollectNodeNames {
        private static final CollectNodeNames UNUSED = new CollectNodeNames();
        private final boolean used;
        private final Class<? extends Throwable> expectedErrorClass;
        private final String docPath;
        private final String objectName;
        private final String expectedValue;
        private final Class defaultClass;
        private final String testName;

        private CollectNodeNames(
                String testName,
                boolean used,
                Class<? extends Throwable> expectedErrorClass,
                String docPath,
                String objectName,
                String expectedValue,
                Class defaultClass) {

            this.testName = testName;
            this.used = used;
            this.expectedErrorClass = expectedErrorClass;
            this.docPath = docPath;
            this.objectName = objectName;
            this.expectedValue = expectedValue;
            this.defaultClass = defaultClass;
        }

        private CollectNodeNames() {
            this("", false, null, null, null, null, null);
        }

        public static CollectNodeNames error(
                String testName, String docPath, String objectName, Class<? extends Throwable> expectedErrorClass) {
            return new CollectNodeNames(testName, true, expectedErrorClass, docPath, objectName, null, DefaultWidgetNodeWithDebugId.class);
        }

        public static CollectNodeNames checkWithDefaultNodeClass(
                String testName, String docPath, String objectName, String expectedValue) {
            return new CollectNodeNames(testName, true, null, docPath, objectName, expectedValue, DefaultWidgetNodeWithDebugId.class);
        }

        public static CollectNodeNames checkWithNodeClass(
                String testName, Class defaultClass, String docPath, String objectName, String expectedValue) {
            return new CollectNodeNames(testName, true, null, docPath, objectName, expectedValue, defaultClass);
        }

        public static CollectNodeNames delimetr(String... data) {
            return UNUSED;
        }

        public static CollectNodeNames unused() {
            return UNUSED;
        }

        public CollectNodeNames ignore() {
            return UNUSED;
        }

        public String testName() {
            return testName;
        }

        public boolean used() {
            return used;
        }

        @Override
        public String toString() {
            return "CollectNodeNames{" +
                    "used=" + used +
                    ", expectedErrorClass=" + expectedErrorClass +
                    ", docPath='" + docPath + '\'' +
                    ", objectName='" + objectName + '\'' +
                    ", expectedValue='" + expectedValue + '\'' +
                    ", defaultClass=" + defaultClass +
                    '}';
        }
    }

//    public static class OnlyMonkeyId implements UnmappedProperties, DebugId, Identity<SqNodeKeyType> {
//        private String monkeyId;
//        private String debugId;
//
//        public OnlyMonkeyId() {
//        }
//
//        public void setDebugId(String debugId) {
//            this.debugId = debugId;
//        }
//
//
//        @SuppressWarnings("unused")
//        public void setMonkeyId(String monkeyId) {
//            this.monkeyId = monkeyId;
//        }
//
//        public String monkeyId() {
//            return monkeyId;
//        }
//
//        @Override
//        public void setValue(String propertyName, String VALUE) {
//        }
//
//        @Override
//        public String id() {
//            return debugId;
//        }
//
//        @Override
//        public boolean supports(Class<?> identityType) {
//            return SqNodeKeyType.class == identityType;
//        }
//
//        @Override
//        public String ident(SqNodeKeyType identityType) {
//            switch (identityType) {
//                case componentType: {
//                    return null;
//                }
//
//                case monkeyId: {
//                    return monkeyId;
//                }
//
//                default:
//                    throw new IllegalStateException("unknown identityType " + identityType);
//            }
//        }
//    }
//
//    public static class OnlyComponentType implements UnmappedProperties, DebugId, Identity<SqNodeKeyType> {
//        private String componentType;
//        private String debugId;
//
//
//        public OnlyComponentType() {
//        }
//
//
//        @SuppressWarnings("unused")
//        public void setComponentType(String componentType) {
//            this.componentType = componentType;
//        }
//
//        public void setDebugId(String debugId) {
//            this.debugId = debugId;
//        }
//
//        public String componentType() {
//            return componentType;
//        }
//
//        @Override
//        public void setValue(String propertyName, String VALUE) {
//        }
//
//        @Override
//        public boolean supports(Class<?> identityType) {
//            return SqNodeKeyType.class == identityType;
//        }
//
//        @Override
//        public String ident(SqNodeKeyType identityType) {
//            switch (identityType) {
//                case componentType: {
//                    return componentType;
//                }
//
//                case monkeyId: {
//                    return null;
//                }
//
//                default:
//                    throw new IllegalStateException("unknown identityType " + identityType);
//            }
//        }
//
//        @Override
//        public String id() {
//            return debugId;
//        }
//    }

//    private static Executable collectNodeNamesExec(CollectNodeNames param) {
//        return () -> {
//            LoadResult load = JSonMapper.load(param.docPath, param.defaultClass, Collections.emptyMap());
//            Map<String, Object> objects =
//                    TreeFlatter.flatten(load.root()).stream().map(i -> (DebugId) i.object()).
//                            collect(Collectors.toMap(DebugId::id, di -> di));
//            Object object = objects.get(param.objectName);
//
//
//            /* objects содержит соответствие объекта имени*/
//            Association<String> association = null;
//
//            String actualResult = null;
//            try {
//                association = JSonExclusions.collectNodeNames(load.root());
//                actualResult = association.associatedValue(object);
//                if (param.expectedErrorClass != null) {
//                    Assertions.assertEquals(param.expectedErrorClass, null);
//                }
//            } catch (Throwable throwable) {
//                if (param.expectedErrorClass == null) {
//                    Assertions.assertEquals(null, throwable.getClass());
//                } else if (!param.expectedErrorClass.isAssignableFrom(throwable.getClass())) {
//                    Assertions.assertEquals(param.expectedErrorClass, throwable.getClass());
//                }
//                return;
//            }
//            Assertions.assertEquals(param.expectedValue, actualResult);
//
//        };
//    }


    /*

   Схематичная струтура документа - resource_search_deep_tree.json
                                       one
                                        |
                                        |
                        -------------------------------
                        |               |             |
                        |               |             |
                        two            three         four
                                                      |
                                                      |
                                                -----------------------
                                               |          |           |
                                               |          |           |
                                               five      six      seven

 */


//    @TestFactory
//    Stream<DynamicTest> collectNodeNames() {
//        return DynamicTestParams.of(
//                CollectNodeNames.delimetr(),
//
//                CollectNodeNames.error("error 10", docPath("../res/abstracts/search/resource_search_deep_tree.json"),
//                        "OBJECT_THAT_IS_NOT_THERE", NullPointerException.class),
//
//                CollectNodeNames.checkWithDefaultNodeClass("default node class 10",
//                        docPath("../res/abstracts/search/resource_search_deep_tree.json"), "one", "/one"),
//                CollectNodeNames.checkWithDefaultNodeClass("default node class 20",
//                        docPath("../res/abstracts/search/resource_search_deep_tree.json"), "two", "/one/two"),
//                CollectNodeNames.checkWithDefaultNodeClass("default node class 30",
//                        docPath("../res/abstracts/search/resource_search_deep_tree.json"), "three", "/one/three"),
//                CollectNodeNames.checkWithDefaultNodeClass("default node class 40",
//                        docPath("../res/abstracts/search/resource_search_deep_tree.json"), "four", "/one/four"),
//                CollectNodeNames.checkWithDefaultNodeClass("default node class 50",
//                        docPath("../res/abstracts/search/resource_search_deep_tree.json"), "five", "/one/four/five"),
//                CollectNodeNames.checkWithDefaultNodeClass("default node class 60",
//                        docPath("../res/abstracts/search/resource_search_deep_tree.json"), "six", "/one/four/six"),
//                CollectNodeNames.checkWithDefaultNodeClass("default node class 70",
//                        docPath("../res/abstracts/search/resource_search_deep_tree.json"), "seven", "/one/four/seven"),
//
//                CollectNodeNames.checkWithNodeClass("check with node class 10",
//                        OnlyMonkeyId.class, docPath("../res/abstracts/search/resource_search_deep_tree.json"),
//                        "one", "/one"),
//                CollectNodeNames.checkWithNodeClass("check with node class 20",
//                        OnlyMonkeyId.class, docPath("../res/abstracts/search/resource_search_deep_tree.json"),
//                        "two", "/one/two"),
//                CollectNodeNames.checkWithNodeClass("check with node class 30",
//                        OnlyMonkeyId.class, docPath("../res/abstracts/search/resource_search_deep_tree.json"),
//                        "three", "/one/three"),
//                CollectNodeNames.checkWithNodeClass("check with node class 40",
//                        OnlyMonkeyId.class, docPath("../res/abstracts/search/resource_search_deep_tree.json"),
//                        "four", "/one/four"),
//                CollectNodeNames.checkWithNodeClass("check with node class 50",
//                        OnlyMonkeyId.class, docPath("../res/abstracts/search/resource_search_deep_tree.json"),
//                        "five", "/one/four/five"),
//                CollectNodeNames.checkWithNodeClass("check with node class 60",
//                        OnlyMonkeyId.class, docPath("../res/abstracts/search/resource_search_deep_tree.json"),
//                        "six", "/one/four/six"),
//                CollectNodeNames.checkWithNodeClass("check with node class 70",
//                        OnlyMonkeyId.class, docPath("../res/abstracts/search/resource_search_deep_tree.json"),
//                        "seven", "/one/four/seven"),
//                CollectNodeNames.checkWithNodeClass("check with node class 80",
//                        OnlyComponentType.class, docPath("../res/abstracts/search/resource_search_deep_tree.json"),
//                        "one", "/@sqc_scriptable_dialog"),
//                CollectNodeNames.checkWithNodeClass("check with node class 90",
//                        OnlyComponentType.class, docPath("../res/abstracts/search/resource_search_deep_tree.json"),
//                        "two", "/@sqc_scriptable_dialog/@milk_bevel"),
//                CollectNodeNames.checkWithNodeClass("check with node class 100",
//                        OnlyComponentType.class, docPath("../res/abstracts/search/resource_search_deep_tree.json"),
//                        "three", "/@sqc_scriptable_dialog/@milk_bevel"),
//                CollectNodeNames.checkWithNodeClass("check with node class 110",
//                        OnlyComponentType.class, docPath("../res/abstracts/search/resource_search_deep_tree.json"),
//                        "four", "/@sqc_scriptable_dialog/@milk_bevel"),
//                CollectNodeNames.checkWithNodeClass("check with node class 120",
//                        OnlyComponentType.class, docPath("../res/abstracts/search/resource_search_deep_tree.json"),
//                        "five", "/@sqc_scriptable_dialog/@milk_bevel/@milk_bevel"),
//                CollectNodeNames.checkWithNodeClass("check with node class 130",
//                        OnlyComponentType.class, docPath("../res/abstracts/search/resource_search_deep_tree.json"),
//                        "six", "/@sqc_scriptable_dialog/@milk_bevel/@milk_bevel"),
//                CollectNodeNames.checkWithNodeClass("check with node class 140",
//                        OnlyComponentType.class, docPath("../res/abstracts/search/resource_search_deep_tree.json"),
//                        "seven", "/@sqc_scriptable_dialog/@milk_bevel/@milk_bevel"),
//
//                CollectNodeNames.unused()
//        ).lock().dynamicTests(CollectNodeNames::testName, JSonExclusionsCheck::collectNodeNamesExec,
//                DynamicTest::dynamicTest, CollectNodeNames::used);
//    }


}
