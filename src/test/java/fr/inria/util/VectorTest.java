package fr.inria.util;

import com.google.common.collect.testing.ListTestSuiteBuilder;
import com.google.common.collect.testing.TestStringListGenerator;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.ListFeature;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

/**
 * Your test class must be annotated with {@link RunWith} to specify that it's a
 * test suite and not a single test.
 */

public class VectorTest extends TestCase {


        public static TestSuite suite() {
            TestSuite suite = new TestSuite();
            suite.addTest(ListTestSuiteBuilder.using(new TestStringListGenerator() {
                        @Override
                        protected List<String> create(String[] elements) {
                            return new Vector<String>(Arrays.asList(elements));
                        }
                    })

                    .named("Vector tests")

                    .withFeatures(
                            ListFeature.GENERAL_PURPOSE,
                            ListFeature.SUPPORTS_ADD_WITH_INDEX,
                            ListFeature.SUPPORTS_REMOVE_WITH_INDEX,
                            ListFeature.SUPPORTS_SET,
                            CollectionFeature.SUPPORTS_ADD,
                            CollectionFeature.SUPPORTS_REMOVE,
                            CollectionFeature.SUPPORTS_ITERATOR_REMOVE,
                            CollectionFeature.ALLOWS_NULL_VALUES,
                            CollectionFeature.GENERAL_PURPOSE,
                            CollectionSize.ANY
                    )

                    .createTestSuite());

            return suite;
        }
}