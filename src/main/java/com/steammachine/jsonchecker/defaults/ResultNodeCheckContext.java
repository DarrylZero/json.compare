package com.steammachine.jsonchecker.defaults;

import com.steammachine.jsonchecker.types.NodeCheckResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * Вспомогательный класс - производный от {@link DefaultNodeCheckContext}
 * Дефолтная реализация интерфейса {@link NodeCheckResult}
 *
 * Класс накапливает в результат выполения проверки.
 *
 * Created 30/01/17 13:12
 * @author Vladimir Bogodukhov
 * {@link com.steammachine.jsonchecker.defaults.ResultNodeCheckContext}
 **/
public class ResultNodeCheckContext extends DefaultNodeCheckContext implements NodeCheckResult {

    private List<String> messages = new ArrayList<>();

    @Override
    protected void doLog(String compiledString) {
        messages.add(compiledString);
    }

    @Override
    public void reset() {
        messages.clear();
        super.reset();
    }

    public List<String> messages() {
        return Collections.unmodifiableList(messages);
    }

    @Override
    public String toString() {
        return "ResultNodeCheckContext(" +  "messages=" + messages + " \r\n " +
                "isSuccessful = " + isSuccessful() + ")";
    }
}
