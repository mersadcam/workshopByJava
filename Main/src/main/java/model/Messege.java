package model;

import controller.Const;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.mongo.MongoClientUpdateResult;
import org.bson.types.ObjectId;

public class Messege {

  private String _id;
  private Messege reply;
  private String text;

  public Messege(String text,Messege reply){
    this._id = new ObjectId().toString();
    this.text = text;
    this.reply = reply;
  }
  public Messege(JsonObject jsonObject){

    this._id = jsonObject.getString("_id");
    this.text = jsonObject.getString("text");
    this.reply = new Messege(jsonObject.getString("reply"));

  }
  public Messege(){

    this._id = new ObjectId().toString();

  }
  public Messege(String _id){

    this._id = _id;

  }

  public void set_id(String _id) {
    this._id = _id;
  }

  public void setReply(Messege reply) {
    this.reply = reply;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String get_id() {
    return _id;
  }

  public String getText() {
    return text;
  }

  public Messege getReply() {
    return reply;
  }

  public JsonObject toJson(){

    JsonObject jsonObject = new JsonObject()
      .put("_id",this._id)
      .put("reply",this.reply.get_id())
      .put("text",this.text);

    return  jsonObject;

  }

  public void saveToDB(MongoClient client){

    client.insert(Const.messege,this.toJson(),handler->{});

  }

  public void saveToDB(MongoClient client , Handler<AsyncResult<String>> handler){

    client.insert(Const.messege,this.toJson(),handler);

  }

  public void update(MongoClient client, Handler<AsyncResult<MongoClientUpdateResult>> handler) {

    JsonObject query = new JsonObject().put("_id", this._id);
    JsonObject update = new JsonObject().put("$set", this.toJson());

    client.updateCollection(Const.messege, query, update, handler);
  }

  public void update(MongoClient client) {

    JsonObject query = new JsonObject().put("_id", this._id);
    JsonObject update = new JsonObject().put("$set", this.toJson());

    client.updateCollection(Const.messege, query, update, handler->{});
  }

}
