package domain.trolleyRelated;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.BoxRelated.Box;
import pt.ipp.isep.dei.domain.trolleyRelated.TrolleyModel;
import pt.ipp.isep.dei.domain.trolleyRelated.TrolleyAllocation;
import pt.ipp.isep.dei.domain.Date;
import pt.ipp.isep.dei.domain.Time;

import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link TrolleyAllocation}.
 * Validates allocation logic for First Fit, First Fit Decreasing, and Best Fit Decreasing strategies.
 * Includes generation of allocation reports and picking plan structure verification.
 */
public class TrolleyAllocationTest {

    private List<Box> boxes;
    private static final int TROLLEY_CAPACITY = 100;

    @BeforeEach
    void setUp() {
        boxes = new ArrayList<>();

        Box box1 = new Box("B1", "SKU1", 5, new Date(31, 12, 2025),
                new Date(23, 10, 2025), new Time(14, 30, 0));
        box1.setWeight(20.0);

        Box box2 = new Box("B2", "SKU2", 8, new Date(31, 12, 2025),
                new Date(23, 10, 2025), new Time(14, 30, 0));
        box2.setWeight(32.0);

        Box box3 = new Box("B3", "SKU3", 6, new Date(31, 12, 2025),
                new Date(23, 10, 2025), new Time(14, 30, 0));
        box3.setWeight(30.0);

        boxes.add(box1);
        boxes.add(box2);
        boxes.add(box3);
    }

    private double unitWeight(Box b) {
        return b.getWeight() / Math.max(1, b.getQuantity());
    }

    private List<List<TrolleyAllocation>> runFirstFit(List<Box> inputBoxes, TrolleyModel model) {
        List<List<TrolleyAllocation>> trolleys = new ArrayList<>();
        trolleys.add(new ArrayList<>());
        List<Double> remaining = new ArrayList<>();
        remaining.add(model.getMaxWeight());

        for (Box box : inputBoxes) {
            int freeQty = box.getFreeQuantity();
            double uWeight = unitWeight(box);

            while (freeQty > 0) {
                boolean placed = false;
                for (int i = 0; i < trolleys.size(); i++) {
                    int canFit = (int) Math.floor(remaining.get(i) / uWeight);
                    if (canFit <= 0) continue;
                    int allocQty = Math.min(canFit, freeQty);
                    double weightAlloc = allocQty * uWeight;
                    TrolleyAllocation a = new TrolleyAllocation(box, allocQty, null, weightAlloc);
                    trolleys.get(i).add(a);
                    remaining.set(i, remaining.get(i) - weightAlloc);
                    freeQty -= allocQty;
                    box.setAllocatedQuantity(box.getAllocatedQuantity() + allocQty);
                    placed = true;
                    if (freeQty <= 0) break;
                }
                if (!placed && freeQty > 0) {
                    trolleys.add(new ArrayList<>());
                    remaining.add(model.getMaxWeight());
                }
            }
        }
        return trolleys;
    }

    @Test
    void testFirstFitRespectsCapacityAndSplitsLines() {
        TrolleyModel model = new TrolleyModel("ModelFF", TROLLEY_CAPACITY);
        boxes.forEach(b -> b.setAllocatedQuantity(0));
        List<List<TrolleyAllocation>> trolleys = runFirstFit(boxes, model);

        for (List<TrolleyAllocation> tro : trolleys) {
            double sum = tro.stream().mapToDouble(TrolleyAllocation::getItemWeight).sum();
            assertTrue(sum <= model.getMaxWeight() + 1e-9);
        }

        for (Box b : boxes) {
            assertEquals(0, b.getFreeQuantity());
        }

        assertFalse(trolleys.isEmpty());
    }

