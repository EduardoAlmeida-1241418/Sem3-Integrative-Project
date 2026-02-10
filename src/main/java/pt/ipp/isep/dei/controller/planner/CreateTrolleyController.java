package pt.ipp.isep.dei.controller.planner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pt.ipp.isep.dei.domain.trolleyRelated.TrolleyModel;
import pt.ipp.isep.dei.data.repository.Repositories;
import pt.ipp.isep.dei.data.repository.sprint1.TrolleyModelRepository;

/**
 * Controller responsible for creating and managing trolley models.
 * <p>
 * Provides methods to list existing trolley models, validate input,
 * and register new models in the repository.
 */
public class CreateTrolleyController {

    /** Repository for accessing and managing trolley models. */
    private final TrolleyModelRepository trolleyModelRepository;

    /** Observable list containing all trolley models for UI binding. */
    private final ObservableList<TrolleyModel> trolleyModelsObservable;

    /**
     * Default constructor that initializes the repository and model list.
     */
    public CreateTrolleyController() {
        trolleyModelRepository = Repositories.getInstance().getTrolleyModelRepository();
        trolleyModelsObservable = FXCollections.observableArrayList(trolleyModelRepository.findAll());
    }

    /**
     * Retrieves all existing trolley models as an observable list.
     *
     * @return an {@link ObservableList} of {@link TrolleyModel} objects
     */
    public ObservableList<TrolleyModel> getTrolleyModelList() {
        return trolleyModelsObservable;
    }

    /**
     * Creates and saves a new trolley model with validation checks.
     *
     * @param name the name of the trolley model
     * @param maxWeight the maximum weight capacity as a string
     * @return an error message if validation fails, or {@code null} if creation succeeds
     */
    public String createTrolleyModel(String name, String maxWeight) {
        if (trolleyModelRepository.findByName(name) != null) {
            return "Name already exists";
        }

        int weight;
        try {
            weight = Integer.parseInt(maxWeight);
            if (weight <= 0) return "Max weight must be a positive number";
        } catch (NumberFormatException e) {
            return "Max weight must be a valid number";
        }

        TrolleyModel model = new TrolleyModel(name, weight);
        trolleyModelRepository.add(model);
        trolleyModelsObservable.add(model);

        return null;
    }
}
