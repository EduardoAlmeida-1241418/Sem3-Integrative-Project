package pt.ipp.isep.dei.controller.global;

import pt.ipp.isep.dei.data.config.ActualDatabaseConnection;
import pt.ipp.isep.dei.data.config.ConnectionFactory;
import pt.ipp.isep.dei.data.config.DatabaseConnection;
import pt.ipp.isep.dei.data.config.DatabasePrinter;
import pt.ipp.isep.dei.data.memory.*;
import pt.ipp.isep.dei.data.store.*;
import pt.ipp.isep.dei.domain.*;
import pt.ipp.isep.dei.domain.schedule.GeneralSchedule;
import pt.ipp.isep.dei.domain.schedule.ScheduleEvent;
import pt.ipp.isep.dei.domain.trackRelated.RailwayLine;
import pt.ipp.isep.dei.domain.trackRelated.RailwayLineSegment;
import pt.ipp.isep.dei.domain.trackRelated.Siding;
import pt.ipp.isep.dei.domain.trackRelated.TrackGauge;
import pt.ipp.isep.dei.domain.trainRelated.Train;
import pt.ipp.isep.dei.domain.transportationRelated.Freight;
import pt.ipp.isep.dei.domain.transportationRelated.Path;
import pt.ipp.isep.dei.domain.transportationRelated.Route;
import pt.ipp.isep.dei.domain.wagonRelated.Wagon;
import pt.ipp.isep.dei.domain.wagonRelated.WagonModel;
import pt.ipp.isep.dei.domain.wagonRelated.WagonType;

import java.io.IOException;

/**
 * Controller responsável por persistir os dados existentes em memória
 * para a base de dados.
 *
 * Esta classe apaga previamente os dados persistidos e volta a gravar
 * rotas, caminhos, comboios e horários.
 */
public class SaveDataBaseController implements Runnable {

    /**
     * Ligação ativa à base de dados.
     */
    private DatabaseConnection db;

    /**
     * Store responsável pela persistência de comboios.
     */
    private TrainStore trainStore;

    /**
     * Store responsável pela persistência de caminhos.
     */
    private PathStore pathStore;

    /**
     * Store responsável pela persistência de rotas.
     */
    private RouteStore routeStore;

    /**
     * Store responsável pela persistência de horários gerais.
     */
    private GeneralScheduleStore generalScheduleStore;

    /**
     * Store responsável pela persistência de eventos de horário.
     */
    private ScheduleEventStore scheduleEventStore;

    /**
     * Construtor do controller.
     *
     * Inicializa a ligação à base de dados e todas as stores necessárias
     * para a persistência dos dados.
     */
    public SaveDataBaseController() {
        this.db = ActualDatabaseConnection.getDb();
        trainStore = new TrainStore();
        pathStore = new PathStore();
        routeStore = new RouteStore();
        generalScheduleStore = new GeneralScheduleStore();
        scheduleEventStore = new ScheduleEventStore();
    }

    /**
     * Executa o processo de persistência dos dados.
     *
     * Remove previamente os dados existentes na base de dados
     * e grava novamente os dados presentes em memória.
     */
    @Override
    public void run() {
        scheduleEventStore.deleteAll(db);
        generalScheduleStore.deleteAll(db);
        trainStore.deleteAll(db);
        routeStore.deleteAll(db);
        pathStore.deleteAll(db);
        saveRoutes();
        savePaths();
        saveTrains();
        saveSchedules();
    }

    /**
     * Guarda todas as rotas existentes em memória na base de dados.
     */
    private void saveRoutes() {
        for (Route r : new RouteStoreInMemory().findAll()) {
            routeStore.save(db, r);
        }
    }

    /**
     * Guarda todos os caminhos existentes em memória na base de dados.
     */
    private void savePaths() {
        for (Path p : new PathStoreInMemory().findAll()) {
            pathStore.save(db, p);
        }
    }

    /**
     * Guarda todos os comboios existentes em memória na base de dados.
     */
    private void saveTrains() {
        for (Train t : new TrainStoreInMemory().findAll()) {
            trainStore.save(db, t);
        }
    }

    /**
     * Guarda todos os horários gerais e respetivos eventos
     * existentes em memória na base de dados.
     */
    private void saveSchedules() {
        for (GeneralSchedule generalSchedule : new GeneralScheduleStoreInMemory().findAll()) {
            generalScheduleStore.save(db, generalSchedule);
            for (ScheduleEvent scheduleEvent : generalSchedule.getAllEvents()) {
                scheduleEventStore.save(db, scheduleEvent);
            }
        }
    }
}
