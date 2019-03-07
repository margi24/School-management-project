package validation;

import domain.Profesor;

public class ProfValidator implements Validator<Profesor> {

    /**
     * Valideaza un user
     * @param entity - profesorul pe care il va valida
     * @throws ValidationException daca profesorul nu e valid
     */
    @Override
    public void validate(Profesor entity) throws ValidationException {
        if(entity.getID().equals("") || entity.getID() == null) {
            throw new ValidationException("Nume incorect");
        }
        if(entity.getEmail().equals("")){
            throw new ValidationException("Parola incorecta!");
        }

    }
}
