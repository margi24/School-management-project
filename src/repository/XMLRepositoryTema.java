package repository;

import domain.Tema;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

public class XMLRepositoryTema extends XMLRepositoryAbstract<String, Tema> {

    public XMLRepositoryTema(String numeF) {
        super(numeF);
    }

    @Override
    public Tema createFromElement(Element node) {
        String id=node.getAttributeNode("id").getValue();
        String descriere=node.getElementsByTagName("descriere").item(0).getTextContent();
        int deadline= Integer.parseInt(node.getElementsByTagName("deadline").item(0).getTextContent());
        int primire= Integer.parseInt(node.getElementsByTagName("primire").item(0).getTextContent());

        return new Tema(id,descriere,deadline,primire);
    }

    @Override
    public Element createFromEntity(Tema entity, Document doc) {
        Element root=doc.getDocumentElement();
        Element e=doc.createElement("Tema");
        e.setAttribute("id",entity.getID());
        e.appendChild(createElement("descriere",doc,entity.getDescriere()));
        e.appendChild(createElement("deadline",doc, String.valueOf(entity.getDeadline())));
        e.appendChild(createElement("primire",doc, String.valueOf(entity.getPrimire())));
        return  e;
    }

    public List<Tema> getPieceDataT(int startIndex, int len){
        ArrayList data = new ArrayList();
        int k=0;
        for(Tema s : this.findAll()){
            if(k>=startIndex && k<=len){
                data.add(s);
            }
            k++;
        }
        return data;
    }

}
