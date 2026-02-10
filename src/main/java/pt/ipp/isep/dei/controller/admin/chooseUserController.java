package pt.ipp.isep.dei.controller.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pt.ipp.isep.dei.domain.User;
import pt.ipp.isep.dei.data.repository.Repositories;
import pt.ipp.isep.dei.data.repository.UserRepository;

/**
 * Controller responsible for retrieving user information for administrative selection.
 * <p>
 * Provides access to all registered users as an observable list for UI display.
 */
public class chooseUserController {

    /** Repository for managing user data. */
    private UserRepository userRepository;

    /**
     * Default constructor that initializes the user repository.
     */
    public chooseUserController() {
        userRepository = Repositories.getInstance().getUserRepository();
    }

    /**
     * Retrieves all users from the repository.
     *
     * @return an observable list containing all users
     */
    public ObservableList<User> getUserInfo() {
        return FXCollections.observableArrayList(userRepository.findAll());
    }
}
