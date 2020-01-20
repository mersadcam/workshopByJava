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
	private String requestDate;

	//contructor dige ro namidonam chitor benvisam
  //yaani faghat hamin 2 ta ro dare???

	public Grader( String requestDate ){

	  this._id = new ObjectId().toString();
	  this.requestDate = requestDate;

  }

  public Grader(JsonObject jsonObject){
	  this._id = jsonObject.getString("_id");
	  this.requestDate = jsonObject.getString("requestDate");
  }
  public Grader(){

  }

  public void set_id(String _id) {
    this._id = _id;
  }

  public String get_id() {
    return _id;
  }

  public JsonObject toJson(){

	  JsonObject json = new JsonObject()
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


  public static void graderReport(MongoClient client , JsonObject userJson , JsonObject clientJson , Handler<AsyncResult<String>> handler){


	  JsonObject studentId = new JsonObject()
    .put("_id",clientJson.getString("studentId"));
    //#Change

    client.find(Const.role , studentId , res ->{
      if(res.succeeded() && !res.result().isEmpty()){

        FormAnswer answer = new FormAnswer(clientJson.getJsonObject("answerBody"));


        client.insert(Const.answer , new JsonObject().put("answerBody",clientJson.getJsonArray("answerBody"))
        .put("form",clientJson.getString("formId")).put("writer",clientJson.getString("graderId")) , resInsert->{

          JsonObject report = new JsonObject()
            .put("_id",res.result().get(0).getString("report") );

          client.find(Const.report , report , resReport ->{
//            if(resReport.succeeded() && !resReport.result().isEmpty()){


            if(resReport.succeeded()){
              //there is no report atfirst : Error
              JsonArray answersId = resReport.result().get(0).getJsonArray("answersId");
              JsonObject query = new JsonObject()
                .put("answer",answersId);
              JsonArray ans = answersId.add(resInsert.result());
              JsonObject update = new JsonObject()
                .put("$set", new JsonObject()
                .put("answer",ans));
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
