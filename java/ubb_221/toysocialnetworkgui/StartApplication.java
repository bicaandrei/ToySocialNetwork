package ubb_221.toysocialnetworkgui;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import ubb_221.toysocialnetworkgui.domain.*;
import ubb_221.toysocialnetworkgui.domain.Validators.FriendshipValidator;
import ubb_221.toysocialnetworkgui.domain.Validators.UserValidator;
import ubb_221.toysocialnetworkgui.repository.paging.*;
import ubb_221.toysocialnetworkgui.repository.*;
import ubb_221.toysocialnetworkgui.service.*;
import java.util.Scanner;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class StartApplication extends Application {

    DBRepository UserRepository;
    MessageRepository messageRepository;
    FriendshipDBRepository friendshipDBRepository;
    UserService userService;
    MessageService messageService;
    FriendshipService friendshipService;

    private Integer pages;

    public static void main(String[] args) {
        //System.out.println("ok");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        //System.out.println("ok");
        //String fileN = ApplicationContext.getPROPERTIES().getProperty("data.tasks.messageTask");
        String url="jdbc:postgresql://localhost:5432/socialnetwork";
        String username = "postgres";
        String password = "parola123";
        this.UserRepository = new DBRepository(url, username, password);
        this.friendshipDBRepository = new FriendshipDBRepository(url, username, password, UserRepository);
        this.messageRepository = new MessageRepository(url, username, password, UserRepository);
        Pageable pageable = new PageableImplementation(3, 2);
        this.UserRepository.findAll(pageable).getContent().forEach(System.out::println);
        Scanner scanner = new Scanner(System.in);

        // Prompt the user to enter an integer
        System.out.print("Enter the number of pages: ");

        // Check if the next input is an integer
        if (scanner.hasNextInt()) {
            // Read the integer
            this.pages = scanner.nextInt();

            // Display the entered integer
            System.out.println("You entered: " + this.pages);
        } else {
            // Display an error message if the input is not an integer
            System.out.println("Invalid input. Please enter an integer.");
        }
        this.userService = new UserService(this.UserRepository, this.pages);
        this.userService.setPageSize(this.pages);
        UserValidator userValidator = new UserValidator();
        FriendshipValidator friendshipValidator = new FriendshipValidator(userValidator);
        this.friendshipService = new FriendshipService(this.UserRepository, this.friendshipDBRepository, friendshipValidator, this.pages);
        this.friendshipService.setPageSize(this.pages);
        this.messageService = new MessageService(messageRepository);

        initView(primaryStage);
        primaryStage.setWidth(800);
        primaryStage.show();

    }

    private void initView(Stage primaryStage) throws IOException {

        FXMLLoader messageLoader = new FXMLLoader();
        messageLoader.setLocation(getClass().getResource("views/users-view.fxml"));
        AnchorPane messageTaskLayout = messageLoader.load();
        primaryStage.setScene(new Scene(messageTaskLayout));

        UserController userController = messageLoader.getController();
        userController.setUserService(userService, friendshipService, messageService);

    }

}
