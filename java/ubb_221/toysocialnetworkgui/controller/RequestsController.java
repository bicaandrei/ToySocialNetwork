package ubb_221.toysocialnetworkgui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import ubb_221.toysocialnetworkgui.UserProfileController;
import ubb_221.toysocialnetworkgui.domain.Friendship;
import ubb_221.toysocialnetworkgui.domain.User;
import ubb_221.toysocialnetworkgui.service.FriendshipService;
import ubb_221.toysocialnetworkgui.utils.events.FriendshipsChangeEvent;
import ubb_221.toysocialnetworkgui.utils.events.UserChangeEvent;
import ubb_221.toysocialnetworkgui.utils.observer.Observer;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class RequestsController implements Observer<FriendshipsChangeEvent> {

    @FXML
    TableView<Friendship> tableView;

    @FXML
    TableColumn<Friendship, String> tableColumnUsername1;

    @FXML
    TableColumn<Friendship, String> tableColumnUsername2;

    @FXML
    TableColumn<Friendship, String> tableColumnStatus;

    FriendshipService friendshipService;

    ObservableList<Friendship> model = FXCollections.observableArrayList();
    User user;

    User selected;
    @FXML
    private Stage dialogStage;

    private UserProfileController userProfileController;

    public void setService(FriendshipService friendshipService, User user, Stage dialogStage, UserProfileController controller){

        this.friendshipService = friendshipService;
        this.user = user;
        this.dialogStage = dialogStage;
        initModel();
        this.friendshipService.addObserver(this);
        this.userProfileController = controller;

    }
    private void initModel(){

        Set<Friendship> friendshipSet = this.friendshipService.getFriendshipsOnPage(0, this.user);
        model.setAll(friendshipSet);

    }

    @FXML
    private void initialize(){

        tableView.getColumns().add(this.acceptButton());
        tableView.getColumns().add(this.rejectButton());
        tableColumnUsername1.setCellValueFactory(new PropertyValueFactory<Friendship, String>("username1"));
        tableColumnUsername2.setCellValueFactory(new PropertyValueFactory<Friendship, String>("username2"));
        tableColumnStatus.setCellValueFactory(new PropertyValueFactory<Friendship, String>("status"));
        tableView.setItems(model);

    }

    private TableColumn<Friendship, Void> acceptButton(){

        TableColumn<Friendship, Void> acceptColumn = new TableColumn<>("Accept");
        acceptColumn.setCellFactory(param -> new TableCell<>() {
            private final Button acceptButton = new Button("Accept");

            {
                acceptButton.setOnAction(event -> {
                    Friendship friendRequest = getTableView().getItems().get(getIndex());
                    // Handle the accept action here
                    handleAcceptAction(friendRequest, this);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Friendship friendRequest = getTableView().getItems().get(getIndex());

                    // Assuming you have a currentUser object with user information
                    if (user.getId().equals(friendRequest.get_e2().getId()) && "PENDING".equals(friendRequest.getStatus())) {
                        // Show the button only if the current user is the recipient and the status is pending
                        setGraphic(acceptButton);
                    } else {
                        setGraphic(null);
                    }
                }
            }
        });

        return acceptColumn;

    }

    private void handleAcceptAction(Friendship friendRequest, TableCell<Friendship, Void> cell) {

        friendRequest.set_status("APPROVED");
        this.friendshipService.updateEntity(friendRequest);
        initModel();
        this.userProfileController.initModel();

    }

    private TableColumn<Friendship, Void> rejectButton(){

        TableColumn<Friendship, Void> acceptColumn = new TableColumn<>("Reject");
        acceptColumn.setCellFactory(param -> new TableCell<>() {
            private final Button acceptButton = new Button("Reject");

            {
                acceptButton.setOnAction(event -> {
                    Friendship friendRequest = getTableView().getItems().get(getIndex());
                    // Handle the accept action here
                    handleRejectAction(friendRequest, this);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Friendship friendRequest = getTableView().getItems().get(getIndex());

                    // Assuming you have a currentUser object with user information
                    if (user.getId().equals(friendRequest.get_e2().getId()) && "PENDING".equals(friendRequest.getStatus())) {
                        // Show the button only if the current user is the recipient and the status is pending
                        setGraphic(acceptButton);
                    } else {
                        setGraphic(null);
                    }
                }
            }
        });

        return acceptColumn;

    }

    private void handleRejectAction(Friendship friendRequest, TableCell<Friendship, Void> cell) {

        friendRequest.set_status("REJECTED");
        this.friendshipService.updateEntity(friendRequest);
        this.userProfileController.initModel();
        initModel();

    }

    @Override
    public void update(FriendshipsChangeEvent friendshipsChangeEvent){

        initModel();

    }
    @FXML
    public void handleCancel() { dialogStage.close(); }

}
