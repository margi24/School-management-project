package view;

import domain.Student;
import utils.ChangeEventType;
import utils.Event;

public class StudentEvent implements Event {
    private Student oldData;
    private Student data;
    private ChangeEventType type;

    public StudentEvent(Student oldData, Student data, ChangeEventType type) {
        this.oldData = oldData;
        this.data = data;
        this.type = type;
    }
    public Student getOldData() {
        return oldData;
    }

    public void setOldData(Student oldData) {
        this.oldData = oldData;
    }

    public Student getData() {
        return data;
    }

    public void setData(Student data) {
        this.data = data;
    }

    public ChangeEventType getType() {
        return type;
    }

    public void setType(ChangeEventType type) {
        this.type = type;
    }






}
