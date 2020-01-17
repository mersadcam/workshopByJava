package model;

import controller.Const;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

public class Form {

  private JsonObject formJson;

  public Form(JsonObject formJson){
    this.formJson = formJson;
  }

  public void addToDB(MongoClient client ,JsonObject teacherJson, Handler<AsyncResult<String>> handler){

    String teacherId = teacherJson.getString("_id");

    JsonObject toInsert = new JsonObject()
      .put("formBody",formJson)
      .put("Teacher",teacherId);

    client.insert(Const.form,toInsert,handler);

  }

}
