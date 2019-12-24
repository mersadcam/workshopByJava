package school;
import java.io.File;

public class ContactPoint {
	
	private String firstName;
	private String lastName;
	private String emailAddress;
	private Gender gender;
	private File image;
	
	public enum Gender{
		MALE,
		FEMALE,
		OTHERS
	}

}
