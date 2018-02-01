package com.steammachine.jsonchecker.types;

import com.steammachine.common.apilevel.Api;
import com.steammachine.common.apilevel.State;
import com.steammachine.jsonchecker.types.exceptions.ParamNotFound;

/**
 * Stub implementation constant for JSONParams interface
 * <p>
 * {@link com.steammachine.jsonchecker.types.JSONParamsStub}
 * com.steammachine.jsonchecker.types.JSONParamsStub
 */
public class JSONParamsStub {

    private JSONParamsStub() {
        /* ---- */
    }

    /**
     * Stub implementation for JSONParams interface
     */
    @Api(State.MAINTAINED)
    public static final JSONParams STUB_PARAMS = name -> {
        throw new ParamNotFound("param " + name + " not found");
    };

}
