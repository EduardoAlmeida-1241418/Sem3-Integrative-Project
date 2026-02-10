package pt.ipp.isep.dei.domain.Tree.BST;

import java.util.List;
import java.util.Map;

public interface BSTInterface<E> {
    boolean isEmpty();

    void insert(E var1);

    void remove(E var1);

    int size();

    int height();

    E smallestElement();

    Iterable<E> inOrder();

    Iterable<E> preOrder();

    Iterable<E> posOrder();

    Map<Integer, List<E>> nodesByLevel();
}
