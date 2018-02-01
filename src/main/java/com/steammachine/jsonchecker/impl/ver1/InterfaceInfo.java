package com.steammachine.jsonchecker.impl.ver1;

import java.util.List;
import java.util.Map;

/**
 * Несмотря на то, что класс объявлен как public его использование не предполагается извне пакета
 * он может быть удален в любой момент.
 * 30.12.2017 10:21:46
 *
 * @author Vladimir Bogodukhov
 **/
public interface InterfaceInfo {
    Class defaultClass();

    Map<String, Object> params();

    Map<String, Class> types();

    /**
     * @return user defined strings - немодифицируемый список дополнительных данных (всегда не null)
     */
    List<String> userDefinedData();
}
