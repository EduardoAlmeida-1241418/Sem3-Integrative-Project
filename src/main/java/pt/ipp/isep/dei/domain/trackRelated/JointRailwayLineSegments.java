package pt.ipp.isep.dei.domain.trackRelated;

import pt.ipp.isep.dei.data.memory.GeneralScheduleStoreInMemory;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa um conjunto lógico de segmentos consecutivos de linha ferroviária.
 * <p>
 * A classe {@code JointRailwayLineSegments} agrega vários {@link RailwayLineSegment}
 * físicos que funcionam como uma única entidade lógica, tipicamente utilizada
 * para representar troços contínuos de via única sem desvios intermédios.
 * </p>
 * <p>
 * Esta classe implementa {@link SegmentRelated}, permitindo que o conjunto
 * seja tratado como um único segmento no processo de agendamento.
 * </p>
 */
public class JointRailwayLineSegments implements SegmentRelated {

    /**
     * Identificador único do conjunto de segmentos.
     */
    private int id;

    /**
     * Lista de segmentos físicos que compõem este conjunto lógico.
     */
    private List<RailwayLineSegment> segments;

    /**
     * Cria um conjunto de segmentos com um identificador explícito.
     * <p>
     * O identificador fornecido é validado para garantir que não existe
     * outro conjunto registado com o mesmo ID.
     * </p>
     *
     * @param id identificador do conjunto
     * @param segments lista de segmentos físicos
     * @throws IllegalArgumentException se o ID já existir
     */
    public JointRailwayLineSegments(int id, List<RailwayLineSegment> segments) {
        this.id = id;
        if (new GeneralScheduleStoreInMemory().existsJointRailwayLineSegmentWithId(id)) {
            throw new IllegalArgumentException("Joint Railway Line Segments with this ID already exists.");
        }

        this.segments = new ArrayList<>(segments);
        new GeneralScheduleStoreInMemory().addJointRailwayLineSegmentId(id);
    }

    /**
     * Cria um conjunto de segmentos com um identificador gerado automaticamente.
     *
     * @param segments lista de segmentos físicos
     */
    public JointRailwayLineSegments(List<RailwayLineSegment> segments) {
        this.id = new GeneralScheduleStoreInMemory().getNextJointRailwayLineSegmentId();
        this.segments = new ArrayList<>(segments);
        new GeneralScheduleStoreInMemory().addJointRailwayLineSegmentId(id);
    }

    /**
     * Devolve o identificador do conjunto de segmentos.
     *
     * @return identificador
     */
    public int getId() {
        return id;
    }

    /**
     * Define o identificador do conjunto de segmentos.
     *
     * @param id novo identificador
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Devolve a lista de segmentos físicos associados.
     *
     * @return lista de {@code RailwayLineSegment}
     */
    public List<RailwayLineSegment> getSegments() {
        return segments;
    }

    /**
     * Define a lista de segmentos físicos associados.
     *
     * @param segments nova lista de segmentos
     */
    public void setSegments(List<RailwayLineSegment> segments) {
        this.segments = segments;
    }

    /**
     * Devolve o número de vias do conjunto lógico.
     * <p>
     * Um conjunto de segmentos é sempre tratado como via única.
     * </p>
     *
     * @return número de vias
     */
    @Override
    public int getNumberTracks(){
        return 1;
    }

    /**
     * Calcula o comprimento total do conjunto de segmentos.
     *
     * @return comprimento total
     */
    @Override
    public int getLength(){
        int length = 0;

        for(RailwayLineSegment segment : segments){
            length += segment.getLength();
        }

        return length;
    }

    /**
     * Calcula o código de hash com base na identidade física dos segmentos.
     *
     * @return valor de hash
     */
    @Override
    public int hashCode() {
        return physicalKey().hashCode();
    }

    /**
     * Verifica a igualdade entre conjuntos de segmentos com base
     * nos segmentos físicos que os compõem.
     *
     * @param o objeto a comparar
     * @return {@code true} se forem equivalentes
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JointRailwayLineSegments other)) return false;
        return physicalKey().equals(other.physicalKey());
    }

    /**
     * Gera a chave física do conjunto com base nos IDs dos segmentos.
     *
     * @return lista ordenada de identificadores de segmentos
     */
    private List<Integer> physicalKey() {
        return segments.stream()
                .map(RailwayLineSegment::getId)
                .sorted()
                .toList();
    }

    /**
     * Determina o limite de velocidade do conjunto.
     * <p>
     * O limite de velocidade corresponde ao menor limite
     * entre todos os segmentos que compõem o conjunto.
     * </p>
     *
     * @return limite de velocidade
     */
    @Override
    public int getSpeedLimit(){
        int speedLimit = Integer.MAX_VALUE;

        for(RailwayLineSegment segment : segments){
            if(segment.getSpeedLimit() < speedLimit){
                speedLimit = segment.getSpeedLimit();
            }
        }

        return speedLimit;
    }

    /**
     * Devolve o nome lógico do conjunto de segmentos.
     * <p>
     * O nome é constituído pela concatenação dos IDs dos segmentos.
     * </p>
     *
     * @return nome do conjunto
     */
    @Override
    public String name() {
        StringBuilder sb = new StringBuilder();

        for (RailwayLineSegment segment : segments) {
            sb.append(segment.getId()).append(" ");
        }

        return sb.toString().trim();
    }

    /**
     * Representação textual do conjunto de segmentos.
     *
     * @return representação em {@code String}
     */
    @Override
    public String toString() {
        return "Line Segments IDs:\n" +
                getSegments().stream()
                        .map(seg -> String.valueOf(seg.getId()))
                        .toList();
    }
}
