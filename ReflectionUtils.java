package utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ReflectionUtils {
    public static void inspectClass(Object obj) {
        if (obj == null) {
            System.out.println("Object is null");
            return;
        }
        
        Class<?> clazz = obj.getClass();
        System.out.println("\n=== Class Inspection: " + clazz.getSimpleName() + " ===");
        
        System.out.println("\nFields:");
        Field[] fields = clazz.getDeclaredFields();
        if (fields.length == 0) {
            System.out.println("  No fields");
        } else {
            for (Field field : fields) {
                String modifiers = Modifier.toString(field.getModifiers());
                String type = field.getType().getSimpleName();
                System.out.println("  " + modifiers + " " + type + " " + field.getName());
            }
        }
        
        System.out.println("\nMethods:");
        Method[] methods = clazz.getDeclaredMethods();
        if (methods.length == 0) {
            System.out.println("  No methods");
        } else {
            for (Method method : methods) {
                String modifiers = Modifier.toString(method.getModifiers());
                String returnType = method.getReturnType().getSimpleName();
                System.out.println("  " + modifiers + " " + returnType + " " + method.getName() + "()");
            }
        }
        
        System.out.println("\nSuperclass: " + clazz.getSuperclass().getSimpleName());
    }
    
    public static void listAllMethods(Class<?> clazz) {
        System.out.println("\n=== All Methods of " + clazz.getSimpleName() + " ===");
        Method[] methods = clazz.getDeclaredMethods();
        
        for (Method method : methods) {
            System.out.print("  " + Modifier.toString(method.getModifiers()) + " ");
            System.out.print(method.getReturnType().getSimpleName() + " ");
            System.out.print(method.getName() + "(");
            
            Class<?>[] params = method.getParameterTypes();
            for (int i = 0; i < params.length; i++) {
                System.out.print(params[i].getSimpleName());
                if (i < params.length - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println(")");
        }
    }
}
