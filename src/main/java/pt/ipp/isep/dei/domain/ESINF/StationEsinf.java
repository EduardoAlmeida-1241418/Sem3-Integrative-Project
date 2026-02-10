package pt.ipp.isep.dei.domain.ESINF;

import pt.ipp.isep.dei.domain.Country;
import pt.ipp.isep.dei.domain.TimeZone;
import pt.ipp.isep.dei.domain.TimeZoneGroup;
import pt.ipp.isep.dei.domain.Tree.KDTree.KDTreeNodeInterface;

/**
 * Representa uma estação ferroviária no contexto ESINF.
 *
 * Contém informação geográfica, administrativa e operacional,
 * sendo comparável por identificador e compatível com estruturas KD-Tree.
 */
public class StationEsinf implements Comparable<StationEsinf>, KDTreeNodeInterface {

    /**
     * Identificador único da estação.
     */
    private String id;

    /**
     * País onde a estação se encontra.
     */
    private Country country;

    /**
     * Fuso horário da estação.
     */
    private TimeZone timeZone;

    /**
     * Grupo de fuso horário da estação.
     */
    private TimeZoneGroup timeZoneGroup;

    /**
     * Nome da estação.
     */
    private String stationName;

    /**
     * Latitude geográfica da estação.
     */
    private double latitude;

    /**
     * Longitude geográfica da estação.
     */
    private double longitude;

    /**
     * Indica se a estação se localiza numa cidade.
     */
    private boolean is_city;

    /**
     * Indica se a estação é principal.
     */
    private boolean is_main_station;

    /**
     * Indica se a estação corresponde a um aeroporto.
     */
    private boolean is_airport;

    /**
     * Coordenada X utilizada para representação gráfica.
     */
    private double coordX;

    /**
     * Coordenada Y utilizada para representação gráfica.
     */
    private double coordY;

    /**
     * Construtor completo da estação ESINF.
     *
     * Gera automaticamente o identificador com base
     * nos atributos fornecidos.
     *
     * @param country país
     * @param timeZone fuso horário
     * @param timeZoneGroup grupo de fuso horário
     * @param stationName nome da estação
     * @param latitude latitude
     * @param longitude longitude
     * @param is_city indica se é cidade
     * @param is_main_station indica se é estação principal
     * @param is_airport indica se é aeroporto
     */
    public StationEsinf(Country country, TimeZone timeZone, TimeZoneGroup timeZoneGroup,
                        String stationName, double latitude, double longitude,
                        boolean is_city, boolean is_main_station, boolean is_airport) {

        this.id =
                (country != null ? country.toString() : "null") + "_" +
                        (stationName != null ? stationName : "null") + "_" +
                        (timeZone != null ? timeZone.getZoneId() : "null") + "_" +
                        (timeZoneGroup != null ? timeZoneGroup.getDescription() : "null") + "_" +
                        latitude + "_" +
                        longitude + "_" +
                        is_city + "_" +
                        is_main_station + "_" +
                        is_airport;

        this.country = country;
        this.timeZone = timeZone;
        this.timeZoneGroup = timeZoneGroup;
        this.stationName = stationName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.is_city = is_city;
        this.is_main_station = is_main_station;
        this.is_airport = is_airport;
    }

    /**
     * Construtor com coordenadas gráficas.
     *
     * @param id identificador
     * @param stationName nome da estação
     * @param latitude latitude
     * @param longitude longitude
     * @param coordX coordenada X
     * @param coordY coordenada Y
     */
    public StationEsinf(String id, String stationName, double latitude, double longitude, double coordX, double coordY) {
        this.id = id;
        this.stationName = stationName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.coordX = coordX;
        this.coordY = coordY;
    }

