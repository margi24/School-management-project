package service;

import curent.Curent;
import domain.*;
import org.mindrot.jbcrypt.BCrypt;
import repository.*;
import utils.ChangeEventType;
import utils.Observable;
import utils.Observer;
import validation.*;
import view.NotaEvent;
import view.ProfEvent;
import view.StudentEvent;
import view.TemaEvent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * Clasa Service
 */
public class Service {
    private StudentValidator studentValidator;
    private TemaValidator temaValidator;
    private NotaValidator notaValidator;
    private UserValidator userValidator;
    private ProfValidator profValidator;
    private XMLRepositoryTema temaFileRepository;
    private XMLRepositoryNota notaFileRepository;
    private XMLRepositoryStudent studentFileRepository;
    private XMLRepositoryUser userFileRepository;
    private XMLRepositoryProf profFileRepository;
    private ArrayList<Observer<StudentEvent>> studentObservers;
    private ArrayList<Observer<NotaEvent>> notaObservers;
    private ArrayList<Observer<TemaEvent>> temaObservers;
    private ArrayList<Observer<ProfEvent>> profObservers;

    private Map<String, String> useriSt;
    private Map<String, String> useriPr;

    /**
     * Class Constructor
     *
     * @param studentFileRepository - repository student
     * @param studentValidator      - validator student
     * @param temaFileRepository    - repository tema
     * @param temaValidator         - validator tema
     * @param notaFileRepository    - repository nota
     * @param notaValidator         - validator nota
     * @param userValidator         - validator user
     * @param xmlRepositoryUser     - repository user
     * @param profFileRepository    - repository profesor
     * @param profValidator         - validator profesor
     */
    public Service(XMLRepositoryStudent studentFileRepository, StudentValidator studentValidator, XMLRepositoryTema temaFileRepository,
                   TemaValidator temaValidator, XMLRepositoryNota notaFileRepository, NotaValidator notaValidator, XMLRepositoryUser xmlRepositoryUser,
                   UserValidator userValidator, XMLRepositoryProf profFileRepository, ProfValidator profValidator) {
        this.studentFileRepository = studentFileRepository;
        this.studentValidator = studentValidator;
        this.temaFileRepository = temaFileRepository;
        this.temaValidator = temaValidator;
        this.notaFileRepository = notaFileRepository;
        this.notaValidator = notaValidator;
        this.userFileRepository = xmlRepositoryUser;
        this.userValidator = userValidator;
        this.profFileRepository = profFileRepository;
        this.profValidator = profValidator;
        this.studentObservers = new ArrayList<>();
        this.notaObservers = new ArrayList<>();
        this.temaObservers = new ArrayList<>();
        this.profObservers = new ArrayList<>();


    }

    public Observable<StudentEvent> studentEventObservable = new Observable<StudentEvent>() {
        @Override
        public void addObserver(Observer<StudentEvent> e) {
            studentObservers.add(e);
        }

        @Override
        public void removeObserver(Observer<StudentEvent> e) {
            studentObservers.remove(e);
        }

        @Override
        public void notifyObservers(StudentEvent studentEvent) {
            studentObservers.forEach(e -> e.update(studentEvent));
        }
    };

    public Observable<TemaEvent> temaEventObservable = new Observable<TemaEvent>() {
        @Override
        public void addObserver(Observer<TemaEvent> e) {
            temaObservers.add(e);
        }

        @Override
        public void removeObserver(Observer<TemaEvent> e) {
            temaObservers.remove(e);
        }

        @Override
        public void notifyObservers(TemaEvent temaEvent) {
            temaObservers.forEach(e -> e.update(temaEvent));
        }
    };
    public Observable<NotaEvent> notaEventObservable = new Observable<NotaEvent>() {
        @Override
        public void addObserver(Observer<NotaEvent> e) {
            notaObservers.add(e);
        }

        @Override
        public void removeObserver(Observer<NotaEvent> e) {
            notaObservers.remove(e);
        }

        @Override
        public void notifyObservers(NotaEvent notaEvent) {
            notaObservers.forEach(e -> e.update(notaEvent));
        }
    };
    public Observable<ProfEvent> profEventObservable = new Observable<ProfEvent>() {
        @Override
        public void addObserver(Observer<ProfEvent> e) {
            profObservers.add(e);
        }

        @Override
        public void removeObserver(Observer<ProfEvent> e) {
            profObservers.remove(e);
        }

        @Override
        public void notifyObservers(ProfEvent notaEvent) {
            profObservers.forEach(e -> e.update(notaEvent));
        }
    };


