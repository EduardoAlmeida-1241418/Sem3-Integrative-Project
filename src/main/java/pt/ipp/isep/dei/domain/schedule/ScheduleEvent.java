package pt.ipp.isep.dei.domain.schedule;

import pt.ipp.isep.dei.data.memory.GeneralScheduleStoreInMemory;
import pt.ipp.isep.dei.domain.DateTime;
import pt.ipp.isep.dei.domain.TimeInterval;
import pt.ipp.isep.dei.domain.trackRelated.TrackLocation;
import pt.ipp.isep.dei.domain.trainRelated.Train;

/**
 * Representa um evento de agendamento no sistema.
 * <p>
 * Um {@code ScheduleEvent} descreve um acontecimento associado a um comboio,
 * incluindo o intervalo temporal em que ocorre, as posições inicial e final,
 * o tipo de evento e a ligação opcional a um {@code GeneralSchedule}.
 * </p>
 * <p>
 * Cada evento possui um identificador único, gerido através de um
 * armazenamento em memória para garantir unicidade.
 * </p>
 */
public class ScheduleEvent {

    /**
     * Identificador único do evento de agendamento.
     */
    private int id;

    /**
     * Agenda geral à qual o evento pertence.
     */
    private GeneralSchedule generalSchedule;

    /**
     * Intervalo temporal do evento.
     */
    private TimeInterval timeInterval;

    /**
     * Localização inicial do evento.
     */
    private TrackLocation startPosition;

    /**
     * Localização final do evento.
     */
    private TrackLocation endPosition;

    /**
     * Comboio associado ao evento.
     */
    private Train train;

    /**
     * Tipo do evento de agendamento.
     */
    private ScheduleEventType scheduleEventType;

    /**
     * Construtor de cópia.
     * <p>
     * Cria um novo {@code ScheduleEvent} a partir de outro evento,
     * atribuindo um novo identificador único.
     * </p>
     *
     * @param scheduleEvent evento de origem
     */
    public ScheduleEvent(ScheduleEvent scheduleEvent) {
        this.id = new GeneralScheduleStoreInMemory().getNextScheduleEventId();
        this.timeInterval = scheduleEvent.getTimeInterval();
        this.startPosition = scheduleEvent.getStartPosition();
        this.endPosition = scheduleEvent.getEndPosition();
        this.train = scheduleEvent.getTrain();
        this.scheduleEventType = scheduleEvent.getScheduleEventType();
        new GeneralScheduleStoreInMemory().addScheduleEventId(id);
    }

    /**
     * Cria um novo evento de agendamento com base nos dados fornecidos.
     *
     * @param timeInterval intervalo temporal do evento
     * @param startPosition posição inicial
     * @param endPosition posição final
     * @param train comboio associado
     * @param scheduleEventType tipo do evento
     */
    public ScheduleEvent(TimeInterval timeInterval, TrackLocation startPosition, TrackLocation endPosition, Train train, ScheduleEventType scheduleEventType) {
        this.id = new GeneralScheduleStoreInMemory().getNextScheduleEventId();
        this.timeInterval = timeInterval;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.train = train;
        this.scheduleEventType = scheduleEventType;
        new GeneralScheduleStoreInMemory().addScheduleEventId(id);
    }

    /**
     * Cria um evento de agendamento a partir de uma ação de agendamento.
     * <p>
     * O intervalo temporal é construído a partir da data/hora inicial da ação
     * e da data/hora final fornecida.
     * </p>
     *
     * @param scheduleAction ação de agendamento de origem
     * @param endDateTime data e hora de fim do evento
     * @param scheduleEventType tipo do evento
     */
    public ScheduleEvent(ScheduleAction scheduleAction, DateTime endDateTime, ScheduleEventType scheduleEventType) {
        this.id = new GeneralScheduleStoreInMemory().getNextScheduleEventId();
        this.timeInterval = new TimeInterval(scheduleAction.getDateTime(), endDateTime);
        this.startPosition = scheduleAction.getStartPosition();
        this.endPosition = scheduleAction.getEndPosition();
        this.train = scheduleAction.getTrain();
        this.scheduleEventType = scheduleEventType;
        new GeneralScheduleStoreInMemory().addScheduleEventId(id);
    }

