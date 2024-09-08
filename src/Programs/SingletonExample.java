package Programs;

import java.lang.reflect.Constructor;

class Singleton{
	private static Singleton instance=null;
	
	/**
	 * 
	 */
	private Singleton() {}
	
	public static Singleton getInstance() {
		if(instance==null) {
			instance= new Singleton();
		}
		return instance;
	}
}
public class SingletonExample {
 
	public static void main(String[] args) {
		
		Singleton s1=Singleton.getInstance();
		System.out.println("s1 Hashcode: "+s1.hashCode());
		
		Singleton s2=Singleton.getInstance();
		System.out.println("s2 Hashcode: "+s2.hashCode());
		
		// Use reflection to break the Singleton pattern
		Singleton s3=null;
		try {
			Constructor<Singleton> constructor = Singleton.class.getDeclaredConstructor();
			constructor.setAccessible(true); // Bypass Java language access checking
			s3=constructor.newInstance();  // Create a new instance via reflection
		}
		catch (Exception e) {
			System.out.println(e);
		}
		
		System.out.println("s3 Hashcode: " +s3.hashCode());
	}

}
