package repository;

import domain.Nota;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class XMLRepositoryNota extends XMLRepositoryAbstract<String, Nota> {

    public XMLRepositoryNota(String numeF) {
        super(numeF);
    }

    @Override
    public Nota createFromElement(Element node) {
        String idStudent=node.getAttributeNode("idStudent").getValue();
        //String idStudent=node.getElementsByTagName("idStudent").item(0).getTextContent();
        String idTema=node.getElementsByTagName("idTema").item(0).getTextContent();
        double nota= Double.parseDouble((node.getElementsByTagName("nota").item(0).getTextContent()));
        String data= node.getElementsByTagName("data").item(0).getTextContent();
        String []words=data.split("-");
        LocalDate date=LocalDate.of(Integer.parseInt(words[0]),Integer.parseInt(words[1]),Integer.parseInt(words[2]));
        String numeStudent=node.getElementsByTagName("numeStudent").item(0).getTextContent();
        String descriereTema=node.getElementsByTagName("descriereTema").item(0).getTextContent();
        String id=idStudent+"#"+idTema;
        return new Nota(id,idStudent,idTema,nota,date,numeStudent,descriereTema);
    }

    @Override
    public Element createFromEntity(Nota entity, Document doc) {
        Element root=doc.getDocumentElement();
        Element e=doc.createElement("Nota");
        e.setAttribute("idStudent",entity.getIdStudent());
        //e.appendChild(createElement("idStudent",doc,entity.getIdStudent()));
        e.appendChild(createElement("idTema",doc, entity.getIdTema()));
        LocalDate data=entity.getData();
        e.appendChild(createElement("nota",doc, String.valueOf(entity.getNota())));
        String date=String.valueOf(data.getYear())+"-"+String.valueOf(data.getMonthValue())+"-"+String.valueOf(data.getDayOfMonth());
        e.appendChild(createElement("data",doc, date));
        e.appendChild(createElement("numeStudent",doc,entity.getNumeStudent()));
        e.appendChild(createElement("descriereTema",doc,entity.getDescriereTema()));


        return  e;
    }

    public List<Nota> getPieceDataN(int startIndex, int len){
        ArrayList data = new ArrayList();
        int k=0;
        for(Nota s : this.findAll()){
            if(k>=startIndex && k<=len){
                data.add(s);
            }
            k++;
        }
        return data;
    }
}
