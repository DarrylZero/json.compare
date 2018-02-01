package com.steammachine.jsonchecker.impl.directcomparison.flatterprocs;

import org.json.simple.JSONObject;
import com.steammachine.jsonchecker.impl.flatter2.ValRec;

import java.util.List;

/**
 *
 * 30.12.2017 10:21:45
 * @author Vladimir Bogodukhov
 **/
@FunctionalInterface
public interface FlattingProc {

    List<ValRec> flatten(JSONObject o);

}
