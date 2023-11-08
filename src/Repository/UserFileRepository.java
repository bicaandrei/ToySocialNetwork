package Repository;

import Domain.User;
import Domain.Validators.Validator;

import javax.swing.text.Utilities;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserFileRepository extends AbstractFileRepository<Long, User>{

    Validator<User> validator;
    public UserFileRepository(String fileName, Validator<User> validator) { super(fileName, validator); this.validator = validator; }

    public User extractEntity(List<String> attributes){

        User user = new User((String) attributes.get(1), (String) attributes.get(2));
        user.setId(Long.parseLong((String)attributes.get(0)));
        return user;

    }

    protected String createEntityAsString(User entity){

        Object varId = entity.getId();
        return "" + varId + ";" + entity.get_first_name() + ";" + entity.get_last_name();

    }

}
