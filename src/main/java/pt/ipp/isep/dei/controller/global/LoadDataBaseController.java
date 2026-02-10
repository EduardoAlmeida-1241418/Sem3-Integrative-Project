package pt.ipp.isep.dei.controller.global;

import pt.ipp.isep.dei.data.config.ActualDatabaseConnection;
import pt.ipp.isep.dei.data.config.ConnectionFactory;
import pt.ipp.isep.dei.data.config.DatabaseConnection;
import pt.ipp.isep.dei.data.memory.*;
import pt.ipp.isep.dei.data.store.*;
import pt.ipp.isep.dei.domain.*;
import pt.ipp.isep.dei.domain.schedule.GeneralSchedule;
import pt.ipp.isep.dei.domain.schedule.ScheduleEvent;
import pt.ipp.isep.dei.domain.schedule.ScheduleGenerator;
import pt.ipp.isep.dei.domain.trackRelated.RailwayLine;
import pt.ipp.isep.dei.domain.trackRelated.RailwayLineSegment;
import pt.ipp.isep.dei.domain.trackRelated.Siding;
import pt.ipp.isep.dei.domain.trackRelated.TrackGauge;
import pt.ipp.isep.dei.domain.trainRelated.Train;
import pt.ipp.isep.dei.domain.transportationRelated.Freight;
import pt.ipp.isep.dei.domain.transportationRelated.Path;
import pt.ipp.isep.dei.domain.transportationRelated.Route;
import pt.ipp.isep.dei.domain.wagonRelated.Wagon;
import pt.ipp.isep.dei.domain.wagonRelated.WagonModel;
import pt.ipp.isep.dei.domain.wagonRelated.WagonType;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Responsible for loading domain data from the configured database into the
 * in-memory stores used by the application.
 *
 * <p>This Runnable sequentially invokes loaders for each domain entity. It is
 * intended to be run at application start-up to populate the in-memory
 * repositories from persistent storage. The class performs no schema changes
 * and only prints summarising information to standard output.
 */
public class LoadDataBaseController implements Runnable {

    private final DatabaseConnection db;

    /**
     * Construct a new LoadDataBaseController and obtain the database
     * connection from the configured connection factory.
     *
     * @throws IOException if the database connection cannot be initialised
     */
    public LoadDataBaseController() throws IOException {
        this.db = ActualDatabaseConnection.getDb();
    }

    /**
     * Execute the full load sequence.
     *
     * <p>This method invokes each specific loader in turn. It is safe to call
     * repeatedly; individual stores are cleared prior to being repopulated.
     */
    @Override
    public void run() {
        UIUtils.addLog("------------------------------ Starting database load sequence... ------------------------------", LogType.INFO, RoleType.GLOBAL);
        loadOperators();
        loadOwners();
        loadMakers();
        loadTrackGauges();
        loadDimensions();
        loadFacilities();
        loadBuildings();
        loadLocomotiveModels();
        loadWagonTypes();
        loadWagonModels();
        loadRailwayLineSegments();
        loadRailwayLines();
        RailwayTopologyBuilder.build(db);
        loadSidings();
        loadRoutes();
        loadLocomotives();
        loadTrains();
        loadWagons();
        loadPaths();
        loadFreights();
        loadGeneralSchedules();
        loadScheduleEvents();
    }

    /**
     * Load operator entities from persistent storage into the in-memory store.
     */
    private void loadOperators() {
        OperatorStore operatorStore = new OperatorStore();
        OperatorStoreInMemory operatorStoreInMemory = new OperatorStoreInMemory();
        operatorStoreInMemory.clear();
        List<Operator> operators = operatorStore.findAll(db);

        for (Operator operator : operators) {
            operatorStoreInMemory.save(db, operator);
        }

        for (Operator operator : operatorStoreInMemory.findAll()) {
            UIUtils.addLog("Loaded Operator from db: " + operator.getId(), LogType.INFO, RoleType.GLOBAL);
        }
    }

    /**
     * Load owner entities from persistent storage into the in-memory store.
     */
    private void loadOwners() {
        OwnerStore ownerStore = new OwnerStore();
        OwnerStoreInMemory ownerStoreInMemory = new OwnerStoreInMemory();
        ownerStoreInMemory.clear();
        List<Owner> owners = ownerStore.findAll(db);

        for (Owner owner : owners) {
            ownerStoreInMemory.save(db, owner);
        }

        for (Owner owner : ownerStoreInMemory.findAll()) {
            UIUtils.addLog("Loaded Owner from db: " + owner.getVatNumber(), LogType.INFO, RoleType.GLOBAL);
        }
    }

