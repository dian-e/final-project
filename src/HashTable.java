import java.util.*;

@SuppressWarnings("rawtypes")
public class HashTable<K, D> {

    protected class TableEntry<K, D> {
        private K key;
        private D data;

        public TableEntry(K key, D data) {
            this.key = key;
            this.data = data;
        }

        public void setData(D newData) { this.data = newData; }

        public D getData() { return this.data; }

        public K getKey() { return this.key; }

        @Override
        public boolean equals(Object obj) {
            if ((obj == null) || !(obj instanceof TableEntry))
                return false;
            return key.equals(((TableEntry<?, ?>) obj).key);
        }

        @Override
        public int hashCode() {
            return key.hashCode();
        }
    }

    private LinkedList<TableEntry<K, D>>[] table;
    private int nElems;

    private static final int MIN_THRESHOLD = 10;
    private static final double MAX_LF = (double) 2 / 3;

    @SuppressWarnings("unchecked")
    public HashTable(int capacity) {
        if (capacity < MIN_THRESHOLD) { throw new IllegalArgumentException(); }
        this.table = new LinkedList[capacity];
        for (int i = 0; i < capacity; i ++) {
            table[i] = new LinkedList<TableEntry<K, D>>();
        }
        this.nElems = 0;
    }

    /**
     * Inserts the key-data pair, returning whether it is inserted. Rehashes before insertion if load factor > 2/3
     * @return true if the pair is inserted and false if the key is already present
     * @throws NullPointerException if key or data is null
     */
    public boolean insert(K key, D data) {
        if (key == null || data == null) { throw new NullPointerException(); }

        if (this.lookup(key)!= null) { return false; }
        else {
            double loadFactor = (double) this.size() / this.capacity();
            if (loadFactor > MAX_LF) { this.rehash(); }

            int bucket = hashValue(key);
            TableEntry entry = new TableEntry(key, data);
            this.table[bucket].add(entry);
            this.nElems++;
            return true;
        }
    }

    /**
     * Updates the data in the given key, returning whether it is updated
     * @return true if the data is updated and false if the key is not found
     * @throws NullPointerException if key or new data is null
     */
    public boolean update(K key, D newData) {
        if (key == null || newData == null) { throw new NullPointerException(); }

        int bucket = hashValue(key);
        LinkedList<TableEntry<K, D>> list = this.table[bucket];
        for (TableEntry entry : list) {
            if (entry.key.equals(key)) {
                entry.setData(newData);
                return true;
            }
        }
        return false;
    }

    /**
     * Deletes the entry of the given key, returning whether it is deleted
     * @return true if the pair is deleted and false if the key is not found
     * @throws NullPointerException if key is null
     */
    public boolean delete(K key) {
        if (key == null) { throw new NullPointerException(); }

        int bucket = hashValue(key);
        LinkedList<TableEntry<K, D>> list = this.table[bucket];

        int nEntry = 0;
        for (TableEntry entry : list) {
            if (entry.key.equals(key)) {
                list.remove(nEntry);
                this.nElems--;
                return true;
            }
            nEntry++;
        }
        return false;
    }

    /**
     * Returns the value associated with the given key, returning null if key is not found
     * @return true key's data or null if the key is already present
     * @throws NullPointerException if key is null
     */
    public D lookup(K key) {
        if (key == null) { throw new NullPointerException(); }

        int bucket = hashValue(key);
        LinkedList<TableEntry<K, D>> list = this.table[bucket];
        for (TableEntry<K, D> entry : list) {
            if (entry.key.equals(key)) {
                return entry.getData();
            }
        }
        return null;
    }

    /**
     * Returns the total number of entries stored in the hash table
     * @return the number of entries currently in the table
     */
    public int size() { return this.nElems; }

    /**
     * Returns the total capacity of the hash table
     * @return the the capacity of the table
     */
    public int capacity() { return this.table.length; }

    /**
     * Calculates the hash value (expected index) of the key in the hash table
     * @return true if the pair is inserted and false if the key is already present
     * @throws NullPointerException if key or data is null
     */
    private int hashValue(K key) {
        TableEntry<K, D> dummy = new TableEntry<>(key, null);
        return Math.abs(dummy.hashCode() % this.capacity());
    }

    @SuppressWarnings("unchecked")
    /**
     * Doubles the capacity of the original table and re-inserts all values
     */
    private void rehash() {
        LinkedList<TableEntry<K, D>>[] oldTable = new LinkedList[this.capacity()];
        for (int i = 0; i < this.table.length; i++) { oldTable[i] = this.table[i]; }

        this.table = new LinkedList[this.capacity() * 2];
        for (int i = 0; i < this.capacity(); i ++) {
            table[i] = new LinkedList<TableEntry<K, D>>();
        }
        this.nElems = 0;

        for (LinkedList<TableEntry<K, D>> list : oldTable) {
            for (TableEntry<K, D> entry : list)  {
                this.insert(entry.getKey(), entry.getData());
            }
        }
    }

}
