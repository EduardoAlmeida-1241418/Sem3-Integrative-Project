package pt.ipp.isep.dei.controller.freightManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pt.ipp.isep.dei.data.memory.FreightStoreInMemory;
import pt.ipp.isep.dei.data.memory.LocomotiveStoreInMemory;
import pt.ipp.isep.dei.data.memory.OperatorStoreInMemory;
import pt.ipp.isep.dei.data.memory.RouteStoreInMemory;
import pt.ipp.isep.dei.data.repository.*;
import pt.ipp.isep.dei.domain.Locomotive;
import pt.ipp.isep.dei.domain.Operator;
import pt.ipp.isep.dei.domain.Owner;
import pt.ipp.isep.dei.domain.transportationRelated.Freight;
import pt.ipp.isep.dei.domain.transportationRelated.Route;

/**
 * Controller responsável pela gestão de comboios no contexto do Freight Manager,
 * permitindo obter locomotivas, rotas, fretes e operadores disponíveis.
 */
public class FreightManagerTrainsController {

    /**
     * Repositório em memória de locomotivas.
     */
    private LocomotiveStoreInMemory locomotiveStoreInMemory;

    /**
     * Repositório em memória de rotas.
     */
    private RouteStoreInMemory routeStoreInMemory;

    /**
     * Repositório em memória de fretes.
     */
    private FreightStoreInMemory freightStoreInMemory;

    /**
     * Repositório em memória de operadores.
     */
    private OperatorStoreInMemory operatorStoreInMemory;

    /**
     * Construtor do FreightManagerTrainsController.
     * Inicializa os repositórios necessários.
     */
    public FreightManagerTrainsController() {
        initRepos();
    }

    /**
     * Inicializa todos os repositórios em memória utilizados pelo controller.
     */
    private void initRepos() {
        locomotiveStoreInMemory = new LocomotiveStoreInMemory();
        routeStoreInMemory = new RouteStoreInMemory();
        freightStoreInMemory = new FreightStoreInMemory();
        operatorStoreInMemory = new OperatorStoreInMemory();
    }

    /**
     * Obtém todas as locomotivas existentes.
     *
     * @return lista observável de locomotivas
     */
    public ObservableList<Locomotive> getAllLocomotives() {
        return FXCollections.observableArrayList(locomotiveStoreInMemory.findAll());
    }

    /**
     * Obtém todas as rotas existentes.
     *
     * @return lista observável de rotas
     */
    public ObservableList<Route> getAllRoutes() {

        int counter = 0;
        return FXCollections.observableArrayList(routeStoreInMemory.findAll());

    }

    /**
     * Obtém todos os fretes existentes.
     *
     * @return lista observável de fretes
     */
    public ObservableList<Freight> getAllFreights() {
        return FXCollections.observableArrayList(freightStoreInMemory.findAll());

    }

    /**
     * Obtém todos os operadores existentes.
     *
     * @return lista observável de operadores
     */
    public ObservableList<Operator> getAllOperators() {
        return FXCollections.observableArrayList(operatorStoreInMemory.findAll());
    }
}