    /**
     * Construtor simples com identificação e coordenadas.
     *
     * @param id identificador
     * @param stationName nome da estação
     * @param latitude latitude
     * @param longitude longitude
     */
    public StationEsinf(String id, String stationName, double latitude, double longitude) {
        this.id = id;
        this.stationName = stationName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Construtor mínimo com identificação e coordenadas.
     *
     * @param id identificador
     * @param latitude latitude
     * @param longitude longitude
     */
    public StationEsinf(String id, double latitude, double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Obtém o identificador da estação.
     *
     * @return identificador
     */
    public String getId() {
        return id;
    }

    /**
     * Atualiza o identificador da estação com base
     * no país e no nome da estação.
     */
    public void setId() {
        this.id = country + "_" + stationName;
    }

    /**
     * Obtém o país da estação.
     *
     * @return país
     */
    public Country getCountry() {
        return country;
    }

    /**
     * Define o país da estação.
     *
     * @param country país
     */
    public void setCountry(Country country) {
        this.country = country;
        setId();
    }

    /**
     * Obtém o fuso horário.
     *
     * @return fuso horário
     */
    public TimeZone getTimeZone() {
        return timeZone;
    }

    /**
     * Define o fuso horário.
     *
     * @param timeZone fuso horário
     */
    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    /**
     * Obtém o grupo de fuso horário.
     *
     * @return grupo de fuso horário
     */
    public TimeZoneGroup getTimeZoneGroup() {
        return timeZoneGroup;
    }

    /**
     * Define o grupo de fuso horário.
     *
     * @param timeZoneGroup grupo
     */
    public void setTimeZoneGroup(TimeZoneGroup timeZoneGroup) {
        this.timeZoneGroup = timeZoneGroup;
    }

    /**
     * Obtém o nome da estação.
     *
     * @return nome da estação
     */
    public String getStationName() {
        return stationName;
    }

    /**
     * Define o nome da estação.
     *
     * @param stationName nome
     */
    public void setStationName(String stationName) {
        this.stationName = stationName;
        setId();
    }

    /**
     * Obtém a latitude.
     *
     * @return latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Define a latitude.
     *
     * @param latitude latitude
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Obtém a longitude.
     *
     * @return longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Define a longitude.
     *
     * @param longitude longitude
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Indica se a estação se localiza numa cidade.
     *
     * @return true se for cidade
     */
    public boolean isIs_city() {
        return is_city;
    }

    /**
     * Define se a estação se localiza numa cidade.
     *
     * @param is_city flag de cidade
     */
    public void setIs_city(boolean is_city) {
        this.is_city = is_city;
    }

    /**
     * Indica se é estação principal.
     *
     * @return true se for principal
     */
    public boolean isIs_main_station() {
        return is_main_station;
    }

    /**
     * Define se é estação principal.
     *
     * @param is_main_station flag de principal
     */
    public void setIs_main_station(boolean is_main_station) {
        this.is_main_station = is_main_station;
    }

    /**
     * Indica se é aeroporto.
     *
     * @return true se for aeroporto
     */
    public boolean isIs_airport() {
        return is_airport;
    }

    /**
     * Define se é aeroporto.
     *
     * @param is_airport flag de aeroporto
     */
    public void setIs_airport(boolean is_airport) {
        this.is_airport = is_airport;
    }

    /**
     * Obtém a coordenada X.
     *
     * @return coordenada X
     */
    public double getCoordX() {
        return coordX;
    }

    /**
     * Define a coordenada X.
     *
     * @param coordX coordenada X
     */
    public void setCoordX(double coordX) {
        this.coordX = coordX;
    }

    /**
     * Obtém a coordenada Y.
     *
     * @return coordenada Y
     */
    public double getCoordY() {
        return coordY;
    }

    /**
     * Define a coordenada Y.
     *
     * @param coordY coordenada Y
     */
    public void setCoordY(double coordY) {
        this.coordY = coordY;
    }

    @Override
    public String toString() {
        return stationName + " (" + country + ") [" + latitude + ", " + longitude + "]";
    }

    /**
     * Compara esta estação com outra com base no identificador.
     *
     * @param o estação a comparar
     * @return resultado da comparação
     */
    @Override
    public int compareTo(StationEsinf o) {
        return this.id.compareTo(o.id);
    }

    /**
     * Obtém a coordenada X para utilização em KD-Tree.
     *
     * @return valor X
     */
    @Override
    public double getX() {
        return latitude;
    }

    /**
     * Obtém a coordenada Y para utilização em KD-Tree.
     *
     * @return valor Y
     */
    @Override
    public double getY() {
        return longitude;
    }
}
