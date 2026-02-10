package pt.ipp.isep.dei.domain.pickingPlanRelated;

import pt.ipp.isep.dei.domain.trolleyRelated.Trolley;
import pt.ipp.isep.dei.domain.trolleyRelated.TrolleyModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a picking plan consisting of multiple {@link Trolley} instances
 * generated from a specific {@link TrolleyModel}.
 * Each picking plan is assigned a unique identifier.
 */
public class PickingPlan {
    private List<Trolley> trolleys = new ArrayList<>();
    private TrolleyModel trolleyModel;
    private String Id;

    private static int counter = 0;

    /**
     * Constructs a {@code PickingPlan} for the specified trolley model.
     * Automatically generates a unique ID for the plan.
     *
     * @param trolleyModel the model used to create trolleys for this plan
     */
    public PickingPlan(TrolleyModel trolleyModel) {
        this.trolleyModel = trolleyModel;
        this.Id = generateId();
        counter++;
    }

    /**
     * Generates a unique ID for the picking plan in a thread-safe manner.
     *
     * @return the generated picking plan ID
     */
    private synchronized String generateId() {
        return String.format("PP%03d", counter);
    }

    /**
     * @return the list of trolleys included in this picking plan
     */
    public List<Trolley> getTrolleys() {
        return trolleys;
    }

    /**
     * @param trolleys the list of trolleys to set for this picking plan
     */
    public void setTrolleys(List<Trolley> trolleys) {
        this.trolleys = trolleys;
    }

    /**
     * @return the trolley model used in this picking plan
     */
    public TrolleyModel getTrolleyModel() {
        return trolleyModel;
    }

    /**
     * @param trolleyModel the trolley model to assign to this picking plan
     */
    public void setTrolleyModel(TrolleyModel trolleyModel) {
        this.trolleyModel = trolleyModel;
    }

    /**
     * Adds a new {@link Trolley} to the plan using the defined {@link TrolleyModel}.
     */
    public void addTrolley() {
        trolleys.add(new Trolley(trolleyModel));
    }

    /**
     * @return the unique identifier of this picking plan
     */
    public String getId() {
        return Id;
    }

    /**
     * @param id the identifier to assign to this picking plan
     */
    public void setId(String id) {
        Id = id;
    }

    /**
     * @return a formatted string containing detailed information about this picking plan
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Picking Plan ID: ").append(Id).append("\n");
        sb.append("Trolley Model: ").append(trolleyModel != null ? trolleyModel.getName() : "N/A").append("\n");
        sb.append("Number of Trolleys: ").append(trolleys.size()).append("\n");

        if (trolleys.isEmpty()) {
            sb.append("No trolleys in this picking plan.\n");
        } else {
            sb.append("Trolleys Details:\n");
            for (int i = 0; i < trolleys.size(); i++) {
                Trolley trolley = trolleys.get(i);
                sb.append("  Trolley ").append(i + 1).append(": ").append(trolley.toString()).append("\n");
            }
        }
        return sb.toString();
    }
}