    /**
     * Cria um evento de agendamento com um identificador explícito.
     * <p>
     * Caso já exista um evento com o mesmo identificador, é lançada
     * uma exceção.
     * </p>
     *
     * @param id identificador do evento
     * @param generalSchedule agenda geral associada
     * @param timeInterval intervalo temporal
     * @param startPosition posição inicial
     * @param endPosition posição final
     * @param train comboio associado
     * @param scheduleEventType tipo do evento
     */
    public ScheduleEvent(int id, GeneralSchedule generalSchedule, TimeInterval timeInterval, TrackLocation startPosition, TrackLocation endPosition, Train train, ScheduleEventType scheduleEventType) {
        this.id = id;
        if (new GeneralScheduleStoreInMemory().existsScheduleEventWithId(id)) {
            throw new IllegalArgumentException("ScheduleEvent with id " + id + " already exists.");
        }

        this.generalSchedule = generalSchedule;
        this.timeInterval = timeInterval;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.train = train;
        this.scheduleEventType = scheduleEventType;
        new GeneralScheduleStoreInMemory().addScheduleEventId(id);
    }

    /**
     * Obtém a agenda geral associada ao evento.
     *
     * @return agenda geral
     */
    public GeneralSchedule getGeneralSchedule() {
        return generalSchedule;
    }

    /**
     * Define a agenda geral associada ao evento.
     *
     * @param generalSchedule nova agenda geral
     */
    public void setGeneralSchedule(GeneralSchedule generalSchedule) {
        this.generalSchedule = generalSchedule;
    }

    /**
     * Obtém o identificador do evento.
     *
     * @return identificador do evento
     */
    public int getId() {
        return id;
    }

    /**
     * Define o identificador do evento.
     *
     * @param id novo identificador
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtém o intervalo temporal do evento.
     *
     * @return intervalo temporal
     */
    public TimeInterval getTimeInterval() {
        return timeInterval;
    }

    /**
     * Define o intervalo temporal do evento.
     *
     * @param timeInterval novo intervalo temporal
     */
    public void setTimeInterval(TimeInterval timeInterval) {
        this.timeInterval = timeInterval;
    }

    /**
     * Obtém o tipo do evento.
     *
     * @return tipo do evento
     */
    public ScheduleEventType getScheduleEventType() {
        return scheduleEventType;
    }

    /**
     * Define o tipo do evento.
     *
     * @param scheduleEventType novo tipo de evento
     */
    public void setScheduleEventType(ScheduleEventType scheduleEventType) {
        this.scheduleEventType = scheduleEventType;
    }

    /**
     * Obtém o comboio associado ao evento.
     *
     * @return comboio
     */
    public Train getTrain() {
        return train;
    }

    /**
     * Define o comboio associado ao evento.
     *
     * @param train novo comboio
     */
    public void setTrain(Train train) {
        this.train = train;
    }

    /**
     * Obtém a posição inicial do evento.
     *
     * @return posição inicial
     */
    public TrackLocation getStartPosition() {
        return startPosition;
    }

    /**
     * Define a posição inicial do evento.
     *
     * @param startPosition nova posição inicial
     */
    public void setStartPosition(TrackLocation startPosition) {
        this.startPosition = startPosition;
    }

    /**
     * Obtém a posição final do evento.
     *
     * @return posição final
     */
    public TrackLocation getEndPosition() {
        return endPosition;
    }

    /**
     * Define a posição final do evento.
     *
     * @param endPosition nova posição final
     */
    public void setEndPosition(TrackLocation endPosition) {
        this.endPosition = endPosition;
    }

    /**
     * Obtém a data e hora inicial do evento.
     *
     * @return data e hora inicial
     */
    public DateTime getStartDateTime() {
        return timeInterval.getInitialDateTime();
    }

    /**
     * Obtém a data e hora final do evento.
     *
     * @return data e hora final
     */
    public DateTime getEndDateTime() {
        return timeInterval.getFinalDateTime();
    }

    /**
     * Representação textual do evento de agendamento.
     *
     * @return representação em {@code String} do evento
     */
    @Override
    public String toString() {
        return "ScheduleEvent{" +
                "trainId=" + (train != null ? train.getId() : "null") +
                ", type=" + scheduleEventType +
                ", start=" + (startPosition != null ? startPosition.name() : "null") +
                ", end=" + (endPosition != null ? endPosition.name() : "null") + "\n"+
                "interval=" + (timeInterval != null ? timeInterval : "null") +
                '}';
    }

}
