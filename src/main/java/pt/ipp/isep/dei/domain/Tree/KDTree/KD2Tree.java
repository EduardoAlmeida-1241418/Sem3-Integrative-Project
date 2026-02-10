package pt.ipp.isep.dei.domain.Tree.KDTree;

import pt.ipp.isep.dei.domain.Tree.BST.BSTGeneric;
import pt.ipp.isep.dei.domain.Tree.Node.Node;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class KD2Tree<E extends Comparable<E> & KDTreeNodeInterface> extends BSTGeneric<E> {

    public KD2Tree() {
        super();
    }

    protected static class KD2Node<E extends Comparable<E> & KDTreeNodeInterface> extends Node<E> {
        private final Point2D.Double coords;

        public KD2Node(E element) {
            super(element, null, null);
            this.coords = new Point2D.Double(element.getX(), element.getY());
        }

        public double getX() {
            return coords.x;
        }

        public double getY() {
            return coords.y;
        }

        public E getElement() {
            return super.getElement();
        }
    }

    private final Comparator<KD2Node<E>> cmpX = new Comparator<KD2Node<E>>() {
        @Override
        public int compare(KD2Node<E> p1, KD2Node<E> p2) {
            return Double.compare(p1.getX(), p2.getX());
        }
    };

    private final Comparator<KD2Node<E>> cmpY = new Comparator<KD2Node<E>>() {
        @Override
        public int compare(KD2Node<E> p1, KD2Node<E> p2) {
            return Double.compare(p1.getY(), p2.getY());
        }
    };

    private int compareByAxis(KD2Node<E> n1, KD2Node<E> n2, boolean divX) {
        if (divX) {
            return cmpX.compare(n1, n2);
        }
        return cmpY.compare(n1, n2);
    }

    @Override
    public void insert(E element) {
        if (element == null) return;

        KD2Node<E> newNode = new KD2Node<>(element);
        if (root == null) {
            root = newNode;
        } else {
            insert((KD2Node<E>) root, newNode, true);
        }
    }

    private void insert(KD2Node<E> currentNode, KD2Node<E> node, boolean divX) {
        if (node.getX() == currentNode.getX() && node.getY() == currentNode.getY()) {
            return;
        }

        int cmpResult = compareByAxis(currentNode, node, divX);
        if (cmpResult < 0) {
            if (currentNode.getLeft() == null) {
                currentNode.setLeft(node);
            } else {
                insert((KD2Node<E>) currentNode.getLeft(), node, !divX);
            }
        } else {
            if (currentNode.getRight() == null) {
                currentNode.setRight(node);
            } else {
                insert((KD2Node<E>) currentNode.getRight(), node, !divX);
            }
        }
    }

    public boolean contains(E element) {
        if (root == null || element == null)
            return false;
        return contains((KD2Node<E>) root, element, true);
    }

    private boolean contains(KD2Node<E> current, E element, boolean divX) {
        if (current == null)
            return false;

        if (current.getX() == element.getX() && current.getY() == element.getY())
            return true;

        int cmpResult = compareByAxis(current, new KD2Node<>(element), divX);
        if (cmpResult < 0)
            return contains((KD2Node<E>) current.getLeft(), element, !divX);
        else
            return contains((KD2Node<E>) current.getRight(), element, !divX);
    }

    // Nota **
    // Como a o metodo rangeSerach nao eh deterministico, só conseguimos identificar a complexidade no pior e melhor caso.
    //      * Pior caso: se range abranger quase toda a KdTree a complexidade será O(n),
    //      * Melhor caso: se range for muito pequeno e abranger  poucos nós, a complexidade será O(log n).
    public List<E> rangeSearch(double xMin, double xMax, double yMin, double yMax) {
        List<E> result = new ArrayList<>();                                             // complexidade da linha = O(1).
        rangeSearch((KD2Node<E>) root, xMin, xMax, yMin, yMax, true, result);      // vide Nota **.
        return result;
    }

    private void rangeSearch(KD2Node<E> current, double xMin, double xMax, double yMin, double yMax, boolean divX, List<E> result) {
        if (current == null)                           // complexidade da linha = O(1)
            return;                                    // complexidade da linha = O(1)

        double x = current.getX();                    // complexidade da linha = O(1)
        double y = current.getY();

        if (x >= xMin && x <= xMax && y >= yMin && y <= yMax) {             // complexidade da linha = O(1)
            result.add(current.getElement());                               // complexidade da linha = O(1)
        }

        if (divX) {
            if (xMin < x)                                                                                    // complexidade da linha = O(1).
                rangeSearch((KD2Node<E>) current.getLeft(), xMin, xMax, yMin, yMax, false, result);    // vide nota **

            if (xMax >= x)                                                                                   //complexidade da linha = O(1).
                rangeSearch((KD2Node<E>) current.getRight(), xMin, xMax, yMin, yMax, false, result);    // vide nota **

        } else {
            if (yMin < y)                                                                                   //complexidade da linha = O(1).
                rangeSearch((KD2Node<E>) current.getLeft(), xMin, xMax, yMin, yMax, true, result);    //vide nota **.

            if (yMax >= y)                                                                                 //complexidade da linha = O(1).
                rangeSearch((KD2Node<E>) current.getRight(), xMin, xMax, yMin, yMax, true, result);   //vide nota **
        }
    }



    public E findNearestNeighbour(double x, double y) {
        if (root == null)
            return null;

        KD2Node<E> best = (KD2Node<E>) root;
        best = findNearestNeighbour((KD2Node<E>) root, x, y, best, true);
        return best.getElement();
    }

    private KD2Node<E> findNearestNeighbour(KD2Node<E> node, double x, double y, KD2Node<E> bestNode, boolean divX) {
        if (node == null)
            return bestNode;

        double nodeDist = Point2D.distanceSq(node.getX(), node.getY(), x, y);
        double bestDist = Point2D.distanceSq(bestNode.getX(), bestNode.getY(), x, y);

        if (nodeDist < bestDist)
            bestNode = node;

        double delta;
        KD2Node<E> nearNode;
        KD2Node<E> farNode;

        if (divX) {
            delta = x - node.getX();
        } else {
            delta = y - node.getY();
        }
        double deltaSquared = delta * delta;

        if (delta < 0) {
            nearNode = (KD2Node<E>) node.getLeft();
            farNode = (KD2Node<E>) node.getRight();
        } else {
            nearNode = (KD2Node<E>) node.getRight();
            farNode = (KD2Node<E>) node.getLeft();
        }

        bestNode = findNearestNeighbour(nearNode, x, y, bestNode, !divX);
        double newBestDist = Point2D.distanceSq(bestNode.getX(), bestNode.getY(), x, y);
        if (deltaSquared < newBestDist) {
            bestNode = findNearestNeighbour(farNode, x, y, bestNode, !divX);
        }

        return bestNode;
    }

    private class DistanceComparator implements Comparator<KD2Node<E>> {
        private final double queryX;
        private final double queryY;

        public DistanceComparator(double x, double y) {
            this.queryX = x;
            this.queryY = y;
        }

        @Override
        public int compare(KD2Node<E> n1, KD2Node<E> n2) {
            double d1 = Point2D.distanceSq(n1.getX(), n1.getY(), queryX, queryY);
            double d2 = Point2D.distanceSq(n2.getX(), n2.getY(), queryX, queryY);
            return Double.compare(d2, d1);
        }
    }

    public List<E> findKNearestNeighbours(double x, double y, int k) {
        if (root == null || k <= 0)
            return new ArrayList<>();

        PriorityQueue<KD2Node<E>> heap = new PriorityQueue<>(k, new DistanceComparator(x, y));
        findKNearestNeighbours((KD2Node<E>) root, x, y, k, true, heap);

        List<E> result = new ArrayList<>(heap.size());
        while (!heap.isEmpty()) {
            result.addFirst(heap.poll().getElement());
        }
        return result;
    }

    public void findKNearestNeighbours(KD2Node<E> node, double x, double y, int k, boolean divX, PriorityQueue<KD2Node<E>> heap) {
        if (node == null)
            return;

        double distSq = Point2D.distanceSq(node.getX(), node.getY(), x, y);

        if (heap.size() < k) {
            heap.offer(node);
        } else {
            double farthestDist = Point2D.distanceSq(heap.peek().getX(), heap.peek().getY(), x, y);
            if (distSq < farthestDist) {
                heap.poll();
                heap.offer(node);
            }
        }

        double delta;
        KD2Node<E> nearNode;
        KD2Node<E> farNode;

        if (divX) {
            delta = x - node.getX();
        } else {
            delta = y - node.getY();
        }
        double deltaSquared = delta * delta;

        if (delta < 0) {
            nearNode = (KD2Node<E>) node.getLeft();
            farNode = (KD2Node<E>) node.getRight();
        } else {
            nearNode = (KD2Node<E>) node.getRight();
            farNode = (KD2Node<E>) node.getLeft();
        }

        findKNearestNeighbours(nearNode, x, y, k, !divX, heap);

        double farthestDistNow;
        if (heap.size() < k) {
            farthestDistNow = Double.MAX_VALUE;
        } else {
            KD2Node<E> farthest = heap.peek();
            farthestDistNow = Point2D.distanceSq(farthest.getX(), farthest.getY(), x, y);
        }

        if (deltaSquared < farthestDistNow) {
            findKNearestNeighbours(farNode, x, y, k, !divX, heap);
        }
    }
}