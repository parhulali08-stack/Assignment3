package models;

public class Author extends BaseEntity {
    private String country;

    public Author(int id, String name, String country) {
        super(id, name);
        this.country = country;
    }

    @Override
    public String getDescription() {
        return "Автор: " + getName() + " (" + country + ")";
    }

    @Override
    public boolean isValid() {
        return getName() != null && !getName().trim().isEmpty();
    }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
}