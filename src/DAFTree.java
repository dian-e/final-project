import java.util.*;

@SuppressWarnings("rawtypes")
public class DAFTree<K extends Comparable<? super K>, D> implements Iterable {

    private DAFNode<K, D> root;
    private int nElems;
    private int nUnique;

    protected class DAFNode<K extends Comparable<? super K>, D> {
        K key;
        D data;
        int count; // duplicate counter
        DAFNode<K, D> left, right, parent;

        public DAFNode(K key, D data) { this(key, data, 1); }

        public DAFNode(K key, D data, int nCopy) {
            if (key == null || data == null) { throw new NullPointerException(); }
            if (nCopy < 1) { throw new IllegalArgumentException(); }

            this.key = key;
            this.data = data;
            this.left = null;
            this.right = null;
            this.parent = null;
            this.count = nCopy;
        }
    }

    public DAFTree() {
        this.root = null;
        this.nElems = this.nUnique = 0;
    }

    public int size() { return this.nElems; }

    public int nUniqueKeys() { return this.nUnique; }

    public DAFNode<K, D> insert(K key, D data, int nCopy) {
        // NPE and IAE caught by DAFNode constructor
        DAFNode<K, D> node = new DAFNode(key, data, nCopy);

        // inserts node at root if tree is empty. otherwise, finds position to insert
        if (this.root == null) {
            this.root = node;
            this.nElems += nCopy;
            this.nUnique++;
            return this.root;
        }

        DAFNode<K, D> curr = this.root;
        while (curr != null) {
            int compared = key.compareTo(curr.key);

            // key already in table
            if (compared == 0) {
                curr.count += nCopy;
                this.nElems += nCopy;
                return curr;
            } else if (compared < 0) {
                // key not in table, or loop again on left side
                if (curr.left == null) {
                    curr.left = node;
                    curr.left.parent = curr;
                    this.nElems += nCopy;
                    this.nUnique++;
                    return curr.left;
                } else { curr = curr.left; }
            } else {
                if (curr.right == null) {
                    curr.right = node;
                    curr.right.parent = curr;
                    this.nElems += nCopy;
                    this.nUnique++;
                    return curr.right;
                } else { curr = curr.right; }
            }
        }
        return curr;
    }

    public DAFNode<K, D> insertDuplicate(K key, int nCopy) {
        if (key == null) { throw new NullPointerException(); }
        if (nCopy < 1) { throw new IllegalArgumentException(); }

        DAFNode<K, D> curr = this.root;
        while (curr != null) {
            int compared = key.compareTo(curr.key);

            // key in table
            if (compared == 0) {
                curr.count += nCopy;
                this.nElems += nCopy;
                break; }
            else if (compared < 0) { curr = curr.left; }
            else { curr = curr.right; }
        }
        return curr;
    }

    public DAFNode<K, D> lookup(K key) {
        if (key == null) { throw new NullPointerException(); }

        DAFNode<K, D> curr = this.root;
        while (curr != null) {
            int compared = key.compareTo(curr.key);

            if (compared == 0) { break; }
            else if (compared < 0) { curr = curr.left; }
            else { curr = curr.right; }
        }
        return curr;
    }

    public DAFNode<K, D> updateData(K key, D newData) {
        if (key == null || newData == null) { throw new NullPointerException(); }

        DAFNode<K, D> curr = this.root;
        while (curr != null) {
            int compared = key.compareTo(curr.key);

            if (compared == 0) {
                curr.data = newData;
                break; }
            else if (compared < 0) { curr = curr.left; }
            else { curr = curr.right; }
        }
        return curr;
    }

    public DAFNode<K, D> remove(K key, int nCopy) {
        if (key == null) { throw new NullPointerException(); }
        if (nCopy < 1) { throw new IllegalArgumentException(); }

        // node not in tree
        DAFNode<K, D> node = lookup(key);
        if (node == null) { return null; }

        // if node will remain, update count. otherwise, node will need to be removed
        if (nCopy < node.count) {
            node.count -= nCopy;
            this.nElems -= nCopy;
            return node;
        }

        // node is leaf
        if (node.left == null && node.right == null) {
            if (node.parent == null) { this.root = null; } // if node is root
            attachParent(node, null);
        }
        // node has only left child
        else if (node.right == null) {
            if (node.parent == null) { // if node is root
                this.root = node.left;
                node.left.parent = null;
            }
            attachParent(node, node.left);
        }
        // node has only right child
        else if (node.left == null) { // if node is root
            if (node.parent == null) {
                this.root = node.right;
                node.right.parent = null;
            }
            attachParent(node, node.right);
        }
        // node has 2 children
        else {
            DAFNode<K, D> curr = node.right;

            // first right child doesn't have a left child
            if (curr.left == null) {
                if (node.parent == null) {  // root removal
                    this.root = curr;
                    curr.parent = null;
                }
                else { attachParent(node, curr); }
                curr.left = node.left;
                curr.left.parent = curr;
            } else {
                while (curr.left != null) { curr = curr.left; }

                // if curr has a right child
                if (curr.right != null) { attachParent(curr, curr.right); }
                else { curr.parent.left = null; }

                curr.left = node.left;
                curr.right = node.right;
                curr.left.parent = curr;
                curr.right.parent = curr;
                curr.parent = node.parent;
                if (node.parent == null) { this.root = curr; } // root removal
            }
        }
        this.nUnique -= 1;
        this.nElems -= node.count;
        node.count = 0;
        return node;
    }

