package planet.info.skyline.model;
public class Technician {

    private String Name;
    private String Id;
    private boolean isChecklist_Completed;

    public Technician(String name, String id, boolean isChecklist_Completed) {
        Name = name;
        Id = id;
        this.isChecklist_Completed = isChecklist_Completed;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public boolean isChecklist_Completed() {
        return isChecklist_Completed;
    }

    public void setChecklist_Completed(boolean checklist_Completed) {
        isChecklist_Completed = checklist_Completed;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}