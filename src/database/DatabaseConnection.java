package database;

/**
 * Имитация подключения к БД через коллекции в памяти
 * Для Assignment 3 достаточно, чтобы показать архитектуру
 */
public class DatabaseConnection {

    public static String getConnectionInfo() {
        return "✅ Используется хранение данных в памяти (коллекции)";
    }

    public static void initialize() {
        System.out.println("📊 База данных инициализирована в памяти");
        System.out.println("   - Авторы: ArrayList<Author>");
        System.out.println("   - Книги: ArrayList<Book>");
        System.out.println("   - Выдачи: ArrayList<Loan>");
    }
}