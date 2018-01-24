package com.steammachine.jsonchecker.utils.compatibletypescomparator.ver2;

import java.util.Arrays;
import java.util.Objects;

import static com.steammachine.common.utils.commonutils.CommonUtils.check;

/**
 * 30.12.2017 10:21:46
 *
 * @author Vladimir Bogodukhov
 **/
public class Comparators {

    private Comparators() {
        /**/
    }

    private static final Class[] INTS = {Byte.class, Byte.TYPE, Short.class, Short.TYPE,
            Integer.class, Integer.TYPE, Long.class, Long.TYPE};
    private static final Class[] FLOATS = {Float.class, Float.TYPE, Double.class, Double.TYPE};

    public static class SameTypeComparator<T> implements CompareTo<T, T> {
        private final Class<T> t;

        public SameTypeComparator(Class<T> t) {
            this.t = Objects.requireNonNull(t);
        }

        @Override
        public Class<T> type1() {
            return t;
        }

        @Override
        public Class<T> type2() {
            return t;
        }

        @Override
        public boolean isEqualTo(T value1, T value2) {
            return Objects.equals(value1, value2);
        }
    }

    public static class IntegerTypesComparator<T1, T2> implements CompareTo<T1, T2> {
        private final Class<T1> type1;
        private final Class<T2> type2;

        public IntegerTypesComparator(Class<T1> type1, Class<T2> type2) {
            this.type1 = check(() -> type1, Comparators::isInt, IllegalStateException::new);
            this.type2 = check(() -> type2, Comparators::isInt, IllegalStateException::new);
        }

        @Override
        public Class<T1> type1() {
            return type1;
        }

        @Override
        public Class<T2> type2() {
            return type2;
        }

        @Override
        public boolean isEqualTo(T1 value1, T2 value2) {
            return compareInts(value1, value2);
        }
    }

    public static class FloatTypesComparator<T1, T2> implements CompareTo<T1, T2> {
        private final Class<T1> type1;
        private final Class<T2> type2;

        public FloatTypesComparator(Class<T1> type1, Class<T2> type2) {
            this.type1 = check(() -> type1, Comparators::isFloat, IllegalStateException::new);
            this.type2 = check(() -> type2, Comparators::isFloat, IllegalStateException::new);
        }

        @Override
        public Class<T1> type1() {
            return type1;
        }

        @Override
        public Class<T2> type2() {
            return type2;
        }

        @Override
        public boolean isEqualTo(T1 value1, T2 value2) {
            return compareFloats(value1, value2);
        }
    }


    public static class IntFloatTypesComparator<T1, T2> implements CompareTo<T1, T2> {
        private final Class<T1> type1;
        private final Class<T2> type2;

        public IntFloatTypesComparator(Class<T1> type1, Class<T2> type2) {
            this.type1 = check(() -> type1, t -> isInt(t) || isFloat(t), IllegalStateException::new);
            this.type2 = check(() -> type2, t -> isInt(t) || isFloat(t), IllegalStateException::new);
        }

        @Override
        public Class<T1> type1() {
            return type1;
        }

        @Override
        public Class<T2> type2() {
            return type2;
        }

        @Override
        public boolean isEqualTo(T1 value1, T2 value2) {
            return compareIntsAndFloat(value1, value2);
        }

        private static boolean compareIntsAndFloat(Object o1, Object o2) {
            if (o1 == null && o2 == null) return true;
            if ((o1 == null) != (o2 == null)) return false;

            if (isInt(o1.getClass()) && isInt(o2.getClass())) {
                return compareInts(o1, o2);
            } else if (isFloat(o1.getClass()) && isFloat(o2.getClass())) {
                return compareFloats(o1, o2);
            } else if (isInt(o1.getClass()) && isFloat(o2.getClass())) {
                return Objects.equals(normalizedNumString(o1), normalizedNumString(o2));
            } else if (isFloat(o1.getClass()) && isInt(o2.getClass())) {
                return Objects.equals(normalizedNumString(o1), normalizedNumString(o2));
            }

            throw new IllegalStateException();
        }

        private static String normalizedNumString(Object o) {
            Objects.requireNonNull(o);
            check(() -> isInt(o.getClass()) || isFloat(o.getClass()), IllegalStateException::new);
            if (isInt(o.getClass())) {
                return "" + toLong(o);
            }

            if (isFloat(o.getClass())) {

                double d = toDouble(o);
                if (isFrac(d)) {
                    return toDoubleString(o);
                } else {
                    return "" + toLong(wholePart(d));
                }
            }
            throw new IllegalStateException();
        }

        private static long wholePart(double d) {
            return (long) d;
        }

        private static boolean isFrac(double d) {
            return d % 1 != 0;
        }