    /**
     * adauga un Student in memorie
     *
     * @param student - studentul pe care il adauga
     * @return null daca studentul a fost adaugat cu succes sau studentul din memorie daca acesta exista deja
     */
    public Student addStudent(Student student) {
        if (findStudent(student.getID()) != null) {
            throw new ValidationException("student existent");
        }
        studentValidator.validate(student);
        studentEventObservable.notifyObservers(new StudentEvent(null, student, ChangeEventType.ADD));
        return studentFileRepository.save(student);
    }

    /**
     * Sterge un student
     *
     * @param id - id-ul studentului
     * @return studentul daca acesta a fost sters sau null daca studentul nu exista
     */
    public Optional<Student> deleteStudent(String id) {
        if (id == null || id.equals("")) {
            throw new ValidationException("Id-ul nu poate fi null!");
        }
        Student student = findStudent(id);
        studentEventObservable.notifyObservers(new StudentEvent(student, null, ChangeEventType.DELETE));
        return studentFileRepository.delete(id);
    }

    /**
     * Cauta un student dupa id
     *
     * @param id - id-ul studentului
     * @return studentul daca acesta exista sau null altfel
     */
    public Student findStudent(String id) {
        if (id == null || id.equals("")) {
            throw new ValidationException("Id-ul nu poate fi null!");
        }
        return studentFileRepository.findOne(id);
    }


    public Student findStudentDupaNume(String nume) {
        if (nume == null || nume.equals("")) {
            throw new ValidationException("Nume incorect");
        }
        Student student = new Student("19999", "19999", 19999, "199999");
        for (Student s : this.getAllStudenti()) {
            if (s.getNume().equals(nume)) {
                student.setEmail(s.getEmail());
                student.setGrupa(s.getGrupa());
                student.setNume(s.getNume());
                student.setID(s.getID());
            }
        }
        if (!student.getID().equals("19999"))
            return student;
        else
            throw new ValidationException("Student inexistent");
    }

    public Student findStudentDupaEmail(String nume) {
        if (nume == null || nume.equals("")) {
            throw new ValidationException("Email incorect");
        }
        Student student = new Student("19999", "19999", 19999, "199999");
        for (Student s : this.getAllStudenti()) {
            if (s.getEmail().equals(nume)) {
                student.setEmail(s.getEmail());
                student.setGrupa(s.getGrupa());
                student.setNume(s.getNume());
                student.setID(s.getID());
            }
        }
        if (!student.getID().equals("19999"))
            return student;
        else
            throw new ValidationException("Email negasit");
    }

    /**
     * Modifica un student
     *
     * @param student - noul student
     * @return noul student daca s-a facut modificarea sau null daca acesta nu exista
     */
    public Optional<Student> updateStudent(Student student) {
        studentValidator.validate(student);
        Student student1 = studentFileRepository.findOne(student.getID());
        studentEventObservable.notifyObservers(new StudentEvent(student1, student, ChangeEventType.UPDATE));
        return studentFileRepository.update(student);
    }

    /**
     * @return toti studentii din memorie
     */
    public Iterable<Student> getAllStudenti() {
        return studentFileRepository.findAll();
    }

    /**
     *
     * @return lista cu numele studentilor
     */
    public ArrayList<String> getNumeStudenti() {

        ArrayList<String> listaNume = new ArrayList<>();
        for (Student studenti : this.getAllStudenti()) {
            listaNume.add(studenti.getNume());
        }
        return listaNume;
    }

    /**
     *
     * @return nr de grupe
     */
    public List<String> getNrGrupe() {

        List<String> listaNume = new ArrayList<>();
        List<String> listaNume2 = new ArrayList<>();
        for (Student studenti : this.getAllStudenti()) {
            listaNume.add(String.valueOf(studenti.getGrupa()));
        }
        listaNume2 = listaNume.stream().distinct().collect(Collectors.toList());
        return listaNume2;
    }

    /**
     * Adauga o tema noua
     *
     * @param tema - tema pe care o adauga
     * @return null daca s-a facut adaugarea sau tema daca aceasta exista deja
     */

    public Tema addTema(Tema tema) {
        if (findTema(tema.getID()) != null) {
            throw new ValidationException("Id-ul exista deja");
        }
        temaValidator.validate(tema);
        ;
        temaEventObservable.notifyObservers(new TemaEvent(null, tema, ChangeEventType.ADD));
        return temaFileRepository.save(tema);
    }

