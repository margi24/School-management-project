package repository;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.util.Optional;

public abstract class XMLRepositoryAbstract<ID, E extends HasID<ID>> extends AbstractCrudRepository<ID, E> {
    private String numeF;


    public XMLRepositoryAbstract(String numeF) {

        this.numeF = numeF;
        loadFromFile();
    }
    public void loadFromFile(){
        try{
            Document doc= DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(numeF);
            Element root=doc.getDocumentElement();
            NodeList list= root.getChildNodes();
            for (int i=0;i<list.getLength();i++){
                Node node=list.item(i);
                if (node instanceof Element){
                    save(createFromElement((Element)node));
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    public void writeToFile() {
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            doc.appendChild(doc.createElement("Entity"));
            Element root = doc.getDocumentElement();
            findAll().forEach(entity -> {
                Element thing =  createFromEntity(entity, doc);
                root.appendChild(thing);
            });

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(new DOMSource(doc), new StreamResult(numeF));

        } catch (TransformerException | ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public Element createElement(String tag,Document doc,String value){
        Element e=doc.createElement(tag);
        e.setTextContent(value);
        return e;
    }
    /**
     * Salveaza un obiect
     * @param entity - obiectul pe care il salveaza
     * @return null daca obiectul s-a salvat cu succes sau obiectul daca acesta exista deja in memorie
     */
    @Override
    public E save(E entity) {

            super.save(entity);
            writeToFile();
            return entity;
    }

    /**
     * Sterge un obiect
     * @param  id - id-ul obiectului
     * @return obiectul daca s-a reusit stergerea sau null daca obiectul nu exista
     */
    @Override
    public Optional<E> delete(ID id){
        Optional<E>entity=super.delete(id);
        writeToFile();
        return entity;
    }
    /**
     * Modifica un obiect
     * @param entity - noul obiect
     * @return null daca obiectul a fost modificat sau obiectul, daca acesta nu exista
     */
    @Override
    public Optional<E> update(E entity){
        Optional<E> entity1=super.update(entity);
        writeToFile();
        return entity1;
    }



    public abstract E createFromElement(Element node);
    public abstract Element createFromEntity(E entity, Document doc);
}

