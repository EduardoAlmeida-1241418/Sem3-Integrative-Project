package pt.ipp.isep.dei.controller.planner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pt.ipp.isep.dei.comparator.WeightDecreasingComparator;
import pt.ipp.isep.dei.data.repository.*;
import pt.ipp.isep.dei.data.repository.sprint1.*;
import pt.ipp.isep.dei.domain.BoxRelated.Box;
import pt.ipp.isep.dei.domain.Log;
import pt.ipp.isep.dei.domain.LogType;
import pt.ipp.isep.dei.domain.OrderRelated.*;
import pt.ipp.isep.dei.domain.RoleType;
import pt.ipp.isep.dei.domain.pickingPlanRelated.PickingPlan;
import pt.ipp.isep.dei.domain.pickingPlanRelated.heuristicRelated.PickingHeuristicType;
import pt.ipp.isep.dei.domain.pickingPlanRelated.spacePolicyRelated.SpacePolicyType;
import pt.ipp.isep.dei.domain.trolleyRelated.Trolley;
import pt.ipp.isep.dei.domain.trolleyRelated.TrolleyAllocation;
import pt.ipp.isep.dei.domain.trolleyRelated.TrolleyModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsible for generating and managing picking plans.
 * <p>
 * Implements the following heuristics:
 * <ul>
 *   <li>First Fit</li>
 *   <li>First Fit Decreasing</li>
 *   <li>Best Fit Decreasing</li>
 * </ul>
 * and the following space policies:
 * <ul>
 *   <li>Split</li>
 *   <li>Defer</li>
 *   <li>Automatic</li>
 * </ul>
 * It allows generating optimized picking plans, assigning trolleys,
 * and logging each operation.
 */
public class PickingPlansController {

    private OrderLineRepository orderLineRepository;
    private ItemInfoRepository itemInfoRepository;
    private TrolleyModelRepository trolleyModelRepository;
    private OrderRepository orderRepository;
    private BoxRepository boxRepository;
    private PickingPlansRepository pickingPlansRepository;
    private LogRepository logRepository;

    /**
     * Initializes all repositories required for generating picking plans.
     */
    public PickingPlansController() {
        this.orderLineRepository = Repositories.getInstance().getOrderLineRepository();
        this.itemInfoRepository = Repositories.getInstance().getItemInfoRepository();
        this.trolleyModelRepository = Repositories.getInstance().getTrolleyModelRepository();
        this.orderRepository = Repositories.getInstance().getOrderRepository();
        this.boxRepository = Repositories.getInstance().getBoxRepository();
        this.pickingPlansRepository = Repositories.getInstance().getPickingPlansRepository();
        this.logRepository = Repositories.getInstance().getLogRepository();
    }

    /**
     * Returns all order lines that are dispatchable and have completed allocation.
     *
     * @return observable list of valid order lines.
     */
    public ObservableList<OrderLine> getOrderLines() {
        List<OrderLine> filtered = new ArrayList<>();

        for (OrderLine orderLine : orderLineRepository.findAll()) {
            LineState state = orderLine.getRealStatus().getState();
            if (!LineState.UNDISPATCHABLE.equals(state)
                    && orderLine.containsAllocatedOrderByState(AllocationStatusType.ALLOCATION_DONE)) {
                filtered.add(orderLine);
            }
        }

        return FXCollections.observableArrayList(filtered);
    }

    /**
     * Returns the total number of allocated boxes in a given order line.
     *
     * @param selectedLine the order line.
     * @return total number of allocated boxes.
     */
    public int getTotalAllocatedUnits(OrderLine selectedLine) {
        return selectedLine.getAllocatedBoxCount();
    }

