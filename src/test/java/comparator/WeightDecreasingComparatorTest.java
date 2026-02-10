package comparator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.comparator.WeightDecreasingComparator;
import pt.ipp.isep.dei.domain.BoxRelated.Box;
import pt.ipp.isep.dei.domain.Date;
import pt.ipp.isep.dei.domain.Item;
import pt.ipp.isep.dei.domain.Time;
import pt.ipp.isep.dei.domain.OrderRelated.AllocatedInfo;
import pt.ipp.isep.dei.domain.OrderRelated.OrderLine;
import pt.ipp.isep.dei.data.repository.sprint1.BoxRepository;
import pt.ipp.isep.dei.data.repository.sprint1.ItemInfoRepository;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link WeightDecreasingComparator}.
 * Validates that {@link OrderLine} objects are correctly compared
 * based on their total allocated weight in descending order.
 */
class WeightDecreasingComparatorTest {

    /**
     * In-memory implementation of {@link ItemInfoRepository} for testing.
     */
    private static class InMemoryItemInfoRepository extends ItemInfoRepository {
        private final Map<String, Item> items = new HashMap<>();
        void save(Item item) { items.put(item.getSku(), item); }
        @Override
        public Item findBySku(String sku) { return items.get(sku); }
    }

    /**
     * In-memory implementation of {@link BoxRepository} for testing.
     */
    private static class InMemoryBoxRepository extends BoxRepository {
        private final Map<String, Box> boxes = new HashMap<>();
        void save(Box b) { boxes.put(b.getBoxId(), b); }
        @Override
        public Box findById(String id) { return boxes.get(id); }
    }

    private InMemoryItemInfoRepository itemRepo;
    private InMemoryBoxRepository boxRepo;
    private WeightDecreasingComparator comparator;

    /**
     * Initializes repositories and test data before each test case.
     */
    @BeforeEach
    void setUp() {
        itemRepo = new InMemoryItemInfoRepository();
        boxRepo = new InMemoryBoxRepository();

        Item item1 = new Item("SKU1", "Product 1", null, null, 0.0, 2.0); // 2 kg
        Item item2 = new Item("SKU2", "Product 2", null, null, 0.0, 5.0); // 5 kg
        itemRepo.save(item1);
        itemRepo.save(item2);

        Box b1 = new Box("B1", "SKU1", 10,
                new Date(1,1,2025),
                new Date(1,1,2024),
                new Time(10,0,0));
        Box b2 = new Box("B2", "SKU2", 10,
                new Date(1,1,2025),
                new Date(1,1,2024),
                new Time(10,0,0));

        boxRepo.save(b1);
        boxRepo.save(b2);

        comparator = new WeightDecreasingComparator(itemRepo, boxRepo);
    }

    /**
     * Creates an {@link OrderLine} instance with a single allocation.
     *
     * @param boxId the ID of the box used in allocation
     * @param allocated the allocated quantity
     * @return a configured {@link OrderLine} instance
     */
    private OrderLine createOrderLine(String boxId, int allocated) {
        OrderLine line = new OrderLine("O1", 1, "SKU_TEST", 10);
        AllocatedInfo info = new AllocatedInfo(boxId, allocated);
        Map<String, AllocatedInfo> map = new HashMap<>();
        map.put(boxId, info);
        line.setAllocatedInfoList(map);
        return line;
    }

    /**
     * Ensures that the comparator correctly sorts in descending order by total allocated weight.
     * The heavier order line should appear first.
     */
    @Test
    void testCompareDescendingByWeight() {
        OrderLine lighter = createOrderLine("B1", 2); // 2 * 2 = 4 kg
        OrderLine heavier = createOrderLine("B2", 1); // 1 * 5 = 5 kg
        int result = comparator.compare(lighter, heavier);
        assertTrue(result > 0);
    }

    /**
     * Ensures that the comparator returns zero when total allocated weights are equal.
     */
    @Test
    void testCompareEqualWeights() {
        OrderLine l1 = createOrderLine("B1", 2); // 4 kg
        OrderLine l2 = createOrderLine("B1", 2); // 4 kg
        int result = comparator.compare(l1, l2);
        assertEquals(0, result);
    }

    /**
     * Ensures that the comparator identifies correctly when the first order line is heavier.
     */
    @Test
    void testCompareFirstHeavierThanSecond() {
        OrderLine heavier = createOrderLine("B2", 2); // 2 * 5 = 10 kg
        OrderLine lighter = createOrderLine("B1", 2); // 2 * 2 = 4 kg
        int result = comparator.compare(heavier, lighter);
        assertTrue(result < 0);
    }
}