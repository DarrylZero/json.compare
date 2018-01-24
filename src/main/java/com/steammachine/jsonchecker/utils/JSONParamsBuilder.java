package com.steammachine.jsonchecker.utils;

import com.steammachine.jsonchecker.types.DirectValue;
import com.steammachine.jsonchecker.impl.directcomparison.flatterprocs.FlattersCommon;
import com.steammachine.jsonchecker.types.JSONMatchedParam;
import com.steammachine.jsonchecker.types.JSONParam;
import com.steammachine.jsonchecker.types.JSONParams;
import com.steammachine.jsonchecker.types.exceptions.ParamError;
import com.steammachine.jsonchecker.types.exceptions.ParamNotFound;
import com.steammachine.jsonchecker.types.exceptions.ParamTypeError;
import com.steammachine.jsonchecker.utils.compatibletypescomparator.CompatibleTypesComparator;
import com.steammachine.common.utils.commonutils.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

/**
 * Вспомогательный класс - билдер параметров, передаваемых в .
 * Используется для построения экземпларов {@link JSONParams}
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * Created by Vladimir Bogodukhov on 01.12.2017.
 *
 * @author Vladimir Bogodukhov
 */
public class JSONParamsBuilder {

    private static class JSONMatchedParamWrapper implements JSONParam {
        private final JSONMatchedParam matchedParam;
        private final String name;

        private JSONMatchedParamWrapper(String name, JSONMatchedParam matchedParam) {
            this.name = Objects.requireNonNull(name) ;
            this.matchedParam = Objects.requireNonNull(matchedParam);
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public String paramsRep() {
            return "JSONMatchedParam";
        }

        @Override
        public void checkValues() throws ParamTypeError, ParamError {
        }

        @Override
        public boolean matches(Object value) {
            return matchedParam.matches(value);
        }

        @Override
        public boolean matchesParam(JSONParam thatParam) {
            return false;
        }
    }

    private static class SingleParam implements JSONParam, DirectValue {
        private final Object value;
        private final String name;

        @Override
        public String name() {
            return name;
        }

        @Override
        public String paramsRep() {
            return "" + value;
        }

        public SingleParam(String name, Object value) {
            this.name = Objects.requireNonNull(name);
            this.value = value;
        }

        @Override
        public void checkValues() throws ParamTypeError {
            CommonUtils.check(() -> FlattersCommon.isFlatValue(value),
                    () -> new ParamTypeError("VALUE " + value + " has wrong type " +
                            value.getClass().getName()));
        }

        @Override
        public boolean matches(Object value) {
            return CompatibleTypesComparator.compatibleEquals(this.value, value);
        }

        @Override
        public boolean matchesParam(JSONParam thatParam) {
            return thatParam instanceof SingleParam &&
                    CompatibleTypesComparator.compatibleEquals(((SingleParam) thatParam).value, value);
        }

        @Override
        public Object value() {
            return value;
        }
    }


    private static class RegexpMatcherParam implements JSONParam {
        private final Pattern pattern;
        private final String name;
        private final String regexp;

        private RegexpMatcherParam(String name, String regexp) {
            this.name = name;
            this.regexp = Objects.requireNonNull(regexp);
            this.pattern = loadTregExp(regexp);
        }

        private RegexpMatcherParam(String name, Pattern regexp) {
            this.name = name;
            this.pattern = Objects.requireNonNull(regexp);
            this.regexp = regexp.pattern();
        }

        private static Pattern loadTregExp(String regexp) {
            try {
                return Pattern.compile(regexp);
            } catch (PatternSyntaxException e) {
                return null;
            }
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public String paramsRep() {
            return regexp;
        }

        @Override
        public void checkValues() throws ParamTypeError, ParamError {
            if (pattern == null) {
                throw new ParamError("pattern is not valid. " + regexp);
            }
        }

        @Override
        public boolean matches(Object value) {
            return value instanceof String && pattern.matcher(String.class.cast(value)).matches();
        }

        @Override
        public boolean matchesParam(JSONParam thatParam) {
            return false;
        }
    }


    private final List<JSONParam> params = new ArrayList<>();

    private JSONParamsBuilder() {
    }

    public static JSONParamsBuilder of() {
        return new JSONParamsBuilder();
    }

