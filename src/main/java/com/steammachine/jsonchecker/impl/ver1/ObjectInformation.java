package com.steammachine.jsonchecker.impl.ver1;

import com.steammachine.common.definitions.annotations.SignatureSensitive;
import com.steammachine.jsonchecker.types.UnmappedProperties;
import com.steammachine.common.utils.commonutils.CommonUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 30.12.2017 10:21:46
 *
 * @author Vladimir Bogodukhov
 **/
public class ObjectInformation {

    private class FixupItem {
        private final String objectName;
        private final Method method;

        public FixupItem(Method method, String objectName) {
            this.method = Objects.requireNonNull(method);
            this.method.setAccessible(true);
            this.objectName = objectName;
        }

        public void fixup() {
            if (!nameSpace.containsKey(objectName)) {
                throw new IllegalStateException("object '" + objectName + "' not found");
            }
            Object refValue = nameSpace.get(objectName);
            CommonUtils.suppress(() -> method.invoke(object, refValue));
        }
    }

    private static class MethodDataInfoKey {
        private final Method method;
        private final String name;
        private final Class<?> paramType;

        MethodDataInfoKey(Method method, String name, Class<?> paramType) {
            this.method = Objects.requireNonNull(method);
            this.name = Objects.requireNonNull(name);
            this.paramType = Objects.requireNonNull(paramType);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MethodDataInfoKey)) return false;

            MethodDataInfoKey that = (MethodDataInfoKey) o;

