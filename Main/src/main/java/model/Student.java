package model;

import controller.Const;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.mongo.MongoClientUpdateResult;
import org.bson.types.ObjectId;

public class Student implements RequestType{


  private String _id;
  private String roleName;
  private Course course;
	private PaymentStatus paymentStatus;


	public Student(Course course,PaymentStatus paymentStatus){

	  this._id = new ObjectId().toString();
	  this.roleName = "Student";
	  this.course = course;
	  this.paymentStatus = paymentStatus;

  }

  public String get_id() {
    return _id;
  }

  public void setCourse(Course course){
	  this.course = course;
  }

  public void setPaymentStatus(PaymentStatus paymentStatus) {
    this.paymentStatus = paymentStatus;
  }

  public JsonObject toJson(){

	  JsonObject json = new JsonObject();
	  json.put("_id",this._id)
      .put("roleName",this.roleName)
      .put("course",this.course.getName())
      .put("paymentStatus",this.paymentStatus.get_id());

	  return json;

  }

  public void saveToDB(MongoClient client, Handler<AsyncResult<String>> handler){

	  client.insert(Const.role,this.toJson(),handler);

  }

  public void update(MongoClient client, Handler<AsyncResult<MongoClientUpdateResult>> handler){

    JsonObject query = new JsonObject().put("_id",this._id);
    JsonObject update = new JsonObject().put("$set",this.toJson());

    client.updateCollection(Const.role,query,update,handler);

  }

}
