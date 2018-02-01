package com.steammachine.jsonchecker.types;

import com.steammachine.common.apilevel.Api;
import com.steammachine.common.apilevel.State;
import com.steammachine.jsonchecker.types.exceptions.ParamNotFound;

/**
 *
 * Param interface
 *
 * @author Vladimir Bogodukhov
 */
@Api(State.MAINTAINED)
public interface JSONParams {


    /**
     * get the param by it's name
     *
     * @param name param's name
     * @return a param
     * @exception ParamNotFound if no params are found
     */
    JSONParam get(String name);
}
