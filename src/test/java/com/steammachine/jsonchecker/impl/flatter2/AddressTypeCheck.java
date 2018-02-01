package com.steammachine.jsonchecker.impl.flatter2;

import org.junit.jupiter.api.Test;
import com.steammachine.jsonchecker.defaults.AddressType;
import com.steammachine.common.utils.enumerations.EnumComparisonUtils;

/**
 * Created by Vladimir Bogodukhov on 01.12.2017.
 *
 * @author Vladimir Bogodukhov
 */
class AddressTypeCheck {

    private enum AddressTypeTemplate {
        DIRECT,
        MONKEY
    }

    @Test
    void testNames() {
        EnumComparisonUtils.checkIfEnumsEqual(AddressTypeTemplate.class, AddressType.class);
    }

}