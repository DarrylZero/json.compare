package com.steammachine.jsonchecker.types;

/**
 * Результат загрузки разобраного документа json.
 *
 * 30.12.2017 10:21:46
 * @author Vladimir Bogodukhov
 **/
public interface LoadResult {

    /**
     *
     * @return Корневой элемент дерева
     */
    TreeNode root();


    /**
     *
     * @return  Дополнительные пользовательские строковые данные. Не модифицируемые список.
     */
    ExtraInfo extrainfo();
}
