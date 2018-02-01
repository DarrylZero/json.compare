package com.steammachine.jsonchecker.impl.ver1;

import com.steammachine.common.definitions.annotations.SignatureSensitive;
import com.steammachine.common.utils.metodsutils.MethodUtils;
import com.steammachine.jsonchecker.impl.ver1.res.Class1;
import com.steammachine.jsonchecker.impl.ver1.res.Class2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.steammachine.common.lazyeval.LazyEval;
import com.steammachine.common.utils.metodsutils.MethodCaller;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author Vladimir Bogodukhov
 */
class ObjectInformationCheck {

    @Test
    void loadClassInfo10() {
        Assertions.assertEquals(
                Arrays.asList(
                        "void com.steammachine.jsonchecker.impl.ver1.res.Class1.setA(java.lang.String)",
                        "void com.steammachine.jsonchecker.impl.ver1.res.Class1.setB(int)"
                ),
                asStrings(loadClassInfo(Class1.class))
        );
    }

    @Test
    void loadClassInfo20() {
        /* Здесь проверяем методы наследника */
        Assertions.assertEquals(
                Arrays.asList(
                        "void com.steammachine.jsonchecker.impl.ver1.res.Class1.setA(java.lang.String)",
                        "void com.steammachine.jsonchecker.impl.ver1.res.Class1.setB(int)",
                        "void com.steammachine.jsonchecker.impl.ver1.res.Class2.setC(boolean)"
                ),
                asStrings(loadClassInfo(Class2.class))
        );
    }


    /* ----------------------------------- privates --------------------------------------------------------------- */

    private static List<String> asStrings(Map<String, List<Method>> methods) {
        Objects.requireNonNull(methods);
        return sort(methods.entrySet().stream().map(e -> e.getValue().stream().filter(i -> true).map(Method::toString).
                collect(Collectors.joining(" "))).
                collect(Collectors.toList()));
    }

    private static <T extends Comparable> List<T> sort(List<T> list) {
        Objects.requireNonNull(list);
        List<T> result = new ArrayList<>(list);
        Collections.sort(result);
        return result;

    }

    private static final LazyEval<MethodCaller> LOAD_CLASS_INFO =
            LazyEval.eval(() -> new MethodUtils.BaseMethodCaller(
                    MethodUtils.findMethod(MethodUtils.Level.PRIVATE, ObjectInformation.class,
                            "loadClassInfo", Map.class, Class.class)));

    @SignatureSensitive
    private static Map<String, List<Method>> loadClassInfo(Class clazz) {
        return LOAD_CLASS_INFO.value().invoke(null, clazz);
    }

}