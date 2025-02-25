package src.lab7.domain.validators;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}