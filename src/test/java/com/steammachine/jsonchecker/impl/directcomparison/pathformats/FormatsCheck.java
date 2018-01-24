package com.steammachine.jsonchecker.impl.directcomparison.pathformats;

import com.steammachine.jsonchecker.types.exceptions.PathError;
import com.steammachine.org.junit5.extensions.dynamictests.dynamictestparam.DynamicTestParams;
import org.junit.jupiter.api.*;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 *
 * @author Vladimir Bogodukhov
 */
class FormatsCheck {

        /* ----------------------------------------------- checkPathFormat ---------------------------------------------- */

    private static class PathFormatParam {
        private static final PathFormatParam SKIPPED = new PathFormatParam();

        private final boolean used;
        private final String testName;
        private final String path;
        private final Class<? extends Throwable> expectedError;

        private PathFormatParam() {
            this.used = false;
            this.testName = null;
            this.path = null;
            this.expectedError = null;
        }

        private PathFormatParam(String testName, String path, Class<? extends Throwable> expectedError) {
            this.used = true;
            this.testName = testName;
            this.path = path;
            this.expectedError = expectedError;
        }

        public boolean used() {
            return used;
        }

        public static PathFormatParam delimetr(String data) {
            return SKIPPED;
        }

        public static PathFormatParam delimetr() {
            return SKIPPED;
        }


        public static PathFormatParam error(String testName, String path, Class<? extends Throwable> expectedError) {
            return new PathFormatParam(testName, path, expectedError);
        }

        public static PathFormatParam check(String testName, String path) {
            return new PathFormatParam(testName, path, null);
        }

        public PathFormatParam ignore() {
            return SKIPPED;
        }

        public PathFormatParam done() {
            return SKIPPED;
        }
    }


    @Test
    void checkFormatNameUniquiness() {
        Formats.formats().map(PathFormat::name).collect(Collectors.toMap(s -> s, s -> s));
    }

