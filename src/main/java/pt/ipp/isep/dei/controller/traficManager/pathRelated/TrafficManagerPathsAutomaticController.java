package pt.ipp.isep.dei.controller.traficManager.pathRelated;

import pt.ipp.isep.dei.controller.algorithms.BellmanFordAlgorithm;
import pt.ipp.isep.dei.controller.algorithms.LAPR.LAPRShortestPathAlgorithm;
import pt.ipp.isep.dei.data.memory.*;
import pt.ipp.isep.dei.domain.Facility;
import pt.ipp.isep.dei.domain.trackRelated.RailwayLine;
import pt.ipp.isep.dei.domain.trackRelated.RailwayLineSegment;
import pt.ipp.isep.dei.domain.trackRelated.RailwaySegmentType;
import pt.ipp.isep.dei.domain.trackRelated.TrackGauge;
import pt.ipp.isep.dei.domain.transportationRelated.Path;
import pt.ipp.isep.dei.domain.transportationRelated.Route;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Controller responsável pela geração automática de caminhos
 * para o gestor de tráfego.
 *
 * Este controller seleciona linhas ferroviárias compatíveis
 * com o tipo de segmento e bitola, e calcula o caminho mais curto
 * entre as instalações de uma rota.
 */
public class TrafficManagerPathsAutomaticController {

    /**
     * Store de rotas em memória.
     */
    private RouteStoreInMemory routeStoreInMemory;

    /**
     * Store de bitolas em memória.
     */
    private TrackGaugeStoreInMemory  trackGaugeStoreInMemory;

    /**
     * Store de instalações em memória.
     */
    private FacilityStoreInMemory facilityStoreInMemory;

    /**
     * Store de linhas ferroviárias em memória.
     */
    private RailwayLineStoreInMemory railwayLineStoreInMemory;

    /**
     * Lista de instalações disponíveis.
     */
    private List<Facility> facilities;

    /**
     * Lista de linhas ferroviárias disponíveis.
     */
    private List<RailwayLine> railwayLineList;

    /**
     * Rota atualmente selecionada.
     */
    private Route selectedRoute;

    /**
     * Caminho calculado para a rota selecionada.
     */
    private Path path;

    /**
     * Construtor do controller.
     *
     * Inicializa as stores em memória e carrega as listas necessárias.
     */
    public TrafficManagerPathsAutomaticController() {
        loadMemory();

        facilities = facilityStoreInMemory.findAll();

        loadRailwayLists();
    }

    /**
     * Inicializa todas as stores em memória.
     */
    private void loadMemory() {
        routeStoreInMemory = new RouteStoreInMemory();
        trackGaugeStoreInMemory = new TrackGaugeStoreInMemory();
        facilityStoreInMemory = new FacilityStoreInMemory();
        railwayLineStoreInMemory = new RailwayLineStoreInMemory();
    }

    /**
     * Carrega a lista de linhas ferroviárias disponíveis.
     */
    private void loadRailwayLists() {
        railwayLineList = railwayLineStoreInMemory.findAll();
    }

    /**
     * Atualiza a lista de linhas ferroviárias disponíveis
     * de acordo com o tipo de segmento e a bitola selecionados.
     *
     * @param railwaySegmentType tipo de segmento ferroviário
     * @param trackGauge bitola pretendida
     */
    private void updateAvailableRailwayLines(RailwaySegmentType railwaySegmentType, TrackGauge trackGauge){
        loadRailwayLists();
        Iterator<RailwayLine> it = railwayLineList.iterator();

        while(it.hasNext()){
            RailwayLine line = it.next();
            for(RailwayLineSegment segment : line.getSegments()){

                TrackGauge segmentGauge = trackGaugeStoreInMemory.findById(null, segment.getTrackGaugeId() + "");
                if(trackGauge != segmentGauge || (railwaySegmentType == RailwaySegmentType.NOT_ELECTRIC && segment.getElectrifiedLine() == RailwaySegmentType.ELECTRIC)){
                    it.remove();
                    break;
                }
            }
        }
    }

    /**
     * Gera automaticamente um caminho para uma rota.
     *
     * O caminho é calculado com base nas instalações,
     * linhas ferroviárias compatíveis e critérios definidos.
     *
     * @param route rota selecionada
     * @param railwaySegmentType tipo de segmento ferroviário
     * @param trackGauge bitola selecionada
     */
    public void generatePath(Route route, RailwaySegmentType railwaySegmentType, TrackGauge trackGauge){
        this.selectedRoute = route;

        updateAvailableRailwayLines(railwaySegmentType, trackGauge);

        LAPRShortestPathAlgorithm LAPRShortestPathAlgorithm = new LAPRShortestPathAlgorithm();

        List<Facility> facilityPath = LAPRShortestPathAlgorithm.findGeneralShortestPath(route, facilities, railwayLineList);

        path = new Path(facilityPath);
        route.setPath(path);
        new PathStoreInMemory().save(null, path);
    }

    /**
     * Calcula o custo total do caminho gerado.
     *
     * @return custo total do caminho
     */
    public int getPathCost(){
        LAPRShortestPathAlgorithm LAPRShortestPathAlgorithm = new LAPRShortestPathAlgorithm();
        return LAPRShortestPathAlgorithm.totalPathCost(path.getFacilities(), railwayLineList);
    }

    /**
     * Obtém as rotas que ainda não possuem caminho associado.
     *
     * @return lista de rotas disponíveis
     */
    public List<Route> getRoutes() {
        List<Route> routes = new ArrayList<>(routeStoreInMemory.findAll());

        Iterator<Route> iterator = routes.iterator();
        while (iterator.hasNext()) {
            Route route = iterator.next();
            if (route.getPath() != null) {
                iterator.remove();
            }
        }

        return List.copyOf(routes);
    }

    /**
     * Obtém todas as bitolas disponíveis.
     *
     * @return lista de bitolas
     */
    public List<TrackGauge> getTrackGauges() {
        return List.copyOf(trackGaugeStoreInMemory.findAll());
    }

    /**
     * Obtém o caminho atualmente gerado.
     *
     * @return caminho
     */
    public Path getPath() {
        return path;
    }

    /**
     * Define o caminho atual.
     *
     * @param path caminho
     */
    public void setPath(Path path) {
        this.path = path;
    }

    /**
     * Obtém a rota selecionada.
     *
     * @return rota selecionada
     */
    public Route getSelectedRoute() {
        return selectedRoute;
    }

    /**
     * Define a rota selecionada.
     *
     * @param selectedRoute rota
     */
    public void setSelectedRoute(Route selectedRoute) {
        this.selectedRoute = selectedRoute;
    }

    /**
     * Procura uma bitola pelo seu valor textual.
     *
     * @param selectedItem valor selecionado
     * @return bitola correspondente ou null
     */
    public TrackGauge findTrackGauge(String selectedItem) {
        for (TrackGauge trackGauge :  trackGaugeStoreInMemory.findAll()) {
            if (String.valueOf(trackGauge.getGaugeSize()).equals(selectedItem)) {
                return trackGauge;
            }
        }
        return null;
    }

    /**
     * Associa o caminho calculado à rota selecionada.
     */
    public void createPath() {
        this.selectedRoute.setPath(path);
    }
}
