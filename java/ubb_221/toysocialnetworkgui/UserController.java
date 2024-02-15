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
import ubb_221.toysocialnetworkgui.repository.paging.Page;
import ubb_221.toysocialnetworkgui.service.FriendshipService;
import ubb_221.toysocialnetworkgui.service.MessageService;
import ubb_221.toysocialnetworkgui.service.UserService;
import ubb_221.toysocialnetworkgui.utils.events.UserChangeEvent;
import ubb_221.toysocialnetworkgui.utils.observer.Observer;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class UserController implements Observer<UserChangeEvent> {

    UserService service;
    FriendshipService friendshipService;
    MessageService messageService;
    ObservableList<User> model = FXCollections.observableArrayList();
    @FXML
    TableView<User> tableView;

    @FXML
    TableColumn<User, Long> tableColumnId;

    @FXML
    TableColumn<User, String> tableColumnFirstName;

    @FXML
    TableColumn<User, String> tableColumnLastName;

    @FXML
    TableColumn<User, String> tableColumnUsername;

    @FXML
    TableColumn<User, String> tableColumnPassword;
    public void setUserService(UserService userService, FriendshipService friendshipService, MessageService messageService){

        this.service = userService;
        this.messageService = messageService;
        this.friendshipService = friendshipService;
        initModel();
        this.service.addObserver(this);

    }

    private void initModel() {

        Set<User> userSet = this.service.getMessagesOnPage(0);
        model.setAll(userSet);

    }

    @FXML
    private void initialize(){

        tableColumnId.setCellValueFactory(new PropertyValueFactory<User, Long>("id"));
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<User, String>("first_name"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<User, String>("last_name"));
        tableColumnUsername.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        //tableColumnPassword.setCellValueFactory(new PropertyValueFactory<User, String>("unCryptedPassword"));
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

    @FXML
    public void handleLoginMessage(ActionEvent event){

        User selected = tableView.getSelectionModel().getSelectedItem();
        if(selected != null){

            ShowMessageTaskLoginDialog(selected);
            //ShowMessageTaskProfileDialog(selected);

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

    public void ShowMessageTaskLoginDialog(User user) {

        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("views/user-login.fxml"));


            AnchorPane root = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Login");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            LoginUsersController loginUserViewController = loader.getController();
            loginUserViewController.setService(this.service, this.friendshipService, this.messageService, dialogStage, user);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
