package com.steammachine.jsonchecker.utils;

import com.steammachine.common.definitions.annotations.SignatureSensitive;
import com.steammachine.jsonchecker.impl.ver1.*;
import com.steammachine.jsonchecker.types.LoadResult;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.steammachine.jsonchecker.defaults.DefaultWidgetNode;
import com.steammachine.jsonchecker.types.ExtraInfo;
import com.steammachine.jsonchecker.types.exceptions.MalformedDocument;
import com.steammachine.jsonchecker.types.TreeNode;
import com.steammachine.jsonchecker.types.exceptions.ParamNotFound;

import java.io.*;
import java.util.*;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.steammachine.common.utils.commonutils.CommonUtils.*;
import static com.steammachine.common.utils.commonutils.CommonUtils.SupressedExceptionSupplier;
import static com.steammachine.common.utils.commonutils.CommonUtils.suppress;

/**
 * Класс с набором утилит для представления JSON данных в виде древовидной структуры объектов маппинга
 * <p>
 * 30.12.2017 10:21:46
 *
 * @author Vladimir Bogodukhov
 **/
public class JSonMapper {

    private static final Predicate<Map.Entry<String, List<String>>> MORE_THAN_ONE_ITEM = e -> e.getValue().size() > 1;
    private static final Predicate<Map.Entry<String, List<ParamInfo>>> MORE_THAN_ONE_PARAM = e -> e.getValue().size() > 1;

    public static LoadResult load(String path, Class defaultClass, Map<String, Object> params) throws IOException {
        try (InputStream stream = new BufferedInputStream(suppress(() -> new FileInputStream(path)));) {
            return load(stream, defaultClass, params);
        }
    }

    public static LoadResult load(String path, Map<String, Object> param) throws IOException {
        return load(path, DefaultWidgetNode.class, param);
    }

    public static LoadResult load(String path) throws IOException {
        return load(path, DefaultWidgetNode.class, Collections.emptyMap());
    }

    public static LoadResult load(InputStream stream, Class defaultClass, Map<String, Object> params) {
        Object root = suppress(() -> new JSONParser().parse(new InputStreamReader(stream)));
        InterfaceInfo interfaceInfo = loadInterfaceInfo(defaultClass, params, root);
        List<ObjectInformation> objects = loadTreeAsList(root, interfaceInfo);
        TreeNode rootNode = fixup(objects);

        DefaultExtraInfo extraInfo = new DefaultExtraInfo(interfaceInfo.userDefinedData());

        return new LoadResult() {
            @Override
            public TreeNode root() {
                return rootNode;
            }

            @Override
            public ExtraInfo extrainfo() {
                return extraInfo;
            }
        };
    }

/* ----------------------------------------------------- privates -------------------------------------------------- */


