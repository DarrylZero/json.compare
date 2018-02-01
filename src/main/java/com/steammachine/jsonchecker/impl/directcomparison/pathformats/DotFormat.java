package com.steammachine.jsonchecker.impl.directcomparison.pathformats;

import com.steammachine.jsonchecker.types.Path;
import com.steammachine.common.lazyeval.LazyEval;
import com.steammachine.common.utils.commonutils.CommonUtils;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Created by Vladimir Bogodukhov on 01.12.2017.
 *
 * @author Vladimir Bogodukhov
 */
public class DotFormat implements PathFormat {

    private static class AuxParam {
        private final boolean universal;
        private final String data;
        private final boolean first;
        private final boolean last;

        private AuxParam(boolean universal, boolean first, boolean last, String data) {
            this.universal = universal;
            this.first = first;
            this.last = last;
            this.data = Objects.requireNonNull(data);
        }

        public boolean first() {
            return first;
        }

        public boolean last() {
            return last;
        }

        public boolean universal() {
            return universal;
        }

        public String data() {
            return data;
        }

        @Override
        public String toString() {
            return "AuxParam(universal=" + universal + ", data=" + data + ", first=" + first + ", last=" + last + ")";
        }
    }

    private enum MatcherId {
        POINT,
        NUMBER,
        ASTERIX,
        TWO_ASTERIXES,
        BRACKETS_GROUP
    }

    private static final String AST = Pattern.quote("*");
    private static final char LEFT_BRACKET = '[';
    private static final char RIGHT_BRACKET = ']';
    private static final String LB = Pattern.quote("" + LEFT_BRACKET);
    private static final String RB = Pattern.quote("" + RIGHT_BRACKET);
    private static final String POINT = Pattern.quote(".");
    private static final LazyEval<Map<MatcherId, Pattern>> PATTERNS = LazyEval.eval(() -> {
        Map<MatcherId, Pattern> map = new EnumMap<>(MatcherId.class);
        map.put(MatcherId.POINT, Pattern.compile(POINT));
        map.put(MatcherId.NUMBER, Pattern.compile("^\\d+$"));
        map.put(MatcherId.ASTERIX, Pattern.compile(AST));
        map.put(MatcherId.TWO_ASTERIXES, Pattern.compile(AST + AST));
        map.put(MatcherId.BRACKETS_GROUP, Pattern.compile(LB + "([.&&[^" + LB + "]]&&[^" + RB + "])*" + RB)); /* Все, что находится между скобками */

        return Collections.unmodifiableMap(map);
    });

    @Override
    public String name() {
        return "dot";
    }

    @Override
    public boolean checkPathFormat(String path) {
        final boolean universal;
        final String data;
        if (path.startsWith("**.")) {
            data = path.substring("**.".length());
            universal = true;
        } else if (path.startsWith("***.")) {
            data = path.substring("***.".length());
            universal = true;
        } else {
            data = path;
            universal = false;
        }

        String[] items = data.split(pattern(MatcherId.POINT).pattern());
        return !(path.startsWith(".") || path.endsWith(".")) &&
                items.length != 0 &&
                Stream.iterate(0, i -> i + 1).limit(items.length).
                        map(i -> new AuxParam(universal, i == 0, i == items.length - 1, items[i])).allMatch(DotFormat::isMatched);
    }

    @Override
    public Path parsePath(String path) {
        CommonUtils.check(() -> checkPathFormat(path), () -> new IllegalStateException("path " + path + " is not valid"));
        String currPath = path;

        Path.Builder pathBuilder = Path.builder();
        final boolean universal;
        if (currPath.startsWith("**.")) {
            currPath = path.substring("**.".length());
            pathBuilder.ast().ast().del();
            universal = true;
        } else if (path.startsWith("***.")) {
            currPath = path.substring("***.".length());
            pathBuilder.ast().ast().ast().del();
            universal = true;
        } else {
            universal = false;
        }


        String[] items = currPath.split(pattern(MatcherId.POINT).pattern());
        Stream.iterate(0, i -> i + 1).limit(items.length).map(i -> new AuxParam(universal, i == 0, i == items.length - 1, items[i])).
                sequential().forEach(t -> auxToPath(t, pathBuilder));

        return pathBuilder.get();
    }

    @Override
    public boolean isApplied(Path path, Path templatePath) {
        return PathApplication.isPathApplied(path, templatePath);
    }

    /* -------------------------------------------------- privates ------------------------------------------------- */

    private static boolean anyMatch(String data, MatcherId... ids) {
        return Stream.of(ids).anyMatch(i -> pattern(i).matcher(data).matches());
    }

    private static boolean isValidName(String item) {
        return item.chars().allMatch((cp) -> Character.isJavaIdentifierPart(cp) || cp == ':');
    }

