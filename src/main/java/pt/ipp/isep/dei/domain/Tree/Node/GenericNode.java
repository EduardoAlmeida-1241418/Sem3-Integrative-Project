package pt.ipp.isep.dei.domain.Tree.Node;

public class GenericNode<E, N extends GenericNode<E, N>> {

    protected E element;
    protected N left;
    protected N right;
    protected int height = 1;

    public GenericNode(E element) {
        this.element = element;
    }

    public E getElement() { return element; }
    public void setElement(E element) { this.element = element; }

    public N getLeft() { return left; }
    public void setLeft(N left) { this.left = left; }

    public N getRight() { return right; }
    public void setRight(N right) { this.right = right; }

    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }
}