    /**
     * Sterge o tema
     *
     * @param nrTema - nr-ul temei
     * @return tema daca aceasta a fost stearsa sau null daca tema nu exista
     */
    public Optional<Tema> deleteTema(String nrTema) {
        if (nrTema == null || nrTema.equals("")) {
            throw new ValidationException("Id-ul nu poate fi null!");
        }
        temaEventObservable.notifyObservers(new TemaEvent(findTema(nrTema), null, ChangeEventType.DELETE));

        return temaFileRepository.delete(nrTema);
    }

    /**
     *
     * @return numarul de studenti
     */
    public Integer sizeSt() {
        return this.getStudentiLista().size();
    }

    /**
     *
     * @return numarul de teme
     */

    public Integer sizeT() {
        return this.getTemeLista().size();
    }

    /**
     *
     * @return numarul temelor
     */
    public Integer sizeN() {
        return this.getNoteLista().size();
    }


    /**
     * Cauta o tema
     *
     * @param id - id-ul temei
     * @return tema sau null daca aceasta nu exista
     */
    public Tema findTema(String id) {
        if (id == null || id.equals("")) {
            throw new ValidationException("Id-ul nu poate fi null!");
        }
        return temaFileRepository.findOne(id);
    }

    /**
     *
     * @param nume  - numele temei cautate
     * @return  se returneaza tema cu numele nume sau arunca eroare in caz contrar
     */
    public Tema findTemaDupaDescriere(String nume) {
        if (nume == null || nume.equals("")) {
            throw new ValidationException("Descriere incorecta");
        }
        Tema tema = new Tema("38129", "6423879", 12, 11);
        for (Tema s : this.getAllTeme()) {
            if (s.getDescriere().equals(nume)) {
                tema.setDescriere(s.getDescriere());
                tema.setDeadline(s.getDeadline());
                tema.setPrimire(s.getPrimire());
                tema.setID(s.getID());
            }
        }
        if (!tema.getID().equals("38129"))
            return tema;
        else
            throw new ValidationException("Tema nu exista");
    }

    /**
     * Modifica o tema
     *
     * @param tema - noua tema
     * @return tema daca s-a facut modificarea sau null daca acesta nu exisra
     */
    public Optional<Tema> updateTema(Tema tema) {
        temaValidator.validate(tema);
        Tema temaAct = findTema(tema.getID());
        temaEventObservable.notifyObservers(new TemaEvent(tema, temaAct, ChangeEventType.UPDATE));

        return temaFileRepository.update(tema);
    }

    /**
     * @return toate temele din memorie
     */
    public Iterable<Tema> getAllTeme() {
        return temaFileRepository.findAll();
    }

    /**
     *
     * @return o lista formata din numele temelor
     */
    public List<String> getNumeTeme() {
        List<String> numeTeme = new ArrayList<>();
        for (Tema tema : this.getAllTeme()) {
            numeTeme.add(tema.getDescriere());
        }
        return numeTeme;
    }
    /*
    public Double getPctPenalizari(String note){
        Double n=0.0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate nota = LocalDate.parse(note, formatter);
        int predare = calculeazaSPredare(nota);
        Tema tema = temaFileRepository.findOne(nota);
        if (predare != tema.getDeadline()) {
            if (predare - tema.getDeadline() == 1) {
                n=2.5;
            } else if (predare - tema.getDeadline() == 2) {
                n=5.0;
            }
        }
        return n;
    }*/

    /**
     * Adauga o nota
     *
     * @param nota          - nota
     * @param feedback      - feedback-ul notei
     * @param verifCheckBox
     * @return null daca nota a fost adaugata sau nota daca aceasta exista deja
     */