        private static double toDouble(Object o) {
            if (o.getClass() == Double.class || o.getClass() == Double.TYPE) {
                return (double) o;
            }
            if (o.getClass() == Float.class || o.getClass() == Float.TYPE) {
                return (double) (float) o;
            }
            throw new IllegalStateException();
        }
    }


    public static class CharacterTypesComparator<T1, T2> implements CompareTo<T1, T2> {
        private final Class<T1> type1;
        private final Class<T2> type2;

        public CharacterTypesComparator(Class<T1> type1, Class<T2> type2) {
            check(() -> type1 == Character.TYPE || type1 == Character.class, IllegalStateException::new);
            check(() -> type2 == Character.TYPE || type2 == Character.class, IllegalStateException::new);
            this.type1 = Objects.requireNonNull(type1);
            this.type2 = Objects.requireNonNull(type2);
        }

        @Override
        public Class<T1> type1() {
            return type1;
        }

        @Override
        public Class<T2> type2() {
            return type2;
        }

        @Override
        public boolean isEqualTo(T1 value1, T2 value2) {
            return compareChars(value1, value2);
        }

        private static boolean compareChars(Object o1, Object o2) {
            if (o1 == null && o2 == null) return true;
            return (o1 != null && o2 != null && toChars(o1) == toChars(o2));
        }

        private static char toChars(Object o) {
            if (o.getClass() == Character.class || o.getClass() == Character.TYPE) {
                return (char) o;
            }
            throw new IllegalStateException();
        }


    }

    public static class BooleanTypesComparator<T1, T2> implements CompareTo<T1, T2> {
        private final Class<T1> type1;
        private final Class<T2> type2;

        public BooleanTypesComparator(Class<T1> type1, Class<T2> type2) {
            check(() -> type1 == Boolean.TYPE || type1 == Boolean.class, IllegalStateException::new);
            check(() -> type2 == Boolean.TYPE || type2 == Boolean.class, IllegalStateException::new);
            this.type1 = Objects.requireNonNull(type1);
            this.type2 = Objects.requireNonNull(type2);
        }

        @Override
        public Class<T1> type1() {
            return type1;
        }

        @Override
        public Class<T2> type2() {
            return type2;
        }

        @Override
        public boolean isEqualTo(T1 value1, T2 value2) {
            return compareBooleans(value1, value2);
        }

        private static boolean compareBooleans(Object o1, Object o2) {
            if (o1 == null && o2 == null) return true;
            return (o1 != null && o2 != null && toBoolean(o1) == toBoolean(o2));
        }

        private static boolean toBoolean(Object o) {
            if (o.getClass() == Boolean.class || o.getClass() == Boolean.TYPE) {
                return (boolean) o;
            }
            throw new IllegalStateException();
        }
    }

    public static final CompareTo<Object, Object> NEVER_COMPARED = new CompareTo<Object, Object>() {
        @Override
        public Class<Object> type1() {
            return Object.class;
        }

        @Override
        public Class<Object> type2() {
            return Object.class;
        }

        @Override
        public boolean isEqualTo(Object value1, Object value2) {
            return false;
        }

        @Override
        public String toString() {
            return "NEVER COMPARED";
        }
    };



    private static boolean compareInts(Object o1, Object o2) {
        if (o1 == null && o2 == null) return true;
        return (o1 != null && o2 != null && toLong(o1) == toLong(o2));
    }

    private static boolean compareFloats(Object o1, Object o2) {
        if (o1 == null && o2 == null) return true;
        return (o1 != null && o2 != null && Objects.equals(toDoubleString(o1), toDoubleString(o2)));
    }

    private static String toDoubleString(Object o) {
        if (o.getClass() == Double.class || o.getClass() == Double.TYPE) {
            return "" + o;
        }
        if (o.getClass() == Float.class || o.getClass() == Float.TYPE) {
            return "" + (double) (float) o;
        }
        throw new IllegalStateException();
    }

    private static long toLong(Object o) {
        if (o.getClass() == Long.class || o.getClass() == Long.TYPE) {
            return (long) o;
        }
        if (o.getClass() == Integer.class || o.getClass() == Integer.TYPE) {
            return (long) (int) o;
        }
        if (o.getClass() == Short.class || o.getClass() == Short.TYPE) {
            return (long) (short) o;
        }
        if (o.getClass() == Byte.class || o.getClass() == Byte.TYPE) {
            return (long) (byte) o;
        }
        throw new IllegalStateException();
    }


    private static boolean isInt(Class clazz) {
        return Arrays.stream(INTS).anyMatch(c -> c == clazz);
    }

    private static boolean isFloat(Class clazz) {
        return Arrays.stream(FLOATS).anyMatch(c -> c == clazz);
    }

}
