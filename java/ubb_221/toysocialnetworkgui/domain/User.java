package ubb_221.toysocialnetworkgui.domain;

import java.util.*;

public class User extends Entity<Long>{

    private String first_name;
    private String last_name;
    private String username;
    private String password;
    private String unCryptedPassword;
    private Boolean loggedIn;
    private List<User> friends;

    public User(String first_name, String last_name){

        this.first_name = first_name;
        this.last_name = last_name;
        this.friends = new ArrayList<>();

    }
    public void set_first_name(String new_first_name) { this.first_name = new_first_name; }

    public void set_last_name(String new_last_name) { this.last_name = new_last_name; }

    public String getFirst_name() { return first_name; }

    public String getLast_name() { return last_name; }

    public void setUsername(String username) { this.username = username; }

    public void setPassword(String password) { this.password = password; }

    public String getUsername() { return this.username; }

    public String getPassword() { return this.password; }
    public void setLoggedIn(Boolean loggedIn) { this.loggedIn = loggedIn; }

    public String getUnCryptedPassword() { return this.unCryptedPassword; }
    public void setUnCryptedPassword(String cryptedPassword) { this.unCryptedPassword = cryptedPassword; }
    public Boolean getLoggedIn() { return this.loggedIn; }

    public void addFriend(User friend) { this.friends.add(friend); }
    public List<User> get_friends() { return this.friends; }

    @Override
    public String toString(){

        String user_info = "User#" + super.getId() + ": " + first_name + " " + last_name;
        /*
        if(this.friends.size() == 0)
           user_info = user_info + " nu are prieteni.";
        else
           user_info = user_info + " are prietenii: " + friends;
        */
        return user_info;

    }

    //@Override
    public boolean equals(User other){

        if(this == other) return true;
        //if(!(o instanceof User)) return false;
        //User other = (User) o;
        return (this.getFirst_name() == other.getFirst_name()) && (this.getLast_name() == other.getLast_name());

    }

    @Override
    public int hashCode() { return Objects.hash(this.first_name, this.last_name, this.username, this.password); }

}