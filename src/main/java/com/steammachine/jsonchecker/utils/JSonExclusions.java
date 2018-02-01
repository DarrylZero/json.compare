package com.steammachine.jsonchecker.utils;


import com.steammachine.common.definitions.annotations.SignatureSensitive;
import com.steammachine.jsonchecker.defaults.DefaultWidgetNodeChecker;
import com.steammachine.jsonchecker.impl.internal.Association;
import com.steammachine.jsonchecker.impl.internal.DefaultAssociation;
import com.steammachine.jsonchecker.types.Identity;
import com.steammachine.jsonchecker.types.SqNodeKeyType;
import com.steammachine.jsonchecker.types.TreeNode;
import com.steammachine.jsonchecker.types.exceptions.PathError;

import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Класс для фильтрации исключений
 * <p>
 * 30.12.2017 10:21:46
 *
 * @author Vladimir Bogodukhov
 * @deprecated is about to be deleted
 **/
@Deprecated
public class JSonExclusions {


    private static class ProcessedResult {
        private final boolean processed;
        private final TreeNode treeNode;

        ProcessedResult(boolean processed, TreeNode treeNode) {
            this.processed = processed;
            this.treeNode = Objects.requireNonNull(treeNode);
        }

        public boolean processed() {
            return processed;
        }

        public TreeNode treeNode() {
            return treeNode;
        }
    }

    private static class CollectsFullNodeNameResult {
        private final TreeNode treeNode;
        private final List<TreeNode> fullPath;

        CollectsFullNodeNameResult(List<TreeNode> fullPath, TreeNode treeNode) {
            this.fullPath = Objects.requireNonNull(fullPath);
            this.treeNode = Objects.requireNonNull(treeNode);
            fullPath.forEach(Objects::requireNonNull);
        }

        public TreeNode treeNode() {
            return treeNode;
        }

        public String fullPath() {
            return fullPath.stream().
                    map(TreeNode::object).
                    map(CollectsFullNodeNameResult::objectIdentName).
                    map(on -> "/" + on).
                    collect(Collectors.joining());
        }

        private static String objectIdentName(Object o) {
            if (!(o instanceof Identity)) {
                return "?";
            }
            //noinspection unchecked
            if (!((Identity) o).supports(SqNodeKeyType.class)) {
                return "?";
            }
            //noinspection unchecked
            Identity<SqNodeKeyType> identity = (Identity<SqNodeKeyType>) o;

            final String identifier;
            if (identity.ident(SqNodeKeyType.monkeyId) != null) {
                identifier = identity.ident(SqNodeKeyType.monkeyId);
            } else if (identity.ident(SqNodeKeyType.componentType) != null) {
                identifier = "@" + identity.ident(SqNodeKeyType.componentType);
            } else {
                identifier = "?";
            }
            return identifier;
        }
    }


    /**
     * формат данных
     * <p>
     * Начало строки
     * 1 / - обязательная часть
     * 2 * ИЛИ латинские буквы и цифры + / - группа повторяется нескольно раз.
     * 3 * - одна или две звездочки ИЛИ латинские буквы и цифры.
     * <p>
     * <p>
     * <p>
     * ^[/]
     * <p>
     * ([*]|[@]?([a-zA-Z0-9]+)[/])*
     * <p>
     * (([*]{1,2})|([@]?[a-zA-Z0-9]+))$
     */

    private static final String SLASH = "/";

    private static final Pattern ITEM_PATTERN = Pattern.compile("^([*]|([@]?[a-zA-Z0-9_]+))$");
    private static final Predicate<String> ITEM_PATTERN_PREDICATE = ITEM_PATTERN.asPredicate();
    private static final Predicate<String> ITEM_PATTERN_FILTER = ITEM_PATTERN_PREDICATE.negate();

    private static final Pattern LAST_ITEM_PATTERN = Pattern.compile("^([*]{1,2}|([@]?[a-zA-Z0-9_]+))$");
    private static final Predicate<String> LAST_ITEM_PREDICATE = LAST_ITEM_PATTERN.asPredicate();
    private static final Predicate<String> LAST_ITEM_FILTER = LAST_ITEM_PREDICATE.negate();


