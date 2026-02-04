package models;

public abstract class BaseEntity {
    private int id;
    private String name;
    
    public BaseEntity(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public abstract String getDescription();
    public abstract boolean isValid();
    
    public String getInfo() {
        return "ID: " + id + ", Name: " + name;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}
