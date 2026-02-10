package pt.ipp.isep.dei.controller.picker;

import pt.ipp.isep.dei.comparator.StrategyAComparator;
import pt.ipp.isep.dei.data.repository.*;
import pt.ipp.isep.dei.data.repository.sprint1.AisleRepository;
import pt.ipp.isep.dei.data.repository.sprint1.BayRepository;
import pt.ipp.isep.dei.data.repository.sprint1.PickingPlansRepository;
import pt.ipp.isep.dei.domain.*;
import pt.ipp.isep.dei.domain.pickingPath.PathPoint;
import pt.ipp.isep.dei.domain.pickingPath.PickingPath;
import pt.ipp.isep.dei.domain.pickingPath.PickingPathReport;
import pt.ipp.isep.dei.domain.pickingPath.PickingStrategyType;
import pt.ipp.isep.dei.domain.pickingPlanRelated.PickingPlan;
import pt.ipp.isep.dei.domain.trolleyRelated.Trolley;
import pt.ipp.isep.dei.domain.trolleyRelated.TrolleyAllocation;

import java.util.LinkedList;
import java.util.List;

/**
 * Controller responsible for generating picking paths for trolleys in picking plans.
 * <p>
 * Implements two strategies (A and B) for path optimization and logs results.
 */
public class PickingPathController {

    private PickingPlansRepository pickingPlansRepository;
    private AisleRepository aisleRepository;
    private BayRepository bayRepository;
    private LogRepository logRepository;

    /**
     * Default constructor that initializes all required repositories.
     */
    public PickingPathController() {
        initRepos();
    }

    /**
     * Initializes repository instances from {@link Repositories}.
     */
    private void initRepos() {
        pickingPlansRepository = Repositories.getInstance().getPickingPlansRepository();
        aisleRepository = Repositories.getInstance().getAisleRepository();
        bayRepository = Repositories.getInstance().getBayRepository();
        logRepository = Repositories.getInstance().getLogRepository();
    }

    /**
     * Retrieves all picking plans from the repository.
     *
     * @return a list of all {@link PickingPlan} objects
     */
    public List<PickingPlan> getPickingPlanList() {
        return pickingPlansRepository.findAll();
    }

    // ################
    // # Picking Path #
    // ################

    // ##############
    // # Strategy A #
    // ##############

    /**
     * Generates a complete picking path report using Strategy A.
     * <p>
     * Each trolley's allocations are ordered using {@link StrategyAComparator}.
     *
     * @param pickingPlan the picking plan to process
     * @return a {@link PickingPathReport} with all paths generated for the plan
     */
    public PickingPathReport pickingPathReport_StratAGeneral(PickingPlan pickingPlan) {
        PickingPathReport pickingPathReport = new PickingPathReport(PickingStrategyType.STRATEGY_A);
        List<TrolleyAllocation> trolleyAllocations;

        for (Trolley trolley : pickingPlan.getTrolleys()) {
            trolleyAllocations = trolley.getTrolleyAllocations();
            trolleyAllocations.sort(new StrategyAComparator());
            pickingPathReport.addPickingPath(findPathForTrolley_StratA(trolley));
        }

        logRepository.add(new Log(pickingPathReport.toString(), LogType.PIKING_PATH, RoleType.PICKER));
        return pickingPathReport;
    }

    /**
     * Builds a picking path for a single trolley using Strategy A.
     * <p>
     * Sequentially processes each trolley allocation and merges duplicate path points.
     *
     * @param trolley the trolley to process
     * @return a {@link PickingPath} representing the optimized route
     */
    private PickingPath findPathForTrolley_StratA(Trolley trolley) {
        PickingPath pickingPath = new PickingPath(trolley);
        int currentAisle, currentBay;
        int previousAisle = 0, previousBay = 0;

        for (TrolleyAllocation trolleyAllocation : trolley.getTrolleyAllocations()) {
            currentAisle = aisleRepository.findByKey(bayRepository.findByKey(trolleyAllocation.getBox().getBayId()).getAisleId()).getNumber();
            currentBay = bayRepository.findByKey(trolleyAllocation.getBox().getBayId()).getBay();

            if (currentAisle == previousAisle && currentBay == previousBay) {
                pickingPath.getPathPointList().getLast().addBox(trolleyAllocation.getBox());
            } else {
                pickingPath.addPathPoint(new PathPoint(
                        trolleyAllocation.getBox(),
                        calculateDistance(previousAisle, previousBay, currentAisle, currentBay),
                        currentAisle,
                        currentBay));
            }

            previousAisle = currentAisle;
            previousBay = currentBay;
        }

        return pickingPath;
    }

