package ubb_221.toysocialnetworkgui.service;


import ubb_221.toysocialnetworkgui.domain.*;
import ubb_221.toysocialnetworkgui.repository.paging.*;
import ubb_221.toysocialnetworkgui.repository.*;
import ubb_221.toysocialnetworkgui.utils.events.ChangeEventType;
import ubb_221.toysocialnetworkgui.utils.events.UserChangeEvent;
import ubb_221.toysocialnetworkgui.utils.observer.Observable;
import ubb_221.toysocialnetworkgui.utils.observer.Observer;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UserService implements Observable<UserChangeEvent> {
    private PagingRepository<Long, User> repo;
    private Integer pages;
    public UserService(PagingRepository<Long, User> repo, Integer pages)
    {
        this.repo = repo;
        this.pages = pages;
    }

    private <T> Iterable <T> filter(Iterable <T> list, Predicate<T> cond)
    {
        List<T> rez=new ArrayList<>();
        list.forEach((T x)->{if (cond.test(x)) rez.add(x);});
        return rez;
    }

    public User findUser(User user){

        User u = repo.findOne(user.getId());
        return u;

    }

    public User findUserByUsername(String username){

        for(User u : this.getAll()){

            if(u.getUsername().equals(username))
                return u;

        }
        return null;

    }
    public User addUser(User user) {

        User u = repo.save(user);
        if(u == null) {
            notifyObservers(new UserChangeEvent(ChangeEventType.ADD, user));
        }
        return u;
    }

    public User deleteUser(User u){
        User user=repo.delete(u.getId());
        if(user!=null) {
            notifyObservers(new UserChangeEvent(ChangeEventType.DELETE, user));
        }
        return user;
    }

    public User updateUser(User newUser) {
        User oldUser=repo.findOne(newUser.getId());
        if(oldUser!=null) {
            User res=repo.update(newUser);
            notifyObservers(new UserChangeEvent(ChangeEventType.UPDATE, newUser, oldUser));
            return res;
        }
        return oldUser;
    }

    public Iterable<User> getAll(){

        return repo.getAll();

    }

    public List<User> getAllExceptOne(User user){

        List<User> usersExceptOne = new ArrayList<User>();
        List<User> users = StreamSupport.stream(getAll().spliterator(), false)
                .collect(Collectors.toList());
        for(User u : users)
        {
            if(u.getId() != user.getId())
                usersExceptOne.add(u);
        }
        return usersExceptOne;

    }
    private List<Observer<UserChangeEvent>> observers=new ArrayList<>();

    @Override
    public void addObserver(Observer<UserChangeEvent> e) {
        observers.add(e);

    }

    @Override
    public void removeObserver(Observer<UserChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(UserChangeEvent t) {
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

    public Set<User> getNextMessages() {
//        Pageable pageable = new PageableImplementation(this.page, this.size);
//        Page<MessageTask> studentPage = repo.findAll(pageable);
//        this.page++;
//        return studentPage.getContent().collect(Collectors.toSet());
        this.page++;
        return getMessagesOnPage(this.page);
    }

    public Set<User> getMessagesOnPage(int page) {
        this.page=page;
        Pageable pageable = new PageableImplementation(page, this.size);
        Page<User> studentPage = repo.findAll(pageable);
        return studentPage.getContent().collect(Collectors.toSet());
    }

}
