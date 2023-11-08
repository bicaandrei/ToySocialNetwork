package Ui;

import Domain.Friendship;
import Domain.Graph;
import Domain.User;
import Domain.Validators.ValidateException;
import Service.CommunitiesService;
import Service.FriendshipService;
import Service.ServiceException;
import Service.UserService;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class UserInterface {

    private UserService user_service;
    private FriendshipService friendship_service;

    private CommunitiesService community_service;
    public UserInterface(UserService user_service, FriendshipService friendship_service, CommunitiesService community_service) {

        this.user_service = user_service;
        this.friendship_service = friendship_service;
        this.community_service = community_service;
        this.user_service.loadUserData();
        //this.user_service.loadUserData();
        //this.friendship_service.loadFriendshipData();
        System.out.println("Press Enter to start the app!");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        int time = 0;
        int loadTime = 29;
        while(time < loadTime){

            System.out.print("#");

            try {
                // Sleep for 1 second (1000 milliseconds)
                Thread.sleep(30);
            } catch (InterruptedException e) {
                // Handle the exception if needed
            }

            time += 1;

        }

        System.out.println();

    }

    public void CommunityMenu(){

        Scanner scanner = new Scanner(System.in);
        int command = -1;
        while (command != 3){

            System.out.println("Choose an option!");
            System.out.println("1.Show the number of communities.");
            System.out.println("2.Show the most sociable community.");
            System.out.println("3.Back to main menu.");
            System.out.print("Option: ");
            command=scanner.nextInt();
            scanner.nextLine();
            switch (command){

                case 1:
                    System.out.println("There are " + this.community_service.getNumberOfCommunities() + " communities in total!");
                    break;
                case 2:
                   // if(this.community_service.getBiggestCommunity() != null) {
                        Set<Long> usersInCommunity = this.community_service.getBiggestCommunity();
                        System.out.print("The most sociable community is:\n");
                        for (Long userId : usersInCommunity)
                            System.out.println(this.user_service.findUserById(userId));
                        System.out.println();
                  //  }
                   // else{

                   //     System.out.println("There are no communities registered!");

                    //}
                    break;
                case 3:
                    this.run();
                    break;

            }

        }
        /*
        Map<Long, List<Long>> graph = this.community_service.getCommunities();
        for(Long node : graph.keySet()){

            List<Long> neighbors = graph.get(node);
            System.out.print("Node " + node + " is connected to: ");
            for(Long neigbor : neighbors){

                System.out.print(neigbor + " ");

            }
            System.out.println();

        }

         */



    }
    public void FrienshipMenu(){

        Scanner scanner = new Scanner(System.in);
        int command = -1;
        while (command != 4){

            System.out.println("Choose an option!");
            System.out.println("1.Add friendship.");
            System.out.println("2.Delete friendship.");
            System.out.println("3.Show all friendships.");
            System.out.println("4.Back to main menu.");
            System.out.print("Option: ");
            command = scanner.nextInt();
            scanner.nextLine();
            switch (command){

                case 1:
                    System.out.print("Enter user first name: ");
                    String first_name1 = scanner.nextLine();
                    System.out.print("Enter user last name: ");
                    String last_name1 = scanner.nextLine();
                    User user1 = new User(first_name1, last_name1);
                    System.out.print("Enter user first name: ");
                    String first_name2 = scanner.nextLine();
                    System.out.print("Enter user last name: ");
                    String last_name2 = scanner.nextLine();
                    User user2 = new User(first_name2, last_name2);
                    Long id1 = this.user_service.findUserByName(user1);
                    if(id1!=null){

                        user1.setId(id1);

                    }
                    else {

                        System.out.println(user1 + " doesn't exist!");

                    }
                    Long id2 = this.user_service.findUserByName(user2);
                    if(id2!=null){

                        user2.setId(id2);

                    }
                    else {

                        System.out.println(user2 + " doesn't exist!");

                    }
                    Friendship friendship = new Friendship(user1, user2);
                    friendship.setId(this.friendship_service.create_friendship_id());

                    try {

                        this.friendship_service.addEntity(friendship);
                        this.community_service.addFriendshipToCommunity(friendship);
                        System.out.println(friendship.toString() + " has been created succesfully!");

                    }catch (ValidateException e){

                        System.out.println(e.toString());

                    }
                    catch (ServiceException e){

                        System.out.println(e.toString());

                    }
                    break;
                case 2:
                    System.out.print("Enter user first name: ");
                    first_name1 = scanner.nextLine();
                    System.out.print("Enter user last name: ");
                    last_name1 = scanner.nextLine();
                    user1 = new User(first_name1, last_name1);
                    System.out.print("Enter user first name: ");
                    first_name2 = scanner.nextLine();
                    System.out.print("Enter user last name: ");
                    last_name2 = scanner.nextLine();
                    user2 = new User(first_name2, last_name2);
                    id1 = this.user_service.findUserByName(user1);
                    if(id1!=null){

                        user1.setId(id1);

                    }
                    else {

                        System.out.println(user1 + " doesn't exist!");

                    }
                    id2 = this.user_service.findUserByName(user2);
                    if(id2!=null){

                        user2.setId(id2);

                    }
                    else {

                        System.out.println(user2 + " doesn't exist!");

                    }
                    Friendship deleted_friendship = new Friendship(user1, user2);
                    Long friendship_id = this.friendship_service.findFriendshipByNames(deleted_friendship);
                    if (friendship_id != null){

                        deleted_friendship.setId(friendship_id);

                    }
                    else {

                        System.out.println(deleted_friendship + " doesn't exist!");

                    }

                    try {

                        this.friendship_service.removeEntity(deleted_friendship);
                        System.out.println(deleted_friendship.toString() + " has been deleted succesfully!");

                    }catch (ValidateException e){

                        System.out.println(e.toString());

                    }
                    catch (ServiceException e){

                        System.out.println(e.toString());

                    }
                    break;
                case 3:
                    if(this.friendship_service.getRepoSize() == 0){

                        System.out.println("There are no friendships registered!");

                    }
                    else {
                        for (Friendship f : this.friendship_service.getAll()) {

                            System.out.println(f.toString());

                        }
                    }
                    break;
                case 4:
                    this.run();
                    break;

            }

        }

    }
    public void UserMenu(){

        Scanner scanner = new Scanner(System.in);
        int command = -1;
        while (command != 5) {
            System.out.println("Choose an option!");
            System.out.println("1.Add user.");
            System.out.println("2.Delete user.");
            System.out.println("3.Update user.");
            System.out.println("4.Show all users.");
            System.out.println("5.Back to main menu.");
            System.out.print("Option: ");
            command = scanner.nextInt();
            scanner.nextLine();
            switch (command) {

                case 1:
                    System.out.print("Enter first name: ");
                    String first_name = scanner.nextLine();
                    System.out.print("Enter last name: ");
                    String last_name = scanner.nextLine();
                    User user = new User(first_name, last_name);
                    user.setId(this.user_service.create_user_id());
                    try {

                        this.user_service.addEntity(user);
                        System.out.println("User " + first_name + " " + last_name + " has been added succesfully!");
                        this.user_service.writeData();

                    }catch (ServiceException e){

                        System.out.println(e.toString());

                    }
                    catch (ValidateException e){

                        System.out.println(e.toString());

                    }
                    break;
                case 2:
                    System.out.print("Enter first name: ");
                    first_name = scanner.nextLine();
                    System.out.print("Enter last name: ");
                    last_name = scanner.nextLine();
                    user = new User(first_name, last_name);
                    try{

                        Long id = this.user_service.findUserByName(user);
                        if(id != null)
                        {
                            user.setId(id);
                            this.user_service.removeEntity(user);
                            this.friendship_service.removeUserFriendships(user);
                            this.community_service.removeUserFromCommunities(user.getId());
                            List<Friendship> friendships= this.friendship_service.removeUserFriendships(user);
                            for(Friendship friendship : friendships)
                                this.friendship_service.removeEntity(friendship);
                            this.user_service.writeData();
                        }

                    }catch (ServiceException e){

                        System.out.println(e.toString());

                    }
                    this.user_service.writeData();
                    break;
                case 3:
                    System.out.print("Enter first name: ");
                    first_name = scanner.nextLine();
                    System.out.print("Enter last name: ");
                    last_name = scanner.nextLine();
                    User oldUser = new User(first_name, last_name);
                    System.out.print("Enter updated first name: ");
                    String updated_first_name = scanner.nextLine();
                    System.out.print("Enter update last name: ");
                    String updated_last_name = scanner.nextLine();
                    User updatedUser = new User(updated_first_name, updated_last_name);
                    Long id = this.user_service.findUserByName(oldUser);
                    if(id != null)
                    {
                        updatedUser.setId(id);
                        this.user_service.updateEntity(updatedUser);
                    }
                    else
                    {
                        System.out.println(oldUser + " doesn't exist!");
                    }
                    break;

                case 4:
                    if(this.user_service.getRepoSize() == 0){

                        System.out.println("There are no users registered!");

                    }
                    else {
                        for (User u : this.user_service.getAll())
                            System.out.println(u.toString());
                        System.out.println();
                    }
                    break;
                case 5:
                    this.run();
                default:
                    System.out.println("Invalid option!");

            }
        }

    }
    public void run(){

        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose an option!");
        System.out.println("1.Users menu.");
        System.out.println("2.Friendships menu.");
        System.out.println("3.Communities menu.");
        System.out.println("4.Exit app.");
        System.out.print("Option: ");
        int command = scanner.nextInt();
        switch (command){

            case 1:
                this.UserMenu();
                break;
            case 2:
                this.FrienshipMenu();
                break;
            case 3:
                this.CommunityMenu();
                break;
            case 4:
                System.exit(0);
            default:
                System.out.println("Invalid option!");

        }

    }

}