    public void addNota(Nota nota, String feedback, int verifCheckBox) {
        notaValidator.validate(nota);
        Student student = studentFileRepository.findOne(nota.getIdStudent());
        Tema tema = temaFileRepository.findOne(nota.getIdTema());
        /*
        int predare = calculeazaSPredare(nota.getData());
        if (verifCheckBox == 0) {
            if (predare != tema.getDeadline()) {
                if (predare - tema.getDeadline() == 1) {
                    nota.setNota(nota.getNota() - 2.5);
                } else if (predare - tema.getDeadline() == 2) {
                    nota.setNota(nota.getNota() - 5);
                } else {
                    nota.setNota(1);
                }
            }
        }*/
        notaFileRepository.save(nota);
        notaEventObservable.notifyObservers(new NotaEvent(null, nota, ChangeEventType.ADD));/*
        String filename = "fisiere/" + student.getNume() + ".txt";
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filename, true))) {
            bufferedWriter.write("\nTema: " + tema.getID());
            bufferedWriter.write("\nNota: " + nota.getNota());
            bufferedWriter.write("\nPredata in saptamana: " + predare);
            bufferedWriter.write("\nDeadline: " + tema.getDeadline());
            bufferedWriter.write("\nFeedback: " + feedback);
            bufferedWriter.newLine();
        } catch (IOException exception) {
            throw new ValidationException(exception.getMessage());
        }
        //System.out.println("Eduard e cel mai tare");
      //  return nota.getNota();*/
    }

    /**
     *
     * @param student - emailul unui student
     * @return lista temelor studentilor care au emailul student
     */
    public List<Tema> getTemaSt(String student) {
        List<Tema> lista = new ArrayList<>();
        List<Nota> nota = this.getNoteLista();
        nota.forEach(n -> {
            if (findStudentDupaNume(n.getNumeStudent()).getEmail().equals(student)) {
                lista.add(findTema(n.getIdTema()));
            }
        });
        return lista;
    }

    /**
     *
     * @param lst -lista de note
     * @return lista de note
     */
    public List<Nota> getNote(List<Nota> lst) {
        List<Nota> lista = new ArrayList<>();
        lst.forEach(nota -> {
            Nota n = new Nota();
            n.setNumeStudent(findStudent(nota.getIdStudent()).getNume());
            n.setDescriereTema(findTema(nota.getIdTema()).getDescriere());
            n.setNota(nota.getNota());
            n.setID(nota.getID());
            n.setData(nota.getData());
            lista.add(n);
        });
        return lista;
    }

    /**
     * Sterge o nota
     *
     * @param id - id-ul notei
     * @return nota daca aceasta a fost stearsa sau null daca nota nu exista
     */
    public Optional<Nota> deleteNota(String id) {
        if (id == null || id.equals("")) {
            throw new ValidationException("Id-ul nu poate fi null!");
        }
        notaEventObservable.notifyObservers(new NotaEvent(findNota(id), null, ChangeEventType.DELETE));
        return notaFileRepository.delete(id);
    }

    /**
     *
     * @param st -un strudent
     *           functia sterge toate notele unui student
     */
    public void deleteNotaSt(Student st) {
        if (st == null || st.equals("")) {
            throw new ValidationException("Student inexistent");
        }
        for (Nota s : this.getAllNote()) {
            if (st.getID().equals(s.getIdStudent())) {
                notaEventObservable.notifyObservers(new NotaEvent(findNota(s.getID()), null, ChangeEventType.DELETE));
                notaFileRepository.delete(s.getID());
            }
        }
    }


    /**
     *
     * @param t -tema
     *          functia sterge toate notele de la o teme
     */
    public void deleteNotaT(Tema t) {
        if (t == null || t.equals("")) {
            throw new ValidationException("Tema inexistenta");
        }
        for (Nota s : this.getAllNote()) {
            if (t.getID().equals(s.getIdTema())) {
                notaEventObservable.notifyObservers(new NotaEvent(findNota(s.getID()), null, ChangeEventType.DELETE));
                notaFileRepository.delete(s.getID());
            }
        }
    }

    /**
     * Cauta o nota
     *
     * @param id - id-ul notei
     * @return nota sau null daca aceasta nu exista
     */
    public Nota findNota(String id) {
        if (id == null || id.equals("")) {
            throw new ValidationException("Id-ul nu poate fi null!");
        }
        return notaFileRepository.findOne(id);
    }

    /**
     * @return toate notele
     */
    public Iterable<Nota> getAllNote() {
        return notaFileRepository.findAll();
    }

    public List<Nota> getNoteLista() {
        List<Nota> l = new ArrayList<>();
        getAllNote().forEach(nota -> {
            l.add(nota);
        });
        return l;
    }

    /**
     *
     * @return lista studentilor
     */
    public List<Student> getStudentiLista() {
        List<Student> l = new ArrayList<>();
        getAllStudenti().forEach(st -> {
            l.add(st);
        });
        return l;
    }

