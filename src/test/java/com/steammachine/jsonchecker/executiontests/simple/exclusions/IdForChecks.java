package com.steammachine.jsonchecker.executiontests.simple.exclusions;

import com.steammachine.jsonchecker.types.Identity;
import com.steammachine.jsonchecker.types.SqNodeKeyType;

import java.util.Objects;

/**
 * Created 26/01/17 11:45
 *
 * @author Vladimir Bogodukhov
 **/
public class IdForChecks implements Identity<SqNodeKeyType> {

    private final String monkeyId;
    private final String componentType;

    public IdForChecks(String monkeyId, String componentType) {
        this.monkeyId = Objects.requireNonNull(monkeyId);
        this.componentType = Objects.requireNonNull(componentType);
    }

    @Override
    public boolean supports(Class identityType) {
        return SqNodeKeyType.class == identityType;
    }

    @Override
    public String ident(SqNodeKeyType identityType) {
        switch (identityType) {
            case componentType: {
                return componentType;
            }

            case monkeyId: {
                return monkeyId;
            }

            default:
                throw new IllegalStateException();
        }
    }
}
