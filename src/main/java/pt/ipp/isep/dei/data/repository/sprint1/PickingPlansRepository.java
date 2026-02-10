package pt.ipp.isep.dei.data.repository.sprint1;

import pt.ipp.isep.dei.domain.pickingPlanRelated.PickingPlan;

import java.util.*;

/**
 * Repository responsible for managing {@link PickingPlan} entities.
 * Provides CRUD operations and utilities for plan retrieval and removal.
 */
public class PickingPlansRepository {

    /** Internal list storing all picking plans in insertion order. */
    private final LinkedList<PickingPlan> pickingPlans = new LinkedList<>();

    /**
     * Adds a new picking plan to the repository.
     *
     * @param pickingPlan the picking plan to add
     * @throws IllegalArgumentException if the picking plan or its ID is null or empty
     * @throws IllegalStateException if a picking plan with the same ID already exists
     */
    public void add(PickingPlan pickingPlan) {
        if (pickingPlan == null) {
            throw new IllegalArgumentException("Picking Plan cannot be null.");
        }

        String id = pickingPlan.getId();
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Picking Plan ID cannot be null or empty.");
        }

        if (existsById(id)) {
            throw new IllegalStateException("A Picking Plan with ID already exists: " + id);
        }

        pickingPlans.add(pickingPlan);
    }

    /**
     * Finds a picking plan by its unique ID.
     *
     * @param pickingPlanId the ID of the picking plan
     * @return the corresponding {@link PickingPlan}
     * @throws IllegalArgumentException if the ID is null or empty
     * @throws NoSuchElementException if no picking plan is found with the given ID
     */
    public PickingPlan findById(String pickingPlanId) {
        if (pickingPlanId == null || pickingPlanId.isEmpty()) {
            throw new IllegalArgumentException("Picking Plan ID cannot be null or empty.");
        }

        for (PickingPlan plan : pickingPlans) {
            if (pickingPlanId.equals(plan.getId())) {
                return plan;
            }
        }

        throw new NoSuchElementException("Picking Plan not found with ID: " + pickingPlanId);
    }

    /**
     * Checks if a picking plan exists with the specified ID.
     *
     * @param pickingPlanId the picking plan ID
     * @return true if a plan with the given ID exists, false otherwise
     * @throws IllegalArgumentException if the ID is null or empty
     */
    public boolean existsById(String pickingPlanId) {
        if (pickingPlanId == null || pickingPlanId.isEmpty()) {
            throw new IllegalArgumentException("Picking Plan ID cannot be null or empty.");
        }

        for (PickingPlan plan : pickingPlans) {
            if (pickingPlanId.equals(plan.getId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Removes a picking plan from the repository by its ID.
     *
     * @param pickingPlanId the ID of the picking plan to remove
     * @throws IllegalArgumentException if the ID is null or empty
     * @throws NoSuchElementException if no picking plan exists with the given ID
     */
    public void remove(String pickingPlanId) {
        if (pickingPlanId == null || pickingPlanId.isEmpty()) {
            throw new IllegalArgumentException("Picking Plan ID cannot be null or empty.");
        }

        Iterator<PickingPlan> it = pickingPlans.iterator();
        while (it.hasNext()) {
            PickingPlan plan = it.next();
            if (pickingPlanId.equals(plan.getId())) {
                it.remove();
                return;
            }
        }

        throw new NoSuchElementException("Picking Plan not found with ID: " + pickingPlanId);
    }

    /**
     * Retrieves all picking plans stored in the repository.
     *
     * @return unmodifiable list of all picking plans
     */
    public List<PickingPlan> findAll() {
        return Collections.unmodifiableList(new ArrayList<>(pickingPlans));
    }

    /**
     * Removes all picking plans from the repository.
     */
    public void clear() {
        pickingPlans.clear();
    }

    /**
     * Counts the total number of picking plans currently stored.
     *
     * @return total count of picking plans
     */
    public int count() {
        return pickingPlans.size();
    }

    /**
     * Removes a specific picking plan instance from the repository.
     *
     * @param pickingPlan the picking plan to remove
     * @return true if the plan was removed successfully, false otherwise
     * @throws IllegalArgumentException if the picking plan is null
     */
    public boolean remove(PickingPlan pickingPlan) {
        if (pickingPlan == null) {
            throw new IllegalArgumentException("Picking Plan cannot be null.");
        }
        return pickingPlans.remove(pickingPlan);
    }

    /**
     * Removes a picking plan located at the specified index.
     *
     * @param index the position of the picking plan in the list
     * @return the removed {@link PickingPlan}
     * @throws NoSuchElementException if the index is out of range
     */
    public PickingPlan removeAt(int index) {
        try {
            return pickingPlans.remove(index);
        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchElementException("Index out of range: " + index);
        }
    }
}
