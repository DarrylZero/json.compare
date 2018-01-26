package com.steammachine.jsonchecker.examples;

import com.steammachine.common.apilevel.Api;
import com.steammachine.common.apilevel.State;
import com.steammachine.common.definitions.annotations.Example;
import com.steammachine.common.utils.ResourceUtils;
import com.steammachine.common.utils.commonutils.CommonUtils;
import com.steammachine.jsonchecker.utils.JSonDirectComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;


/**
 * Class with examples
 *
 * {@link com.steammachine.jsonchecker.examples.Examples }
 * com.steammachine.jsonchecker.examples.Examples
 */
@Api(State.MAINTAINED)
public class Examples {

    private static final Logger LOG = LoggerFactory.getLogger(Examples.class);

    public static void main(String[] args) throws IOException {
        simpleJsonComparison();
        simpleJsonDataComparison();
    }


    /**
     * In given example - a stream containing json data is compared with another stream
     * Pay your attention that the caller should take care of used stream in respect of closing it.
     *
     */
    @Example
    private static void simpleJsonDataComparison() throws IOException {
        try (InputStream data1 = dataStream("resources/resource_json1.json")) {
            try (InputStream data2 = dataStream("resources/resource_json2.json")) {
                boolean result = JSonDirectComparator.compareJSonStreams(data1, data2);
                LOG.info("comparison result =  {0}", result); /* jsons are different */
                CommonUtils.check(() -> !result, IllegalStateException::new);
            }
        }

        try (InputStream data1 = dataStream("resources/resource_json1.json")) {
            try (InputStream data2 = dataStream("resources/resource_json3.json")) {
                boolean result = JSonDirectComparator.compareJSonStreams(data1, data2);
                LOG.info("comparison result is {0}", result); /* jsons are equal */
                CommonUtils.check(() -> result, IllegalStateException::new);
            }
        }
    }

    /*
     * in this example two json strings are compared
     * No details are returned - only boolean status of comparison
     *
     */
    @Example
    private static void simpleJsonComparison() {
        boolean result = JSonDirectComparator.compareJSonStrings("{\"d\": 11}", "{   }");
        LOG.info("comparison result = {0}", result);
        CommonUtils.check(() -> !result, IllegalStateException::new);

        boolean result2 = JSonDirectComparator.compareJSonStrings("{\"d\": 11, \"f\": 21}", "{\"f\": 21, \"d\": 11 }");
        LOG.info("comparison result = {0}", result);
        CommonUtils.check(() -> result2, IllegalStateException::new);
    }

    private static InputStream dataStream(String path) {
        return ResourceUtils.loadResourceByRelativePath(Examples.class, path);
    }

}
