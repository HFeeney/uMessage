package datastructures.dictionaries;

import cse332.datastructures.trees.BinarySearchTree;
import cse332.interfaces.worklists.LIFOWorkList;
import datastructures.worklists.ArrayStack;

/**
 * AVLTree must be a subclass of BinarySearchTree<E> and must use
 * inheritance and calls to superclass methods to avoid unnecessary
 * duplication or copying of functionality.
 * <p>
 * 1. Create a subclass of BSTNode, perhaps named AVLNode.
 * 2. Override the insert method such that it creates AVLNode instances
 * instead of BSTNode instances.
 * 3. Do NOT "replace" the children array in BSTNode with a new
 * children array or left and right fields in AVLNode.  This will
 * instead mask the super-class fields (i.e., the resulting node
 * would actually have multiple copies of the node fields, with
 * code accessing one pair or the other depending on the type of
 * the references used to access the instance).  Such masking will
 * lead to highly perplexing and erroneous behavior. Instead,
 * continue using the existing BSTNode children array.
 * 4. Ensure that the class does not have redundant methods
 * 5. Cast a BSTNode to an AVLNode whenever necessary in your AVLTree.
 * This will result a lot of casts, so we recommend you make private methods
 * that encapsulate those casts.
 * 6. Do NOT override the toString method. It is used for grading.
 * 7. The internal structure of your AVLTree (from this.root to the leaves) must be correct
 */

public class AVLTree<K extends Comparable<? super K>, V> extends BinarySearchTree<K, V> {

    protected class AVLNode extends BSTNode {
        public int height;
        public AVLNode(K key, V value) {
            super(key, value);
            this.height = 0;
        }

        public AVLNode left() {
            return (AVLNode) this.children[0];
        }

        public AVLNode right() {
            return (AVLNode) this.children[1];
        }

        public void setLeft(AVLNode n) {
            this.children[0] = n;
        }

        public void setRight(AVLNode n) {
            this.children[1] = n;
        }

        public boolean balanced() {
            int leftHeight = left() == null ? -1 : left().height;
            int rightHeight = right() == null ? -1 : right().height;
            return Math.abs(leftHeight - rightHeight) < 2;
        }

        public void updateHeight() {
            int leftHeight = left() == null ? -1 : left().height;
            int rightHeight = right() == null ? -1 : right().height;
            this.height = Math.max(leftHeight, rightHeight) + 1;
        }
    }

    public AVLTree() {
        super();
    }

//    @Override
    public V insert(K key, V value) {
        // in event of an empty tree, value can be inserted at root
        //      return null since nothing is there
        if (root == null) {
            root = new AVLNode(key, value);
            size++;
            return null;
        }

        // look at root first
        AVLNode current = (AVLNode) root;
        // store all visited nodes
        LIFOWorkList<AVLNode> s = new ArrayStack<>();
        // record whether a new node has been placed in the tree (not replaced)
        boolean inserted = false;

        // keep track of which subtree of last visited node insertion is in (-1 = left, 1 = right)
        int subtree = 0;

        while (!inserted) {
            // compare the key we want to insert to the current node's value to determine direction
            int dir = (int) Math.signum(key.compareTo(current.key));

            // in the event that the keys are equal, we can replace the node's value and return the previous value
            if (dir == 0) {
                V previous = current.value;
                current.value = value;
                return previous;
            }

            // otherwise, this key was less (left) or greater (right)
            // go to left
            if (dir < 0 ) {
                if (current.left() == null) {
                    // insert a new node as current's left child if it doesn't have one
                    current.setLeft(new AVLNode(key, value));
                    inserted = true;
                    subtree = -1;
                } else {
                    // add this node to the list of visited ones, then advance to the left child
                    s.add(current);
                    current = current.left();
                }
            } else {
                if (current.right() == null) {
                    // insert a new node as current's right child if it doesn't have one
                    current.setRight(new AVLNode(key, value));
                    inserted = true;
                    subtree = 1;
                } else {
                    // add this node to the list of visited ones, then advance to the right child
                    s.add(current);
                    current = current.right();
                }
            }
        }

        // a node was inserted
        size++;

        // keep track of which subtree of child insertion is in
        int subtreeOfChild = 0;

        // we have inserted a new node and have a stack of previously visited nodes
        while (current.balanced()) {
            // every balanced node needs its height updated
            current.updateHeight();
            if (!s.hasWork()) {
                // we are at the root, everything is balanced
                // no replacement occurred so return null
                return null;
            }

            // this node will be child of next node considered
            // so the subtree of child insertion occurred in will be what is currently subtree
            subtreeOfChild = subtree;
            // new subtree is the relation of this node to the next node being considered
            subtree = (int) Math.signum(current.key.compareTo(s.peek().key));

            // look at the next previously visited node
            current = s.next();
        }

        // current now is an unbalanced node (root of unbalanced subtree)
        if (!s.hasWork()) {
            // we are at the root, reassign root to be balance of entire tree
            assert(root.equals(current));
            root = balance(current, subtree, subtreeOfChild);
        } else {
            // this subtree must be reassigned to the balanced version of itself
            // do this by reassigning parent's reference to this subtree to balanced version
            // we must determine whether this subtree is the parent's left or right
            AVLNode parent = s.next();
            int dir = (int) Math.signum(current.key.compareTo(parent.key));
            if (dir < 0) {
                parent.setLeft(balance(current, subtree, subtreeOfChild));
            } else {
                parent.setRight(balance(current, subtree, subtreeOfChild));
            }
        }

        return null;
    }

    /**
     * Balances a subtree given the root and where in the tree insertion occurred
     * @param t the node to balance relative to
     * @param subtree the child subtree of t insertion occurred in
     * @param subtreeOfChild the subtree of the child node insertion occurred in
     * @return balanced subtree that t was the root of
     */
    private AVLNode balance(AVLNode t, int subtree, int subtreeOfChild) {

        // CASE 1
        if (subtree == -1 && subtreeOfChild == -1) {
            t = rotateRight(t);
        }

        // CASE 2
        else if(subtree == -1 && subtreeOfChild == 1) {
            t.setLeft(rotateLeft(t.left()));
            t = rotateRight(t);
        }

        // CASE 3
        else if (subtree == 1 && subtreeOfChild == -1) {
            t.setRight(rotateRight(t.right()));
            t = rotateLeft(t);
        }

        // CASE 4
        else { // (subtree == 1 && subtreeOfChild == 1)
            t = rotateLeft(t);
        }

        return t;
    }

    /**
     * Adjusts a subtree with an imbalance caused by a node inserted in the left subtree
     * @param t the root of the subtree to adjust
     * @return the new root of the adjusted subtree
     */
    private AVLNode rotateRight(AVLNode t) {
        AVLNode ret = t.left();
        t.setLeft(ret.right());
        ret.setRight(t);
        t.updateHeight();
        ret.updateHeight();
        return ret;
    }

    /**
     * Adjusts a subtree with an imbalance caused by a node inserted in the right subtree
     * @param t the root of the subtree to adjust
     * @return the new root of the adjusted subtree
     */
    private AVLNode rotateLeft(AVLNode t) {
        AVLNode ret = t.right();
        t.setRight(ret.left());
        ret.setLeft(t);
        t.updateHeight();
        ret.updateHeight();
        return ret;
    }
}