    /**
     * Calculates the distance between two positions in the warehouse.
     *
     * @param previousAisle previous aisle number
     * @param previousBay previous bay number
     * @param currentAisle current aisle number
     * @param currentBay current bay number
     * @return the calculated distance as an integer
     */
    private static int calculateDistance(int previousAisle, int previousBay, int currentAisle, int currentBay) {
        if (previousAisle == currentAisle) {
            return Math.abs(previousBay - currentBay);
        } else {
            return previousBay + Math.abs(previousAisle - currentAisle) * 3 + currentBay;
        }
    }

    // ##############
    // # Strategy B #
    // ##############

    /**
     * Generates a complete picking path report using Strategy B.
     * <p>
     * Each trolley path is optimized by dynamically choosing the closest next allocation.
     *
     * @param pickingPlan the picking plan to process
     * @return a {@link PickingPathReport} containing all generated paths
     */
    public PickingPathReport pickingPathReport_StratBGeneral(PickingPlan pickingPlan) {
        PickingPathReport pickingPathReport = new PickingPathReport(PickingStrategyType.STRATEGY_B);

        for (Trolley trolley : pickingPlan.getTrolleys()) {
            pickingPathReport.addPickingPath(findPathForTrolley_StratB(trolley));
        }

        logRepository.add(new Log(pickingPathReport.toString(), LogType.PIKING_PATH, RoleType.PICKER));
        return pickingPathReport;
    }

    /**
     * Builds a picking path for a trolley using Strategy B (nearest next point).
     *
     * @param trolley the trolley to process
     * @return a {@link PickingPath} optimized by proximity
     */
    private PickingPath findPathForTrolley_StratB(Trolley trolley) {
        PickingPath pickingPath = new PickingPath(trolley);
        List<TrolleyAllocation> trolleyAllocations = new LinkedList<>(trolley.getTrolleyAllocations());
        int currentAisle, currentBay;
        int previousAisle = 0, previousBay = 0;

        while (!trolleyAllocations.isEmpty()) {
            TrolleyAllocation closestAllocation = findClosestAllocation(trolleyAllocations, previousAisle, previousBay);

            currentAisle = aisleRepository.findByKey(bayRepository.findByKey(closestAllocation.getBox().getBayId()).getAisleId()).getNumber();
            currentBay = bayRepository.findByKey(closestAllocation.getBox().getBayId()).getBay();

            if (currentAisle == previousAisle && currentBay == previousBay) {
                pickingPath.getPathPointList().getLast().addBox(closestAllocation.getBox());
            } else {
                pickingPath.addPathPoint(new PathPoint(
                        closestAllocation.getBox(),
                        calculateDistance(previousAisle, previousBay, currentAisle, currentBay),
                        currentAisle,
                        currentBay));
            }

            previousAisle = currentAisle;
            previousBay = currentBay;
            trolleyAllocations.remove(closestAllocation);
        }

        return pickingPath;
    }

    /**
     * Finds the closest {@link TrolleyAllocation} to a given position based on warehouse distance.
     *
     * @param trolleyAllocations list of remaining allocations
     * @param previousAisle the last aisle visited
     * @param previousBay the last bay visited
     * @return the closest trolley allocation to the previous position
     */
    private TrolleyAllocation findClosestAllocation(List<TrolleyAllocation> trolleyAllocations, int previousAisle, int previousBay) {
        TrolleyAllocation closestOne = null;
        int minDistance = Integer.MAX_VALUE;

        for (TrolleyAllocation trolleyAllocation : trolleyAllocations) {
            Bay bay = bayRepository.findByKey(trolleyAllocation.getBox().getBayId());
            Aisle aisle = aisleRepository.findByKey(bay.getAisleId());

            int currentAisle = aisle.getNumber();
            int currentBay = bay.getBay();

            int distance = calculateDistance(previousAisle, previousBay, currentAisle, currentBay);

            if (distance < minDistance) {
                minDistance = distance;
                closestOne = trolleyAllocation;
            }
        }

        return closestOne;
    }
}
