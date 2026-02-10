package pt.ipp.isep.dei.domain.Tree.AVL;

import pt.ipp.isep.dei.domain.Tree.Node.GenericNode;

public class AVLTree<E extends Comparable<E>, N extends GenericNode<E, N>> {

    protected N root;

    // Complexidade Total = O(1)
    protected int height(N node) {
        return node == null ? 0 : node.getHeight();
    }

    // Complexidade Total = O(1)
    protected void updateHeight(N node) {
        node.setHeight(1 + Math.max(height(node.getLeft()), height(node.getRight())));
    }

    // Complexidade Total = O(1)
    protected int balanceFactor(N node) {
        return height(node.getLeft()) - height(node.getRight());
    }

    // Complexidade Total = O(1)
    protected N rotateRight(N y) {
        N x = y.getLeft();      // Complexidade de Linha = O(1)
        N t2 = x.getRight();
        x.setRight(y);          // Complexidade de Linha = O(1)
        y.setLeft(t2);
        updateHeight(y);        // Complexidade de Linha = O(1)
        updateHeight(x);
        return x;
    }

    // Complexidade Total = O(1)
    protected N rotateLeft(N x) {
        N y = x.getRight();       // Complexidade de Linha = O(1)
        N t2 = y.getLeft();
        y.setLeft(x);             // Complexidade de Linha = O(1)
        x.setRight(t2);
        updateHeight(x);          // Complexidade de Linha = O(1)
        updateHeight(y);
        return y;
    }

    // Complexidade Total = O(1)
    protected N balance(N node) {
        int bf = balanceFactor(node);                       // Complexidade de Linha = O(1)
        if (bf > 1) {
            if (balanceFactor(node.getLeft()) < 0)
                node.setLeft(rotateLeft(node.getLeft()));   // Complexidade de Linha = O(1) + O(1) = O(1)
            return rotateRight(node);                       // Complexidade de Linha = O(1)
        }
        if (bf < -1) {
            if (balanceFactor(node.getRight()) > 0)
                node.setRight(rotateRight(node.getRight()));
            return rotateLeft(node);
        }
        return node;
    }

    // Complexidade total = O(log(n))
    public N search(E key) {
        return searchRecursive(root, key);  // Complexidade de linha = O(log(n))
    }

    // Complexidade total = O(log(n))
    private N searchRecursive(N node, E key) {
        if (node == null || node.getElement().compareTo(key) == 0)
            return node;

        if (key.compareTo(node.getElement()) < 0)
            return searchRecursive(node.getLeft(), key);
        else
            return searchRecursive(node.getRight(), key);
    }

    public N getRoot() { return root; }
    public boolean isEmpty() { return root == null; }
}
