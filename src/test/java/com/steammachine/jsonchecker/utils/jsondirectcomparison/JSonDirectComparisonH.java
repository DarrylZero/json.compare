package com.steammachine.jsonchecker.utils.jsondirectcomparison;

import com.steammachine.common.lazyeval.LazyEval;
import com.steammachine.common.utils.metodsutils.MethodCaller;
import com.steammachine.common.utils.metodsutils.MethodUtils;
import com.steammachine.jsonchecker.impl.flatter2.PathCluster;
import com.steammachine.jsonchecker.utils.JSonDirectComparator;
import org.json.simple.JSONObject;

import java.util.List;

import static com.steammachine.common.utils.metodsutils.MethodUtils.findMethod;

/**
 * 30.12.2017 10:21:46
 *
 * @author Vladimir Bogodukhov
 **/
class JSonDirectComparisonH {
    public static final Class CLAZZ = JSonDirectComparator.class;

    private static final LazyEval<MethodCaller> IS_FLAT_VALUE = LazyEval.eval(() ->
            new MethodUtils.BaseMethodCaller(findMethod(MethodUtils.Level.PRIVATE, CLAZZ,
                    "isFlatValue", boolean.class, Object.class)));

    private static final LazyEval<MethodCaller> FLATTEN_JSON_OBJECT = LazyEval.eval(() ->
            new MethodUtils.BaseMethodCaller(findMethod(MethodUtils.Level.PRIVATE, CLAZZ,
                    "flattenJSonObject", List.class, JSONObject.class)));

    private static final LazyEval<MethodCaller> IS_APPLIED = LazyEval.eval(() ->
            new MethodUtils.BaseMethodCaller(findMethod(MethodUtils.Level.PRIVATE, CLAZZ,
                    "isApplied", boolean.class, PathCluster.class, PathCluster.class)));

    private static final LazyEval<MethodCaller> CHECK_PATH_FORMAT = LazyEval.eval(() ->
            new MethodUtils.BaseMethodCaller(findMethod(MethodUtils.Level.PRIVATE, CLAZZ,
                    "checkPathFormat", null, String.class)));

    private static final LazyEval<MethodCaller> OBJECT_NAME = LazyEval.eval(() ->
            new MethodUtils.BaseMethodCaller(findMethod(MethodUtils.Level.PRIVATE, CLAZZ,
                    "objectName", String.class, JSONObject.class)));

    public static String objectName(JSONObject o) {
        return OBJECT_NAME.value().invoke(null, o);
    }


    public static boolean isApplied(PathCluster path, PathCluster template) {
        return IS_APPLIED.value().invoke(null, path, template);
    }

    public static void checkPathFormat(String string) {
        CHECK_PATH_FORMAT.value().invoke(null, string);
    }


}
