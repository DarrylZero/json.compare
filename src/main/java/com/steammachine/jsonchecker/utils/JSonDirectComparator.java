package com.steammachine.jsonchecker.utils;

import com.steammachine.common.apilevel.Api;
import com.steammachine.common.apilevel.State;
import com.steammachine.common.definitions.annotations.SignatureSensitive;
import com.steammachine.jsonchecker.defaults.AddressType;
import com.steammachine.jsonchecker.defaults.MonkeyPathRepresentation;
import com.steammachine.jsonchecker.defaults.ResultNodeCheckContext;
import com.steammachine.jsonchecker.impl.directcomparison.flatterprocs.Flatters;
import com.steammachine.jsonchecker.impl.directcomparison.pathformats.Formats;
import com.steammachine.jsonchecker.impl.directcomparison.pathformats.PathFormat;
import com.steammachine.jsonchecker.impl.flatter2.Element;
import com.steammachine.jsonchecker.impl.flatter2.Id;
import com.steammachine.jsonchecker.impl.flatter2.IdentifierType;
import com.steammachine.jsonchecker.impl.flatter2.PathCluster;
import com.steammachine.jsonchecker.types.*;
import com.steammachine.jsonchecker.types.exceptions.ParamNotFound;
import com.steammachine.jsonchecker.types.exceptions.WrongDataFormat;
import com.steammachine.jsonchecker.utils.compatibletypescomparator.ver2.CompatibleTypesComparatorV2;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.Format;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.steammachine.common.utils.commonutils.CommonUtils.check;
import static com.steammachine.common.utils.commonutils.CommonUtils.intersectCollections;

/**
 * Utility class for json data comparison
 *
 * @author Vladimir Bogodukhov
 * {@link com.steammachine.jsonchecker.utils.JSonDirectComparator}
 * com.steammachine.jsonchecker.utils.JSonDirectComparator
 **/
@Api(State.MAINTAINED)
public class JSonDirectComparator {

    private static final String START_PARAM = "${";
    private static final String END_PARAM = "}";
    private static final String VALUES_BY_PATH = "values by path";
    private static final String PARAM_NOT_FOUND = "Param %s not found";

    private static class Values {

        private final Path keyPath;
        private final PathCluster cluster1;
        private final PathCluster cluster2;
        private final Object value1;
        private final JSONParam param1;

        private final Object value2;
        private final JSONParam param2;

        public Values(
                Path keyPath,

                PathCluster cluster1,
                PathCluster cluster2,

                Object value1,
                JSONParam param1,

                Object value2,
                JSONParam param2) {

            this.keyPath = Objects.requireNonNull(keyPath);
            this.cluster1 = Objects.requireNonNull(cluster1);
            this.cluster2 = Objects.requireNonNull(cluster2);
            this.value1 = value1;
            this.param1 = param1;

            this.value2 = value2;
            this.param2 = param2;
        }

        public Path keyPath() {
            return keyPath;
        }

        public PathCluster cluster1() {
            return cluster1;
        }

        public PathCluster cluster2() {
            return cluster1;
        }

        public Object value1() {
            return value1;
        }

        public JSONParam param1() {
            return param1;
        }

        public Object value2() {
            return value2;
        }

        public JSONParam param2() {
            return param2;
        }

        public boolean valuesDefined() {
            return value1() != null && value2() != null;
        }

        public boolean oneValueNotDefined() {
            return (value1() == null) != (value2() == null);
        }

        public boolean valuesHaveSameType() {
            return (value1() == null && value2() == null) ||
                    (value1() != null && value2() != null && value1().getClass() == value2().getClass());
        }

        private Values applyParams(JSONParams params) {
            Objects.requireNonNull(params);
            if (!(value1() instanceof String) && !(value2() instanceof String)) {
                return this;
            }

            /* параметры могут быть записаны только в строке */
            return new Values(keyPath, cluster1, cluster2,
                    value(value1()), appliedParam(value1(), params), value(value2()), appliedParam(value2(), params));
        }

        private static Object value(Object value) {
            if (value == null) {
                return null;
            }
            if (!(value instanceof String)) {
                return value;
            }
            String stringVal = (String) value;
            if (!hasParam(stringVal)) {
                return value;
            }
            return null;
        }

