package fr.inria.util;

import com.google.common.collect.testing.ListTestSuiteBuilder;
import com.google.common.collect.testing.TestStringListGenerator;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.ListFeature;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.Test;

import java.util.*;
import java.util.ArrayList;

/**
 * User: Simon
 * Date: 24/05/16
 * Time: 13:40
 */
public class LinkedListTest extends TestCase {

    public static TestSuite suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(ListTestSuiteBuilder.using(new TestStringListGenerator() {
            @Override
            protected List<String> create(String[] elements) {
                return new LinkedList<String>(Arrays.asList(elements));
            }
        })

                .named("LinkedList tests")

                .withFeatures(
                        ListFeature.GENERAL_PURPOSE,
                        ListFeature.SUPPORTS_SET,
                        CollectionFeature.SUPPORTS_ADD,
                        CollectionFeature.SUPPORTS_REMOVE,
                        CollectionFeature.SUPPORTS_ITERATOR_REMOVE,
                        CollectionFeature.ALLOWS_NULL_VALUES,
                        CollectionFeature.GENERAL_PURPOSE,
                        CollectionSize.ANY
                )

                .createTestSuite());

        suite.addTestSuite(LinkedListTest.class);
        return suite;
    }


    private LinkedList<String> collection;
    private java.util.LinkedList<String> confirmed;


    //-----------------------------------------------------------------------
    /**
     *  Verifies that {@link #collection} and {@link #confirmed} have
     *  identical state.
     */
    public void verify() {
        final int confirmedSize = getConfirmed().size();
        assertEquals("Collection size should match confirmed collection's", confirmedSize,
                getCollection().size());
        assertEquals("Collection isEmpty() result should match confirmed collection's",
                getConfirmed().isEmpty(), getCollection().isEmpty());

        // verify the collections are the same by attempting to match each
        // object in the collection and confirmed collection.  To account for
        // duplicates and differing orders, each confirmed element is copied
        // into an array and a flag is maintained for each element to determine
        // whether it has been matched once and only once.  If all elements in
        // the confirmed collection are matched once and only once and there
        // aren't any elements left to be matched in the collection,
        // verification is a success.

        // copy each collection value into an array
        final Object[] confirmedValues = new Object[confirmedSize];

        Iterator<String> iter;

        iter = getConfirmed().iterator();
        int pos = 0;
        while (iter.hasNext()) {
            confirmedValues[pos++] = iter.next();
        }

        // allocate an array of boolean flags for tracking values that have
        // been matched once and only once.
        final boolean[] matched = new boolean[confirmedSize];

        // now iterate through the values of the collection and try to match
        // the value with one in the confirmed array.
        iter = getCollection().iterator();
        while (iter.hasNext()) {
            final Object o = iter.next();
            boolean match = false;
            for (int i = 0; i < confirmedSize; i++) {
                if (matched[i]) {
                    // skip values already matched
                    continue;
                }
                if (o == confirmedValues[i] || o != null && o.equals(confirmedValues[i])) {
                    // values matched
                    matched[i] = true;
                    match = true;
                    break;
                }
            }
            // no match found!
            if (!match) {
                fail("Collection should not contain a value that the "
                        + "confirmed collection does not have: " + o + "\nTest: " + getCollection()
                        + "\nReal: " + getConfirmed());
            }
        }

        // make sure there aren't any unmatched values
        for (int i = 0; i < confirmedSize; i++) {
            if (!matched[i]) {
                // the collection didn't match all the confirmed values
                fail("Collection should contain all values that are in the confirmed collection"
                        + "\nTest: " + getCollection() + "\nReal: " + getConfirmed());
            }
        }

        final Iterator<String> iterator1 = getCollection().iterator();
        for (String e : getConfirmed()) {
            assertTrue(iterator1.hasNext());
            final Object o1 = iterator1.next();
            final Object o2 = e;
            assertEquals(o1, o2);
        }
    }

    //-----------------------------------------------------------------------
    /**
     *  Resets the {@link #collection} and {@link #confirmed} fields to empty
     *  collections.  Invoke this method before performing a modification
     *  test.
     */
    public void resetEmpty() {
        this.collection = makeObject();
        this.confirmed = makeConfirmedCollection();
    }

    /**
     *  Resets the {@link #collection} and {@link #confirmed} fields to full
     *  collections.  Invoke this method before performing a modification
     *  test.
     */
    public void resetFull() {
        this.collection = makeFullCollection();
        this.confirmed = makeConfirmedFullCollection();
    }


    //-----------------------------------------------------------------------

    /**
     * Returns an empty {@link java.util.ArrayList}.
     */
    public java.util.LinkedList<String> makeConfirmedCollection() {
        final java.util.LinkedList<String> list = new java.util.LinkedList<String>();
        return list;
    }

    /**
     * Returns a full {@link java.util.ArrayList}.
     */
    public java.util.LinkedList<String> makeConfirmedFullCollection() {
        final java.util.LinkedList<String> list = new java.util.LinkedList<String>();
        list.addAll(Arrays.asList(getFullElements()));
        return list;
    }

    /**
     * Returns {@link #makeObject()}.
     *
     * @return an empty queue to be used for testing
     */
    public  LinkedList<String> makeObject() {
        return new LinkedList<String>();
    }


    public LinkedList<String> makeFullCollection() {
        // only works if queue supports optional "addAll(Collection)"
        final LinkedList<String> queue = makeObject();
        queue.addAll(Arrays.asList(getFullElements()));
        return queue;
    }


    public LinkedList<String> getCollection() {
        return collection;
    }

    public java.util.LinkedList<String> getConfirmed() {
        return confirmed;
    }

    public String[] getFullElements() {
        return  new String[] {
                new String(""),
                new String("One"),
                "Three",
                "One",
                null,
                "Seven",
                "Eight",
                new String("Nine"),
                "Thirteen",
                "14",
                "15",
        };
    }

    //-----------------------------------------------------------------------

    @Test
    public void testQueueOffer() {
        final String[] elements = getFullElements();
        for (final String element : elements) {
            resetEmpty();
            final boolean r = getCollection().offer(element);
            getConfirmed().add(element);
            verify();
            assertTrue("Empty queue changed after add", r);
            assertEquals("Queue size is 1 after first add", 1, getCollection().size());
        }

        resetEmpty();
        int size = 0;
        for (final String element : elements) {
            final boolean r = getCollection().offer(element);
            getConfirmed().add(element);
            verify();
            if (r) {
                size++;
            }
            assertEquals("Queue size should grow after add", size, getCollection().size());
            assertTrue("Queue should contain added element", getCollection().contains(element));
        }
    }

    @Test
    public void testQueueElement() {
        resetEmpty();

        try {
            getCollection().element();
            fail("Queue.element should throw NoSuchElementException");
        } catch (final NoSuchElementException e) {
            // expected
        }

        resetFull();

        assertTrue(getConfirmed().contains(getCollection().element()));


        final int max = getFullElements().length;
        for (int i = 0; i < max; i++) {
            final String element = getCollection().element();

            assertTrue(getConfirmed().contains(element));

            getCollection().remove(element);
            getConfirmed().remove(element);

            verify();
        }

        try {
            getCollection().element();
            fail("Queue.element should throw NoSuchElementException");
        } catch (final NoSuchElementException e) {
            // expected
        }
    }

    @Test
    public void testQueuePeek() {
        resetEmpty();

        String element = getCollection().peek();
        assertNull(element);

        resetFull();

        final int max = getFullElements().length;
        for (int i = 0; i < max; i++) {
            element = getCollection().peek();

            assertTrue(getConfirmed().contains(element));

            getCollection().remove(element);
            getConfirmed().remove(element);

            verify();
        }

        element = getCollection().peek();
        assertNull(element);
    }

    @Test
    public void testQueuePoll() {
      resetEmpty();

        String element = getCollection().poll();
        assertNull(element);

        resetFull();

        final int max = getFullElements().length;
        for (int i = 0; i < max; i++) {
            element = getCollection().poll();
            final boolean success = getConfirmed().remove(element);
            assertTrue("poll should return correct element", success);
            verify();
        }

        element = getCollection().poll();
        assertNull(element);
    }

    @Test
    public void testAddFirst() {
        resetEmpty();

        String[] fullElements = getFullElements();
        for (int i = 0; i < fullElements.length; i++) {
            String element = fullElements[i];

            getCollection().addFirst(element);
            getConfirmed().addFirst(element);

            assertEquals(getCollection().getFirst(), element);
            assertEquals(getCollection().getFirst(), getConfirmed().getFirst());
            verify();
        }
    }

    @Test
    public void testAddLast() {
        resetEmpty();

        String[] fullElements = getFullElements();
        for (int i = 0; i < fullElements.length; i++) {
            String element = fullElements[i];

            getCollection().addLast(element);
            getConfirmed().addLast(element);

            assertEquals(getCollection().getLast(), element);
            assertEquals(getCollection().getLast(), getConfirmed().getLast());
            verify();
        }
    }

    @Test
    public void testGetFirst() {
        resetEmpty();

        try {
            getCollection().getFirst();
            fail("Queue.element should throw NoSuchElementException");
        } catch (final NoSuchElementException e) {
            // expected
        }

        resetFull();

        assertTrue(getConfirmed().contains(getCollection().getFirst()));


        final int max = getFullElements().length;
        for (int i = 0; i < max; i++) {
            final String element = getCollection().getFirst();

            assertTrue(getConfirmed().contains(element));

            getCollection().remove(element);
            getConfirmed().remove(element);

            verify();
        }

        try {
            getCollection().getFirst();
            fail("Queue.element should throw NoSuchElementException");
        } catch (final NoSuchElementException e) {
            // expected
        }
    }

    @Test
    public void testGetLast() {
        resetEmpty();

        try {
            getCollection().getLast();
            fail("Queue.element should throw NoSuchElementException");
        } catch (final NoSuchElementException e) {
            // expected
        }

        resetFull();

        assertTrue(getConfirmed().contains(getCollection().getLast()));


        final int max = getFullElements().length;
        for (int i = 0; i < max; i++) {
            final String element = getCollection().getLast();

            assertTrue(getConfirmed().contains(element));

            getCollection().remove(element);
            getConfirmed().remove(element);

            verify();
        }

        try {
            getCollection().getFirst();
            fail("Queue.element should throw NoSuchElementException");
        } catch (final NoSuchElementException e) {
            // expected
        }
    }

    @Test
    public void testRemoveFirst() {
        resetEmpty();

        try {
            getCollection().removeFirst();
            fail("Queue.element should throw NoSuchElementException");
        } catch (final NoSuchElementException e) {
            // expected
        }

        resetFull();

        for (int i = 0; i < getCollection().size(); i++) {

            String removeFirstCollection = getCollection().removeFirst();
            String removeFirstCollectionConfirmed = getConfirmed().removeFirst();

//            assertFalse(getCollection().contains(removeFirstCollection));
            assertEquals(removeFirstCollection, removeFirstCollectionConfirmed);
            verify();
        }
    }

    @Test
    public void testRemoveLast() {
        resetEmpty();

        try {
            getCollection().removeLast();
            fail("Queue.element should throw NoSuchElementException");
        } catch (final NoSuchElementException e) {
            // expected
        }

        resetFull();

        for (int i = 0; i < getCollection().size(); i++) {

            String removeLastCollection = getCollection().removeLast();
            String removeLastCollectionConfirmed = getConfirmed().removeLast();

//            assertFalse(getCollection().contains(removeLastCollection));
            assertEquals(removeLastCollection, removeLastCollectionConfirmed);
            verify();
        }
    }
}
