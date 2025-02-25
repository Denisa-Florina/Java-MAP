package src.lab7.domain.validators;

import src.lab7.domain.Friendship;

public class ValidationFriendship implements Validator<Friendship> {

    @Override
    public void validate(Friendship entity) throws ValidationException {
        if (entity == null) {
            throw new ValidationException("Prietenia nu poate fi null.\n");
        }

        if (entity.getUserId1() == null || entity.getUserId2() == null) {
            throw new ValidationException("Utilizatorii nu au fost gasiti.\n");
        }

        if (entity.getUserId1().equals(entity.getUserId2())) {
            throw new ValidationException("Un utilizator nu poate fi prieten cu el insusi.\n");
        }
    }
    }
