package com.steammachine.jsonchecker.examples;

import com.steammachine.common.definitions.annotations.Example;
import com.steammachine.jsonchecker.defaults.MonkeyPathRepresentation;
import com.steammachine.jsonchecker.utils.JSonDirectComparator;


public class SimpleComparison {

    @Example
    public static void main(String[] args) {
        /*
         * in this example two json strings are compared
         *
         */
        System.out.println(JSonDirectComparator.comapreJSonStrings("{\"d\": 11}", "{   }", MonkeyPathRepresentation.REPRESENTATION));



    }
}
