package com.steammachine.jsonchecker.examples;

import com.steammachine.common.apilevel.Api;
import com.steammachine.common.apilevel.State;
import com.steammachine.common.definitions.annotations.Example;
import com.steammachine.common.utils.ResourceUtils;
import com.steammachine.common.utils.commonutils.CommonUtils;
import com.steammachine.jsonchecker.types.JSONParams;
import com.steammachine.jsonchecker.types.NodeCheckResult;
import com.steammachine.jsonchecker.utils.JSONParamsBuilder;
import com.steammachine.jsonchecker.utils.JSonDirectComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.Arrays;

import static com.steammachine.common.utils.commonutils.CommonUtils.check;
import static java.util.Arrays.asList;


/**
 * Class with examples
 * <p>
 * {@link com.steammachine.jsonchecker.examples.Examples }
 * com.steammachine.jsonchecker.examples.Examples
 */
@Api(State.MAINTAINED)
public class Examples {

    private static final Logger LOG = LoggerFactory.getLogger(Examples.class);

    public static void main(String... args) throws IOException {
        simpleJsonComparison();
        simpleJsonDataComparison();
        comparisonWithParams();
        comparisonWithIncusion();
    }

    /**
     *  compares two jsons in their parts only
     *  only param1, param3, param4[0]
     *
     */
    private static void comparisonWithIncusion() throws IOException {
        try (InputStream data1 = dataStream("resources/resource_json1.json")) {
            try (InputStream data2 = dataStream("resources/resource_json5.json")) {
                NodeCheckResult result = JSonDirectComparator.compareJSonStreams(data1, data2, null,
                        asList("param1", "param3", "param4[0]"), null);

                LOG.info("comparison result =   {0}", result); /* jsons are the same */
                check(result::isSuccessful, IllegalStateException::new);
            }
        }
    }

    /**
     * Comparison the document with params
     *
     **/
    private static void comparisonWithParams() throws IOException {
        try (InputStream data1 = dataStream("resources/resource_json1.json")) {
            try (InputStream data2 = dataStream("resources/resource_json4.json")) {
                JSONParams params = JSONParamsBuilder.of().single("p2", false).build();
                NodeCheckResult result = JSonDirectComparator.compareJSonStreams(data1, data2, params, null, null);

                LOG.info("comparison result =   {0}", result); /* jsons are the same */
                check(result::isSuccessful, IllegalStateException::new);
            }
        }
        try (InputStream data1 = dataStream("resources/resource_json1.json")) {
            try (InputStream data2 = dataStream("resources/resource_json4.json")) {
                JSONParams params = JSONParamsBuilder.of().single("p2", true).build();
                NodeCheckResult result = JSonDirectComparator.compareJSonStreams(data1, data2, params, null, null);

                LOG.info("comparison result =  {0}", result); /* jsons are different */
                LOG.info("comparison result.messages =  {0}", result.messages()); /* jsons are different */
                check(() -> !result.isSuccessful(), IllegalStateException::new);
            }
        }
    }


    /**
     * In given example - a stream containing json data is compared with another stream
     * Pay your attention that the caller should take care of used stream in respect of closing it.
     */
    @Example
    private static void simpleJsonDataComparison() throws IOException {
        try (InputStream data1 = dataStream("resources/resource_json1.json")) {
            try (InputStream data2 = dataStream("resources/resource_json2.json")) {
                boolean result = JSonDirectComparator.compareJSonStreams(data1, data2);
                LOG.info("comparison result =  {0}", result); /* jsons are different */
                check(() -> !result, IllegalStateException::new);
            }
        }

        try (InputStream data1 = dataStream("resources/resource_json1.json")) {
            try (InputStream data2 = dataStream("resources/resource_json3.json")) {
                boolean result = JSonDirectComparator.compareJSonStreams(data1, data2);
                LOG.info("comparison result is {0}", result); /* jsons are equal */
                check(() -> result, IllegalStateException::new);
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
        check(() -> !result, IllegalStateException::new);

        boolean result2 = JSonDirectComparator.compareJSonStrings("{\"d\": 11, \"f\": 21}", "{\"f\": 21, \"d\": 11 }");
        LOG.info("comparison result = {0}", result);
        check(() -> result2, IllegalStateException::new);
    }

    private static InputStream dataStream(String path) {
        return ResourceUtils.loadResourceByRelativePath(Examples.class, path);
    }

}