    private void attachParent(DAFNode<K, D> node, DAFNode<K, D> child) {
        if (node.parent == null && child == null) { this.root = null; }
        else if (node.parent == null) {
            this.root = child;
            child.parent = null;
        } else {
            DAFNode<K, D> parLChild = node.parent.left;
            if (parLChild != null && parLChild.equals(node)) { node.parent.left = child; }
            else { node.parent.right = child; }

            if (child != null) { child.parent = node.parent; }
        }
    }

    /* ASSUMES NODE IS IN TREE */
    public DAFNode<K, D> removeAll(K key) {
        DAFNode<K, D> node = lookup(key);

        // node is leaf
        if (node.left == null && node.right == null) {
            if (node.parent == null) { this.root = null; } // if node is root
            attachParent(node, null);
        }
        // node has only left child
        else if (node.right == null) {
            if (node.parent == null) { // if node is root
                this.root = node.left;
                node.left.parent = null;
            }
            attachParent(node, node.left);
        }
        // node has only right child
        else if (node.left == null) { // if node is root
            if (node.parent == null) {
                this.root = node.right;
                node.right.parent = null;
            }
            attachParent(node, node.right);
        }
        // node has 2 children
        else {
            DAFNode<K, D> curr = node.right;

            // first right child doesn't have a left child
            if (curr.left == null) {
                if (node.parent == null) {  // root removal
                    this.root = curr;
                    curr.parent = null;
                }
                else { attachParent(node, curr); }
                curr.left = node.left;
                curr.left.parent = curr;
            } else {
                while (curr.left != null) { curr = curr.left; }

                // if curr has a right child
                if (curr.right != null) { attachParent(curr, curr.right); }
                else { curr.parent.left = null; }

                curr.left = node.left;
                curr.right = node.right;
                curr.left.parent = curr;
                curr.right.parent = curr;
                curr.parent = node.parent;
                if (node.parent == null) { this.root = curr; } // root removal
            }
        }
        this.nUnique -= 1;
        this.nElems -= node.count;
        node.count = 0;
        return node;
    }

    public DAFNode<K, D> getRoot() { return this.root; }

    public DAFNode<K, D> findExtreme(boolean isMax) {
        if (this.root == null) { return null; }

        DAFNode<K, D> curr = this.root;
        if (isMax) {
            while (curr.right != null) { curr = curr.right; }
        } else {
            while (curr.left != null) { curr = curr.left; }
        }
        return curr;
    }

    public class DAFTreeIterator implements Iterator<K> {

        Stack<DAFNode<K, D>> stack;

        public DAFTreeIterator() {
            this.stack = new Stack();
            addLeftPath(getRoot());
        }

        public boolean hasNext() { return !this.stack.isEmpty(); }

        public K next() {
            DAFNode<K, D> popped;
            if (!hasNext()) { throw new NoSuchElementException(); }
            else {
                popped = this.stack.pop();
                // if iterator still has items & is done popping this key
                // adds left path of right child
                if (hasNext() && !stack.peek().equals(popped)) { addLeftPath(popped.right); }
            }
            return popped.key;
        }

        private void addLeftPath(DAFNode<K, D> curr) {
            // robust even if the given root is null
            while (curr != null) {
                int remaining = curr.count;
                while (remaining > 0) {
                    this.stack.push(curr);
                    remaining--;
                }
                curr = curr.left;
            }
        }
    }

    public Iterator<K> iterator() { return new DAFTreeIterator(); }

    public Iterator<K> uniqueIterator() { return new DAFTreeUniqueIterator(); }

    public class DAFTreeUniqueIterator implements Iterator<K> {

        Stack<DAFNode<K, D>> stack;

        public DAFTreeUniqueIterator() {
            this.stack = new Stack();
            addLeftPath(getRoot());
        }

        public boolean hasNext() { return !this.stack.isEmpty(); }

        public K next() {
            DAFNode<K, D> popped;
            if (!hasNext()) { throw new NoSuchElementException(); }
            else {
                popped = this.stack.pop();
                // adds left path of right child if applicable
                addLeftPath(popped.right);
            }
            return popped.key;
        }

        private void addLeftPath(DAFNode<K, D> curr) {
            // robust even if the given root is null
            while (curr != null) {
                this.stack.push(curr);
                curr = curr.left;
            }
        }
    }

}
