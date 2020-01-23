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
	private String fullName;
	private String emailAddress;
  private String subTitle;
  private String biography;


	public ContactPoint(String fullName, String emailAddress){
	  this._id = new ObjectId().toString();
	  this.fullName = fullName;
	  this.emailAddress = emailAddress;
	  this.subTitle = "";
	  this.biography = "";
  }

  public ContactPoint(String _id){
	  this._id = _id;
  }

	public ContactPoint(JsonObject json){

    this.fullName = json.getString("fullName");
    this.emailAddress = json.getString("emailAddress");
    this._id = json.getString("_id");
    this.biography = json.getString("biography");
    this.subTitle = json.getString("subTitle");

  }

  public String get_id(){

    return this._id;

  }

  public JsonObject toJson(){

	  JsonObject json = new JsonObject()
      .put("fullName",this.fullName)
      .put("emailAddress",this.emailAddress)
      .put("_id",this._id)
      .put("biography",this.biography)
      .put("subTitle",this.subTitle);

	  return json;

  }

  public void saveToDB(MongoClient client, Handler<AsyncResult<String>> handler){

    client.insert(Const.contactPoint,this.toJson(),handler);

  }

  public void saveToDB(MongoClient client){

    client.insert(Const.contactPoint,this.toJson(),handler->{});

  }

  public void update(MongoClient client, Handler<AsyncResult<MongoClientUpdateResult>> handler){

    JsonObject query = new JsonObject().put("_id",this._id);
    JsonObject update = new JsonObject().put("$set",this.toJson());

    client.updateCollection(Const.contactPoint,query,update,handler);

  }

  public void update(MongoClient client){

    JsonObject query = new JsonObject().put("_id",this._id);
    JsonObject update = new JsonObject().put("$set",this.toJson());

    client.updateCollection(Const.contactPoint,query,update,handler->{});

  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public void setBiography(String biography) {
    this.biography = biography;
  }

  public void setSubTitle(String subTitle) {
    this.subTitle = subTitle;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public void setFirstName(String firstName) {
    this.fullName = firstName;
  }


}
