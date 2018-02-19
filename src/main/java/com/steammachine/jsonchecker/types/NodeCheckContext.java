package com.steammachine.jsonchecker.types;

/**
 * Json comparison context. Userd to pass into comparison utils.
 *
 * @author Vladimir Bogodukhov
 **/
@FunctionalInterface
public interface NodeCheckContext {

    /**
     * Запись (отправка) сообщений о логировании - отчет о проведении проверки.
     *
     * @param parts части сообщения. Части сообщения собируются в запись с использованием
     *              алгоритма Object... parts -->  String message = "" + part_1 + part_2 + part_2 + ... + part_n;
     */
    default void log(Object... parts) {
    }

    /**
     *
     * Вызывается в момент проверки, если для пометки проверки как неуспешной.
     */
    void failed();
}
