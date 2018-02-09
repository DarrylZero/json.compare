package com.steammachine.jsonchecker.defaulttypes;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.steammachine.jsonchecker.defaults.DefaultNodeCheckContext;

/**
 *
 * @author Vladimir Bogodukhov
 */
class DefaultNodeCheckContextCheck {

    @Test
    void nameTest() {
        Assertions.assertEquals("com.steammachine.jsonchecker.defaults.DefaultNodeCheckContext",
                DefaultNodeCheckContext.class.getName());
    }

 /*  ---------------------------------------------- simpletests ---------------------------------------------- */

    @Test
    void simpletests10() {
        Assertions.assertTrue(new DefaultNodeCheckContext().isSuccessful());
    }

    @Test
    void simpletests20() {
        DefaultNodeCheckContext context = new DefaultNodeCheckContext();
        Assertions.assertTrue(context.isSuccessful());
        context.failed();
        Assertions.assertFalse(context.isSuccessful());
    }

    @Test
    void simpletests30() {
        DefaultNodeCheckContext context = new DefaultNodeCheckContext();
        Assertions.assertTrue(context.isSuccessful());
        context.failed();
        Assertions.assertFalse(context.isSuccessful());
        context.reset();
        Assertions.assertTrue(context.isSuccessful());
    }
}