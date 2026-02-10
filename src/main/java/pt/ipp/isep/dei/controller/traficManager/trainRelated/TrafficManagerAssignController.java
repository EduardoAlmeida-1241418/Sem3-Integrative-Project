package pt.ipp.isep.dei.controller.traficManager.trainRelated;

import javafx.scene.control.Spinner;
import pt.ipp.isep.dei.data.config.ActualDatabaseConnection;
import pt.ipp.isep.dei.data.config.DatabaseConnection;
import pt.ipp.isep.dei.data.memory.*;
import pt.ipp.isep.dei.domain.*;
import pt.ipp.isep.dei.domain.schedule.*;
import pt.ipp.isep.dei.domain.trainRelated.Train;
import pt.ipp.isep.dei.domain.transportationRelated.Freight;
import pt.ipp.isep.dei.domain.transportationRelated.Route;
import pt.ipp.isep.dei.domain.wagonRelated.Wagon;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsável pela atribuição de comboios a rotas
 * no gestor de tráfego.
 *
 * Permite selecionar operadores, rotas, locomotivas,
 * gerar horários e associar comboios ao planeamento.
 */
public class TrafficManagerAssignController {

    /**
     * Store de operadores em memória.
     */
    private OperatorStoreInMemory operatorStoreInMemory;

    /**
     * Store de rotas em memória.
     */
    private RouteStoreInMemory routeStoreInMemory;

    /**
     * Store de comboios em memória.
     */
    private TrainStoreInMemory trainStoreInMemory;

    /**
     * Store de locomotivas em memória.
     */
    private LocomotiveStoreInMemory locomotiveStoreInMemory;

    /**
     * Store de horários gerais em memória.
     */
    private GeneralScheduleStoreInMemory generalScheduleStoreInMemory;

    /**
     * Ligação ativa à base de dados.
     */
    private DatabaseConnection db = ActualDatabaseConnection.getDb();

    /**
     * Comboio gerado a ser atribuído.
     */
    private Train generatedTrain;

    /**
     * Horário geral gerado.
     */
    private GeneralSchedule generalSchedule;

    /**
     * Construtor do controller.
     *
     * Inicializa todas as stores necessárias.
     */
    public TrafficManagerAssignController() {
        this.operatorStoreInMemory = new OperatorStoreInMemory();
        this.routeStoreInMemory = new RouteStoreInMemory();
        this.trainStoreInMemory = new TrainStoreInMemory();
        this.locomotiveStoreInMemory = new LocomotiveStoreInMemory();
        this.generalScheduleStoreInMemory = new GeneralScheduleStoreInMemory();
    }

    /**
     * Obtém a lista de operadores disponíveis.
     *
     * @return lista de operadores
     */
    public List<Operator> getOperators() {
        return new ArrayList<>(operatorStoreInMemory.findAll());
    }

    /**
     * Obtém as rotas prontas a serem atribuídas a comboios,
     * considerando data, caminhos e utilização atual.
     *
     * @param dateDatePickerValue data selecionada
     * @return lista de rotas disponíveis
     */
    public List<Route> getReadyRoutes(LocalDate dateDatePickerValue) {
        Date date = new Date(
                dateDatePickerValue.getDayOfMonth(),
                dateDatePickerValue.getMonthValue(),
                dateDatePickerValue.getYear()
        );

        List<Route> routes = new ArrayList<>(routeStoreInMemory.findAll());

        // Mais antigo
        routes.removeIf(route ->
                route.getFreights().stream()
                        .anyMatch(freight -> date.isBefore(freight.getDate()))
        );

        // Sem Caminhos
        routes.removeIf(route -> route.getPath() == null);

        // Já a ser usado
        routes.removeIf(route ->
                trainStoreInMemory.findAll().stream()
                        .anyMatch(train -> route.equals(train.getRoute()))
        );

        for (Train train : trainStoreInMemory.findAll()) {
            if (train.getRoute() != null) {
                routes.remove(train.getRoute());
            }
        }

        return routes;
    }

