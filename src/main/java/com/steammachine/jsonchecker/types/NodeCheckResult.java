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
     * @return whether documents are equal(matched).
     */
    boolean isSuccessful();

    /**
     * @return - unmodified list of changes report(always not null)
     */
    List<String> messages();
}
