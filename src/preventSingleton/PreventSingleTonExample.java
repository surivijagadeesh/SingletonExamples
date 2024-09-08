package preventSingleton;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Singleton class that demonstrates the Singleton design pattern.
 * It ensures that only one instance of the class is created.
 */
class Singleton implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L; // For serialization compatibility

    // The single instance of the class, initially set to null
    private static volatile Singleton instance = null; // Volatile keyword ensures visibility

    /**
     * Private constructor to prevent direct instantiation from outside the class.
     * This ensures that the class can only be instantiated through the getInstance method.
     */
    private Singleton() {
        if (instance != null) {
            throw new IllegalStateException("Singleton instance already created!");
        }
    }

    /**
     * Provides access to the single instance of the class.
     * Creates the instance if it does not already exist.
     * Uses double-checked locking to ensure thread safety.
     * 
     * @return the single instance of the class
     */
    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }

    /**
     * Overrides the clone method to prevent cloning of the Singleton instance.
     * 
     * @return throws CloneNotSupportedException to prevent cloning
     * @throws CloneNotSupportedException if cloning is not supported
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Cloning is not supported for Singleton class.");
    }

    /**
     * Ensures Singleton property during deserialization.
     * The readResolve method is called after the object is deserialized.
     * 
     * @return the single instance of the class
     */
    private Object readResolve() {
        return getInstance();
    }
}

/**
 * Test class to demonstrate breaking and protecting the Singleton pattern.
 * Includes techniques for breaking the Singleton pattern using reflection, serialization, and cloning.
 */
public class PreventSingleTonExample {

    public static void main(String[] args) {

        // Demonstrate standard Singleton pattern functionality
        Singleton s1 = Singleton.getInstance();
        System.out.println("s1 Hashcode: " + s1.hashCode());

        Singleton s2 = Singleton.getInstance();
        System.out.println("s2 Hashcode: " + s2.hashCode());

        System.out.println("----------------------------------------------");

        System.out.println("Preventing from reflection");
        
        // preventing
        try {
            Constructor<Singleton> constructor = Singleton.class.getDeclaredConstructor();
            constructor.setAccessible(true); // Bypass Java language access checking
            Singleton s3 = (Singleton) constructor.newInstance(); // Create a new instance via reflection
            
            System.out.println("s3 Hashcode: " + s3.hashCode()); // Print hash code of the new instance
        } catch (InvocationTargetException e) {
            System.out.println("Reflection Error: " + e.getCause().getMessage());
        } catch (Exception e) {
            System.out.println("Reflection Error: " + e.getMessage());
        }

        System.out.println("----------------------------------------------");

        System.out.println("Preventing from Serialization");

        // Preventing from Serialization
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("singleton.ser"))) {
            output.writeObject(s1);
        } catch (IOException e) {
            System.out.println("Serialization Error: " + e.getMessage());
        }

        // Deserialize the Singleton instance from the file
        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream("singleton.ser"))) {
            Singleton s4 = (Singleton) input.readObject();
            System.out.println("s4 Hashcode: " + s4.hashCode());
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Deserialization Error: " + e.getMessage());
        }

        System.out.println("----------------------------------------------");

        System.out.println("preventing from Cloning");

        // preventing from Cloning
        try {
            Singleton s5 = (Singleton) s1.clone(); // Attempt to clone the instance
            System.out.println("s5 Hashcode: " + s5.hashCode());
        } catch (CloneNotSupportedException e) {
            System.out.println("Cloning Error: " + e.getMessage());
        }
    }
}
