package pt.ipp.isep.dei;

import pt.ipp.isep.dei.controller.global.*;
import pt.ipp.isep.dei.controller.graphviz.GraphvizCreateFiles;
import pt.ipp.isep.dei.data.config.ActualDatabaseConnection;
import pt.ipp.isep.dei.data.config.DatabaseConnection;
import pt.ipp.isep.dei.data.config.DatabasePrinter;
import pt.ipp.isep.dei.data.memory.*;
import pt.ipp.isep.dei.data.repository.*;
import pt.ipp.isep.dei.data.repository.sprint1.TrolleyModelRepository;
import pt.ipp.isep.dei.domain.*;
import pt.ipp.isep.dei.domain.schedule.ScheduleGenerator;
import pt.ipp.isep.dei.domain.trackRelated.RailwayLine;
import pt.ipp.isep.dei.domain.trackRelated.RailwayLineSegment;
import pt.ipp.isep.dei.domain.trainRelated.Train;
import pt.ipp.isep.dei.domain.transportationRelated.Freight;
import pt.ipp.isep.dei.domain.transportationRelated.Path;
import pt.ipp.isep.dei.domain.transportationRelated.Route;
import pt.ipp.isep.dei.domain.trolleyRelated.TrolleyModel;
import pt.ipp.isep.dei.domain.wagonRelated.Wagon;
import pt.ipp.isep.dei.domain.wagonRelated.WagonModel;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for bootstrapping the system when the application starts.
 * It loads initial data such as users, trolley models, and other CSV-defined information.
 */
public class Bootstrap implements Runnable {

    private UserRepository userRepository;
    private TrolleyModelRepository trolleyModelRepository;

    private DatabaseConnection db = ActualDatabaseConnection.getDb();

    private static boolean bootstrapLoaded = false;

