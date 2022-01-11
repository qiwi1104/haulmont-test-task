package qiwi.model.input;

public class BankInput {
    private String id;
    private String name;

    public boolean hasEmptyFields() {
        return name.isEmpty();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
