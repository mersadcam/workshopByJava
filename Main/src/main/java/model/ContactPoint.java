package model;
import controller.Const;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.mongo.MongoClientUpdateResult;
import org.bson.types.ObjectId;

public class ContactPoint {

  private String _id;
	private String firstName;
	private String lastName;
	private String emailAddress;
  private Gender gender;

	public ContactPoint(JsonObject json){

    this.firstName = json.getString("firstName");
    this.lastName = json.getString("lastName");
    this.emailAddress = json.getString("emailAddress");
    this._id = new ObjectId().toString();
    this.gender = StringToGender(json.getString("gender"));

  }


  public JsonObject toJson(){

	  JsonObject json = new JsonObject()
      .put("firstname",this.firstName)
      .put("lastname",this.lastName)
      .put("emailAddress",this.emailAddress)
      .put("_id",this._id)
      .put("gender",this.gender);

	  return json;

  }

  public void saveToDB(MongoClient client, Handler<AsyncResult<String>> handler){

    client.insert(Const.contactPoint,this.toJson(),handler);

  }

  public void update(MongoClient client, Handler<AsyncResult<MongoClientUpdateResult>> handler){

    JsonObject query = new JsonObject().put("_id",this._id);
    JsonObject update = new JsonObject().put("$set",this.toJson());

    client.updateCollection(Const.contactPoint,query,update,handler);

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

    JsonObject toSet = new JsonObject()
      .put("emailAddress",json.getValue("emailAddress"))
      .put("firstName",json.getValue("firstName"))
      .put("lastName",json.getValue("lastName"))
      .put("gender",StringToGender(json.getString("gender")));

    JsonObject update = new JsonObject().put("$set",toSet);
    JsonObject query = new JsonObject().put("_id", _id);

    client.updateCollection("contactPoint",query,update,handler);

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
