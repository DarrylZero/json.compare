package com.steammachine.jsonchecker.names;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.steammachine.jsonchecker.types.exceptions.JSonParseException;

/**
 * Oбобщенный тип исключения для обозначения ошибок разбора документа json
 * <p>
 * <p>
 * 30.12.2017 10:21:46
 *
 * @author Vladimir Bogodukhov
 **/
class JSonParseExceptionCheck {

    @Test
    void testName() {
        Assertions.assertEquals("com.steammachine.jsonchecker.types.exceptions.JSonParseException",
                JSonParseException.class.getName());
    }


}
