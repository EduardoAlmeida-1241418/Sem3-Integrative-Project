package pt.ipp.isep.dei.controller.global;

import pt.ipp.isep.dei.data.repository.Repositories;
import pt.ipp.isep.dei.data.repository.sprint3.EdgeEsinfRepository;
import pt.ipp.isep.dei.data.repository.sprint3.NodeEsinfRepository;
import pt.ipp.isep.dei.data.repository.sprint3.RailwayLineEsinfRepository;
import pt.ipp.isep.dei.data.repository.sprint3.StationEsinf3Repository;
import pt.ipp.isep.dei.domain.ESINF.RailwayLineEsinf;
import pt.ipp.isep.dei.domain.ESINF.StationEsinf;
import pt.ipp.isep.dei.domain.Graph.Edge;
import pt.ipp.isep.dei.domain.Graph.MetricsStationEdge;
import pt.ipp.isep.dei.domain.Graph.Node;
import pt.ipp.isep.dei.domain.LogType;
import pt.ipp.isep.dei.domain.RoleType;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.util.List;

/**
 * Controller responsável pelo carregamento do dataset do Sprint 3.
 *
 * Esta classe trata da leitura de ficheiros CSV, validação dos dados,
 * armazenamento em repositórios e construção do grafo ferroviário.
 */
public class LoadSprint3DataSetController implements Runnable{

    /**
     * Diretório base onde se encontram os ficheiros CSV do Sprint 3.
     */
    private String DATA_SET_DIRECTORY_CSV_ESINF_PATH = "dataset/sprint3";

    /**
     * Repositório de estações.
     */
    private StationEsinf3Repository stationRepository;

    /**
     * Repositório de linhas ferroviárias.
     */
    private RailwayLineEsinfRepository railwayLineRepository;

    /**
     * Repositório de nós do grafo.
     */
    private NodeEsinfRepository nodeEsinfRepository;

    /**
     * Repositório de arestas do grafo.
     */
    private EdgeEsinfRepository edgeEsinfRepository;

    /**
     * Executa o carregamento completo do dataset do Sprint 3.
     *
     * Inicializa os repositórios, carrega os dados das estações,
     * das linhas ferroviárias e constrói o grafo da rede ferroviária.
     */
    @Override
    public void run() {
        Repositories repositories = Repositories.getInstance();

        this.stationRepository = repositories.getStationEsinf3Repository();
        this.railwayLineRepository = repositories.getRailwayLineEsinfRepository();
        this.nodeEsinfRepository = repositories.getNodeEsinfRepository();
        this.edgeEsinfRepository = repositories.getEdgeEsinfRepository();

        UIUtils.addLog("------------------------------ Starting CSV data sprint 3 loading... ------------------------------", LogType.INFO, RoleType.GLOBAL);
        loadDataSet();
        loadSprint3Lines();
        loadRailwayNetworkGraph();
    }

