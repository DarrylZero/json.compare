package com.steammachine.jsonchecker.names;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.steammachine.jsonchecker.types.exceptions.WrongDataFormat;

/**
 * Created by Vladimir Bogodukhov on 01.12.2017.
 *
 * @author Vladimir Bogodukhov
 */
class WrongDataFormatCheck {


    /**
     * Класс находится в интерфейсном пакете поэтому проверяется имя на случай изменения или перемещения.
     */
    @Test
    void testName() {
        Assertions.assertEquals("com.steammachine.jsonchecker.types.exceptions.WrongDataFormat",
                WrongDataFormat.class.getName());
    }


}