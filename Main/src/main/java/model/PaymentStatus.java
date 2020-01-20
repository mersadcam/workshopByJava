package model;

import controller.Const;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.mongo.MongoClientUpdateResult;
import io.vertx.ext.mongo.UpdateOptions;
import org.bson.types.ObjectId;

import java.util.ArrayList;

public class PaymentStatus {


  private String _id;
	private Status paymentStatus;
	private int totalValue;
	private int paid;
	private int notPaid;

	private ArrayList<Payment> payments = new ArrayList<Payment>();

	enum Status {
		IN_PAYMENT,
		PAID
	}

	public PaymentStatus(int value){

	  this._id = new ObjectId().toString();
	  this.paymentStatus = Status.IN_PAYMENT;
	  this.notPaid = this.totalValue = value;

  }
  public PaymentStatus(JsonObject jsonObject){
	  this._id = jsonObject.getString("_id");
	  JsonArray jsonArray = jsonObject.getJsonArray("payments");

	  for(int i = 0 ; i < jsonArray.size() ; i++)
	    this.payments.add(new Payment(jsonArray.getString(i)));

  }

  public PaymentStatus(String _id){
	  this._id = _id;
  }

  public void addPayment(Payment payment){

	  this.payments.add(payment);

  }

  public void pay(Payment payment){

	  this.payments.add(payment);
	  this.paid += payment.getValue();
	  this.notPaid -= payment.getValue();

  }

  public JsonObject toJson(){

	  JsonObject json =  new JsonObject();
    JsonArray paymentsId = new JsonArray();

    for (int i = 0 ; i < this.payments.size() ; i++ ){
      paymentsId.add(this.payments.get(i).get_id());
    }

	  json.put("paymentStatus",paymentStatus)
      .put("totalValue",this.totalValue)
      .put("paid",this.paid)
      .put("_id",this._id)
      .put("notPaid",this.notPaid)
      .put("payments",paymentsId);

    return json;

  }

  public String get_id() {
    return this._id;
  }

  public void saveToDB(MongoClient client, Handler<AsyncResult<String>> handler){

	  client.insert(Const.paymentStatus,this.toJson(),handler);

  }

  public void saveToDB(MongoClient client){

    client.insert(Const.paymentStatus,this.toJson(),handler->{});

  }

  public void update(MongoClient client, Handler<AsyncResult<MongoClientUpdateResult>> handler){

    JsonObject query = new JsonObject().put("_id",this._id);
    JsonObject update = new JsonObject().put("$set",this.toJson());

    client.updateCollection(Const.paymentStatus,query,update,handler);


  }

}
