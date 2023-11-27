package ubb_221.toysocialnetworkgui.domain;

import java.util.List;
import java.util.Objects;

public class User extends Entity<Long>{

    private String first_name;
    private String last_name;
    private List<User> friends;
    public User(String first_name, String last_name){

        this.first_name = first_name;
        this.last_name = last_name;

    }
    public void set_first_name(String new_first_name) { this.first_name = new_first_name; }

    public void set_last_name(String new_last_name) { this.last_name = new_last_name; }

    public String getFirst_name() { return first_name; }

    public String getLast_name() { return last_name; }

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
        return (this.first_name == other.first_name) && (this.last_name == other.last_name);

    }

    @Override
    public int hashCode() { return Objects.hash(this.first_name, this.last_name); }

}