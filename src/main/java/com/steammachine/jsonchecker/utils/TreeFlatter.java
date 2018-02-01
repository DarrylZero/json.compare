package com.steammachine.jsonchecker.utils;

import com.steammachine.jsonchecker.types.TreeNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 30.12.2017 10:21:46
 * <p>
 * Вспомогательный класс "выравнивания" элементов дерева в список.
 *
 * @author Vladimir Bogodukhov
 *         {@link TreeFlatter}
 **/
@Deprecated
public class TreeFlatter {

    /**
     * Метод разворачивания дерева в плоский вид.
     *
     * @param root корневой элемент дерева (всегда не null)
     * @return дерево в плоском виде.
     */
    public static List<TreeNode> flatten(TreeNode root) {
        Objects.requireNonNull(root);
        List<TreeNode> list = new ArrayList<>();
        list.add(root);
        int index = 0;
        while (index < list.size()) {
            TreeNode node = list.get(index);
            Iterator<TreeNode> children = node.children();
            while (children.hasNext()) {
                list.add(children.next());
            }
            index++;
        }
        return list;
    }

    /**
     * Метод разворачивания дерева в плоский вид с использованием фильтрации.
     *
     * @param root   корневой элемент дерева (всегда не null)
     * @param filter фильтр (всегда не null)
     * @return дерево в плоском виде.
     */
    public static List<TreeNode> flatten(TreeNode root, Predicate<TreeNode> filter) {
        Objects.requireNonNull(root);
        Objects.requireNonNull(filter);
        return flatten(root).stream().filter(filter).collect(Collectors.toList());
    }

}
