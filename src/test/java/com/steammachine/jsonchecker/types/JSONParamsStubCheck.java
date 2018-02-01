package com.steammachine.jsonchecker.types;

import com.steammachine.jsonchecker.types.exceptions.ParamNotFound;
import com.steammachine.org.junit5.extensions.expectedexceptions.Expected;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class JSONParamsStubCheck {

    @Test
    void checkNameIntegrity() {
        Assertions.assertEquals("com.steammachine.jsonchecker.types.JSONParamsStub",
                JSONParamsStub.class.getName());
    }


    @Test
    @Expected(expected = ParamNotFound.class)
    void stubParamsGet10() {
        JSONParamsStub.STUB_PARAMS.get(null);
    }

    @Test
    @Expected(expected = ParamNotFound.class)
    void stubParamsGet20() {
        JSONParamsStub.STUB_PARAMS.get("Pasa_el_tiempo_y_ahora_creo_que");
    }
}