        public static JSONParam appliedParam(Object value, JSONParams params) {
            Objects.requireNonNull(params);
            if (value == null) {
                return null;
            }
            if (!(value instanceof String)) {
                return null;
            }
            String stringVal = (String) value;
            if (!(hasParam(stringVal))) {
                return null;
            }

            String paramName = extractParamName(stringVal);
            JSONParam jsonParam = params.get(paramName);
            check(() -> jsonParam != null, () -> new ParamNotFound(String.format(PARAM_NOT_FOUND, jsonParam.name())));
            return jsonParam;
        }
    }


    private JSonDirectComparator() {
      /*  */
    }

    private static String extractParamName(String stringVal) {
        return stringVal.substring(START_PARAM.length(), stringVal.length() - END_PARAM.length());
    }

    private static boolean hasParam(String stringVal) {
        return stringVal.startsWith(START_PARAM) && stringVal.endsWith(END_PARAM);
    }

    /**
     * Простое сравнение двух строк содержащих данные json
     * Сравнение производится с предварительным преобразованием к приведенному виду.
     * Результат выполнения не дает даталей расхождений.
     *
     * @param json1 строка (not null)
     * @param json2 строка (not null)
     * @return {@code true} если два данных json идентичны  по структуре
     */
    @Api(State.INTERNAL)
    public static boolean compareJSonStrings(String json1, String json2, PathRepresentation representation) {
        Objects.requireNonNull(json1);
        Objects.requireNonNull(json2);
        return compareJSonsObjects(loadJson(json1), loadJson(json2), representation).isSuccessful();
    }

    /**
     * Простое сравнение двух потоков - содержащих данные json.
     * Сравнение производится с предварительным преобразованием к приведенному виду.
     * Результат выполнения не дает даталей расхождений.
     * <p>
     * Api note - внутри метода не осуществляется закрытия потоков.
     *
     * @param json1          поток с данными (not null)
     * @param json2          поток с данными (not null)
     * @param representation объект интерпретации пути (not null)
     * @return {@code true} если два данных json идентичны  по структуре
     */
    public static NodeCheckResult jsonStreamComparison(
            InputStream json1, InputStream json2,
            PathRepresentation representation) throws IOException {
        Objects.requireNonNull(json1);
        Objects.requireNonNull(json2);
        return compareJSonsObjects(loadJson(json1), loadJson(json2), representation);
    }

    /**
     * Простое сравнение двух потоков - содержащих данные json.
     * Сравнение производится с предварительным преобразованием к приведенному виду.
     * Результат выполнения не дает даталей расхождений.
     * <p>
     * Api note - внутри метода не осуществляется закрытия потоков.
     *
     * @param json1          поток с данными (not null)
     * @param json2          поток с данными (not null)
     * @param representation объект представления пути (not null)
     * @return {@code true} если два данных json идентичны  по структуре
     */
    @Api(State.INTERNAL)
    public static boolean compareJSonStreams(
            InputStream json1,
            InputStream json2,
            PathRepresentation representation) throws IOException {

        Objects.requireNonNull(json1);
        Objects.requireNonNull(json2);
        return jsonStreamComparison(json1, json2, representation).isSuccessful();
    }

    /**
     * Сравнение двух объектов JSON на соответсвие
     *
     * @param o1 - объект
     * @param o2 - объект
     * @return результат сравнения
     */
    @Api(State.INTERNAL)
    public static NodeCheckResult compareJSonsObjects(JSONObject o1, JSONObject o2, PathRepresentation representation) {
        return compareJSonsObjects(o1, o2, representation, Collections.emptyList());
    }

 /* ----------------------------------------------------- compareJSonsObjects ---------------------------------------- */

