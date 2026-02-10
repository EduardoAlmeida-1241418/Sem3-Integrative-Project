package pt.ipp.isep.dei.controller.freightManager.routeRelated;

import pt.ipp.isep.dei.data.memory.RouteStoreInMemory;
import pt.ipp.isep.dei.domain.transportationRelated.Route;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsável pela visualização das rotas existentes.
 */
public class ViewRouteController {

    /**
     * Repositório em memória responsável pela gestão das rotas.
     */
    private RouteStoreInMemory routeStoreInMemory;


    /**
     * Construtor da classe ViewRouteController.
     * Inicializa os repositórios necessários.
     */
    public ViewRouteController() {
        initRepos();
    }

    /**
     * Inicializa o repositório de rotas em memória.
     */
    private void initRepos() {
        this.routeStoreInMemory = new RouteStoreInMemory();
    }

    /**
     * Obtém todas as rotas existentes.
     *
     * @return lista de rotas
     */
    public List<Route> getRoutes() {
        return new ArrayList<>(routeStoreInMemory.findAll());
    }

}
