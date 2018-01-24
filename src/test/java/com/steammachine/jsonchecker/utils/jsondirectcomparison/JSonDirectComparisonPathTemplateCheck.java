package com.steammachine.jsonchecker.utils.jsondirectcomparison;

import com.steammachine.org.junit5.extensions.dynamictests.dynamictestparam.DynamicTestParams;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.Executable;
import com.steammachine.jsonchecker.impl.flatter2.PathCluster;
import com.steammachine.jsonchecker.types.NodeCheckResult;
import com.steammachine.jsonchecker.types.exceptions.WrongDataFormat;
import com.steammachine.jsonchecker.utils.JSonDirectComparator;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Created by Vladimir Bogoduhov on 01.12.2017.
 *
 * @author Vladimir Bogoduhov
 */
class JSonDirectComparisonPathTemplateCheck {
    private static final PathCluster CLUSTER = PathCluster.of(null, null);
    private static final Supplier<PathCluster> STUB_CLUSTER = () -> CLUSTER;
    private static final Supplier<PathCluster> NULL_CLUSTER = () -> null;


 /* --------------------------------------------- isApplied -------------------------------------------  */

    private static class ExclusionParam {
        private static final ExclusionParam SKIPPED = new ExclusionParam();

        private final boolean used;
        private final Class<? extends Throwable> expectedError;
        private final boolean errorChecks;
        private final Supplier<PathCluster> path;
        private final Supplier<PathCluster> exclusion;
        private final boolean expectedResult;
        private final String testName;

        /**
         * Создает объект который не используется
         */
        public ExclusionParam() {
            this.used = false;
            this.testName = null;
            this.expectedError = null;
            this.path = null;
            this.exclusion = null;
            this.expectedResult = false;
            this.errorChecks = false;
        }

        /**
         * Создает параметр для проверки выпонения
         *
         * @param testName       - имя теста
         * @param path           - проверяемый путь
         * @param exclusion      - проверяемое исключение
         * @param expectedResult - результат
         */
        public ExclusionParam(
                String testName,
                Supplier<PathCluster> path,
                Supplier<PathCluster> exclusion,
                boolean expectedResult
        ) {
            this.used = true;
            this.testName = testName;
            this.expectedError = null;
            this.path = Objects.requireNonNull(path);
            this.exclusion = Objects.requireNonNull(exclusion);
            this.expectedResult = expectedResult;
            this.errorChecks = false;
        }

        /**
         * Создает параметр для проверки выброшеных исключений
         *
         * @param testName  - имя теста
         * @param path      - проверяемый путь
         * @param exclusion - проверяемое исключение
         */
        public ExclusionParam(
                String testName,
                Supplier<PathCluster> path,
                Supplier<PathCluster> exclusion,
                Class<? extends Throwable> expectedError
        ) {
            this.used = true;
            this.testName = testName;
            this.expectedError = expectedError;
            this.path = Objects.requireNonNull(path);
            this.exclusion = Objects.requireNonNull(exclusion);
            this.expectedResult = false;
            this.errorChecks = true;
        }


        public boolean used() {
            return used;
        }

        public ExclusionParam ignore() {
            return SKIPPED;
        }

        @Override
        public String toString() {
            return "ExclusionParam{" +
                    "used=" + used +
                    ", expectedError=" + expectedError +
                    ", errorChecks=" + errorChecks +
                    ", path='" + path + '\'' +
                    ", exclusion='" + exclusion + '\'' +
                    ", expectedResult=" + expectedResult +
                    ", testName='" + testName + '\'' +
                    '}';
        }

        public static ExclusionParam skipped() {
            return SKIPPED;
        }

        @SuppressWarnings("unused")
        public static ExclusionParam delemiter(String... message) {
            return SKIPPED;
        }

        public static ExclusionParam error(String testName, Supplier<PathCluster> path, Supplier<PathCluster> exclusion, Class<? extends Throwable> expectedError) {
            return new ExclusionParam(testName, path, exclusion, expectedError);
        }

        public static ExclusionParam comaprison(String testName, Supplier<PathCluster> path, Supplier<PathCluster> exclusion, boolean result) {
            return new ExclusionParam(testName, path, exclusion, result);
        }

        public static DynamicTest isApplied(ExclusionParam param) {
            String displayName = param.testName != null ? param.testName : "" + param;
            return DynamicTest.dynamicTest(displayName, () -> {
                @SuppressWarnings("UnusedAssignment")
                Boolean result = null;
                try {
                /* <--- isExclusionApplied - тестируемый метод <--- */
                    result = JSonDirectComparisonH.isApplied(param.path.get(), param.exclusion.get());
                } catch (Throwable throwable) {
                    if (param.expectedError == null) {
                        Assertions.assertEquals(null, throwable.getClass());
                    } else if (!param.expectedError.isAssignableFrom(throwable.getClass())) {
                        Assertions.assertEquals(param.expectedError, throwable.getClass());
                    }
                    return;
                }

                if (param.errorChecks) {
                    if (param.expectedError != null) {
                        Assertions.assertEquals(null, param.expectedError);
                    }
                } else {
                    /* Если не проверяем какое исключение должно выброситься - проверяем полученый результат */
                    Objects.requireNonNull(result);
                    Assertions.assertEquals(param.expectedResult, result);
                }

            });
        }
    }

