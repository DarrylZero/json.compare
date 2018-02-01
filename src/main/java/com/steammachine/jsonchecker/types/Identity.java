package com.steammachine.jsonchecker.types;

import java.util.Objects;

/**
 * Интерфейс идентификации объекта, который может быть более чем одним способом.
 * <p>
 *
 * @param <IT> Identity type - тип ключа - по которому производится идентификация.
 * @author Vladimir Bogodukhov
 **/
public interface Identity<IT> {

    /**
     * Проверить, что объект поддерживает идентификацию по передаваемому типу ключа.
     * Вызывается перед {@link #ident(Object)}
     *
     * {@link #ident(Object)} вызывается только в случае если этот метод возвращает {@code true}
     *
     *
     * @param identityType тип ключа (всегда не null)
     * @return {@code true}  если поддерживает. {@code false} если не поддерживает.
     */
    boolean supports(Class<?> identityType);

    /**
     * Метод выдает значение идентификатора в соответсвии с типом ключа.
     * Результат может как быть так и не быть null
     *
     * @param identityType тип ключа (всегда не null)
     * @return значение идентификатора (значение null возможно) если возвращается null -
     * объект не имеет идентификатора по данному типу ключа.
     */
    String ident(IT identityType);




}
