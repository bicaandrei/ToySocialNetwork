package ubb_221.toysocialnetworkgui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ubb_221.toysocialnetworkgui.controller.MessageAlert;
import ubb_221.toysocialnetworkgui.domain.Validators.UserValidator;
import ubb_221.toysocialnetworkgui.encryption.SHA256;
import ubb_221.toysocialnetworkgui.service.FriendshipService;
import ubb_221.toysocialnetworkgui.service.MessageService;
import ubb_221.toysocialnetworkgui.service.UserService;
import ubb_221.toysocialnetworkgui.domain.User;

import java.io.IOException;

import static java.lang.Long.parseLong;

public class LoginUsersController {

    @FXML
    private TextField textFieldUsername;

    @FXML
    private TextField textFieldPassword;

    private UserService service;

    private FriendshipService friendshipService;
    private MessageService messageService;

    private Boolean is_logged = Boolean.FALSE;
    Stage dialogStage;
    private User user;
    private UserValidator validator;

    private SHA256 sha256;

    @FXML
    private void initialize(){}

    public void setService(UserService service, FriendshipService friendshipService, MessageService messageService, Stage stage, User user){

        this.service = service;
        this.friendshipService = friendshipService;
        this.messageService = messageService;
        this.dialogStage = stage;
        this.user = user;
        this.sha256 = new SHA256();

    }

    private void setFields(User user){

        textFieldUsername.setText(user.getUsername());
        textFieldPassword.setText(user.getPassword());

    }

    private void clearFields(User user){

        textFieldUsername.setText("");
        textFieldPassword.setText("");

    }


    public void handleLogin(){

        String username = textFieldUsername.getText();
        String password = textFieldPassword.getText();
        if(username.equals(this.user.getUsername()) && sha256.hashString(password).equals(this.user.getPassword())) {
            LoginMessage(user);
            ShowMessageTaskProfileDialog(user);
            user.setLoggedIn(Boolean.TRUE);
        }
        else {
            user.setLoggedIn(Boolean.FALSE);
            NoLoginMessage(user);
        }
    }

    private void LoginMessage(User user){

        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "User login", "User logged in successfully");
        dialogStage.close();

    }

    public void ShowMessageTaskProfileDialog(User user) {

        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("views/user-profile.fxml"));


            AnchorPane root = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle(user.toString() + "'s profile");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            UserProfileController loginUserViewController = loader.getController();
            loginUserViewController.setService(this.service, this.friendshipService, this.messageService, user);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void NoLoginMessage(User user){

        MessageAlert.showErrorMessage(null,  "User credentials incorrect!");
        dialogStage.close();

    }

    @FXML
    public void handleCancel() { dialogStage.close(); }

}
