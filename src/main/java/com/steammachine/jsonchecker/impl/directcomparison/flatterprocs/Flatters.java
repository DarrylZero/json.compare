package com.steammachine.jsonchecker.impl.directcomparison.flatterprocs;

import com.steammachine.jsonchecker.defaults.AddressType;
import com.steammachine.jsonchecker.impl.flatter2.ValRec;
import org.json.simple.JSONObject;
import com.steammachine.common.lazyeval.LazyEval;

import java.util.*;

/**
 * 30.12.2017 10:21:45
 *
 * @author Vladimir Bogodukhov
 **/
public class Flatters {
    private static final LazyEval<Map<String, LazyEval<FlattingProc>>> FLATTERS =
            LazyEval.eval(() -> {
                Map<String, LazyEval<FlattingProc>> map = new HashMap<>();
                map.put(AddressType.DIRECT.name(), LazyEval.eval(() -> DirectFlatter::flattenJSonObject));
                map.put(AddressType.MONKEY.name(), LazyEval.eval(() -> MonkeyIdFlatter::flattenJSonObject));
                return Collections.unmodifiableMap(map);
            });

    public static List<ValRec> flatten(String kind, JSONObject o) {
        Objects.requireNonNull(kind);
        Objects.requireNonNull(o);

        LazyEval<FlattingProc> flatter = FLATTERS.value().get(kind);
        if (flatter == null) {
            throw new IllegalStateException("flatter of kind " + kind + " not found.");
        }
        return flatter.value().flatten(o);
    }
}
