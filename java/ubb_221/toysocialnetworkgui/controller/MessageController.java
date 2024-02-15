package ubb_221.toysocialnetworkgui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ubb_221.toysocialnetworkgui.domain.Message;
import ubb_221.toysocialnetworkgui.domain.Validators.UserValidator;
import ubb_221.toysocialnetworkgui.service.MessageService;
import ubb_221.toysocialnetworkgui.domain.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ubb_221.toysocialnetworkgui.utils.events.UserChangeEvent;
import ubb_221.toysocialnetworkgui.utils.observer.Observer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.lang.Long.parseLong;

public class MessageController {

    @FXML
    private ListView listView;

    private ObservableList<String> model = FXCollections.observableArrayList();

    @FXML
    private TextArea textFieldMessage;

    private MessageService service;
    Stage dialogStage;
    private User user;

    private User selected;

    private void initModel(){


        List<Message> messages = this.service.getMessagesOfUsers(this.user, this.selected);
        List<String> messages1 = new ArrayList<String>();
        for (Message m : messages) {

            if(m.getFrom().getId() == this.user.getId())
                m.setMessage("You: " + m.getMessage());
            else
                m.setMessage(m.getFrom().getUsername() + ": " + m.getMessage());
            messages1.add(m.getMessage());

        }
        model.setAll(messages1);

    }

    @FXML
    private void initialize() {

        listView.setItems(model);

    }
    public void setService(MessageService service, User user, User selected, Stage dialogStage){

        this.service = service;
        this.user = user;
        this.selected = selected;
        this.dialogStage = dialogStage;
        initModel();

    }

    private void setFields(Message message){

        textFieldMessage.setText(message.getMessage());

    }

    private void clearFields(){

        textFieldMessage.setText("");

    }

    public void handleSend() {

        String message = this.textFieldMessage.getText();
        LocalDateTime dateTime = LocalDateTime.now();
        Message m = new Message(this.user, this.selected, message, dateTime);
        m.setId(this.service.createId());
        try {

            this.service.addMessage(m);
            m.setMessage("You: " + m.getMessage());
            this.model.add(m.getMessage());
            this.SendMessage();

        }catch (IllegalArgumentException e){

            NoSend();

        }

    }

    private void SendMessage(){

        this.clearFields();

    }

    private void NoSend(){

        MessageAlert.showErrorMessage(null, "Message must not be empty!");

    }

    @FXML
    public void handleCancel() { dialogStage.close(); }


}
