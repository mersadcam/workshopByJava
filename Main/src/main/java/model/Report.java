package model;

import controller.Const;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.mongo.MongoClientUpdateResult;
import org.bson.types.ObjectId;

import java.util.ArrayList;

public class Report {

  private String _id;
	private Status studentCourseStatus;
	private double finalNumber;
	private double completeNumber;
	private ArrayList<FormAnswer> answer = new ArrayList<FormAnswer>();

	public Report(){
	  this._id = new ObjectId().toString();
	  this.studentCourseStatus = Status.NOTPASSED;
	  this.completeNumber = 100;
  }

	public Report(JsonObject jsonObject){

	  this._id = jsonObject.getString("_id");
	  this.studentCourseStatus = stringToStatus(jsonObject.getString("studentCourseStatus"));
	  this.finalNumber = jsonObject.getDouble("finalNumber");
	  this.completeNumber = jsonObject.getDouble("completeNumber");
    JsonArray jsonArray = jsonObject.getJsonArray("answer");

    for(int i = 0 ; i < jsonArray.size() ; i++){
      answer.add(new FormAnswer(jsonArray.getString(i)));
    }
  }

	public Report(String _id){
    this._id = _id;
  }

  public String get_id() {
    return _id;
  }

  public void setFinalNumber(double finalNumber) {
    this.finalNumber = finalNumber;
  }

  public void setStudentCourseStatus(String studentCourseStatus) {
    this.studentCourseStatus = stringToStatus(studentCourseStatus);
  }

  public Status stringToStatus(String status){

	  if(status.equals("PASSED"))
	    return Status.PASSED;
	  return Status.NOTPASSED;

  }


  public void set_id(String _id) {
    this._id = _id;
  }

  enum Status{
		PASSED,
		NOTPASSED
	}

	public void addAnswer(FormAnswer formAnswer){
	  this.answer.add(formAnswer);
  }

  public double getCompleteNumber() {
    return completeNumber;
  }

  public double getFinalNumber() {
    return finalNumber;
  }

  public Status getStudentCourseStatus() {
    return studentCourseStatus;
  }

  public ArrayList<FormAnswer> getAnswer() {
    return answer;
  }

  public JsonObject toJson(){
	  JsonArray jsonArray = new JsonArray();
	  JsonObject jsonObject = new JsonObject();

	  for(int i = 0 ; i < answer.size() ; i++){
	    jsonArray.add(answer.get(i).get_id());
    }

	  jsonObject
      .put("_id",this._id)
      .put("studentCourseStatus",this.studentCourseStatus)
      .put("finalNumber",this.finalNumber)
      .put("answer",jsonArray);

	  return jsonObject;
  }

  public void setCompleteNumber(double completeNumber) {
    this.completeNumber = completeNumber;
  }

  public void saveToDB(MongoClient client, Handler<AsyncResult<String>> handler){

    client.insert(Const.report,this.toJson(),handler);

  }

  public void saveToDB(MongoClient client){

    client.insert(Const.report,this.toJson(),handler->{});

  }

  public void update(MongoClient client, Handler<AsyncResult<MongoClientUpdateResult>> handler) {

    JsonObject query = new JsonObject().put("_id", this._id);
    JsonObject update = new JsonObject().put("$set", this.toJson());

    client.updateCollection(Const.report, query, update, handler);
  }

  public void update(MongoClient client) {

    JsonObject query = new JsonObject().put("_id", this._id);
    JsonObject update = new JsonObject().put("$set", this.toJson());

    client.updateCollection(Const.report, query, update, handler->{});
  }

//	public static void addReport(MongoClient client , String answerId , String reportId){
//    JsonObject report = new JsonObject()
//      .put("_id", reportId);
//
//	  client.find(Const.report , report , res ->{
//	    if(res.succeeded() && !res.result().isEmpty()){
//	      JsonObject answerIds = res.result().get(0).getJsonObject("answerIds");
//	      answerIds.put()
//	      client.updateCollection()
//	      res.result().get(0)
//      }
//	    else{
//
//      }
//    });
//  }

}