    @TestFactory
    Stream<DynamicTest> exclusionTest() {
        return DynamicTestParams.of(
                ExclusionParam.delemiter("--------------------------------- Errors ----------------------------------"),
                ExclusionParam.delemiter("--------------------------------- Null checks -----------------------------"),
                ExclusionParam.delemiter(),
                ExclusionParam.error("Null checks 10", NULL_CLUSTER, NULL_CLUSTER, NullPointerException.class),
                ExclusionParam.error("Null checks 20", STUB_CLUSTER, NULL_CLUSTER, NullPointerException.class),
                ExclusionParam.error("Null checks 30", NULL_CLUSTER, STUB_CLUSTER, NullPointerException.class),
                ExclusionParam.error("Null checks 40", STUB_CLUSTER, STUB_CLUSTER, null),
                ExclusionParam.error("Null checks 50", () -> cluster("a"), () -> cluster("a"), null),
                ExclusionParam.delemiter(),
                ExclusionParam.delemiter("--------------------------------- Format checks ---------------------------"),
                ExclusionParam.delemiter(),
                ExclusionParam.error("format checks 10", STUB_CLUSTER, STUB_CLUSTER, null),
                ExclusionParam.error("format checks 30", () -> cluster("VALUE.."), () -> cluster("VALUE.."), WrongDataFormat.class),
                ExclusionParam.error("format checks 40", () -> cluster("VALUE"), () -> cluster("VALUE.."), WrongDataFormat.class),
                ExclusionParam.error("format checks 50", () -> cluster("VALUE.."), () -> cluster("VALUE"), WrongDataFormat.class),
                ExclusionParam.error("format checks 60", () -> cluster("VALUE"), () -> cluster("VALUE[*]"), null),
                ExclusionParam.error("format checks - correct format 10", () -> cluster("object.VALUE"), () -> cluster("object.VALUE"), null),
                ExclusionParam.error("format checks - correct format 20", () -> cluster("object.VALUE[1]"), () -> cluster("object.VALUE[1]"), null),
                ExclusionParam.error("format checks - correct format 30", () -> cluster("object.VALUE[1].type"), () -> cluster("object.VALUE[1].type"), null),
                ExclusionParam.error("format checks - correct format 40", () -> cluster("object.VALUE[0][0]"), () -> cluster("object.VALUE[0][0]"), null),
                ExclusionParam.error("format checks - correct format 50", () -> cluster("object.VALUE[0][0][0]"), () -> cluster("object.VALUE[0][0][0]"), null),
                ExclusionParam.error("format checks - correct format 60", () -> cluster("object.VALUE[0][0][0].type[0]"), () -> cluster("object.VALUE[0][0][0].type[0]"), null),
                ExclusionParam.error("format checks - correct format 70", () -> cluster("obj::ect.va::lue[0][0][0].type[0]"), () -> cluster("object.VALUE[0][0][0].type[0]"), null),
                ExclusionParam.delemiter(),
                ExclusionParam.delemiter("--------------------------------- Comparison checks -------------------------"),
                ExclusionParam.delemiter(),
                ExclusionParam.comaprison("Comparison 10", () -> cluster("c"), () -> cluster("c"), true),
                ExclusionParam.comaprison("Comparison 20", () -> cluster("c"), () -> cluster("a"), false),
                ExclusionParam.comaprison("Comparison 30", () -> cluster("a"), () -> cluster("c"), false),
                ExclusionParam.comaprison("Comparison 40", () -> cluster("a"), () -> cluster("c"), false),
                ExclusionParam.comaprison("Comparison 50", () -> cluster("a.b"), () -> cluster("a.b"), true),
                ExclusionParam.comaprison("Comparison 60", () -> cluster("a.b"), () -> cluster("a.c"), false),
                ExclusionParam.comaprison("Comparison 70", () -> cluster("a.b"), () -> cluster("c.b"), false),
                ExclusionParam.comaprison("Comparison 80", () -> cluster("a[1]"), () -> cluster("a[1]"), true),
                ExclusionParam.delemiter("-------------------- Comparison (monkeyId format) checks ------------------"),
                ExclusionParam.comaprison("Comparison (monkeyId format) 10", () -> cluster("/c"), () -> cluster("/c"), true),
                ExclusionParam.comaprison("Comparison (monkeyId format) 20", () -> cluster("/c"), () -> cluster("/a"), false),
                ExclusionParam.comaprison("Comparison (monkeyId format) 30", () -> cluster("/a"), () -> cluster("/c"), false),
                ExclusionParam.comaprison("Comparison (monkeyId format) 40", () -> cluster("/a"), () -> cluster("/c"), false),
                ExclusionParam.comaprison("Comparison (monkeyId format) 50", () -> cluster("/a/b"), () -> cluster("/a/b"), true),
                ExclusionParam.comaprison("Comparison (monkeyId format) 60", () -> cluster("/a/b"), () -> cluster("/a/c"), false),
                ExclusionParam.comaprison("Comparison (monkeyId format) 70", () -> cluster("/a/b"), () -> cluster("/c/b"), false),
                ExclusionParam.comaprison("Comparison (monkeyId format) 80", () -> cluster("/a/[1]"), () -> cluster("/a/[1]"), true),
                ExclusionParam.delemiter(),
                ExclusionParam.delemiter("--------------------------------- Comparison with wildcards ---------------"),
                ExclusionParam.delemiter(),
                ExclusionParam.comaprison("Comparison with wildcards 10", () -> cluster("a[1]"), () -> cluster("a[*]"), true),
                ExclusionParam.comaprison("Comparison with wildcards 20", () -> cluster("a.b"), () -> cluster("a.*"), true),
                ExclusionParam.comaprison("Comparison with wildcards 30", () -> cluster("a.b.c.d"), () -> cluster("a.*.c.d"), true),
                ExclusionParam.comaprison("Comparison with wildcards 40", () -> cluster("a.b.c.d[1]"), () -> cluster("a.*.c.d[1]"), true),
                ExclusionParam.comaprison("Comparison with wildcards 50", () -> cluster("a.b.c.d[1]"), () -> cluster("a.*.c.d[*]"), true),
                ExclusionParam.delemiter(),
                ExclusionParam.comaprison("Comparison with wildcards 110", () -> cluster("/a/[1]"), () -> cluster("/a/[*]"), true),
                ExclusionParam.comaprison("Comparison with wildcards 120", () -> cluster("/a/b"), () -> cluster("/a/*"), true),
                ExclusionParam.comaprison("Comparison with wildcards 130", () -> cluster("/a/b/c/d"), () -> cluster("/a/*/c/d"), true),
                ExclusionParam.comaprison("Comparison with wildcards 140", () -> cluster("/a/b/c/d/[1]"), () -> cluster("/a/*/c/d/[1]"), true),
                ExclusionParam.comaprison("Comparison with wildcards 150", () -> cluster("/a/b/c/d/[1]"), () -> cluster("/a/*/c/d/[*]"), true),
                ExclusionParam.comaprison("Comparison with wildcards 160", () -> cluster("/a::b/b::b/c/d/[1]"), () -> cluster("/a::b/b::b/c/d/[*]"), true),
                ExclusionParam.comaprison("Comparison with wildcards 170", () -> cluster("/a::b/b::b/c::c/d::d/[1]"), () -> cluster("/a::b/*/c::c/d::d/[*]"), true),
                ExclusionParam.delemiter(),
                ExclusionParam.delemiter("--------------------------------- Comparison with different length ---------"),
                ExclusionParam.delemiter(),
                ExclusionParam.comaprison("Comparison with different length 10", () -> cluster("a.b"), () -> cluster("a"), false),
                ExclusionParam.comaprison("Comparison with different length 20", () -> cluster("a"), () -> cluster("a.b"), false),
                ExclusionParam.comaprison("Comparison with different length 30", () -> cluster("a.b.c.d"), () -> cluster("a.*.c"), false),
                ExclusionParam.comaprison("Comparison with different length 40", () -> cluster("a.b.c[1]"), () -> cluster("a.*.c.d[1]"), false),
                ExclusionParam.comaprison("Comparison with different length 50", () -> cluster("a.b.b.b.b"), () -> cluster("a.b"), false),
                ExclusionParam.comaprison("Comparison with different length 60", () -> cluster("a[1][1]"), () -> cluster("a[1]"), false),
                ExclusionParam.comaprison("Comparison with different length 70", () -> cluster("a[0][1].b"), () -> cluster("a[0][1].b.c"), false),

                ExclusionParam.comaprison("Comparison with different length 110", () -> cluster("/a/b"), () -> cluster("/a"), false),
                ExclusionParam.comaprison("Comparison with different length 120", () -> cluster("/a"), () -> cluster("/a/b"), false),
                ExclusionParam.comaprison("Comparison with different length 130", () -> cluster("/a/b/c/d"), () -> cluster("/a/*/c"), false),
                ExclusionParam.comaprison("Comparison with different length 140", () -> cluster("/a/b/c/[1]"), () -> cluster("/a/*/c/d/[1]"), false),
                ExclusionParam.comaprison("Comparison with different length 150", () -> cluster("/a/b/b/b/b"), () -> cluster("/a/b"), false),
                ExclusionParam.comaprison("Comparison with different length 160", () -> cluster("/a/[1]/[1]"), () -> cluster("/a/[1]"), false),
                ExclusionParam.comaprison("Comparison with different length 170", () -> cluster("/a/[0]/[1]/b"), () -> cluster("/a/[0]/[1]/b/c"), false),
                ExclusionParam.delemiter(),
                ExclusionParam.delemiter("---------------------------- Universal wildcards -----------"),
                ExclusionParam.delemiter(" Тут проверяются универсальные шаблоны (wildcards)."),
                ExclusionParam.delemiter(" в том случае, когда используются универсальные шаблоны считаем, что "),
                ExclusionParam.delemiter(" шаблон применим если окончание проверяемого пути соответствует шаблону."),
                ExclusionParam.delemiter("  a.b.c.d  ->>>  **.d  "),
                ExclusionParam.delemiter("                                                                        "),
                ExclusionParam.delemiter("  Второй тип шаблона"),
                ExclusionParam.delemiter("  шаблон применим если шаблон соответствует какой либо части."),
                ExclusionParam.delemiter("  a.b.ddd.e  ->>>  ***.ddd  "),
                ExclusionParam.delemiter(),
                ExclusionParam.delemiter(),
                ExclusionParam.comaprison("Universal wildcards 7", () -> cluster("a.b.c.d"), () -> cluster("**.b.c.d"), true),
                ExclusionParam.comaprison("Universal wildcards 5", () -> cluster("c.d"), () -> cluster("**.b.c.d"), false),
                ExclusionParam.comaprison("Universal wildcards 10", () -> cluster("a.b.c.d"), () -> cluster("**.d"), true),
                ExclusionParam.comaprison("Universal wildcards 20", () -> cluster("start.a.b.c.d.e.f.j.h.i.j.k.x.y.z"), () -> cluster("**.x.y.z"), true),
                ExclusionParam.comaprison("Universal wildcards 40", () -> cluster("start.a.b.c.d.e.f.j.h.i.j.k.coordinates[1]"), () -> cluster("**.coordinates[*]"), true),
                ExclusionParam.comaprison("Universal wildcards 50", () -> cluster("start.a.b.c.d.e.f.j.h.i.j.k.coordinates[2]"), () -> cluster("**.coordinates[*]"), true),
                ExclusionParam.comaprison("Universal wildcards 60", () -> cluster("start.a.b.c.d.e.f.j.h.i.j.k.coordinates[1]"), () -> cluster("**.*"), false),
                ExclusionParam.comaprison("Universal wildcards 70", () -> cluster("start.a.b.c.d.e.f.j.h.i.j.k.coordinates[1]"), () -> cluster("**.coordinates[*]"), true),
                ExclusionParam.comaprison("Universal wildcards 80", () -> cluster("start.a.b.c.d.e.f.j.h.i.j.k.coordinates"), () -> cluster("**.[*]"), false),
                ExclusionParam.comaprison("Universal wildcards 90", () -> cluster("start.a.b.c.d.e.f.j.h.i.j.k.coordinates"), () -> cluster("**.coordinates[*]"), false),
                ExclusionParam.comaprison("Universal wildcards 100", () -> cluster("start.a.b.c.d.e.f.j.h.i.j.k.coordinates[1]"), () -> cluster("**.coordinates[*]"), true),
                ExclusionParam.comaprison("Universal wildcards 110", () -> cluster("start.a.b.c.d.e.f.j.h.i.j.k.coordinates[1]"), () -> cluster("**.coordinates[1]"), true),
                ExclusionParam.comaprison("Universal wildcards 120", () -> cluster("start.a.b.c.d.e.f.j.h.i.j.k.coordinates[2]"), () -> cluster("**.coordinates[2]"), true),
                ExclusionParam.comaprison("Universal wildcards 130", () -> cluster("start.a.b.c.d.e.f.j.h.i.j.k[2]"), () -> cluster("**.k[2]"), true),
                ExclusionParam.comaprison("Universal wildcards 140", () -> cluster("start.a.b.c.d.e.f.j.h.i.j.k[2][1]"), () -> cluster("**.[1]"), true),
                ExclusionParam.comaprison("Universal wildcards 150", () -> cluster("start.a.b.c.d.e.f.j.h.i.j.k[2][1]"), () -> cluster("**.[2]"), false),
                ExclusionParam.comaprison("Universal wildcards 160", () -> cluster("start.a.b.c.d.e.f.j.h.i.j.k[2][1]"), () -> cluster("**.[*]"), true),
                ExclusionParam.comaprison("Universal wildcards 170", () -> cluster("start.a.b.c.d.e.f.j.h.i.j.k[2]"), () -> cluster("**.k[2]"), true),
                ExclusionParam.comaprison("Universal wildcards 180", () -> cluster("start.a.b.c.d.e.f.j.h.i.j.k[2]"), () -> cluster("**.k[*]"), true),
                ExclusionParam.comaprison("Universal wildcards 190", () -> cluster("start.a.b.c.d.e.f.j.h.i.j.k[2]"), () -> cluster("**.k[3]"), false),
                ExclusionParam.delemiter(),
                ExclusionParam.comaprison("Universal wildcards 200", () -> cluster("a.b.c.d"), () -> cluster("***.b.c.d"), true),
                ExclusionParam.comaprison("Universal wildcards 210", () -> cluster("c.d"), () -> cluster("***.b.c.d"), false),
                ExclusionParam.comaprison("Universal wildcards 214", () -> cluster("a.b.c.d"), () -> cluster("***.d"), true),
                ExclusionParam.comaprison("Universal wildcards 220", () -> cluster("start.a.b.c.d.e.f.j.h.i.j.k.x.y.z"), () -> cluster("***.x.y.z"), true),
                ExclusionParam.comaprison("Universal wildcards 230", () -> cluster("start.a.b.c.d.e.f.j.h.i.j.k.coordinates[1]"), () -> cluster("***.coordinates[*]"), true),
                ExclusionParam.comaprison("Universal wildcards 250", () -> cluster("start.a.b.c.d.e.f.j.h.i.j.k.coordinates[2]"), () -> cluster("***.coordinates[*]"), true),
                ExclusionParam.comaprison("Universal wildcards 260", () -> cluster("start.a.b.c.d.e.f.j.h.i.j.k.coordinates[1]"), () -> cluster("***.*"), true),
                ExclusionParam.comaprison("Universal wildcards 270", () -> cluster("start.a.b.c.d.e.f.j.h.i.j.k.coordinates[1]"), () -> cluster("***.coordinates[*]"), true),
                ExclusionParam.comaprison("Universal wildcards 280", () -> cluster("start.a.b.c.d.e.f.j.h.i.j.k.coordinates"), () -> cluster("***.[*]"), false),
                ExclusionParam.comaprison("Universal wildcards 290", () -> cluster("start.a.b.c.d.e.f.j.h.i.j.k.coordinates"), () -> cluster("***.coordinates[*]"), false),
                ExclusionParam.comaprison("Universal wildcards 300", () -> cluster("start.a.b.c.d.e.f.j.h.i.j.k.coordinates[1]"), () -> cluster("***.coordinates[*]"), true),
                ExclusionParam.comaprison("Universal wildcards 310", () -> cluster("start.a.b.c.d.e.f.j.h.i.j.k.coordinates[1]"), () -> cluster("***.coordinates[1]"), true),
                ExclusionParam.comaprison("Universal wildcards 320", () -> cluster("start.a.b.c.d.e.f.j.h.i.j.k.coordinates[2]"), () -> cluster("***.coordinates[2]"), true),
                ExclusionParam.comaprison("Universal wildcards 330", () -> cluster("start.a.b.c.d.e.f.j.h.i.j.k[2]"), () -> cluster("***.k[2]"), true),
                ExclusionParam.comaprison("Universal wildcards 340", () -> cluster("start.a.b.c.d.e.f.j.h.i.j.k[2][1]"), () -> cluster("***.[1]"), true),
                ExclusionParam.comaprison("Universal wildcards 350", () -> cluster("start.a.b.c.d.e.f.j.h.i.j.k[2][1]"), () -> cluster("***.[2]"), true),
                ExclusionParam.comaprison("Universal wildcards 352", () -> cluster("start.a.b.c.d.e.f.j.h.i.j.k[2][1]"), () -> cluster("***.[3]"), false),
                ExclusionParam.comaprison("Universal wildcards 360", () -> cluster("start.a.b.c.d.e.f.j.h.i.j.k[2][1]"), () -> cluster("***.[*]"), true),
                ExclusionParam.comaprison("Universal wildcards 370", () -> cluster("start.a.b.c.d.e.f.j.h.i.j.k[2]"), () -> cluster("***.k[2]"), true),
                ExclusionParam.comaprison("Universal wildcards 380", () -> cluster("start.a.b.c.d.e.f.j.h.i.j.k[2]"), () -> cluster("***.k[*]"), true),
                ExclusionParam.comaprison("Universal wildcards 390", () -> cluster("start.a.b.c.d.e.f.j.h.i.j.k[2]"), () -> cluster("***.k[3]"), false),
                ExclusionParam.delemiter(),
                ExclusionParam.comaprison("Universal wildcards 400", () -> cluster("a.b.c.d.z.y.x"), () -> cluster("***.b.c.d"), true),
                ExclusionParam.comaprison("Universal wildcards 410", () -> cluster("c.d"), () -> cluster("***.b.c.d"), false),
                ExclusionParam.comaprison("Universal wildcards 413", () -> cluster("a.b.c.d"), () -> cluster("***.d"), true),
                ExclusionParam.comaprison("Universal wildcards 420", () -> cluster("start.a.b.c.d.e.f.j.h.i.j.k.x.y.z.z.y.x"), () -> cluster("***.x.y.z"), true),
                ExclusionParam.comaprison("Universal wildcards 430", () -> cluster("start.a.b.c.d.e.f.j.h.i.j.k.coordinates[1].y.x"), () -> cluster("***.coordinates[*]"), true),
                ExclusionParam.comaprison("Universal wildcards 450", () -> cluster("start.a.b.c.d.e.f.j.h.i.j.k.coordinates[2].y.x"), () -> cluster("***.coordinates[*]"), true),
                ExclusionParam.comaprison("Universal wildcards 460", () -> cluster("start.a.b.c.d.e.f.j.h.i.j.k.coordinates[1].y.x"), () -> cluster("***.*"), true),
                ExclusionParam.comaprison("Universal wildcards 470", () -> cluster("start.a.b.c.d.e.f.j.h.i.j.k.coordinates[1].y.x"), () -> cluster("***.coordinates[*]"), true),
                ExclusionParam.comaprison("Universal wildcards 480", () -> cluster("start.a.b.c.d.e.f.j.h.i.j.k.coordinates"), () -> cluster("***.[*]"), false),
                ExclusionParam.comaprison("Universal wildcards 490", () -> cluster("start.a.b.c.d.e.f.j.h.i.j.k.coordinates"), () -> cluster("***.coordinates[*]"), false),
                ExclusionParam.comaprison("Universal wildcards 500", () -> cluster("start.a.b.c.d.e.f.j.h.i.j.k.coordinates[1].y.x"), () -> cluster("***.coordinates[*]"), true),
                ExclusionParam.comaprison("Universal wildcards 510", () -> cluster("start.a.b.c.d.e.f.j.h.i.j.k.coordinates[1].y.x"), () -> cluster("***.coordinates[1]"), true),
                ExclusionParam.comaprison("Universal wildcards 520", () -> cluster("start.a.b.c.d.e.f.j.h.i.j.k.coordinates[2].y.x"), () -> cluster("***.coordinates[2]"), true),
                ExclusionParam.comaprison("Universal wildcards 530", () -> cluster("start.a.b.c.d.e.f.j.h.i.j.k[2].y.x"), () -> cluster("***.k[2]"), true),
                ExclusionParam.comaprison("Universal wildcards 540", () -> cluster("start.a.b.c.d.e.f.j.h.i.j.k[2][1].y.x"), () -> cluster("***.[1].y.x"), true),
                ExclusionParam.comaprison("Universal wildcards 550", () -> cluster("start.a.b.c.d.e.f.j.h.i.j.k[2][1].y.x"), () -> cluster("***.[33]"), false),
                ExclusionParam.comaprison("Universal wildcards 555", () -> cluster("start.a.b.c.d.e.f.j.h.i.j.k[2][1].y.x"), () -> cluster("***.[2]"), true),
                ExclusionParam.comaprison("Universal wildcards 560", () -> cluster("start.a.b.c.d.e.f.j.h.i.j.k[2][1].y.x"), () -> cluster("***.[*]"), true),
                ExclusionParam.comaprison("Universal wildcards 570", () -> cluster("start.a.b.c.d.e.f.j.h.i.j.k[2]"), () -> cluster("***.k[2]"), true),
                ExclusionParam.comaprison("Universal wildcards 580", () -> cluster("start.a.b.c.d.e.f.j.h.i.j.k[2].y.x"), () -> cluster("***.k[*]"), true),
                ExclusionParam.comaprison("Universal wildcards 590", () -> cluster("start.a.b.c.d.e.f.j.h.i.j.k[2].y.x"), () -> cluster("***.k[3]"), false),
                ExclusionParam.delemiter(),
                ExclusionParam.delemiter(" Double asterix at the end - dot format"),
                ExclusionParam.delemiter(),
                ExclusionParam.comaprison("Double asterix at the end - dot format 10", () -> cluster("y.x"), () -> cluster("y.**"), true),
                ExclusionParam.comaprison("Double asterix at the end - dot format 20", () -> cluster("y.x"), () -> cluster("x.**"), false),
                ExclusionParam.comaprison("Double asterix at the end - dot format 23", () -> cluster("y.x"), () -> cluster("y.x.**"), false),
                ExclusionParam.comaprison("Double asterix at the end - dot format 30", () -> cluster("y[1].z"), () -> cluster("y[1].**"), true),
                ExclusionParam.comaprison("Double asterix at the end - dot format 40", () -> cluster("y[1].z.w.w.w.w.w.w"), () -> cluster("y[1].**"), true),
                ExclusionParam.comaprison("Double asterix at the end - dot format 50", () -> cluster("y[1].z.x"), () -> cluster("y[1].**"), true),
                ExclusionParam.comaprison("Double asterix at the end - dot format 60", () -> cluster("y[1].z.x"), () -> cluster("y[2].**"), false),
                ExclusionParam.comaprison("Double asterix at the end - dot format 70", () -> cluster("y[1].z.x"), () -> cluster("y[*].**"), true),
                ExclusionParam.comaprison("Double asterix at the end - dot format 80", () -> cluster("y[1]"), () -> cluster("y[*].**"), false),
                ExclusionParam.comaprison("Double asterix at the end - dot format 90", () -> cluster("y[1].z.x.a.b.c"), () -> cluster("y[*].**"), true),
                ExclusionParam.comaprison("Double asterix at the end - dot format 95", () -> cluster("y[1]"), () -> cluster("y[*].**"), false),
                ExclusionParam.comaprison("Double asterix at the end - dot format 100", () -> cluster("y[1].z.d.f.g.h"), () -> cluster("y[*].**"), true),
                ExclusionParam.comaprison("Double asterix at the end - dot format 110", () -> cluster("a.b.c.d.e.y.x.a.s.a.a"), () -> cluster("**.y.x.**"), true),
                ExclusionParam.comaprison("Double asterix at the end - dot format 120", () -> cluster("a.b.c.d.e.y.x.a"), () -> cluster("**.y.x.**"), true),
                ExclusionParam.comaprison("Double asterix at the end - dot format 130", () -> cluster("a.b.c.d.e.y.x"), () -> cluster("**.y.x.**"), false),
                ExclusionParam.comaprison("Double asterix at the end - dot format 150", () -> cluster("a.b.c.d.e.y.x.a.s.a"), () -> cluster("***.y.x.**"), true),
                ExclusionParam.comaprison("Double asterix at the end - dot format 160", () -> cluster("a.b.c.d.e.y.x"), () -> cluster("***.y.x.**"), false),
                ExclusionParam.comaprison("Double asterix at the end - dot format 164", () -> cluster("a.b.c.d.e.y.x.ss"), () -> cluster("***.y.x.**"), true),
                ExclusionParam.comaprison("Double asterix at the end - dot format 170", () -> cluster("a.b.c.d.e.y.x.a.s.a"), () -> cluster("***.y.z.**"), false),
                ExclusionParam.comaprison("Double asterix at the end - dot format 180", () -> cluster("a.b.c.d.e.y.x.a.s.a"), () -> cluster("***.y.x.**"), true),
                ExclusionParam.comaprison("Double asterix at the end - dot format 190", () -> cluster("a[1].b"), () -> cluster("***.a[1].**"), true),
                ExclusionParam.comaprison("Double asterix at the end - dot format 200", () -> cluster("a[1].b"), () -> cluster("***.a[2].**"), false),
                ExclusionParam.comaprison("Double asterix at the end - dot format 210", () -> cluster("a[1].b"), () -> cluster("***.a[77].**"), false),
                ExclusionParam.comaprison("Double asterix at the end - dot format 220", () -> cluster("a"), () -> cluster("***.a.**"), false),
                ExclusionParam.comaprison("Double asterix at the end - dot format 230", () -> cluster("a.b"), () -> cluster("***.a.**"), true),
                ExclusionParam.comaprison("Double asterix at the end - dot format 240", () -> cluster("a[1]"), () -> cluster("***.a[1].**"), false),
                ExclusionParam.comaprison("Double asterix at the end - dot format 250", () -> cluster("a[1].a"), () -> cluster("***.a[1].**"), true),
                ExclusionParam.comaprison("Double asterix at the end - dot format 260", () -> cluster("a[1]"), () -> cluster("***.a[*].**"), false),
                ExclusionParam.comaprison("Double asterix at the end - dot format 270", () -> cluster("a[1].a"), () -> cluster("***.a[*].**"), true),
                ExclusionParam.delemiter(),
                ExclusionParam.delemiter(" Double asterix at the end - monkeyId format"),
                ExclusionParam.delemiter(),
                ExclusionParam.comaprison("Double asterix at the end - monkeyId format 10", () -> cluster("/y/x"), () -> cluster("/y/**"), true),
                ExclusionParam.comaprison("Double asterix at the end - monkeyId format 20", () -> cluster("/y/x"), () -> cluster("/x/**"), false),
                ExclusionParam.comaprison("Double asterix at the end - monkeyId format 23", () -> cluster("/y/x"), () -> cluster("/y/x/**"), false),
                ExclusionParam.comaprison("Double asterix at the end - monkeyId format 30", () -> cluster("/y/[1]/z"), () -> cluster("/y/[1]/**"), true),
                ExclusionParam.comaprison("Double asterix at the end - monkeyId format 40", () -> cluster("/y/[1]/z/w/w/w/w/w/w"), () -> cluster("/y/[1]/**"), true),
                ExclusionParam.comaprison("Double asterix at the end - monkeyId format 50", () -> cluster("/y/[1]/z/x"), () -> cluster("/y/[1]/**"), true),
                ExclusionParam.comaprison("Double asterix at the end - monkeyId format 60", () -> cluster("/y/[1]/z/x"), () -> cluster("/y/[2]/**"), false),
                ExclusionParam.comaprison("Double asterix at the end - monkeyId format 70", () -> cluster("/y/[1]/z/x"), () -> cluster("/y/[*]/**"), true),
                ExclusionParam.comaprison("Double asterix at the end - monkeyId format 80", () -> cluster("/y/[1]"), () -> cluster("/y/[*]/**"), false),
                ExclusionParam.comaprison("Double asterix at the end - monkeyId format 90", () -> cluster("/y/[1]/z/x/a/b/c"), () -> cluster("/y/[*]/**"), true),
                ExclusionParam.comaprison("Double asterix at the end - monkeyId format 100", () -> cluster("/y/[1]"), () -> cluster("/y/[*]/**"), false),
                ExclusionParam.comaprison("Double asterix at the end - monkeyId format 110", () -> cluster("/y/[1]"), () -> cluster("/y/[1]/**"), false),
                ExclusionParam.comaprison("Double asterix at the end - monkeyId format 120", () -> cluster("/a/b/c/d/e/y/x/a/s/a"), () -> cluster("**/y/x/**"), true),
                ExclusionParam.comaprison("Double asterix at the end - monkeyId format 130", () -> cluster("/a/b/c/d/e/y/x/a"), () -> cluster("**/y/x/**"), true),
                ExclusionParam.comaprison("Double asterix at the end - monkeyId format 140", () -> cluster("/a/b/c/d/e/y/x"), () -> cluster("**/y/x/**"), false),
                ExclusionParam.comaprison("Double asterix at the end - monkeyId format 150", () -> cluster("/a/b/c/d/e/y/x/a/s/a"), () -> cluster("***/y/x/**"), true),
                ExclusionParam.comaprison("Double asterix at the end - monkeyId format 160", () -> cluster("/a/b/c/d/e/y/x"), () -> cluster("***/y/x/**"), false),
                ExclusionParam.comaprison("Double asterix at the end - monkeyId format 169", () -> cluster("/a/b/c/d/e/y/x/e/e/e/e/e"), () -> cluster("***/y/x/**"), true),
                ExclusionParam.comaprison("Double asterix at the end - monkeyId format 170", () -> cluster("/a/b/c/d/e/y/x/a/s/a"), () -> cluster("***/y/z/**"), false),
                ExclusionParam.comaprison("Double asterix at the end - monkeyId format 180", () -> cluster("/a/b/c/d/e/y/x/a/s/a"), () -> cluster("***/y/x/**"), true),
                ExclusionParam.comaprison("Double asterix at the end - monkeyId format 190", () -> cluster("/a/[1]/b"), () -> cluster("***/a/[1]/**"), true),
                ExclusionParam.comaprison("Double asterix at the end - monkeyId format 200", () -> cluster("/a/[1]/b"), () -> cluster("***/a/[2]/**"), false),
                ExclusionParam.comaprison("Double asterix at the end - monkeyId format 210", () -> cluster("/a/[1]/b"), () -> cluster("***/a/[77]/**"), false),
                ExclusionParam.comaprison("Double asterix at the end - monkeyId format 220", () -> cluster("/a"), () -> cluster("***/a/**"), false),
                ExclusionParam.comaprison("Double asterix at the end - monkeyId format 230", () -> cluster("/a/b"), () -> cluster("***/a/**"), true),
                ExclusionParam.comaprison("Double asterix at the end - monkeyId format 240", () -> cluster("/a/[1]"), () -> cluster("***/a/[1]/**"), false),
                ExclusionParam.comaprison("Double asterix at the end - monkeyId format 250", () -> cluster("/a/[1]/a"), () -> cluster("***/a/[1]/**"), true),
                ExclusionParam.comaprison("Double asterix at the end - monkeyId format 260", () -> cluster("/a/[1]"), () -> cluster("***/a/[*]/**"), false),
                ExclusionParam.comaprison("Double asterix at the end - monkeyId format 270", () -> cluster("/a/[1]/a"), () -> cluster("***/a/[*]/**"), true),

                ExclusionParam.skipped()).
                lock().dynamicTests(ExclusionParam::isApplied, ExclusionParam::used);
    }


