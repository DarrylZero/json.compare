package com.steammachine.jsonchecker.names;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.steammachine.jsonchecker.types.exceptions.MalformedDocument;

/**
 * Специально выделенный тип сообщения - обозначающий неверную структуру документа.
 * <p>
 * Используется в тех случаях когда требуется указать недопустимые несоответствия структуры.
 * Исключение НЕ предполагает использования в случае неверного json документа (not well formed)
 * <p>
 * <p>
 * 30.12.2017 10:21:46
 *
 * @author Vladimir Bogodukhov
 **/
public class MalformedDocumentCheck {


    @Test
    void testName() {
        Assertions.assertEquals("com.steammachine.jsonchecker.types.exceptions.MalformedDocument",
                MalformedDocument.class.getName());
    }
}