    public JSONParamsBuilder fromMap(Map<String, Object> value) {
        if (value != null) {
            value.entrySet().forEach(entry -> single(entry.getKey(), entry.getValue()));
        }
        return this;
    }

    /**
     *
     * Построение объекта {@link JSONParams}
     * @return новый экземпляр {@link JSONParams}
     */
    public JSONParams build() {
        params.forEach(JSONParam::checkValues);
        Map<String, JSONParam> temp = params.stream().collect(Collectors.toMap(JSONParam::name, e -> e,
                JSONParamsBuilder::alreadyDefined));

        return new JSONParams() {
            private Map<String, JSONParam> map = temp;

            @Override
            public JSONParam get(String name) {
                return map.computeIfAbsent(name, paramName -> {
                    throw new ParamNotFound("param " + paramName);
                });
            }

            @Override
            public String toString() {
                return "JSONParams(" + "map=" + map + ")";
            }
        };

    }


    /**
     * Добавление произвольного параметра
     *
     * @param param произвольный параметр  (всегда не null) - смотреть больше  {@link JSONParam}
     * @return этот билдер
     */
    public JSONParamsBuilder param(JSONParam param) {
        Objects.requireNonNull(param);
        params.add(param);
        return this;
    }

    /**
     * Добавление произвольного параметра
     *
     * @param param произвольный параметр  (всегда не null) - смотреть больше  {@link JSONMatchedParam} {@link JSONParam}
     * @return этот билдер
     */
    public JSONParamsBuilder param(String name, JSONMatchedParam param) {
        return param(new JSONMatchedParamWrapper(name, param));
    }


    /**
     * Параметр - простое сравнение с объектом
     *
     * @param name  - имя параметра
     * @param value значение
     * @return этот билдер
     */
    public JSONParamsBuilder single(String name, Object value) {
        return param(new SingleParam(name, value));
    }

    /**
     * Параметр - простое сравнение
     *
     * @param name  - имя параметра
     * @param value значение
     * @return этот билдер
     */
    public JSONParamsBuilder single(String name, boolean value) {
        return single(name, (Object) value);
    }

    /**
     * Параметр - простое сравнение
     *
     * @param name  - имя параметра
     * @param value значение
     * @return этот билдер
     */
    public JSONParamsBuilder single(String name, byte value) {
        return single(name, (Object) value);
    }

    /**
     * Параметр - простое сравнение
     *
     * @param name  - имя параметра
     * @param value значение
     * @return этот билдер
     */
    public JSONParamsBuilder single(String name, int value) {
        return single(name, (Object) value);
    }

    /**
     * Параметр - простое сравнение
     *
     * @param name  - имя параметра
     * @param value значение
     * @return этот билдер
     */
    public JSONParamsBuilder single(String name, long value) {
        return single(name, (Object) value);
    }

    /**
     * Параметр - простое сравнение
     *
     * @param name  - имя параметра
     * @param value значение
     * @return этот билдер
     */
    public JSONParamsBuilder single(String name, float value) {
        return single(name, (Object) value);
    }

    /**
     * Параметр - простое сравнение
     *
     * @param name  - имя параметра
     * @param value значение
     * @return этот билдер
     */
    public JSONParamsBuilder single(String name, double value) {
        return single(name, (Object) value);
    }


    /**
     * Создает параметр, который содержит регулярное выражение.
     * Значение сравнивается на соответствие с регулярным выражением.
     *
     * @param name   имя параметра (не null)
     * @param regexp регулярное выражение (не null)
     * @return этот билдер
     */
    public JSONParamsBuilder regexp(String name, Pattern regexp) {
        return param(new RegexpMatcherParam(name, regexp));
    }

    /**
     * Создает параметр, который содержит регулярное выражение.
     * Значение сравнивается на соответствие с регулярным выражением.
     *
     * @param name   имя параметра (не null)
     * @param regexp регулярное выражение (не null)
     * @return этот билдер
     */
    public JSONParamsBuilder regexp(String name, String regexp) {
        return param(new RegexpMatcherParam(name, regexp));
    }



/* -------------------------------------------------------- privates ------------------------------------------------ */

    private static JSONParam alreadyDefined(JSONParam param, JSONParam param2) {
        return param2;
    }

}
