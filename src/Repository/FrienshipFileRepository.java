package Repository;

import Domain.Entity;
import Domain.Friendship;
import Domain.Tuple;
import Domain.User;
import Domain.Validators.Validator;

import java.util.List;

public class FrienshipFileRepository extends AbstractFileRepository<Long, Friendship>{

     public FrienshipFileRepository(String filename, Validator<Friendship> validator) { super(filename, validator); }

     public Friendship extractEntity(List<String> attributes){

          User user1 = new User((String) attributes.get(2), (String) attributes.get(3));
          user1.setId(Long.parseLong((String)attributes.get(1)));
          User user2 = new User((String) attributes.get(5), (String) attributes.get(6));
          user2.setId(Long.parseLong((String)attributes.get(4)));
          Friendship friendship = new Friendship(user1, user2);
          friendship.setId(Long.parseLong((String) attributes.get(0)));
          return friendship;

     }

     protected String createEntityAsString(Friendship entity){

          Object varId = entity.getId();
          return "" + varId + ";" + entity.get_e1().getId() + ";" + entity.get_e1().get_first_name() + ";" + entity.get_e1().get_last_name() + ";"

                  + entity.get_e2().getId() + ";" + entity.get_e2().get_first_name() + ";" + entity.get_e2().get_last_name();

     }
}