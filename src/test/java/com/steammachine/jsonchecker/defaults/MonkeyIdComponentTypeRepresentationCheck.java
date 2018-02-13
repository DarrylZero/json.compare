package com.steammachine.jsonchecker.defaults;

import com.steammachine.jsonchecker.utils.jsondirectcomparison.SimpleTest;
import com.steammachine.org.junit5.extensions.dynamictests.dynamictestparam.DynamicTestParams;
import org.junit.jupiter.api.*;
import com.steammachine.jsonchecker.impl.directcomparison.pathformats.PathFormat;
import com.steammachine.jsonchecker.impl.directcomparison.pathformats.DotFormat;
import com.steammachine.jsonchecker.impl.directcomparison.pathformats.MonkeyIdCompTypeFormat;
import com.steammachine.jsonchecker.impl.flatter2.Id;
import com.steammachine.jsonchecker.types.Path;
import com.steammachine.jsonchecker.types.PathRepresentation;

import java.util.stream.Stream;

/**
 *
 * @author Vladimir Bogodukhov
 */
class MonkeyIdComponentTypeRepresentationCheck {


    private static final PathRepresentation REP = MonkeyPathRepresentation.REPRESENTATION;


    @TestFactory
    Stream<DynamicTest> pathAsString() {

        return DynamicTestParams.of(
                SimpleTest.delimer(" ================================ simple tests ================================= "),
                SimpleTest.simpleTest("simple /a", () -> Assertions.assertEquals("/a",
                        REP.path(Path.of().del().id(Id.obj("a")).path())
                )),

                SimpleTest.simpleTest("simple /a/b/c/ ", () -> Assertions.assertEquals("/a/b/c/",
                        REP.path(Path.of().del().id(Id.obj("a")).del().id(Id.obj("b")).del().id(Id.obj("c")).del())
                )),

                SimpleTest.simpleTest("simple a/b/c/ ", () -> Assertions.assertEquals("a/b/c/",
                        REP.path(Path.of().id(Id.obj("a")).del().id(Id.obj("b")).del().id(Id.obj("c")).del())
                )),

                SimpleTest.simpleTest("simple a/b/c ", () -> Assertions.assertEquals("a/b/c",
                        REP.path(Path.of().id(Id.obj("a")).del().id(Id.obj("b")).del().id(Id.obj("c")).path())
                )),

                SimpleTest.simpleTest("simple /a/[1]/c/ ", () -> Assertions.assertEquals("/a/[1]/c/",
                        REP.path(Path.of().del().id(Id.obj("a")).del().id(Id.arr(1)).del().id(Id.obj("c")).del())
                )),

                SimpleTest.simpleTest("simple a/[1]/c/ ", () -> Assertions.assertEquals("a/[1]/c/",
                        REP.path(Path.of().id(Id.obj("a")).del().id(Id.arr(1)).del().id(Id.obj("c")).del())
                )),

                SimpleTest.simpleTest("simple a/[1]/c ", () -> Assertions.assertEquals("a/[1]/c",
                        REP.path(Path.of().id(Id.obj("a")).del().id(Id.arr(1)).del().id(Id.obj("c")).path())
                )),

                SimpleTest.simpleTest("simple /a/[1]/[2]/ ", () -> Assertions.assertEquals("/a/[1]/[2]/",
                        REP.path(Path.of().del().id(Id.obj("a")).del().id(Id.arr(1)).del().id(Id.arr(2)).del())
                )),

                SimpleTest.simpleTest("simple a/[1]/[2]/ ", () -> Assertions.assertEquals("a/[1]/[2]/",
                        REP.path(Path.of().id(Id.obj("a")).del().id(Id.arr(1)).del().id(Id.arr(2)).del())
                )),

                SimpleTest.simpleTest("simple a/[1]/[2]", () -> Assertions.assertEquals("a/[1]/[2]",
                        REP.path(Path.of().id(Id.obj("a")).del().id(Id.arr(1)).del().id(Id.arr(2)).path())
                )),

                SimpleTest.simpleTest("simple a/[1]/[2]/c", () -> Assertions.assertEquals("a/[1]/[2]/c",
                        REP.path(Path.of().id(Id.obj("a")).del().id(Id.arr(1)).del().id(Id.arr(2)).del().id(Id.obj("c")).path())
                )),

                SimpleTest.simpleTest("simple /a/@b/c/ ", () -> Assertions.assertEquals("/a/@b/c/",
                        REP.path(Path.of().del().id(Id.obj("a")).del().id(Id.obj("@b")).del().id(Id.obj("c")).del())
                )),

                SimpleTest.simpleTest("simple a/@b/c/ ", () -> Assertions.assertEquals("a/@b/c/",
                        REP.path(Path.of().id(Id.obj("a")).del().id(Id.obj("@b")).del().id(Id.obj("c")).del())
                )),

                SimpleTest.simpleTest("simple a/@b/c ", () -> Assertions.assertEquals("a/@b/c",
                        REP.path(Path.of().id(Id.obj("a")).del().id(Id.obj("@b")).del().id(Id.obj("c")).path())
                )),


                SimpleTest.delimer()
        ).lock().dynamicTests(SimpleTest::testName, SimpleTest::executable, DynamicTest::dynamicTest, SimpleTest::used);

    }

