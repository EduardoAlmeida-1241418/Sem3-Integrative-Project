package pt.ipp.isep.dei.domain.trackRelated;

import pt.ipp.isep.dei.domain.Facility;

import java.util.*;

/**
 * Representa uma linha ferroviária que liga duas instalações (facilities).
 * <p>
 * A classe {@code RailwayLine} define uma ligação entre duas facilities,
 * contendo um conjunto ordenado de segmentos ({@link RailwayLineSegment})
 * que descrevem fisicamente o trajeto da linha.
 * </p>
 */
public class RailwayLine {

    /**
     * Identificador único da linha ferroviária.
     */
    private int id;

    /**
     * Nome da linha ferroviária.
     */
    private String name;

    /**
     * Identificador da facility de origem da linha.
     */
    private int startFacilityId;

    /**
     * Identificador da facility de destino da linha.
     */
    private int endFacilityId;

    /**
     * Número de identificação fiscal do proprietário da linha.
     */
    private String ownerVat;

    /**
     * Mapa ordenado dos segmentos da linha, indexados pela sua ordem.
     */
    private Map<Integer, RailwayLineSegment> segments;

    /**
     * Cria uma nova linha ferroviária.
     *
     * @param id identificador da linha
     * @param name nome da linha
     * @param startFacilityId identificador da facility de origem
     * @param endFacilityId identificador da facility de destino
     * @param ownerVat NIF do proprietário da linha
     */
    public RailwayLine(int id, String name, int startFacilityId, int endFacilityId, String ownerVat) {
        this.id = id;
        this.name = name;
        this.startFacilityId = startFacilityId;
        this.endFacilityId = endFacilityId;
        this.ownerVat = ownerVat;
        this.segments = new TreeMap<>();
    }

    /**
     * Adiciona um segmento à linha ferroviária.
     * <p>
     * Se a ordem for {@code -1}, o segmento é adicionado no final.
     * </p>
     *
     * @param segment segmento a adicionar
     * @param order ordem do segmento
     */
    public void addSegment(RailwayLineSegment segment, int order) {
        if (order == -1) {
            order = segments.size() + 1;
        }
        segments.put(order, segment);
    }

    /**
     * Devolve o identificador da linha ferroviária.
     *
     * @return identificador da linha
     */
    public int getId() {
        return id;
    }

    /**
     * Define o identificador da linha ferroviária.
     *
     * @param id novo identificador
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Devolve o nome da linha ferroviária.
     *
     * @return nome da linha
     */
    public String getName() {
        return name;
    }

    /**
     * Define o nome da linha ferroviária.
     *
     * @param name novo nome
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Devolve o identificador da facility de origem.
     *
     * @return identificador da facility de origem
     */
    public int getStartFacilityId() {
        return startFacilityId;
    }

    /**
     * Define o identificador da facility de origem.
     *
     * @param startFacilityId novo identificador
     */
    public void setStartFacilityId(int startFacilityId) {
        this.startFacilityId = startFacilityId;
    }

    /**
     * Devolve o identificador da facility de destino.
     *
     * @return identificador da facility de destino
     */
    public int getEndFacilityId() {
        return endFacilityId;
    }

    /**
     * Define o identificador da facility de destino.
     *
     * @param endFacilityId novo identificador
     */
    public void setEndFacilityId(int endFacilityId) {
        this.endFacilityId = endFacilityId;
    }

    /**
     * Devolve o NIF do proprietário da linha.
     *
     * @return NIF do proprietário
     */
    public String getOwnerVat() {
        return ownerVat;
    }

    /**
     * Define o NIF do proprietário da linha.
     *
     * @param ownerVat novo NIF
     */
    public void setOwnerVat(String ownerVat) {
        this.ownerVat = ownerVat;
    }

    /**
     * Devolve a lista de segmentos da linha na ordem correta.
     *
     * @return lista de {@code RailwayLineSegment}
     */
    public List<RailwayLineSegment> getSegments() {
        return new ArrayList<>(segments.values());
    }

    /**
     * Define o mapa de segmentos da linha.
     *
     * @param segments mapa de segmentos
     */
    public void setSegments(Map<Integer, RailwayLineSegment> segments) {
        this.segments = segments;
    }

    /**
     * Obtém a ordem de um segmento específico na linha.
     *
     * @param segment segmento a procurar
     * @return ordem do segmento ou {@code -1} se não existir
     */
    public int getSegmentOrder(RailwayLineSegment segment) {
        for (Map.Entry<Integer, RailwayLineSegment> entry : segments.entrySet()) {
            if (entry.getValue().equals(segment)) {
                return entry.getKey();
            }
        }
        return -1;
    }

    /**
     * Devolve os segmentos da linha na ordem correta.
     *
     * @return lista ordenada de segmentos
     */
    public List<RailwayLineSegment> getCorrectSegmentsOrder() {
        return new ArrayList<>(segments.values());
    }

    /**
     * Devolve os segmentos da linha na ordem inversa.
     *
     * @return lista de segmentos em ordem inversa
     */
    public List<RailwayLineSegment> getReverseSegmentOrder() {
        List<RailwayLineSegment> reversed = new ArrayList<>(segments.values());
        Collections.reverse(reversed);
        return reversed;
    }

    /**
     * Devolve os segmentos da linha na ordem correta entre duas facilities.
     *
     * @param start facility de origem
     * @param end facility de destino
     * @return lista de segmentos na ordem adequada ou {@code null} se não corresponder
     */
    public List<RailwayLineSegment> getSegmentsInOrder(Facility start, Facility end) {
        if (startFacilityId == start.getId() && endFacilityId == end.getId()) {
            return getCorrectSegmentsOrder();
        } else if (startFacilityId == end.getId() && endFacilityId == start.getId()){
            return getReverseSegmentOrder();
        }

        return null;
    }

    /**
     * Determina a direção do percurso entre duas facilities.
     *
     * @param start facility de origem
     * @param end facility de destino
     * @return {@code true} se a direção for direta, {@code false} caso contrário
     */
    public boolean getDirection(Facility start, Facility end) {
        return startFacilityId == start.getId() && endFacilityId == end.getId();
    }

    /**
     * Verifica se a linha ferroviária liga corretamente duas facilities.
     *
     * @param fac1 primeira facility
     * @param fac2 segunda facility
     * @return {@code true} se a linha ligar ambas as facilities
     */
    public boolean correctRailwayLine(Facility fac1, Facility fac2){
        if (fac1.getId() == startFacilityId && fac2.getId() == endFacilityId){
            return true;
        } else if (fac1.getId() == endFacilityId && fac2.getId() == startFacilityId){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Representação textual da linha ferroviária.
     *
     * @return representação em {@code String}
     */
    @Override
    public String toString() {
        return "RailwayLine{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", startFacilityId=" + startFacilityId +
                ", endFacilityId=" + endFacilityId +
                ", ownerVat='" + ownerVat + '\'' +
                '}';
    }
}