    private static InterfaceInfo loadInterfaceInfo(Class defaultClass, Map<String, Object> params, Object jsonRoot) {
        Objects.requireNonNull(defaultClass);

        DefaultInterfaceInfo info = new DefaultInterfaceInfo();
        info.setDefaultNodeClass(defaultClass);

        if (!(jsonRoot instanceof JSONObject)) {
            return info.lock();
        }


        if (!(((JSONObject) jsonRoot).get(ReservedWords.INTERFACE.ident()) instanceof JSONObject)) {
            return info.lock();
        }
        JSONObject interfaceNode = (JSONObject) ((JSONObject) jsonRoot).get(ReservedWords.INTERFACE.ident());
//        info.setDefaultNodeClass(defaultClass);


        check(interfaceNode.get(ReservedWords.TYPES.ident()) == null || interfaceNode.get(ReservedWords.TYPES.ident()) instanceof JSONObject,
                () -> ReservedWords.TYPES.ident() + " element must be of type " + JSONObject.class);

        if (interfaceNode.get(ReservedWords.TYPES.ident()) != null) {
            JSONObject types = (JSONObject) interfaceNode.get(ReservedWords.TYPES.ident());


            if (types.get(ReservedWords.ITEMS.ident()) != null) {
                check(types.get(ReservedWords.ITEMS.ident()) instanceof JSONArray,
                        () -> "items element must be of type " + JSONObject.class);
                info.types().putAll(loadTypes(types.get(ReservedWords.ITEMS.ident())));
            }

            if (types.get(ReservedWords.DEFAULTCLASS.ident()) != null) {
                check(types.get(ReservedWords.DEFAULTCLASSALIAS.ident()) == null,
                        () -> ReservedWords.DEFAULTCLASS.ident() + " and " + ReservedWords.DEFAULTCLASSALIAS.ident() +
                                " cannnot be defined simulteniousely");
            }

            if (types.get(ReservedWords.DEFAULTCLASSALIAS.ident()) != null) {
                check(types.get(ReservedWords.DEFAULTCLASS.ident()) == null,
                        () -> ReservedWords.DEFAULTCLASS.ident() + " and " + ReservedWords.DEFAULTCLASSALIAS.ident() +
                                " cannnot be defined simulteniousely");
            }

            if (types.get(ReservedWords.DEFAULTCLASSALIAS.ident()) != null) {
                check(types.get(ReservedWords.DEFAULTCLASSALIAS.ident()) instanceof String,
                        () -> ReservedWords.DEFAULTCLASSALIAS.ident() + " element must be of type " + JSONObject.class);
                final String classAliasName = (String) types.get(ReservedWords.DEFAULTCLASSALIAS.ident());
                check(info.types().get(classAliasName) != null,
                        () -> "no class found for alias " + classAliasName);
                info.setDefaultNodeClass(info.types().get(classAliasName));

            } else if (types.get(ReservedWords.DEFAULTCLASS.ident()) != null) {
                check(types.get(ReservedWords.DEFAULTCLASS.ident()) instanceof String,
                        () -> ReservedWords.DEFAULTCLASS.ident() + " element must be of type " + JSONObject.class);
                final String className = (String) types.get(ReservedWords.DEFAULTCLASS.ident());
                info.setDefaultNodeClass(suppress(() -> Class.forName(className)));

            }
        }

        if (interfaceNode.get(ReservedWords.PARAMS.ident()) != null) {
            check(interfaceNode.get(ReservedWords.PARAMS.ident()) instanceof JSONArray,
                    () -> ReservedWords.PARAMS.ident() + " element must be of type " + JSONArray.class);
            info.params().putAll(loadParams(params, interfaceNode.get(ReservedWords.PARAMS.ident())));
        }


        if (interfaceNode.get(ReservedWords.USERDATA.ident()) != null) {
            check(interfaceNode.get(ReservedWords.USERDATA.ident()) instanceof JSONArray,
                    () -> ReservedWords.USERDATA.ident() + " element must be of type " + JSONArray.class);
            info.userDefinedData().addAll(loadUserData(interfaceNode.get(ReservedWords.USERDATA.ident())));
        }

        return info.lock();
    }


    static class DefautParamInfo implements ParamInfo {
        Class clazz;
        String name;
        Object defaultValue;
        Object value;
        boolean hasDefault;

