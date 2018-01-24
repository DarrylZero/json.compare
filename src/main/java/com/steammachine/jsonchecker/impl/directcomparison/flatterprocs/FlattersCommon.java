package com.steammachine.jsonchecker.impl.directcomparison.flatterprocs;

import java.util.*;

/**
 * 30.12.2017 10:21:45
 *
 * @author Vladimir Bogodukhov 
 **/
public class FlattersCommon {

    public static final Set<Class> FLAT_TYPES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            String.class,

            Byte.class,
            Byte.TYPE,

            Short.class,
            Short.TYPE,

            Integer.class,
            Integer.TYPE,

            Long.class,
            Long.TYPE,

            Float.class,
            Float.TYPE,

            Double.class,
            Double.TYPE,

            Character.class,
            Character.TYPE,

            Boolean.class,
            Boolean.TYPE
    )));

    /**
     * Проверить, что объект является простым значением содержашим непоседственно данное,
     * (а не массив или другой объект)
     */
    public static boolean isFlatValue(Object value) {
        return value == null || FLAT_TYPES.contains(value.getClass());
    }


    @SuppressWarnings("unchecked")
    public static Set<Map.Entry<String, Object>> castToEntrySet(Object o) {
        return (Set<Map.Entry<String, Object>>) o;
    }

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj) {
        return (T) obj;
    }

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj, Class<T> clazz) {
        return (T) obj;
    }



}
