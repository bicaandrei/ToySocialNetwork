import Domain.*;
import Domain.Validators.FriendshipValidator;
import Domain.Validators.UserValidator;
import Domain.Validators.ValidateException;
import Domain.Validators.Validator;
import Repository.*;
import Service.CommunitiesService;
import Service.FriendshipService;
import Service.UserService;
import Ui.UserInterface;

import java.util.Objects;

public class Main {
    public static void main(String[] args) {

        String user_file = "Users.txt";
        String friendships_file = "Friendships.txt";
        UserValidator user_validator = new UserValidator();
        Validator<User> userValidator = user_validator::validate;
        FriendshipValidator friendship_validator = new FriendshipValidator(userValidator);
        Validator<Friendship> friendshipValidator = friendship_validator::validate;
        UserFileRepository user_repository = new UserFileRepository(user_file, userValidator);
        FrienshipFileRepository friendship_repository = new FrienshipFileRepository(friendships_file, friendshipValidator);
        CommunitiesRepository communities_repository = new CommunitiesRepository();
        UserService user_service = new UserService(user_repository, userValidator);
        FriendshipService friendship_service = new FriendshipService(user_repository, friendship_repository, friendshipValidator);
        CommunitiesService communities_service = new CommunitiesService(communities_repository);
        UserInterface ui = new UserInterface(user_service, friendship_service, communities_service);
        ui.run();

    }
}