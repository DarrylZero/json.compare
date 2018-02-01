package com.steammachine.jsonchecker.impl.directcomparison.pathformats;

import com.steammachine.common.utils.enumerations.EnumComparisonUtils;
import org.junit.jupiter.api.Test;
//import com.steammachine.common.utils.enumerations.EnumComparisonUtils;

/**
 * Created by Vladimir Bogodukhov on 01.12.2017.
 *
 * @author Vladimir Bogodukhov
 */
class FormatTypeCheck {

    public enum FormatTypeTemplate {
        dot,
        monkeyId,
    }

    @Test
    void formatTypeCheckIntegrity() {
        EnumComparisonUtils.checkIfEnumsEqual(FormatTypeTemplate.class, FormatTypeTemplate.class);
        EnumComparisonUtils.checkIfEnumOrdinalsEqual(FormatTypeTemplate.class, FormatTypeTemplate.class);
    }
}