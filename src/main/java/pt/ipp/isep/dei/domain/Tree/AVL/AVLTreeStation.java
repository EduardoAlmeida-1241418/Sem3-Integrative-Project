package pt.ipp.isep.dei.domain.Tree.AVL;

import pt.ipp.isep.dei.domain.Country;
import pt.ipp.isep.dei.domain.ESINF.StationEsinf;
import pt.ipp.isep.dei.domain.TimeZoneGroup;
import pt.ipp.isep.dei.domain.Tree.TZGKey;
import pt.ipp.isep.dei.domain.Tree.Node.TreeNode;

import java.util.ArrayList;
import java.util.List;

public class AVLTreeStation<E extends Comparable<E>> extends AVLTree<E, TreeNode<E>> {
    private final int LONG_LIMIT = 180;
    private final int LAT_LIMIT = 90;

    // ########################
    // ### Inserção de dados ###
    // ########################

    // Complexidade total = O(log(n))
    public void insert(E element, StationEsinf s, boolean isAscendingOrder) {

        if(s.getLongitude() < -LONG_LIMIT || s.getLongitude() > LONG_LIMIT || s.getLatitude() < -LAT_LIMIT || s.getLatitude() > LAT_LIMIT){
            return;
        }

        if (s.getStationName() == null || s.getStationName().isEmpty()){
            return;
        }

        if (s.getCountry() == null){
            return;
        }

        if (s.getTimeZoneGroup() == null){
            return;
        }

        if (s.getTimeZone() == null){
            return;
        }

        root = insertRec(root, element, s, isAscendingOrder);     // Complexidade de linha = O(log(n))
    }

    // Complexidade total média = O(log(n))
    // Complexidade total pior Caso = O(log(n) + s)
    // s = nº de estações dentro do node
    private TreeNode<E> insertRec(TreeNode<E> node, E element, StationEsinf station, boolean isAscendingOrder) {
        if (node == null)
            return new TreeNode<>(element, station);

        int cmp = element.compareTo(node.getElement());
        if (cmp < 0)
            node.setLeft(insertRec(node.getLeft(), element, station, isAscendingOrder));       // Complexidade de linha = O(log(n)) + O(1) = O(log(n))
        else if (cmp > 0)
            node.setRight(insertRec(node.getRight(), element, station, isAscendingOrder));     // Complexidade de linha = O(log(n)) + O(1) = O(log(n))
        else {
            node.addStation(station, isAscendingOrder);                                        // Complexidade de linha = O(s)
            return node;
        }

        updateHeight(node);     // Complexidade de linha = O(1)
        return balance(node);   // Complexidade de linha = O(1)
    }

    // #####################
    // ### Imprimir tudo ###
    // #####################

    // complexidade total = O(n)
    public void printInOrder() {
        printInOrderRec(root);
    }

    // complexidade total = O(n)    <- Explora todos os nodes
    private void printInOrderRec(TreeNode<E> node) {
        if (node == null) return;
        printInOrderRec(node.getLeft());
        System.out.println("Node: " + node.getElement()
                + " | Stations: " + node.getStations().size());
        printInOrderRec(node.getRight());
    }

    // complexidade total = O(1)
    public TreeNode<E> getRoot() {
        return root;
    }

    //#################
    //# Area Searches #
    //#################

    // Complexidade total = O(log(n) + m)
    public List<StationEsinf> searchCoordRange(double min, double max) {
        List<StationEsinf> result = new ArrayList<>();
        searchCoordRangeRec(root, min, max, result);    // Complexidade de linha =  O(log(n) + m)
        return result;
    }

    // Complexidade total = O(log(n) + m + s)
    // m = nº de nós no intervalo [min,max]
    // s = nº total de estações copiadas para o resultado
    // log(n) = procura pelo primeiro elemento no intervalo
    private void searchCoordRangeRec(TreeNode<E> node, double min, double max, List<StationEsinf> result) {
        if (node == null) return;

        double key = ((Number) node.getElement()).doubleValue();

        if (key > min)
            searchCoordRangeRec(node.getLeft(), min, max, result);

        if (key >= min && key <= max)
            result.addAll(node.getStations());  // Complexidade de linha = O(s)

        if (key < max)
            searchCoordRangeRec(node.getRight(), min, max, result);
    }

