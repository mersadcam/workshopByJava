package model;
import java.io.File;

public class ContactPoint {

	private String firstName;
	private String lastName;
	private String emailAddress;
  private Gender gender;
	private File image;

	public ContactPoint(String firstName,String lastName,
                      Gender gender,String emailAddress){

    this.firstName = firstName;
    this.lastName = lastName;
    this.gender = gender;
    this.emailAddress = emailAddress;

  }


  public File getImage() {
    return image;
  }

  public void setImage(File image) {
    this.image = image;
  }

  public Gender getGender() {
    return gender;
  }

  public void setGender(Gender gender) {
    this.gender = gender;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public enum Gender{
    MALE,
    FEMALE,
    OTHERS,
    NOTSET
  }




}
