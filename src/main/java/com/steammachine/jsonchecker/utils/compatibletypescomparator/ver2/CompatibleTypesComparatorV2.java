package com.steammachine.jsonchecker.utils.compatibletypescomparator.ver2;

import com.steammachine.jsonchecker.utils.compatibletypescomparator.ver2.Comparators.*;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.steammachine.jsonchecker.utils.compatibletypescomparator.ver2.Comparators.*;
import static com.steammachine.common.utils.commonutils.CommonUtils.eval;

/**
 * 30.12.2017 10:21:46
 *
 * @author Vladimir Bogodukhov
 **/
public class CompatibleTypesComparatorV2 {

    private static final int VERSION = 2;

    private static final class TypesKey {
        private final Class type1;
        private final Class type2;

        private TypesKey(Class type1, Class type2) {
            this.type1 = type1;
            this.type2 = type2;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TypesKey)) return false;

            TypesKey typesKey = (TypesKey) o;

            if (type1 != null ? !type1.equals(typesKey.type1) : typesKey.type1 != null) return false;
            return type2 != null ? type2.equals(typesKey.type2) : typesKey.type2 == null;
        }

        @Override
        public int hashCode() {
            int result = type1 != null ? type1.hashCode() : 0;
            result = 31 * result + (type2 != null ? type2.hashCode() : 0);
            return result;
        }
    }

    private static final Map<TypesKey, CompareTo> COMPATIBLE_TYPES;

    static {
        COMPATIBLE_TYPES = eval(() -> Stream.<CompareTo>of(
         /* String */
                new SameTypeComparator<>(String.class),
        /* Byte */
                new SameTypeComparator<>(Byte.class),
                new SameTypeComparator<>(Byte.TYPE),
                new IntegerTypesComparator<>(Byte.TYPE, Byte.class),
                new IntegerTypesComparator<>(Byte.TYPE, Short.class),
                new IntegerTypesComparator<>(Byte.class, Short.class),
                new IntegerTypesComparator<>(Byte.TYPE, Integer.class),
                new IntegerTypesComparator<>(Byte.class, Integer.class),
                new IntegerTypesComparator<>(Byte.TYPE, Long.class),
                new IntegerTypesComparator<>(Byte.class, Long.class),
                new IntFloatTypesComparator<>(Byte.TYPE, Float.class),
                new IntFloatTypesComparator<>(Byte.class, Float.class),
                new IntFloatTypesComparator<>(Byte.TYPE, Double.class),
                new IntFloatTypesComparator<>(Byte.class, Double.class),
                new IntegerTypesComparator<>(Byte.class, Byte.TYPE),
                new IntegerTypesComparator<>(Byte.TYPE, Short.TYPE),
                new IntegerTypesComparator<>(Byte.class, Short.TYPE),
                new IntegerTypesComparator<>(Byte.TYPE, Integer.TYPE),
                new IntegerTypesComparator<>(Byte.class, Integer.TYPE),
                new IntegerTypesComparator<>(Byte.TYPE, Long.TYPE),
                new IntegerTypesComparator<>(Byte.class, Long.TYPE),
                new IntFloatTypesComparator<>(Byte.TYPE, Float.TYPE),
                new IntFloatTypesComparator<>(Byte.class, Float.TYPE),
                new IntFloatTypesComparator<>(Byte.TYPE, Double.TYPE),
                new IntFloatTypesComparator<>(Byte.class, Double.TYPE),

        /* Short */
                new SameTypeComparator<>(Short.class),
                new SameTypeComparator<>(Short.TYPE),
                new IntegerTypesComparator<>(Short.TYPE, Byte.class),
                new IntegerTypesComparator<>(Short.class, Byte.class),
                new IntegerTypesComparator<>(Short.TYPE, Short.class),
                new IntegerTypesComparator<>(Short.TYPE, Integer.class),
                new IntegerTypesComparator<>(Short.class, Integer.class),
                new IntegerTypesComparator<>(Short.TYPE, Long.class),
                new IntegerTypesComparator<>(Short.class, Long.class),
                new IntFloatTypesComparator<>(Short.TYPE, Float.class),
                new IntFloatTypesComparator<>(Short.class, Float.class),
                new IntFloatTypesComparator<>(Short.TYPE, Double.class),
                new IntFloatTypesComparator<>(Short.class, Double.class),
                new IntegerTypesComparator<>(Short.TYPE, Byte.TYPE),
                new IntegerTypesComparator<>(Short.class, Byte.TYPE),
                new IntegerTypesComparator<>(Short.class, Short.TYPE),
                new IntegerTypesComparator<>(Short.TYPE, Integer.TYPE),
                new IntegerTypesComparator<>(Short.class, Integer.TYPE),
                new IntegerTypesComparator<>(Short.TYPE, Long.TYPE),
                new IntegerTypesComparator<>(Short.class, Long.TYPE),
                new IntFloatTypesComparator<>(Short.TYPE, Float.TYPE),
                new IntFloatTypesComparator<>(Short.class, Float.TYPE),
                new IntFloatTypesComparator<>(Short.TYPE, Double.TYPE),
                new IntFloatTypesComparator<>(Short.class, Double.TYPE),

        /* Integer */
                new SameTypeComparator<>(Integer.class),
                new SameTypeComparator<>(Integer.TYPE),
                new IntegerTypesComparator<>(Integer.TYPE, Byte.class),
                new IntegerTypesComparator<>(Integer.class, Byte.class),
                new IntegerTypesComparator<>(Integer.TYPE, Short.class),
                new IntegerTypesComparator<>(Integer.class, Short.class),
                new IntegerTypesComparator<>(Integer.TYPE, Integer.class),
                new IntegerTypesComparator<>(Integer.TYPE, Long.class),
                new IntegerTypesComparator<>(Integer.class, Long.class),
                new IntFloatTypesComparator<>(Integer.TYPE, Float.class),
                new IntFloatTypesComparator<>(Integer.class, Float.class),
                new IntFloatTypesComparator<>(Integer.TYPE, Double.class),
                new IntFloatTypesComparator<>(Integer.class, Double.class),
                new IntegerTypesComparator<>(Integer.TYPE, Byte.TYPE),
                new IntegerTypesComparator<>(Integer.class, Byte.TYPE),
                new IntegerTypesComparator<>(Integer.TYPE, Short.TYPE),
                new IntegerTypesComparator<>(Integer.class, Short.TYPE),
                new IntegerTypesComparator<>(Integer.class, Integer.TYPE),
                new IntegerTypesComparator<>(Integer.TYPE, Long.TYPE),
                new IntegerTypesComparator<>(Integer.class, Long.TYPE),
                new IntFloatTypesComparator<>(Integer.TYPE, Float.TYPE),
                new IntFloatTypesComparator<>(Integer.class, Float.TYPE),
                new IntFloatTypesComparator<>(Integer.TYPE, Double.TYPE),
                new IntFloatTypesComparator<>(Integer.class, Double.TYPE),

        /* Long */
                new SameTypeComparator<>(Long.class),
                new SameTypeComparator<>(Long.TYPE),
                new IntegerTypesComparator<>(Long.TYPE, Byte.class),
                new IntegerTypesComparator<>(Long.class, Byte.class),
                new IntegerTypesComparator<>(Long.TYPE, Short.class),
                new IntegerTypesComparator<>(Long.class, Short.class),
                new IntegerTypesComparator<>(Long.TYPE, Integer.class),
                new IntegerTypesComparator<>(Long.class, Integer.class),
                new IntegerTypesComparator<>(Long.TYPE, Long.class),
                new IntFloatTypesComparator<>(Long.TYPE, Float.class),
                new IntFloatTypesComparator<>(Long.class, Float.class),
                new IntFloatTypesComparator<>(Long.TYPE, Double.class),
                new IntFloatTypesComparator<>(Long.class, Double.class),
                new IntegerTypesComparator<>(Long.TYPE, Byte.TYPE),
                new IntegerTypesComparator<>(Long.class, Byte.TYPE),
                new IntegerTypesComparator<>(Long.TYPE, Short.TYPE),
                new IntegerTypesComparator<>(Long.class, Short.TYPE),
                new IntegerTypesComparator<>(Long.TYPE, Integer.TYPE),
                new IntegerTypesComparator<>(Long.class, Integer.TYPE),
                new IntegerTypesComparator<>(Long.class, Long.TYPE),
                new IntFloatTypesComparator<>(Long.TYPE, Float.TYPE),
                new IntFloatTypesComparator<>(Long.class, Float.TYPE),
                new IntFloatTypesComparator<>(Long.TYPE, Double.TYPE),
                new IntFloatTypesComparator<>(Long.class, Double.TYPE),

        /* Float */
                new SameTypeComparator<>(Float.class),
                new SameTypeComparator<>(Float.TYPE),
                new IntFloatTypesComparator<>(Float.class, Byte.class),
                new IntFloatTypesComparator<>(Float.TYPE, Byte.class),
                new IntFloatTypesComparator<>(Float.class, Short.class),
                new IntFloatTypesComparator<>(Float.TYPE, Short.class),
                new IntFloatTypesComparator<>(Float.class, Integer.class),
                new IntFloatTypesComparator<>(Float.TYPE, Integer.class),
                new IntFloatTypesComparator<>(Float.class, Long.class),
                new IntFloatTypesComparator<>(Float.TYPE, Long.class),
                new FloatTypesComparator<>(Float.class, Float.TYPE),
                new FloatTypesComparator<>(Float.TYPE, Float.class),
                new FloatTypesComparator<>(Float.TYPE, Double.class),
                new FloatTypesComparator<>(Float.class, Double.TYPE),
                new FloatTypesComparator<>(Float.TYPE, Double.TYPE),
                new FloatTypesComparator<>(Float.class, Double.class),

        /* Double */
                new SameTypeComparator<>(Double.class),
                new SameTypeComparator<>(Double.TYPE),
                new IntFloatTypesComparator<>(Double.class, Byte.class),
                new IntFloatTypesComparator<>(Double.TYPE, Byte.class),
                new IntFloatTypesComparator<>(Double.class, Short.class),
                new IntFloatTypesComparator<>(Double.TYPE, Short.class),
                new IntFloatTypesComparator<>(Double.class, Integer.class),
                new IntFloatTypesComparator<>(Double.TYPE, Integer.class),
                new IntFloatTypesComparator<>(Double.class, Long.class),
                new IntFloatTypesComparator<>(Double.TYPE, Long.class),
                new FloatTypesComparator<>(Double.class, Double.TYPE),
                new FloatTypesComparator<>(Double.TYPE, Double.class),
                new FloatTypesComparator<>(Double.class, Float.class),
                new FloatTypesComparator<>(Double.class, Float.TYPE),
                new FloatTypesComparator<>(Double.TYPE, Float.class),
                new FloatTypesComparator<>(Double.TYPE, Float.TYPE),

        /* Chars */
                new SameTypeComparator<>(Character.class),
                new SameTypeComparator<>(Character.TYPE),
                new CharacterTypesComparator<>(Character.TYPE, Character.class),
                new CharacterTypesComparator<>(Character.class, Character.TYPE),

         /* Boolean */
                new SameTypeComparator<>(Boolean.class),
                new SameTypeComparator<>(Boolean.TYPE),
                new BooleanTypesComparator<>(Boolean.TYPE, Boolean.class),
                new BooleanTypesComparator<>(Boolean.class, Boolean.TYPE)
        ).collect(Collectors.toMap(c -> new TypesKey(c.type1(), c.type2()), c -> c)));
    }

    /* ------------------------------------------------------------------------------------------------------------- */

    public static int version() {
        return VERSION;
    }


    public static boolean objectTypesCompatible(Object o1, Object o2) {
        return (o1 == null && o2 == null) ||
                (o1 != null && o2 != null && COMPATIBLE_TYPES.containsKey(new TypesKey(o1.getClass(), o2.getClass())));
    }

    @SuppressWarnings("unchecked")
    public static boolean compatibleEquals(Object o1, Object o2) {
        if (o1 == null && o2 == null) {
            return true;
        } else if ((o1 == null) != (o2 == null)) {
            return false;
        }

        return COMPATIBLE_TYPES.getOrDefault(new TypesKey(o1.getClass(), o2.getClass()), NEVER_COMPARED).isEqualTo(o1, o2);
    }

    /* --------------------------------------------------- privates ------------------------------------------------ */

}
