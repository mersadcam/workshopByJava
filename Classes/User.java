package school;

import java.io.File;
import java.util.ArrayList;
import school.ContactPoint.Gender;

public class User {
	
	private ArrayList<Role> roles = new ArrayList<Role>();
	private ContactPoint information;
	
	private String UserName;
	private String hashKey;
	
	private ArrayList<LoginLogs> loginLogs = new ArrayList<LoginLogs>();
	
	public User(String userName , String hashKey 
			, String firstName ,String lastName 
			, String emailAddress , Gender gender,File url) {
		// not completed
	}
	
	public void setHashKey(String hashKey) {
		this.hashKey = hashKey;
	}
	
	public void setUserName(String userName) {
		UserName = userName;
	}
	
	
}
