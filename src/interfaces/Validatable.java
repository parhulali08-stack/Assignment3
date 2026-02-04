package interfaces;

public interface Validatable<T> {
    boolean validate(T entity);
    
    default String getValidationRules() {
        return "Basic validation rules";
    }
    
    static <T> boolean isNotNull(T obj) {
        return obj != null;
    }
}
