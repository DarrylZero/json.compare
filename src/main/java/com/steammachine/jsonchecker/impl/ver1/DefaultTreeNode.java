package com.steammachine.jsonchecker.impl.ver1;

import com.steammachine.jsonchecker.types.TreeNode;

import java.util.*;

/**
 * 30.12.2017 10:21:46
 *
 *  Несмотря на то, что класс объявлен как public его использование не предполагается извне пакета
 *  он может быть удален в любой момент.
 * @author Vladimir Bogodukhov
 *
 * @deprecated
 **/
@Deprecated
public class DefaultTreeNode implements TreeNode {


    private final List<TreeNode> children = new ArrayList<>();
    private final Object data;

    public DefaultTreeNode(Object data) {
        this.data = Objects.requireNonNull(data);
    }

    public void addChild(TreeNode child) {
        children.add(child);
    }

    @Override
    public Object object() {
        return data;
    }

    @Override
    public Iterator<TreeNode> iterator() {
        return children.iterator();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DefaultTreeNode)) return false;

        DefaultTreeNode treeNodes = (DefaultTreeNode) o;

        if (!children.equals(treeNodes.children)) return false;
        return data != null ? data.equals(treeNodes.data) : treeNodes.data == null;
    }

    @Override
    public int hashCode() {
        int result = children.hashCode();
        result = 31 * result + data.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "DefaultTreeNode{" +
                "children=" + children +
                ", data=" + data +
                '}';
    }
}
