package com.steammachine.jsonchecker.types;

import java.util.Collection;
import java.util.List;

/**
 *
 * @author Vladimir Bogodukhov 
 **/
public interface ExtraInfo {

    /**
     * @return - Пользовательские строки.
     */
    Collection<String> userDefinedData();
}