    /**
     *
     * @return lista cu teme
     */
    public List<Tema> getTemeLista() {
        List<Tema> l = new ArrayList<>();
        getAllTeme().forEach(t -> {
            l.add(t);
        });
        return l;
    }
    /**
     * Prelungeste deadline-ul unei teme
     *
     * @param nrTema   - nr-ul temei
     * @param deadline - noul deadline
     */
    public void prelungireDeadline(String nrTema, int deadline) {

        int diff = Curent.getCurrentWeek();
        Tema tema = temaFileRepository.findOne(nrTema);
        if (tema == null) {
            throw new ValidationException("Tema nu exista!");
        }
        if (tema.getDeadline() >= diff) {
            tema.setDeadline(deadline);
            temaFileRepository.writeToFile();
        } else {
            throw new ValidationException("Nu se mai poate prelungi deadline-ul!");
        }
    }

    /**
     * Calculeaza saptamana de predare
     *
     * @param predare - data predarii unei teme
     * @return saptamana in care a fost predata tema
     */
    public int calculeazaSPredare(LocalDate predare) {
        LocalDate startDate = Curent.getStartDate();
        long days = DAYS.between(startDate, predare);
        double saptamanaPredare = Math.ceil((double) days / 7);
        return (int) saptamanaPredare;
    }

    /**
     *
     * @return returneaza tema saptamanii curente
     */
    public Tema temaCurenta() {
        int data = Curent.getCurrentWeek();
        Tema tema = new Tema();
        for (Tema t : this.getAllTeme()) {
            if (t.getDeadline() == data) {
                tema.setID(t.getID());
            tema.setPrimire(t.getPrimire());
            tema.setDeadline(t.getDeadline());
            tema.setDescriere(t.getDescriere());}
        }
        return tema;
    }

    /**
     *
     * @param student
     * @return returneaza toate notele unui student
     */

    public List<Nota> filtrareNoteStudent(Student student) {
        List<Nota> lista = this.getNoteLista();
        List<Nota> listaNote = lista.stream().filter(st -> st.getIdStudent().equals(student.getID())).collect(Collectors.toList());
        return listaNote;
    }

    /**
     *
     * @param tema
     * @return toate notele la o tema
     */
    public List<Nota> filtrareNoteTema(Tema tema) {
        List<Nota> lista = this.getNoteLista();
        List<Nota> listaNote = lista.stream().filter(t -> t.getIdTema().equals(tema.getID())).collect(Collectors.toList());
        return listaNote;
    }

    /**
     *
     * @param tema
     * @param grupa
     * @return returneaza toate notele unei grupe
     */
    public List<Nota> filtrareNoteGrupa(Tema tema, int grupa) {
        List<Nota> lista = this.getNoteLista();
        List<Nota> listaNote = lista.stream().filter(t -> t.getIdTema().equals(tema.getID())).filter(gr -> this.findStudent(gr.getIdStudent()).getGrupa() == grupa)
                .collect(Collectors.toList());
        return listaNote;
    }

    /**
     *
     * @param d1
     * @param d2
     * @return returneaza notele dintre 2 date
     */
    public List<Nota> filtrareData(LocalDate d1, LocalDate d2) {
        if (d1 != null && d2 != null) {
            List<Nota> lista = this.getNoteLista();
            List<Nota> listaNote = lista.stream().filter(n -> n.getData().isAfter(d1) && n.getData().isBefore(d2))
                    .collect(Collectors.toList());
            return listaNote;
        }
        throw new ValidationException("Data incorecta");
    }

    /**
     *
     * @param student -userul care urmeaza sa fie adaugat
     * @return eroare daca userul exista sau salveaza userul in fisier altfel
     */

    public User addUser(User student) {
        if (findUser(student.getID()) != null) {
            throw new ValidationException("user existent");
        }
        userValidator.validate(student);
        return userFileRepository.save(student);
    }

    /**
     *
     * @param id
     * @return se sterge userul dupa id, iar daca id-ul nu exista se va arunca o eroare
     */
    public Optional<User> deleteUser(String id) {
        if (id == null || id.equals("")) {
            throw new ValidationException("Id-ul nu poate fi null!");
        }
        User student = findUser(id);
        return userFileRepository.delete(id);
    }

    /**
     *
     * @param id
     * @return returneaza userul cu id-ul id sau arunca eroarre daca acesta nu exista
     */
    public User findUser(String id) {
        if (id == null || id.equals("")) {
            throw new ValidationException("Id-ul nu poate fi null!");
        }
        return userFileRepository.findOne(id);
    }

