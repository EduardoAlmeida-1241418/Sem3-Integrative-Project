package pt.ipp.isep.dei.controller.freightManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pt.ipp.isep.dei.data.memory.FreightStoreInMemory;
import pt.ipp.isep.dei.data.repository.Repositories;
import pt.ipp.isep.dei.domain.*;
import pt.ipp.isep.dei.domain.trainRelated.Train;
import pt.ipp.isep.dei.domain.transportationRelated.Freight;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Controller responsável pela gestão de fretes no contexto do Freight Manager,
 * incluindo a seleção de fretes e criação de comboios.
 */
public class FreightManagerFreightController {

    /**
     * Operador responsável pela gestão do frete.
     */
    private Operator operator;

    /**
     * Data associada ao frete.
     */
    private Date date;

    /**
     * Hora associada ao frete.
     */
    private Time time;

    /**
     * Lista de locomotivas associadas ao comboio.
     */
    private List<Locomotive> locomotiveList;

    /**
     * Lista de fretes selecionados pela ordem definida.
     */
    private List<Freight> freightOrderList = new ArrayList<>();

    /**
     * Repositório em memória responsável pela gestão de fretes.
     */
    private FreightStoreInMemory freightStoreInMemory;

    /**
     * Construtor da classe FreightManagerFreightController.
     * Inicializa os repositórios necessários.
     */
    public FreightManagerFreightController() {
        initRepos();
    }

    /**
     * Inicializa o repositório de fretes em memória.
     */
    private void initRepos() {
        this.freightStoreInMemory = new FreightStoreInMemory();
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
     * @param operator operador a definir
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
     * @param date data a definir
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
     * @param time hora a definir
     */
    public void setTime(Time time) {
        this.time = time;
    }

    /**
     * Obtém a lista de locomotivas associadas.
     *
     * @return lista de locomotivas
     */
    public List<Locomotive> getLocomotiveList() {
        return locomotiveList;
    }

    /**
     * Define a lista de locomotivas associadas.
     *
     * @param locomotiveList lista de locomotivas
     */
    public void setLocomotiveList(List<Locomotive> locomotiveList) {
        this.locomotiveList = locomotiveList;
    }


    /**
     * Obtém os fretes disponíveis cuja data é anterior à data definida.
     *
     * @return lista observável de fretes
     */
    public ObservableList<Freight> getFreights() {
        return FXCollections.observableArrayList(findAllBefore(date, freightStoreInMemory.findAll()));
    }

    /**
     * Filtra uma lista de fretes, retornando apenas os que têm data
     * anterior à data fornecida.
     *
     * @param date data de referência
     * @param freights lista de fretes a filtrar
     * @return coleção imutável de fretes
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
     * Adiciona um frete à lista de fretes selecionados.
     *
     * @param selectedItem frete a adicionar
     */
    public void addFreight(Freight selectedItem) {
        freightOrderList.add(selectedItem);
    }

    /**
     * Obtém a lista de fretes selecionados.
     *
     * @return lista de fretes
     */
    public List<Freight> getFreightOrderList() {
        return freightOrderList;
    }

    /**
     * Define a lista de fretes selecionados.
     *
     * @param freightOrderList lista de fretes
     */
    public void setFreightOrderList(List<Freight> freightOrderList) {
        this.freightOrderList = freightOrderList;
    }

    /**
     * Cria um comboio com base nos dados definidos.
     */
    public void createTrain(){

    }
}
