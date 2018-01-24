package com.steammachine.jsonchecker.names;

import com.steammachine.jsonchecker.types.exceptions.WrongNodeData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Created by Vladimir Bogodukhov on 01.12.2017.
 *
 * @author Vladimir Bogodukhov
 */
public class WrongNodeDataCheck {

    /**
     * Класс находится в интерфейсном пакете поэтому проверяется имя на случай изменения или перемещения.
     */
    @Test
    void testName() {
        Assertions.assertEquals("com.steammachine.jsonchecker.types.exceptions.WrongNodeData",
                WrongNodeData.class.getName());
    }
}
