package pt.ipp.isep.dei.controller.freightManager;

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
 * Controller responsável pela gestão do agendamento de comboios,
 * permitindo consultar horários, eventos e estado de despacho.
 */
public class FreightManagerSchedulerController {

    /**
     * Repositório em memória do horário geral.
     */
    GeneralScheduleStoreInMemory generalScheduleStoreInMemory;

    /**
     * Construtor do FreightManagerSchedulerController.
     * Inicializa o repositório do horário geral.
     */
    public FreightManagerSchedulerController() {
        generalScheduleStoreInMemory = new GeneralScheduleStoreInMemory();
    }

    /**
     * Obtém todos os horários de comboios do horário geral mais recente.
     *
     * @return lista de horários de comboios
     */
    public List<TrainSchedule> getTrainSchedules() {
        if (generalScheduleStoreInMemory.findLatest() == null) {
            return new ArrayList<>();
        }

        return generalScheduleStoreInMemory.findLatest().getAllTrainSchedules();
    }

    /**
     * Calcula o tempo perdido (waiting) com base nos eventos de um horário.
     *
     * @param scheduleEvents lista de eventos de horário
     * @return string formatada com tempo perdido e percentagem do tempo total
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
     * Obtém todos os eventos de horário do horário geral mais recente.
     *
     * @return lista de eventos de horário
     */
    public List<ScheduleEvent> getAllScheduleEvents() {
        GeneralSchedule generalSchedule = generalScheduleStoreInMemory.findLatest();

        if (generalSchedule == null) {
            return new ArrayList<>();
        }

        return generalSchedule.getAllScheduleEvents();
    }

    /**
     * Obtém todos os comboios presentes no horário geral.
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
     * Obtém os eventos de horário associados a um comboio específico.
     *
     * @param newTrain comboio a consultar
     * @return lista de eventos de horário do comboio
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
     * Marca um comboio como despachado no horário.
     *
     * @param selectedItem horário do comboio a despachar
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
     * @param selectedItem horário do comboio
     * @return true se já foi despachado, false caso contrário
     */
    public boolean verifyIfAlreadyDispatched(TrainSchedule selectedItem) {
        return selectedItem.getTrain().isDispatched();
    }
}
