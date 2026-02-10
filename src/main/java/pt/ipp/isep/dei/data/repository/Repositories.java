package pt.ipp.isep.dei.data.repository;

import pt.ipp.isep.dei.data.repository.sprint1.*;
import pt.ipp.isep.dei.data.repository.sprint2.CountryRepository;
import pt.ipp.isep.dei.data.repository.sprint2.StationEsinf2Repository;
import pt.ipp.isep.dei.data.repository.sprint3.*;

/**
 * Singleton class that manages all repositories in the application.
 * <p>
 * Inspired by: <a href="https://refactoring.guru/design-patterns/singleton/java/example">Refactoring Guru - Singleton Pattern</a>
 * </p>
 * <p>
 * Provides a single access point to all repository instances, ensuring that only one instance of each repository exists throughout the system.
 * </p>
 */
public class Repositories {

    /** The single instance of the {@link Repositories} class. */
    private static Repositories instance;

    /** Repository for managing user data. */
    private static UserRepository userRepository;

    private final LogRepository logRepository;
    private final AisleRepository aisleRepository;
    private final BayRepository bayRepository;
    private final BoxRepository boxRepository;
    private final FacilityRepository facilityRepository;
    private final ItemInfoRepository itemInfoRepository;
    private final LocomotiveModelRepository locomotiveModelRepository;
    private final LocomotiveRepository locomotiveRepository;
    private final OperatorRepository operatorRepository;
    private final OrderLineRepository orderLineRepository;
    private final OrderRepository orderRepository;
    private final OwnerRepository ownerRepository;
    private final PickingPlansRepository pickingPlansRepository;
    private final RailwayLineRepository railwayLineRepository;
    private final RailwayLineSegmentRepository railwayLineSegmentRepository;
    private final ReturnRepository returnRepository;
    private final TrolleyModelRepository trolleyModelRepository;
    private final TrolleyRepository trolleyRepository;
    private final WagonRepository wagonRepository;
    private final WarehouseRepository warehouseRepository;
    private final CountryRepository countryRepository;
    private final StationEsinf2Repository stationEsinf2Repository;
    private final EdgeEsinfRepository edgeEsinfRepository;
    private final NodeEsinfRepository nodeEsinfRepository;
    private final RailwayLineEsinfRepository railwayLineEsinfRepository;
    private final StationEsinf3Repository stationEsinf3Repository;

    public Repositories() {
        userRepository = new UserRepository();
        logRepository = new LogRepository();
        aisleRepository = new AisleRepository();
        bayRepository = new BayRepository();
        boxRepository = new BoxRepository();
        facilityRepository = new FacilityRepository();
        itemInfoRepository = new ItemInfoRepository();
        locomotiveModelRepository = new LocomotiveModelRepository();
        locomotiveRepository = new LocomotiveRepository();
        operatorRepository = new OperatorRepository();
        orderLineRepository = new OrderLineRepository();
        orderRepository = new OrderRepository();
        ownerRepository = new OwnerRepository();
        pickingPlansRepository = new PickingPlansRepository();
        railwayLineRepository = new RailwayLineRepository();
        railwayLineSegmentRepository = new RailwayLineSegmentRepository();
        returnRepository = new ReturnRepository();
        trolleyModelRepository = new TrolleyModelRepository();
        trolleyRepository = new TrolleyRepository();
        wagonRepository = new WagonRepository();
        warehouseRepository = new WarehouseRepository();
        countryRepository = new CountryRepository();
        stationEsinf2Repository = new StationEsinf2Repository();
        edgeEsinfRepository = new EdgeEsinfRepository();
        nodeEsinfRepository = new NodeEsinfRepository();
        railwayLineEsinfRepository = new RailwayLineEsinfRepository();
        stationEsinf3Repository = new StationEsinf3Repository();
    }

    /**
     * Returns the single instance of {@link Repositories}.
     * If it does not exist yet, it is created in a thread-safe manner.
     *
     * @return the singleton instance of {@link Repositories}
     */
    public static Repositories getInstance() {
        if (instance == null) {
            synchronized (Repositories.class) {
                instance = new Repositories();
            }
        }
        return instance;
    }

    public static UserRepository getUserRepository() {
        return userRepository;
    }

    public LogRepository getLogRepository() {
        return logRepository;
    }

    public AisleRepository getAisleRepository() {
        return aisleRepository;
    }

    public BayRepository getBayRepository() {
        return bayRepository;
    }

    public BoxRepository getBoxRepository() {
        return boxRepository;
    }

    public FacilityRepository getFacilityRepository() {
        return facilityRepository;
    }

    public ItemInfoRepository getItemInfoRepository() {
        return itemInfoRepository;
    }

    public LocomotiveModelRepository getLocomotiveModelRepository() {
        return locomotiveModelRepository;
    }

    public LocomotiveRepository getLocomotiveRepository() {
        return locomotiveRepository;
    }

    public OperatorRepository getOperatorRepository() {
        return operatorRepository;
    }

    public OrderLineRepository getOrderLineRepository() {
        return orderLineRepository;
    }

    public OrderRepository getOrderRepository() {
        return orderRepository;
    }

    public OwnerRepository getOwnerRepository() {
        return ownerRepository;
    }

    public PickingPlansRepository getPickingPlansRepository() {
        return pickingPlansRepository;
    }

    public RailwayLineRepository getRailwayLineRepository() {
        return railwayLineRepository;
    }

    public RailwayLineSegmentRepository getRailwayLineSegmentRepository() {
        return railwayLineSegmentRepository;
    }

    public ReturnRepository getReturnRepository() {
        return returnRepository;
    }

    public TrolleyModelRepository getTrolleyModelRepository() {
        return trolleyModelRepository;
    }

    public TrolleyRepository getTrolleyRepository() {
        return trolleyRepository;
    }

    public WagonRepository getWagonRepository() {
        return wagonRepository;
    }

    public WarehouseRepository getWarehouseRepository() {
        return warehouseRepository;
    }

    public CountryRepository getCountryRepository() {
        return countryRepository;
    }

    public StationEsinf2Repository getStationEsinf2Repository() {
        return stationEsinf2Repository;
    }

    public EdgeEsinfRepository getEdgeEsinfRepository() {
        return edgeEsinfRepository;
    }

    public NodeEsinfRepository getNodeEsinfRepository() {
        return nodeEsinfRepository;
    }

    public RailwayLineEsinfRepository getRailwayLineEsinfRepository() {
        return railwayLineEsinfRepository;
    }

    public StationEsinf3Repository getStationEsinf3Repository() {
        return stationEsinf3Repository;
    }

    public static void setInstance(Repositories newInstance) {
        instance = newInstance;
    }
}
