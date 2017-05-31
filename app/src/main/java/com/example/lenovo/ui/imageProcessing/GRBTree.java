package com.example.lenovo.ui.imageProcessing;

/**
 * Created by lenovo on 2016/11/5.
 */

public class GRBTree {
    int iCount;
    int[] treeValue;
    GRBTreeNode root;
    boolean BLACK = false;
    boolean RED = true;

    public GRBTree() {
        root = null;
    }

    public GRBTree(int i) {
        Free();
    }

    public class GRBTreeNode {
        int key;
        boolean color;
        GRBTreeNode left, right, parent;

        public GRBTreeNode(int newKey, boolean newColor, GRBTreeNode newLeft, GRBTreeNode newRight) {
            key = newKey;
            color = newColor;
            left = newLeft;
            right = newRight;
            if (left != null)
                left.parent = this;
            if (right != null)
                right.parent = this;
            parent = null;
        }

        public GRBTreeNode() {
            if (left != null)
                left = null;
            if (right != null)
                right = null;
        }
    }

    void Free() {
        if (root != null)
            root = null;
    }

    void Dereference() {
        root = null;
    }

    public GRBTreeNode Grandparent(GRBTreeNode n) {
        if (n == null || n.parent == null)
            return null;
        return n.parent.parent;
    }

    public GRBTreeNode Sibling(GRBTreeNode n) {
        if (n == null || n.parent == null)
            return null;
        if (n == n.parent.left)
            return n.parent.right;
        else
            return n.parent.left;
    }

    public GRBTreeNode Uncle(GRBTreeNode n) {
        if (n == null)
            return null;
        return Sibling(n.parent);
    }

    boolean ColorOf(GRBTreeNode n) {
        return n == null ? BLACK : n.color;
    }

    public GRBTreeNode Find(int keyToFind) {
        GRBTreeNode n = root;
        while (n != null) {
            if (keyToFind == n.key)
                return n;
            if (keyToFind < n.key)
                n = n.left;
            else
                n = n.right;
        }
        return n;
    }

    void Replace(GRBTreeNode oldNode, GRBTreeNode newNode) {
        if (oldNode.parent == null)
            root = newNode;
        else {
            if (oldNode == oldNode.parent.left)
                oldNode.parent.left = newNode;
            else
                oldNode.parent.right = newNode;
        }
        if (newNode != null)
            newNode.parent = oldNode.parent;
    }

    void RotateLeft(GRBTreeNode n) {
        GRBTreeNode r = n.right;
        Replace(n, r);
        n.right = r.left;
        if (r.left != null)
            r.left.parent = n;
        r.left = n;
        n.parent = r;
    }

    void RotateRight(GRBTreeNode n) {
        GRBTreeNode l = n.left;
        Replace(n, l);
        n.left = l.right;
        if (l.right != null)
            l.right.parent = n;
        l.right = n;
        n.parent = l;
    }

    void Insert1(GRBTreeNode n) {
        if (n.parent == null)
            n.color = BLACK;
        else
            Insert2(n);
    }

    void Insert2(GRBTreeNode n) {
        if (n.parent.color != BLACK)
            Insert3(n);
    }

    void Insert3(GRBTreeNode n) {
        GRBTreeNode u = Uncle(n);
        GRBTreeNode g = Grandparent(n);
        if (ColorOf(u) == RED) {
            n.parent.color = BLACK;
            u.color = BLACK;
            g.color = RED;
            Insert1(g);
        } else
            Insert4(n);
    }

    void Insert4(GRBTreeNode n) {
        GRBTreeNode g = Grandparent(n);
        if (n == n.parent.right && n.parent == g.left) {
            RotateLeft(n.parent);
            n = n.left;
        } else if (n == n.parent.left && n.parent == g.right) {
            RotateRight(n.parent);
            n = n.right;
        }
        Insert5(n);
    }

    void Insert5(GRBTreeNode n) {
        GRBTreeNode g = Grandparent(n);
        n.parent.color = BLACK;
        g.color = RED;
        if (n == n.parent.left && n.parent == g.left)
            RotateRight(g);
        else
            RotateLeft(g);
    }

