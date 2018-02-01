package com.steammachine.jsonchecker.impl.ver1;

import com.steammachine.jsonchecker.types.ExtraInfo;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 30.12.2017 10:21:45
 *
 * @author Vladimir Bogodukhov
 **/
public class DefaultExtraInfo implements ExtraInfo {
    private final List<String> userDefinedItems;

    public DefaultExtraInfo(List<String> userDefinedItems) {
        this.userDefinedItems = Objects.requireNonNull(userDefinedItems);
    }
    @Override
    public Collection<String> userDefinedData() {
        return userDefinedItems ;
    }
}
