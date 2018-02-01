package com.steammachine.jsonchecker.impl.ver1;

import java.util.*;

/**
 * 30.12.2017 10:21:45
 *
 *  Дефолтная реализация {@link InterfaceInfo}
 *  Несмотря на то, что класс объявлен как public его использование не предполагается извне пакета
 *  он может быть удален в любой момент.
 *
 * @author Vladimir Bogodukhov
 **/
public class DefaultInterfaceInfo implements InterfaceInfo {
    private boolean locked;
    private Class defaultNodeClass;
    private Map<String, Object> params = new HashMap<>();
    private Map<String, Class> types = new HashMap<>();
    private List<String> userDefinedData = new ArrayList<>();

/* ------------------------------------------------------ InterfaceInfo --------------------------------------------- */

    @Override
    public Class defaultClass() {
        checkLocked(true);
        return defaultNodeClass;
    }

    @Override
    public Map<String, Object> params() {
        return params;
    }


    @Override
    public Map<String, Class> types() {
        return types;
    }

    @Override
    public List<String> userDefinedData() {
        return userDefinedData;
    }

    /*  ---------------------------------------------------------------------------------------------------------------- */

    public void setDefaultNodeClass(Class defaultNodeClass) {
        this.defaultNodeClass = defaultNodeClass;
    }

    public DefaultInterfaceInfo lock() {
        if (locked) {
            return null;
        }
        types = Collections.unmodifiableMap(types);
        params = Collections.unmodifiableMap(params);
        userDefinedData = Collections.unmodifiableList(userDefinedData);

        locked = true;
        checkLocked(true);
        return this;
    }

        /*  ----------------------------------------- privates ----------------------------------------- */

    private void checkLocked(boolean expectedLocked) {
        if (expectedLocked != locked) {
            throw new IllegalStateException("expectedLocked(" + expectedLocked + ") != locked(" + locked + ")");
        }
    }
}