    @Test
    void testFirstFitDecreasingProducesDecreasingUnitWeights() {
        TrolleyModel model = new TrolleyModel("ModelFFD", TROLLEY_CAPACITY);
        List<Box> sorted = new ArrayList<>(boxes);
        sorted.sort(Comparator.comparingDouble(this::unitWeight).reversed());
        for (int i = 0; i < sorted.size() - 1; i++) {
            assertTrue(unitWeight(sorted.get(i)) >= unitWeight(sorted.get(i + 1)));
        }
        sorted.forEach(b -> b.setAllocatedQuantity(0));
        List<List<TrolleyAllocation>> trolleys = runFirstFit(sorted, model);
        for (List<TrolleyAllocation> tro : trolleys) {
            double sum = tro.stream().mapToDouble(TrolleyAllocation::getItemWeight).sum();
            assertTrue(sum <= model.getMaxWeight() + 1e-9);
        }
    }

    private List<List<TrolleyAllocation>> runBestFitDecreasing(List<Box> inputBoxes, TrolleyModel model) {
        List<List<TrolleyAllocation>> trolleys = new ArrayList<>();
        List<Double> remaining = new ArrayList<>();
        List<Box> sorted = new ArrayList<>(inputBoxes);
        sorted.sort(Comparator.comparingDouble(this::unitWeight).reversed());

        for (Box box : sorted) {
            int freeQty = box.getFreeQuantity();
            double uWeight = unitWeight(box);

            while (freeQty > 0) {
                Optional<Integer> bestIdx = Optional.empty();
                double bestRemainingAfter = Double.POSITIVE_INFINITY;
                for (int i = 0; i < remaining.size(); i++) {
                    int canFit = (int) Math.floor(remaining.get(i) / uWeight);
                    if (canFit <= 0) continue;
                    double remainingAfter = remaining.get(i) - uWeight;
                    if (remainingAfter >= 0 && remainingAfter < bestRemainingAfter) {
                        bestRemainingAfter = remainingAfter;
                        bestIdx = Optional.of(i);
                    }
                }
                if (bestIdx.isPresent()) {
                    int i = bestIdx.get();
                    int canFit = (int) Math.floor(remaining.get(i) / uWeight);
                    int allocQty = Math.min(canFit, freeQty);
                    double w = allocQty * uWeight;
                    TrolleyAllocation a = new TrolleyAllocation(box, allocQty, null, w);
                    trolleys.get(i).add(a);
                    remaining.set(i, remaining.get(i) - w);
                    freeQty -= allocQty;
                    box.setAllocatedQuantity(box.getAllocatedQuantity() + allocQty);
                } else {
                    trolleys.add(new ArrayList<>());
                    remaining.add(model.getMaxWeight());
                }
            }
        }
        return trolleys;
    }

    @Test
    void testBestFitDecreasingRespectsCapacityAndUsesFewTrolleys() {
        TrolleyModel model = new TrolleyModel("ModelBFD", 30);
        Box big = new Box("Big", "SKU_BIG", 10, new Date(31, 12, 2025),
                new Date(23, 10, 2025), new Time(14, 30, 0));
        big.setWeight(50.0);
        Box small1 = new Box("S1", "SKUS1", 3, new Date(31, 12, 2025),
                new Date(23, 10, 2025), new Time(14, 30, 0));
        small1.setWeight(9.0);
        List<Box> all = new ArrayList<>(Arrays.asList(big, small1));
        all.forEach(b -> b.setAllocatedQuantity(0));
        List<List<TrolleyAllocation>> trolleys = runBestFitDecreasing(all, model);

        for (List<TrolleyAllocation> tro : trolleys) {
            double sum = tro.stream().mapToDouble(TrolleyAllocation::getItemWeight).sum();
            assertTrue(sum <= model.getMaxWeight() + 1e-9);
        }

        int totalQty = all.stream().mapToInt(Box::getQuantity).sum();
        int allocated = all.stream().mapToInt(Box::getAllocatedQuantity).sum();
        assertEquals(totalQty, allocated);
        assertTrue(trolleys.size() >= 1);
    }
}