    @TestFactory
    Stream<DynamicTest> checkPathFormat() {

        return DynamicTestParams.of(
                PathFormatParam.delimetr(" --------------------------- null checks ----------------------------"),
                PathFormatParam.error("null checks 10", null, NullPointerException.class),
                PathFormatParam.delimetr(),
                PathFormatParam.delimetr(" --------------------------- valid paths -----------------------------"),
                PathFormatParam.delimetr(),
                PathFormatParam.error("Valid path 10", "a", null),
                PathFormatParam.error("Valid path 20", "a.b", null),
                PathFormatParam.error("Valid path 30", "a.b.c", null),
                PathFormatParam.error("Valid path 40", "a[1].b.c", null),
                PathFormatParam.error("Valid path 50", "a[1][1]", null),
                PathFormatParam.error("Valid path 60", "a[1][1].c", null),
                PathFormatParam.error("Valid path 70", "a[1][1].b.c", null),
                PathFormatParam.error("Valid path 80", "b[1]", null),
                PathFormatParam.error("Valid path 90", "a1[1]", null),
                PathFormatParam.error("Valid path 100", "1[1]", null),
                PathFormatParam.error("Valid path 110", "1.2.3.4.5.6", null), /* такой путь считаем правильным */
                PathFormatParam.error("Valid path 120", "a[1]", null),
                PathFormatParam.error("Valid path 130", "a[236]", null),
                PathFormatParam.error("Valid path 140", "a[TTT]", PathError.class),
                PathFormatParam.delimetr(),
                PathFormatParam.delimetr(" ------------------ valid paths (monkeyId format) --------------------"),
                PathFormatParam.delimetr(),
                PathFormatParam.error("valid paths (monkeyId format) 10", "/a", null),
                PathFormatParam.error("valid paths (monkeyId format) 20", "/a/b", null),
                PathFormatParam.error("valid paths (monkeyId format) 30", "/a/b/c", null),
                PathFormatParam.error("valid paths (monkeyId format) 40", "/a/[1]/b/c", null),
                PathFormatParam.error("valid paths (monkeyId format) 50", "/a/[1]/[1]", null),
                PathFormatParam.error("valid paths (monkeyId format) 60", "/a/[1]/[1]/c", null),
                PathFormatParam.error("valid paths (monkeyId format) 70", "/a/[1]/[1]/b/c", null),
                PathFormatParam.error("valid paths (monkeyId format) 80", "/b/[1]", null),
                PathFormatParam.error("valid paths (monkeyId format) 90", "/a1/[1]", null),
                PathFormatParam.error("valid paths (monkeyId format) 100", "/1/[1]", null),
                PathFormatParam.error("valid paths (monkeyId format) 110", "/1/2/3/4/5/6", null),  /* такой путь считаем правильным */
                PathFormatParam.error("valid paths (monkeyId format) 120", "/a/[1]", null),
                PathFormatParam.error("valid paths (monkeyId format) 130", "/a/[236]", null),
                PathFormatParam.delimetr(),
                PathFormatParam.delimetr(" --------------------------- valid paths with wildcards --------------"),
                PathFormatParam.delimetr(),
                PathFormatParam.error("valid paths with wildcards 10", "*", null),
                PathFormatParam.error("valid paths with wildcards 20", "*.b", null),
                PathFormatParam.error("valid paths with wildcards 30", "a.*.c", null),
                PathFormatParam.error("valid paths with wildcards 40", "a[*].b.c", null),
                PathFormatParam.error("valid paths with wildcards 50", "a[1][*]", null),
                PathFormatParam.error("valid paths with wildcards 60", "a[1][*].c", null),
                PathFormatParam.error("valid paths with wildcards 70", "a[*][1].b.c", null),
                PathFormatParam.error("valid paths with wildcards 80", "b[*]", null),
                PathFormatParam.error("valid paths with wildcards 90", "a1[*]", null),
                PathFormatParam.error("valid paths with wildcards 100", "1[*]", null),
                PathFormatParam.error("valid paths with wildcards 110", "1.2.*.4.5.6", null), /* такой путь считаем правильным */
                PathFormatParam.error("valid paths with wildcards 130", "a[*]", null),
                PathFormatParam.error("valid paths with wildcards 150", "a[*].*", null),
                PathFormatParam.error("valid paths with wildcards 160", "a[*].**", null),
                PathFormatParam.delimetr(),
                PathFormatParam.error("universal wildcards 10", "**.a", null),
                PathFormatParam.error("universal wildcards 20", "**.a.b.c.d", null),
                PathFormatParam.error("universal wildcards 30", "**.a.b[1]", null),
                PathFormatParam.error("universal wildcards 40", "**.a.b[*]", null),
                PathFormatParam.error("universal wildcards 50", "**.a.b.c[1]", null),
                PathFormatParam.error("universal wildcards 60", "**.a.b.c[1][2][3][4]", null),
                PathFormatParam.error("universal wildcards 70", "**.c[1]", null),
                PathFormatParam.error("universal wildcards 80", "**.[1]", null),
                PathFormatParam.delimetr(),
                PathFormatParam.error("universal wildcards §10", "**/a", null),
                PathFormatParam.error("universal wildcards 120", "**/a/b/c/d", null),
                PathFormatParam.error("universal wildcards 130", "**/a/b/[1]", null),
                PathFormatParam.error("universal wildcards 140", "**/a/b/[*]", null),
                PathFormatParam.error("universal wildcards 150", "**/a/b/c/[1]", null),
                PathFormatParam.error("universal wildcards 152", "**/a/b/c/*", null),
                PathFormatParam.error("universal wildcards 154", "**/a/b/c/**", null),
                PathFormatParam.error("universal wildcards 155", "**/a/b/c/***", PathError.class),
                PathFormatParam.error("universal wildcards 160", "**/a/b/c/[1]/[2]/[3]/[4]", null),
                PathFormatParam.error("universal wildcards 170", "**/c/[1]", null),
                PathFormatParam.error("universal wildcards 180", "**/[1]", null),
                PathFormatParam.delimetr(),
                PathFormatParam.error("universal wildcards 210", "***.a", null),
                PathFormatParam.error("universal wildcards 220", "***.a.b.c.d", null),
                PathFormatParam.error("universal wildcards 230", "***.a.b[1]", null),
                PathFormatParam.error("universal wildcards 240", "***.a.b[*]", null),
                PathFormatParam.error("universal wildcards 242", "***.a.b[*].*", null),
                PathFormatParam.error("universal wildcards 245", "***.a.b[*].**", null),
                PathFormatParam.error("universal wildcards 250", "***.a.b.c[1]", null),
                PathFormatParam.error("universal wildcards 260", "***.a.b.c[1][2][3][4]", null),
                PathFormatParam.error("universal wildcards 270", "***.c[1]", null),
                PathFormatParam.error("universal wildcards 280", "***.[1]", null),
                PathFormatParam.delimetr(),
                PathFormatParam.error("universal wildcards 310", "***/a", null),
                PathFormatParam.error("universal wildcards 320", "***/a/b/c/d", null),
                PathFormatParam.error("universal wildcards 330", "***/a/b/[1]", null),
                PathFormatParam.error("universal wildcards 340", "***/a/b/[*]", null),
                PathFormatParam.error("universal wildcards 350", "***/a/b/c/[1]", null),
                PathFormatParam.error("universal wildcards 360", "***/a/b/c/[1]/[2]/[3]/[4]", null),
                PathFormatParam.error("universal wildcards 370", "***/c/[1]", null),
                PathFormatParam.error("universal wildcards 380", "***/[1]", null),
                PathFormatParam.error("universal wildcards 390", "***/[1]/*", null),
                PathFormatParam.error("universal wildcards 400", "***/[1]/**", null),
                PathFormatParam.delimetr(),
                PathFormatParam.delimetr(" --------------------------- invalid paths ---------------------------"),
                PathFormatParam.delimetr(),
                PathFormatParam.delimetr(),
                PathFormatParam.error("invalid path 10", "..", PathError.class),
                PathFormatParam.error("invalid path 30", ".b", PathError.class),
                PathFormatParam.error("invalid path 40", "[1]", PathError.class),
                PathFormatParam.error("invalid path 50", "[]", PathError.class),
                PathFormatParam.error("invalid path 60", "d.[1]c", PathError.class),
                PathFormatParam.error("invalid path 70", "..", PathError.class),
                PathFormatParam.error("invalid path 80", "[not a number]", PathError.class),
                PathFormatParam.error("invalid path 90", "a[not a number]", PathError.class),
                PathFormatParam.error("invalid path 100", "a[**]", PathError.class),
                PathFormatParam.delimetr(),
                PathFormatParam.delimetr(),
                PathFormatParam.delimetr(" --------------------------- not implemented yet --------------------"),
                PathFormatParam.delimetr(),
                PathFormatParam.error("a.", "a.", PathError.class),
                PathFormatParam.error("c[1]c", "c[1]c", PathError.class),
                PathFormatParam.delimetr()).lock().dynamicTests(FormatsCheck::checkPathFormatTest, PathFormatParam::used);
    }