    /**
     * Carrega e valida os dados das estações a partir do ficheiro stations.csv.
     *
     * Cada estação é validada antes de ser adicionada ao repositório,
     * garantindo integridade dos dados.
     */
    private void loadDataSet() {
        List<String[]> stationsList = UIUtils.readCSV( DATA_SET_DIRECTORY_CSV_ESINF_PATH + "/stations.csv");

        for (String[] stationData : stationsList) {
            if (stationData.length != 6) {
                UIUtils.addLog("Invalid station data: incorrect number of fields.", LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            String stationId = stationData[0];
            if (stationId == null || stationId.isEmpty()) {
                UIUtils.addLog("Invalid station data: missing station ID.", LogType.ERROR, RoleType.GLOBAL);
                continue;
            }
            if (stationRepository.existsById(stationId)) {
                UIUtils.addLog("Duplicate station ID: " + stationId, LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            String stationName = stationData[1];
            if (stationName == null || stationName.isEmpty()) {
                UIUtils.addLog("Invalid station data: missing station name.", LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            try {
                double latitude = Double.parseDouble(stationData[2]);
                if (latitude < -90 || latitude > 90) {
                    UIUtils.addLog("Invalid latitude for station ID: " + stationId, LogType.ERROR, RoleType.GLOBAL);
                    continue;
                }

                double longitude = Double.parseDouble(stationData[3]);
                if (longitude < -180 || longitude > 180) {
                    UIUtils.addLog("Invalid longitude for station ID: " + stationId, LogType.ERROR, RoleType.GLOBAL);
                    continue;
                }

                double coordX = Double.parseDouble(stationData[4]);

                double coordY = Double.parseDouble(stationData[5]);

                StationEsinf newStation = new StationEsinf(stationId, stationName, latitude, longitude, coordX, coordY);

                stationRepository.add(newStation);
                UIUtils.addLog("Station loaded: " + stationId, LogType.INFO, RoleType.GLOBAL);
            } catch (NumberFormatException e) {
                continue;
            }
        }
    }

    /**
     * Carrega e valida os dados das linhas ferroviárias a partir do ficheiro lines.csv.
     *
     * Garante que as estações de origem e destino existem
     * antes de criar a linha ferroviária.
     */
    private void loadSprint3Lines() {
        List<String[]> linesList = UIUtils.readCSV(DATA_SET_DIRECTORY_CSV_ESINF_PATH + "/lines.csv");

        for (String[] line : linesList) {
            if (line.length != 5) {
                UIUtils.addLog("Invalid line data: incorrect number of fields.", LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            if (line[0] == null || line[0].isEmpty()
                    || line[1] == null || line[1].isEmpty()
                    || line[2] == null || line[2].isEmpty()
                    || line[3] == null || line[3].isEmpty()
                    || line[4] == null || line[4].isEmpty()) {
                UIUtils.addLog("Invalid line data: missing required fields.", LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            String departureStationId = line[0];
            if (Integer.parseInt(departureStationId) < 0) {
                UIUtils.addLog("Invalid departure station ID: " + departureStationId, LogType.ERROR, RoleType.GLOBAL);
                continue;
            }
            if (!stationRepository.existsById(departureStationId)) {
                UIUtils.addLog("Departure station does not exist: " + departureStationId, LogType.ERROR, RoleType.GLOBAL);
                continue;
            }
            StationEsinf departureStation = stationRepository.findById(departureStationId);
            if (departureStation == null) {
                UIUtils.addLog("Departure station retrieval failed: " + departureStationId, LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            String arrivalStationId = line[1];
            if (Integer.parseInt(arrivalStationId) < 0) {
                UIUtils.addLog("Invalid arrival station ID: " + arrivalStationId, LogType.ERROR, RoleType.GLOBAL);
                continue;
            }
            if (!stationRepository.existsById(arrivalStationId)) {
                UIUtils.addLog("Arrival station does not exist: " + arrivalStationId, LogType.ERROR, RoleType.GLOBAL);
                continue;
            }
            StationEsinf arrivalStation = stationRepository.findById(arrivalStationId);
            if (arrivalStation == null) {
                UIUtils.addLog("Arrival station retrieval failed: " + arrivalStationId, LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            try {
                double distanceKm = Double.parseDouble(line[2]);
                if (distanceKm < 0) {
                    UIUtils.addLog("Invalid distance for line from " + departureStationId + " to " + arrivalStationId, LogType.ERROR, RoleType.GLOBAL);
                    continue;
                }

                int capacity = Integer.parseInt(line[3]);
                if (capacity < 0) {
                    UIUtils.addLog("Invalid capacity for line from " + departureStationId + " to " + arrivalStationId, LogType.ERROR, RoleType.GLOBAL);
                    continue;
                }

                double cost = Double.parseDouble(line[4]);

                RailwayLineEsinf newLine = new RailwayLineEsinf(departureStation, arrivalStation, distanceKm, capacity, cost);

                railwayLineRepository.add(newLine);
                UIUtils.addLog("Railway line loaded from " + departureStationId + " to " + arrivalStationId, LogType.INFO, RoleType.GLOBAL);

            } catch (NumberFormatException e) {
                continue;
            }
        }
    }

    /**
     * Constrói o grafo da rede ferroviária.
     *
     * Cria os nós correspondentes às estações e as arestas
     * correspondentes às linhas ferroviárias.
     */
    private void loadRailwayNetworkGraph() {
        for (StationEsinf station : stationRepository.findAll()) {
            nodeEsinfRepository.addNode(new Node<>(station, station.getId()));
            UIUtils.addLog("Node added for station: " + station.getId(), LogType.INFO, RoleType.GLOBAL);
        }

        for (RailwayLineEsinf line : railwayLineRepository.findAll()) {
            Node<StationEsinf> sourceNode = nodeEsinfRepository.getNodeByKey(line.getDepartureStation().getId());
            Node<StationEsinf> targetNode = nodeEsinfRepository.getNodeByKey(line.getArrivalStation().getId());
            if (sourceNode != null && targetNode != null) {
                edgeEsinfRepository.addEdge(new Edge<>(sourceNode, targetNode, new MetricsStationEdge(line.getDistanceKm(), line.getCapacity(), line.getCost()), true));
                UIUtils.addLog("Edge added from " + line.getDepartureStation().getId() + " to " + line.getArrivalStation().getId(), LogType.INFO, RoleType.GLOBAL);
            }
        }
    }
}
