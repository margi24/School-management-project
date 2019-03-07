package validation;

import domain.User;

public class UserValidator implements Validator<User> {
    /**
     * Valideaza un user
     * @param entity - userul pe care il va valida
     * @throws ValidationException daca userul nu e valid
     */
    @Override
    public void validate(User entity) throws ValidationException {
        if(entity.getID().equals("") || entity.getID() == null) {
            throw new ValidationException("User incorect");
        }
        if(entity.getParola().equals("")){
            throw new ValidationException("Parola incorecta!");
        }
        if(!entity.getTip().equals("admin") && !entity.getTip().equals("student") && !entity.getTip().equals("profesor")){
            throw new ValidationException("Tip incorect!");
        }
    }
}
