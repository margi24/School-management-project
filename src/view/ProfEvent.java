package view;

import domain.Profesor;
import utils.ChangeEventType;
import utils.Event;

public class ProfEvent implements Event {
    private Profesor oldData;
    private Profesor data;
    private ChangeEventType type;

    public ProfEvent(Profesor oldData, Profesor data, ChangeEventType type) {
        this.oldData = oldData;
        this.data = data;
        this.type = type;
    }
    public Profesor getOldData() {
        return oldData;
    }

    public void setOldData(Profesor oldData) {
        this.oldData = oldData;
    }

    public Profesor getData() {
        return data;
    }

    public void setData(Profesor data) {
        this.data = data;
    }

    public ChangeEventType getType() {
        return type;
    }

    public void setType(ChangeEventType type) {
        this.type = type;
    }
}
