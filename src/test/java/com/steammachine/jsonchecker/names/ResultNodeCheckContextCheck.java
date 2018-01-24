package com.steammachine.jsonchecker.names;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.steammachine.jsonchecker.defaults.ResultNodeCheckContext;

/**
 * 30.12.2017 10:21:46
 *
 * @author Vladimir Bogodukhov
 **/
public class ResultNodeCheckContextCheck {

    @Test
    void testName() {
        Assertions.assertEquals("com.steammachine.jsonchecker.defaults.ResultNodeCheckContext",
                ResultNodeCheckContext.class.getName());
    }
}
