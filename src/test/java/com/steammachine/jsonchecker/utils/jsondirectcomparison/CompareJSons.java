package com.steammachine.jsonchecker.utils.jsondirectcomparison;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.steammachine.jsonchecker.defaults.MonkeyPathRepresentation;
import com.steammachine.jsonchecker.types.NodeCheckResult;
import com.steammachine.jsonchecker.types.PathRepresentation;
import com.steammachine.jsonchecker.utils.JSONParamsBuilder;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import static com.steammachine.common.utils.ResourceUtils.loadResourceAsString;
import static com.steammachine.common.utils.commonutils.CommonUtils.suppress;

/**
 * Created 02/02/17 10:44
 *
 * @author Vladimir Bogodukhov
 **/

/**
 * Вспомогательный класс параметр для проверки метода FlatCompare
 */
class CompareJSons {

    public interface Pattern {

        /**
         * Установить значение параметров равное null
         *
         * @return этот объек
         */
        Pattern drop();

        /**
         * Добавить n параметров
         *
         * @param strings - значения
         * @return
         */
        Pattern add(String... strings);

        Pattern clear();
    }

    private static class PatternI implements Pattern {
        private List<String> list = new ArrayList<>();

        private PatternI() {
        }

        public static PatternI of() {
            return new PatternI();
        }

        public PatternI drop() {
            list = null;
            return this;
        }

        public PatternI add(String... strings) {
            if (list == null) {
                list = new ArrayList<>();
            }
            list.addAll(Arrays.asList(strings));
            return this;
        }

        public PatternI clear() {
            if (list != null) {
                list.clear();
            }
            return this;
        }

        public List<String> list() {
            return list;
        }
    }


    private static final Charset CHARSET = Charset.forName("utf-8");
    private static final CompareJSons UNUSED = new CompareJSons(null, null, null,
            null, null, false);

    private final boolean used;
    private final String testName;
    private final JSONObject jsonObject;
    private final JSONObject jsonObject2;
    private final Class<? extends Throwable> expectedError;
    private final Consumer<NodeCheckResult> resultHandler;
    private PatternI inclusions = PatternI.of();
    private PatternI exclusions = PatternI.of();
    private JSONParamsBuilder params = JSONParamsBuilder.of();
    private final PathRepresentation pathRepresentation = MonkeyPathRepresentation.REPRESENTATION;

    public String testName() {
        return testName;
    }

    public JSONObject jsonObject() {
        return jsonObject;
    }

    public JSONObject jsonObject2() {
        return jsonObject2;
    }

    public Class<? extends Throwable> expectedError() {
        return expectedError;
    }

    public Consumer<NodeCheckResult> resultHandler() {
        return resultHandler;
    }


    public List<String> inclusions() {
        return inclusions.list();
    }


    public List<String> exclusions() {
        return exclusions.list();
    }

    public PathRepresentation pathRepresentation() {
        return pathRepresentation;
    }

    public boolean used() {
        return used;
    }

    private CompareJSons(
            String testName,
            JSONObject jsonObject,
            JSONObject jsonObject2,
            Class<? extends Throwable> expectedError,
            Consumer<NodeCheckResult> resultHandler,
            boolean used) {
        this.testName = testName;
        this.jsonObject = jsonObject;
        this.jsonObject2 = jsonObject2;
        this.used = used;
        this.expectedError = expectedError;
        this.resultHandler = resultHandler;
    }

    public CompareJSons ignore() {
        return UNUSED;
    }

    public CompareJSons exclusions(Consumer<Pattern> patternConsumer) {
        patternConsumer.accept(exclusions);
        return this;
    }

    public CompareJSons inclusions(Consumer<Pattern> patternConsumer) {
        patternConsumer.accept(inclusions);
        return this;
    }

    public CompareJSons useParam(Consumer<JSONParamsBuilder> paramsConsumer) {
        paramsConsumer.accept(params);
        return this;
    }

    public JSONParamsBuilder params() {
        return params;
    }

    public static CompareJSons errorTest(
            String testName,
            JSONObject slot,
            JSONObject slot2,
            Class<? extends Throwable> expectedError) {
        return new CompareJSons(testName, slot, slot2, expectedError, null, true);

    }

    public static CompareJSons errorTest(
            JSONObject slot,
            JSONObject slot2,
            Class<? extends Throwable> expectedError) {
        return errorTest(null, slot, slot2, expectedError);
    }


    public static CompareJSons simpleTest(
            String testName,
            JSONObject slot,
            JSONObject slot2,
            Consumer<NodeCheckResult> resultHandler) {
        return new CompareJSons(testName, slot, slot2, null, resultHandler, true);
    }

    public static CompareJSons simpleTest(
            JSONObject slot,
            JSONObject slot2,
            Consumer<NodeCheckResult> resultHandler) {
        return simpleTest(null, slot, slot2, resultHandler);
    }

    public static CompareJSons delimer(String ... data) {
        return UNUSED;
    }

    public static JSONObject jsonFormString(String data) {
        Objects.requireNonNull(data);
        return suppress(() -> (JSONObject) new JSONParser().parse(data));

    }

    public static JSONObject jsonFormPath(Class clazz, String path) {
        Objects.requireNonNull(path);
        return suppress(() -> (JSONObject) new JSONParser().parse(
                loadResourceAsString(clazz, path, CHARSET)));
    }
}