    @TestFactory
    @Tag("DebugRun")
    Stream<DynamicTest> exclusionTestDebug() {
        return DynamicTestParams.of(
                ExclusionParam.delemiter("--------------------------------- Debugs ----------------------------------"),

                ExclusionParam.error("format checks - correct format 70",
                        () -> cluster("obj::ect.va::lue[0][0][0].type[0]"), () -> cluster("obj::ect.va::lue[0][0][0].type[0]"), null),

                ExclusionParam.skipped()).
                lock().dynamicTests(ExclusionParam::isApplied, ExclusionParam::used);
    }


    @TestFactory
    @Tag("DebugRun")
    Stream<DynamicTest> exclusionTestDebug2() {
        return DynamicTestParams.of(
                ExclusionParam.delemiter("--------------------------------- Debugs ----------------------------------"),
                ExclusionParam.skipped()).
                checkTestNamesUniquiness(false).lock().dynamicTests(ExclusionParam::isApplied, ExclusionParam::used);
    }

    @TestFactory
    Stream<DynamicTest> templateChecks() {
        return DynamicTestParams.of(
                CompareJSons.delimer(" ------------------------------ check  ---------------------------------"),
                CompareJSons.delimer(),
                CompareJSons.simpleTest(
                        "template Checks 10",
                        CompareJSons.jsonFormString("{ \"a\": \"11\"}"),
                        CompareJSons.jsonFormString("{ \"a\": \"11\"}"),
                        r -> Assertions.assertTrue(r.isSuccessful())).
                        exclusions(CompareJSons.Pattern::drop).inclusions(CompareJSons.Pattern::drop)
                ,
                CompareJSons.simpleTest(
                        "template Checks 20",
                        CompareJSons.jsonFormString("{ \"a\": \"11\", \"b\": 11132312312312, \"c\": \"11\",\"e\": \"11\",\"i\": \"11\"}"),
                        CompareJSons.jsonFormString("{ \"a\": \"11\", \"b\": \"hola mundo!!! \", \"c\": \"12\",\"d\": \"11\"}"),
                        r -> Assertions.assertTrue(r.isSuccessful())).
                        inclusions(p -> p.add("a"))
                ,

                CompareJSons.simpleTest(
                        "template Checks 30",
                        CompareJSons.jsonFormString("{ \"a\": \"11\", \"b\": 11132312312312, \"c\": \"11\",\"e\": \"11\",\"i\": \"11\"}"),
                        CompareJSons.jsonFormString("{ \"a\": \"11\", \"b\": \"hola mundo!!! \", \"c\": \"12\",\"d\": \"11\"}"),
                        r -> Assertions.assertFalse(r.isSuccessful())).
                        exclusions(CompareJSons.Pattern::drop).inclusions(CompareJSons.Pattern::drop)
                ,
                CompareJSons.simpleTest(
                        "template Checks 40 - comparing the same param",
                        CompareJSons.jsonFormString("{ \"a\": [\"11\"], \"b\": 221}"),
                        CompareJSons.jsonFormString("{ \"a\": [\"12\"], \"b\": 221}"),
                        r -> Assertions.assertTrue(r.isSuccessful())).
                        useParam(p -> p.single("param", false)).exclusions(p -> p.add("a[0]"))
                ,
                CompareJSons.simpleTest(
                        "template Checks 50 - comparing the same param",
                        CompareJSons.jsonFormString("{ \"a\": [\"11\"], \"b\": 221}"),
                        CompareJSons.jsonFormString("{ \"a\": [\"12\"], \"b\": 221}"),
                        r -> Assertions.assertTrue(r.isSuccessful())).
                        useParam(p -> p.single("param", false)).exclusions(p -> p.add("a[*]"))
                ,
                CompareJSons.simpleTest(
                        "template Checks 60 - comparing the same param",
                        CompareJSons.jsonFormString("{ \"a\": [\"11\"], \"b\": 221}"),
                        CompareJSons.jsonFormString("{ \"a\": [\"12\"], \"b\": 221}"),
                        r -> Assertions.assertTrue(r.isSuccessful())).
                        useParam(p -> p.single("param", false)).inclusions(p -> p.add("b"))
                ,
                CompareJSons.simpleTest(
                        "template Checks 70 - comparing the same param",
                        CompareJSons.jsonFormString("{ \"a\": [\"11\"], \"b\": 21, \"c\": 22.1}"),
                        CompareJSons.jsonFormString("{ \"a\": [\"12\"], \"b\": 221, \"c\": 22.1}"),
                        r -> Assertions.assertTrue(r.isSuccessful())).
                        useParam(p -> p.single("param", false)).inclusions(p -> p.add("c"))
                ,
                CompareJSons.simpleTest(
                        "template Checks 80 - comparing the same param",
                        CompareJSons.jsonFormString("{ \"a\": [[[[[\"ya no parara mas\"]]]]], \"b\": 221}"),
                        CompareJSons.jsonFormString("{ \"a\": [[[[[\"ya no parara mas\"]]]]], \"b\": 21}"),
                        r -> Assertions.assertTrue(r.isSuccessful())).
                        useParam(p -> p.single("param", false)).inclusions(p -> p.add("a[0]"))
                ,
                CompareJSons.simpleTest(
                        "template Checks 80",
                        CompareJSons.jsonFormString("{ \"a\": [[[[[\"negro\"]]]]], \"b\": 31.4}"),
                        CompareJSons.jsonFormString("{ \"a\": [[[[[\"negro\"]]]]], \"b\": 4.13}"),
                        r -> Assertions.assertTrue(r.isSuccessful(), () -> "" + r.messages())).
                        inclusions(p -> p.add("a[0][0][0][0][0]"))
                ,
                CompareJSons.simpleTest(
                        "template Checks 90",
                        CompareJSons.jsonFormString("{ \"a\": [[[[[\"negro\"]]]]], \"b\": 31.4}"),
                        CompareJSons.jsonFormString("{ \"a\": [[[[[\"negro\"]]]]], \"b\": 4.13}"),
                        r -> Assertions.assertTrue(r.isSuccessful(), () -> "" + r.messages())).
                        inclusions(p -> p.add("a[*][*][*][*][*]"))
                ,
                CompareJSons.simpleTest(
                        "template Checks 100",
                        CompareJSons.jsonFormString("{ \"a\": [[[[[\"negro\"]]]]], \"b\": 31.4}"),
                        CompareJSons.jsonFormString("{ \"a\": [[[[[\"negro\"]]]]], \"b\": 4.13}"),
                        r -> Assertions.assertTrue(r.isSuccessful(), () -> "" + r.messages())).
                        inclusions(p -> p.add("***.[*]"))
                ,
                CompareJSons.simpleTest(
                        "template Checks 110",
                        CompareJSons.jsonFormString("{ \"a\": [[[[[\"negro\"]]]]], \"b\": 31.4}"),
                        CompareJSons.jsonFormString("{ \"a\": [[[[[\"negro\"]]]]], \"b\": 4.13}"),
                        r -> Assertions.assertTrue(r.isSuccessful(), () -> "" + r.messages())).
                        inclusions(p -> p.add("**.[*]"))
                ,
                CompareJSons.delimer()).
                lock().dynamicTests(CompareJSons::testName, JSonDirectComparisonPathTemplateCheck::templateChecks,
                DynamicTest::dynamicTest, CompareJSons::used);
    }