    /**
     * Load maker entities from persistent storage into the in-memory store.
     */
    private void loadMakers() {
        MakerStore makerStore = new MakerStore();
        MakerStoreInMemory makerStoreInMemory = new MakerStoreInMemory();
        makerStoreInMemory.clear();
        List<Maker> makers = makerStore.findAll(db);

        for (Maker maker : makers) {
            makerStoreInMemory.save(db, maker);
        }

        for (Maker maker : makerStoreInMemory.findAll()) {
            UIUtils.addLog("Loaded Maker from db: " + maker.getId(), LogType.INFO, RoleType.GLOBAL);
        }
    }

    /**
     * Load track gauge entities from persistent storage into the in-memory
     * store.
     */
    private void loadTrackGauges() {
        TrackGaugeStore trackGaugeStore = new TrackGaugeStore();
        TrackGaugeStoreInMemory trackGaugeStoreInMemory = new TrackGaugeStoreInMemory();
        trackGaugeStoreInMemory.clear();
        List<TrackGauge> trackGauges = trackGaugeStore.findAll(db);

        for (TrackGauge trackGauge : trackGauges) {
            trackGaugeStoreInMemory.save(db, trackGauge);
        }

        for (TrackGauge trackGauge : trackGaugeStoreInMemory.findAll()) {
            UIUtils.addLog("Loaded Track Gauge from db: " + trackGauge.getId(), LogType.INFO, RoleType.GLOBAL);
        }
    }

    /**
     * Load dimension entities from persistent storage into the in-memory
     * store.
     */
    private void loadDimensions() {
        DimensionsStore dimensionsStore = new DimensionsStore();
        DimensionsStoreInMemory dimensionsStoreInMemory = new DimensionsStoreInMemory();
        dimensionsStoreInMemory.clear();
        List<Dimensions> dimensionsList = dimensionsStore.findAll(db);

        for (Dimensions dimensions : dimensionsList) {
            dimensionsStoreInMemory.save(db, dimensions);
        }

        for (Dimensions dimensions : dimensionsStoreInMemory.findAll()) {
            UIUtils.addLog("Loaded Dimensions from db: " + dimensions.getId(), LogType.INFO, RoleType.GLOBAL);
        }
    }

    /**
     * Load facility entities from persistent storage into the in-memory
     * store.
     */
    private void loadFacilities() {
        FacilityStore facilityStore = new FacilityStore();
        FacilityStoreInMemory facilityStoreInMemory = new FacilityStoreInMemory();
        facilityStoreInMemory.clear();
        List<Facility> facilities = facilityStore.findAll(db);

        for (Facility facility : facilities) {
            facilityStoreInMemory.save(db, facility);
        }

        for (Facility facility : facilityStoreInMemory.findAll()) {
            UIUtils.addLog("Loaded Facility from db: " + facility.getId(), LogType.INFO, RoleType.GLOBAL);
        }
    }

    /**
     * Load building entities from persistent storage into the in-memory
     * store.
     */
    private void loadBuildings() {
        BuildingStore buildingStore = new BuildingStore();
        BuildingStoreInMemory buildingStoreInMemory = new BuildingStoreInMemory();
        buildingStoreInMemory.clear();
        List<Building> buildings = buildingStore.findAll(db);

        for (Building building : buildings) {
            buildingStoreInMemory.save(db, building);
        }

        for (Building building : buildingStoreInMemory.findAll()) {
            UIUtils.addLog("Loaded Building from db: " + building.getId(), LogType.INFO, RoleType.GLOBAL);
        }
    }

    /**
     * Load locomotive model entities from persistent storage into the
     * in-memory store.
     */
    private void loadLocomotiveModels() {
        LocomotiveModelStore locomotiveModelStore = new LocomotiveModelStore();
        LocomotiveModelStoreInMemory locomotiveModelStoreInMemory = new LocomotiveModelStoreInMemory();
        locomotiveModelStoreInMemory.clear();
        List<LocomotiveModel> locomotiveModels = locomotiveModelStore.findAll(db);

        for (LocomotiveModel locomotiveModel : locomotiveModels) {
            locomotiveModelStoreInMemory.save(db, locomotiveModel);
        }

        for (LocomotiveModel locomotiveModel : locomotiveModelStoreInMemory.findAll()) {
            UIUtils.addLog("Loaded Locomotive Model from db: " + locomotiveModel.getId(), LogType.INFO, RoleType.GLOBAL);
        }
    }

