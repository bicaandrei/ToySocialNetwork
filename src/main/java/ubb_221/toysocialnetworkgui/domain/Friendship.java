package ubb_221.toysocialnetworkgui.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class Friendship extends Entity<Long>{
    LocalDate date;
    Tuple<User, User> friendship;
    public Friendship(User e1, User e2) {

        super();
        friendship = new Tuple(e1, e2);
        this.date = LocalDate.now();

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

    @Override
    public String toString(){

        return "Friendship#"+ super.id + ": " + this.friendship.toString() + " from: " + this.get_date_of_friendship().toString();

    }

}