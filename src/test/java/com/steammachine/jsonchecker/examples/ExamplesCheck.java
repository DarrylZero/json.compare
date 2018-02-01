package com.steammachine.jsonchecker.examples;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class ExamplesCheck {

    @Test
    void checkNameIntegrity() {
        Assertions.assertEquals("com.steammachine.jsonchecker.examples.Examples",
                Examples.class.getName());
    }

    @Test
    void examplesCheck() throws IOException {
        Examples.main();
    }


}