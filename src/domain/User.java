package domain;

import repository.HasID;

public class User implements HasID<String> {
    private String user;
    private String parola;
    private String tip;

    public User(){
        this.user="";
        this.parola="";
        this.tip="";
    }
    /*
     * Class Constructor
     * @param user - username-ul utilizatorului
     * @param parola - parola utilizatorului
     * @param tip - tipul utilizatorului:admin,profesor,student
     * @param email - emailul unui student
    */
    public User(String user, String parola, String tip) {
        this.user = user;
        this.parola = parola;
        this.tip = tip;
    }
    /*
    * returneaza user-ul
    * */
    public String getID() {
        return user;
    }
    /*
    * modifica user-ul
    *  @param ID - noul username
    * */
    public void setID(String user) {
        this.user = user;
    }
    /*
    * returneaza parola
    * */
    public String getParola() {
        return parola;
    }
    /*
    *  modifica parola-ul
     *  @param parola - noua parola
    * */
    public void setParola(String parola) {
        this.parola = parola;
    }
    /*
    * returneaza tipul utilizatorului
    * */

    public String getTip() {
        return tip;
    }
    /* modifica tipul
       * @tip-noul tip
     */
    public void setTip(String tip) {
        this.tip = tip;
    }
    /**
     * @return un obiect de tip User sub forma de string
     */
    public String toString() {
        return user + "," + parola + "," + tip ;
    }

}
