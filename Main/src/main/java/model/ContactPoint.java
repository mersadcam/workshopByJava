package model;
import com.mongodb.Mongo;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.mongo.MongoClientUpdateResult;
import org.bson.types.ObjectId;

import java.io.File;

public class ContactPoint {

  private String _id;
	private String firstName;
	private String lastName;
	private String emailAddress;
  private Gender gender;
	private File image;

	public ContactPoint(JsonObject json){

    this.firstName = json.getString("firstName");
    this.lastName = json.getString("lastName");
    this.emailAddress = json.getString("emailAddress");
    this._id = ObjectId.get().toString();
    this.gender = StringToGender(json.getString("gender"));

  }


  public void addCPToDB(MongoClient client , Handler<AsyncResult<String>> handler){

	  JsonObject query = new JsonObject()
      .put("firstName",this.firstName)
      .put("lastName",this.lastName)
      .put("emailAddress",this.emailAddress)
      .put("gender",this.gender)
      .put("_id",this._id);

	  //transaction :

	  client.insert("contactPoint",query,res->{

	    handler.handle(Future.succeededFuture(this._id));

    });


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

  public String get_id(){

	  return this._id;

  }

  public static void editContactPoint(MongoClient client,JsonObject json,String _id,Handler<AsyncResult<MongoClientUpdateResult>> handler){

    JsonObject update = new JsonObject()
      .put("emailAddress",json.getValue("emailAddress"))
      .put("firstName",json.getValue("firstName"))
      .put("lastName",json.getValue("lastName"))
      .put("gender",StringToGender(json.getString("gender")));

    JsonObject query = new JsonObject().put("_id", new ObjectId(_id));

    client.updateCollection("ContactPoint",query,update,handler);

  }

  public static Gender StringToGender(String genderString){

	  Gender gender;

    if (genderString == null)
      gender = ContactPoint.Gender.NOTSET;
    else if (genderString.equals("male"))
      gender = ContactPoint.Gender.MALE;
    else if (genderString.equals("female"))
      gender = ContactPoint.Gender.FEMALE;
    else {
      gender = ContactPoint.Gender.OTHERS;
    }

    return gender;

  }


}
