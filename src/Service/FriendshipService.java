package Service;

import Domain.Friendship;
import Domain.Graph;
import Domain.User;
import Domain.Validators.Validator;
import Repository.FrienshipFileRepository;
import Repository.UserFileRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FriendshipService extends AbstractService<Long, Friendship> {

    UserFileRepository user_repository;
    FrienshipFileRepository repository;

    Validator<Friendship> validator;

    Graph graph;

    public FriendshipService(UserFileRepository user_repository,FrienshipFileRepository repository, Validator<Friendship> validator) {

        super(repository, validator);
        this.user_repository = user_repository;
        this.repository = repository;
        this.validator = validator;
        this.graph = new Graph();

    }

    public void addEntity(Friendship friendship){

        this.validator.validate(friendship);
        if(this.user_repository.findOne(friendship.get_e1().getId()) == null)
            throw new ServiceException(friendship.get_e1().toString() + " nu este inregistrat!");
        if(this.user_repository.findOne(friendship.get_e2().getId()) == null)
            throw new ServiceException(friendship.get_e2().toString() + " nu este inregistrat!");
        this.repository.save(friendship);
        this.graph.addEdge(friendship.get_e1().getId(), friendship.get_e2().getId());

    }

    public void removeEntity(Friendship friendship){

        if(this.findEntity(friendship) != null)
            this.repository.delete(friendship.getId());

    }

    public void updateEntity(Friendship friendship){}

    public Friendship findEntity(Friendship friendship){

        if(this.repository.findOne(friendship.getId()).isPresent())
           return this.repository.findOne(friendship.getId()).get();
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
    public void loadFriendshipData(){

        this.repository.loadData();

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

        return this.repository.getAll();

    }
    public Iterable<Long> getKeys(){

        return this.repository.getKeys();

    }
    public Long create_friendship_id(){

        if(this.repository.getSize() == 0)
            return 1L;
        Long MaxKey = Long.MIN_VALUE;
        for(Long key : this.getKeys()){

            if(key > MaxKey)
                MaxKey = key;

        }

        return MaxKey+1;

    }
    public void writeData(){

        this.repository.clearFile();
        for(Friendship friendship : this.repository.getAll()){

            this.repository.writeToFile(friendship);

        }

    }
    public int getRepoSize(){

        return this.repository.getSize();

    }

}
