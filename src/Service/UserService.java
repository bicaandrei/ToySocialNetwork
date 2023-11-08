package Service;

import Domain.User;
import Domain.Validators.Validator;
import Repository.InMemoryRepository;
import Repository.UserFileRepository;

public class UserService extends AbstractService<Long, User> {

    UserFileRepository repository;
    Validator<User> validator;

    public UserService(UserFileRepository repository, Validator<User> validator){

        super(repository, validator);
        this.repository = repository;
        this.validator = validator;

    }

    public void addEntity(User user){

        if(this.findUserByName(user) == null) {

            this.validator.validate(user);
            this.repository.save(user);

        }
        else{

            throw new ServiceException("User already exists!");

        }

    }

    public void removeEntity(User user){

        this.repository.delete(user.getId());

    }

    public void updateEntity(User user){

        this.validator.validate(user);
        this.repository.update(user);

    }

    public User findEntity(User user){

        if(this.repository.findOne(user.getId()).isPresent())
           return this.repository.findOne(user.getId()).get();
        else
           return null;
    }

    public User findUserById(Long id){

        if(this.repository.findOne(id).isPresent())
            return this.repository.findOne(id).get();
        else
            return null;
    }

    public Long findUserByName(User user){

        for(User u : this.getAll()){

            if(user.hashCode() == u.hashCode())
            {
                return u.getId();
            }

        }
        return null;

    }

    public void loadUserData(){

        this.repository.loadData();

    }

    public Iterable<User> getAll(){

        return this.repository.getAll();

    }

    public Iterable<Long> getKeys(){

        return this.repository.getKeys();

    }
    public Long create_user_id(){

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
        for(User user : this.repository.getAll()){

            this.repository.writeToFile(user);

        }

    }

    public int getRepoSize(){

        return this.repository.getSize();

    }

}
