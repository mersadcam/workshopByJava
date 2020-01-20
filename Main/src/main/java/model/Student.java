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
	private PaymentStatus paymentStatus;


	public Student(PaymentStatus paymentStatus){

	  this._id = new ObjectId().toString();
	  this.paymentStatus = paymentStatus;

  }
  public Student(){

	  this._id = new ObjectId().toString();

  }
  public Student(String _id){
	  this._id = _id;
  }
  public Student(JsonObject jsonObject){
	  //payment status ham inja dorost kon
	  this._id = jsonObject.getString("_id");
	  this.paymentStatus = new PaymentStatus(jsonObject.getString("paymentStatus"));
  }

  public String get_id() {
    return _id;
  }

  public void setPaymentStatus(PaymentStatus paymentStatus) {
    this.paymentStatus = paymentStatus;
  }

  public JsonObject toJson(){

	  JsonObject json = new JsonObject();

	  json.put("_id",this._id)
      .put("paymentStatus",this.paymentStatus.get_id());

	  return json;

  }

  public void saveToDB(MongoClient client, Handler<AsyncResult<String>> handler){

	  client.insert(Const.student, this.toJson() ,handler);

  }

  public void saveToDB(MongoClient client){

    client.insert(Const.student, this.toJson() ,handler->{});

  }

  public void update(MongoClient client, Handler<AsyncResult<MongoClientUpdateResult>> handler){

    JsonObject query = new JsonObject().put("_id",this._id);
    JsonObject update = new JsonObject().put("$set",this.toJson());

    client.updateCollection(Const.student,query,update,handler);

  }

}
