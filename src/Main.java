import controller.LibraryController;

public class Main {
    public static void main(String[] args) {
        System.out.println("🚀 ASSIGNMENT 3: OOP API PROJECT");
        System.out.println("=================================\n");

        try {
            LibraryController controller = new LibraryController();
            controller.start();
        } catch (Exception e) {
            System.err.println("❌ Критическая ошибка: " + e.getMessage());
            e.printStackTrace();
        } finally {
           
            DatabaseConnection.shutdown();
        }
    }
}
