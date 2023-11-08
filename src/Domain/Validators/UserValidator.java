package Domain.Validators;

import Domain.User;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator implements Validator<User>{
    @Override
    public void validate(User entity) throws ValidateException {

        String errors = "";
           if(entity.getId() == null)
               errors += "Invalid id!\n";
           if(entity.get_first_name().isEmpty() == true)
               errors += "Invalid first name!\n";
           if(entity.get_last_name().isEmpty() == true)
               errors += "Invalid last name!\n";
        Pattern pattern = Pattern.compile("[^a-zA-z]");
        Matcher matcher = pattern.matcher(entity.get_first_name());
        if (matcher.find() == true)
            errors += "Invalid first name!\n";
        matcher = pattern.matcher(entity.get_last_name());
        if (matcher.find() == true)
            errors += "Invalid last name!\n";

        if(errors.length() > 0)
            throw new ValidateException(errors);
    }

}
