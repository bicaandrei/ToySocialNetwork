package Domain.Validators;

import Domain.Friendship;
import Domain.User;

public class FriendshipValidator implements Validator<Friendship> {

      Validator<User> userValidator;
      public FriendshipValidator(Validator<User> userValidator) { this.userValidator = userValidator; }
      @Override
      public void validate(Friendship friendship) throws ValidateException{

          if(friendship.getId() == null)
              throw new ValidateException("Invalid frienship id!");
          if(friendship.get_e1().getId() == friendship.get_e2().getId())
              throw new ValidateException("Users must be different!");
          this.userValidator.validate(friendship.get_e1());
          this.userValidator.validate(friendship.get_e2());

      }

}