    boolean Insert(int newKey) {
        GRBTreeNode insNode = new GRBTreeNode(newKey, RED, null, null);
        GRBTreeNode n;

        if (root == null)
            root = insNode;
        else {
            n = root;
            while (true) {
                if (newKey == n.key) {
                    return false;
                }

                if (newKey < n.key) {
                    if (n.left == null) {
                        n.left = insNode;
                        break;
                    } else
                        n = n.left;
                } else {
                    if (n.right == null) {
                        n.right = insNode;
                        break;
                    } else
                        n = n.right;
                }
            }
            insNode.parent = n;
        }

        Insert1(insNode);

        return true;
    }

    public GRBTreeNode Maximum(GRBTreeNode n) {
        if (n == null)
            return null;

        while (n.right != null)
            n = n.right;
        return n;
    }

    public GRBTreeNode Minimum(GRBTreeNode n) {
        if (n == null)
            return null;

        while (n.left != null)
            n = n.left;
        return n;
    }

    void Delete1(GRBTreeNode n) {
        if (n.parent != null)
            Delete2(n);
    }

    void Delete2(GRBTreeNode n) {
        GRBTreeNode s = Sibling(n);

        if (ColorOf(s) == RED) {
            n.parent.color = RED;
            s.color = BLACK;

            if (n == n.parent.left) {
                RotateLeft(n.parent);
            } else {
                RotateRight(n.parent);
            }
        }
        Delete3(n);
    }

    void Delete3(GRBTreeNode n) {
        GRBTreeNode s = Sibling(n);

        if (ColorOf(n.parent) == BLACK && ColorOf(s) == BLACK &&
                ColorOf(s.left) == BLACK && ColorOf(s.right) == BLACK) {
            s.color = RED;
            Delete1(n.parent);
        } else
            Delete4(n);
    }

    void Delete4(GRBTreeNode n) {
        GRBTreeNode s = Sibling(n);

        if (ColorOf(n.parent) == RED && ColorOf(s) == BLACK &&
                ColorOf(s.left) == BLACK && ColorOf(s.right) == BLACK) {
            s.color = RED;
            n.parent.color = BLACK;
        } else
            Delete5(n);
    }

    void Delete5(GRBTreeNode n) {
        GRBTreeNode s = Sibling(n);

        if (n == n.parent.left &&
                ColorOf(s) == BLACK && ColorOf(s.left) == RED && ColorOf(s.right) == BLACK) {
            s.color = RED;
            s.left.color = BLACK;
            RotateRight(s);
        } else if (n == n.parent.right &&
                ColorOf(s) == BLACK && ColorOf(s.right) == RED && ColorOf(s.left) == BLACK) {
            s.color = RED;
            s.right.color = BLACK;
            RotateLeft(s);
        }
        Delete6(n);
    }

    void Delete6(GRBTreeNode n) {
        GRBTreeNode s = Sibling(n);

        s.color = ColorOf(n.parent);
        n.parent.color = BLACK;
        if (n == n.parent.left) {
            s.right.color = BLACK;
            RotateLeft(n.parent);
        } else {
            s.left.color = BLACK;
            RotateRight(n.parent);
        }
    }

    void Delete(int keyToDel) {
        GRBTreeNode c;
        GRBTreeNode n = Find(keyToDel);
        if (n == null)
            return;

        if (n.left != null && n.right != null) {
            GRBTreeNode pred = Maximum(n.left);
            n.key = pred.key;
            n = pred;
        }

        c = n.right == null ? n.left : n.right;
        if (ColorOf(n) == BLACK) {
            n.color = ColorOf(c);
            Delete1(n);
        }
        Replace(n, c);
        if (n.parent == null && c != null)
            c.color = BLACK;

        n.left = null;
        n.right = null;
    }

    /*   前序遍历的结果放到一个数组里面*/
    private void preOrder(GRBTreeNode n, int width, int height) {

        if (n != null) {
            treeValue[iCount] = n.key;
            iCount++;
            preOrder(n.left, width, height);
            preOrder(n.right, width, height);
        }

    }

    public int[] preOrder(int width, int height) {
        treeValue = new int[width * height];
        iCount = 0;
        preOrder(root, width, height);
        return treeValue;
    }

    public int getiCount() {
        return iCount;
    }
}