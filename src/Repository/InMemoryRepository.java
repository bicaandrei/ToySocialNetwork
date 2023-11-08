package Repository;

import Domain.Entity;
import Domain.Validators.Validator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryRepository<Id, E extends Entity<Id>> implements RepositoryInterface<Id, E> {

    private Validator<E> validator;
    private Map<Id, E> entities;

    public InMemoryRepository(Validator<E> validator) {

        this.validator = validator;
        entities = new HashMap<Id, E>();

    }

    @Override
    public Optional<E> findOne(Id id){

        if (id==null)
            throw new IllegalArgumentException("Id must not be null!");
        if(entities.get(id) != null)
           return Optional.of(entities.get(id));
        else
           return null;
    }

    public Iterable<Id> getKeys() { return entities.keySet(); }

    public int getSize() { return entities.size(); }
    @Override
    public Iterable<E> getAll() { return entities.values(); }

    @Override
    public Optional<E> save(E entity) {

        if(entity==null)
            throw new IllegalArgumentException("Entity must not be null!");
        if(entities.get(entity.getId()) != null){

            return Optional.of(entity);

        }
        else entities.put(entity.getId(), entity);
        return null;

    }
    @Override
    public Optional<E> delete(Id id) {

        if(id == null)
            throw new IllegalArgumentException("Id must not be null!");
        if(entities.get(id) != null){

            E deleted_entity = entities.get(id);
            entities.remove(id);
            return Optional.of(deleted_entity);

        }
        return null;

    }

    @Override
    public Optional<E> update(E updated_entity){

        if(updated_entity == null)
            throw new IllegalArgumentException("Entity must not be null!");

        if(entities.get(updated_entity.getId())!=null){

            if(updated_entity.equals(entities.get(updated_entity.getId())) == true)
                return Optional.of(updated_entity);
            else {

                entities.put(updated_entity.getId(), updated_entity);
                return null;

            }
        }
        return null;

    }

    public void clearRepository(){

        this.entities.clear();

    }

}
