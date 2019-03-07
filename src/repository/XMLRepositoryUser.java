package repository;

import domain.User;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLRepositoryUser extends XMLRepositoryAbstract<String, User> {

        public XMLRepositoryUser(String numeF) {
            super(numeF);
        }

        @Override
        public User createFromElement(Element node) {
            String user=node.getAttributeNode("user").getValue();
            String parola=node.getElementsByTagName("parola").item(0).getTextContent();
            String tip= node.getElementsByTagName("tip").item(0).getTextContent();
            return new User(user,parola,tip);
        }

        @Override
        public Element createFromEntity(User entity, Document doc) {
            Element root=doc.getDocumentElement();
            Element e=doc.createElement("User");
            e.setAttribute("user",entity.getID());
            e.appendChild(createElement("parola",doc, entity.getParola()));
            e.appendChild(createElement("tip",doc,entity.getTip()));
            return  e;
        }

    }