    /**
     * Load wagon type entities from persistent storage into the in-memory
     * store.
     */
    private void loadWagonTypes() {
        WagonTypeStore wagonTypeStore = new WagonTypeStore();
        WagonTypeStoreInMemory wagonTypeStoreInMemory = new WagonTypeStoreInMemory();
        wagonTypeStoreInMemory.clear();
        List<WagonType> wagonTypes = wagonTypeStore.findAll(db);

        for (WagonType wagonType : wagonTypes) {
            wagonTypeStoreInMemory.save(db, wagonType);
        }

        for (WagonType wagonType : wagonTypeStoreInMemory.findAll()) {
            UIUtils.addLog("Loaded Wagon Type from db: " + wagonType.getId(), LogType.INFO, RoleType.GLOBAL);
        }
    }

    /**
     * Load wagon model entities from persistent storage into the in-memory
     * store.
     */
    private void loadWagonModels() {
        WagonModelStore wagonModelStore = new WagonModelStore();
        WagonModelStoreInMemory wagonModelStoreInMemory = new WagonModelStoreInMemory();
        wagonModelStoreInMemory.clear();
        List<WagonModel> wagonModels = wagonModelStore.findAll(db);

        for (WagonModel wagonModel : wagonModels) {
            wagonModelStoreInMemory.save(db, wagonModel);
        }

        for (WagonModel wagonModel : wagonModelStoreInMemory.findAll()) {
            UIUtils.addLog("Loaded Wagon Model from db: " + wagonModel.getId(), LogType.INFO, RoleType.GLOBAL);
        }
    }

    /**
     * Load railway line segment entities from persistent storage into the
     * in-memory store.
     */
    private void loadRailwayLineSegments() {
        RailwayLineSegmentStore railwayLineSegmentStore = new RailwayLineSegmentStore();
        RailwayLineSegmentStoreInMemory railwayLineSegmentStoreInMemory = new RailwayLineSegmentStoreInMemory();
        railwayLineSegmentStoreInMemory.clear();
        List<RailwayLineSegment> railwayLineSegments = railwayLineSegmentStore.findAll(db);

        for (RailwayLineSegment railwayLineSegment : railwayLineSegments) {
            railwayLineSegmentStoreInMemory.save(db, railwayLineSegment);
        }

        for (RailwayLineSegment railwayLineSegment : railwayLineSegmentStoreInMemory.findAll()) {
           UIUtils.addLog("Loaded Railway Line Segment from db: " + railwayLineSegment.getId(), LogType.INFO, RoleType.GLOBAL);
        }
    }

    /**
     * Load railway line entities from persistent storage into the in-memory
     * store.
     */
    private void loadRailwayLines() {
        RailwayLineStore railwayLineStore = new RailwayLineStore();
        RailwayLineStoreInMemory railwayLineStoreInMemory = new RailwayLineStoreInMemory();
        railwayLineStoreInMemory.clear();
        List<RailwayLine> railwayLines = railwayLineStore.findAll(db);

        for (RailwayLine railwayLine : railwayLines) {
            railwayLineStoreInMemory.save(db, railwayLine);
        }

        for (RailwayLine railwayLine : railwayLineStoreInMemory.findAll()) {
            UIUtils.addLog("Loaded Railway Line from db: " + railwayLine.getId(), LogType.INFO, RoleType.GLOBAL);
        }
    }

    /**
     * Load siding entities from persistent storage into the in-memory store.
     */
    private void loadSidings() {
        SidingStore sidingStore = new SidingStore();
        SidingStoreInMemory sidingStoreInMemory = new SidingStoreInMemory();
        sidingStoreInMemory.clear();
        List<Siding> sidings = sidingStore.findAll(db);

        for (Siding siding : sidings) {
            sidingStoreInMemory.save(db, siding);
        }

        for (Siding siding : sidingStoreInMemory.findAll()) {
           UIUtils.addLog("Loaded Siding from db: " + siding.getId(), LogType.INFO, RoleType.GLOBAL);
        }
    }

    /**
     * Load locomotive entities from persistent storage into the in-memory
     * store.
     */
    private void loadLocomotives() {
        LocomotiveStore locomotiveStore = new LocomotiveStore();
        LocomotiveStoreInMemory locomotiveStoreInMemory = new LocomotiveStoreInMemory();
        locomotiveStoreInMemory.clear();
        List<Locomotive> locomotives = locomotiveStore.findAll(db);

        for (Locomotive locomotive : locomotives) {
            locomotiveStoreInMemory.save(db, locomotive);
        }

        for (Locomotive locomotive : locomotiveStoreInMemory.findAll()) {
            UIUtils.addLog("Loaded Locomotive from db: " + locomotive.getId(), LogType.INFO, RoleType.GLOBAL);
        }
    }