        @Override
        public Class clazz() {
            return clazz;
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public Object value() {
            return value;
        }

        public Object defaultValue() {
            return defaultValue;
        }

        public boolean hasDefault() {
            return hasDefault;
        }

        public void setClass(Class clazz) {
            this.clazz = clazz;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setDefaultValue(Object defaultValue) {
            this.defaultValue = defaultValue;
            this.hasDefault = true;
        }

        public DefautParamInfo setValues(Map<String, Object> params) {
            Objects.requireNonNull(params);
            if (!hasDefault() && !params.containsKey(name())) {
                throw new IllegalStateException("param " + name() + " is not found");
            }

            value = defaultValue;
            if (params.containsKey(name())) {
                value = params.get(name());
            }

            return this;
        }

        @Override
        public String toString() {
            return "DefautParamInfo{" +
                    "clazz=" + clazz +
                    ", name='" + name + '\'' +
                    ", defaultValue=" + defaultValue +
                    ", VALUE=" + value +
                    ", hasDefault=" + hasDefault +
                    '}';
        }
    }

    private static DefautParamInfo parseParamInfo(JSONObject jsonObject) {
        Objects.requireNonNull(jsonObject);

        DefautParamInfo result = new DefautParamInfo();
        check(jsonObject.get(ReservedWords.NAME.ident()) != null, () -> "name item must be present in each param");
        check(jsonObject.get(ReservedWords.NAME.ident()) instanceof String, () -> "name item must be of type String in each param");
        result.setName((String) jsonObject.get(ReservedWords.NAME.ident()));

        check(jsonObject.get(ReservedWords.CLASS.ident()) != null, () -> "class item must be present in each param");
        check(jsonObject.get(ReservedWords.CLASS.ident()) instanceof String, () -> "class item must be of type String in each param");
        result.setClass(ObjectInformation.getLoadedClass((String) jsonObject.get(ReservedWords.CLASS.ident())));

        if (jsonObject.get(ReservedWords.DEFAULT.ident()) != null) {
            String defaultValue = "" + jsonObject.get(ReservedWords.DEFAULT.ident());
            check(ObjectInformation.isValue(result.clazz()),
                    () -> "param type must be one of " + ObjectInformation.VALUE_CLASSES.values());
            result.setDefaultValue(ObjectInformation.value(result.clazz(), defaultValue));
        }

        return result;
    }

    private static List<String> loadUserData(Object json) {
        check(json instanceof JSONArray, () -> "json object must be JSONArray");
        return Stream.of(((JSONArray) json).toArray()).
                filter(i -> i instanceof String).
                map(o -> (String) o).
                collect(Collectors.toList());
    }


    private static Map<String, Object> loadParams(Map<String, Object> params, Object json) {
        check(json instanceof JSONArray, () -> "json object must be JSONArray");
        /*  Если передается null считаем, что передали пустую мапу параметров  */
        Map<String, Object> parametrs = params != null ? params : Collections.emptyMap();

        List<ParamInfo> definedParams = Stream.of(((JSONArray) json).toArray()).
                filter(o -> o instanceof JSONObject).
                map(o -> (JSONObject) o).
                map(JSonMapper::parseParamInfo).
                map(i -> (ParamInfo) i.setValues(parametrs)).collect(Collectors.toList());

        {
            /* предварительно проверим отсутствие дубликаты параметров */

            Map<String, List<ParamInfo>> preQweriedParams = definedParams.stream().
                    collect(Collectors.toMap(ParamInfo::name, Collections::singletonList, JSonMapper::reduceList));

            if (preQweriedParams.entrySet().stream().anyMatch(MORE_THAN_ONE_PARAM)) {
                String message = preQweriedParams.entrySet().stream().filter(MORE_THAN_ONE_PARAM).
                        map(e -> "param " + e.getKey() + " is mapped to " + e.getValue()).
                        reduce("Some params are declared more than one time:", (s, s2) -> s + "\r\n" + s2);
                throw new MalformedDocument(message);
            }

        }

        return definedParams.stream().collect(Collectors.toMap(ParamInfo::name, ParamInfo::value));
    }

    private static Map<String, Class> loadTypes(Object jsonRoot) {
        Objects.requireNonNull(jsonRoot);
        check(jsonRoot instanceof JSONArray, () -> "jsonRoot(" + jsonRoot + ") is not instanceof JSONArray");

        //noinspection ConstantConditions
        List<String[]> classAndAlias = Stream.of(((JSONArray) jsonRoot).toArray()).
                filter(o -> o instanceof JSONObject).
                map(o -> (JSONObject) o).
                map(ti -> toArray((String) ti.get(ReservedWords.CLASS_ALIAS.ident()), (String) ti.get(ReservedWords.CLASS.ident()))).
                collect(Collectors.toList());

        {
            /* для проверки дубликатов предварительно пробежим по набору определений */
            Map<String, List<String>> collect = classAndAlias.stream().
                    collect(Collectors.toMap(a -> a[0], a -> Collections.singletonList(a[1]), JSonMapper::reduceList));

            if (collect.entrySet().stream().anyMatch(MORE_THAN_ONE_ITEM)) {
                String message = collect.entrySet().stream().filter(MORE_THAN_ONE_ITEM).
                        map(e -> "type alias " + e.getKey() + " is mapped to " + e.getValue() + " classes").
                        reduce("Some type aliases are mapped more than once:", (s, s2) -> s + "\r\n" + s2);
                throw new MalformedDocument(message);
            }
        }

        return classAndAlias.stream().collect(Collectors.toMap(a -> a[0], a -> suppress(() -> Class.forName(a[1]))));
    }


    private static <T> List<T> reduceList(List<T> l1, List<T> l2) {
        List<T> accumumator = new ArrayList<>();
        accumumator.addAll(l1);
        accumumator.addAll(l2);
        return accumumator;
    }

    private static List<ObjectInformation> loadTreeAsList(
            Object jsonRoot,
            InterfaceInfo interfaceInfo) {
        List<ObjectInformation> objInfos = new ArrayList<>();
        Map<String, Object> namespace = new HashMap<>();
        loadFlat(jsonRoot, null, interfaceInfo, objInfos, namespace);
        return objInfos;
    }

    private static void loadFlat(
            Object jsobj,
            Object parent,
            InterfaceInfo interfaceInfo,
            List<ObjectInformation> objInfos,
            Map<String, Object> namespace) {
        Objects.requireNonNull(interfaceInfo);

        if (jsobj instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) jsobj;


            if (jsonObject.get(ReservedWords.CLASS.ident()) != null) {
                check((jsonObject.get(ReservedWords.CLASS_ALIAS.ident()) == null),
                        () -> "class and classalias items must not be defined at the same time");
            }
            if (jsonObject.get(ReservedWords.CLASS_ALIAS.ident()) != null) {
                check((jsonObject.get(ReservedWords.CLASS.ident()) == null),
                        () -> "class and classalias items must not be defined at the same time");
            }

            final Class clazz = getInstanceClass(interfaceInfo, jsonObject);
            Object instance = suppress((SupressedExceptionSupplier<Object>) clazz::newInstance);

            if (jsonObject.get(ReservedWords.INSTANCE_NAME.ident()) != null) {
                check(jsonObject.get(ReservedWords.INSTANCE_NAME.ident()) instanceof String, null);
            }

            String instancename = (jsonObject.get(ReservedWords.INSTANCE_NAME.ident()) != null) ?
                    (String) jsonObject.get(ReservedWords.INSTANCE_NAME.ident()) : null;

            final ObjectInformation info;
            objInfos.add((info = new ObjectInformation(parent, instance, instancename, namespace)));

            //noinspection unchecked
            ((Set<Map.Entry<String, Object>>) jsonObject.entrySet()).stream().
                    filter(e -> !excludedPropertyName(parent == null, e.getKey())).
                    forEach(e -> setValue(interfaceInfo, info, e));

            Object children = jsonObject.get(ReservedWords.CHILDREN.ident());
            if (children != null) {
                /* Считаем, что дочерние объекты расположены в массиве children */
                check(children instanceof JSONArray, () -> "children element must be of type JSONArray");

                //noinspection ConstantConditions
                JSONArray childrenArray = (JSONArray) children;
                for (Object arrayItem : childrenArray) {
                    loadFlat(arrayItem, instance, interfaceInfo, objInfos, namespace);
                }
            }
        }
    }

