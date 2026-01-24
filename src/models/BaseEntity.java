package models;

public abstract class BaseEntity {
    protected int id;
    protected String name;

    public BaseEntity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // 2 абстрактных метода (требование)
    public abstract String getDescription();
    public abstract boolean isValid();

    // 1 конкретный метод (требование)
    public String getFullInfo() {
        return "ID: " + id + " | Название: " + name;
    }

    // Геттеры/сеттеры
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