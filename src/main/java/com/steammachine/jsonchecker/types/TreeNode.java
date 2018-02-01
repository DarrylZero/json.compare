package com.steammachine.jsonchecker.types;

import java.util.Iterator;

/**
 * Интерфейс узла древовидной структуры.
 *
 * Держатель данных для о разобранных объектах дерева.
 *
 *
 *
 * 30.12.2017 10:21:46
 * @author Vladimir Bogodukhov
 *         {@link TreeNode}
 **/
@Deprecated
public interface TreeNode extends Iterable<TreeNode> {



    /**
     * @return - Объект узла дерева (not null)
     */
    Object object();

    /**
     * @return итератор дочерних объектов (всегда not null)
     * каждый вызов выдает новый итератор
     */
    default Iterator<TreeNode> children() {
        return iterator();
    }


}
