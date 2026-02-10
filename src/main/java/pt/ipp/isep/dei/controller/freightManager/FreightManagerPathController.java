package pt.ipp.isep.dei.controller.freightManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pt.ipp.isep.dei.data.memory.*;
import pt.ipp.isep.dei.data.repository.Repositories;
import pt.ipp.isep.dei.domain.*;
import pt.ipp.isep.dei.domain.trackRelated.RailwayLine;
import pt.ipp.isep.dei.domain.transportationRelated.Freight;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsável pela definição do percurso (path) de um comboio,
 * associando instalações, fretes, locomotivas e dados temporais.
 */
public class FreightManagerPathController {

    /**
     * Repositórios em memória utilizados pelo controller.
     */
    private FreightStoreInMemory freightStoreInMemory;
    private RailwayLineStoreInMemory railwayLineStoreInMemory;
    private TrainStoreInMemory trainStoreInMemory;
    private FacilityStoreInMemory facilityStoreInMemory;
    private SidingStoreInMemory sidingStoreInMemory;

    /**
     * Informação associada ao comboio em criação.
     */
    private Operator operator;
    private Date date;
    private Time time;
    private List<Locomotive> locomotiveList;
    private List<Freight> freightOrderList = new ArrayList<>();

    /**
     * Lista de instalações que representa o percurso do comboio.
     */
    private List<Facility> facilityPath = new ArrayList<>();

    /**
     * Construtor do FreightManagerPathController.
     * Inicializa todos os repositórios em memória necessários.
     */
    public FreightManagerPathController() {
        freightStoreInMemory = new FreightStoreInMemory();
        railwayLineStoreInMemory = new RailwayLineStoreInMemory();
        trainStoreInMemory = new TrainStoreInMemory();
        sidingStoreInMemory = new SidingStoreInMemory();
        facilityStoreInMemory = new FacilityStoreInMemory();
    }

    /**
     * Carrega a informação necessária para a criação do comboio.
     *
     * @param operator operador responsável
     * @param date data associada
     * @param time hora associada
     * @param locomotiveList lista de locomotivas
     * @param freightOrderList lista de fretes
     */
    public void loadControllerInfo(Operator operator, Date date, Time time, List<Locomotive> locomotiveList, List<Freight> freightOrderList){
        this.operator = operator;
        this.date = date;
        this.time = time;
        this.locomotiveList = locomotiveList;
        this.freightOrderList = freightOrderList;
    }

    /**
     * Obtém o operador associado.
     *
     * @return operador
     */
    public Operator getOperator() {
        return operator;
    }

    /**
     * Define o operador responsável.
     *
     * @param operator operador
     */
    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    /**
     * Obtém a data associada.
     *
     * @return data
     */
    public Date getDate() {
        return date;
    }

    /**
     * Define a data associada.
     *
     * @param date data
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Obtém a hora associada.
     *
     * @return hora
     */
    public Time getTime() {
        return time;
    }

    /**
     * Define a hora associada.
     *
     * @param time hora
     */
    public void setTime(Time time) {
        this.time = time;
    }

    /**
     * Obtém a lista de locomotivas.
     *
     * @return lista de locomotivas
     */
    public List<Locomotive> getLocomotiveList() {
        return locomotiveList;
    }

    /**
     * Define a lista de locomotivas.
     *
     * @param locomotiveList lista de locomotivas
     */
    public void setLocomotiveList(List<Locomotive> locomotiveList) {
        this.locomotiveList = locomotiveList;
    }

    /**
     * Obtém a lista de fretes.
     *
     * @return lista de fretes
     */
    public List<Freight> getFreightOrderList() {
        return freightOrderList;
    }

    /**
     * Define a lista de fretes.
     *
     * @param freightOrderList lista de fretes
     */
    public void setFreightOrderList(List<Freight> freightOrderList) {
        this.freightOrderList = freightOrderList;
    }

    /**
     * Adiciona uma instalação ao percurso.
     *
     * @param facility instalação a adicionar
     */
    public void addFacility(Facility facility) {
        this.facilityPath.add(facility);
    }

    /**
     * Remove a última instalação do percurso.
     */
    public void removeLastFacility() {
        this.facilityPath.remove(facilityPath.size()-1);
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
     * Obtém as estações adjacentes a uma instalação fornecida.
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
     * Determina o estado de um frete com base no percurso atual.
     *
     * @param freight frete a avaliar
     * @return estado do frete
     */
    public String findStatus(Freight freight) {

        Facility origin = freight.getOriginFacility();
        Facility destination = freight.getDestinationFacility();

        int originIndex = -1;
        int destinationIndex = -1;

        for (int i = 0; i < facilityPath.size(); i++) {
            Facility s = facilityPath.get(i);

            if (originIndex == -1 && s.getId() == origin.getId()) {
                originIndex = i;
            }

            if (s.getId() == destination.getId()) {
                destinationIndex = i;
            }
        }

        if (originIndex == -1) {
            return "Not Collected";
        }

        if (destinationIndex == -1) {
            return "Collected";
        }

        if (originIndex < destinationIndex) {
            return "Delivered";
        }

        return "Collected";
    }

    /**
     * Cria o comboio com base na informação definida.
     */
    public void createTrain() {
        /*
        Código intencionalmente comentado
         */
    }

    /**
     * Obtém o percurso atual.
     *
     * @return lista de instalações
     */
    public List<Facility> getFacilityPath() {
        return facilityPath;
    }
}
