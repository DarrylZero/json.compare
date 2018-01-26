package com.steammachine.jsonchecker.examples;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExamplesCheck {
    @Test
    void checkNameIntegrity() {
        Assertions.assertEquals("com.steammachine.jsonchecker.examples.Examples",
                Examples.class.getName());
    }
}