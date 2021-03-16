import org.junit.*;
import static org.junit.Assert.*;

public class HashTableTest {

    HashTable table1;
    HashTable table2;
    Integer[] keys1, data2;
    String[] data1, keys2;

    @Test (expected = IllegalArgumentException.class)
    public void testConstructorThrowsIAE() { HashTable hash1 = new HashTable(9); }

    @Before
    public void setUp() {
        table1 = new HashTable(10);
        keys1 = new Integer[]{1, 2, 3, 4, 5, 6, 7, 0};
        data1 = new String[]{"one", "two", "three", "four", "five", "six", "seven", "zero"};
        for (int i = 0; i < keys1.length; i++) { table1.insert(keys1[i], data1[i]); }

        table2 = new HashTable(12);
        keys2 = new String[]{"AA", "BB", "CC", "DD", "EE", "FF", "GG"};
        data2 = new Integer[]{1, 2, 3, 4, 5, 6, 7};
    }

    @Test (expected = NullPointerException.class)
    public void testInsertKeyThrowsNPE() { table1.insert(null, "null"); }
    @Test (expected = NullPointerException.class)
    public void testInsertDataThrowsNPE() { table1.insert("null", null); }

    @Test (expected = NullPointerException.class)
    public void testUpdateKeyThrowsNPE() { table1.update(null, "null"); }
    @Test (expected = NullPointerException.class)
    public void testUpdateDataThrowsNPE() { table1.update("null", null); }

    @Test (expected = NullPointerException.class)
    public void testDeleteKeyThrowsNPE() { table1.delete(null); }

    @Test (expected = NullPointerException.class)
    public void testLookupKeyThrowsNPE() { table1.lookup(null); }

    @Test
    public void testInsert() {
        assertFalse(table1.insert(2, "should be false"));
        assertFalse(table1.insert(0, "false"));
        assertTrue(table1.insert(9, "truee"));

        for (int i = 0; i < keys2.length; i++) { assertTrue(table2.insert(keys2[i], data2[i])); }
        assertFalse(table2.insert("EE", "be false"));
        assertTrue(table2.insert("ZZ", "TRUE"));
    }

    @Test
    public void testUpdate() {
        assertTrue(table1.update(0, "zz"));
        assertFalse(table1.update(8, "zz"));
        assertFalse(table1.update(10, "zz"));

        assertFalse(table2.update("AA", 10));
    }

    @Test
    public void testDelete() {
        assertTrue(table1.delete(0));
        assertEquals(null, table1.lookup(0));

        assertFalse(table2.delete("GG"));
    }

    @Test
    public void testLookup() {
        assertEquals("zero", table1.lookup(0));
        assertEquals("one", table1.lookup(1));
        assertEquals(null, table1.lookup(8));

        assertEquals(null, table2.lookup("AA"));
        for (int i = 0; i < keys2.length; i++) { table2.insert(keys2[i], data2[i]); }
        assertEquals(2, table2.lookup("BB"));
    }

    @Test
    public void testSize() {
        assertEquals(8, table1.size());

        assertEquals(0, table2.size());
    }

    @Test
    public void testCapacity() {
        assertEquals(20, table1.capacity());

        assertEquals(12, table2.capacity());
        for (int i = 0; i < keys2.length; i++) { table2.insert(keys2[i], data2[i]); }
        assertEquals(12, table2.capacity());
        table2.insert("HH", 8);
        assertEquals(12, table2.capacity());
        table2.insert("II", 9);
        assertEquals(12, table2.capacity());
        table2.insert("JJ", 10);

        assertEquals(24, table2.capacity());
        assertEquals(6, table2.lookup("FF"));
        assertTrue(table2.delete("FF"));
        assertEquals(null, table2.lookup("FF"));
        assertFalse(table2.delete("FF"));
    }
}