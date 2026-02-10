package pt.ipp.isep.dei.domain.schedule;

import pt.ipp.isep.dei.domain.DateTime;
import pt.ipp.isep.dei.domain.trackRelated.TrackLocation;
import pt.ipp.isep.dei.domain.trainRelated.Train;

/**
 * Representa uma ação de agendamento associada a um comboio.
 * <p>
 * Uma {@code ScheduleAction} define um movimento ou evento de um comboio
 * entre duas localizações da linha férrea, ocorrendo num determinado
 * instante temporal.
 * </p>
 * <p>
 * Esta classe implementa {@link Comparable} para permitir a ordenação
 * cronológica das ações, com um critério de desempate determinístico
 * baseado no identificador do comboio.
 * </p>
 */
public class ScheduleAction implements Comparable<ScheduleAction> {

    /**
     * Comboio associado à ação de agendamento.
     */
    private Train train;

    /**
     * Localização inicial do comboio.
     */
    private TrackLocation startPosition;

    /**
     * Localização final do comboio.
     */
    private TrackLocation endPosition;

    /**
     * Data e hora em que a ação ocorre.
     */
    private DateTime dateTime;

    /**
     * Cria uma nova ação de agendamento.
     *
     * @param train comboio associado à ação
     * @param startPosition posição inicial do comboio
     * @param endPosition posição final do comboio
     * @param dateTime data e hora da ação
     */
    public ScheduleAction(Train train, TrackLocation startPosition,
                          TrackLocation endPosition, DateTime dateTime) {
        this.train = train;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.dateTime = dateTime;
    }

    /**
     * Compara esta ação com outra para efeitos de ordenação.
     * <p>
     * A comparação é feita primeiramente pela data e hora.
     * Em caso de empate, é utilizado o identificador do comboio
     * como critério de desempate determinístico.
     * </p>
     *
     * @param other outra ação de agendamento a comparar
     * @return valor negativo, zero ou positivo conforme esta ação seja
     * anterior, igual ou posterior à outra
     */
    @Override
    public int compareTo(ScheduleAction other) {
        int cmp = this.dateTime.compareTo(other.dateTime);
        if (cmp != 0) {
            return cmp;
        }

        // desempate determinístico pelo ID do comboio
        return Integer.compare(
                this.train.getId(),
                other.train.getId()
        );
    }

    /**
     * Obtém o comboio associado à ação.
     *
     * @return comboio da ação
     */
    public Train getTrain() {
        return train;
    }

    /**
     * Define o comboio associado à ação.
     *
     * @param train novo comboio
     */
    public void setTrain(Train train) {
        this.train = train;
    }

    /**
     * Obtém a posição inicial do comboio.
     *
     * @return localização inicial
     */
    public TrackLocation getStartPosition() {
        return startPosition;
    }

    /**
     * Define a posição inicial do comboio.
     *
     * @param startPosition nova posição inicial
     */
    public void setStartPosition(TrackLocation startPosition) {
        this.startPosition = startPosition;
    }

    /**
     * Obtém a posição final do comboio.
     *
     * @return localização final
     */
    public TrackLocation getEndPosition() {
        return endPosition;
    }

    /**
     * Define a posição final do comboio.
     *
     * @param endPosition nova posição final
     */
    public void setEndPosition(TrackLocation endPosition) {
        this.endPosition = endPosition;
    }

    /**
     * Obtém a data e hora da ação.
     *
     * @return data e hora
     */
    public DateTime getDateTime() {
        return dateTime;
    }

    /**
     * Define a data e hora da ação.
     *
     * @param dateTime nova data e hora
     */
    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }
}
