import org.junit.*;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class FADAFTest {

    FADAF fadaf1;

    @Before
    public void setUp(){
        fadaf1 = new FADAF(10);

        String[] keys2 = new String[]{"String", "STRING", "ST", "Str"};
        Double[] data2 = new Double[]{0.6, -10.2, -20.202, -4.8};
        int[] counts2 = {1, 2, 4, 1};
        for (int j = 0; j < keys2.length; j++) {
            assertTrue(fadaf1.insert(keys2[j], data2[j], counts2[j]));
        }

        assertEquals(8, fadaf1.size());
        assertEquals(4, fadaf1.nUniqueKeys());

        assertFalse(fadaf1.insert("ST", 22.0, 1));
        assertTrue(fadaf1.insert("SS", 220, 2));

        assertEquals("SS", fadaf1.getMinKey());
        assertEquals("String", fadaf1.getMaxKey());

        assertEquals(11, fadaf1.size());
        assertEquals(5, fadaf1.nUniqueKeys());

        assertEquals(0, fadaf1.lookup("string"));
        assertEquals(1, fadaf1.lookup("String"));
        assertEquals(2, fadaf1.lookup("STRING"));
        assertEquals(5, fadaf1.lookup("ST"));
        assertEquals(1, fadaf1.lookup("Str"));
        assertEquals(2, fadaf1.lookup("SS"));
        assertEquals(0, fadaf1.lookup("STR"));

        assertTrue(fadaf1.update("ST", 20.202));
        assertTrue(fadaf1.update("String", 0.8));
        assertFalse(fadaf1.update("string", 0.2));
        assertFalse(fadaf1.update("STR", 0.2));
        assertEquals(5, fadaf1.lookup("ST"));
        assertEquals(1, fadaf1.lookup("String"));
        assertEquals(0, fadaf1.lookup("string"));
        assertEquals(0, fadaf1.lookup("string"));

        assertTrue(fadaf1.remove("SS", 1));
        assertEquals(1, fadaf1.lookup("SS"));
        assertTrue(fadaf1.update("SS", 0.04));

        assertTrue(fadaf1.remove("ST", 4));
        assertEquals(1, fadaf1.lookup("ST"));
        assertTrue(fadaf1.remove("ST", 1));
        assertEquals(0, fadaf1.lookup("ST"));

        assertTrue(fadaf1.remove("Str", 1));

        assertTrue(fadaf1.remove("String", 1));
        assertTrue(fadaf1.insert("String", 220, 2));
        assertFalse(fadaf1.insert("String", 22.0, 1));

        assertEquals(6, fadaf1.size());
        assertEquals(3, fadaf1.nUniqueKeys());

        assertTrue(fadaf1.removeAll("SS"));
        assertEquals(0, fadaf1.lookup("SS"));

        assertEquals("STRING", fadaf1.getMinKey());
        assertEquals("String", fadaf1.getMaxKey());

        assertEquals(2, fadaf1.lookup("STRING"));
        assertTrue(fadaf1.removeAll("STRING"));
        assertEquals(0, fadaf1.lookup("SS"));

        assertEquals(3, fadaf1.size());
        assertEquals(1, fadaf1.nUniqueKeys());

        assertEquals("String", fadaf1.getMinKey());
        assertEquals("String", fadaf1.getMaxKey());

        assertEquals(3, fadaf1.lookup("String"));
        assertTrue(fadaf1.removeAll("String"));
        assertEquals(0, fadaf1.lookup("String"));

        assertEquals(0, fadaf1.size());
        assertEquals(0, fadaf1.nUniqueKeys());

        assertEquals(null, fadaf1.getMinKey());
        assertEquals(null, fadaf1.getMaxKey());
    }

    @Test
    public void testIterator() {
        String[] inOrder = new String[]{"SS", "SS", "ST", "ST", "ST", "ST", "ST", "STRING", "STRING", "Str", "String"};
        List<String> fromIterator = fadaf1.getAllKeys(true);
        int i = 0;
        for (String key : fromIterator) {
            assertEquals(key, inOrder[i]);
            i++;
        }

        String[] inOrderUnique = new String[]{"SS", "ST", "STRING", "Str", "String"};
        List<String> fromIteratorUnique = fadaf1.getAllKeys(false);
        int j = 0;
        for (String key : fromIteratorUnique) {
            assertEquals(key, inOrderUnique[j]);
            j++;
        }

        String[] inRange = new String[]{"SS", "ST", "STRING", "Str", "String"};
        List<String> fromIteratorRange = fadaf1.getUniqueKeysInRange("AA", "Z");
        int k = 0;
        for (String key : fromIteratorRange) {
            assertEquals(key, inRange[k]);
            k++;
        }

        String[] inRange2 = new String[]{"STRING", "Str", "String"};
        List<String> fromIteratorRange2 = fadaf1.getUniqueKeysInRange("ST", "string");
        int l = 0;
        for (String key : fromIteratorRange2) {
            assertEquals(key, inRange2[l]);
            l++;
        }
    }

    // test exceptions
    @Test (expected = IllegalArgumentException.class)
    public void testConstructorThrowsIAE() { FADAF ffaaddaaff = new FADAF(9); }

    @Test (expected = NullPointerException.class)
    public void testInsertKeyThrowsNPE() { fadaf1.insert(null, 2, 2); }
    @Test (expected = NullPointerException.class)
    public void testInsertDataThrowsNPE() { fadaf1.insert("Str", null, 2); }
    @Test (expected = IllegalArgumentException.class)
    public void testInsertThrowsIAE() { fadaf1.insert("Str", 2, 0); }

    @Test (expected = NullPointerException.class)
    public void testLookupThrowsNPE() { fadaf1.lookup(null); }

    @Test (expected = NullPointerException.class)
    public void testUpdateDataKeyThrowsNPE() { fadaf1.update(null, 0); }
    @Test (expected = NullPointerException.class)
    public void testUpdateDataDataNPE() { fadaf1.update("Str", null); }

    @Test (expected = NullPointerException.class)
    public void testRemoveThrowsNPE() { fadaf1.remove(null, 4); }
    @Test (expected = IllegalArgumentException.class)
    public void testRemoveThrowsIAE() { fadaf1.remove("Str", 0); }

    @Test (expected = NullPointerException.class)
    public void testRemoveAllThrowsNPE() { fadaf1.removeAll(null); }

}