    @TestFactory
    @Tag("DebugRun")
    Stream<DynamicTest> checkPathFormatDebug() {
        return DynamicTestParams.of(
                PathFormatParam.delimetr(" ----------------------------- Debugs ------------------------"),

                PathFormatParam.error("valid paths with wildcards 160", "a[*].**", null),
                PathFormatParam.error("universal wildcards 245", "***.a.b[*].**", null),

                PathFormatParam.delimetr()).lock().dynamicTests(FormatsCheck::checkPathFormatTest, PathFormatParam::used);

    }

    @TestFactory
    Stream<DynamicTest> checkPathFormatUniquiness() {
        return DynamicTestParams.of(
                PathFormatParam.check("Valid path 10", "a"),
                PathFormatParam.check("Valid path 20", "a.b"),
                PathFormatParam.check("Valid path 30", "a.b.c"),
                PathFormatParam.check("Valid path 40", "a[1].b.c"),
                PathFormatParam.check("Valid path 50", "a[1][1]"),
                PathFormatParam.check("Valid path 60", "a[1][1].c"),
                PathFormatParam.check("Valid path 70", "a[1][1].b.c"),
                PathFormatParam.check("Valid path 80", "b[1]"),
                PathFormatParam.check("Valid path 90", "a1[1]"),
                PathFormatParam.check("Valid path 100", "1[1]"),
                PathFormatParam.check("Valid path 110", "1.2.3.4.5.6"), /* такой путь считаем правильным */
                PathFormatParam.check("Valid path 120", "a[1]"),
                PathFormatParam.check("Valid path 130", "a[236]"),
                PathFormatParam.check("Valid path 140", "a[TTT]"),
                PathFormatParam.check("valid paths (monkeyId format) 10", "/a"),
                PathFormatParam.check("valid paths (monkeyId format) 20", "/a/b"),
                PathFormatParam.check("valid paths (monkeyId format) 30", "/a/b/c"),
                PathFormatParam.check("valid paths (monkeyId format) 40", "/a/[1]/b/c"),
                PathFormatParam.check("valid paths (monkeyId format) 50", "/a/[1]/[1]"),
                PathFormatParam.check("valid paths (monkeyId format) 60", "/a/[1]/[1]/c"),
                PathFormatParam.check("valid paths (monkeyId format) 70", "/a/[1]/[1]/b/c"),
                PathFormatParam.check("valid paths (monkeyId format) 80", "/b/[1]"),
                PathFormatParam.check("valid paths (monkeyId format) 90", "/a1/[1]"),
                PathFormatParam.check("valid paths (monkeyId format) 100", "/1/[1]"),
                PathFormatParam.check("valid paths (monkeyId format) 110", "/1/2/3/4/5/6"),  /* такой путь считаем правильным */
                PathFormatParam.check("valid paths (monkeyId format) 120", "/a/[1]"),
                PathFormatParam.check("valid paths (monkeyId format) 130", "/a/[236]"),
                PathFormatParam.check("valid paths with wildcards 10", "*"),
                PathFormatParam.check("valid paths with wildcards 20", "*.b"),
                PathFormatParam.check("valid paths with wildcards 30", "a.*.c"),
                PathFormatParam.check("valid paths with wildcards 40", "a[*].b.c"),
                PathFormatParam.check("valid paths with wildcards 50", "a[1][*]"),
                PathFormatParam.check("valid paths with wildcards 60", "a[1][*].c"),
                PathFormatParam.check("valid paths with wildcards 70", "a[*][1].b.c"),
                PathFormatParam.check("valid paths with wildcards 80", "b[*]"),
                PathFormatParam.check("valid paths with wildcards 90", "a1[*]"),
                PathFormatParam.check("valid paths with wildcards 100", "1[*]"),
                PathFormatParam.check("valid paths with wildcards 110", "1.2.*.4.5.6"), /* такой путь считаем правильным */
                PathFormatParam.check("valid paths with wildcards 130", "a[*]"),
                PathFormatParam.check("universal wildcards 10", "**.a"),
                PathFormatParam.check("universal wildcards 20", "**.a.b.c.d"),
                PathFormatParam.check("universal wildcards 30", "**.a.b[1]"),
                PathFormatParam.check("universal wildcards 40", "**.a.b[*]"),
                PathFormatParam.check("universal wildcards 50", "**.a.b.c[1]"),
                PathFormatParam.check("universal wildcards 60", "**.a.b.c[1][2][3][4]"),
                PathFormatParam.check("universal wildcards 70", "**.c[1]"),
                PathFormatParam.check("universal wildcards 80", "**.[1]"),
                PathFormatParam.check("universal wildcards §10", "**/a"),
                PathFormatParam.check("universal wildcards 120", "**/a/b/c/d"),
                PathFormatParam.check("universal wildcards 130", "**/a/b/[1]"),
                PathFormatParam.check("universal wildcards 140", "**/a/b/[*]"),
                PathFormatParam.check("universal wildcards 150", "**/a/b/c/[1]"),
                PathFormatParam.check("universal wildcards 160", "**/a/b/c/[1]/[2]/[3]/[4]"),
                PathFormatParam.check("universal wildcards 170", "**/c/[1]"),
                PathFormatParam.check("universal wildcards 180", "**/[1]"),
                PathFormatParam.check("universal wildcards 210", "***.a"),
                PathFormatParam.check("universal wildcards 220", "***.a.b.c.d"),
                PathFormatParam.check("universal wildcards 230", "***.a.b[1]"),
                PathFormatParam.check("universal wildcards 240", "***.a.b[*]"),
                PathFormatParam.check("universal wildcards 250", "***.a.b.c[1]"),
                PathFormatParam.check("universal wildcards 260", "***.a.b.c[1][2][3][4]"),
                PathFormatParam.check("universal wildcards 270", "***.c[1]"),
                PathFormatParam.check("universal wildcards 280", "***.[1]"),
                PathFormatParam.check("universal wildcards 310", "***/a"),
                PathFormatParam.check("universal wildcards 320", "***/a/b/c/d"),
                PathFormatParam.check("universal wildcards 330", "***/a/b/[1]"),
                PathFormatParam.check("universal wildcards 340", "***/a/b/[*]"),
                PathFormatParam.check("universal wildcards 350", "***/a/b/c/[1]"),
                PathFormatParam.check("universal wildcards 360", "***/a/b/c/[1]/[2]/[3]/[4]"),
                PathFormatParam.check("universal wildcards 370", "***/c/[1]"),
                PathFormatParam.check("universal wildcards 380", "***/[1]"),
                PathFormatParam.check("universal wildcards 400", "a.*"),
                PathFormatParam.check("universal wildcards 410", "a.**"),
                PathFormatParam.check("universal wildcards 420", "a[*]"),
                PathFormatParam.check("universal wildcards 422", "a[1].*"),
                PathFormatParam.check("universal wildcards 423", "a[*].*"),
                PathFormatParam.check("universal wildcards 430", "a[1].**"),
                PathFormatParam.check("universal wildcards 440", "a[*].**"),

                PathFormatParam.delimetr("not implemented yet"),
                PathFormatParam.check("not implemented yet 10", "a."),
                PathFormatParam.check("not implemented yet 20", "c[1]c"),

                PathFormatParam.delimetr()).lock().dynamicTests(FormatsCheck::formatUniquiness, PathFormatParam::used);
    }


