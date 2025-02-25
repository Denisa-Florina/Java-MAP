package src.lab7.domain.validators;


import src.lab7.domain.Utilizator;

public class UtilizatorValidator implements Validator<Utilizator> {
    @Override
    public void validate(Utilizator entity) throws ValidationException {
        if(entity.getFirstName().equals(""))
            throw new ValidationException("Utilizatorul nu este valid");
    }
}
