package model;


import controller.Const;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.mongo.MongoClientUpdateResult;
import org.bson.types.ObjectId;

import java.io.Writer;

public class FormAnswer {

  private String _id;
	private Form form;
	private JsonObject answerBody;
	private FormWriter writer;


	public FormAnswer(Form form , FormWriter formWriter,JsonObject answerBody){
	  this._id = new ObjectId().toString();
	  this.form = form;
	  this.answerBody = answerBody;
	  this.writer = formWriter;
  }

	public FormAnswer(JsonObject jsonObject ){

	  this._id = jsonObject.getString("_id");
	  this.answerBody = jsonObject.getJsonObject("answerBody");
	  this.form = new Form(jsonObject.getString("form"));

  }
  public FormAnswer(String _id){
	  this._id = _id;
  }

  public void setWriter(FormWriter writer) {
    this.writer = writer;
  }

  public JsonObject getAnswerBody() {
    return answerBody;
  }

  public void setAnswerBody(JsonObject answerBody) {
    this.answerBody = answerBody;
  }

  public JsonObject toJson(){

	  JsonObject json = new JsonObject();

	  json
      .put("_id",this._id)
      .put("form" , this.form.get_id())
      .put("answerBody" , this.answerBody)
      .put("write",this.writer.get_id());

	  return json;

  }

  public void saveToDB(MongoClient client , Handler<AsyncResult<String>> handler){
	  client.insert(Const.formAnswer , this.toJson() , handler);
  }

  public void saveToDB(MongoClient client ){
    client.insert(Const.formAnswer , this.toJson() , handler->{});
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