    /**
     * Сравнение двух объектов Json c учетом исключений путей до ноды и прилагаемыми параметрами
     * <p>
     * Используемые шаблоны включени и исключения работают так :
     * Если inclusionsTemplate не определен (== null) выбираются все возможные значения
     * Если inclusionsTemplate  (  != null) выбираются все значения путь до которых совпадает с переданными шаблонами.
     * <p>
     * Из выбранных нод исключаются все ноды которые соответствую шаблонам исключения.
     * (Либо ноды не исключаются если exclusionsTemplate == null)
     *
     * @param o1                 первый объект (всегда не null)
     * @param o2                 второй объект (всегда не null)
     * @param representation     объект представления данных путей (всегда не null)
     * @param params             Набор параметров - параметры определяются в мапе (допустимо значение null -
     *                           при этом работает так же так если бы папраметров не было)
     * @param inclusionsTemplate набор шаблонов для включения (допустимо null значение - в этом случае выбираются все ноды)
     * @param exclusionsTemplate набор шаблонов для исключения (допустимо null значение - в этом случае работает так как если бы исключений не было)
     *                           exclusions[n] всегда не null
     * @return результат сравнения
     */
    @Api(State.INTERNAL)
    public static NodeCheckResult compareJSonsObjects(
            JSONObject o1,
            JSONObject o2,
            PathRepresentation representation,
            JSONParams params,
            Collection<String> inclusionsTemplate,
            Collection<String> exclusionsTemplate) {

        ResultNodeCheckContext result = new ResultNodeCheckContext();
        doCompareJSonsObjects(o1, o2, params == null ? JSONParamsStub.STUB_PARAMS : params,
                inclusionsTemplate, exclusionsTemplate, result,
                representation);
        return result;
    }


    /**
     * Сравнение двух объектов Json c учетом исключений путей до ноды
     *
     * @param o1             первый объект (всегда не null)
     * @param o2             второй объект (всегда не null)
     * @param representation объект представления пути
     * @param exclusions     наборы исключений (допустимо null значение - в этом случае работает так как если бы исключений не было)
     *                       exclusions[n] всегда не null
     * @return результат сравнения
     */
    @SafeVarargs
    @Api(State.INTERNAL)
    public static NodeCheckResult compareJSonsObjects(
            JSONObject o1, JSONObject o2,
            PathRepresentation representation,
            Collection<String>... exclusions) {

        return compareJSonsObjects(o1, o2, representation, null, null, uniteCollections(exclusions));
    }

    public static PathCluster parseCluster(String item) {
        String kind = Formats.formatType(item);
        PathFormat pathFormat = Formats.format(kind);
        return PathCluster.of(null, null).putPath(kind, pathFormat.parsePath(item));
    }

