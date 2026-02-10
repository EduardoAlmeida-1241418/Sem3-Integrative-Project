package pt.ipp.isep.dei.comparator;

import pt.ipp.isep.dei.domain.OrderRelated.OrderLine;
import pt.ipp.isep.dei.data.repository.sprint1.ItemInfoRepository;
import pt.ipp.isep.dei.data.repository.sprint1.BoxRepository;
import pt.ipp.isep.dei.domain.OrderRelated.AllocatedInfo;

import java.util.Comparator;

/**
 * Comparator for {@link OrderLine} objects that sorts by total allocated weight in descending order.
 * <p>
 * The total weight is calculated from the quantities and unit weights of allocated items.
 */
public class WeightDecreasingComparator implements Comparator<OrderLine> {

    private final ItemInfoRepository itemRepo;
    private final BoxRepository boxRepo;

    /**
     * Constructs a comparator using the given repositories.
     *
     * @param itemRepo the repository for retrieving item information
     * @param boxRepo the repository for retrieving box information
     */
    public WeightDecreasingComparator(ItemInfoRepository itemRepo, BoxRepository boxRepo) {
        this.itemRepo = itemRepo;
        this.boxRepo = boxRepo;
    }

    /**
     * Compares two order lines by total allocated weight in descending order.
     *
     * @param o1 the first order line
     * @param o2 the second order line
     * @return a negative integer, zero, or a positive integer if the first order line
     *         has greater, equal, or smaller total weight than the second
     */
    @Override
    public int compare(OrderLine o1, OrderLine o2) {
        double w1 = calculateWeight(o1);
        double w2 = calculateWeight(o2);
        return Double.compare(w2, w1); // descending
    }

    /**
     * Calculates the total allocated weight of an order line.
     *
     * @param orderLine the order line to evaluate
     * @return the total weight based on allocated quantities and item unit weights
     */
    private double calculateWeight(OrderLine orderLine) {
        double total = 0;
        for (AllocatedInfo info : orderLine.getAllocatedInfoList().values()) {
            double unitWeight = itemRepo.findBySku(
                    boxRepo.findById(info.getBoxID()).getSkuItem()
            ).getUnitWeight();
            total += info.getAllocatedQuantity() * unitWeight;
        }
        return total;
    }
}
