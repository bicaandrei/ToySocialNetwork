package ubb_221.toysocialnetworkgui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ubb_221.toysocialnetworkgui.controller.EditUsersController;
import ubb_221.toysocialnetworkgui.controller.MessageAlert;
import ubb_221.toysocialnetworkgui.domain.User;
import ubb_221.toysocialnetworkgui.service.UserService;
import ubb_221.toysocialnetworkgui.utils.events.UserChangeEvent;
import ubb_221.toysocialnetworkgui.utils.observer.Observer;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UserController implements Observer<UserChangeEvent> {

    UserService service;
    ObservableList<User> model = FXCollections.observableArrayList();
    @FXML
    TableView<User> tableView;

    @FXML
    TableColumn<User, Long> tableColumnId;

    @FXML
    TableColumn<User, String> tableColumnFirstName;

    @FXML
    TableColumn<User, String> tableColumnLastName;

    public void setUserService(UserService userService){

        this.service = userService;
        initModel();
        this.service.addObserver(this);

    }

    private void initModel(){

        Iterable<User> messages = service.getAll();
        List<User> UserList = StreamSupport.stream(messages.spliterator(), false)
                .collect(Collectors.toList());
        model.setAll(UserList);

    }

    @FXML
    private void initialize(){

        tableColumnId.setCellValueFactory(new PropertyValueFactory<User, Long>("id"));
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<User, String>("first_name"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<User, String>("last_name"));
        tableView.setItems(model);

    }
    @FXML
    public void handleAddMessage(ActionEvent ev) {
        // TODO
        showMessageTaskEditDialog(null);
    }
    public void handleDeleteMessage(ActionEvent actionEvent){

        User selected = tableView.getSelectionModel().getSelectedItem();
        if(selected != null) {

            User deleted = service.deleteUser(selected);
            if (deleted != null)
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Delete", "User deleted succesfully!");

        }
        else MessageAlert.showErrorMessage(null, "Select a user!");


    }

    @Override
    public void update(UserChangeEvent userChangeEvent){

        initModel();

    }

    @FXML
    public void handleUpdateMessage(ActionEvent event){

        User selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            showMessageTaskEditDialog(selected);
        } else
            MessageAlert.showErrorMessage(null, "Select a user!");

    }

    public void showMessageTaskEditDialog(User user) {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("views/edituser-view.fxml"));


            AnchorPane root = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Message");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            EditUsersController editUserViewController = loader.getController();
            editUserViewController.setService(this.service, dialogStage, user);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
