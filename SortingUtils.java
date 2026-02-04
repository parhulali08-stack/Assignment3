package utils;

import java.util.Comparator;
import java.util.List;

public class SortingUtils {
    
    public static <T> void sortByName(List<T> list, NameExtractor<T> extractor) {
        list.sort(Comparator.comparing(extractor::extractName));
    }
    
    public static <T> void sortById(List<T> list, IdExtractor<T> extractor) {
        list.sort(Comparator.comparingInt(extractor::extractId));
    }
    
    @FunctionalInterface
    public interface NameExtractor<T> {
        String extractName(T item);
    }
    
    @FunctionalInterface
    public interface IdExtractor<T> {
        int extractId(T item);
    }
    
    public static void demonstrateLambdaSorting() {
        System.out.println("\n=== Lambda Sorting Demonstration ===");
        
        List<String> names = List.of("Alice", "Bob", "Charlie", "David");
        
        System.out.println("Original: " + names);
        
        names.sort((a, b) -> a.compareTo(b));
        System.out.println("Sorted alphabetically: " + names);
        
        names.sort((a, b) -> Integer.compare(a.length(), b.length()));
        System.out.println("Sorted by length: " + names);
        
        names.sort(Comparator.reverseOrder());
        System.out.println("Sorted in reverse: " + names);
    }
}