    @TestFactory
    @Tag("DebugRun")
    Stream<DynamicTest> debug() {

        return DynamicTestParams.of(
                SimpleTest.delimer(" ================================ simple tests ================================= "),

                SimpleTest.simpleTest("simple a/@b/c ", () -> Assertions.assertEquals("a/@b/c",
                        REP.path(Path.of().id(Id.obj("a")).del().id(Id.obj("@b")).del().id(Id.obj("c")).path())
                )),

                SimpleTest.delimer()
        ).lock().dynamicTests(SimpleTest::testName, SimpleTest::executable, DynamicTest::dynamicTest, SimpleTest::used);
    }


    @Test
    void repTest() {
        PathFormat format = new DotFormat();
        Assertions.assertAll(
                () -> Assertions.assertEquals("a/[1]/[2]", REP.path(format.parsePath("a[1][2]"))),
                () -> Assertions.assertEquals("a/[1]", REP.path(format.parsePath("a[1]"))),
                () -> Assertions.assertEquals("a/b/c", REP.path(format.parsePath("a.b.c"))),
                () -> Assertions.assertEquals("***/a/b/c", REP.path(format.parsePath("***.a.b.c"))),
                () -> Assertions.assertEquals("**/a/b/c", REP.path(format.parsePath("**.a.b.c"))),
                () -> Assertions.assertEquals("***/a/b/[1]/c", REP.path(format.parsePath("***.a.b[1].c"))),
                () -> Assertions.assertEquals("***/a/b/[1]/[1]", REP.path(format.parsePath("***.a.b[1][1]")))
        );
    }


    @Test
    void repTest2() {
        PathFormat format = new MonkeyIdCompTypeFormat();
        Assertions.assertAll(
                () -> Assertions.assertEquals("/a/[1]/[2]", REP.path(format.parsePath("/a/[1]/[2]"))),
                () -> Assertions.assertEquals("/a/b/c", REP.path(format.parsePath("/a/b/c"))),
                () -> Assertions.assertEquals("/a/[1]", REP.path(format.parsePath("/a/[1]"))),
                () -> Assertions.assertEquals("***/a/b/c", REP.path(format.parsePath("***/a/b/c"))),
                () -> Assertions.assertEquals("**/a/b/c", REP.path(format.parsePath("**/a/b/c"))),
                () -> Assertions.assertEquals("***/a/b/[1]/c", REP.path(format.parsePath("***/a/b/[1]/c"))),
                () -> Assertions.assertEquals("***/a/b/[1]/[1]", REP.path(format.parsePath("***/a/b/[1]/[1]"))),
                () -> Assertions.assertEquals("***/a/b/[1]/[1]", REP.path(format.parsePath("***/a/b/[1]/[1]")))
        );
    }

    @Test
    void repTest3() {
        Assertions.assertAll(
                () -> Assertions.assertEquals("/@a1/",
                        REP.path(Path.of().del().addId(Id.obj("@a1")).addId(Id.obj("@a2")).del())),
                () -> Assertions.assertEquals("/a1/",
                        REP.path(Path.of().del().addId(Id.obj("a1")).addId(Id.obj("@a2")).del())),
                () -> Assertions.assertEquals("/a1/b",
                        REP.path(Path.of().del().addId(Id.obj("a1")).addId(Id.obj("@a2")).del().addId(Id.obj("b")))),
                () -> Assertions.assertEquals("/a/c/e/g/",
                        REP.path(Path.of().
                                del().addId(Id.obj("a")).addId(Id.obj("@b")).del().
                                addId(Id.obj("c")).addId(Id.obj("@d")).del().
                                addId(Id.obj("e")).addId(Id.obj("@f")).del().
                                addId(Id.obj("g")).addId(Id.obj("@h")).del()
                        )
                )
        );

    }


}