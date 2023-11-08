package Domain;

import java.time.LocalDateTime;

public class Friendship extends Entity<Long>{
    LocalDateTime date;
    Tuple<User, User> friendship;
    public Friendship(User e1, User e2) {

        super();
        friendship = new Tuple(e1, e2);
        this.date = LocalDateTime.now();

    }

    public User get_e1() { return this.friendship.get_e1(); }

    public User get_e2() { return this.friendship.get_e2(); }
    /**
     *
     * @return the date when the friendship was created
     */
    public LocalDateTime get_date_of_friendship() {
        return date;
    }

    @Override
    public String toString(){

        return "Friendship#"+ super.id + ": " + this.friendship.toString();

    }

}
