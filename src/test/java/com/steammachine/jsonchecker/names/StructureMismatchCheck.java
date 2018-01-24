package com.steammachine.jsonchecker.names;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.steammachine.jsonchecker.types.exceptions.StructureMismatch;

/**
 * Специально выделенный тип сообщения - обозначающий несовпадение структур документа и проверяемого шаблона.
 * Используется в тех случаях когда требуется указать недопустимые несоответствия структуры.
 * <p>
 * <p>
 * <p>
 * 30.12.2017 10:21:46
 *
 * @author Vladimir Bogodukhov
 **/
public class StructureMismatchCheck {


    @Test
    void testName() {
        Assertions.assertEquals("com.steammachine.jsonchecker.types.exceptions.StructureMismatch",
                StructureMismatch.class.getName());
    }
}
