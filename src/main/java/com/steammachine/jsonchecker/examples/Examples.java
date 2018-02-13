package com.steammachine.jsonchecker.examples;

import com.steammachine.common.apilevel.Api;
import com.steammachine.common.apilevel.State;
import com.steammachine.common.definitions.annotations.Example;
import com.steammachine.common.utils.ResourceUtils;
import com.steammachine.jsonchecker.types.JSONParams;
import com.steammachine.jsonchecker.types.NodeCheckResult;
import com.steammachine.jsonchecker.utils.JSONParamsBuilder;
import com.steammachine.jsonchecker.utils.JSonDirectComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

import static com.steammachine.common.utils.commonutils.CommonUtils.check;
import static com.steammachine.jsonchecker.utils.JSONParamsBuilder.params;
import static java.util.Arrays.asList;


/**
 * Class with examples
 * <p>
 * {@link com.steammachine.jsonchecker.examples.Examples }
 * com.steammachine.jsonchecker.examples.Examples
 */
@Api(State.MAINTAINED)
@Example
public class Examples {

    private static final Logger LOG = LoggerFactory.getLogger(Examples.class);

    public static void main(String... args) throws IOException {
        simpleJsonComparison();
        simpleJsonDataComparison();
        comparisonWithParams();
        comparisonWithIncusion();
        compareWithExclusion();
        compareWithExclusions2();
        compareWithRegexpPattern();
        compareWithWildCardPattern();
        compareJsonsWithOutput();
    }

    /**
     * Here two json documents are compared using regexp param
     * <p>
     * data from one document is checked with regexp params.
     * <p>
     * a param 'p2' must conform the json field value.
     */
    @Example
    private static void compareWithRegexpPattern() throws IOException {
        try (InputStream data1 = dataStream("resources/resource_json4.json")) {
            try (InputStream data2 = dataStream("resources/resource_json6.json")) {
                NodeCheckResult result = JSonDirectComparator.compareJSonStreams(data1, data2,
                        params().regexp("p2", Pattern.compile("^[(0-9)]{1,5}$")).build(), null, null);

                LOG.info("comparison result =   {0}", result); /* jsons are the same */
                check(result::isSuccessful, IllegalStateException::new);
            }
        }

        try (InputStream data1 = dataStream("resources/resource_json4.json")) {
            try (InputStream data2 = dataStream("resources/resource_json6.json")) {
                NodeCheckResult result = JSonDirectComparator.compareJSonStreams(data1, data2,
                        params().regexp("p2", "^[(0-9)]{1,5}$").build(), null, null);

                LOG.info("comparison result =   {0}", result); /* jsons are the same */
                check(result::isSuccessful, IllegalStateException::new);
            }
        }
    }


    /**
     */
    @Example
    private static void compareWithWildCardPattern() throws IOException {
        // compares only those child elements that are called paramN - the rest of elenens are not checked
        // ***/paramN/**

        try (InputStream data1 = dataStream("resources/resource_json7.json")) {
            try (InputStream data2 = dataStream("resources/resource_json8.json")) {
                NodeCheckResult result = JSonDirectComparator.compareJSonStreams(data1, data2,
                        null, asList("***/paramN/**"), null);

                LOG.info("comparison result =   {0}", result); /* jsons are the same */
                check(result::isSuccessful, IllegalStateException::new);
            }
        }
    }

    /**
     */
    @Example
    private static void compareJsonsWithOutput() throws IOException {
        // compares only those child elements that are called paramN
        // ***/paramN/**

        try (InputStream data1 = dataStream("resources/resource_json7.json")) {
            try (InputStream data2 = dataStream("resources/resource_json8.json")) {
                NodeCheckResult result = JSonDirectComparator.compareJSonStreams(data1, data2, null, null, null);

                LOG.info("comparison result =   {0}", result); /* jsons are NOT the same */
                LOG.info("comparison result =   {0}", result.messages()); /* differences */
                result.messages().stream().forEachOrdered(System.out::println);/* differences line by line */

                check(() -> !result.isSuccessful(), IllegalStateException::new);
            }
        }
    }


    /**
     * Compares two json documents where everything is compared except for first and second (in zero based array) elements of param4
     */
    @Example
    private static void compareWithExclusions2() throws IOException {
        try (InputStream data1 = dataStream("resources/resource_json1.json")) {
            try (InputStream data2 = dataStream("resources/resource_json5.json")) {
                NodeCheckResult result = JSonDirectComparator.compareJSonStreams(data1, data2, null,
                        null, asList("param4[1]", "param4[2]"));

                LOG.info("comparison result =   {0}", result); /* jsons are the same */
                check(result::isSuccessful, IllegalStateException::new);
            }
        }
    }


    /**
     * Compares two json documents where everything is compared except for all array elements of param4
     */
    @Example
    private static void compareWithExclusion() throws IOException {
        try (InputStream data1 = dataStream("resources/resource_json1.json")) {
            try (InputStream data2 = dataStream("resources/resource_json5.json")) {
                NodeCheckResult result = JSonDirectComparator.compareJSonStreams(data1, data2, null,
                        null, asList("param4[*]"));

                LOG.info("comparison result =   {0}", result); /* jsons are the same */
                check(result::isSuccessful, IllegalStateException::new);
            }
        }
    }

    /**
     * compares two jsons in their parts only
     * only param1, param3, param4[0]
     */
    @Example
    private static void comparisonWithIncusion() throws IOException {
        try (InputStream data1 = dataStream("resources/resource_json1.json")) {
            try (InputStream data2 = dataStream("resources/resource_json5.json")) {
                NodeCheckResult result = JSonDirectComparator.compareJSonStreams(data1, data2,
                        null, asList("param1", "param3", "param4[0]"), null);

                LOG.info("comparison result =   {0}", result); /* jsons are the same */
                check(result::isSuccessful, IllegalStateException::new);
            }
        }
    }

    /**
     * Comparison the document with params
     **/
    @Example
    private static void comparisonWithParams() throws IOException {
        try (InputStream data1 = dataStream("resources/resource_json1.json")) {
            try (InputStream data2 = dataStream("resources/resource_json4.json")) {
                JSONParams params = params().single("p2", false).build();
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


    /*  -------------------------------------------------- privates --------------------------------------------*/

    private static InputStream dataStream(String path) {
        return ResourceUtils.loadResourceByRelativePath(Examples.class, path);
    }

}
