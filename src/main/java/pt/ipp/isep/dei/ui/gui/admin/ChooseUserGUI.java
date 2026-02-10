package pt.ipp.isep.dei.ui.gui.admin;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import pt.ipp.isep.dei.controller.admin.chooseUserController;
import pt.ipp.isep.dei.domain.User;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * GUI controller for the Admin "Choose User" page.
 * Displays a list of users in a table and allows the admin to edit their roles or status.
 */
public class ChooseUserGUI implements Initializable {

    private chooseUserController controller;
    private UIUtils uiUtils;

    @FXML
    private TableView<User> usersTableView;

    @FXML
    private TableColumn<User, Integer> colId;

    @FXML
    private TableColumn<User, String> colEmail;

    @FXML
    private TableColumn<User, String> colStatus;

    @FXML
    private TableColumn<User, Void> colEdit;

    /**
     * Initializes the controller and populates the table with user data.
     *
     * @param url not used
     * @param resourceBundle not used
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        controller = new chooseUserController();
        uiUtils = new UIUtils();
        fixColunm();
        loadInfo();
    }

    /**
     * Centers all table column content using inline CSS.
     */
    private void fixColunm() {
        colId.setStyle("-fx-alignment: CENTER;");
        colEmail.setStyle("-fx-alignment: CENTER;");
        colStatus.setStyle("-fx-alignment: CENTER;");
        colEdit.setStyle("-fx-alignment: CENTER;");
    }

    /**
     * Loads user data into the table and configures column bindings.
     * Adds the edit button to each row.
     */
    private void loadInfo() {
        controller = new chooseUserController();

        // Bind data properties to table columns
        colId.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getId()));

        colEmail.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEmail()));

        colStatus.setCellValueFactory(user ->
                new SimpleStringProperty(user.getValue().isActive() ? "Active" : "Inactive"));

        addEditButtonToTable();

        ObservableList<User> items = controller.getUserInfo();
        usersTableView.setItems(items);

        // Enable sorting by ID
        colId.setSortable(true);
        colId.setSortType(TableColumn.SortType.ASCENDING);
        usersTableView.getSortOrder().add(colId);
        usersTableView.sort();
    }

    /**
     * Logs out the admin and returns to the Home Page.
     *
     * @param event the logout button action event
     */
    @FXML
    public void logoutButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/homePage.fxml", "MABEC - Logout");
    }

    /**
     * Adds an "Edit" button to each row of the user table.
     * When clicked, it opens the Edit User page for the selected user.
     */
    private void addEditButtonToTable() {
        colEdit.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Edit");

            {
                editButton.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    handleEditUser(user, event);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(editButton);
                }
            }
        });
    }

    /**
     * Opens the Admin Edit User page and loads data for the selected user.
     *
     * @param user the selected user
     * @param event the action event that triggered the navigation
     */
    private void handleEditUser(User user, ActionEvent event) {
        FXMLLoader loader = uiUtils.loadFXMLScene(event, "/fxml/admin/adminEditUser.fxml", "MABEC - Edit User");
        AdminEditUserGUI adminEditUserGUI = loader.getController();
        adminEditUserGUI.loadInfo(user);
    }

    /**
     * Navigates back to the Admin Home Page.
     *
     * @param event the home button action event
     */
    public void homeButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/admin/adminHomePage.fxml", "MABEC - Admin Home Page");
    }
}
