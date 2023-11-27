package ubb_221.toysocialnetworkgui.service;

import ubb_221.toysocialnetworkgui.domain.*;
import ubb_221.toysocialnetworkgui.domain.Validators.*;
import ubb_221.toysocialnetworkgui.repository.*;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class FriendshipService{

    DBRepository db_user_repository;
    FriendshipDBRepository db_repository;
    Validator<Friendship> validator;

    public FriendshipService(DBRepository db_user_repository, FriendshipDBRepository db_repository, Validator<Friendship> validator){

        this.db_user_repository = db_user_repository;
        this.db_repository = db_repository;
        this.validator = validator;

    }

    public void addEntity(Friendship friendship){

        this.validator.validate(friendship);
        if(this.db_user_repository.findOne(friendship.get_e1().getId()) == null)
            throw new ServiceException(friendship.get_e1().toString() + " nu este inregistrat!");
        if(this.db_user_repository.findOne(friendship.get_e2().getId()) == null)
            throw new ServiceException(friendship.get_e2().toString() + " nu este inregistrat!");
        this.db_repository.save(friendship);

    }

    public void removeEntity(Friendship friendship){

        if(this.findEntity(friendship) != null) {

            this.db_repository.delete(friendship.getId());

        }

    }

    public void updateEntity(Friendship friendship){}

    public Friendship findEntity(Friendship friendship){

        if(this.db_repository.findOne(friendship.getId()) != null)
           return this.db_repository.findOne(friendship.getId());
        else
           return null;

    }
    public Long findFriendshipByNames(Friendship friendship){

        for(Friendship f : this.getAll()){

            if(f.get_e1().hashCode() == friendship.get_e1().hashCode() && f.get_e2().hashCode() == friendship.get_e2().hashCode())
            {
                return f.getId();
            }

        }
        return null;

    }
    public List<Friendship> removeUserFriendships(User user){

        List<Friendship> friendships = new LinkedList<Friendship>();
        for(Friendship friendship : this.getAll()){

            if(friendship.get_e1().hashCode() == user.hashCode() || friendship.get_e2().hashCode() == user.hashCode())
                friendships.add(friendship);

        }

        return friendships;

    }
    public Iterable<Friendship> getAll(){

        return this.db_repository.getAll();

    }
    public Long create_friendship_id(){

        Long MaxID = 0L;
        for(Friendship friendship : this.getAll()){

            if(friendship.getId() > MaxID)
                MaxID = friendship.getId();

        }

        return MaxID+1;

    }
    public List<Tuple<User, LocalDate>> getFriendshipsOfUser(User user, String month){

        List<Friendship> friendships = new LinkedList<Friendship>();
        this.getAll().forEach(friendships::add);
        List<Friendship> friendshipsOfUser = friendships.stream().filter(friendship -> (friendship.get_e1().getId() == user.getId() || friendship.get_e2().getId() == user.getId()) &&
                friendship.get_date_of_friendship().toString().split("-")[1].equals(month)).collect(Collectors.toList());
        List<Tuple<User, LocalDate>> friends = new LinkedList<Tuple<User, LocalDate>>();
        for(Friendship friendship : friendshipsOfUser)
        {
            Tuple<User, LocalDate> friendDate = new Tuple<User, LocalDate>();
            if(friendship.get_e1().getId() != user.getId())
            {
                friendDate.set_e1(friendship.get_e1());
                friendDate.set_e2(friendship.get_date_of_friendship());
            }
            else {
                friendDate.set_e1(friendship.get_e2());
                friendDate.set_e2(friendship.get_date_of_friendship());
            }
            friends.add(friendDate);
        }
        return friends;

    }
    public int getRepoSize(){

        int count = 0;
        for(Friendship friendship : this.getAll())
        {
            count += 1;
        }

        return count;

    }

}
