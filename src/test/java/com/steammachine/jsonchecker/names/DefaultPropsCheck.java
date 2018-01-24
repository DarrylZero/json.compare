package com.steammachine.jsonchecker.names;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.steammachine.jsonchecker.types.UnmappedProperties;

/**
 * Created by Vladimir Bogoduhov on 01.12.2017.
 *
 * @author Vladimir Bogoduhov
 */
class DefaultPropsCheck {

    @Test
    void testName() {
        Assertions.assertEquals("com.steammachine.jsonchecker.types.UnmappedProperties",
                UnmappedProperties.class.getName());
    }

}