package repository;

import domain.Profesor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLRepositoryProf extends  XMLRepositoryAbstract<String, Profesor>{

    public XMLRepositoryProf(String numeF) {
        super(numeF);
    }

    @Override
    public Profesor createFromElement(Element node) {
        String user=node.getAttributeNode("nume").getValue();
        String parola=node.getElementsByTagName("email").item(0).getTextContent();

        return new Profesor(user,parola);
    }

    @Override
    public Element createFromEntity(Profesor entity, Document doc) {
        Element root=doc.getDocumentElement();
        Element e=doc.createElement("Profesor");
        e.setAttribute("nume",entity.getID());
        e.appendChild(createElement("email",doc, entity.getEmail()));
        return  e;
    }
}