    private static Class getInstanceClass(InterfaceInfo interfaceInfo, JSONObject jsonObject) {
        final Class clazz;
        if (jsonObject.get(ReservedWords.CLASS.ident()) != null) {
            check(jsonObject.get(ReservedWords.CLASS.ident()) instanceof String, () -> "class item must be of type String");
            clazz = ObjectInformation.getLoadedClass((String) jsonObject.get(ReservedWords.CLASS.ident()));
        } else if (jsonObject.get(ReservedWords.CLASS_ALIAS.ident()) != null) {
            check(jsonObject.get(ReservedWords.CLASS_ALIAS.ident()) instanceof String,
                    () -> "class alias item must be of type String");
            final String type = (String) jsonObject.get(ReservedWords.CLASS_ALIAS.ident());
            check(interfaceInfo.types().get(type) != null, () -> "class alias " + type + " not found");
            clazz = interfaceInfo.types().get(type);
        } else {
            clazz = interfaceInfo.defaultClass();
        }
        return clazz;
    }

    private static void setValue(InterfaceInfo interfaceInfo,
                                 ObjectInformation info,
                                 Map.Entry<String, Object> entry) {
        Objects.requireNonNull(interfaceInfo);
        Objects.requireNonNull(info);
        Objects.requireNonNull(entry);

        if (entry.getValue() == null) {
            info.setValueCandidate(entry.getKey(), null);
        } else if (isParamRef("" + entry.getValue())) {
            String paramName = refParamName("" + entry.getValue());
            check(() -> interfaceInfo.params().containsKey(paramName),
                    () -> new ParamNotFound("param " + paramName + " not found"));
            /* Здась мы устанавливаем данные по передаваемому параметру */
            info.setValue(entry.getKey(), interfaceInfo.params().get(paramName));
        } else {
            info.setValueCandidate(entry.getKey(), "" + entry.getValue());
        }
    }

