package repository;

import validation.ValidationException;

import java.io.*;
import java.util.Optional;

public abstract class AbstractFileRepository<ID, E extends HasID<ID>> extends AbstractCrudRepository<ID, E> implements FileRepository<ID, E> {

    private String filename;

    /**
     * Class constructor
     * @param filename - numele fisierului
     */
    AbstractFileRepository(String filename) {
        this.filename = filename;
        loadFromFile();
    }

    /**
     * Extrage un obiect dintr-un String
     * @param linie - String-ul din care extrage obiectul
     * @return - obiectul
     */
    public abstract E extractEntity(String linie);

    /**
     * Incarca datele din fisier
     */
    public void loadFromFile(){
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filename))) {
            String linie;
            while ((linie = bufferedReader.readLine()) != null) {
                E entity = extractEntity(linie);
                super.save(entity);
            }
        } catch (IOException exception) {
            throw new ValidationException(exception.getMessage());
        }
    }

    /**
     * Scrie un obiect nou in fisier
     * @param entity - obiectul pe care il scrie
     */
    public void saveToFile(E entity){
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filename, true))) {
            bufferedWriter.write(entity.toString());
            bufferedWriter.newLine();
        } catch (IOException exception) {
            throw new ValidationException(exception.getMessage());
        }
    }

    /**
     * Rescrie fisierului cu toate obiectele din memorie
     */
    public void writeToFile(){
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filename))) {
            Iterable<E> all = super.findAll();
//------------------>>>>aci
            all.forEach((value)->{
                try {
                    bufferedWriter.write(value.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    bufferedWriter.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        } catch (IOException exception) {
            throw new ValidationException(exception.getMessage());
        }
    }

    /**
     * Salveaza un obiect
     * @param entity - obiectul pe care il salveaza
     * @return null daca obiectul s-a salvat cu succes sau obiectul daca acesta exista deja in memorie
     */
    @Override
    public E save(E entity) {
        E entity1 = super.save(entity);
        if (entity1 == null) {
            saveToFile(entity);
        }
        return entity1;
    }

    /**
     * Sterge un obiect
     * @param id - id-ul obiectului
     * @return obiectul daca s-a reusit stergerea sau null daca obiectul nu exista
     */
    @Override
    public Optional<E> delete(ID id) {
        Optional<E> entity = super.delete(id);
        if(entity != null){
            writeToFile();
        }
        return entity;
    }

    /**
     * Modifica un obiect
     * @param entity - noul obiect
     * @return null daca obiectul a fost modificat sau obiectul, daca acesta nu exista
     */
    @Override
    public Optional<E> update(E entity) {
        Optional<E> entity1 = super.update(entity);
        if(entity == null){
            writeToFile();
        }
        return entity1;
    }
}
