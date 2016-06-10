package fr.inria.util;

import com.google.common.collect.testing.MapTestSuiteBuilder;
import com.google.common.collect.testing.TestStringMapGenerator;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Simon
 * Date: 24/05/16
 * Time: 14:11
 */
public class LinkedHashMapTest extends TestCase {

    public static TestSuite suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(MapTestSuiteBuilder.using(new TestStringMapGenerator() {

            @Override
            protected Map<String, String> create(Map.Entry<String, String>[] entries) {
                Map<String, String> map = new LinkedHashMap<String, String>();
                for (Map.Entry<String, String> entry : entries) {
                    map.put(entry.getKey(), entry.getValue());
                }
                return map;
            }
        })

                .named("LinkedHashMap tests")

                .withFeatures(
                        MapFeature.GENERAL_PURPOSE,
                        MapFeature.ALLOWS_NULL_KEY_QUERIES,
                        MapFeature.ALLOWS_NULL_VALUES,
                        CollectionFeature.SUPPORTS_ITERATOR_REMOVE,
                        CollectionFeature.SERIALIZABLE,
                        CollectionSize.ANY
                )

                .createTestSuite());

        return suite;

    }
}