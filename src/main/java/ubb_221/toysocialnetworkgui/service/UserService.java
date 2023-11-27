package ubb_221.toysocialnetworkgui.service;


import ubb_221.toysocialnetworkgui.domain.*;
import ubb_221.toysocialnetworkgui.repository.paging.*;
import ubb_221.toysocialnetworkgui.repository.*;
import ubb_221.toysocialnetworkgui.utils.events.ChangeEventType;
import ubb_221.toysocialnetworkgui.utils.events.UserChangeEvent;
import ubb_221.toysocialnetworkgui.utils.observer.Observable;
import ubb_221.toysocialnetworkgui.utils.observer.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class UserService implements Observable<UserChangeEvent> {
    private PagingRepository<Long, User> repo;

    public UserService(PagingRepository<Long, User> repo) {
        this.repo = repo;
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
    private int size = 1;

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
