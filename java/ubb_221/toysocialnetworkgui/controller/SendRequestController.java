package ubb_221.toysocialnetworkgui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import ubb_221.toysocialnetworkgui.domain.Friendship;
import ubb_221.toysocialnetworkgui.domain.User;
import ubb_221.toysocialnetworkgui.service.FriendshipService;
import ubb_221.toysocialnetworkgui.service.MessageService;
import ubb_221.toysocialnetworkgui.service.UserService;
import ubb_221.toysocialnetworkgui.controller.MessageAlert;
import javafx.scene.control.Alert;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class SendRequestController {

    ObservableList<User> model = FXCollections.observableArrayList();
    @FXML
    TableView<User> tableView;

    private UserService service;

    private FriendshipService friendshipService;
    private MessageService messageService;
    Stage dialogStage;
    private User user;
    @FXML
    TableColumn<User, Long> tableColumnId;

    @FXML
    TableColumn<User, String> tableColumnFirstName;

    @FXML
    TableColumn<User, String> tableColumnLastName;

    @FXML
    TableColumn<User, String> tableColumnUsername;

    public void setService(UserService userService, FriendshipService friendshipService, User user, Stage dialogStage){

        this.service = userService;
        this.messageService = messageService;
        this.friendshipService = friendshipService;
        this.user = user;
        this.dialogStage = dialogStage;
        initModel();

    }

    private void initModel() {

        List<User> userSet = this.friendshipService.getNonFriendsOfUser(this.user);
        model.setAll(userSet);

    }

    @FXML
    private void initialize(){

        tableColumnId.setCellValueFactory(new PropertyValueFactory<User, Long>("id"));
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<User, String>("first_name"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<User, String>("last_name"));
        tableColumnUsername.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        tableView.setItems(model);

    }

    public void handleSendRequest(ActionEvent event){

        User selected = tableView.getSelectionModel().getSelectedItem();
        if(selected != null){

            Friendship friendship = new Friendship(this.user, selected);
            friendship.setId(this.friendshipService.create_friendship_id());
            this.friendshipService.addEntity(friendship);
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Friend request", "Friend request sent successfully!");

        } else
            MessageAlert.showErrorMessage(null, "Select a user!");

    }
}
