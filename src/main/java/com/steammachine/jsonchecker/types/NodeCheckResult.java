package com.steammachine.jsonchecker.types;


import java.util.List;

/**
 * 30.12.2017 10:21:46
 *
 * @author Vladimir Bogodukhov
 * {@link com.steammachine.jsonchecker.types.NodeCheckResult}
 * com.steammachine.jsonchecker.types.NodeCheckResult
 **/
public interface NodeCheckResult {

    /**
     * @return Признак успешного завершения проверки.
     */
    boolean isSuccessful();

    /**
     * @return - Немодифицируемый список строк сообщений проверки (всегда не null)
     */
    List<String> messages();
}
