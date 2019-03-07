package domain;

import repository.HasID;

public class Profesor implements HasID<String> {

    private String nume;
    private String email;

    /*
    * Clasa constructor
    * @param nume-numele profesorului
    * @param email-email-ul profesorului
    * */

    public Profesor(String nume, String email) {
        this.nume = nume;
        this.email = email;
    }
    /*
    * returneaza numele profesorului
    * */
    public String getID() {
        return nume;
    }
    /*modifica numele profesorului
    @params nume- numele nou al profesorului
    * */
    public void setID(String nume) {
        this.nume = nume;
    }

    /*
    returneaza email-ul profesorului
    * */
    public String getEmail() {
        return email;
    }
    /*
    *modifica emailul profesorului
    * @params email- emailul nou al profesorului
     *  */
    public void setEmail(String email) {
        this.email = email;
    }
}
