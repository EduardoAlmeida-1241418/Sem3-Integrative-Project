package pt.ipp.isep.dei.controller.traficManager.pathRelated;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pt.ipp.isep.dei.data.config.ActualDatabaseConnection;
import pt.ipp.isep.dei.data.config.DatabaseConnection;
import pt.ipp.isep.dei.data.memory.*;
import pt.ipp.isep.dei.domain.Facility;
import pt.ipp.isep.dei.domain.trackRelated.RailwayLine;
import pt.ipp.isep.dei.domain.transportationRelated.Freight;
import pt.ipp.isep.dei.domain.transportationRelated.Path;
import pt.ipp.isep.dei.domain.transportationRelated.Route;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsável pela criação manual de caminhos
 * no gestor de tráfego.
 *
 * Permite ao utilizador construir passo a passo um caminho,
 * associá-lo a uma rota e acompanhar o estado das mercadorias.
 */
public class TrafficManagerPathsManualController {

    /**
     * Store de linhas ferroviárias em memória.
     */
    private RailwayLineStoreInMemory railwayLineStoreInMemory;

    /**
     * Store de instalações em memória.
     */
    private FacilityStoreInMemory facilityStoreInMemory;

    /**
     * Store de caminhos em memória.
     */
    private PathStoreInMemory pathStoreInMemory;

    /**
     * Ligação ativa à base de dados.
     */
    private DatabaseConnection db = ActualDatabaseConnection.getDb();

    /**
     * Lista de instalações que compõem o caminho.
     */
    private List<Facility> facilityPath = new ArrayList<>();



    /**
     * Rota atualmente escolhida.
     */
    private Route chosenRoute;

    /**
     * Construtor do controller.
     *
     * Inicializa todas as stores necessárias.
     */
    public TrafficManagerPathsManualController() {
        railwayLineStoreInMemory = new RailwayLineStoreInMemory();
        facilityStoreInMemory = new FacilityStoreInMemory();
        pathStoreInMemory = new PathStoreInMemory();
    }

    /**
     * Obtém a rota escolhida.
     *
     * @return rota selecionada
     */
    public Route getChosenRoute() {
        return chosenRoute;
    }

    /**
     * Define a rota escolhida.
     *
     * @param chosenRoute rota
     */
    public void setChosenRoute(Route chosenRoute) {
        this.chosenRoute = chosenRoute;
    }

    /**
     * Obtém as instalações adjacentes a uma dada instalação.
     *
     * @param facility instalação de referência
     * @return lista observável de instalações adjacentes
     */
    public ObservableList<Facility> getAdjacentStations(Facility facility) {
        if (facility == null) {
            return FXCollections.observableArrayList();
        }

        List<RailwayLine> allLines = new ArrayList<>(railwayLineStoreInMemory.findAll());
        List<Facility> allFacilities = new ArrayList<>(facilityStoreInMemory.findAll());

        List<Facility> adjacent = new ArrayList<>();

        for (RailwayLine line : allLines) {

            if (line.getStartFacilityId() == facility.getId()) {
                for (Facility f : allFacilities) {
                    if (f.getId() == (line.getEndFacilityId())) {
                        adjacent.add(f);
                        break;
                    }
                }
            }

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
     * Determina o estado de uma mercadoria ao longo do caminho atual.
     *
     * @param freight mercadoria a analisar
     * @return estado da mercadoria
     */
    public String findStatus(Freight freight) {

        Facility origin = freight.getOriginFacility();
        Facility destination = freight.getDestinationFacility();

        int originIndex = -1;
        int destinationIndex = -1;

        for (int i = 0; i < facilityPath.size(); i++) {
            Facility s = facilityPath.get(i);

            // Guardar apenas a PRIMEIRA vez que passa pela ORIGIN
            if (originIndex == -1 && s.getId() == origin.getId()) {
                originIndex = i;
            }

            // Guardar SEMPRE a ÚLTIMA vez que passa pela DESTINATION
            if (s.getId() == destination.getId()) {
                destinationIndex = i; // <- reatribui sempre
            }
        }

        // Ainda não passou pela origem
        if (originIndex == -1) {
            return "Not Collected";
        }

        // Já passou pela origem mas não pela final
        if (destinationIndex == -1) {
            return "Collected";
        }

        // Passou pela origem antes da última passagem pela final
        if (originIndex < destinationIndex) {
            return "Delivered";
        }

        return "Collected";
    }

    /**
     * Obtém o caminho atual de instalações.
     *
     * @return lista de instalações
     */
    public List<Facility> getFacilityPath() {
        return facilityPath;
    }

    /**
     * Obtém todas as instalações disponíveis.
     *
     * @return lista de instalações
     */
    public List<Facility> getAllFacilities() {
        return new ArrayList<>(facilityStoreInMemory.findAll());
    }

    /**
     * Obtém a lista de mercadorias associadas à rota escolhida.
     *
     * @return lista de mercadorias
     */
    public List<Freight> getFreightOrderList() {
        return chosenRoute.getFreights();
    }

    /**
     * Adiciona uma instalação ao caminho.
     *
     * @param facility instalação a adicionar
     */
    public void addFacility(Facility facility) {
        this.facilityPath.add(facility);
    }

    /**
     * Remove a última instalação do caminho.
     */
    public void removeLastFacility() {
        this.facilityPath.remove(facilityPath.size()-1);
    }

    /**
     * Associa o caminho construído à rota escolhida
     * e guarda-o na base de dados.
     */
    public void assignPath() {
        Path path = new Path(facilityPath);
        chosenRoute.setPath(path);

        pathStoreInMemory.save(db, path);

    }
}
