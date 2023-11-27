package ubb_221.toysocialnetworkgui;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import ubb_221.toysocialnetworkgui.domain.*;
import ubb_221.toysocialnetworkgui.repository.paging.*;
import ubb_221.toysocialnetworkgui.repository.*;
import ubb_221.toysocialnetworkgui.service.*;


import java.io.IOException;

public class StartApplication extends Application {

    PagingRepository<Long, User> UserRepository;
    UserService userService;

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
        Pageable pageable = new PageableImplementation(3, 2);
        this.UserRepository.findAll(pageable).getContent().forEach(System.out::println);
        this.userService = new UserService(this.UserRepository);
        //this.userService.getAll().forEach(System.out::println);

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
        userController.setUserService(userService);

    }
}
