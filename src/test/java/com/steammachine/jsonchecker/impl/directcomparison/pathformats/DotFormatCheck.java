package com.steammachine.jsonchecker.impl.directcomparison.pathformats;

import com.steammachine.org.junit5.extensions.dynamictests.dynamictestparam.DynamicTestParams;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestFactory;
import com.steammachine.jsonchecker.types.Path;

import java.util.stream.Stream;

/**
 * Created by Vladimir Bogodukhov on 01.12.2017.
 *
 * @author Vladimir Bogodukhov
 */
class DotFormatCheck {

    private static final DynamicTestParams<CheckPathFormat> PARAMS = DynamicTestParams.of(
            CheckPathFormat.check("a", true, Path.builder().obj("a").get()),
            CheckPathFormat.check("a.b.c", true, Path.builder().obj("a").del().obj("b").del().obj("c").get()),
            CheckPathFormat.check("a.b.c[1]", true, Path.builder().obj("a").del().obj("b").del().obj("c").del().arr(1).get()),
            CheckPathFormat.check("ab.cs", true, Path.builder().obj("ab").del().obj("cs").get()),
            CheckPathFormat.check("a.b.c[1][2]", true, Path.builder().obj("a").del().obj("b").del().obj("c").del().arr(1).del().arr(2).get()),
            CheckPathFormat.check("*.*", true, Path.builder().ast().del().ast().get()),
            CheckPathFormat.check("***.a", true, Path.builder().ast().ast().ast().del().obj("a").get()),
            CheckPathFormat.check("**.a", true, Path.builder().ast().ast().del().obj("a").get()),
            CheckPathFormat.check("***.a[1]", true, Path.builder().ast().ast().ast().del().obj("a").del().arr(1).get()),
            CheckPathFormat.check("**.a[1]", true, Path.builder().ast().ast().del().obj("a").del().arr(1).get()),
            CheckPathFormat.check("**.a[1][1]", true, Path.builder().ast().ast().del().obj("a").del().arr(1).del().arr(1).get()),
            CheckPathFormat.check("a2.db.c[1][2]", true, Path.builder().obj("a2").del().obj("db").del().obj("c").del().arr(1).del().arr(2).get()),
            CheckPathFormat.check("**.*", true, Path.builder().ast().ast().del().ast().get()),
            CheckPathFormat.check("***.*", true, Path.builder().ast().ast().ast().del().ast().get()),
            CheckPathFormat.check("***.1", true, Path.builder().ast().ast().ast().del().obj("1").get()),
            CheckPathFormat.check("a.b.c.*", true, Path.builder().obj("a").del().obj("b").del().obj("c").del().ast().get()),
            CheckPathFormat.check("***.[1]", true, Path.builder().ast().ast().ast().del().arr(1).get()),
            CheckPathFormat.check("***.[*]", true, Path.builder().ast().ast().ast().del().anyArr().get()),
            CheckPathFormat.check("**.[*]", true, Path.builder().ast().ast().del().anyArr().get()),
            CheckPathFormat.check("***.", false),
            CheckPathFormat.check("**.", false),
            CheckPathFormat.check("a2.db.c.[1][2]", false),
            CheckPathFormat.check("***.a.[1]", false),
            CheckPathFormat.check("a2.db.c.[1].[2]", false),
            CheckPathFormat.check("*.**", true),
            CheckPathFormat.check("**.**", true),
            CheckPathFormat.check("@componentType.monkeyId", false),
            CheckPathFormat.check("a u", false),
            CheckPathFormat.check("a.b.c/[1]", false),
            CheckPathFormat.check(".a.b.c[1][2]", false),
            CheckPathFormat.check("a.b.c.[q]", false),
            CheckPathFormat.check(".a.b.c[q]", false),
            CheckPathFormat.check("a.b.c[1][s]", false),
            CheckPathFormat.check("/a/b/c", false),
            CheckPathFormat.check("***.a.b[1][1]", true, Path.builder().ast().ast().ast().del().obj("a").del().obj("b").del().arr(1).del().arr(1).get()),
            CheckPathFormat.check("a[1]", true, Path.builder().obj("a").del().arr(1).get()),
            CheckPathFormat.check("..", false),
            CheckPathFormat.check("a.", false),
            CheckPathFormat.check("c[1]c", false),
            CheckPathFormat.check("c::c", true),
            CheckPathFormat.check("c.a::c", true),
            CheckPathFormat.check("a::c.a::c", true),


            CheckPathFormat.delimer("")).lock();


    private static final DotFormat DOT_FORMAT = new DotFormat();

    @TestFactory
    Stream<DynamicTest> checkPathFormat() {
        return PARAMS.
                dynamicTests(
                        CheckPathFormat::testName,
                        cpf -> () -> Assertions.assertEquals(cpf.result(), DOT_FORMAT.checkPathFormat(cpf.data())),
                        DynamicTest::dynamicTest,
                        CheckPathFormat::used);
    }

    @TestFactory
    @Tag("DebugRun")
    Stream<DynamicTest> checkPathFormatDebug() {
        return DynamicTestParams.of(
                CheckPathFormat.delimer(" ------------------checkPathFormat is of format ------------------ "),

                CheckPathFormat.check("c::c", true),
                CheckPathFormat.check("c.a::c", true),
                CheckPathFormat.check("a::c.a::c", true),

                CheckPathFormat.delimer("")).lock().
                dynamicTests(CheckPathFormat::testName,
                        cpf -> () -> Assertions.assertEquals(cpf.result(), DOT_FORMAT.checkPathFormat(cpf.data())),
                        DynamicTest::dynamicTest,
                        CheckPathFormat::used);
    }


    @TestFactory
    Stream<DynamicTest> parsePath() {
        return PARAMS.
                dynamicTests(
                        CheckPathFormat::testName,
                        cpf -> () -> {
                            if (cpf.path() != null) {
                                Assertions.assertEquals(cpf.path(), DOT_FORMAT.parsePath(cpf.data()));
                            }
                        },
                        DynamicTest::dynamicTest,
                        CheckPathFormat::used);
    }

    @TestFactory
    @Tag("DebugRun")
    Stream<DynamicTest> parsePathDebug() {
        return DynamicTestParams.of(

                CheckPathFormat.check("***.a.b[1][1]", true, Path.builder().ast().ast().ast().del().obj("a").del().obj("b").del().arr(1).del().arr(1).get()),

                CheckPathFormat.delimer("")).lock().
                dynamicTests(CheckPathFormat::testName,
                        cpf -> () -> {
                            if (cpf.path() != null) {
                                Assertions.assertEquals(cpf.path(), DOT_FORMAT.parsePath(cpf.data()));
                            }
                        },
                        DynamicTest::dynamicTest,
                        CheckPathFormat::used);
    }


}