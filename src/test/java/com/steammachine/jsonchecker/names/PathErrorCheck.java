package com.steammachine.jsonchecker.names;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.steammachine.jsonchecker.types.exceptions.PathError;

/**
 * 30.12.2017 10:21:46
 *
 * @author Vladimir Bogodukhov
 */
class PathErrorCheck {


    @Test
    void testName() {
        Assertions.assertEquals("com.steammachine.jsonchecker.types.exceptions.PathError",
                PathError.class.getName());
    }

}
