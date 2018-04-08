package com.steammachine.jsonchecker.types;

import com.steammachine.common.apilevel.Api;
import com.steammachine.common.apilevel.State;
import com.steammachine.jsonchecker.types.exceptions.ParamError;
import com.steammachine.jsonchecker.types.exceptions.ParamTypeError;

/**
 * Interface of param  used for passing into Json comparison utils.
 *
 * @author Vladimir Bogodukhov
 */
@Api(State.MAINTAINED)
public interface JSONParam {

    /**
     * @return Param name (never null)
     */
    String name();


    /**
     * @return Param value as a string (never null)
     */
    String paramsRep();

    /**
     * Checks if param type is allowed for actual param value.
     */
    void checkValues();


    /**
     * Метод сравнения параметра со значением.
     *
     * @param value значение сравниваемое на соответствие.
     * @return {@code true} если параметр соответствует переданному значению.
     */
    boolean matches(Object value);


    /**
     * Сравнение параметра с другим параметром.
     * Не частый случай, когда в обоих документах использованы параметры по одинаковым путям.
     * <p>
     * При сравнени двух параметров с использованием этого метода результат вычисления истиннен в том случае если :
     * <p>
     * result param1.matchesParam(param2) || param2.matchesParam(param1)
     * <p>
     * Каждая конкретная реализация сама решает является ли объект ее класса эквивалентен другому параметру
     *
     * @param thatParam Другой параметр (Всегда не null)
     * @return {@code true} если параметр соответствует переданному параметру.
     */
    boolean matchesParam(JSONParam thatParam);

}