    private void loadTrains() {
        TrainStore trainStore = new TrainStore();
        TrainStoreInMemory trainStoreInMemory = new TrainStoreInMemory();
        trainStoreInMemory.clear();
        List<Train> trains = trainStore.findAll(db);

        for (Train train : trains) {
            for (Locomotive loco : train.getLocomotives()) {
                loco.getTrains().add(train);
            }
        }

        for (Train train : trains) {
            trainStoreInMemory.save(db, train);
        }

        for (Train train : trainStoreInMemory.findAll()) {
            UIUtils.addLog("Loaded Train from db: " + train.getId(), LogType.INFO, RoleType.GLOBAL);
        }
    }

    /**
     * Load wagon entities from persistent storage into the in-memory store.
     */
    private void loadWagons() {
        WagonStore wagonStore = new WagonStore();
        WagonStoreInMemory wagonStoreInMemory = new WagonStoreInMemory();
        wagonStoreInMemory.clear();
        List<Wagon> wagons = wagonStore.findAll(db);

        for (Wagon wagon : wagons) {
            wagonStoreInMemory.save(db, wagon);
        }

        for (Wagon wagon : wagonStoreInMemory.findAll()) {
            UIUtils.addLog("Loaded Wagon from db: " + wagon.getWagonID(), LogType.INFO, RoleType.GLOBAL);
        }
    }

    /**
     * Load path entities from persistent storage into the in-memory store.
     */
    private void loadPaths() {
        PathStore pathStore = new PathStore();
        PathStoreInMemory pathStoreInMemory = new PathStoreInMemory();
        pathStoreInMemory.clear();
        List<Path> paths = pathStore.findAll(db);

        for (Path path : paths) {
            pathStoreInMemory.save(db, path);
        }

        for (Path path : pathStoreInMemory.findAll()) {
            UIUtils.addLog("Loaded Path from db: " + path.getId(), LogType.INFO, RoleType.GLOBAL);
        }

        for (Route route : new RouteStoreInMemory().findAll()) {
            if (route.getPath() == null) {
                continue;
            }
            route.setPath(pathStoreInMemory.findById(db,route.getPath().getId() + ""));
        }
    }

    /**
     * Load route entities from persistent storage into the in-memory store.
     */
    private void loadRoutes() {
        RouteStore routeStore = new RouteStore();
        RouteStoreInMemory routeStoreInMemory = new RouteStoreInMemory();
        routeStoreInMemory.clear();
        List<Route> routes = routeStore.findAll(db);

        for (Route route : routes) {
            routeStoreInMemory.save(db, route);
        }

        for (Route route : routeStoreInMemory.findAll()) {
            UIUtils.addLog("Loaded Route from db: " + route.getId(), LogType.INFO, RoleType.GLOBAL);
        }
    }

    /**
     * Load freight entities from persistent storage into the in-memory store.
     */
    private void loadFreights() {
        FreightStore freightStore = new FreightStore();
        FreightStoreInMemory freightStoreInMemory = new FreightStoreInMemory();
        freightStoreInMemory.clear();
        List<Freight> freights = freightStore.findAll(db);

        for (Freight freight : freights) {
            freightStoreInMemory.save(db, freight);
        }

        for (Freight freight : freightStoreInMemory.findAll()) {
            UIUtils.addLog("Loaded Freight from db: " + freight.getId(), LogType.INFO, RoleType.GLOBAL);
        }
    }

    private void loadGeneralSchedules() {
        GeneralScheduleStore generalScheduleStore = new GeneralScheduleStore();
        GeneralScheduleStoreInMemory generalScheduleStoreInMemory = new GeneralScheduleStoreInMemory();
        generalScheduleStoreInMemory.clear();
        List<GeneralSchedule> generalSchedules = generalScheduleStore.findAll(db);

        for (GeneralSchedule generalSchedule : generalSchedules) {
            generalScheduleStoreInMemory.save(db, generalSchedule);
        }

        for (GeneralSchedule generalSchedule : generalScheduleStoreInMemory.findAll()) {
            UIUtils.addLog("Loaded General Schedule from db: " + generalSchedule.getId(), LogType.INFO, RoleType.GLOBAL);
        }
    }

    private void loadScheduleEvents() {
        for (ScheduleEvent scheduleEvent : new ScheduleEventStore().findAll(db)) {
            UIUtils.addLog("Loaded Schedule Event from db: " + scheduleEvent.getId(), LogType.INFO, RoleType.GLOBAL);
        }
    }
}