    @TestFactory
    @Tag("DebugRun")
    Stream<DynamicTest> templateChecksDebug() {
        return DynamicTestParams.of(
                CompareJSons.delimer(" ------------------------------ check  ---------------------------------"),
                CompareJSons.delimer(),


                CompareJSons.simpleTest(
                        "template Checks 20",
                        CompareJSons.jsonFormString("{ \"a\": \"11\", \"b\": 11132312312312, \"c\": \"11\",\"e\": \"11\",\"i\": \"11\"}"),
                        CompareJSons.jsonFormString("{ \"a\": \"11\", \"b\": \"hola mundo!!! \", \"c\": \"12\",\"d\": \"11\"}"),
                        r -> Assertions.assertTrue(r.isSuccessful())).
                        inclusions(p -> p.add("a")).exclusions(CompareJSons.Pattern::drop)
                ,

                CompareJSons.simpleTest(
                        "template Checks 30",
                        CompareJSons.jsonFormString("{ \"a\": \"11\", \"b\": 11132312312312, \"c\": \"11\",\"e\": \"11\",\"i\": \"11\"}"),
                        CompareJSons.jsonFormString("{ \"a\": \"11\", \"b\": \"hola mundo!!! \", \"c\": \"12\",\"d\": \"11\"}"),
                        r -> Assertions.assertFalse(r.isSuccessful())).
                        exclusions(CompareJSons.Pattern::drop).inclusions(CompareJSons.Pattern::drop)
                ,
                CompareJSons.simpleTest(
                        "template Checks 40 - comparing the same param",
                        CompareJSons.jsonFormString("{ \"a\": [\"11\"], \"b\": 221}"),
                        CompareJSons.jsonFormString("{ \"a\": [\"12\"], \"b\": 221}"),
                        r -> Assertions.assertTrue(r.isSuccessful())).
                        useParam(p -> p.single("param", false)).exclusions(p -> p.add("a[0]"))
                ,
                CompareJSons.simpleTest(
                        "template Checks 50 - comparing the same param",
                        CompareJSons.jsonFormString("{ \"a\": [\"11\"], \"b\": 221}"),
                        CompareJSons.jsonFormString("{ \"a\": [\"12\"], \"b\": 221}"),
                        r -> Assertions.assertTrue(r.isSuccessful())).
                        useParam(p -> p.single("param", false)).exclusions(p -> p.add("a[*]"))
                ,
                CompareJSons.simpleTest(
                        "template Checks 60 - comparing the same param",
                        CompareJSons.jsonFormString("{ \"a\": [\"11\"], \"b\": 221}"),
                        CompareJSons.jsonFormString("{ \"a\": [\"12\"], \"b\": 221}"),
                        r -> Assertions.assertTrue(r.isSuccessful())).
                        useParam(p -> p.single("param", false)).inclusions(p -> p.add("b"))
                ,
                CompareJSons.simpleTest(
                        "template Checks 70 - comparing the same param",
                        CompareJSons.jsonFormString("{ \"a\": [\"11\"], \"b\": 21, \"c\": 22.1}"),
                        CompareJSons.jsonFormString("{ \"a\": [\"12\"], \"b\": 221, \"c\": 22.1}"),
                        r -> Assertions.assertTrue(r.isSuccessful())).
                        useParam(p -> p.single("param", false)).inclusions(p -> p.add("c"))
                ,
                CompareJSons.simpleTest(
                        "template Checks 80 - comparing the same param",
                        CompareJSons.jsonFormString("{ \"a\": [[[[[\"ya no parara mas\"]]]]], \"b\": 221}"),
                        CompareJSons.jsonFormString("{ \"a\": [[[[[\"ya no parara mas\"]]]]], \"b\": 21}"),
                        r -> Assertions.assertTrue(r.isSuccessful())).
                        useParam(p -> p.single("param", false)).inclusions(p -> p.add("a[0]"))
                ,
                CompareJSons.simpleTest(
                        "template Checks 80",
                        CompareJSons.jsonFormString("{ \"a\": [[[[[\"negro\"]]]]], \"b\": 31.4}"),
                        CompareJSons.jsonFormString("{ \"a\": [[[[[\"negro\"]]]]], \"b\": 4.13}"),
                        r -> Assertions.assertTrue(r.isSuccessful(), () -> "" + r.messages())).
                        inclusions(p -> p.add("a[0][0][0][0][0]"))
                ,
                CompareJSons.simpleTest(
                        "template Checks 90",
                        CompareJSons.jsonFormString("{ \"a\": [[[[[\"negro\"]]]]], \"b\": 31.4}"),
                        CompareJSons.jsonFormString("{ \"a\": [[[[[\"negro\"]]]]], \"b\": 4.13}"),
                        r -> Assertions.assertTrue(r.isSuccessful(), () -> "" + r.messages())).
                        inclusions(p -> p.add("a[*][*][*][*][*]"))
                ,
                CompareJSons.simpleTest(
                        "template Checks 100",
                        CompareJSons.jsonFormString("{ \"a\": [[[[[\"negro\"]]]]], \"b\": 31.4}"),
                        CompareJSons.jsonFormString("{ \"a\": [[[[[\"negro\"]]]]], \"b\": 4.13}"),
                        r -> Assertions.assertTrue(r.isSuccessful(), () -> "" + r.messages())).
                        inclusions(p -> p.add("***.[*]"))
                ,
                CompareJSons.simpleTest(
                        "template Checks 110",
                        CompareJSons.jsonFormString("{ \"a\": [[[[[\"negro\"]]]]], \"b\": 31.4}"),
                        CompareJSons.jsonFormString("{ \"a\": [[[[[\"negro\"]]]]], \"b\": 4.13}"),
                        r -> Assertions.assertTrue(r.isSuccessful(), () -> "" + r.messages())).
                        inclusions(p -> p.add("**.[*]"))


                ,
                CompareJSons.delimer()).
                lock().dynamicTests(CompareJSons::testName, JSonDirectComparisonPathTemplateCheck::templateChecks,
                        DynamicTest::dynamicTest, CompareJSons::used);
    }

    private static PathCluster cluster(String data) {
        return JSonDirectComparator.parseCluster(data);
    }

    private static Executable templateChecks(CompareJSons param) {
        return () -> {
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
        };
    }

}
