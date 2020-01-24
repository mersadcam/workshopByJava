package model;

import controller.Const;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class Form {

  private JsonObject formJson;
  private String _id;

// mersad : bebin dige constructor nadere???
  public Form(JsonObject formJson){
    this.formJson = formJson;
    this._id = new ObjectId().toString();
  }

  public Form(){

  }

  public void setFormJson(JsonObject formJson) {
    this.formJson = formJson;
  }

  public void set_id(String _id) {
    this._id = _id;
  }

  public Form(String _id){
    this._id = _id;
  }

  public String get_id(){
    return this._id;
  }

  public JsonObject toJson(){
    JsonObject toInsert = new JsonObject()
      .put("formBody",formJson)
      .put("_id",this._id);
    return toInsert;
  }

  public void addToDB(MongoClient client , Handler<AsyncResult<String>> handler){

    client.insert(Const.form,toJson(),res->{
      handler.handle(Future.succeededFuture(res.result()));
    });

  }

  public static void findForm(
    MongoClient client,
    ArrayList<JsonObject> arr,//output
    JsonArray formsId,//input
    int counter ,
    Handler<AsyncResult<ArrayList<JsonObject>>> handler){

    if (counter == formsId.size()){
      handler.handle(Future.succeededFuture(arr));
    }

    else {
      client.find(Const.form , new JsonObject().put("_id",formsId.getValue(counter)) , resFind->{
        arr.add(new JsonObject().put("_id",resFind.result().get(0).getString("_id"))
        .put("formBody",resFind.result().get(0).getJsonObject("formBody")));
        findForm(client,arr,formsId,counter+1,handler);
      });
    }
  }

}
