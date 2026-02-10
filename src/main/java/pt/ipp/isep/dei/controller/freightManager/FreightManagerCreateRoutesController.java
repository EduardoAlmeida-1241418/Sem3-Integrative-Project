package pt.ipp.isep.dei.controller.freightManager;

import javafx.collections.ObservableList;
import pt.ipp.isep.dei.data.memory.FacilityStoreInMemory;
import pt.ipp.isep.dei.data.memory.RailwayLineStoreInMemory;
import pt.ipp.isep.dei.data.memory.RouteStoreInMemory;
import pt.ipp.isep.dei.domain.Facility;
import pt.ipp.isep.dei.domain.trackRelated.RailwayLine;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsável pela criação de rotas no contexto do Freight Manager,
 * permitindo selecionar instalações e obter ligações ferroviárias adjacentes.
 */
public class FreightManagerCreateRoutesController {

    /**
     * Repositório em memória responsável pela gestão das instalações.
     */
    private FacilityStoreInMemory facilityStoreInMemory;

    /**
     * Repositório em memória responsável pela gestão das linhas ferroviárias.
     */
    private RailwayLineStoreInMemory railwayLineStoreInMemory;

    /**
     * Repositório em memória responsável pela gestão das rotas.
     */
    private RouteStoreInMemory routeStoreInMemory;

    /**
     * Lista que representa a rota atualmente em construção.
     */
    private List<Facility> actualRoute = new ArrayList<>();

    /**
     * Construtor da classe FreightManagerCreateRoutesController.
     * Inicializa os repositórios necessários.
     */
    public FreightManagerCreateRoutesController() {
        initRepos();
    }

    /**
     * Inicializa os repositórios em memória utilizados pelo controller.
     */
    private void initRepos() {
        this.facilityStoreInMemory = new FacilityStoreInMemory();
        this.railwayLineStoreInMemory = new RailwayLineStoreInMemory();
        this.routeStoreInMemory = new RouteStoreInMemory();
    }

    /**
     * Obtém todas as instalações existentes.
     *
     * @return lista de instalações
     */
    public List<Facility> getAllFacilities() {
        return new ArrayList<>(facilityStoreInMemory.findAll());
    }

    /**
     * Obtém as instalações adjacentes a uma instalação fornecida,
     * com base nas linhas ferroviárias existentes.
     *
     * @param facility instalação de referência
     * @return lista observável de instalações adjacentes
     */
    public ObservableList<Facility> getAdjacentFacilities(Facility facility) {
        if (facility == null) {
            return javafx.collections.FXCollections.observableArrayList();
        }

        List<RailwayLine> allLines = new ArrayList<>(railwayLineStoreInMemory.findAll());
        List<Facility> allFacilities = new ArrayList<>(facilityStoreInMemory.findAll());

        List<Facility> adjacent = new ArrayList<>();

        for (RailwayLine line : allLines) {

            // Caso 1: facility é o start → adicionar end
            if (line.getStartFacilityId()== facility.getId()) {
                for (Facility f : allFacilities) {
                    if (f.getId() == (line.getEndFacilityId())) {
                        adjacent.add(f);
                        break;
                    }
                }
            }

            // Caso 2: facility é o end → adicionar start
            if (line.getEndFacilityId() == facility.getId()) {
                for (Facility f : allFacilities) {
                    if (f.getId() == line.getStartFacilityId()) {
                        adjacent.add(f);
                        break;
                    }
                }
            }
        }

        return javafx.collections.FXCollections.observableArrayList(adjacent);
    }

    /**
     * Adiciona uma instalação à rota atual.
     *
     * @param facility instalação a adicionar
     */
    public void addFacility(Facility facility) {
        this.actualRoute.add(facility);
    }

    /**
     * Cria uma rota com base na seleção fornecida.
     *
     * @param selectedItem item selecionado que define o tipo de rota
     */
    public void createRoute(String selectedItem) {
        /*
        RouteType routeType = RouteType.valueOf(selectedItem);

        Route route = new Route(routeType, actualRoute);

        routeStoreInMemory.save(null, route);

         */
    }

    /**
     * Reinicia a rota atual, removendo todas as instalações selecionadas.
     */
    public void reset() {
        actualRoute.clear();
    }

    /**
     * Obtém a rota atualmente em construção.
     *
     * @return lista de instalações da rota atual
     */
    public List<Facility> getActualRoute() {
        return actualRoute;
    }
}
