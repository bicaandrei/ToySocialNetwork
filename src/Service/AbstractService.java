package Service;

import Domain.Entity;
import Domain.Validators.Validator;
import Repository.AbstractFileRepository;
import Repository.InMemoryRepository;

public abstract class AbstractService<ID, E extends Entity<ID>> {

     private AbstractFileRepository<ID, E> repository;
     private Validator<E> validator;

     public AbstractService() {}
     public AbstractService(AbstractFileRepository<ID, E> repository, Validator<E> validator){

         this.repository = repository;
         this.validator = validator;

     }
     public abstract void addEntity(E entity);

     public abstract void removeEntity(E entity);

     public abstract void updateEntity(E entity);

     public abstract E findEntity(E entity);
     Iterable<E> getAll() { return this.repository.getAll(); }

}
