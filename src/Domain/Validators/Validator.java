package Domain.Validators;

@FunctionalInterface
public interface Validator<E> {
    void validate(E entity) throws ValidateException;

}