    /**
     *
     * @param id
     * @param parola
     * @return true daca user-ul cu parola cryptata exista, altfel arunca eroare
     */
    public boolean verifParola(String id, String parola) {

        User user = new User();
        user = this.findUser(id);
         if (BCrypt.checkpw(parola, user.getParola())) {
            return true;
        } else {
            throw new ValidationException("User inexistent");
        }

    }

    /**
     *
     * @param student-profesul care urmeaza sa fie adaugat
     * @return se adauga profesorul daca nu exista altfel se arunca o eroare
     */
    public Profesor addProfesor(Profesor student) {
        if (findProf(student.getID()) != null && findEmailProf(student.getEmail()) != null) {
            throw new ValidationException("profesor existent");
        }
        profValidator.validate(student);
        profEventObservable.notifyObservers(new ProfEvent(null, student, ChangeEventType.ADD));
        return profFileRepository.save(student);
    }

    /**
     *
     * @param email
     * @return profesul daca exista dupa email
     */
    public Profesor findEmailProf(String email) {

        if (email == null || email.equals("")) {
            throw new ValidationException("Email incorect");
        }
        Profesor profesor = new Profesor("19999", "19999");
        for (Profesor s : this.getAllProfi()) {
            if (s.getEmail().equals(email)) {
                profesor.setEmail(s.getEmail());
                profesor.setID(s.getID());
            }
        }
        if (!profesor.getID().equals("19999"))
            return profesor;
        else
            throw new ValidationException("Email negasit");
    }

    /**
     * Sterge un prof
     *
     * @param id - id-ul prof
     * @return prof daca acesta a fost sters sau null daca prof nu exista
     */
    public Optional<Profesor> deleteProf(String id) {
        if (id == null || id.equals("")) {
            throw new ValidationException("Id-ul nu poate fi null!");
        }
        Profesor student = findProf(id);
        profEventObservable.notifyObservers(new ProfEvent(student, null, ChangeEventType.DELETE));
        return profFileRepository.delete(id);
    }

    /**
     * Cauta un prof dupa id
     *
     * @param id - id-ul prof
     * @return prof daca acesta exista sau null altfel
     */
    public Profesor findProf(String id) {
        if (id == null || id.equals("")) {
            throw new ValidationException("Id-ul nu poate fi null!");
        }
        return profFileRepository.findOne(id);
    }

    /**
     *
     * @param student
     * @return updateaza profesorul care are id-ul la fel cu cea a entitatii student
     */
    public Optional<Profesor> updateProf(Profesor student) {
        profValidator.validate(student);
        Profesor student1 = profFileRepository.findOne(student.getID());
        profEventObservable.notifyObservers(new ProfEvent(student1, student, ChangeEventType.UPDATE));
        return profFileRepository.update(student);
    }

    /**
     *
     * @return profesorii
     */
    public Iterable<Profesor> getAllProfi() {
        return profFileRepository.findAll();
    }

    /**
     *
     * @param nume
     * @return returneaza profesorul daca are acelasi email ca si nume
     */
    public Profesor findProfEmail(String nume) {
        if (nume == null || nume.equals("")) {
            throw new ValidationException("Email incorect");
        }
        Profesor student = new Profesor("19999", "19999");
        for (Profesor s : this.getAllProfi()) {
            if (s.getEmail().equals(nume)) {
                student.setEmail(s.getEmail());
                student.setID(s.getID());
            }
        }
        if (!student.getID().equals("19999"))
            return student;
        else
            throw new ValidationException("Email negasit");
    }

    /**
     *
     * @param startIndex-index de start pe pagin
     * @param len nr elevi pe pagina
     * @return o pagina cu studenti pornind de la indexul startindex
     */
    public List<Student> getPieceDataSt(int startIndex, int len) {

        return studentFileRepository.getPieceData(startIndex, len);

    }

    /**
     *
     @param startIndex-index de start pe pagin
      * @param len nr teme pe pagina
     * @return o pagina cu studenti pornind de la indexul startindex
     */
    public List<Tema> getPieceDataT(int startIndex, int len) {

        return temaFileRepository.getPieceDataT(startIndex, len);

    }

    /**
     *
     @param startIndex-index de start pe pagin
      * @param len nr note pe pagina
     * @return o pagina cu notele pornind de la indexul startindex
     */
    public List<Nota> getPieceDataN(int startIndex, int len) {

        return notaFileRepository.getPieceDataN(startIndex, len);

    }

}
