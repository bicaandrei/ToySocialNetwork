package ubb_221.toysocialnetworkgui.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class Friendship extends Entity<Long>{
    LocalDate date;
    Tuple<User, User> friendship;
    private String username1;
    private String username2;
    private String status;
    public Friendship(User e1, User e2) {

        super();
        friendship = new Tuple(e1, e2);
        this.date = LocalDate.now();
        this.status = "PENDING";
        this.username1 = e1.getUsername();
        this.username2 = e2.getUsername();

    }

    public User get_e1() { return this.friendship.get_e1(); }

    public User get_e2() { return this.friendship.get_e2(); }
    /**
     *
     * @return the date when the friendship was created
     */
    public void set_date_of_friendship(LocalDate date) { this.date = date; }
    public LocalDate get_date_of_friendship() {
        return date;
    }

    public void set_status(String status){ this.status = status; }

    public String getUsername1() { return this.username1; }

    public String getUsername2() { return this.username2; }
    public String getStatus() { return this.status; }

    public boolean equals(Friendship other){

        if(this == other) return true;
        //if(!(o instanceof User)) return false;
        //User other = (User) o;
        Boolean of_users = (this.get_e1().equals(other.get_e1()) && this.get_e2().equals(other.get_e2())) || (this.get_e1().equals(other.get_e2()) && this.get_e2().equals(other.get_e1()));
        return (of_users && (this.date.isEqual(other.date)) && (this.status == other.status));

    }

    @Override
    public String toString(){

        return "Friendship#"+ super.id + ": " + this.friendship.toString() + " from: " + this.get_date_of_friendship().toString() + " status: " + this.getStatus();

    }

}