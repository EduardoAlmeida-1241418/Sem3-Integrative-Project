package pt.ipp.isep.dei.controller.global;

import pt.ipp.isep.dei.data.repository.*;
import pt.ipp.isep.dei.data.repository.sprint1.*;
import pt.ipp.isep.dei.domain.*;
import pt.ipp.isep.dei.domain.BoxRelated.Box;
import pt.ipp.isep.dei.domain.OrderRelated.Order;
import pt.ipp.isep.dei.domain.OrderRelated.OrderLine;
import pt.ipp.isep.dei.domain.trackRelated.RailwayLine;
import pt.ipp.isep.dei.domain.trackRelated.RailwayLineSegment;
import pt.ipp.isep.dei.domain.trackRelated.TrackGauge;
import pt.ipp.isep.dei.domain.wagonRelated.Wagon;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsible for loading CSV datasets into the application's
 * in-memory repositories.
 *
 * <p>This {@code Runnable} locates CSV files in the project dataset folders
 * and populates the various domain repositories (warehouse and railway
 * related). Only Javadoc comments have been added; no code or existing
 * comments were modified.
 */
public class LoadSprint1DataSetController implements Runnable {

    private final String DATA_SET_WAREHOUSE_RELATED_DIRECTORY_PATH = "dataset/sprint1/train_station_dataset/";
    private final String DATA_SET_RAILWAY_NETWORK_DIRECTORY_PATH = "dataset/sprint1/Dataset_Sprint_1_v0/";

    private AisleRepository aisleRepository;
    private BayRepository bayRepository;
    private BoxRepository boxRepository;
    private ItemInfoRepository itemInfoRepository;
    private OrderLineRepository orderLineRepository;
    private OrderRepository orderRepository;
    private ReturnRepository returnRepository;
    private WagonRepository wagonRepository;
    private WarehouseRepository warehouseRepository;

    private FacilityRepository facilityRepository;
    private OperatorRepository operatorRepository;
    private OwnerRepository ownerRepository;
    private RailwayLineRepository railwayLineRepository;
    private RailwayLineSegmentRepository railwayLineSegmentRepository;
    private LocomotiveRepository locomotiveRepository;
    private LocomotiveModelRepository locomotiveModelRepository;

    /**
     * Run the CSV loading process.
     *
     * <p>This method initialises repository references and
     * invokes the CSV loading routine. The loading call is currently
     * commented out in the source;
     */
    @Override
    public void run() {
        Repositories repositories = Repositories.getInstance();

        aisleRepository = repositories.getAisleRepository();
        bayRepository = repositories.getBayRepository();
        boxRepository = repositories.getBoxRepository();
        itemInfoRepository = repositories.getItemInfoRepository();
        orderLineRepository = repositories.getOrderLineRepository();
        orderRepository = repositories.getOrderRepository();
        returnRepository = repositories.getReturnRepository();
        wagonRepository = repositories.getWagonRepository();
        warehouseRepository = repositories.getWarehouseRepository();

        facilityRepository = repositories.getFacilityRepository();
        operatorRepository = repositories.getOperatorRepository();
        ownerRepository = repositories.getOwnerRepository();
        railwayLineRepository = repositories.getRailwayLineRepository();
        railwayLineSegmentRepository = repositories.getRailwayLineSegmentRepository();
        locomotiveRepository = repositories.getLocomotiveRepository();
        locomotiveModelRepository = repositories.getLocomotiveModelRepository();

        UIUtils.addLog("------------------------------ Starting CSV data sprint 1 loading... ------------------------------", LogType.INFO, RoleType.GLOBAL);
        loadDataCSVs();
    }

