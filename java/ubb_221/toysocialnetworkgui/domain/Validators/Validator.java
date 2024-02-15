package ubb_221.toysocialnetworkgui.domain.Validators;

@FunctionalInterface
public interface Validator<E> {
    void validate(E entity) throws ValidateException;

}
