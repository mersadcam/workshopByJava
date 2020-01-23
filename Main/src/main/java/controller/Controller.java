package controller;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;

import java.util.List;

public class Controller {



  public static void findIn(MongoClient client, String fieldName, String in, Handler<AsyncResult<List<JsonObject>>>handler){

    JsonObject query = new JsonObject()
      .put(fieldName,new JsonObject().put("$in",new JsonArray().add(in)));

    client.find(Const.user,query,handler);

  }



}