    /**
     * Main entry point for the bootstrap process.
     * Initializes repositories, loads predefined users, trolley models, and other CSV-based data.
     */
    @Override
    public void run() {
        try {
            initializeRepos();
            loadUsers();
            loadTrolleyModels();

            // new LoadSprint1DataSetController().run();

            // new LoadSprint2DataSetController().run();

            new LoadSprint3DataSetController().run();

            new GraphvizCreateFiles().create("output/ESINF/Global Graph/", "globalGraph",
                    Repositories.getInstance().getNodeEsinfRepository().getAllNodes(),
                    Repositories.getInstance().getEdgeEsinfRepository().getAllEdges());

            new LoadDataBaseController().run();

            //DatabasePrinter.printAll();

            // TESTE ITEMS
            // createMockItems();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isBootstrapLoaded() {
        return bootstrapLoaded;
    }

    public static void setBootstrapLoaded(boolean loaded) {
        bootstrapLoaded = loaded;
    }

    public void generateDefaultSchedules() {
        ScheduleGenerator generator = new ScheduleGenerator(new ArrayList<>(new TrainStoreInMemory().findAll()));
        new GeneralScheduleStoreInMemory().save(ActualDatabaseConnection.getDb(),generator.generateNewSchedule());
    }

    private void createMockItems() {
        createWagons();
        createStation();
        createFreights();
        createRoute();
        createRailwaySystem();

    }

    private void createRailwaySystem() {

        RailwayLineStoreInMemory railwayLineStoreInMemory = new RailwayLineStoreInMemory();
        RailwayLineSegmentStoreInMemory railwayLineSegmentStoreInMemory =
                new RailwayLineSegmentStoreInMemory();

        // ===== SEGMENTS =====
        RailwayLineSegment s1 = new RailwayLineSegment(
                1, true, 10_000, 5_000, 1, 120, 1
        );

        RailwayLineSegment s2 = new RailwayLineSegment(
                2, true, 10_000, 7_000, 1, 100, 1
        );

        RailwayLineSegment s3 = new RailwayLineSegment(
                3, false, 8_000, 6_000, 1, 80, 1
        );

        RailwayLineSegment s4 = new RailwayLineSegment(
                4, true, 12_000, 9_000, 2, 140, 1
        );

        List.of(s1, s2, s3, s4)
                .forEach(seg -> railwayLineSegmentStoreInMemory.save(db, seg));

        // ===== RAILWAY LINES =====

        // Linha 51 -> 52
        RailwayLine lineA = new RailwayLine(
                1, "Linha A (51-52)", 51, 52, "PT500000000"
        );
        lineA.addSegment(s1, 1);

        // Linha 52 -> 53
        RailwayLine lineB = new RailwayLine(
                2, "Linha B (52-53)", 52, 53, "PT500000000"
        );
        lineB.addSegment(s2, 1);
        lineB.addSegment(s3, 2);

        // Linha 53 -> 54
        RailwayLine lineC = new RailwayLine(
                3, "Linha C (53-54)", 53, 54, "PT500000000"
        );
        lineC.addSegment(s3, 1);
        lineC.addSegment(s4, 2);

        // Linha direta 51 -> 54 (mais longa, alternativa)
        RailwayLine lineD = new RailwayLine(
                4, "Linha D (51-54 Direta)", 51, 54, "PT600000000"
        );
        lineD.addSegment(s1, 1);
        lineD.addSegment(s4, 2);

        List.of(lineA, lineB, lineC, lineD)
                .forEach(line -> railwayLineStoreInMemory.save(db, line));

        // ===== ASSOCIAR SEGMENTOS ÀS LINHAS =====
        s1.addRailwayLine(lineA);
        s1.addRailwayLine(lineD);

        s2.addRailwayLine(lineA);
        s2.addRailwayLine(lineB);

        s3.addRailwayLine(lineB);
        s3.addRailwayLine(lineC);

        s4.addRailwayLine(lineC);
        s4.addRailwayLine(lineD);
    }

    private void createRoute() {
        RouteStoreInMemory routeStoreInMemory = new RouteStoreInMemory();
        FreightStoreInMemory freightStoreInMemory = new FreightStoreInMemory();

        // Freights já existentes no store
        Freight f1 = freightStoreInMemory.findById(db, "1");
        Freight f2 = freightStoreInMemory.findById(db, "2");
        Freight f3 = freightStoreInMemory.findById(db, "3");
        Freight f4 = freightStoreInMemory.findById(db, "4");
        Freight f5 = freightStoreInMemory.findById(db, "5");

        List.of(
                // SIMPLE route (1 freight)
                new Route(List.of(f1)),
                new Route(List.of(f5)),

                // COMPLEX routes (vários freights)
                new Route(List.of(f1, f2)),
                new Route(List.of(f3, f4)),
                new Route(List.of(f1, f3, f4))
        ).forEach(route -> routeStoreInMemory.save(db, route));
    }


    private void createStation() {
        FacilityStoreInMemory facilityStoreInMemory = new FacilityStoreInMemory();

        List.of(
                new Facility(51, "Teste1"),
                new Facility(52, "Teste2"),
                new Facility(53, "Teste3"),
                new Facility(54, "Teste4")
        ).forEach(facility -> facilityStoreInMemory.save(db, facility));

    }


    private void createFreights() {
        FacilityStoreInMemory facilityStoreInMemory = new FacilityStoreInMemory();
        WagonStoreInMemory wagonStoreInMemory = new WagonStoreInMemory();
        FreightStoreInMemory freightStoreInMemory = new FreightStoreInMemory();

        Facility f1 = facilityStoreInMemory.findById(db, "51");
        Facility f2 = facilityStoreInMemory.findById(db, "52");
        Facility f3 = facilityStoreInMemory.findById(db, "53");
        Facility f4 = facilityStoreInMemory.findById(db, "54");

        List<Wagon> wagons1 = List.of(
                wagonStoreInMemory.findById(db, "999"),
                wagonStoreInMemory.findById(db, "1000")
        );

        List<Wagon> wagons2 = List.of(
                wagonStoreInMemory.findById(db, "1001"),
                wagonStoreInMemory.findById(db, "1002")
        );

        List<Wagon> wagons3 = List.of(
                wagonStoreInMemory.findById(db, "1003"),
                wagonStoreInMemory.findById(db, "1004"),
                wagonStoreInMemory.findById(db, "1005")
        );

        Date d1 = new Date(25, 12, 2025); // 25/12/2025
        Date d2 = new Date(10, 11, 2025); // 10/11/2025
        Date d3 = new Date(5, 1, 2025);   // 05/01/2025
        Date d4 = new Date(18, 3, 2025);  // 18/03/2025

        List.of(
                new Freight(1, wagons1, f1, f2, d1),
                new Freight(2, wagons2, f2, f3, d2),
                new Freight(3, wagons3, f3, f4, d3),
                new Freight(4, wagons1, f4, f1, d4),
                new Freight(5, wagons2, f2, f1, d1)
        ).forEach(freight -> freightStoreInMemory.save(db, freight));
    }

    private void createWagons() {
        WagonStoreInMemory wagonStoreInMemory = new WagonStoreInMemory();
        WagonModelStoreInMemory wagonModelStoreInMemory = new WagonModelStoreInMemory();

        WagonModel model = new WagonModel(
                1, "Wagon-Cargo-A", 3200.5, 45.8, 8, 90, 3, 7, 2
        );

        wagonModelStoreInMemory.save(db, model);

        /*
        List.of(
                new Wagon(999, model, "OPERATOR1", 1999),
                new Wagon(1000, model, "OPERATOR1", 1999),
                new Wagon(1001, model, "OPERATOR1", 2000),
                new Wagon(1002, model, "OPERATOR2", 2001),
                new Wagon(1003, model, "OPERATOR2", 2002),
                new Wagon(1004, model, "OPERATOR3", 2003),
                new Wagon(1005, model, "OPERATOR3", 2004)
        ).forEach(w -> wagonStoreInMemory.save(db, w));

         */
    }




    /**
     * Initializes all repositories required by the bootstrap process.
     * This method retrieves repository instances from the global Repositories singleton.
     */
    private void initializeRepos() {
        userRepository = Repositories.getInstance().getUserRepository();
        trolleyModelRepository = Repositories.getInstance().getTrolleyModelRepository();
    }

    /**
     * Loads user data from the CSV file located at {@code data/userData.csv}.
     * Each line represents a user with their email, password, and assigned roles.
     * The first line of the file (header) is ignored.
     * <p>
     * Expected CSV structure:
     * <pre>
     * ID, Email, Password, Admin, Picker, Planner, QualityOperator, TerminalOperator, TrafficDispatcher, WarehousePlanner, DataEngineer
     * </pre>
     * <p>
     * Only rows with all required fields are processed.
     * Errors in reading or parsing are logged through {@link UIUtils#addLog(String, LogType, RoleType)}.
     */
    public void loadUsers() {
        String USER_CSV_PATH = "data/userData.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(USER_CSV_PATH))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] parts = line.split(",");

                if (parts.length >= 10) {
                    int id = Integer.parseInt(parts[0].trim());
                    String email = parts[1].trim();
                    String password = parts[2].trim();

                    ArrayList<String> roles = new ArrayList<>();
                    if ("1".equals(parts[3].trim())) roles.add("Admin");
                    if ("1".equals(parts[4].trim())) roles.add("Analyst");
                    if ("1".equals(parts[5].trim())) roles.add("Data Engineer");
                    if ("1".equals(parts[6].trim())) roles.add("Freight Manager");
                    if ("1".equals(parts[7].trim())) roles.add("Infrastructure Planner");
                    if ("1".equals(parts[8].trim())) roles.add("Maintenance Planner");
                    if ("1".equals(parts[9].trim())) roles.add("Operations Analyst");
                    if ("1".equals(parts[10].trim())) roles.add("Operations Planner");
                    if ("1".equals(parts[11].trim())) roles.add("Picker");
                    if ("1".equals(parts[12].trim())) roles.add("Planner");
                    if ("1".equals(parts[13].trim())) roles.add("Quality Operator");
                    if ("1".equals(parts[14].trim())) roles.add("Route Planner");
                    if ("1".equals(parts[15].trim())) roles.add("Terminal Operator");
                    if ("1".equals(parts[16].trim())) roles.add("Traffic Dispatcher");
                    if ("1".equals(parts[17].trim())) roles.add("Traffic Manager");
                    if ("1".equals(parts[18].trim())) roles.add("Warehouse Planner");

                    User user = new User(id, email, password);
                    user.setRoleList(roles);

                    userRepository.add(user);
                }
            }

        } catch (IOException e) {
            UIUtils.addLog("Error reading user data: " + e.getMessage(), LogType.ERROR, RoleType.GLOBAL);
        }
    }

    /**
     * Loads predefined trolley models into the trolley model repository.
     * These models represent the available trolley configurations in the system.
     */
    private void loadTrolleyModels() {
        trolleyModelRepository.add(new TrolleyModel("VICTP150", 150));
        trolleyModelRepository.add(new TrolleyModel("MMT20025426", 300));
    }
}
