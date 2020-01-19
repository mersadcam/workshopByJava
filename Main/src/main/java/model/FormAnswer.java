package model;

import controller.Const;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.mongo.MongoClientUpdateResult;
import org.bson.types.ObjectId;

public class FormAnswer {

  private String _id;
	private Form form;
	private JsonObject jsonAnswer;
	private FormWriter writer;

	public FormAnswer(Form form , JsonObject jsonAnswer , FormWriter formWriter){
	  this._id = new ObjectId().toString();
	  this.form = form;
	  this.jsonAnswer = jsonAnswer;
	  this.writer = formWriter;
  }
	public FormAnswer(JsonObject jsonObject ){
	  this.jsonAnswer = jsonObject;
	  this._id = new ObjectId().toString();
  }
  public FormAnswer(String _id){
	  this._id = _id;
  }
  public JsonObject getJsonAnswer() {
    return jsonAnswer;
  }

  public void setJsonAnswer(JsonObject jsonAnswer) {
    this.jsonAnswer = jsonAnswer;
  }

  public JsonObject toJson(){

	  JsonObject json = new JsonObject();

	  json
      .put("_id",this._id)
      .put("form" , this.form.get_id())
      .put("answer" , this.jsonAnswer)
      .put("write",this.writer.get_id());

	  return json;

  }

  public void saveToDB(MongoClient client , Handler<AsyncResult<String>> handler){
	  client.insert(Const.formAnswer , this.toJson() , handler);
  }

  public void update(MongoClient client , Handler<AsyncResult<MongoClientUpdateResult>> handler){
	  JsonObject query = new JsonObject().put("_id",this._id);
	  JsonObject update = new JsonObject().put("$set",this.toJson());
	  client.updateCollection(Const.formAnswer , query , update , handler);

  }

  public String get_id() {
    return this._id;
	}
}
