package com.steammachine.jsonchecker.defaults;

import com.steammachine.jsonchecker.types.NodeCheckContext;
import com.steammachine.common.utils.commonutils.CommonUtils;

/**
 * Дефолтная реализация {@link NodeCheckContext}
 * Created 09/01/17 18:13
 *
 * @author Vladimir Bogodukhov
 *         <p>
 *         {@link com.steammachine.jsonchecker.defaults.DefaultNodeCheckContext}
 *          com.steammachine.jsonchecker.defaults.DefaultNodeCheckContext
 *
 **/
public class DefaultNodeCheckContext implements NodeCheckContext {

    /**
     * Признак успешного завершения проверки -
     *
     */
    private boolean successful = true;

    @Override
    public final void log(Object... parts) {
        doLog(CommonUtils.compileString("", parts));
    }

    @Override
    public final void failed() {
        successful = false;
    }

    /**
     * метод в который передается собранная строка - что именно делается с этой строкой
     * может быть определено в классе наследнике.
     *
     * @param compiledString - логируемое сообщение. (всегда не null)
     */
    protected void doLog(String compiledString) {
        /* to be overridden in descendants */
    }

    /**
     * @return Признак успешного завершения проверки.
     */
    public final boolean isSuccessful() {
        return successful;
    }

    /**
     * Cбросить результат проверки.
     * метод предполагает стирание данных, накопленных за время проверки.
     *
     */
    public void reset() {
        successful = true;
    }

}
