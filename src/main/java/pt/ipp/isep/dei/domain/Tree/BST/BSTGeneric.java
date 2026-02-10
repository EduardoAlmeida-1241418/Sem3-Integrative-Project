package pt.ipp.isep.dei.domain.Tree.BST;

import pt.ipp.isep.dei.domain.Tree.Node.Node;

import java.util.*;

public class BSTGeneric<E extends Comparable<E>> implements BSTInterface<E> {

    protected Node<E> root;

    public BSTGeneric() {
        root = null;
    }

    protected Node<E> root() {
        return root;
    }

    public boolean isEmpty() {
        return root == null;
    }

    protected Node<E> find(Node<E> node, E element) {
        if (node == null)
            return null;
        int compared = element.compareTo(node.getElement());
        if (compared == 0)
            return node;
        if (compared < 0)
            return find(node.getLeft(), element);
        return find(node.getRight(), element);
    }

    public void insert(E element) {
        if (element == null)
            return;
        root = insert(element, root);
    }

    private Node<E> insert(E element, Node<E> node) {
        if (node == null)
            return new Node<>(element, null, null);
        int compared = element.compareTo(node.getElement());
        if (compared == 0) {
            node.setElement(element);
            return node;
        }
        if (compared > 0) {
            node.setRight(insert(element, node.getRight()));
            return node;
        }
        node.setLeft(insert(element, node.getLeft()));
        return node;
    }

    public void remove(E element) {
        root = remove(element, root());
    }

    private Node<E> remove(E element, Node<E> node) {
        if (node == null)
            return null;

        int compared = element.compareTo(node.getElement());

        if (compared == 0) {
            if (node.getLeft() == null && node.getRight() == null)
                return null;
            if (node.getLeft() == null)
                return node.getRight();
            if (node.getRight() == null)
                return node.getLeft();

            E min = smallestElement(node.getRight());
            node.setElement(min);
            node.setRight(remove(min, node.getRight()));
        } else if (compared < 0) {
            node.setLeft(remove(element, node.getLeft()));
        } else {
            node.setRight(remove(element, node.getRight()));
        }
        return node;
    }

    public int size() {
        return size(root);
    }

    private int size(Node<E> node) {
        if (node == null)
            return 0;
        return size(node.getLeft()) + size(node.getRight()) + 1;
    }

    public int height() {
        return height(root);
    }

    protected int height(Node<E> node) {
        if (node == null)
            return -1;
        int heightLeft = height(node.getLeft()) + 1;
        int heightRight = height(node.getRight()) + 1;
        if (heightLeft < heightRight)
            return heightRight;
        return heightLeft;
    }

    public E smallestElement() {
        return smallestElement(root);
    }

    protected E smallestElement(Node<E> node) {
        if (node.getLeft() != null)
            return smallestElement(node.getLeft());
        return node.getElement();
    }

    public Iterable<E> inOrder() {
        List<E> snapshot = new ArrayList<>();
        if (root != null)
            inOrderSubtree(root, snapshot);
        return snapshot;
    }

    private void inOrderSubtree(Node<E> node, List<E> snapshot) {
        if (node == null)
            return;
        inOrderSubtree(node.getLeft(), snapshot);
        snapshot.add(node.getElement());
        inOrderSubtree(node.getRight(), snapshot);
    }

    public Iterable<E> preOrder() {
        List<E> snapshot = new ArrayList<>();
        if (root != null)
            preOrderSubtree(root, snapshot);
        return snapshot;
    }

    private void preOrderSubtree(Node<E> node, List<E> snapshot) {
        if (node == null)
            return;
        snapshot.add(node.getElement());
        preOrderSubtree(node.getLeft(), snapshot);
        preOrderSubtree(node.getRight(), snapshot);
    }

    public Iterable<E> posOrder() {
        List<E> snapshot = new ArrayList<>();
        if (root != null)
            posOrderSubtree(root, snapshot);
        return snapshot;
    }

    private void posOrderSubtree(Node<E> node, List<E> snapshot) {
        if (node == null)
            return;
        posOrderSubtree(node.getLeft(), snapshot);
        posOrderSubtree(node.getRight(), snapshot);
        snapshot.add(node.getElement());
    }

    public Map<Integer, List<E>> nodesByLevel() {
        Map<Integer, List<E>> map = new HashMap<>();
        processBstByLevel(root, map, 0);
        return map;
    }

    private void processBstByLevel(Node<E> node, Map<Integer, List<E>> result, int level) {
        if (node == null)
            return;
        if (!result.containsKey(level)) {
            List<E> list = new ArrayList<>();
            list.add(node.getElement());
            result.put(level, list);
        } else {
            List<E> list = result.get(level);
            list.add(node.getElement());
            result.put(level, list);
        }
        processBstByLevel(node.getLeft(), result, level + 1);
        processBstByLevel(node.getRight(), result, level + 1);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        toStringRec(root, 0, sb);
        return sb.toString();
    }

    private void toStringRec(Node<E> root, int level, StringBuilder sb) {
        if (root == null)
            return;
        toStringRec(root.getRight(), level + 1, sb);
        if (level != 0) {
            for (int i = 0; i < level - 1; i++)
                sb.append("|\t");
            sb.append("|-------").append(root.getElement()).append("\n");
        } else {
            sb.append(root.getElement()).append("\n");
        }
        toStringRec(root.getLeft(), level + 1, sb);
    }
}