    //########################
    //# TZG + Country Search #
    //########################

    // Complexidade total = O(log(n) + m + s)
    public List<StationEsinf> searchTZG(Country country, TimeZoneGroup tz1, TimeZoneGroup tz2) {
        List<StationEsinf> result = new ArrayList<>();

        // Definir limites do intervalo de TZ
        TimeZoneGroup minTZ = null, maxTZ = null;
        if (tz1 != null && tz2 != null) {
            minTZ = (tz1.compareTo(tz2) <= 0) ? tz1 : tz2;
            maxTZ = (tz1.compareTo(tz2) >= 0) ? tz1 : tz2;
        } else if (tz1 != null) {
            minTZ = maxTZ = tz1;   // pesquisa de 1 único time zone
        } else if (tz2 != null) {
            minTZ = maxTZ = tz2;
        }
        // se ambos forem null -> sem filtro de TZ

        searchTZGRec(root, country, minTZ, maxTZ, result);  // Complexidade de linha = O(log(n) + m + s)
        return result;
    }

    // Complexidade total = O(log(n) + m + s)
    // m = nº de nós no intervalo [min,max]
    // s = nº total de estações copiadas para o resultado
    // log(n) = procura pelo primeiro elemento no intervalo
    private void searchTZGRec(TreeNode<E> node, Country country, TimeZoneGroup minTZ, TimeZoneGroup maxTZ, List<StationEsinf> result) {

        if (node == null) return;

        TZGKey key = (TZGKey) node.getElement();
        TimeZoneGroup keyTZ = key.getTz();          // Complexidade de linha = O(1)
        Country keyCountry = key.getCountry();      // Complexidade de linha = O(1)

        boolean matchTZ = (minTZ == null || keyTZ.compareTo(minTZ) >= 0) && (maxTZ == null || keyTZ.compareTo(maxTZ) <= 0); // Complexidade de linha = O(1)
        boolean matchCountry = (country == null || keyCountry.compareTo(country) == 0); // Complexidade de linha = O(1)


        if (minTZ != null) {
            TZGKey minKey = new TZGKey(minTZ, new Country(""));
            if (key.compareTo(minKey) > 0)
                searchTZGRec(node.getLeft(), country, minTZ, maxTZ, result);
        } else {
            searchTZGRec(node.getLeft(), country, minTZ, maxTZ, result);
        }

        if (matchTZ && matchCountry)
            result.addAll(node.getStations());

        if (maxTZ != null) {
            TZGKey maxKey = new TZGKey(maxTZ, new Country("ZZZ")); // placeholder high value
            if (key.compareTo(maxKey) < 0)
                searchTZGRec(node.getRight(), country, minTZ, maxTZ, result);
        } else {
            searchTZGRec(node.getRight(), country, minTZ, maxTZ, result);
        }
    }

    // ###########################
    // # Tree para list Ordenada #
    // ###########################

    // Complexidade total = O(n)
    public List<StationEsinf> getListAscendingOrder() {
        List<StationEsinf> result = new ArrayList<>();
        if (root == null) return result;
        getListAscendingOrderRec(root, result); // Complexidade de linha = O(n)
        return result;
    }

    // Complexidade total = O(n)
    private void getListAscendingOrderRec(TreeNode<E> node, List<StationEsinf> result) {
        if (node.getLeft() != null)
            getListAscendingOrderRec(node.getLeft(), result);

        result.addAll(node.getStations());

        if (node.getRight() != null)
            getListAscendingOrderRec(node.getRight(), result);
    }

    public void setRoot(TreeNode<E> root) {
        this.root = root;
    }

    public List<List<StationEsinf>> getStationsByNode() {
        List<List<StationEsinf>> result = new ArrayList<>();
        getStationsByNodeRec(root, result);
        return result;
    }

    private void getStationsByNodeRec(TreeNode<E> node, List<List<StationEsinf>> result) {
        if (node == null) return;
        getStationsByNodeRec(node.getLeft(), result);
        result.add(node.getStations());
        getStationsByNodeRec(node.getRight(), result);
    }
}
