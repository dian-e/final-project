import org.junit.*;
import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class DAFTreeTest {

    DAFTree tree1;
    DAFTree tree2;
    Iterator iter2;

    @Before
    public void setUp() {
        // tree1
        tree1 = new DAFTree();

        Character[] keys1 = new Character[]{'L', 'D', 'A', 'J', 'O', 'M', 'N', 'R', 'V', 'Z'};
        Integer[] data1 = new Integer[]{50, 40, 32, 42, 60, 54, 55, 80, 100, 120};
        int[] counts1 = {5, 6, 1, 4, 3, 6, 6, 2, 6, 6};
        for (int i = 0; i < keys1.length; i++) {
            assertEquals(keys1[i], tree1.insert(keys1[i], data1[i], counts1[i]).key);
        }

        assertEquals(45, tree1.size());
        assertEquals(10, tree1.nUniqueKeys());

        assertEquals('D', tree1.insert('D', 22, 2).key);
        assertEquals(10, tree1.insert('D', 0, 2).count);
        assertEquals('M', tree1.insert('M', 12, 1).key);
        assertEquals(8, tree1.insert('M', -18, 1).count);

        assertEquals(51, tree1.size());
        assertEquals(10, tree1.nUniqueKeys());

        assertEquals('D', tree1.insertDuplicate('D', 1).key);
        assertEquals(14, tree1.insertDuplicate('D', 3).count);
        assertEquals('Z', tree1.insertDuplicate('Z', 1).key);
        assertEquals(8, tree1.insertDuplicate('Z', 1).count);
        assertEquals(null, tree1.insertDuplicate('B', 4));
        assertEquals(null, tree1.insertDuplicate('E', 1));

        assertEquals(57, tree1.size());
        assertEquals(10, tree1.nUniqueKeys());

        assertEquals('D', tree1.updateData('D', 38).key);
        assertEquals(36, tree1.updateData('D', 36).data);
        assertEquals('O', tree1.updateData('O', 60).key);
        assertEquals(56, tree1.updateData('Z', 56).data);
        assertEquals(null, tree1.updateData('C', 4));
        assertEquals(null, tree1.updateData('Y', 1));

        assertEquals('A', tree1.findExtreme(false).key);
        assertEquals('A', tree1.findExtreme(false).key);
        assertEquals('Z', tree1.findExtreme(true).key);

        assertEquals(null, tree1.lookup('T'));
        assertEquals(1, tree1.lookup('A').count);
        assertEquals(0, tree1.remove('A', 2).count);
        assertEquals(null, tree1.lookup('A'));
        assertEquals('R', tree1.remove('R', 2).key);
        assertEquals(null, tree1.lookup('R'));
        assertEquals('O', tree1.remove('O', 3).key);
        assertEquals(null, tree1.lookup('O'));
        assertEquals('J', tree1.remove('J', 5).key);
        assertEquals(null, tree1.lookup('J'));
        assertEquals(0, tree1.remove('L', 12).count);
        assertEquals(null, tree1.lookup('L'));

        assertEquals(5, tree1.nUniqueKeys());
        assertEquals('M', tree1.getRoot().key);
        assertEquals('D', tree1.lookup('M').left.key);
        assertEquals('Z', tree1.remove('Z', 2).key);
        assertEquals(1, tree1.remove('Z', 5).count);
        assertEquals(1, tree1.remove('D', 13).count);

        assertEquals(0, tree1.remove('D', 1).count);
//        assertEquals('D', tree1.lookup('M').left);
//        assertEquals('D', tree1.getRoot().left.key);
//        assertEquals(null, tree1.lookup('D').left);
//        assertEquals(null, tree1.lookup('D').right);
//        assertEquals('M', tree1.lookup('D').parent.key);
//        assertEquals(null, tree1.lookup('M').left);
        assertEquals(null, tree1.lookup('D'));

        assertEquals(2, tree1.remove('M', 6).count);
        assertEquals(5, tree1.remove('V', 1).count);


        // tree2
        tree2 = new DAFTree();

        String[] keys2 = new String[]{"String", "STRING", "ST", "Str"};
        Double[] data2 = new Double[]{0.6, -10.2, -20.202, -4.8};
        int[] counts2 = {1, 2, 4, 1};
        for (int j = 0; j < keys2.length; j++) {
            assertEquals(keys2[j], tree2.insert(keys2[j], data2[j], counts2[j]).key);
        }

        assertEquals(8, tree2.size());
        assertEquals(4, tree2.nUniqueKeys());

        assertEquals(5, tree2.insert("ST", 22.0, 1).count);
        assertEquals("SS", tree2.insert("SS", 220, 2).key);

        assertEquals(11, tree2.size());
        assertEquals(5, tree2.nUniqueKeys());


        // test iterator
        iter2 = tree2.iterator();
    }

    // test exceptions
    @Test (expected = NullPointerException.class)
    public void testInsertKeyThrowsNPE() { tree1.insert(null, 2, 2); }
    @Test (expected = NullPointerException.class)
    public void testInsertDataThrowsNPE() { tree1.insert("Str", null, 2); }
    @Test (expected = IllegalArgumentException.class)
    public void testInsertThrowsIAE() { tree1.insert("Str", 2, 0); }

    @Test (expected = NullPointerException.class)
    public void testInsertDuplicateThrowsNPE() { tree1.insertDuplicate(null, 2); }
    @Test (expected = IllegalArgumentException.class)
    public void testInsertDuplicateThrowsIAE() { tree1.insertDuplicate("Str", -1); }

    @Test (expected = NullPointerException.class)
    public void testLookupThrowsNPE() { tree1.lookup(null); }

    @Test (expected = NullPointerException.class)
    public void testUpdateDataKeyThrowsNPE() { tree1.updateData(null, 0); }
    @Test (expected = NullPointerException.class)
    public void testUpdateDataDataNPE() { tree1.updateData("Str", null); }

    @Test (expected = NullPointerException.class)
    public void testRemoveThrowsNPE() { tree1.remove(null, 4); }
    @Test (expected = IllegalArgumentException.class)
    public void testRemoveThrowsIAE() { tree1.remove("Str", 0); }

    @Test
    public void testIterator() {
        String[] inOrder = new String[]{"SS", "SS", "ST", "ST", "ST", "ST", "ST", "STRING", "STRING", "Str", "String"};
        for (String item : inOrder) {
            assertTrue(iter2.hasNext());
            assertEquals(item, iter2.next());
        }
//        while (iter2.hasNext()) { System.out.println(iter2.next()); }
        assertFalse(iter2.hasNext());
    }

    @Test (expected = NoSuchElementException.class)
    public void testNextThrowsNSEE() {
        tree2.remove("SS", 5);
        tree2.remove("ST", 5);
        tree2.remove("STRING", 5);
        iter2 = tree2.iterator();
        assertEquals("Str", iter2.next());
        assertEquals("String", iter2.next());

        iter2.next();
    }

}
