package com.steammachine.jsonchecker.impl.directcomparison.pathformats;

import com.steammachine.jsonchecker.types.exceptions.PathError;
import com.steammachine.jsonchecker.types.Path;

/**
 * 30.12.2017 10:21:45
 *
 * @author Vladimir Bogodukhov
 **/
public interface PathFormat {


    /**
     * @return Наименование формата (not null).
     */
    String name();

    /**
     * Метод проверки, что передаваемый путь соответствует формату.
     * Реализация метода не должна менять внутренного состояния объекта.
     *
     * @param path путь (not null)
     * @return {@code true} если соответствует
     */
    boolean checkPathFormat(String path);

    /**
     * @param data -
     * @return -
     * @throws PathError В случае если строка в данном формате не поддерживается.
     */
    Path parsePath(String data);

    /**
     * производит проверку что путь соответствует шаблону
     *
     * @param path         путь (not null)
     * @param pathTemplate шаблон пути
     * @return {@code true} если исключение применимо к
     */
    boolean isApplied(Path path, Path pathTemplate);


}
