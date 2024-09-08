package breakthesingleton;

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
    private static Singleton instance = null;

    /**
     * Private constructor to prevent direct instantiation from outside the class.
     * This ensures that the class can only be instantiated through the getInstance method.
     */
    private Singleton() {}

    /**
     * Provides access to the single instance of the class.
     * Creates the instance if it does not already exist.
     * 
     * @return the single instance of the class
     */
    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton(); // Create the instance if it doesn't exist
        }
        return instance;
    }

    /**
     * Overrides the clone method to allow cloning of the Singleton instance.
     * 
     * @return a clone of the Singleton instance
     * @throws CloneNotSupportedException if cloning is not supported
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        // Create a clone of the instance. Note: This is not a standard Singleton approach
        return super.clone();
    }
}

/**
 * Test class to demonstrate breaking the Singleton pattern.
 * Includes techniques for breaking the Singleton pattern using reflection, serialization, and cloning.
 */
public class SingletonBreakExample {

    /**
     * Main method to demonstrate Singleton pattern functionality and methods to break it.
     * 
     * @param args command-line arguments
     * @throws NoSuchMethodException if a specified method cannot be found
     * @throws InstantiationException if an application tries to create an instance of an abstract class
     * @throws IllegalAccessException if an application tries to access a field or method it does not have access to
     * @throws IllegalArgumentException if a method has been passed an illegal or inappropriate argument
     * @throws InvocationTargetException if a method throws an exception
     * @throws IOException if an I/O error occurs
     * @throws ClassNotFoundException if the class of a serialized object cannot be found
     * @throws CloneNotSupportedException if cloning is not supported
     */
    public static void main(String[] args) 
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, 
                   IllegalArgumentException, InvocationTargetException, IOException, 
                   ClassNotFoundException, CloneNotSupportedException {

        // Demonstrate standard Singleton pattern functionality
        Singleton s1 = Singleton.getInstance();
        System.out.println("s1 Hashcode: " + s1.hashCode());

        Singleton s2 = Singleton.getInstance();
        System.out.println("s2 Hashcode: " + s2.hashCode());

        System.out.println("----------------------------------------------");

        System.out.println("Break through reflection");
        // Breaking Singleton using reflection
        Constructor<Singleton> constructor = Singleton.class.getDeclaredConstructor();
        constructor.setAccessible(true); // Bypass Java language access checking
        Singleton s3 = constructor.newInstance(); // Create a new instance via reflection

        System.out.println("s3 Hashcode: " + s3.hashCode());

        System.out.println("----------------------------------------------");

        System.out.println("Breaking using Serialization");

        // Breaking Singleton using serialization
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("singleton.ser"))) {
            output.writeObject(s1);
        }

        // Deserialize the Singleton instance from the file
        Singleton s4;
        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream("singleton.ser"))) {
            s4 = (Singleton) input.readObject();
        }

        System.out.println("s4 Hashcode: " + s4.hashCode());

        System.out.println("----------------------------------------------");

        System.out.println("Breaking using Cloning");
        // Breaking Singleton using cloning
        Singleton s5 = (Singleton) s1.clone(); // Attempt to clone the instance
        System.out.println("s5 Hashcode: " + s5.hashCode());
        
    }
}