    @SignatureSensitive
    private static boolean isParamRef(String valueCandidate) {
        return valueCandidate != null && valueCandidate.startsWith("${") && valueCandidate.endsWith("}");
    }

    @SignatureSensitive
    private static String refParamName(String valueCandidate) {
        if (!isParamRef(valueCandidate)) {
            throw new IllegalArgumentException("valueCandidate " + valueCandidate + " is not parametr reference");
        }
        return valueCandidate.substring("${".length(), valueCandidate.length() - "}".length());
    }

    private static boolean excludedPropertyName(boolean root, String propertyName) {
        if (root) {
            return ReservedWords.INTERFACE.ident().equals(propertyName) || ReservedWords.everyObjectExclusion(propertyName);
        } else {
            return ReservedWords.everyObjectExclusion(propertyName);
        }
    }

    private static class DefaultTreeNodeHolder {
        private final DefaultTreeNode node;
        private final ObjectInformation info;

        private DefaultTreeNodeHolder(DefaultTreeNode node, ObjectInformation info) {
            this.node = Objects.requireNonNull(node);
            this.info = Objects.requireNonNull(info);
        }

        public DefaultTreeNode node() {
            return node;
        }

        public ObjectInformation info() {
            return info;
        }
    }

    private static TreeNode fixup(List<ObjectInformation> infos) {
        Objects.requireNonNull(infos);

        List<DefaultTreeNodeHolder> fixes =
                infos.stream().sequential().
                        map((i) -> new DefaultTreeNodeHolder(new DefaultTreeNode(i.object()), i)).
                        collect(Collectors.toList());

        for (DefaultTreeNodeHolder parent : fixes) {
            for (DefaultTreeNodeHolder child : fixes) {
                if (parent != child) {
                    if (parent.info().object() == child.info().parentobject()) {
                        parent.node().addChild(child.node());
                    }
                }
            }
        }

        infos.forEach(ObjectInformation::fixup);
        /* Тут получаем первый элемент - это корень дерева. */
        return fixes.get(0).node();
    }

    private static void check(boolean condition, Supplier<String> stringSupplier) {
        if (!condition) {
            String additionalMessage = stringSupplier == null ? "" : "\r\n with message " + stringSupplier.get();
            throw new IllegalStateException("condition failed" + additionalMessage);
        }
    }

    private static void check(
            BooleanSupplier condition,
            Supplier<? extends RuntimeException> errorSupplier) {
        Objects.requireNonNull(condition);
        Objects.requireNonNull(errorSupplier);
        if (!condition.getAsBoolean()) {
            throw errorSupplier.get();
        }
    }


}
