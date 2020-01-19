package model;

import controller.Const;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.mongo.MongoClientUpdateResult;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

public class Grader implements RequestType , FormWriter {

  private String _id;
  private String roleName;
	private String requestDate;

	public Grader( String requestDate ){

	  this._id = new ObjectId().toString();
	  this.roleName = "Grader";
	  this.requestDate = requestDate;

  }

  public String get_id() {
    return _id;
  }

  public JsonObject toJson(){

	  JsonObject json = new JsonObject()
      .put("roleName",this.roleName)
      .put("_id",this._id)
      .put("requestDate",this.requestDate);

	  return json;
  }

  public void saveToDB(MongoClient client, Handler<AsyncResult<String>> handler){

    client.insert(Const.role,this.toJson(),handler);

  }

  public void update(MongoClient client, Handler<AsyncResult<MongoClientUpdateResult>> handler){

    JsonObject query = new JsonObject().put("_id",this._id);
    JsonObject update = new JsonObject().put("$set",this.toJson());

    client.updateCollection(Const.role,query,update,handler);

  }


  public static void graderReport(MongoClient client , JsonObject user , JsonObject clientJson , Handler<AsyncResult<String>> handler){


	  JsonObject studentId = new JsonObject()
    .put("requestType",clientJson.getString("studentId"));

    client.find(Const.role , studentId , res ->{
      if(res.succeeded() && !res.result().isEmpty()){

        FormAnswer answer = new FormAnswer(clientJson.getJsonObject("answerBody"));


        client.insert(Const.answer , clientJson.getJsonObject("answerBody")
        .put("formId",clientJson.getString("formId")) , resInsert->{

          JsonObject report = new JsonObject()
            .put("_id",res.result().get(0).getString("reportId") );

          client.find(Const.report , report , resReport ->{
//            if(resReport.succeeded() && !resReport.result().isEmpty()){
            if(resReport.succeeded()){

              JsonArray answersId = resReport.result().get(0).getJsonArray("answersId");
              JsonObject query = new JsonObject()
                .put("answerId",answersId);
              JsonArray ans = answersId.add(resInsert.result());
              JsonObject update = new JsonObject()
                .put("$set", new JsonObject()
                .put("answerId",ans));
              client.updateCollection(Const.report , query , update , resUpdate ->{
                handler.handle(Future.succeededFuture());
              } );

            }

          });
        });

      }
      else{
        handler.handle(Future.failedFuture("Student not found."));
      }
    });



  }

}