    /**
     * Получить объект ассоциаци каждой ноды с признком ее обработки {@code true} или не обработки {@code false}.
     *
     * @param root       объект корневой ноды (всегда не null)
     * @param exclusions список исключений (всегда не null)
     * @return объект ассоциаций
     */
    public static Association<Boolean> collectProcessed(TreeNode root, Collection<String> exclusions) {
        Objects.requireNonNull(root);
        Objects.requireNonNull(exclusions);

        List<List<TreeNode>> list = new ArrayList<>();
        list.add(Collections.singletonList(root));
        int index = 0;
        while (index < list.size()) {
            List<TreeNode> node = list.get(index);
            Iterator<TreeNode> children = node.get(node.size() - 1).children();

            while (children.hasNext()) {
                List<TreeNode> treeNodes = new ArrayList<>();
                treeNodes.addAll(node);
                treeNodes.add(children.next());

                list.add(treeNodes);
            }
            index++;
        }

        return list.stream().
//        peek(l -> debugInspection(exclusions, l)).
//        peek(l -> debug(isNodeProcessed(l, exclusions), l, exclusions) ).
        map(l -> new ProcessedResult(isNodeProcessed(l, exclusions), last(l))).
                        <DefaultAssociation<Boolean>>collect(DefaultAssociation::new,
                                (c, r) -> c.associate(r.treeNode().object(), r.processed()), DefaultAssociation::mergeWith);
    }

    private static void debug(boolean result, List<TreeNode> node, Collection<String> exclusions) {
        String debugName = debugName(node);

        isNodeProcessed(node, exclusions);
    }

    private static String debugName(List<TreeNode> node) {
        return node.stream().map(tr -> (DefaultWidgetNodeChecker) tr.object()).map(i -> "/" + i.monkeyId()).
                collect(Collectors.joining());
    }

    private static void debugInspection(Collection<String> exclusions, List<TreeNode> l) {
        ProcessedResult result = new ProcessedResult(isNodeProcessed(l, exclusions), last(l));
//        System.out.println("" + result.treeNode.object() + "  " + result.processed());

    }


    /**
     * Получить ассоциацию объекта и его пути
     *
     * @param root - Коренб дерева
     * @return ассоциация объекта и его пути
     */
    public static Association<String> collectNodeNames(TreeNode root) {
        List<List<TreeNode>> list = new ArrayList<>();
        list.add(Collections.singletonList(root));
        int index = 0;
        while (index < list.size()) {
            List<TreeNode> node = list.get(index);
            Iterator<TreeNode> children = node.get(node.size() - 1).children();

            while (children.hasNext()) {
                List<TreeNode> treeNodes = new ArrayList<>();
                treeNodes.addAll(node);
                treeNodes.add(children.next());

                list.add(treeNodes);
            }
            index++;
        }

        return list.stream().map(l -> new CollectsFullNodeNameResult(l, last(l))).<DefaultAssociation<String>>
                collect(DefaultAssociation::new, (a, r) -> a.associate(r.treeNode().object(), r.fullPath()),
                DefaultAssociation::mergeWith);
    }



    /* ------------------------------------------ privates ---------------------------------------------------------*/


    private static <T> T last(List<T> list) {
        Objects.requireNonNull(list);
        return list.get(list.size() - 1);
    }

    /**
     * Возвращает признак - будет или нет обрабатываться нода.
     *
     * @param fullNode   нода с полным путем
     * @param exclusions разобранные исключения
     * @return {@code true} если нода обрабатывается - {@code false} если нет
     */
    private static boolean isNodeProcessed(List<TreeNode> fullNode, Collection<String> exclusions) {
        Objects.requireNonNull(fullNode);
        Objects.requireNonNull(exclusions);
        if (fullNode.isEmpty()) {
            throw new IllegalStateException("list must not be empty");
        }
        fullNode.forEach(Objects::requireNonNull);
        exclusions.stream().filter(i -> !isCorrectPath(i)).findAny().ifPresent(JSonExclusions::throwPathErrorWrongFormat);

        /* Нода обрабатывается если к ней не применимо ни одно исключение. */
        return exclusions.stream().noneMatch(exclusion -> isExclusionApplied(fullNode, exclusion));
    }

    private static void throwPathErrorWrongFormat(String path) {
        throw new PathError("path " + path + " has wrong format");
    }

