package pt.ipp.isep.dei.controller.traficManager;

import pt.ipp.isep.dei.data.memory.GeneralScheduleStoreInMemory;
import pt.ipp.isep.dei.data.memory.TrainStoreInMemory;
import pt.ipp.isep.dei.domain.schedule.GeneralSchedule;
import pt.ipp.isep.dei.domain.schedule.ScheduleEvent;
import pt.ipp.isep.dei.domain.schedule.ScheduleEventType;
import pt.ipp.isep.dei.domain.schedule.TrainSchedule;
import pt.ipp.isep.dei.domain.trainRelated.Train;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsável pela navegação e análise de horários
 * no gestor de tráfego.
 *
 * Permite consultar eventos, comboios, alternar entre horários
 * mais antigos ou mais recentes e calcular tempos de espera.
 */
public class TrafficManagerSchedulerController {

    /**
     * Store de horários gerais em memória.
     */
    GeneralScheduleStoreInMemory generalScheduleStoreInMemory;

    /**
     * Identificador do horário atualmente selecionado.
     */
    private int actualScheduler;

    /**
     * Construtor do controller.
     *
     * Inicializa a store de horários e define o horário atual
     * como o mais recente disponível.
     */
    public TrafficManagerSchedulerController() {
        generalScheduleStoreInMemory = new GeneralScheduleStoreInMemory();
        actualScheduler = generalScheduleStoreInMemory.findLatest() != null ?
                generalScheduleStoreInMemory.findLatest().getId() : 0;
    }

    /**
     * Obtém o identificador do horário atual.
     *
     * @return id do horário atual
     */
    public int getActualScheduler() {
        return actualScheduler;
    }

    /**
     * Define o identificador do horário atual.
     *
     * @param actualScheduler id do horário
     */
    public void setActualScheduler(int actualScheduler) {
        this.actualScheduler = actualScheduler;
    }

    /**
     * Obtém todos os eventos do horário atual.
     *
     * @return lista de eventos de horário
     */
    public List<ScheduleEvent> getAllScheduleEvents() {
        GeneralSchedule generalSchedule = generalScheduleStoreInMemory.findById(
                null, String.valueOf(actualScheduler));

        if (generalSchedule == null) {
            return new ArrayList<>();
        }

        return generalSchedule.getAllScheduleEvents();
    }

    /**
     * Obtém todos os horários de comboio do horário atual.
     *
     * @return lista de horários de comboio
     */
    public List<TrainSchedule> getTrainSchedules() {
        GeneralSchedule generalSchedule = generalScheduleStoreInMemory.findById(
                null, String.valueOf(actualScheduler));
        if (generalSchedule == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(generalSchedule.getAllTrainSchedules());
    }

    /**
     * Obtém todos os comboios presentes no horário atual.
     *
     * @return lista de comboios
     */
    public ArrayList<Train> getAllTrainsInSchedule() {
        List<Train> trains = new ArrayList<>();
        for (TrainSchedule trainSchedule : getTrainSchedules()) {
            trains.add(trainSchedule.getTrain());
        }
        return new ArrayList<>(trains);
    }

    /**
     * Verifica se existe um horário mais antigo que o atual.
     *
     * @return true se existir, false caso contrário
     */
    public boolean existOlderSchedule() {
        for (GeneralSchedule gs : generalScheduleStoreInMemory.findAll()) {
            if (gs.getId() < actualScheduler) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica se existe um horário mais recente que o atual.
     *
     * @return true se existir, false caso contrário
     */
    public boolean existYoungerSchedule() {
        for (GeneralSchedule gs : generalScheduleStoreInMemory.findAll()) {
            if (gs.getId() > actualScheduler) {
                return true;
            }
        }
        return false;
    }

    /**
     * Define o horário atual como o mais antigo imediatamente anterior.
     */
    public void setOlderTrainScheduleId() {
        int result = -1;
        for (GeneralSchedule gs : generalScheduleStoreInMemory.findAll()) {
            int id = gs.getId();
            if (id < actualScheduler && id > result) {
                result = id;
            }
        }

        if (result != -1) {
            this.actualScheduler = result;
        }
    }

    /**
     * Define o horário atual como o mais recente imediatamente seguinte.
     */
    public void setYoungerTrainScheduleId() {
        int result = -1;
        for (GeneralSchedule gs : generalScheduleStoreInMemory.findAll()) {
            int id = gs.getId();

            if (id > actualScheduler && (result == -1 || id < result)) {
                result = id;
            }
        }

        if (result != -1) {
            this.actualScheduler = result;
        }
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
                    ||scheduleEvent.getScheduleEventType().equals(ScheduleEventType.WAITING_IN_SIDING)
                    || scheduleEvent.getScheduleEventType().equals(ScheduleEventType.WAITING_FOR_ASSEMBLE)
                    || scheduleEvent.getScheduleEventType().equals(ScheduleEventType.WAITING_IN_STATION)){
                seconds += scheduleEvent.getTimeInterval().getDurationInSeconds();
            }
        }

        int totalTime = 0;
        for (ScheduleEvent scheduleEvent : scheduleEvents) {
            totalTime += scheduleEvent.getTimeInterval().getDurationInSeconds();
        }

        double percentage = (double) seconds / totalTime * 100;

        return  seconds / 3600 + "h "
                + (seconds % 3600) / 60 + "m "
                + (seconds % 60) + "s\n"
                + String.format("%.2f", percentage) + "% of All Time";
    }

    /**
     * Obtém os eventos de horário associados a um único comboio.
     *
     * @param newTrain comboio
     * @return lista de eventos do comboio
     */
    public List<ScheduleEvent> getSingleTrainSchedules(Train newTrain) {
        List<ScheduleEvent> scheduleEvents = new ArrayList<>();
        GeneralSchedule generalSchedule = generalScheduleStoreInMemory.findLatest();

        for (ScheduleEvent scheduleEvent : generalSchedule.getScheduleForTrain(newTrain)) {
            scheduleEvents.add(scheduleEvent);
        }
        return scheduleEvents;
    }

    /**
     * Marca um comboio como despachado.
     *
     * @param selectedItem horário de comboio selecionado
     */
    public void dispatchTrain(TrainSchedule selectedItem) {

        TrainStoreInMemory trainStoreInMemory = new TrainStoreInMemory();

        // Redundante, mas não mexer -> Se tocar estraga
        for (Train train : trainStoreInMemory.findAll()) {
            if (train.getId() == selectedItem.getTrain().getId()){
                train.setDispatched(true);
            }
        }

        selectedItem.getTrain().setDispatched(true);
    }

    /**
     * Verifica se um comboio já foi despachado.
     *
     * @param selectedItem horário de comboio
     * @return true se já foi despachado
     */
    public boolean verifyIfAlreadyDispatched(TrainSchedule selectedItem) {
        return selectedItem.getTrain().isDispatched();
    }
}
