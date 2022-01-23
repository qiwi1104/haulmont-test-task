package qiwi.model.input;

public class BankInput {
    private String name = "";
    private String newName = "";

    public boolean hasEmptyFields() {
        return name == null || name.isEmpty()
                || newName == null || newName.isEmpty();
    }

    public String getName() {
        return name;
    }

    public String getNewName() {
        return newName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }
}