    /**
     * Checks if all selected order lines can be deferred to a given trolley model.
     *
     * @param selectedItems the list of order lines selected by the user.
     * @param selectionModel the name of the selected trolley model.
     * @return true if all allocations fit within the trolley's max weight; false otherwise.
     */
    public boolean verifyDeferPossibility(ObservableList<OrderLine> selectedItems, String selectionModel) {
        TrolleyModel trolleyModel = trolleyModelRepository.findByName(selectionModel);

        for (OrderLine orderLine : selectedItems) {
            for (AllocatedInfo allocatedInfo : orderLine.getAllocatedInfoList().values()) {
                double itemWeight = itemInfoRepository
                        .findBySku(boxRepository.findById(allocatedInfo.getBoxID()).getSkuItem())
                        .getUnitWeight();
                double totalWeight = allocatedInfo.getAllocatedQuantity() * itemWeight;

                if (totalWeight > trolleyModel.getMaxWeight()) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Calculates the total allocated weight for a given order line.
     *
     * @param orderLine the order line.
     * @return total allocated weight in kilograms.
     */
    public double calculateTotalWeight(OrderLine orderLine) {
        double totalWeight = 0;
        double unitWeight = itemInfoRepository.findBySku(orderLine.getSkuItem()).getUnitWeight();

        for (AllocatedInfo info : orderLine.getAllocatedInfoByState(AllocationStatusType.ALLOCATION_DONE).values()) {
            totalWeight += info.getAllocatedQuantity() * unitWeight;
        }

        return totalWeight;
    }

    /**
     * Returns the list of available trolley models.
     *
     * @return observable list containing trolley model names.
     */
    public ObservableList<String> getTrolleyModels() {
        ObservableList<String> modelNames = FXCollections.observableArrayList();

        for (TrolleyModel model : trolleyModelRepository.findAll()) {
            modelNames.add(model.getName());
        }

        return modelNames;
    }

    /**
     * Retrieves the priority of an order based on its ID.
     *
     * @param orderId the ID of the order.
     * @return order priority as an integer.
     */
    public int getPriority(String orderId) {
        return orderRepository.findById(orderId).getPriority();
    }

    /**
     * Returns a list of all available picking heuristic names.
     *
     * @return observable list of heuristic names.
     */
    public ObservableList<String> getHeuristicNames() {
        ObservableList<String> list = FXCollections.observableArrayList();
        for (PickingHeuristicType type : PickingHeuristicType.values()) {
            list.add(type.getName());
        }
        return list;
    }

    /**
     * Returns a list of all available space policy names.
     *
     * @return observable list of space policy names.
     */
    public ObservableList<String> getSpacePolicyTypes() {
        ObservableList<String> list = FXCollections.observableArrayList();
        for (SpacePolicyType type : SpacePolicyType.values()) {
            list.add(type.getName());
        }
        return list;
    }

    /**
     * Generates a picking plan using the selected heuristic and space policy.
     *
     * @param orderLines list of order lines to include in the plan.
     * @param trolleyName selected trolley model name.
     * @param heuristicName selected heuristic name.
     * @param spacePolicyName selected space policy name.
     */
    public void generatePickingPlan(ObservableList<OrderLine> orderLines, String trolleyName, String heuristicName, String spacePolicyName) {
        PickingHeuristicType heuristicType = PickingHeuristicType.getByName(heuristicName);
        SpacePolicyType spacePolicyType = SpacePolicyType.getByName(spacePolicyName);
        PickingPlan pickingPlan;
        List<OrderLine> orderLineList = new ArrayList<>(orderLines);

        switch (heuristicType) {
            case FIRST_FIT:
                pickingPlan = firstFitGeneral(orderLineList, trolleyName, spacePolicyType);
                pickingPlansRepository.add(pickingPlan);
                break;
            case FIRST_FIT_DECREASING:
                pickingPlan = firstFitDecreasingGeneral(orderLineList, trolleyName, spacePolicyType);
                pickingPlansRepository.add(pickingPlan);
                break;
            case BEST_FIT_DECREASING:
                pickingPlan = bestFitDecreasingGeneral(orderLineList, trolleyName, spacePolicyType);
                pickingPlansRepository.add(pickingPlan);
                break;
            default:
                throw new IllegalArgumentException("Unknown heuristic type: " + heuristicName);
        }

        logRepository.add(new Log(pickingPlan.toString(), LogType.PICKING_PLAN, RoleType.PLANNER1));
    }

    // ######################
    // # Space Policy Logic #
    // ######################

    /**
     * Implements the automatic space policy.
     * Chooses between defer and split based on trolley capacity.
     */
    private void automaticLogic(PickingPlan pickingPlan, AllocatedInfo info, double itemWeight, OrderLine orderLine) {
        double totalWeight = info.getAllocatedQuantity() * itemWeight;

        if (pickingPlan.getTrolleyModel().getMaxWeight() >= totalWeight) {
            deferLogic(pickingPlan, info, itemWeight, orderLine);
            return;
        }

        splitLogic(pickingPlan, info, itemWeight, orderLine);
    }

    /**
     * Implements the defer policy.
     * Creates a new trolley to accommodate the allocation.
     */
    private void deferLogic(PickingPlan pickingPlan, AllocatedInfo info, double itemWeight, OrderLine orderLine) {
        Box box = boxRepository.findById(info.getBoxID());
        pickingPlan.addTrolley();
        Trolley trolley = pickingPlan.getTrolleys().getLast();
        trolley.addTrolleyAllocation(new TrolleyAllocation(box, info.getAllocatedQuantity(), orderLine, itemWeight));
    }

    /**
     * Implements the split policy.
     * Distributes allocations across existing or new trolleys.
     */
    private void splitLogic(PickingPlan pickingPlan, AllocatedInfo info, double itemWeight, OrderLine orderLine) {
        Box box = boxRepository.findById(info.getBoxID());
        int quantityToAllocate = info.getAllocatedQuantity();
        int availableQty;

        for (Trolley trolley : pickingPlan.getTrolleys()) {
            availableQty = (int) Math.floor(trolley.getAvailableWeight() / itemWeight);

            if (availableQty > 0) {
                int qty = Math.min(quantityToAllocate, availableQty);
                trolley.addTrolleyAllocation(new TrolleyAllocation(box, qty, orderLine, itemWeight));
                quantityToAllocate -= qty;
            }

            if (quantityToAllocate <= 0) return;
        }

        TrolleyModel model = pickingPlan.getTrolleyModel();

        while (quantityToAllocate > 0) {
            availableQty = (int) Math.floor(model.getMaxWeight() / itemWeight);
            int qty = Math.min(quantityToAllocate, availableQty);
            deferLogic(pickingPlan, new AllocatedInfo(info.getBoxID(), qty), itemWeight, orderLine);
            quantityToAllocate -= qty;
        }
    }

    /**
     * Applies the correct space policy method according to the policy type.
     */
    private void SplitPolicyMethod(AllocatedInfo info, PickingPlan pickingPlan, OrderLine orderLine, SpacePolicyType spacePolicyType, double itemWeight, int quantityToAllocate) {
        AllocatedInfo remainingInfo = new AllocatedInfo(info.getBoxID(), quantityToAllocate);
        switch (spacePolicyType) {
            case AUTOMATIC:
                automaticLogic(pickingPlan, remainingInfo, itemWeight, orderLine);
                break;
            case SPLIT:
                splitLogic(pickingPlan, remainingInfo, itemWeight, orderLine);
                break;
            case DEFER:
                deferLogic(pickingPlan, remainingInfo, itemWeight, orderLine);
                break;
        }
    }

    // #######################
    // ## Heuristic Related ##
    // #######################

    /**
     * Implements the First Fit heuristic.
     */
    private PickingPlan firstFitGeneral(List<OrderLine> orderLines, String trolleyName, SpacePolicyType spacePolicyType) {
        PickingPlan pickingPlan = new PickingPlan(trolleyModelRepository.findByName(trolleyName));

        for (OrderLine orderLine : orderLines) {
            for (AllocatedInfo info : orderLine.getAllocatedInfoByState(AllocationStatusType.ALLOCATION_DONE).values()) {
                firstFitLogic(pickingPlan, info, spacePolicyType, orderLine);
                info.setAllocationState(AllocationStatusType.PICKING_PLAN_DONE);
            }
        }

        return pickingPlan;
    }

    /**
     * Internal logic of the First Fit heuristic.
     */
    private boolean firstFitLogic(PickingPlan pickingPlan, AllocatedInfo info, SpacePolicyType spacePolicyType, OrderLine orderLine) {
        Box box = boxRepository.findById(info.getBoxID());
        double itemWeight = itemInfoRepository.findBySku(box.getSkuItem()).getUnitWeight();
        int quantityToAllocate = info.getAllocatedQuantity();

        for (Trolley trolley : pickingPlan.getTrolleys()) {
            int quantityThatFits = (int) Math.floor(trolley.getAvailableWeight() / itemWeight);
            if (quantityThatFits > 0) {
                int qty = Math.min(quantityToAllocate, quantityThatFits);
                trolley.addTrolleyAllocation(new TrolleyAllocation(box, qty, orderLine, itemWeight));
                quantityToAllocate -= qty;
            }
            if (quantityToAllocate <= 0) return true;
        }

        if (quantityToAllocate > 0) {
            SplitPolicyMethod(info, pickingPlan, orderLine, spacePolicyType, itemWeight, quantityToAllocate);
        }

        return true;
    }

    /**
     * Implements the First Fit Decreasing heuristic.
     */
    private PickingPlan firstFitDecreasingGeneral(List<OrderLine> orderLines, String trolleyName, SpacePolicyType spacePolicyType) {
        List<OrderLine> orderLineListSorted = new ArrayList<>(orderLines);
        orderLineListSorted.sort(new WeightDecreasingComparator(itemInfoRepository, boxRepository));
        PickingPlan pickingPlan = firstFitGeneral(orderLineListSorted, trolleyName, spacePolicyType);
        return pickingPlan;
    }

    /**
     * Implements the Best Fit Decreasing heuristic.
     */
    private PickingPlan bestFitDecreasingGeneral(List<OrderLine> orderLines, String trolleyName, SpacePolicyType spacePolicyType) {
        List<OrderLine> orderLineListSorted = new ArrayList<>(orderLines);
        orderLineListSorted.sort(new WeightDecreasingComparator(itemInfoRepository, boxRepository));
        PickingPlan pickingPlan = new PickingPlan(trolleyModelRepository.findByName(trolleyName));

        for (OrderLine orderLine : orderLineListSorted) {
            for (AllocatedInfo info : orderLine.getAllocatedInfoByState(AllocationStatusType.ALLOCATION_DONE).values()) {
                bestFitDecreasingLogic(info, pickingPlan, orderLine, spacePolicyType);
                info.setAllocationState(AllocationStatusType.PICKING_PLAN_DONE);
            }
        }

        return pickingPlan;
    }

    /**
     * Internal logic for the Best Fit Decreasing heuristic.
     */
    private void bestFitDecreasingLogic(AllocatedInfo info, PickingPlan pickingPlan, OrderLine orderLine, SpacePolicyType spacePolicyType) {
        Box box = boxRepository.findById(info.getBoxID());
        double itemWeight = itemInfoRepository.findBySku(box.getSkuItem()).getUnitWeight();
        int quantityToAllocate = info.getAllocatedQuantity();

        int bestFitIndex = findBestFitTrolleyIndex(itemWeight, quantityToAllocate, pickingPlan);

        if (bestFitIndex == -1) {
            SplitPolicyMethod(info, pickingPlan, orderLine, spacePolicyType, itemWeight, quantityToAllocate);
            return;
        }

        pickingPlan.getTrolleys().get(bestFitIndex).addTrolleyAllocation(new TrolleyAllocation(box, quantityToAllocate, orderLine, itemWeight));
    }

    /**
     * Finds the trolley index that best fits the given allocation.
     *
     * @param itemWeight weight of one unit of the item.
     * @param quantityToAllocate quantity to allocate.
     * @param pickingPlan the current picking plan.
     * @return index of the best-fit trolley, or -1 if none fits.
     */
    private int findBestFitTrolleyIndex(double itemWeight, int quantityToAllocate, PickingPlan pickingPlan) {
        int bestFitIndex = -1;
        int actualIndex = 0;
        double bestFitWeight = 99999;
        double actualFitWeight;

        double totalItemWeight = itemWeight * quantityToAllocate;

        for (Trolley trolley : pickingPlan.getTrolleys()) {
            if (trolley.getAvailableWeight() >= totalItemWeight) {
                actualFitWeight = trolley.getAvailableWeight() - totalItemWeight;
                if (actualFitWeight < bestFitWeight) {
                    bestFitIndex = actualIndex;
                    bestFitWeight = actualFitWeight;
                }
            }
            actualIndex++;
        }

        return bestFitIndex;
    }

    /**
     * Retrieves all logs related to generated picking plans.
     *
     * @return list of picking plan logs.
     */
    public List<Log> getLogs() {
        List<Log> logs = new ArrayList<>();
        for (Log log : logRepository.findAll()) {
            if (log.getLogType().equals(LogType.PICKING_PLAN)) {
                logs.add(log);
            }
        }
        return logs;
    }
}
