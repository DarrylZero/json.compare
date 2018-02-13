package com.steammachine.jsonchecker.infrastructure;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

class InfrastructureCheck {


    /**
     * publish.properties is copied from project sources
     */
    @Test
    void checkPublushPropertiesFile() throws IOException {
        Properties properties = new Properties();
        try (InputStream resource = InfrastructureCheck.class.getResourceAsStream("publish.properties")) {
            properties.load(resource);
        }

        /* these two properties mustn't be changed */
        Assertions.assertEquals("json.comparison", properties.getProperty("artifact"));
        Assertions.assertEquals("com.steammachine.org", properties.getProperty("group"));
    }
}