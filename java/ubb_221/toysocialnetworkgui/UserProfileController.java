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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ubb_221.toysocialnetworkgui.LoginUsersController;
import ubb_221.toysocialnetworkgui.controller.MessageAlert;
import ubb_221.toysocialnetworkgui.controller.MessageController;
import ubb_221.toysocialnetworkgui.controller.RequestsController;
import ubb_221.toysocialnetworkgui.controller.SendRequestController;
import ubb_221.toysocialnetworkgui.domain.Friendship;
import ubb_221.toysocialnetworkgui.domain.Validators.UserValidator;
import ubb_221.toysocialnetworkgui.domain.Validators.ValidateException;
import ubb_221.toysocialnetworkgui.repository.MessageRepository;
import ubb_221.toysocialnetworkgui.service.FriendshipService;
import ubb_221.toysocialnetworkgui.service.MessageService;
import ubb_221.toysocialnetworkgui.service.UserService;
import ubb_221.toysocialnetworkgui.domain.User;
import ubb_221.toysocialnetworkgui.utils.events.UserChangeEvent;
import ubb_221.toysocialnetworkgui.utils.events.FriendshipsChangeEvent;
import ubb_221.toysocialnetworkgui.utils.observer.Observer;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.lang.Long.parseLong;

public class UserProfileController implements Observer<FriendshipsChangeEvent> {

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

    public void setService(UserService userService, FriendshipService friendshipService, MessageService messageService, User user){

        this.service = userService;
        this.friendshipService = friendshipService;
        this.messageService = messageService;
        this.user = user;
        initModel();
        this.friendshipService.addObserver(this);

    }

    public void initModel(){

        Set<User> UserList = this.friendshipService.getFriendsOnPage(0, this.user);
        model.setAll(UserList);

    }

    @Override
    public void update(FriendshipsChangeEvent friendshipsChangeEvent){

        initModel();

    }
    @FXML
    private void initialize(){

        tableColumnId.setCellValueFactory(new PropertyValueFactory<User, Long>("id"));
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<User, String>("first_name"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<User, String>("last_name"));
        tableColumnUsername.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        tableView.setItems(model);

    }

    @FXML
    public void handleMessageSend(ActionEvent event){

        User selected = tableView.getSelectionModel().getSelectedItem();
        if(selected != null){

           ShowMessageSendDialog(this.user, selected);

        } else
            MessageAlert.showErrorMessage(null, "Select a user!");

    }

    public void handleFriendRequest(ActionEvent event){

        ShowFriendRequestDialog(this, this.user);
        initModel();

    }

    public void handleSendRequest(ActionEvent event){

        /*
        User selected = tableView.getSelectionModel().getSelectedItem();
        if(selected != null){

            Friendship friendship = new Friendship(this.user, selected);
            friendship.setId(this.friendshipService.create_friendship_id());
            this.friendshipService.addEntity(friendship);
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Friend request", "Friend request sent successfully!");

        } else
            MessageAlert.showErrorMessage(null, "Select a user!");
         */
        RequestSendDialog(this.user);
        initModel();

    }
    public void ShowMessageSendDialog(User user, User selected) {

        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("views/message-view.fxml"));


            AnchorPane root = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Messages");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            MessageController messageController = loader.getController();
            messageController.setService(this.messageService, user, selected, dialogStage);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void ShowFriendRequestDialog(UserProfileController controller, User user){

        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("views/requests-view.fxml"));

            AnchorPane root = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Friend requests");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            RequestsController requestsController = loader.getController();
            requestsController.setService(this.friendshipService, user, dialogStage, controller);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void RequestSendDialog(User user){

        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("views/request-send.fxml"));

            AnchorPane root = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Send friend request");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            SendRequestController sendRequestController = loader.getController();
            sendRequestController.setService(this.service, this.friendshipService, user, dialogStage);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @FXML
    public void handleCancel() { dialogStage.close(); }

}
