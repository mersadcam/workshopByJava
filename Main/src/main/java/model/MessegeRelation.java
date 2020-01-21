package model;

import controller.Const;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.mongo.MongoClientUpdateResult;
import org.bson.types.ObjectId;

import java.text.SimpleDateFormat;

public class MessegeRelation {

  private String _id;
  private User sender;
  private User receiver;
  private String messegeDate;
  private Messege messege;


  public MessegeRelation(User sender, User receiver, Messege messege) {
    this._id = new ObjectId().toString();
    this.sender = sender;
    this.receiver = receiver;
    this.messege = messege;
    this.messegeDate = new SimpleDateFormat("yyyy-MM-dd-HH:mm").format(new java.util.Date());
  }

  public MessegeRelation(String _id){
    this._id = _id;
  }

  public MessegeRelation(){
    this._id = new ObjectId().toString();
  }

  public void set_id(String _id) {
    this._id = _id;
  }

  public void setMessegeDate(String messegeDate) {
    messegeDate = messegeDate;
  }

  public void setReceiver(User receiver) {
    this.receiver = receiver;
  }

  public void setSender(User sender) {
    this.sender = sender;
  }

  public void setMessege(Messege messege) {
    this.messege = messege;
  }

  public Messege getMessege() {
    return messege;
  }

  public User getSender() {
    return sender;
  }

  public String get_id() {
    return _id;
  }

  public String getMessegeDate() {
    return messegeDate;
  }

  public User getReceiver() {
    return receiver;
  }

  public JsonObject toJson(){
    JsonObject jsonObject = new JsonObject()
      .put("sender",this.sender.getUsername())
      .put("receiver",this.receiver.getUsername())
      .put("_id",this._id)
      .put("messege",this.messege.get_id())
      .put("messegeDate",this.messegeDate);

    return jsonObject;

  }

  public void saveToDB(MongoClient client){

    client.insert(Const.messegeRelation,this.toJson(), handler->{});

  }

  public void saveToDB(MongoClient client , Handler<AsyncResult<String>> handler){

    client.insert(Const.messegeRelation,this.toJson(),handler);

  }

  public void update(MongoClient client, Handler<AsyncResult<MongoClientUpdateResult>> handler) {

    JsonObject query = new JsonObject().put("_id", this._id);
    JsonObject update = new JsonObject().put("$set", this.toJson());

    client.updateCollection(Const.messegeRelation, query, update, handler);
  }

  public void update(MongoClient client) {

    JsonObject query = new JsonObject().put("_id", this._id);
    JsonObject update = new JsonObject().put("$set", this.toJson());

    client.updateCollection(Const.messegeRelation, query, update, handler->{});
  }


}
