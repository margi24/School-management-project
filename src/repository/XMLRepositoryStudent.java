package repository;

import domain.Student;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

public class XMLRepositoryStudent extends XMLRepositoryAbstract<String, Student> {

    public XMLRepositoryStudent(String numeF) {
        super(numeF);
    }

    @Override
    public Student createFromElement(Element node) {
        String id=node.getAttributeNode("id").getValue();
        String nume=node.getElementsByTagName("nume").item(0).getTextContent();;
        int grupa= Integer.parseInt(node.getElementsByTagName("grupa").item(0).getTextContent());
        String email=node.getElementsByTagName("email").item(0).getTextContent();
        return new Student(id,nume,grupa,email);
    }

    @Override
    public Element createFromEntity(Student entity, Document doc) {
        Element root=doc.getDocumentElement();
        Element e=doc.createElement("Student");
        e.setAttribute("id",entity.getID());
        e.appendChild(createElement("nume",doc,entity.getNume()));
        e.appendChild(createElement("grupa",doc, String.valueOf(entity.getGrupa())));
        e.appendChild(createElement("email",doc,entity.getEmail()));
        return  e;
    }

    public List<Student>getPieceData(int startIndex, int len){
        ArrayList data = new ArrayList();
        int k=0;
        for(Student s : this.findAll()){
            if(k>=startIndex && k<=len){
                data.add(s);

            }
            k++;
        }
        return data;
    }
}