    /**
     * Obtém a lista de locomotivas disponíveis.
     *
     * @return lista de locomotivas
     */
    public List<Locomotive> locomotiveList() {
        return new ArrayList<>(locomotiveStoreInMemory.findAll());
    }

    /**
     * Obtém a posição atual de uma locomotiva numa determinada data e hora.
     *
     * @param chosenLocomotive locomotiva selecionada
     * @param dateDatePickerValue data
     * @param hour hora
     * @param min minutos
     * @return instalação onde a locomotiva se encontra
     */
    public Facility getLocomotiveActualPosition(Locomotive chosenLocomotive, LocalDate dateDatePickerValue, int hour, int min) {
        DateTime dateTime = convertDateTimeToLocalDate(dateDatePickerValue, hour, min);

        if (generalScheduleStoreInMemory.findLatest() != null) {
            for (ScheduleEvent scheduleEvent : generalScheduleStoreInMemory.findLatest().getEventsUpToMostRecentFirst(dateTime)) {
                for (Locomotive locomotive : scheduleEvent.getTrain().getLocomotives()) {
                    if (locomotive.equals(chosenLocomotive)) {
                        return scheduleEvent.getTrain().getRoute().getPath().getFacilities().getLast();
                    }
                }
            }
        }

        if (chosenLocomotive.getStartFacility() != null) {
            return chosenLocomotive.getStartFacility();
        } else {
            return new Facility(0, "NULL");
        }
    }

    /**
     * Obtém todos os vagões associados a uma rota.
     *
     * @param newRoute rota
     * @return lista de vagões
     */
    public List<Wagon> getRouteWagons(Route newRoute) {
        List<Wagon> wagons = new ArrayList<>();

        for (Freight freight : newRoute.getFreights()) {
            wagons.addAll(freight.getWagons());
        }

        return wagons;
    }

    /**
     * Obtém a posição atual de um vagão numa determinada data e hora.
     *
     * @param chosenWagon vagão
     * @param dateDatePickerValue data
     * @param hour hora
     * @param min minutos
     * @return instalação onde o vagão se encontra
     */
    public Facility findWagonPosition(Wagon chosenWagon, LocalDate dateDatePickerValue, int hour, int min) {
        DateTime dateTime = convertDateTimeToLocalDate(dateDatePickerValue, hour, min);

        if (generalScheduleStoreInMemory.findLatest() != null) {
            for (ScheduleEvent scheduleEvent : generalScheduleStoreInMemory.findLatest().getEventsUpToMostRecentFirst(dateTime)) {
                for (Freight freight : scheduleEvent.getTrain().getRoute().getFreights()) {
                    if (freight.getWagons().contains(chosenWagon)) {
                        return freight.getDestinationFacility();
                    }
                }
            }
        }

        if (chosenWagon.getStartFacility() != null) {
            return chosenWagon.getStartFacility();
        } else {
            return new Facility(0, "NULL");
        }
    }

    /**
     * Converte data e hora para um objeto DateTime.
     *
     * @param localDate data
     * @param hour hora
     * @param min minutos
     * @return DateTime correspondente
     */
    public DateTime convertDateTimeToLocalDate(LocalDate localDate, int hour, int min) {
        Date date = new Date(localDate.getDayOfMonth(), localDate.getMonthValue(), localDate.getYear());
        Time time = new Time(hour, min);
        return new DateTime(date, time);
    }

    /**
     * Gera um novo horário geral com base nos comboios existentes
     * e no comboio recentemente criado.
     */
    public void generateSchedule() {
        GeneralScheduleStoreInMemory generalScheduleStoreInMemory = new GeneralScheduleStoreInMemory();
        List<Train> trains = new ArrayList<>();
        GeneralSchedule gs = generalScheduleStoreInMemory.findLatest();
        if (gs != null) {
            for (TrainSchedule ts : gs.getAllTrainSchedules()) {
                trains.add(ts.getTrain());
            }
        }
        trains.add(generatedTrain);

        List<Train> trainList = new ArrayList<>();
        for (Train train : trains) {
            if (!train.isDispatched()) {
                trainList.add(train);
            }
        }

        ScheduleGenerator generator = new ScheduleGenerator(trains);

        this.generalSchedule = generator.generateNewSchedule();
    }

