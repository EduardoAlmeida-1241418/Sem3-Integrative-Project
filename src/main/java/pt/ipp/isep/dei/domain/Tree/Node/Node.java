package pt.ipp.isep.dei.domain.Tree.Node;

/**
 * Represents a generic node of a binary tree that holds an element
 * and references to its left and right children.
 *
 * @param <E> the type of the element stored in the node
 */
public class Node<E> {
    /**
     * Element stored in the node.
     */
    private E element;

    /**
     * Left child of the node (may be {@code null}).
     */
    private Node<E> left;

    /**
     * Right child of the node (may be {@code null}).
     */
    private Node<E> right;

    /**
     * Creates a new node with the element and references to the children.
     *
     * @param e the element to store in the node
     * @param leftChild reference to the left child (may be {@code null})
     * @param rightChild reference to the right child (may be {@code null})
     */
    public Node(E e, Node<E> leftChild, Node<E> rightChild) {
        element = e;
        left = leftChild;
        right = rightChild;
    }

    /**
     * Returns the element stored in the node.
     *
     * @return the element of this node (may be {@code null})
     */
    public E getElement() { return element; }

    /**
     * Returns the left child of this node.
     *
     * @return the left child node, or {@code null} if none
     */
    public Node<E> getLeft() { return left; }

    /**
     * Returns the right child of this node.
     *
     * @return the right child node, or {@code null} if none
     */
    public Node<E> getRight() { return right; }

    /**
     * Sets the element of this node.
     *
     * @param e the new element (may be {@code null})
     */
    public void setElement(E e) { element = e; }

    /**
     * Sets the left child of this node.
     *
     * @param leftChild the new left child (may be {@code null})
     */
    public void setLeft(Node<E> leftChild) { left = leftChild; }

    /**
     * Sets the right child of this node.
     *
     * @param rightChild the new right child (may be {@code null})
     */
    public void setRight(Node<E> rightChild) { right = rightChild; }
}