    /**
     * Загрузить объект JSONObject из потока
     *
     * @param jsonData поток (всегда не null)
     * @return JSONObject
     * @throws WrongDataFormat в случае ошибки разбора
     */
    public static JSONObject loadJson(InputStream jsonData) {
        Objects.requireNonNull(jsonData);
        try {
            return (JSONObject) new JSONParser().parse(new InputStreamReader(jsonData));
        } catch (ParseException e) {
            throw new WrongDataFormat(e);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

/* ----------------------------------------------------- privates --------------------------------------------------- */
    /*
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     */

    /**
     * Собрать из нескольких коллекций одну с уникальными элементами - коллекция - объединение коллекций.
     *
     * @param collections коллекции
     * @param <T>         Тип элементов
     * @return объдинение коллекций.
     */
    @SafeVarargs
    public static <T> Collection<T> uniteCollections(Collection<T>... collections) {
        return Stream.of(collections).filter(Objects::nonNull).
                map((Function<Collection<T>, Collection<T>>) ts -> ts != null ? ts : Collections.emptyList()).
                flatMap(Collection::stream).collect(Collectors.toSet());
    }

    @SuppressWarnings("unchecked")
    private static Set<Map.Entry<String, Object>> castToEntrySet(Object o) {
        return (Set<Map.Entry<String, Object>>) o;
    }

    @SuppressWarnings("unchecked")
    private static <T> T cast(Object obj) {
        return (T) obj;
    }

    @SuppressWarnings("unchecked")
    private static <T> T cast(Object obj, Class<T> clazz) {
        return (T) obj;
    }

    private static void doCompareJSonsObjects(
            JSONObject o1,
            JSONObject o2,
            JSONParams params,
            Collection<String> inclusion,
            Collection<String> exclusion,
            NodeCheckContext context, PathRepresentation representation) {

        Objects.requireNonNull(o1);
        Objects.requireNonNull(o2);
        Objects.requireNonNull(context);
        Objects.requireNonNull(params);

        Collection<PathCluster> inclusions = inclusion == null ? null : parseTemplate(inclusion);
        Collection<PathCluster> exclusions = exclusion == null ? null : parseTemplate(exclusion);

        Map<Path, PathCluster> objects1 = filter(pathClusters(o1), params, inclusions, exclusions).stream().
                collect(Collectors.toMap(pc -> pc.path(AddressType.DIRECT.name()), i -> i));
        Map<Path, PathCluster> objects2 = filter(pathClusters(o2), params, inclusions, exclusions).stream().
                collect(Collectors.toMap(pc -> pc.path(AddressType.DIRECT.name()), i -> i));

        objects1.entrySet().stream().map(o -> o).
                /*sorted((p1, p2) -> comparePaths(p1.getKey(), p2.getKey())).*/
                        forEach(entry1 -> {
                    if (!objects2.containsKey(entry1.getKey())) {
                        context.log("value by path " + stringRep(entry1.getValue(), entry1.getKey(), representation) +
                                " not found in document 2");
                        context.failed();
                    }
                });
        objects2.entrySet().stream().map(o -> o).
               /*  sorted((p1, p2) -> comparePaths(p1.getKey(), p2.getKey())).*/
                       forEach(entry2 -> {
                   if (!objects1.containsKey(entry2.getKey())) {
                       context.log("value by path " + stringRep(entry2.getValue(), entry2.getKey(), representation) +
                               " not found in document 1");
                       context.failed();
                   }
               });

/*  objects1 и objects2 Содержат отсортированный список общих путей.  */

        Collection<Path> commonPaths = intersectCollections(objects1.keySet(), objects2.keySet());
        commonPaths.stream().
                map(i -> new Values(i, objects1.get(i), objects2.get(i),
                        objects1.get(i).value(), null, objects2.get(i).value(), null)).

                map(i -> i.applyParams(params)).
                forEach(vs -> compareValues(vs, context, representation));
    }

    private static Collection<PathCluster> parseTemplate(Collection<String> items) {
        Objects.requireNonNull(items);
        return items.stream().peek(Objects::requireNonNull).map(JSonDirectComparator::parseCluster).
                collect(Collectors.toCollection(ArrayList::new));
    }

    private static List<PathCluster> filter(
            List<PathCluster> clusters,
            JSONParams params,
            Collection<PathCluster> inclusions,
            Collection<PathCluster> exclusions) {
        Objects.requireNonNull(clusters);
        return clusters.stream().
                map(cluster -> transformCluster(cluster, params)).
                filter(i -> inclusionFilter(i, inclusions)).
                filter(i -> exclusionFilter(i, exclusions)).
                collect(Collectors.toList());
    }

    private static PathCluster transformCluster(PathCluster cluster, JSONParams params) {
        boolean hasParams = cluster.kinds().stream().map(cluster::path).flatMap(path -> path.elements().stream()).
                flatMap(element -> element.list().stream()).map(Id::id).filter(Objects::nonNull).
                peek(value -> checkParam(value, params)).anyMatch(JSonDirectComparator::hasParam);
        if (!hasParams) {
            /* there is no params - returning unfiltered data. */
            return cluster;
        }


        /* На момент трансформации все элементы проверены. Трансформация выполняется только в случае
           если есть хотя бы один элемент, который содержит параметр */

        Map<String, Path> modified = new HashMap<>();

        for (Map.Entry<String, Path> entry : cluster.unmodifiablePathMap().entrySet()) {
            Path.Builder builder = Path.builder();
            for (PathParticle particle : entry.getValue().particles()) {
                switch (particle.type()) {
                    case ANYELEMENT: {
                        builder.ast();
                        break;
                    }

                    case DELIMETR: {
                        builder.del();
                        break;
                    }

                    case NO_TRESPASING: {
                        builder.noTrespassing();
                        break;
                    }

                    case ELEMENT: {
                        Element modifiedElement = Element.empty();
                        for (Id id : Element.class.cast(particle).list()) {
                            Id modifiedId = id;
                            if (id.type().in(IdentifierType.notArrayItem) && hasParam(id.id())) {
                                modifiedId = Id.obj(String.class.cast(DirectValue.class.cast(
                                        params.get(extractParamName(id.id()))).value()));
                            }
                            modifiedElement = modifiedElement.addId(modifiedId);
                        }
                        builder.rawElement(modifiedElement);
                        break;
                    }

                    default: {
                        throw new IllegalStateException("unknown particle type " + particle.type());
                    }
                }
            }
            modified.put(entry.getKey(), builder.get());
        }


        return new PathCluster(cluster.valueKey(), cluster.value(), modified);
    }

    private static void checkParam(String value, JSONParams params) {
        if (!hasParam(value)) return;
        String paramName = extractParamName(value);
        check(() -> DirectValue.class.isInstance(params.get(paramName)),
                () -> new IllegalStateException("param " + paramName + " must support " + DirectValue.class.getName()));
        check(() -> String.class.isInstance(DirectValue.class.cast(params.get(paramName)).value()),
                () -> new IllegalStateException("param " + paramName + " must be of " +
                        DirectValue.class.getName() + " type"));
    }

    private static boolean exclusionFilter(PathCluster path, Collection<PathCluster> exclusions) {
        return exclusions == null || exclusions.stream().noneMatch(exc -> isApplied(path, exc));
    }

    private static boolean inclusionFilter(PathCluster path, Collection<PathCluster> inclusions) {
        return inclusions == null || inclusions.stream().anyMatch(exc -> isApplied(path, exc));
    }

    @SignatureSensitive
    private static boolean isApplied(PathCluster path, PathCluster template) {
        Objects.requireNonNull(template);
        Objects.requireNonNull(path);
        boolean result = false;
        for (String kind : intersectCollections(path.kinds(), template.kinds())) {
            result = result || Formats.isApplied(kind, path.path(kind), template.path(kind));
        }
        return result;
    }

    private static void compareValues(Values vs, NodeCheckContext context, PathRepresentation representation) {


        if ((vs.value1() == null && vs.param1() == null) && vs.value2() != null) {
            /* только одно из значений null */

            context.log(VALUES_BY_PATH, " ", stringRep(vs.cluster1(), vs.keyPath(), representation),
                    " are not equal. " + vs.value1() + " != " + vs.value2());
            context.failed();
        } else if (vs.value1() != null && (vs.value2() == null && vs.param2() == null)) {
            /* только одно из значений null */

            context.log(VALUES_BY_PATH, " ", stringRep(vs.cluster1(), vs.keyPath(), representation),
                    " are not equal. " + vs.value1() + " != " + vs.value2());
            context.failed();
        } else if (vs.param1() != null && vs.param2() != null && !compareParams(vs.param1(), vs.param2())) {
            /* по пути находятся оба параметра */
            context.log("values with params by path ", stringRep(vs.cluster1(), vs.keyPath(), representation),
                    " do not match. " + vs.param1().paramsRep() + " and " + vs.param2().paramsRep());
            context.failed();
        } else if (vs.param1() == null && vs.param2() == null &&
                vs.value1() != null && vs.value2() != null &&
                !CompatibleTypesComparatorV2.objectTypesCompatible(vs.value1(), vs.value2())) {
            /* Оба значения определены и имеют несовместимый тип */

            context.log(VALUES_BY_PATH, " ", stringRep(vs.cluster1(), vs.keyPath(), representation),
                    " have incompatible types. " + vs.value1().getClass().getName() +
                            " and " + vs.value2().getClass().getName());
            context.failed();
        } else if (vs.param1() == null && vs.param2() == null && !CompatibleTypesComparatorV2.compatibleEquals(vs.value1(), vs.value2())) {
            /* both values are not compatible */

            context.log(VALUES_BY_PATH, " ", stringRep(vs.cluster1(), vs.keyPath(), representation),
                    " are not equal. " + vs.value1() + " != " + vs.value2());
            context.failed();
        } else if ((vs.param1() == null && vs.param2() != null) && !vs.param2().matches(vs.value1())) {
            /* Один параметр и одно значение */

            context.log(VALUES_BY_PATH, " ", stringRep(vs.cluster1(), vs.keyPath(), representation),
                    " do not match. " + vs.value1() + " does not match " + vs.param2().paramsRep());
            context.failed();
        } else if ((vs.param1() != null && vs.param2() == null) && !vs.param1().matches(vs.value2())) {
            /* Один параметр и одно значение */

            context.log(VALUES_BY_PATH, " ", stringRep(vs.cluster1(), vs.keyPath(), representation),
                    " do not match. " + vs.value2() + " does not match " + vs.param1().paramsRep());
            context.failed();
        }

    }

    private static boolean compareParams(JSONParam param, JSONParam param2) {
        return param.matchesParam(param2) || param2.matchesParam(param);
    }

    private static String stringRep(PathCluster pathCluster, Path keyPath, PathRepresentation representation) {
        Path path = pathCluster.path(AddressType.MONKEY.name());
        if (path != null) {
            return representation.path(path);
        }
        return representation.path(keyPath);

    }

    @SignatureSensitive
    private static List<PathCluster> pathClusters(JSONObject object) {
        List<PathCluster> monkeyIdCompType = Flatters.flatten(AddressType.MONKEY.name(), object).stream().
                map(vr -> PathCluster.of(vr.valueKey(), vr.value()).putPath(AddressType.MONKEY.name(), vr.path())).
                collect(Collectors.toList());

        List<PathCluster> direct = Flatters.flatten(AddressType.DIRECT.name(), object).stream().
                map(vr -> PathCluster.of(vr.valueKey(), vr.value()).putPath(AddressType.DIRECT.name(), vr.path())).
                collect(Collectors.toList());


        List<PathCluster> clusters = new ArrayList<>();
        clusters.addAll(monkeyIdCompType);
        clusters.addAll(direct);


        return new ArrayList<>(clusters.stream().collect(Collectors.toMap(PathCluster::valueKey, cl -> cl,
                (cl, cl2) -> PathCluster.merge(cl, cl2))).values());
    }

    private static JSONObject loadJson(String jsonData) {
        Objects.requireNonNull(jsonData);
        try {
            return (JSONObject) new JSONParser().parse(jsonData);
        } catch (ParseException e) {
            throw new WrongDataFormat(e);
        }
    }

/* ------------------------------------------- public top-level maintained methods ---------------------------------- */

    /**
     * Simple json string comparison
     *
     * @param json1 строка (not null)
     * @param json2 строка (not null)
     * @return {@code true} if jsons are structurally identical
     */
    @Api(State.MAINTAINED)
    public static boolean compareJSonStrings(String json1, String json2) {
        Objects.requireNonNull(json1);
        Objects.requireNonNull(json2);
        return compareJSonsObjects(loadJson(json1), loadJson(json2), MonkeyPathRepresentation.REPRESENTATION).isSuccessful();
    }


    /**
     * Простое сравнение двух потоков - содержащих данные json.
     * Сравнение производится с предварительным преобразованием к приведенному виду.
     * Результат выполнения не дает даталей расхождений.
     * <p>
     * Api note - внутри метода не осуществляется закрытия потоков.
     *
     * @param json1 поток с данными (not null)
     * @param json2 поток с данными (not null)
     * @return {@code true} если два данных json идентичны  по структуре
     */
    @Api(State.MAINTAINED)
    public static boolean compareJSonStreams(
            InputStream json1,
            InputStream json2) throws IOException {

        Objects.requireNonNull(json1);
        Objects.requireNonNull(json2);
        return compareJSonStreams(json1, json2, MonkeyPathRepresentation.REPRESENTATION);
    }

    /**
     * Простое сравнение двух потоков - содержащих данные json.
     * Сравнение производится с предварительным преобразованием к приведенному виду.
     * Результат выполнения не дает даталей расхождений.
     * <p>
     * Api note - внутри метода не осуществляется закрытия потоков.
     *
     * @param json1 поток с данными (not null)
     * @param json2 поток с данными (not null)
     * @return {@code true} если два данных json идентичны  по структуре
     */
    @Api(State.INCUBATING)
    public static NodeCheckResult compareJSonStreams(
            InputStream json1,
            InputStream json2,
            JSONParams jsonParams,
            Collection<String> inclusionsTemplate,
            Collection<String> exclusionsTemplate) {
        return compareJSonsObjects(loadJson(json1), loadJson(json2), MonkeyPathRepresentation.REPRESENTATION,
                jsonParams, inclusionsTemplate, exclusionsTemplate);
    }


}
