package pt.ipp.isep.dei.controller.wareHousePlanner;

import pt.ipp.isep.dei.domain.BoxRelated.Box;
import pt.ipp.isep.dei.domain.OrderRelated.AllocatedInfo;
import pt.ipp.isep.dei.domain.OrderRelated.OrderLine;
import pt.ipp.isep.dei.data.repository.sprint1.BayRepository;
import pt.ipp.isep.dei.data.repository.Repositories;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsible for displaying box allocations associated with a specific order line
 * in the Warehouse Planner module.
 * <p>
 * Provides methods to retrieve boxes, their aisle locations, and the quantity
 * of items allocated per box.
 */
public class ViewBoxAllocationController {

    private OrderLine orderLine;
    private BayRepository bayRepository;

    /**
     * Default constructor that initializes repository instances.
     */
    public ViewBoxAllocationController() {
        initRepos();
    }

    /**
     * Initializes repository references.
     */
    private void initRepos() {
        bayRepository = Repositories.getInstance().getBayRepository();
    }

    /**
     * Sets the order line whose box allocations will be displayed.
     *
     * @param orderLine the {@link OrderLine} to associate.
     */
    public void setData(OrderLine orderLine) {
        this.orderLine = orderLine;
    }

    /**
     * Retrieves all boxes associated with the current order line’s allocations.
     *
     * @return a list of {@link Box} objects allocated to the order line.
     */
    public List<Box> getBoxes() {
        List<Box> boxesToShow = new ArrayList<>();
        if (orderLine != null) {
            for (String boxId : orderLine.getAllocatedInfoList().keySet()) {
                Box box = orderLine.getPossibleBox().get(boxId);
                if (box != null) {
                    boxesToShow.add(box);
                }
            }
        }
        return boxesToShow;
    }

    /**
     * Retrieves the aisle ID where a given bay is located.
     *
     * @param bayId the bay identifier.
     * @return the associated aisle ID.
     */
    public String getAisleID(String bayId) {
        return bayRepository.findByKey(bayId).getAisleId();
    }

    /**
     * Returns the total number of items allocated to a specific box within the order line.
     *
     * @param box the {@link Box} to inspect.
     * @return the number of items allocated as a string.
     */
    public String getItemQtyInSameBox(Box box) {
        int counter = 0;
        for (AllocatedInfo info : orderLine.getAllocatedInfoList().values()) {
            if (info.getBoxID().equals(box.getBoxId())) {
                counter = info.getAllocatedQuantity();
            }
        }
        return String.valueOf(counter);
    }
}