    private static void auxToPath(AuxParam param, Path.Builder builder) {
        /* правильность пути проверяется до вызова. */

        if (!param.data().contains("" + LEFT_BRACKET)) {
            processNoArrayItem(param, builder);
        } else {

            String prefix = param.data().substring(0, param.data().indexOf(LEFT_BRACKET));
            if (prefix.isEmpty()) {
                /* Префикса нет - есть только элементы массива     [1]/[2]/[3]   */
                /*                                              ** / [1]/[2]/[3] */
                /*                                             *** / [1]/[2]/[3] */

                int index = 0;
                while (index < param.data().length()) {
                    int lbIndex = param.data().indexOf(LEFT_BRACKET, index);
                    int rbIndex = param.data().indexOf("]", lbIndex + 1);
                    String inBrackets = param.data().substring(lbIndex + 1, rbIndex);
                    if (index != 0 || !param.universal() || !param.first()) {
                        builder.del();
                    }

                    if (anyMatch(inBrackets, MatcherId.ASTERIX)) {
                        builder.anyArr();
                    } else if (anyMatch(inBrackets, MatcherId.NUMBER)) {
                        builder.arr(Integer.parseInt(inBrackets));
                    } else {
                        throw new IllegalStateException();
                    }
                    index = rbIndex + 1;
                }


            } else {
                /* Есть элемент массива и префикс             a[1]/[2]/[3]  */
                /*                                        ** /a[1]/[2]/[3]  */
                /*                                       *** /a[1]/[2]/[3]  */
                /*                                       *** /a[1]/[2]/[3]  */

                {
                    final boolean firstDelimeterNeeded;
                    if (param.first() && param.universal()) {
                        firstDelimeterNeeded = false;
                    } else if (!param.first() && param.universal()) {
                        firstDelimeterNeeded = true;
                    } else if (param.first() && !param.universal()) {
                        firstDelimeterNeeded = false;
                    } else if (!param.first() && !param.universal()) {
                        firstDelimeterNeeded = true;
                    } else {
                        firstDelimeterNeeded = false;
                    }
                    if (firstDelimeterNeeded) builder.del();
                }

                builder.obj(prefix);

                int index = 0;
                while (index < param.data().length()) {
                    int lbIndex = param.data().indexOf(LEFT_BRACKET, index);
                    int rbIndex = param.data().indexOf("]", lbIndex + 1);
                    String inBrackets = param.data().substring(lbIndex + 1, rbIndex);

                    builder.del(); /* потому что мы в данном случае всегда добавляем  */
                    if (anyMatch(inBrackets, MatcherId.ASTERIX)) {
                        builder.anyArr();
                    } else if (anyMatch(inBrackets, MatcherId.NUMBER)) {
                        builder.arr(Integer.parseInt(inBrackets));
                    } else {
                        throw new IllegalStateException();
                    }
                    index = rbIndex + 1;
                }
            }
        }
    }

    private static void processNoArrayItem(AuxParam param, Path.Builder builder) {
        if (!param.first()) {
            builder.del();
        }

        if (isValidName(param.data())) {
            builder.obj(param.data());
        } else if (anyMatch(param.data(), MatcherId.ASTERIX)) {
            builder.ast();
        } else if (param.last() && anyMatch(param.data(), MatcherId.TWO_ASTERIXES)) {
            builder.ast().ast();
        } else {
            throw new IllegalStateException();
        }
    }


    private static boolean isMatched(AuxParam element) {
        if (element.data().trim().isEmpty()) {
            return false;
        } else if (!element.data().contains("" + LEFT_BRACKET)) {
            return isCorrectItem(element);
        } else {
            return isCorrectArrayItem(element);
        }
    }

    private static boolean isCorrectItem(AuxParam element) {
        if (element.last()) {
            return anyMatch(element.data(), MatcherId.TWO_ASTERIXES) ||
                    anyMatch(element.data(), MatcherId.ASTERIX) ||
                    isValidName(element.data());
        } else {
            return anyMatch(element.data(), MatcherId.ASTERIX) ||
                    isValidName(element.data());
        }
    }

    private static boolean isCorrectArrayItem(AuxParam auxParam) {
/*
         b[1][2][*]
         ***.[*]
         **.[*]
*/
        if (auxParam.data().indexOf(LEFT_BRACKET) == 0) {
            /*   [1]      */
            /*   a.[1]    */
            /*   **.[1]   */
            /*   ***.[1]  */

            if (!auxParam.universal()) return false;
            if (!auxParam.first()) return false;
        }
        if (auxParam.data().indexOf("]") - auxParam.data().indexOf(LEFT_BRACKET) < 2) return false;
        if (!auxParam.data().endsWith("]")) return false;

        if (!isValidName(auxParam.data().substring(0, auxParam.data().indexOf(LEFT_BRACKET)))) {
            return false;
        }

        boolean result = true;
        int index = 0;
        while (index < auxParam.data().length()) {
            int lbIndex = auxParam.data().indexOf(LEFT_BRACKET, index);
            if (lbIndex < 0) return false;
            int rbIndex = auxParam.data().indexOf("]", lbIndex + 1);
            if (rbIndex < 0) return false;
            index = rbIndex + 1;
            String inBrackets = auxParam.data().substring(lbIndex + 1, rbIndex);
            result = result && anyMatch(inBrackets, MatcherId.NUMBER, MatcherId.ASTERIX);
        }
        return result;
    }

    private static boolean isCorrectArrayItem(String data) {
        // b[1][2][*]
        if (data.indexOf(LEFT_BRACKET) == 0) return false;
        if (!data.endsWith("]")) return false;
        if (!isValidName(data.substring(0, data.indexOf(LEFT_BRACKET)))) {
            return false;
        }

        boolean result = true;
        int index = 0;
        while (index < data.length()) {
            int lbIndex = data.indexOf(LEFT_BRACKET, index);
            if (lbIndex < 0) return false;
            int rbIndex = data.indexOf("]", lbIndex + 1);
            if (rbIndex < 0) return false;
            index = rbIndex + 1;
            String inBrackets = data.substring(lbIndex + 1, rbIndex);
            result = result && anyMatch(inBrackets, MatcherId.NUMBER, MatcherId.ASTERIX);
        }
        return result;
    }

    private static Pattern pattern(MatcherId matcherId) {
        return PATTERNS.value().get(matcherId);
    }


}