   /* --------------------------------------------------  privates -----------------------------------------------  */

    private static DynamicTest checkPathFormatTest(PathFormatParam param) {
        String displayName = param.testName != null ? param.testName : "" + param;
        return DynamicTest.dynamicTest(displayName, () -> {
            try {
                Formats.checkPathFormat(param.path); /* <-- проверяемый метод */
            } catch (Throwable t) {
                if (param.expectedError == null) {
                    Assertions.assertEquals(null, t.getClass());
                } else if (!param.expectedError.isAssignableFrom(t.getClass())) {
                    Assertions.assertEquals(param.expectedError, t.getClass());
                }
                return;
            }
            if (param.expectedError != null) {
                Assertions.assertEquals(param.expectedError, null);
            }

        });
    }

    /**
     * проверить что строка отвечает только одному формату
     *
     * @param param -
     * @return -
     */
    private static DynamicTest formatUniquiness(PathFormatParam param) {
        String displayName = param.testName != null ? param.testName : "" + param;
        return DynamicTest.dynamicTest(displayName, () -> {
            Set<String> set = Formats.formats().map(f -> f.checkPathFormat(param.path) ? f.name() : null).filter(Objects::nonNull).
                    collect(Collectors.toSet());
            if (set.size() > 1) {
                throw new IllegalStateException();
            }
        });
    }


}