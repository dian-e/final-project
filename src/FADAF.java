import java.util.*;

@SuppressWarnings("rawtypes")
public class FADAF<K extends Comparable<? super K>, D> {

    private HashTable<K, DAFTree.DAFNode> hash;
    private DAFTree<K, D> tree;

    public FADAF(int capacity) {
        this.hash = new HashTable<>(10);
        this.tree = new DAFTree<>();
    }

    public int size() { return this.tree.size(); }

    public int nUniqueKeys() { return this.tree.nUniqueKeys(); }

    public boolean insert(K key, D data, int nCopy) {
        DAFTree<K, D>.DAFNode<K, D> node = this.tree.insert(key, data, nCopy);
        return this.hash.insert(key, node);
    }

    public int lookup(K key) {
        DAFTree<K, D>.DAFNode<K, D> data = this.hash.lookup(key);
        if (data == null) { return 0; }
        else { return data.count; }
    }

    public boolean remove(K key, int nCopy) {
        DAFTree<K, D>.DAFNode<K, D> node = this.tree.remove(key, nCopy);
        // key not present
        if (node == null) { return false; }
        else {
            // all counts of key removed
            if (node.count == 0) { this.hash.delete(key); }
            return true;
        }
    }

    public boolean removeAll(K key) {
        // key not present
        if (!this.hash.delete(key)) { return false; }
        else {
            this.tree.removeAll(key);
            return true;
        }
    }

    public boolean update(K key, D newData) {
        DAFTree<K, D>.DAFNode<K, D> newNode = this.tree.updateData(key, newData);
        if (newNode == null) { return false; }
        else {
            this.hash.update(key, newNode);
            return true;
        }
    }

    public List<K> getAllKeys(boolean allowDuplicate) {
        LinkedList<K> list = new LinkedList<>();
        Iterator<K> iter;
        if (allowDuplicate) { iter = this.tree.iterator(); }
        else { iter = this.tree.uniqueIterator(); }

        while (iter.hasNext()) { list.add(iter.next()); }
        return list;
    }

    public List<K> getUniqueKeysInRange(K lower, K upper) {
        LinkedList<K> list = new LinkedList<>();
        Iterator<K> iter = this.tree.uniqueIterator();

        while (iter.hasNext()) {
            K key = iter.next();
            if (key.compareTo(lower) > 0 && key.compareTo(upper) < 0) {
                list.add(key);
            } else if (key.compareTo(upper) >= 0) { break; }
        }
        return list;
    }

    public K getMinKey() {
        if (this.tree.getRoot() == null) { return null; }
        else {
            DAFTree<K, D>.DAFNode<K, D> curr = this.tree.getRoot();
            while (curr.left != null) { curr = curr.left; }
            return curr.key;
        }
    }

    public K getMaxKey() {
        if (this.tree.getRoot() == null) { return null; }
        else {
            DAFTree<K, D>.DAFNode<K, D> curr = this.tree.getRoot();
            while (curr.right != null) { curr = curr.right; }
            return curr.key;
        }
    }

}