    private void loadDataCSVs() {
        Path pasta1 = Paths.get(DATA_SET_WAREHOUSE_RELATED_DIRECTORY_PATH.substring(0, DATA_SET_WAREHOUSE_RELATED_DIRECTORY_PATH.length() - 1));
        Path pasta2 = Paths.get(DATA_SET_RAILWAY_NETWORK_DIRECTORY_PATH.substring(0, DATA_SET_RAILWAY_NETWORK_DIRECTORY_PATH.length() - 1));

        try (DirectoryStream<Path> stream1 = Files.newDirectoryStream(pasta1);
             DirectoryStream<Path> stream2 = Files.newDirectoryStream(pasta2)) {
            List<Path> csvFiles = new ArrayList<>();
            for (Path pathFile : stream1) {
                if (pathFile.getFileName().toString().endsWith(".csv")) {
                    csvFiles.add(pathFile);
                }
            }
            for (Path pathFile : stream2) {
                if (pathFile.getFileName().toString().endsWith(".csv")) {
                    csvFiles.add(pathFile);
                }
            }

            loadFilesInOrder(csvFiles, "bay");
            loadFilesInOrder(csvFiles, "item");
            loadFilesInOrder(csvFiles, "order");
            loadFilesInOrder(csvFiles, "line");
            loadFilesInOrder(csvFiles, "wagon");
            loadFilesInOrder(csvFiles, "return");
            loadFilesInOrder(csvFiles, "Facility");
            loadFilesInOrder(csvFiles, "Operator");
            loadFilesInOrder(csvFiles, "Owner");
            loadFilesInOrder(csvFiles, "Line");
            loadFilesInOrder(csvFiles, "Segment");
            loadFilesInOrder(csvFiles, "Locomotive");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFilesInOrder(List<Path> files, String keyword) {
        for (Path file : files) {
            String fileName = file.getFileName().toString();
            if (fileName.contains(keyword)) {
                switch (keyword) {
                    case "bay" -> loadBays(fileName);
                    case "item" -> loadItems(fileName);
                    case "order" -> loadOrders(fileName);
                    case "line" -> loadOrderLines(fileName);
                    case "wagon" -> loadWagonsAndBoxes(fileName);
                    case "return" -> loadReturns(fileName);
                    case "Facility" -> loadFacilities(fileName);
                    case "Operator" -> loadOperators(fileName);
                    case "Owner" -> loadOwners(fileName);
                    case "Line" -> loadRailwayLines(fileName);
                    case "Segment" -> loadRailwayLineSegments(fileName);
                    case "Locomotive" -> loadLocomotives(fileName);
                }
            }
        }
    }

    private void loadBays(String filePath) {
        List<String[]> baysList = UIUtils.readCSV(DATA_SET_WAREHOUSE_RELATED_DIRECTORY_PATH + filePath);

        for (String[] line : baysList) {
            if (line.length != 4) {
                UIUtils.addLog("Skipping invalid line (wrong number of fields): " + String.join(",", line), LogType.WARNING, RoleType.GLOBAL);
                continue;
            }

            String warehouseID = line[0];
            if (!warehouseID.matches("W\\d+")) {
                UIUtils.addLog("Invalid warehouse ID format: " + warehouseID, LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            int aisleNum, bayNum, capacityBoxes;
            try {
                aisleNum = Integer.parseInt(line[1]);
                bayNum = Integer.parseInt(line[2]);
                capacityBoxes = Integer.parseInt(line[3]);

                if (aisleNum <= 0 || bayNum <= 0 || capacityBoxes <= 0) {
                    UIUtils.addLog("Invalid numeric value in bay record: " + String.join(",", line), LogType.ERROR, RoleType.GLOBAL);
                    continue;
                }
            } catch (NumberFormatException e) {
                UIUtils.addLog("Numeric parsing error in bay record: " + String.join(",", line), LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            String aisleId = warehouseID + "A" + aisleNum;
            Bay bay = new Bay(aisleId, bayNum, capacityBoxes);

            bayRepository.add(bay);
            UIUtils.addLog("Added bay: " + bay.getBayId() + " with capacity " + capacityBoxes, LogType.INFO, RoleType.GLOBAL);

            Warehouse warehouse;
            if (!warehouseRepository.existsByKey(warehouseID)) {
                warehouse = new Warehouse(warehouseID);
                warehouseRepository.add(warehouse);
                UIUtils.addLog("Created new warehouse: " + warehouseID, LogType.INFO, RoleType.GLOBAL);
            } else {
                warehouse = warehouseRepository.findByKey(warehouseID);
            }

            addBayToAisle(bay.getBayId(), aisleId, warehouse);
        }
    }

    private void addBayToAisle(String bayId, String aisleId, Warehouse warehouse) {
        Aisle aisle;
        if (!aisleRepository.existsByKey(aisleId)) {
            aisle = new Aisle(aisleId, warehouse.getWarehouseID());
            aisleRepository.add(aisle);
            UIUtils.addLog("Created new aisle: " + aisleId + " in warehouse " + warehouse.getWarehouseID(), LogType.INFO, RoleType.GLOBAL);
            warehouse.addAisle(aisleId);
        } else {
            aisle = aisleRepository.findByKey(aisleId);
        }
        aisle.addBay(bayId);
    }


    private void loadItems(String filePath) {
        List<String[]> itemsList = UIUtils.readCSV(DATA_SET_WAREHOUSE_RELATED_DIRECTORY_PATH + filePath);
        for (String[] line : itemsList) {
            if (line.length != 6) {
                continue;
            }

            String sku = line[0];
            if (sku == null || sku.isEmpty()) {
                UIUtils.addLog("SKU is empty in item record: " + String.join(",", line), LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            String name = line[1];
            if (name == null || name.isEmpty()) {
                UIUtils.addLog("Item name is empty in item record: " + String.join(",", line), LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            String category = line[2].toUpperCase();
            try {
                CategoryItem.valueOf(category);
            } catch (IllegalArgumentException e) {
                UIUtils.addLog("Invalid category in item record: " + String.join(",", line), LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            String unitType = line[3];
            try {
                UnitType.valueOf(unitType.toUpperCase());
            } catch (IllegalArgumentException e) {
                UIUtils.addLog("Invalid unit type in item record: " + String.join(",", line), LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            double volume, unitWeight;
            try {
                volume = Double.parseDouble(line[4]);
                unitWeight = Double.parseDouble(line[5]);
                if (volume <= 0 || unitWeight <= 0) {
                    UIUtils.addLog("Invalid numeric value in item record: " + String.join(",", line), LogType.ERROR, RoleType.GLOBAL);
                    continue;
                }
            } catch (NumberFormatException e) {
                UIUtils.addLog("Numeric parsing error in item record: " + String.join(",", line), LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            itemInfoRepository.add(new Item(sku, name, CategoryItem.valueOf(category), UnitType.valueOf(unitType.toUpperCase()), volume, unitWeight));
            UIUtils.addLog("Added item: " + sku + " - " + name, LogType.INFO, RoleType.GLOBAL);
        }
    }

    private void loadOrderLines(String filePath) {
        List<String[]> orderLineList = UIUtils.readCSV(DATA_SET_WAREHOUSE_RELATED_DIRECTORY_PATH + filePath);
        for (String[] line : orderLineList) {
            if (line.length != 4) {
                continue;
            }

            String orderId = line[0];
            String skuItem = line[2];

            if (!itemInfoRepository.existsBySku(skuItem)) {
                UIUtils.addLog("Unknown SKU in order line: " + skuItem + " in order " + orderId, LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            int lineNumber, quantity;
            try {
                lineNumber = Integer.parseInt(line[1]);
                quantity = Integer.parseInt(line[3]);
                if (lineNumber <= 0 || quantity <= 0) {
                    UIUtils.addLog("Invalid numeric value in order line: " + String.join(",", line), LogType.ERROR, RoleType.GLOBAL);
                    continue;
                }
            } catch (NumberFormatException e) {
                UIUtils.addLog("Numeric parsing error in order line: " + String.join(",", line), LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            OrderLine orderLine = new OrderLine(orderId, lineNumber, skuItem, quantity);
            if (orderLineRepository.existsById(orderLine.getOrderLineId())) {
                UIUtils.addLog("Duplicate order line ID: " + orderLine.getOrderLineId() + " in line " + String.join(",", line), LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            orderLineRepository.add(orderLine);
            UIUtils.addLog("Added order line: " + orderLine.getOrderLineId(), LogType.INFO, RoleType.GLOBAL);

            if (orderRepository.existsById(orderId)) {
                Order order = orderRepository.findById(orderId);
                order.addOrderLine(orderLine.getOrderLineId());
            } else
                UIUtils.addLog("Order ID not found for order line: " + orderId + " in line " + String.join(",", line), LogType.ERROR, RoleType.GLOBAL);
        }
    }

    private void loadOrders(String filePath) {
        List<String[]> ordersList = UIUtils.readCSV(DATA_SET_WAREHOUSE_RELATED_DIRECTORY_PATH + filePath);
        for (String[] line : ordersList) {
            if (line.length != 3) {
                continue;
            }

            String orderId = line[0];
            int priority;
            Date dueDate;
            Time dueTime;
            try {
                String[] dateTime = line[1].split("T");
                String[] dateParts = dateTime[0].split("-");
                String[] timeParts = dateTime[1].split(":");

                dueDate = new Date(Integer.parseInt(dateParts[2]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[0]));
                dueTime = new Time(Integer.parseInt(timeParts[0]), Integer.parseInt(timeParts[1]), Integer.parseInt(timeParts[2]));

                priority = Integer.parseInt(line[2]);
                if (priority <= 0) {
                    UIUtils.addLog("Invalid priority in order: " + String.join(",", line), LogType.ERROR, RoleType.GLOBAL);
                    continue;
                }

            } catch (Exception e) {
                UIUtils.addLog("Invalid date/time/priority in order: " + String.join(",", line), LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            orderRepository.add(new Order(orderId, dueDate, dueTime, priority));
            UIUtils.addLog("Added order: " + orderId, LogType.INFO, RoleType.GLOBAL);
        }
    }

    private void loadWagonsAndBoxes(String filePath) {
        List<String[]> wagonsList = UIUtils.readCSV(DATA_SET_WAREHOUSE_RELATED_DIRECTORY_PATH + filePath);
        for (String[] line : wagonsList) {
            if (line.length != 6) {
                UIUtils.addLog("Invalid record length in wagon file: " + String.join(",", line), LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            String wagonID = line[0].trim();
            String boxID = line[1].trim();
            String skuItem = line[2].trim();

            if (wagonID.isEmpty() || boxID.isEmpty() || skuItem.isEmpty()) {
                UIUtils.addLog("Missing mandatory field in wagon record: " + String.join(",", line), LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            if (!itemInfoRepository.existsBySku(skuItem)) {
                UIUtils.addLog("Unknown SKU '" + skuItem + "' in wagon '" + wagonID + "'", LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            if (boxRepository.existsById(boxID)) {
                UIUtils.addLog("Duplicate box ID '" + boxID + "' in wagon '" + wagonID + "'", LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            int quantity;
            try {
                quantity = Integer.parseInt(line[3].trim());
                if (quantity <= 0) {
                    UIUtils.addLog("Invalid quantity in box '" + boxID + "' of wagon '" + wagonID + "': " + quantity, LogType.ERROR, RoleType.GLOBAL);
                    continue;
                }
            } catch (NumberFormatException e) {
                UIUtils.addLog("Quantity parsing error in box '" + boxID + "' of wagon '" + wagonID + "'", LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            Date expDate = null;
            if (!line[4].trim().isEmpty()) {
                try {
                    String[] expiryDate = line[4].trim().split("-");
                    expDate = new Date(Integer.parseInt(expiryDate[2]), Integer.parseInt(expiryDate[1]), Integer.parseInt(expiryDate[0]));
                } catch (Exception e) {
                    UIUtils.addLog("Invalid expiry date in box '" + boxID + "' of wagon '" + wagonID + "': " + line[4], LogType.ERROR, RoleType.GLOBAL);
                    continue;
                }
            }

            if (line[5] == null || line[5].trim().isEmpty()) {
                UIUtils.addLog("Missing receivedAt timestamp in box '" + boxID + "' of wagon '" + wagonID + "'", LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            Date recDate;
            Time recTime;
            try {
                String[] receivedAt = line[5].trim().split("T");
                String[] receivedDate = receivedAt[0].split("-");
                String[] receivedTime = receivedAt[1].split(":");
                recDate = new Date(Integer.parseInt(receivedDate[2]), Integer.parseInt(receivedDate[1]), Integer.parseInt(receivedDate[0]));
                recTime = new Time(Integer.parseInt(receivedTime[0]), Integer.parseInt(receivedTime[1]), Integer.parseInt(receivedTime[2]));
            } catch (Exception e) {
                UIUtils.addLog("Invalid receivedAt timestamp in box '" + boxID + "' of wagon '" + wagonID + "': " + line[5], LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            Box box = new Box(boxID, skuItem, quantity, expDate, recDate, recTime);
            boxRepository.add(box);
            UIUtils.addLog("Added box '" + boxID + "' to wagon '" + wagonID + "'", LogType.INFO, RoleType.GLOBAL);
            int idWagon = Integer.parseInt(wagonID.replace("WGN", ""));

            if (wagonRepository.existsById(idWagon)) {
                Wagon existingWagon = wagonRepository.findById(idWagon);
                existingWagon.addBox(boxID);
            } else {
                Wagon wagon = new Wagon(idWagon);
                wagon.addBox(boxID);
                wagonRepository.add(wagon);
                UIUtils.addLog("Added wagon '" + wagonID + "'", LogType.INFO, RoleType.GLOBAL);
            }
        }

        for (Wagon wagon : wagonRepository.findAll()) {
            if (wagon.getBoxIds().isEmpty()) {
                UIUtils.addLog("Wagon '" + wagon.getWagonID() + "' has no valid boxes linked", LogType.WARNING, RoleType.GLOBAL);
            }
        }
    }

    private void loadReturns(String filePath) {
        List<String[]> returnsList = UIUtils.readCSV(DATA_SET_WAREHOUSE_RELATED_DIRECTORY_PATH + filePath);
        for (String[] line : returnsList) {
            if (line.length != 6) {
                continue;
            }

            String returnId = line[0];
            String skuItem = line[1];

            if (!itemInfoRepository.existsBySku(skuItem)) {
                UIUtils.addLog("Unknown SKU in return: " + skuItem + " in return " + returnId, LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            int quantity;
            try {
                quantity = Integer.parseInt(line[2]);
                if (quantity <= 0) {
                    UIUtils.addLog("Invalid quantity in return: " + String.join(",", line), LogType.ERROR, RoleType.GLOBAL);
                    continue;
                }
            } catch (NumberFormatException e) {
                UIUtils.addLog("Quantity parsing error in return: " + String.join(",", line), LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            ReturnReason reason;
            try {
                reason = ReturnReason.valueOf(line[3].replace("-", "_").toUpperCase());
            } catch (IllegalArgumentException e) {
                UIUtils.addLog("Invalid return reason: " + String.join(",", line), LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            Date expDate = null;
            if (!line[5].isEmpty()) {
                try {
                    String[] expiryDate = line[5].split("-");
                    expDate = new Date(Integer.parseInt(expiryDate[2]), Integer.parseInt(expiryDate[1]), Integer.parseInt(expiryDate[0]));
                } catch (Exception e) {
                    UIUtils.addLog("Invalid expiry date in return: " + String.join(",", line), LogType.ERROR, RoleType.GLOBAL);
                    continue;
                }
            }

            Date retDate;
            Time retTime;
            try {
                String[] timeStamp = line[4].split("T");
                String[] returnDate = timeStamp[0].split("-");
                String[] returnTime = timeStamp[1].split(":");

                retDate = new Date(Integer.parseInt(returnDate[2]), Integer.parseInt(returnDate[1]), Integer.parseInt(returnDate[0]));
                retTime = new Time(Integer.parseInt(returnTime[0]), Integer.parseInt(returnTime[1]), Integer.parseInt(returnTime[2]));
            } catch (Exception e) {
                UIUtils.addLog("Invalid timestamp in return: " + String.join(",", line), LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            returnRepository.add(new Return(returnId, skuItem, quantity, reason, retDate, retTime, expDate));
            UIUtils.addLog("Added return: " + returnId, LogType.INFO, RoleType.GLOBAL);
        }
    }

    private void loadFacilities(String filePath) {
        List<String[]> facilitiesList = UIUtils.readCSV(DATA_SET_RAILWAY_NETWORK_DIRECTORY_PATH + filePath);
        for (String[] line : facilitiesList) {
            if (line.length != 2) {
                continue;
            }

            int facilityID = Integer.parseInt(line[0]);

            if (facilityID <= 0) {
                UIUtils.addLog("Invalid facility ID: " + facilityID, LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            String facilityName = line[1];

            if (facilityRepository.existsById(facilityID)) {
                UIUtils.addLog("Duplicate facility ID: " + facilityID, LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            facilityRepository.add(new Facility(facilityID, facilityName, false, FacilityType.STATION));
            UIUtils.addLog("Added facility: " + facilityID + " - " + facilityName, LogType.INFO, RoleType.GLOBAL);
        }
    }

    private void loadOperators(String filePath) {
        List<String[]> operatorsList = UIUtils.readCSV(DATA_SET_RAILWAY_NETWORK_DIRECTORY_PATH + filePath);
        for (String[] line : operatorsList) {

            if (line.length != 3 && line.length != 4) {
                continue;
            }

            String name;
            String shortName;
            String vatNumber;

            if (line.length == 4) {
                name = (line[0] + "," + line[1]).replace("\"", "");
                shortName = line[2];
                vatNumber = line[3];
            } else {
                name = line[0].replace("\"", "");
                shortName = line[1];
                vatNumber = line[2];
            }

            if (operatorRepository.existsById(vatNumber)) {
                UIUtils.addLog("Duplicate operator VAT number: " + vatNumber, LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            operatorRepository.add(new Operator(vatNumber, name, shortName));
            UIUtils.addLog("Added operator: " + name + " - " + vatNumber, LogType.INFO, RoleType.GLOBAL);
        }
    }

    private void loadOwners(String filePath) {
        List<String[]> ownersList = UIUtils.readCSV(DATA_SET_RAILWAY_NETWORK_DIRECTORY_PATH + filePath);
        for (String[] line : ownersList) {
            if (line.length != 3 && line.length != 4) {
                continue;
            }

            String name;
            String shortName;
            String vatNumber;

            if (line.length == 4) {
                name = (line[0] + "," + line[1]).replace("\"", "");
                shortName = line[2];
                vatNumber = line[3];
            } else {
                name = line[0].replace("\"", "");
                shortName = line[1];
                vatNumber = line[2];
            }

            if (ownerRepository.existsById(vatNumber)) {
                UIUtils.addLog("Duplicate owner VAT number: " + vatNumber, LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            ownerRepository.add(new Owner(name, shortName, vatNumber));
            UIUtils.addLog("Added owner: " + name + " - " + vatNumber, LogType.INFO, RoleType.GLOBAL);
        }
    }

    private void loadRailwayLines(String filePath) {
        List<String[]> railwayLinesList = UIUtils.readCSV(DATA_SET_RAILWAY_NETWORK_DIRECTORY_PATH + filePath);
        for (String[] line : railwayLinesList) {
            if (line.length != 8) {
                continue;
            }

            int id = Integer.parseInt(line[0]);
            String name = line[1];
            String ownerName = line[2];
            int startId = Integer.parseInt(line[3]);
            int endId = Integer.parseInt(line[5]);
            int gauge = Integer.parseInt(line[7]);

            if (railwayLineRepository.existsById(id)) {
                UIUtils.addLog("Duplicate railway line ID: " + id, LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            if (!ownerRepository.existsByShortName(ownerName)) {
                UIUtils.addLog("Unknown ownerName VAT number: " + ownerName + " for railway line ID: " + id, LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            if (!facilityRepository.existsById(startId)) {
                UIUtils.addLog("Unknown start facility ID: " + startId + " for railway line ID: " + id, LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            if (!facilityRepository.existsById(endId)) {
                UIUtils.addLog("Unknown end facility ID: " + endId + " for railway line ID: " + id, LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            if (gauge <= 0) {
                UIUtils.addLog("Invalid gauge value for railway line ID: " + id, LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            railwayLineRepository.add(new RailwayLine(id, name, startId, endId, ownerRepository.getOwnerVatNumberByShortName(ownerName)));
            UIUtils.addLog("Added railway line: " + id + " - " + name, LogType.INFO, RoleType.GLOBAL);
        }
    }

    private void loadRailwayLineSegments(String filePath) {
        List<String[]> railwayLineSegmentsList = UIUtils.readCSV(DATA_SET_RAILWAY_NETWORK_DIRECTORY_PATH + filePath);
        for (String[] line : railwayLineSegmentsList) {
            if (line.length != 9) {
                continue;
            }

            int id = Integer.parseInt(line[0]);
            if (id <= 0) {
                UIUtils.addLog("Invalid railway line segment ID: " + id, LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            int railwayLineId = Integer.parseInt(line[1]);
            if (railwayLineId <= 0) {
                UIUtils.addLog("Invalid railway line ID: " + railwayLineId + " for railway line segment ID: " + id, LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            int orderPosition = Integer.parseInt(line[2]);
            String isElectrified = line[3];
            int maxWeight = Integer.parseInt(line[4]);
            int length = Integer.parseInt(line[5]);
            int numberTracks = Integer.parseInt(line[6]);
            String hasSiding = line[7];
            int speedLimit = Integer.parseInt(line[8]);

            if (railwayLineSegmentRepository.existsById(id)) {
                UIUtils.addLog("Duplicate railway line segment ID: " + id, LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            if (!railwayLineRepository.existsById(railwayLineId)) {
                UIUtils.addLog("Unknown railway line ID: " + railwayLineId + " for railway line segment ID: " + id, LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            if (!isElectrified.equalsIgnoreCase("yes") && !isElectrified.equalsIgnoreCase("no")) {
                UIUtils.addLog("Invalid electrification status for railway line segment ID: " + id, LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            if (orderPosition <= 0 || maxWeight <= 0 || length <= 0 || numberTracks <= 0 || speedLimit <= 0) {
                UIUtils.addLog("Invalid numeric value for railway line segment ID: " + id, LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            if (!hasSiding.equalsIgnoreCase("yes") && !hasSiding.equalsIgnoreCase("no")) {
                UIUtils.addLog("Invalid siding status for railway line segment ID: " + id, LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            boolean electrified = isElectrified.equalsIgnoreCase("yes");

            RailwayLineSegment segment = new RailwayLineSegment(id, electrified, maxWeight, length, numberTracks, speedLimit, 0);
            segment.addRailwayLine(railwayLineRepository.findById(railwayLineId));

            railwayLineSegmentRepository.add(segment);
            UIUtils.addLog("Added railway line segment: " + id + " to railway line ID: " + railwayLineId, LogType.INFO, RoleType.GLOBAL);
        }
    }

    private void loadLocomotives(String filePath) {
        try {
            List<String[]> locomotivesList = UIUtils.readCSV(DATA_SET_RAILWAY_NETWORK_DIRECTORY_PATH + filePath);
            for (String[] line : locomotivesList) {
                if (line.length != 21) {
                    continue;
                }

                int number = Integer.parseInt(line[0]);
                if (number <= 0) {
                    UIUtils.addLog("Invalid locomotive number: " + number, LogType.ERROR, RoleType.GLOBAL);
                    continue;
                }
                String name = line[1];
                String make = line[2];
                String model = line[3];
                int startYearService = Integer.parseInt(line[4]);
                int numberBogies = Integer.parseInt(line[5]);
                String bogieTypeStr = line[6];
                int power = Integer.parseInt(line[7]);
                double length = Double.parseDouble(line[8]);
                double width = Double.parseDouble(line[9]);
                double height = Double.parseDouble(line[10]);
                int weight_t = Integer.parseInt(line[11]);
                int maxSpeed = Integer.parseInt(line[12]);
                double operationalSpeed = Double.parseDouble(line[13]);
                int traction_KN = Integer.parseInt(line[14]);
                String locomotiveTypeStr = line[15];
                int voltage_KV = 0;
                if (!line[16].isEmpty()) {
                    voltage_KV = Integer.parseInt(line[16].replace("KV", "").trim());
                    if (voltage_KV < 0) {
                        UIUtils.addLog("Invalid voltage value for locomotive number: " + number, LogType.ERROR, RoleType.GLOBAL);
                        continue;
                    }
                }
                int frequency_Hz = 0;
                if (!line[17].isEmpty()) {
                    frequency_Hz = Integer.parseInt(line[17].replace("Hz", "").trim());
                    if (frequency_Hz < 0) {
                        UIUtils.addLog("Invalid frequency value for locomotive number: " + number, LogType.ERROR, RoleType.GLOBAL);
                        continue;
                    }
                }
                String operatorShortName = line[18];
                int trackGauge = Integer.parseInt(line[19]);
                double fuel_liters = 0;
                if (!line[20].isEmpty()) {
                    fuel_liters = Double.parseDouble(line[20]);
                    if (fuel_liters < 0) {
                        UIUtils.addLog("Invalid fuel liters value for locomotive number: " + number, LogType.ERROR, RoleType.GLOBAL);
                        continue;
                    }
                }

                if (locomotiveRepository.existsById(number)) {
                    UIUtils.addLog("Duplicate locomotive number: " + number, LogType.ERROR, RoleType.GLOBAL);
                    continue;
                }

                if (make.isEmpty() || model.isEmpty()) {
                    UIUtils.addLog("Make or model is empty for locomotive number: " + number, LogType.ERROR, RoleType.GLOBAL);
                    continue;
                }

                if (startYearService <= 0 || numberBogies <= 0 || power <= 0 || length <= 0 || width <= 0 || height <= 0 || weight_t <= 0 ||
                        maxSpeed <= 0 || operationalSpeed <= 0 || traction_KN <= 0 || trackGauge <= 0) {
                    UIUtils.addLog("Invalid numeric value for locomotive number: " + number, LogType.ERROR, RoleType.GLOBAL);
                    continue;
                }

                if (bogieTypeStr.isEmpty() || locomotiveTypeStr.isEmpty()) {
                    UIUtils.addLog("Bogie type or locomotive type is empty for locomotive number: " + number, LogType.ERROR, RoleType.GLOBAL);
                    continue;
                }

                if (operatorShortName.isEmpty()) {
                    UIUtils.addLog("Operator ID is empty for locomotive number: " + number, LogType.ERROR, RoleType.GLOBAL);
                    continue;
                }

                if (!operatorRepository.existOperatorWithShortName(operatorShortName)) {
                    UIUtils.addLog("Unknown operator ID: " + operatorShortName + " for locomotive number: " + number, LogType.ERROR, RoleType.GLOBAL);
                    continue;
                }

                MakeLocomotiveType makeType;
                LocomotiveType locomotiveType;

                if (make.contains("Siemens")) {
                    makeType = MakeLocomotiveType.SIEMENS;
                } else if (make.contains("Sorefame") || make.contains("Alsthom")) {
                    makeType = MakeLocomotiveType.SOREFAME_ALSTHOM;
                } else {
                    continue;
                }

                if (locomotiveTypeStr.contains("Electric")) {
                    locomotiveType = LocomotiveType.ELECTRIC;
                } else if (locomotiveTypeStr.contains("Diesel")) {
                    locomotiveType = LocomotiveType.DIESEL;
                } else {
                    continue;
                }

                int modelId;

                if (locomotiveModelRepository.existsByName(model)) {
                    LocomotiveModel existing = locomotiveModelRepository.findByName(model);
                    modelId = existing.getId();
                } else {
                    int newId = locomotiveModelRepository.getNextId();
                    int makerId = makeType.ordinal() + 1;
                    LocomotiveModel newModel = new LocomotiveModel(
                            newId,
                            model,
                            power,
                            weight_t,
                            0.0,
                            numberBogies * 2,
                            maxSpeed,
                            operationalSpeed,
                            traction_KN,
                            new Maker(makerId, make),
                            new Dimensions(
                                    0,
                                    length,
                                    width,
                                    height,
                                    weight_t
                            ),
                            locomotiveType == LocomotiveType.DIESEL ? FuelType.DIESEL : FuelType.ELECTRIC,
                            List.of(new TrackGauge(0, trackGauge)),
                            (int) fuel_liters,
                            voltage_KV,
                            frequency_Hz
                    );
                    locomotiveModelRepository.add(newModel);
                    UIUtils.addLog("Added locomotive model: " + model + " with ID: " + newId, LogType.INFO, RoleType.GLOBAL);
                    modelId = newId;
                }

                Locomotive locomotive = new Locomotive(
                        number,
                        name,
                        startYearService,
                        locomotiveModelRepository.findById(modelId),
                        operatorRepository.getOperatorByShortName(operatorShortName)
                );

                locomotiveRepository.add(locomotive);
                UIUtils.addLog("Added locomotive: " + number + " - " + name, LogType.INFO, RoleType.GLOBAL);

            }
        } catch (Exception e) {
            System.out.println("Error loading locomotives: " + e.getMessage());
        }
    }
}