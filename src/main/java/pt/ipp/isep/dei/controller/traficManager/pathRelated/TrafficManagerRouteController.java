package pt.ipp.isep.dei.controller.traficManager.pathRelated;

import pt.ipp.isep.dei.data.memory.RouteStoreInMemory;
import pt.ipp.isep.dei.domain.transportationRelated.Route;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsável pela gestão de rotas disponíveis
 * no gestor de tráfego.
 *
 * Permite obter apenas as rotas que ainda não possuem
 * um caminho associado.
 */
public class TrafficManagerRouteController {

    /**
     * Store de rotas em memória.
     */
    private RouteStoreInMemory routeStoreInMemory;

    /**
     * Construtor do controller.
     *
     * Inicializa a store de rotas em memória.
     */
    public TrafficManagerRouteController() {
        routeStoreInMemory = new RouteStoreInMemory();
    }

    /**
     * Obtém a lista de rotas disponíveis para criação de caminhos.
     *
     * Apenas são devolvidas as rotas que ainda não têm
     * um caminho associado.
     *
     * @return lista de rotas disponíveis
     */
    public List<Route> getAvailableRoutes(){
        List<Route> routeList = new ArrayList<>();

        for (Route route : routeStoreInMemory.findAll()) {
            if (route.getPath() == null) {
                routeList.add(route);
            }
        }

        return routeList;
    }
}
