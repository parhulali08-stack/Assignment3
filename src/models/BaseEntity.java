package models;

public abstract class BaseEntity {
    protected int id;
    protected String name;

    public BaseEntity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    
    public abstract String getDescription();
    public abstract boolean isValid();

    
    public String getFullInfo() {
        return "ID: " + id + " | Название: " + name;
    }

    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Название не может быть пустым!");
        }
        this.name = name;
    }
}
