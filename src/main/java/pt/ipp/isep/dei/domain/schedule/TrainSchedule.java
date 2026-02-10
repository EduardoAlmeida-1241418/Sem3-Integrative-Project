package pt.ipp.isep.dei.domain.schedule;

import pt.ipp.isep.dei.domain.trainRelated.Train;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa o horário associado a um comboio específico.
 * <p>
 * A classe {@code TrainSchedule} mantém a referência ao comboio e
 * a lista ordenada de eventos de agendamento ({@link ScheduleEvent})
 * que descrevem todas as ações realizadas pelo comboio ao longo do tempo.
 * </p>
 */
public class TrainSchedule {

    /**
     * Comboio ao qual este horário está associado.
     */
    private Train train;

    /**
     * Lista de eventos que compõem o horário do comboio.
     */
    private List<ScheduleEvent> scheduleEvents;

    /**
     * Cria um novo horário para o comboio fornecido.
     *
     * @param train comboio associado ao horário
     */
    public TrainSchedule(Train train) {
        this.train = train;
        scheduleEvents = new ArrayList<>();
    }

    /**
     * Adiciona um evento ao horário do comboio.
     *
     * @param event evento de agendamento a adicionar
     */
    public void addEvent(ScheduleEvent event) {
        scheduleEvents.add(event);
    }

    /**
     * Remove um evento do horário do comboio.
     *
     * @param event evento de agendamento a remover
     */
    public void removeEvent(ScheduleEvent event) {
        scheduleEvents.remove(event);
    }

    /**
     * Devolve o comboio associado a este horário.
     *
     * @return comboio
     */
    public Train getTrain() {
        return train;
    }

    /**
     * Define o comboio associado a este horário.
     *
     * @param train novo comboio
     */
    public void setTrain(Train train) {
        this.train = train;
    }

    /**
     * Devolve a lista de eventos do horário.
     *
     * @return lista de {@code ScheduleEvent}
     */
    public List<ScheduleEvent> getScheduleEvents() {
        return scheduleEvents;
    }

    /**
     * Define a lista de eventos do horário.
     *
     * @param scheduleEvents nova lista de eventos
     */
    public void setScheduleEvents(List<ScheduleEvent> scheduleEvents) {
        this.scheduleEvents = scheduleEvents;
    }

    @Override
    public String toString() {
        return "TrainSchedule{" + "train=" + train + '}';
    }
}
