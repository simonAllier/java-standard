package fr.inria.util;

import com.google.common.collect.testing.SetTestSuiteBuilder;
import com.google.common.collect.testing.TestStringSetGenerator;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.SetFeature;
import junit.framework.TestSuite;

import java.util.*;
import java.util.HashSet;

/**
 * User: Simon
 * Date: 24/05/16
 * Time: 14:12
 */
public class HashSetTest {

    public static TestSuite suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(SetTestSuiteBuilder.using(new TestStringSetGenerator() {

            @Override
            protected Set<String> create(String[] elements) {
                Set set = new HashSet<String>();
                for(String elem : elements) {
                    set.add(elem);
                }
                return set;
            }
        })

                .named("HashSet tests")

                .withFeatures(
                        SetFeature.GENERAL_PURPOSE,
                        CollectionFeature.ALLOWS_NULL_VALUES,
                        CollectionSize.ANY
                )

                .createTestSuite());

        return suite;
    }
}
