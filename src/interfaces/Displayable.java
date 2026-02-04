package interfaces;

@FunctionalInterface
public interface Displayable {
    void display();
    
    default void displayWithHeader(String header) {
        System.out.println("=== " + header + " ===");
        display();
    }
}
