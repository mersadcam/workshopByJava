package model;

import controller.Const;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.mongo.MongoClientUpdateResult;
import io.vertx.ext.mongo.UpdateOptions;
import io.vertx.reactivex.ext.unit.Async;
import org.bson.types.ObjectId;

import java.util.Date;

public class Payment {

  private String _id;
	private String paymentName;
	private String time;
	private int value;
	private Status paymentStatus;

	enum Status {
		PAID,
		NOTPAID
	}

	public Payment(String paymentName,String time,int value){

	  this._id = new ObjectId().toString();
	  this.paymentName = paymentName;
	  this.time = time;
	  this.value = value;
	  this.paymentStatus = Status.NOTPAID;
  }

  public Payment(String _id){
	  this._id = _id;
  }

  public Payment(JsonObject jsonObject){
	  this._id = jsonObject.getString("_id");
	  this.paymentName = jsonObject.getString("paymentName");
	  this.time = jsonObject.getString("time");
	  this.value = jsonObject.getInteger("value");
	  this.paymentStatus = StringToStatus(jsonObject.getString("paymentStatus"));
  }

  public Status StringToStatus(String status){

	  if(status.equals("PAID"))
	    return Status.PAID;

	  return Status.NOTPAID;

  }

  public int getValue() {
    return value;
  }

  public void paid() {
    this.paymentStatus = Status.PAID;
  }

  public void notPaid(){

	  this.paymentStatus = Status.NOTPAID;

  }

  public String get_id() {
    return _id;
  }

  public JsonObject toJson(){

	  JsonObject json = new JsonObject()
      .put("_id",this._id)
      .put("paymentName",this.paymentName)
      .put("time",this.time)
      .put("value",this.value)
      .put("paymentStatus",this.paymentStatus);

	  return  json;

  }

  public void saveToDB(MongoClient client, Handler<AsyncResult<String>> handler){

	  client.insert(Const.payment,this.toJson(),handler);

  }

  public void update(MongoClient client, Handler<AsyncResult<MongoClientUpdateResult>> handler){

	  JsonObject query = new JsonObject().put("_id",this._id);
	  JsonObject update = new JsonObject().put("$set",this.toJson());

	  client.updateCollection(Const.payment,query,update,handler);

  }

}
