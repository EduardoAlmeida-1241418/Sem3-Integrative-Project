package pt.ipp.isep.dei.comparator;

import pt.ipp.isep.dei.domain.Aisle;
import pt.ipp.isep.dei.domain.Bay;
import pt.ipp.isep.dei.domain.trolleyRelated.TrolleyAllocation;
import pt.ipp.isep.dei.data.repository.sprint1.AisleRepository;
import pt.ipp.isep.dei.data.repository.sprint1.BayRepository;
import pt.ipp.isep.dei.data.repository.Repositories;

import java.util.Comparator;

/**
 * Comparator for {@link TrolleyAllocation} using Strategy A.
 * <p>
 * Orders trolley allocations based on aisle number first, then bay number.
 * Retrieves aisle and bay information from their respective repositories.
 */
public class StrategyAComparator implements Comparator<TrolleyAllocation> {

    private final BayRepository bayRepository = Repositories.getInstance().getBayRepository();
    private final AisleRepository aisleRepository = Repositories.getInstance().getAisleRepository();

    /**
     * Compares two {@link TrolleyAllocation} instances based on their physical positions.
     * <p>
     * Comparison order:
     * <ol>
     *   <li>Aisle number (ascending)</li>
     *   <li>Bay number (ascending)</li>
     * </ol>
     *
     * @param o1 the first trolley allocation
     * @param o2 the second trolley allocation
     * @return a negative integer, zero, or a positive integer if the first allocation
     *         is positioned before, at the same level, or after the second one
     */
    @Override
    public int compare(TrolleyAllocation o1, TrolleyAllocation o2) {
        Bay bay1 = bayRepository.findByKey(o1.getBox().getBayId());
        Bay bay2 = bayRepository.findByKey(o2.getBox().getBayId());

        Aisle aisle1 = aisleRepository.findByKey(bay1.getAisleId());
        Aisle aisle2 = aisleRepository.findByKey(bay2.getAisleId());

        int aisleComparison = Integer.compare(aisle1.getNumber(), aisle2.getNumber());
        if (aisleComparison != 0) {
            return aisleComparison;
        }

        return Integer.compare(bay1.getBay(), bay2.getBay());
    }
}