    /**
     * Не получилось сделать "правильным" методом с использованием regexp - делаем в лоб
     * Проверить является ли путь правильным.
     *
     * @param path - путь
     * @return {@code true} в случае если соответствует
     * <p>
     */
    @SignatureSensitive
    private static boolean isCorrectPath(String path) {
        Objects.requireNonNull(path);
        if (path.length() < 2) {
            return false;
        }
        if (!Objects.equals(SLASH, "" + path.charAt(0))) {
            return false;
        }

        List<String> parts = getParts(path);

        if (!parts.get(0).isEmpty()) {
            return false;
        }
        if (LAST_ITEM_FILTER.test(parts.get(parts.size() - 1))) {
            return false;
        }

        for (int i = 1; i < parts.size() - 1; i++) { /* от второго до препоследнего элемента включительно */
            if (ITEM_PATTERN_FILTER.test(parts.get(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * проверить что данное исключение применимо к узлу ноды.
     *
     * @param fullNode  - Нода
     * @param exclusion - исключение
     * @return - {@code true} если исключение применимо к узлу ноды.
     * {@code false} если исключение НЕ применимо к узлу ноды.
     */
    @SignatureSensitive
    private static boolean isExclusionApplied(List<TreeNode> fullNode, String exclusion) {
        return isExclusionApplicable(extractData(exclusion), fullNode);
    }

    private static List<String> getParts(String path) {
        String temp = path;
        List<String> parts = new ArrayList<>();
        while (temp != null) {
            int indexOfSlash = temp.indexOf(SLASH);

            final String part;
            if (indexOfSlash < 0) {
                part = temp;
                temp = null;
            } else {
                part = temp.substring(0, indexOfSlash);
                temp = temp.substring(indexOfSlash + SLASH.length(), temp.length());
            }
            parts.add(part);
        }
        return parts;
    }

    @SignatureSensitive
    private static List<String> extractData(String path) {
        List<String> parts = getParts(path);
        parts.remove(0);
        return parts;
    }


    private enum ParentState {
        NOT_INITED,
        IDENT,
        ASTERIX,
        TWO_ASTERIX;


        public static ParentState switchTo(ParentState oldValue, ParentState newValue) {
            Objects.requireNonNull(oldValue);
            Objects.requireNonNull(newValue);
            return oldValue == TWO_ASTERIX ? TWO_ASTERIX : newValue;
        }

    }

    /**
     * проверить, что данное исключение применимо к данному узлу ноды.
     *
     * @param parsedExclusions разобраннное
     * @param fullNode         - узел ноды
     * @return - {@code true} если исключение применимо к узлу ноды.
     * {@code false} если исключение НЕ применимо к узлу ноды.
     */
    private static boolean isExclusionApplicable(List<String> parsedExclusions, List<TreeNode> fullNode) {
        Objects.requireNonNull(parsedExclusions);
        Objects.requireNonNull(fullNode);
        final int minSize = parsedExclusions.size() < fullNode.size() ? parsedExclusions.size() : fullNode.size();

        ParentState parentState = ParentState.NOT_INITED;
        for (int i = 0; i < minSize; i++) {
            String pathItem = parsedExclusions.get(i);
            switch (pathItem) {
                case "*": {
                    parentState = ParentState.switchTo(parentState, ParentState.ASTERIX);
                    break;
                }

                case "**": {
                    parentState = ParentState.switchTo(parentState, ParentState.TWO_ASTERIX);
                    break;
                }

                default: {
                    switch (parentState) {
                        case NOT_INITED:
                        case IDENT: {
                            Object object = fullNode.get(i).object();
                            if (!(object instanceof Identity)) {
                                return false;
                            }

                            /* Тут - работаем только с объектами типа Identity */
                            //noinspection unchecked
                            if (!((Identity) fullNode.get(i).object()).supports(SqNodeKeyType.class)) {
                                return false;
                            }
                            //noinspection unchecked
                            Identity<SqNodeKeyType> identity = (Identity<SqNodeKeyType>) fullNode.get(i).object();

                            /* Тут - работаем только с объектами типа Identity, которые поддерживают нужный тип ключа.*/
                            if (!identifies(identity, getIdentityType(pathItem), getIdentity(pathItem))) {
                                /*   Если объект не является - "не идентифицируется" текущим элементом считаем,
                                что исключение не применимо */
                                return false;
                            }
                            break;
                        }

                        case ASTERIX:
                        case TWO_ASTERIX: {
                            /* */
                            break;
                        }

                        default: {
                            throw new IllegalStateException();
                        }
                    }
                    parentState = ParentState.switchTo(parentState, ParentState.IDENT);
                }
            }
        }

        if (parsedExclusions.size() == fullNode.size()) {
/*
            Ситуация - когда размер исключения в точности равен размеру ноды.
            Если это так и до этого моменты мы не вышли с результатом - считаем что данный {@code true}.

*/
            return true;
        } else if (parsedExclusions.size() > fullNode.size()) {
/*
            Ситуация с остатком "хвостом" исключения. Когда нода "короче" прилагаемого исключения.

            Координаты ноды - /node1/node2/node3/node5
            Исключение        /node1/node2/node3/*                      - true
            Исключение        /node1/node2/node3/**                     - true
            Исключение        /node1/node2/node3/node5                  - true
            Исключение        /node1/node2/node3/node17                 - false
            Исключение        /node1/node2/node19/node17                - false

            Координаты ноды - /node1/node2/node3/node5
            Исключение        /node1/node2/node3/node5/node3            - false
            Исключение        /node1/node2/node3/node5/*                - false
            Исключение        /node1/node2/node3/node5/**               - false
            Исключение        /node1/node2/node3/node5/../../../../../  - false
                              в каждом случае, когда проверяемое исключение "длиннее" ноды считаем,
                              что оно не применимо (false)
*/
            return false;
        } else {
/*
            Ситуация с остатком "хвостом" ноды. Когда нода "длиннее" прилагаемого исключения.
            parsedExclusions.size() < fullNode.size()


            Координаты ноды - /node1/node2/node3/node5
            Исключение        /node1/node2/node3/*                      - true
            Исключение        /node1/node2/node3/**                     - true
            Исключение        /node1/node2/node3/node5                  - true
            Исключение        /node1/node2/node3/node17                 - false
            Исключение        /node1/node2/node19/node17                - false


            Координаты ноды - /node1/node2/node3/node5
            Исключение        /node1/node2                              - false
            Исключение        /node1/node2/node3/node5/*                - false
            Исключение        /node1/node2/node3/node5/node6            - false
            Исключение        /node1/node2/node3/*                      - true
            Исключение        /node1/node2/node3/**                     - false
            Исключение        /'*'/node113/node3/**                     - false
            Исключение        /node1/node2/**                           - true
            Исключение        /node1/**                                 - true
            Исключение        /**                                       - true

            В том случае, когда нода "длиннее" прилагаемого исключения, считаем, что она исключается если:
            Когда последний элемент в исключении это '**'
            либо нода длиннее исключения на один элемент И последний элемент исключения равен '*'
*/
            String lastExcusionElement = parsedExclusions.get(parsedExclusions.size() - 1);
            if ("**".equals(lastExcusionElement)) {
                return true;
            }
            if (fullNode.size() - parsedExclusions.size() == 1 && "*".equals(lastExcusionElement)) {
                return true;
            }
        }

        return false;
    }

    private static boolean identifies(Identity<SqNodeKeyType> identity, SqNodeKeyType identityType, String VALUE) {
        Objects.requireNonNull(identity);
        Objects.requireNonNull(identityType);
        Objects.requireNonNull(VALUE);
        switch (identityType) {
            case monkeyId:
            case componentType: {
                return Objects.equals(identity.ident(identityType), VALUE);
            }
            default: {
                throw new IllegalStateException("unknown identityType " + identityType);
            }
        }
    }

    @SignatureSensitive
    private static SqNodeKeyType getIdentityType(String pathElement) {
        Objects.requireNonNull(pathElement);
        if (pathElement.length() == 0) {
            throw new IllegalStateException("pathElenent cannot be empty");
        }
        return pathElement.startsWith("@") ? SqNodeKeyType.componentType : SqNodeKeyType.monkeyId;
    }

    @SignatureSensitive
    private static String getIdentity(String pathElenent) {
        SqNodeKeyType identityType = getIdentityType(pathElenent);
        switch (identityType) {
            case monkeyId: {
                return pathElenent;
            }

            case componentType: {
                return pathElenent.substring(1);
            }

            default: {
                throw new IllegalStateException("unknown type " + identityType);
            }
        }
    }


}
