package ubb_221.toysocialnetworkgui.service;

import ubb_221.toysocialnetworkgui.domain.*;
import ubb_221.toysocialnetworkgui.domain.Validators.*;
import ubb_221.toysocialnetworkgui.repository.*;
import ubb_221.toysocialnetworkgui.repository.paging.*;
import ubb_221.toysocialnetworkgui.utils.events.FriendshipsChangeEvent;
import ubb_221.toysocialnetworkgui.utils.events.UserChangeEvent;
import ubb_221.toysocialnetworkgui.utils.observer.Observable;
import ubb_221.toysocialnetworkgui.utils.observer.Observer;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FriendshipService implements Observable<FriendshipsChangeEvent> {
    private PagingRepository<Long, User> user_repo;
    private PagingRepository<Long, Friendship> frienship_repo;
    Validator<Friendship> validator;

    private Integer pages;
    public FriendshipService(PagingRepository<Long, User> user_repo, PagingRepository<Long, Friendship> friendship_repo, Validator<Friendship> validator, Integer pages) {

         this.user_repo = user_repo;
         this.frienship_repo = friendship_repo;
         this.validator = validator;
         this.pages = pages;

    }

    private <T> Iterable <T> filter(Iterable <T> list, Predicate<T> cond)
    {
        List<T> rez=new ArrayList<>();
        list.forEach((T x)->{if (cond.test(x)) rez.add(x);});
        return rez;
    }

    public void addEntity(Friendship friendship){

        this.validator.validate(friendship);
        if(this.user_repo.findOne(friendship.get_e1().getId()) == null)
            throw new ServiceException(friendship.get_e1().toString() + " nu este inregistrat!");
        if(this.user_repo.findOne(friendship.get_e2().getId()) == null)
            throw new ServiceException(friendship.get_e2().toString() + " nu este inregistrat!");
        this.frienship_repo.save(friendship);

    }

    public void removeEntity(Friendship friendship){

        if(this.findEntity(friendship) != null) {

            this.frienship_repo.delete(friendship.getId());

        }

    }

    public void updateEntity(Friendship friendship){

        this.frienship_repo.update(friendship);

    }

    public Friendship findEntity(Friendship friendship){

        if(this.frienship_repo.findOne(friendship.getId()) != null)
           return this.frienship_repo.findOne(friendship.getId());
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

        return this.frienship_repo.getAll();

    }
    public Long create_friendship_id(){

        Long MaxID = 0L;
        for(Friendship friendship : this.getAll()){

            if(friendship.getId() > MaxID)
                MaxID = friendship.getId();

        }

        return MaxID+1;

    }

    public List<Friendship> getFriendshipsOfUser(User user){

        List<Friendship> friendships = new LinkedList<Friendship>();
        this.getAll().forEach(friendships::add);
        List<Friendship> friends = new LinkedList<Friendship>();
        for(Friendship friendship : friendships)
        {
            if(friendship.get_e1().getId() == user.getId())
                friends.add(friendship);
            if(friendship.get_e2().getId() == user.getId())
                friends.add(friendship);

        }
        return friends;

    }

    public List<User> getFriendsOfUser(User user){

        List<Friendship> friendships = new LinkedList<Friendship>();
        this.getAll().forEach(friendships::add);
        List<User> friends = new LinkedList<User>();
        for(Friendship friendship : friendships)
        {
            if(friendship.getStatus().equals("APPROVED")) {
                if (friendship.get_e1().getId() == user.getId())
                    friends.add(friendship.get_e2());
                else if (friendship.get_e2().getId() == user.getId())
                    friends.add(friendship.get_e1());
            }
        }
        return friends;

    }

    public List<User> getNonFriendsOfUser(User user){

        List<User> nonfriends = new ArrayList<>();
        List<Long> friends = new ArrayList<>();
        for (User friend: this.getFriendsOfUser(user)) {
            friends.add(friend.getId());
        }
        for(User nonfriend: this.user_repo.getAll()){
            if(nonfriend.getId() != user.getId() && friends.contains(nonfriend.getId()) == false)
                nonfriends.add(nonfriend);
        }

        return nonfriends;

    }
    public List<Tuple<User, LocalDate>> getFriendshipsOfUserInMonth(User user, String month){

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

    private List<Observer<FriendshipsChangeEvent>> observers=new ArrayList<>();

    @Override
    public void addObserver(Observer<FriendshipsChangeEvent> e) {
        observers.add(e);

    }

    @Override
    public void removeObserver(Observer<FriendshipsChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(FriendshipsChangeEvent t) {
        observers.stream().forEach(x->x.update(t));
    }

    private int page = 0;
    private int size = 4;

    private Pageable pageable;

    public void setPageSize(int size) {
        this.size = size;
    }

//    public void setPageable(Pageable pageable) {
//        this.pageable = pageable;
//    }

    public Set<Friendship> getNextMessages() {
//        Pageable pageable = new PageableImplementation(this.page, this.size);
//        Page<MessageTask> studentPage = repo.findAll(pageable);
//        this.page++;
//        return studentPage.getContent().collect(Collectors.toSet());
        this.page++;
        return getMessagesOnPage(this.page);
    }

    public Set<Friendship> getMessagesOnPage(int page) {
        this.page=page;
        Pageable pageable = new PageableImplementation(page, this.size);
        Page<Friendship> studentPage = frienship_repo.findAll(pageable);
        return studentPage.getContent().collect(Collectors.toSet());
    }

    public Page<User> findAllFriends(Pageable pageable, User user) {
        Paginator<User> paginator = new Paginator<User>(pageable, this.getFriendsOfUser(user));
        return paginator.paginate();
    }
    public Set<User> getFriendsOnPage(int page, User user) {
        this.page=page;
        Pageable pageable = new PageableImplementation(page, this.size);
        Page<User> studentPage = this.findAllFriends(pageable, user);
        return studentPage.getContent().collect(Collectors.toSet());
    }

    public Page<Friendship> findAllFriendships(Pageable pageable, User user) {
        Paginator<Friendship> paginator = new Paginator<Friendship>(pageable, this.getFriendshipsOfUser(user));
        return paginator.paginate();
    }
    public Set<Friendship> getFriendshipsOnPage(int page, User user) {
        this.page=page;
        Pageable pageable = new PageableImplementation(page, this.size);
        Page<Friendship> studentPage = this.findAllFriendships(pageable, user);
        return studentPage.getContent().collect(Collectors.toSet());
    }
}
