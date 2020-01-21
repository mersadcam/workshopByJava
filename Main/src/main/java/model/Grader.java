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
	private Status status;

	//contructor dige ro namidonam chitor benvisam
  //yaani faghat hamin 2 ta ro dare???

  public enum Status{
    ACCEPTED,
    NOT_ACCEPTED
  }

  //what should i do in this constructor with status
	public Grader( String requestDate ){

	  this._id = new ObjectId().toString();
	  this.requestDate = requestDate;
	  this.status = Status.NOT_ACCEPTED;

  }

  public Grader(JsonObject jsonObject){
	  this._id = jsonObject.getString("_id");
	  this.requestDate = jsonObject.getString("requestDate");
	  this.status = StringToStatus(jsonObject.getString("status"));
  }

  public Grader(){
    this._id = new ObjectId().toString();
  }

  public Status StringToStatus(String status){
    if (status.equals("ACCEPTED"))
      return Status.ACCEPTED;

    return Status.NOT_ACCEPTED;
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
      .put("requestDate",this.requestDate)
      .put("status",this.status);

	  return json;
  }

  public void saveToDB(MongoClient client, Handler<AsyncResult<String>> handler){

    client.insert(Const.grader,this.toJson(),handler);

  }

  public void update(MongoClient client, Handler<AsyncResult<MongoClientUpdateResult>> handler){

    JsonObject query = new JsonObject().put("_id",this._id);
    JsonObject update = new JsonObject().put("$set",this.toJson());

    client.updateCollection(Const.grader,query,update,handler);

  }


  public static void writerReport(MongoClient client , String roleName , JsonObject clientJson , Handler<AsyncResult<String>> handler){



    String formId = clientJson.getString("formId");
    String writerId = clientJson.getString("writer");
    String reportable = clientJson.getString("reportable");
    JsonObject answerBody = clientJson.getJsonObject("answerBody");

    client.find(Const.role , new JsonObject().put("_id",reportable) , res ->{

      if(res.succeeded() && !res.result().isEmpty()){

        FormWriter writer;
        if(roleName.equals("Teacher"))
          writer = new Teacher(writerId);


        else
          writer = new Grader(writerId);

        Form form = new Form(formId);
        FormAnswer answer = new FormAnswer(form,writer,answerBody);

        Identity identity = new Identity(res.result().get(0));

        client.find(Const.report,new JsonObject().put("_id",identity.getReportId()),resFindReport->{

          Report report = new Report(resFindReport.result().get(0));
          report.addAnswer(answer);
          answer.saveToDB(client);
          report.saveToDB(client);

          handler.handle(Future.succeededFuture("Your report entered successfully"));

        });


      }
      else{
        handler.handle(Future.failedFuture("Student not found."));
      }
    });



  }

  public void saveToDB(MongoClient client) {
    client.insert(Const.grader,this.toJson(),handler->{});
  }
}
