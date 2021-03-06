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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Grader implements RequestType , FormWriter {

  private String _id;
	private String requestDate;
	private Status status;


	public void setStatus(Status status){
	  this.status = status;
  }

  //contructor dige ro namidonam chitor benvisam
  //yaani faghat hamin 2 ta ro dare???

  public enum Status{
    ACCEPTED,
    NOT_ACCEPTED,
    REJECTED
  }

  //what should i do in this constructor with status
	public Grader( String _id ){
	  this._id = _id;
  }

  public Grader(JsonObject jsonObject){
	  this._id = jsonObject.getString("_id");
	  this.requestDate = jsonObject.getString("requestDate");
	  this.status = StringToStatus(jsonObject.getString("status"));
  }

  public Grader(){
    this.requestDate = new SimpleDateFormat("mm:HH-dd-MM-yyyy").format(new java.util.Date());
	  this._id = new ObjectId().toString();
    this.status = Status.NOT_ACCEPTED;
  }

  public Status StringToStatus(String status){
    if (status.toUpperCase().equals("ACCEPTED"))
      return Status.ACCEPTED;

    if(status.toUpperCase().equals("REJECT"))
      return Status.REJECTED;

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

  public void update(MongoClient client){

    JsonObject query = new JsonObject().put("_id",this._id);
    JsonObject update = new JsonObject().put("$set",this.toJson());

    client.updateCollection(Const.grader,query,update,handler->{});

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
          report.update(client);

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

  public static void teacherAcceptGrader(MongoClient client, User user , JsonObject clientJson, Handler<AsyncResult<String>> handler) {
//    security part is not completed
//    JsonObject userJson = new JsonObject()
//      .put("_id",user.get_id())
//      .put("")
//    client.find(Const.role , )
//or id
      JsonObject identity = new JsonObject()
        .put("_id", clientJson.getString("_id"))
        .put("roleName","Grader");

      client.find(Const.role , identity , resFind ->{
        if(resFind.succeeded() && !resFind.result().isEmpty()){

          JsonObject grade = new JsonObject()
            .put("_id",resFind.result().get(0).getString("requestType"));

          client.find(Const.grader , grade , resFindInGrader ->{
            if (resFindInGrader.succeeded() && !resFindInGrader.result().isEmpty()){
              JsonObject js = resFindInGrader.result().get(0);
              Grader grader = new Grader(js);
              grader.setStatus(grader.StringToStatus(clientJson.getString("status")));
              grader.update(client);

              handler.handle(Future.succeededFuture("Grader Accepted."));
            }
            else {
              handler.handle(Future.failedFuture("Don't find grader."));
            }
          });
        }
        else {//if we don't find grader in the role
          handler.handle(Future.failedFuture("This Role isn't exist."));
        }
      });

  }

}