            if (name != null ? !name.equals(that.name) : that.name != null) return false;
            return paramType != null ? paramType.equals(that.paramType) : that.paramType == null;
        }

        @Override
        public int hashCode() {
            int result = name != null ? name.hashCode() : 0;
            result = 31 * result + (paramType != null ? paramType.hashCode() : 0);
            return result;
        }

        public Method method() {
            return method;
        }

        public String name() {
            return name;
        }

        public Class<?> paramType() {
            return paramType;
        }
    }


    private final Object parentobject;
    private final Object object;
    private final String instanceName;
    private final Map<String, List<Method>> classInfo;
    private final UnmappedProperties defaultProps;
    private final List<FixupItem> fixups = new ArrayList<>();
    private final Map<String, Object> nameSpace;

    public ObjectInformation(Object parentobject, Object object, String instanceName, Map<String, Object> nameSpace) {
        this.parentobject = parentobject;
        this.object = Objects.requireNonNull(object);
        this.defaultProps = object instanceof UnmappedProperties ? (UnmappedProperties) object : null;
        this.nameSpace = Objects.requireNonNull(nameSpace);
        this.instanceName = instanceName;
        this.classInfo = loadClassInfo(object.getClass());
    }

    public Object object() {
        return object;
    }

    public Object parentobject() {
        return parentobject;
    }

    public void setValue(String propertyName, Object value) {
        List<Method> methods = classInfo.get("set" + capitalize(propertyName));
        if (methods == null) {
            if (defaultProps == null) {
                throw new IllegalStateException("");
            }
            defaultProps.setValue(propertyName, "" + value);
            return;
        }
        if (methods.size() != 1) {
            throw new IllegalStateException("multyprops are not supported yet.  there must be only one " +
                    "set" + capitalize(propertyName) + " method");
        }

        Method method = methods.get(0);
            /* это значение */
        method.setAccessible(true);
        CommonUtils.suppress(() -> method.invoke(object, value));
    }

    public void setValueCandidate(String propertyName, String value) {
        List<Method> methods = classInfo.get("set" + capitalize(propertyName));
        if (methods == null) {
            if (defaultProps == null) {
                throw new IllegalStateException("no methods found for property " + propertyName);
            }
            defaultProps.setValue(propertyName, value);
            return;
        }

        if (methods.size() != 1) {
            throw new IllegalStateException("multyprops are not supported yet.  there must be only one " +
                    "set" + capitalize(propertyName) + " method");
        }
        Method method = methods.get(0);
        Class<?> type = method.getParameterTypes()[0];
        if (!isValue(type)) {
            /* это ссылка */
            fixups.add(new FixupItem(method, value));
        } else {
            /* это значение */
            Object valueToSet = value(type, value);
            method.setAccessible(true);
            CommonUtils.suppress(() -> method.invoke(object, valueToSet));
        }
    }



    public void fixup() {
        fixups.forEach(FixupItem::fixup);
    }

    @SignatureSensitive
    private static Map<String, List<Method>> loadClassInfo(Class clazz) {
        Map<MethodDataInfoKey, Method> methodMap = new HashMap<>();
        Class<?> c = clazz;
        while (c != null) {
            Stream.of(c.getDeclaredMethods()).
                    filter((m) -> !Modifier.isStatic(m.getModifiers())).
                    filter((m) -> m.getName().startsWith("set")).
                    filter((m) -> m.getReturnType() == void.class).
                    filter((m) -> m.getParameterCount() == 1).
                    map((m) -> new MethodDataInfoKey(m, m.getName(), m.getParameterTypes()[0])).
                    forEach((mi) -> methodMap.putIfAbsent(mi, mi.method()));
            c = c.getSuperclass();
        }

        Map<String, List<Method>> result = new HashMap<>();
        methodMap.entrySet().forEach((t) -> {
                    result.putIfAbsent(t.getKey().name(), new ArrayList<>());
                    result.get(t.getKey().name()).add(t.getValue());
                }
        );
        return result;
    }


    public static final Map<String, Class> VALUE_CLASSES = Stream.of(
            String.class,
            Boolean.TYPE,
            Boolean.class,
            Byte.TYPE,
            Byte.class,
            Short.TYPE,
            Short.class,
            Integer.TYPE,
            Integer.class,
            Long.TYPE,
            Long.class,
            Float.TYPE,
            Float.class,
            Double.TYPE,
            Double.class,
            BigDecimal.class,
            BigInteger.class
    ).collect(Collectors.toMap(Class::getName, c -> c));

    public static boolean isValue(Class typeParam) {
        return VALUE_CLASSES.containsValue(typeParam);
    }

    public static Object value(Class typeParam, String value) {
        Objects.requireNonNull(typeParam);
        if (value == null) {
            return null;
        } else if (typeParam == String.class) {
            return value;
        } else if (typeParam == Boolean.TYPE) {
            return parseBoolean(value);
        } else if (typeParam == Boolean.class) {
            return parseBoolean(value);
        } else if (typeParam == Byte.TYPE) {
            return Byte.parseByte(value);
        } else if (typeParam == Byte.class) {
            return Byte.parseByte(value);
        } else if (typeParam == Short.TYPE) {
            return Short.parseShort(value);
        } else if (typeParam == Short.class) {
            return Short.parseShort(value);
        } else if (typeParam == Integer.TYPE) {
            return Integer.parseInt(value);
        } else if (typeParam == Integer.class) {
            return Integer.parseInt(value);
        } else if (typeParam == Long.TYPE) {
            return Long.parseLong(value);
        } else if (typeParam == Long.class) {
            return Long.parseLong(value);
        } else if (typeParam == Float.TYPE) {
            return Float.parseFloat(value);
        } else if (typeParam == Float.class) {
            return Float.parseFloat(value);
        } else if (typeParam == Double.TYPE) {
            return Double.parseDouble(value);
        } else if (typeParam == Double.class) {
            return Double.parseDouble(value);
        } else if (typeParam == BigDecimal.class) {
            return new BigDecimal(value);
        } else if (typeParam == BigInteger.class) {
            return new BigInteger(value);
        }

        throw new IllegalStateException("type " + typeParam + " is not supported");
    }

    public static Class getLoadedClass(String classTypeName) {
        Objects.requireNonNull(classTypeName);
        if (VALUE_CLASSES.get(classTypeName) != null) {
            return VALUE_CLASSES.get(classTypeName);
        }
        return CommonUtils.suppress(() -> Class.forName(classTypeName));
    }

    private static String capitalize(String word) {
        Objects.requireNonNull(word);
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    private static boolean parseBoolean(String value) {
        if ("false".equalsIgnoreCase(value)) {
            return false;
        } else if ("true".equalsIgnoreCase(value)) {
            return true;
        }
        throw new IllegalStateException(" " + value + " is not valid boolean VALUE");
    }


}