    /**
     * Cria um novo comboio com os dados fornecidos.
     *
     * @param stringOperator nome do operador
     * @param dateValue data
     * @param hourSpinner spinner da hora
     * @param minSpinner spinner dos minutos
     * @param route rota
     * @param locomotives locomotivas associadas
     */
    public void createTrain(String stringOperator, LocalDate
            dateValue, Spinner<Integer> hourSpinner, Spinner<Integer> minSpinner, Route
                                    route, ArrayList<Locomotive> locomotives) {
        Operator operator = findOperatorByName(stringOperator);
        DateTime dateTime = new DateTime(new Date(dateValue.getDayOfMonth(), dateValue.getMonthValue(), dateValue.getYear()), new Time(hourSpinner.getValue(), minSpinner.getValue()));

        this.generatedTrain = new Train(operator, dateTime, route, locomotives);
    }

    /**
     * Procura um operador pelo nome.
     *
     * @param stringOperator nome do operador
     * @return operador encontrado ou null
     */
    private Operator findOperatorByName(String stringOperator) {
        OperatorStoreInMemory operatorStoreInMemory = new OperatorStoreInMemory();
        for (Operator operator : operatorStoreInMemory.findAll()) {
            if (operator.getName().equals(stringOperator)) {
                return operator;
            }
        }
        return null;
    }

    /**
     * Calcula o tempo perdido num conjunto de eventos de horário.
     *
     * @param scheduleEvents lista de eventos
     * @return representação textual do tempo perdido
     */
    public String calculateLostTime(List<ScheduleEvent> scheduleEvents) {
        int seconds = 0;

        for (ScheduleEvent scheduleEvent : scheduleEvents) {
            if (scheduleEvent.getScheduleEventType().equals(ScheduleEventType.WAITING)
                    || scheduleEvent.getScheduleEventType().equals(ScheduleEventType.WAITING_IN_SIDING)
                    || scheduleEvent.getScheduleEventType().equals(ScheduleEventType.WAITING_FOR_ASSEMBLE)
                    || scheduleEvent.getScheduleEventType().equals(ScheduleEventType.WAITING_IN_STATION)) {
                seconds += scheduleEvent.getTimeInterval().getDurationInSeconds();
            }
        }

        int totalTime = 0;
        for (ScheduleEvent scheduleEvent : scheduleEvents) {
            totalTime += scheduleEvent.getTimeInterval().getDurationInSeconds();
        }

        double percentage = (double) seconds / totalTime * 100;

        return seconds / 3600 + "h "
                + (seconds % 3600) / 60 + "m "
                + (seconds % 60) + "s\n"
                + String.format("%.2f", percentage) + "% of All Time";
    }

    /**
     * Obtém os horários de comboio do horário geral.
     *
     * @return lista de horários de comboio
     */
    public List<TrainSchedule> getTrainSchedules() {
        return generalSchedule.getAllTrainSchedules();
    }

    public Train getGeneratedTrain() {
        return generatedTrain;
    }

    public void setGeneratedTrain(Train generatedTrain) {
        this.generatedTrain = generatedTrain;
    }

    public GeneralSchedule getGeneralSchedule() {
        return generalSchedule;
    }

    public void setGeneralSchedule(GeneralSchedule generalSchedule) {
        this.generalSchedule = generalSchedule;
    }

    /**
     * Atribui o comboio gerado e o horário geral à base de dados.
     */
    public void assignTrain() {
        Train train = new Train(this.generatedTrain);
        this.generatedTrain = train;

        trainStoreInMemory.save(db, train);

        generalScheduleStoreInMemory.save(db, generalSchedule);

    }
}
