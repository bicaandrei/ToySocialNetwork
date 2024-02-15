package ubb_221.toysocialnetworkgui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ubb_221.toysocialnetworkgui.domain.Validators.UserValidator;
import ubb_221.toysocialnetworkgui.domain.Validators.ValidateException;
import ubb_221.toysocialnetworkgui.encryption.SHA256;
import ubb_221.toysocialnetworkgui.service.UserService;
import ubb_221.toysocialnetworkgui.domain.User;

import static java.lang.Long.parseLong;

public class EditUsersController {

    @FXML
    private TextField textFieldId;

    @FXML
    private TextField textFieldFirstName;

    @FXML
    private TextField textFieldLastName;

    @FXML
    private TextField textFieldUsername;

    @FXML
    private TextField textFieldPassword;

    private UserService service;

    Stage dialogStage;
    private User user;
    private UserValidator validator;

    private SHA256 sha256;

    @FXML
    private void initialize(){}

    public void setService(UserService service, Stage stage, User user){

        this.service = service;
        this.dialogStage = stage;
        this.user = user;
        if(user != null){

            setFields(user);
            textFieldId.setEditable(false);

        }
        this.sha256 = new SHA256();

    }

    private void setFields(User user){

        textFieldId.setText(user.getId().toString());
        textFieldFirstName.setText(user.getFirst_name());
        textFieldLastName.setText(user.getLast_name());
        textFieldUsername.setText(user.getUsername());
        textFieldPassword.setText(user.getPassword());

    }

    private void clearFields(User user){

        textFieldId.setText("");
        textFieldFirstName.setText("");
        textFieldLastName.setText("");
        textFieldUsername.setText("");
        textFieldPassword.setText("");

    }


    public void handleSave(){

        String id = textFieldId.getText();
        String first_name = textFieldFirstName.getText();
        String last_name = textFieldLastName.getText();
        String username = textFieldUsername.getText();
        String password = textFieldPassword.getText();
        User user = new User(first_name, last_name);
        user.setId(parseLong(id));
        user.setUsername(username);
        user.setPassword(sha256.hashString(password));
        user.setUnCryptedPassword(password);
        if(this.user == null)
            saveMessage(user);
        else
            updateMessage(user);

    }

    private void saveMessage(User user){

        try{

            if(this.service.findUser(user) == null) {
                User u = this.service.addUser(user);
                if (u == null)
                    dialogStage.close();
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "User save", "User has been saved!");
            }
            else
            {
                MessageAlert.showErrorMessage(null, "Id is taken!");
            }
        }catch(ValidateException e){

            MessageAlert.showErrorMessage(null, e.getMessage());

        }
        dialogStage.close();

    }

    private void updateMessage(User user){

        try{

            User u = this.service.updateUser(user);
            if (u==null){
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "User update", "The user has been updated!");
            }

        }catch (ValidateException e){

            MessageAlert.showErrorMessage(null, e.getMessage());

        }
        dialogStage.close();

    }

    @FXML
    public void handleCancel() { dialogStage.close(); }

}
