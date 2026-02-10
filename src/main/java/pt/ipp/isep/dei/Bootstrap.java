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

            new LoadSprint1DataSetController().run();

            new LoadSprint2DataSetController().run();

            new LoadSprint3DataSetController().run();

            new GraphvizCreateFiles().create("output/ESINF/Global Graph/", "globalGraph",
                    Repositories.getInstance().getNodeEsinfRepository().getAllNodes(),
                    Repositories.getInstance().getEdgeEsinfRepository().getAllEdges());

            new LoadDataBaseController().run();
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
