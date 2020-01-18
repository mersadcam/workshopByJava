package model;

import controller.Const;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import org.bson.types.ObjectId;

public class Form {

  private JsonObject formJson;

  public Form(JsonObject formJson){
    this.formJson = formJson;
  }

  public void addToDB(MongoClient client , Handler<AsyncResult<String>> handler){

    JsonObject toInsert = new JsonObject()
      .put("formBody",formJson);

    client.insert(Const.form,toInsert,res->{
      handler.handle(Future.succeededFuture(res.result()));
    });

  }

}
