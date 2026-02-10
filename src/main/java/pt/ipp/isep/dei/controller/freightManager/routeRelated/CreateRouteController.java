package pt.ipp.isep.dei.controller.freightManager.routeRelated;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import pt.ipp.isep.dei.data.config.ActualDatabaseConnection;
import pt.ipp.isep.dei.data.memory.FreightStoreInMemory;
import pt.ipp.isep.dei.data.memory.RouteStoreInMemory;
import pt.ipp.isep.dei.domain.Date;
import pt.ipp.isep.dei.domain.transportationRelated.Freight;
import pt.ipp.isep.dei.domain.transportationRelated.Route;
import pt.ipp.isep.dei.domain.transportationRelated.RouteType;
import pt.ipp.isep.dei.domain.wagonRelated.Wagon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Controller responsável pela criação de rotas e gestão da associação
 * de fretes a uma rota no contexto do Freight Manager.
 */
public class CreateRouteController {

    /**
     * Lista que mantém a ordem dos fretes selecionados para a rota.
     */
    private List<Freight> freightOrderList = new ArrayList<>();

    /**
     * Repositório em memória responsável pela gestão de fretes.
     */
    private FreightStoreInMemory freightStoreInMemory;

    /**
     * Repositório em memória responsável pela gestão de rotas.
     */
    private RouteStoreInMemory routeStoreInMemory;

    /**
     * Construtor da classe CreateRouteController.
     * Inicializa os repositórios necessários.
     */
    public CreateRouteController() {
        initRepos();
    }

    /**
     * Inicializa os repositórios em memória utilizados pelo controller.
     */
    private void initRepos() {
        this.freightStoreInMemory = new FreightStoreInMemory();
        this.routeStoreInMemory = new RouteStoreInMemory();
    }

    /**
     * Obtém todos os fretes que ainda não foram utilizados em rotas.
     *
     * @return lista observável de fretes não utilizados
     */
    public ObservableList<Freight> getFreights() {
        return FXCollections.observableArrayList(freightStoreInMemory.findUnusedFreights());
    }

    /**
     * Filtra uma lista de fretes, retornando apenas aqueles cuja data
     * é anterior à data fornecida.
     *
     * @param date data de referência
     * @param freights lista de fretes a filtrar
     * @return coleção imutável de fretes anteriores à data
     * @throws IllegalArgumentException se a data for null
     */
    public Collection<Freight> findAllBefore(Date date, List<Freight> freights) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null.");
        }

        List<Freight> result = new ArrayList<>();

        for (Freight freight : freights) {
            if (freight.getDate().isBefore(date)) {
                result.add(freight);
            }
        }

        return Collections.unmodifiableList(result);
    }

    /**
     * Adiciona um frete à lista de fretes selecionados para a rota.
     *
     * @param selectedItem frete a adicionar
     */
    public void addFreight(Freight selectedItem) {
        freightOrderList.add(selectedItem);
    }

    /**
     * Obtém a lista de fretes associados à rota.
     *
     * @return lista de fretes pela ordem definida
     */
    public List<Freight> getFreightOrderList() {
        return freightOrderList;
    }

    /**
     * Define a lista de fretes associados à rota.
     *
     * @param freightOrderList nova lista de fretes
     */
    public void setFreightOrderList(List<Freight> freightOrderList) {
        this.freightOrderList = freightOrderList;
    }

    /**
     * Cria uma rota com base nos fretes selecionados e guarda-a no repositório.
     * O tipo de rota é determinado pelo número de fretes associados.
     */
    public void createRoute() {
        RouteType routeType;

        if (freightOrderList.size() == 1){
            routeType = RouteType.SIMPLE;
        } else {
            routeType = RouteType.COMPLEX;
        }

        Route route = new Route(routeType, freightOrderList);

        routeStoreInMemory.save(ActualDatabaseConnection.getDb(), route);
    }

    /**
     * Verifica se a combinação de um frete com os já selecionados é possível,
     * garantindo que não existem vagões repetidos.
     *
     * @param selected frete a validar
     * @param freightOrderTableView tabela com os fretes já selecionados
     * @return true se a combinação for possível, false caso contrário
     */
    public boolean combinationIsPossible(Freight selected, TableView<Freight> freightOrderTableView) {

        for (Freight freight : freightOrderTableView.getItems()) {
            for (Wagon wagon : selected.getWagons()) {
                if (freight.getWagons().contains(wagon)) return false;
            }
        }
        return true;
    }
}
