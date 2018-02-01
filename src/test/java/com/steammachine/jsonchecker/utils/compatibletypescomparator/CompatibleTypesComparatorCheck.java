package com.steammachine.jsonchecker.utils.compatibletypescomparator;

import com.steammachine.org.junit5.extensions.dynamictests.dynamictestparam.DynamicTestParams;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.Executable;
import com.steammachine.jsonchecker.utils.compatibletypescomparator.ver2.CompatibleTypesComparatorV2;


import java.util.function.BooleanSupplier;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.steammachine.jsonchecker.utils.compatibletypescomparator.CompatibleTypesParam.delimetr;
import static com.steammachine.jsonchecker.utils.compatibletypescomparator.CompatibleTypesParam.test;

/*
*/

/**
 * Created by Vladimir Bogoduhov on 01.12.2017.
 *
 * @author Vladimir Bogoduhov
 */
class CompatibleTypesComparatorCheck {

    private static final BooleanSupplier VERSION_CHECKS =
            () -> CompatibleTypesComparator.version() >= CompatibleTypesComparatorV2.version();

    @SuppressWarnings("UnnecessaryBoxing")
    @TestFactory
    Stream<DynamicTest> compatibleEquals() {
        return DynamicTestParams.of(
                delimetr("------------------- Strings --------------"),
                test("compare strings 10", null, null, true),
                test("compare strings 20", "", null, false),
                test("compare strings 30", null, "", false),
                test("compare strings 40", "", "", true),
                test("compare strings 50", "1245", "1245", true),
                test("compare strings 60", "1245", "1245", true),
                delimetr(),
                delimetr("------------------- Numerals byte to------------------- "),
                delimetr(),
                test("compare numerals byte to 10", (byte) 1, (byte) 1, true),
                test("compare numerals byte to 20", (byte) 1, (short) 1, true, VERSION_CHECKS),
                test("compare numerals byte to 30", (byte) 1, (int) 1, true, VERSION_CHECKS),
                test("compare numerals byte to 40", (byte) 1, (long) 1, true, VERSION_CHECKS),
                test("compare numerals byte to 50", (byte) 1, (float) 1, true, VERSION_CHECKS),
                test("compare numerals byte to 60", (byte) 1, (double) 1, true, VERSION_CHECKS),
                test("compare numerals byte to 70", (byte) 1, "", false, VERSION_CHECKS),
                test("compare numerals byte to 80", (byte) 1, '1', false, VERSION_CHECKS),
                test("compare numerals byte to 90", (byte) 1, new Character('1'), false, VERSION_CHECKS),
                test("compare numerals byte to 100", (byte) 1, false, false, VERSION_CHECKS),
                test("compare numerals byte to 110", (byte) 1, false, false, VERSION_CHECKS),
                test("compare numerals byte to 112", Byte.valueOf("1"), (byte) 1, true),
                test("compare numerals byte to 120", Byte.valueOf("1"), (short) 1, true, VERSION_CHECKS),
                test("compare numerals byte to 130", Byte.valueOf("1"), (int) 1, true, VERSION_CHECKS),
                test("compare numerals byte to 140", Byte.valueOf("1"), (long) 1, true, VERSION_CHECKS),
                test("compare numerals byte to 150", Byte.valueOf("1"), (float) 1, true, VERSION_CHECKS),
                test("compare numerals byte to 160", Byte.valueOf("1"), (double) 1, true, VERSION_CHECKS),
                test("compare numerals byte to 170", Byte.valueOf("1"), "", false, VERSION_CHECKS),
                test("compare numerals byte to 180", Byte.valueOf("1"), '1', false, VERSION_CHECKS),
                test("compare numerals byte to 190", Byte.valueOf("1"), new Character('1'), false, VERSION_CHECKS),
                test("compare numerals byte to 200", Byte.valueOf("1"), false, false, VERSION_CHECKS),
                test("compare numerals byte to 210", Byte.valueOf("1"), false, false, VERSION_CHECKS),
                delimetr(),
                delimetr("------------------- Numerals short to------------------- "),
                delimetr(),
                test("compare numerals short to 10", (short) 1, (byte) 1, true, VERSION_CHECKS),
                test("compare numerals short to 20", (short) 1, (short) 1, true, VERSION_CHECKS),
                test("compare numerals short to 30", (short) 1, (int) 1, true, VERSION_CHECKS),
                test("compare numerals short to 40", (short) 1, (long) 1, true, VERSION_CHECKS),
                test("compare numerals short to 50", (short) 1, (float) 1, true, VERSION_CHECKS),
                test("compare numerals short to 60", (short) 1, (double) 1, true, VERSION_CHECKS),
                test("compare numerals short to 70", (short) 1, "", false, VERSION_CHECKS),
                test("compare numerals short to 80", (short) 1, '1', false, VERSION_CHECKS),
                test("compare numerals short to 90", (short) 1, new Character('1'), false, VERSION_CHECKS),
                test("compare numerals short to 100", (short) 1, Boolean.FALSE, false, VERSION_CHECKS),
                test("compare numerals short to 110", Short.valueOf("1"), (byte) 1, true, VERSION_CHECKS),
                test("compare numerals short to 120", Short.valueOf("1"), (short) 1, true, VERSION_CHECKS),
                test("compare numerals short to 130", Short.valueOf("1"), (int) 1, true, VERSION_CHECKS),
                test("compare numerals short to 140", Short.valueOf("1"), (long) 1, true, VERSION_CHECKS),
                test("compare numerals short to 150", Short.valueOf("1"), (float) 1, true, VERSION_CHECKS),
                test("compare numerals short to 160", Short.valueOf("1"), (double) 1, true, VERSION_CHECKS),
                test("compare numerals short to 170", Short.valueOf("1"), "", false, VERSION_CHECKS),
                test("compare numerals short to 180", Short.valueOf("1"), '1', false, VERSION_CHECKS),
                test("compare numerals short to 190", Short.valueOf("1"), new Character('1'), false, VERSION_CHECKS),
                test("compare numerals short to 200", Short.valueOf("1"), Boolean.FALSE, false, VERSION_CHECKS),
                delimetr(),
                delimetr("------------------- Numerals int to------------------- "),
                delimetr(),
                test("compare numerals int to 10", (int) 1, (byte) 1, true, VERSION_CHECKS),
                test("compare numerals int to 20", (int) 1, (short) 1, true, VERSION_CHECKS),
                test("compare numerals int to 30", (int) 1, (int) 1, true),
                test("compare numerals int to 40", (int) 1, (long) 1, true, VERSION_CHECKS),
                test("compare numerals int to 50", (int) 1, (float) 1, true, VERSION_CHECKS),
                test("compare numerals int to 60", (int) 1, (double) 1, true, VERSION_CHECKS),
                test("compare numerals int to 70", (int) 1, "", false, VERSION_CHECKS),
                test("compare numerals int to 80", (int) 1, '1', false, VERSION_CHECKS),
                test("compare numerals int to 90", (int) 1, new Character('1'), false, VERSION_CHECKS),
                test("compare numerals int to 100", (int) 1, Boolean.FALSE, false, VERSION_CHECKS),
                test("compare numerals int to 110", Integer.valueOf(1), (byte) 1, true, VERSION_CHECKS),
                test("compare numerals int to 120", Integer.valueOf(1), (short) 1, true, VERSION_CHECKS),
                test("compare numerals int to 130", Integer.valueOf(1), (int) 1, true),
                test("compare numerals int to 140", Integer.valueOf(1), (long) 1, true, VERSION_CHECKS),
                test("compare numerals int to 150", Integer.valueOf(1), (float) 1, true, VERSION_CHECKS),
                test("compare numerals int to 160", Integer.valueOf(1), (double) 1, true, VERSION_CHECKS),
                test("compare numerals int to 170", Integer.valueOf(1), "", false, VERSION_CHECKS),
                test("compare numerals int to 180", Integer.valueOf(1), '1', false, VERSION_CHECKS),
                test("compare numerals int to 190", Integer.valueOf(1), new Character('1'), false, VERSION_CHECKS),
                test("compare numerals int to 200", Integer.valueOf(1), Boolean.FALSE, false, VERSION_CHECKS),
                delimetr(),
                delimetr("------------------- Numerals long to------------------- "),
                delimetr(),
                test("compare numerals long to 10", (long) 1, (byte) 1, true, VERSION_CHECKS),
                test("compare numerals long to 20", (long) 1, (short) 1, true, VERSION_CHECKS),
                test("compare numerals long to 30", (long) 1, (int) 1, true),
                test("compare numerals long to 40", (long) 1, (long) 1, true),
                test("compare numerals long to 50", (long) 1, (float) 1, true, VERSION_CHECKS),
                test("compare numerals long to 60", (long) 1, (double) 1, true, VERSION_CHECKS),
                test("compare numerals long to 70", (long) 1, "", false, VERSION_CHECKS),
                test("compare numerals long to 80", (long) 1, '1', false, VERSION_CHECKS),
                test("compare numerals long to 90", (long) 1, new Character('1'), false, VERSION_CHECKS),
                test("compare numerals long to 100", (long) 1, Boolean.FALSE, false, VERSION_CHECKS),
                test("compare numerals long to 110", Long.valueOf(1), (byte) 1, true, VERSION_CHECKS),
                test("compare numerals long to 120", Long.valueOf(1), (short) 1, true, VERSION_CHECKS),
                test("compare numerals long to 130", Long.valueOf(1), (int) 1, true),
                test("compare numerals long to 140", Long.valueOf(1), (long) 1, true),
                test("compare numerals long to 150", Long.valueOf(1), (float) 1, true, VERSION_CHECKS),
                test("compare numerals long to 160", Long.valueOf(1), (double) 1, true, VERSION_CHECKS),
                test("compare numerals long to 170", Long.valueOf(1), "", false, VERSION_CHECKS),
                test("compare numerals long to 180", Long.valueOf(1), '1', false, VERSION_CHECKS),
                test("compare numerals long to 190", Long.valueOf(1), new Character('1'), false, VERSION_CHECKS),
                test("compare numerals long to 200", Long.valueOf(1), Boolean.FALSE, false, VERSION_CHECKS),
                delimetr(),
                delimetr("------------------- float ------------------- "),
                delimetr(),
                test("compare numerals float to 10", (float) 1, (byte) 1, true, VERSION_CHECKS),
                test("compare numerals float to 20", (float) 1, (short) 1, true, VERSION_CHECKS),
                test("compare numerals float to 30", (float) 1, (int) 1, true),
                test("compare numerals float to 40", (float) 1, (long) 1, true),
                test("compare numerals float to 50", (float) 1, (float) 1, true, VERSION_CHECKS),
                test("compare numerals float to 60", (float) 1, (double) 1, true, VERSION_CHECKS),
                test("compare numerals float to 70", (float) 1, "", false, VERSION_CHECKS),
                test("compare numerals float to 80", (float) 1, '1', false, VERSION_CHECKS),
                test("compare numerals float to 90", (float) 1, new Character('1'), false, VERSION_CHECKS),
                test("compare numerals float to 100", (float) 1, Boolean.FALSE, false, VERSION_CHECKS),
                test("compare numerals float to 110", Float.valueOf(1), (byte) 1, true, VERSION_CHECKS),
                test("compare numerals float to 120", Float.valueOf(1), (short) 1, true, VERSION_CHECKS),
                test("compare numerals float to 130", Float.valueOf(1), (int) 1, true),
                test("compare numerals float to 140", Float.valueOf(1), (long) 1, true),
                test("compare numerals float to 150", Float.valueOf(1), (float) 1, true, VERSION_CHECKS),
                test("compare numerals float to 160", Float.valueOf(1), (double) 1, true, VERSION_CHECKS),
                test("compare numerals float to 170", Float.valueOf(1), "", false, VERSION_CHECKS),
                test("compare numerals float to 180", Float.valueOf(1), '1', false, VERSION_CHECKS),
                test("compare numerals float to 190", Float.valueOf(1), new Character('1'), false, VERSION_CHECKS),
                test("compare numerals float to 200", Float.valueOf(1), Boolean.FALSE, false, VERSION_CHECKS),
                delimetr(),
                delimetr("------------------- double ------------------- "),
                delimetr(),
                test("compare numerals double to 10", (double) 1, (byte) 1, true, VERSION_CHECKS),
                test("compare numerals double to 20", (double) 1, (short) 1, true, VERSION_CHECKS),
                test("compare numerals double to 30", (double) 1, (int) 1, true),
                test("compare numerals double to 40", (double) 1, (long) 1, true),
                test("compare numerals double to 50", (double) 1, (float) 1, true, VERSION_CHECKS),
                test("compare numerals double to 60", (double) 1, (double) 1, true, VERSION_CHECKS),
                test("compare numerals double to 70", (double) 1, "", false, VERSION_CHECKS),
                test("compare numerals double to 80", (double) 1, '1', false, VERSION_CHECKS),
                test("compare numerals double to 90", (double) 1, new Character('1'), false, VERSION_CHECKS),
                test("compare numerals double to 100", (double) 1, Boolean.FALSE, false, VERSION_CHECKS),
                test("compare numerals double to 110", Double.valueOf(1), (byte) 1, true, VERSION_CHECKS),
                test("compare numerals double to 120", Double.valueOf(1), (short) 1, true, VERSION_CHECKS),
                test("compare numerals double to 130", Double.valueOf(1), (int) 1, true),
                test("compare numerals double to 140", Double.valueOf(1), (long) 1, true),
                test("compare numerals double to 150", Double.valueOf(1), (float) 1, true, VERSION_CHECKS),
                test("compare numerals double to 160", Double.valueOf(1), (double) 1, true, VERSION_CHECKS),
                test("compare numerals double to 170", Double.valueOf(1), "", false, VERSION_CHECKS),
                test("compare numerals double to 180", Double.valueOf(1), '1', false, VERSION_CHECKS),
                test("compare numerals double to 190", Double.valueOf(1), new Character('1'), false, VERSION_CHECKS),
                test("compare numerals double to 200", Double.valueOf(1), Boolean.FALSE, false, VERSION_CHECKS),
                delimetr(),
                delimetr("------------------- chars ------------------- "),
                delimetr(),
                test("chars 10", '1', (byte) 1, false, VERSION_CHECKS),
                test("chars 20", '1', (short) 1, false, VERSION_CHECKS),
                test("chars 30", '1', (int) 1, false),
                test("chars 40", '1', '1', true),
                test("chars 50", '1', (float) 1, false, VERSION_CHECKS),
                test("chars 60", '1', (double) 1, false, VERSION_CHECKS),
                test("chars 70", '1', "", false, VERSION_CHECKS),
                test("chars 80", '1', '1', true, VERSION_CHECKS),
                test("chars 90", '1', new Character('1'), true, VERSION_CHECKS),
                test("chars 100", '1', Boolean.FALSE, false, VERSION_CHECKS),
                delimetr(),
                delimetr("------------------- boolean ------------------- "),
                delimetr(),
                test("boolean 10", false, (byte) 1, false, VERSION_CHECKS),
                test("boolean 20", false, (short) 1, false, VERSION_CHECKS),
                test("boolean 30", false, (int) 1, false),
                test("boolean 40", false, '1', false),
                test("boolean 50", false, (float) 1, false, VERSION_CHECKS),
                test("boolean 60", false, (double) 1, false, VERSION_CHECKS),
                test("boolean 70", false, "", false, VERSION_CHECKS),
                test("boolean 80", false, '1', false, VERSION_CHECKS),
                test("boolean 90", false, new Character('1'), false, VERSION_CHECKS),
                test("boolean 100", false, false, true, VERSION_CHECKS),
                test("boolean 110", true, true, true, VERSION_CHECKS),
                test("boolean 120", true, false, false, VERSION_CHECKS),
                test("boolean 130", false, true, false, VERSION_CHECKS),
                test("boolean 140", Boolean.FALSE, Boolean.FALSE, true, VERSION_CHECKS),
                test("boolean 150", Boolean.TRUE, Boolean.TRUE, true, VERSION_CHECKS),
                test("boolean 160", Boolean.TRUE, Boolean.FALSE, false, VERSION_CHECKS),
                test("boolean 170", Boolean.FALSE, Boolean.TRUE, false, VERSION_CHECKS),
                test("boolean 180", Boolean.TRUE, true, true, VERSION_CHECKS),
                test("boolean 190", Boolean.FALSE, false, true, VERSION_CHECKS),
                test("boolean 200", Boolean.FALSE, true, false, VERSION_CHECKS),
                test("boolean 210", Boolean.TRUE, false, false, VERSION_CHECKS),
                delimetr(),
                delimetr(),
                delimetr(),
                delimetr()).
                lock().dynamicTests(CompatibleTypesParam::name, CompatibleTypesComparatorCheck::compatibleEquals,
                DynamicTest::dynamicTest, CompatibleTypesParam::used);
    }

    @Tag("DebugRun")
    @TestFactory
    Stream<DynamicTest> compatibleEqualsDebug() {
        return DynamicTestParams.of(
                delimetr(),
                test("compare numerals float to 160", Float.valueOf(1), (double) 1, true, VERSION_CHECKS),
                delimetr()
        ).checkTestNamesUniquiness(false).lock().
                dynamicTests(CompatibleTypesParam::name, CompatibleTypesComparatorCheck::compatibleEquals,
                        DynamicTest::dynamicTest, CompatibleTypesParam::used);



    }

    private static Executable compatibleEquals(CompatibleTypesParam param) {
        return () -> {
            if (!param.condition().getAsBoolean()) {

            } else {
                assertEquals(param.result(),
                        CompatibleTypesComparator.compatibleEquals(param.obj1(), param.obj2()), param::dataString);

            }
        };
    }

}