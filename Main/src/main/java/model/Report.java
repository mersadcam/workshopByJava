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
	private String finalNumber;
	private Performance performance;
	private ArrayList<FormAnswer> answer = new ArrayList<FormAnswer>();

	public Report(){
	  this._id = new ObjectId().toString();
  }

	public Report(JsonObject jsonObject){
  //enum haeii ke comment shode ro dorost kon

	  this._id = jsonObject.getString("_id");
	  this.studentCourseStatus = stringToStatus(jsonObject.getString("studentCourseStatus"));
	  this.finalNumber = jsonObject.getString("finalNumber");
	  this.performance = stringToPerformance(jsonObject.getString("performance"));
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

  public void setFinalNumber(String finalNumber) {
    this.finalNumber = finalNumber;
  }

  public void setPerformance(Performance performance) {
    this.performance = performance;
  }

  public void setStudentCourseStatus(Status studentCourseStatus) {
    this.studentCourseStatus = studentCourseStatus;
  }

  public Status stringToStatus(String status){

	  if(status.equals("PASSED"))
	    return Status.PASSED;

	  return Status.NOTPASSED;

  }

  public Performance stringToPerformance(String performance){

	  if(performance.equals("BAD"))
	    return Performance.BAD;

	  if(performance.equals("NOTBAD"))
	    return Performance.NOTBAD;

	  if(performance.equals("GOOD"))
	    return Performance.GOOD;

	  return Performance.EXCELLENT;

  }

  public void set_id(String _id) {
    this._id = _id;
  }

  enum Status{
		PASSED,
		NOTPASSED
	}

	enum Performance{
		BAD,
		NOTBAD,
		GOOD,
		EXCELLENT
	}

	public void addAnswer(FormAnswer formAnswer){
	  this.answer.add(formAnswer);
  }

  public JsonObject toJson(){
	  JsonArray jsonArray = new JsonArray();
	  JsonObject jsonObject = new JsonObject();

	  for(int i = 0 ; i < answer.size() ; i++){
	    jsonArray.add(answer.get(i).get_id());
    }

	  jsonObject
      .put("_id",this._id)
      .put("studentStatusCourse",this.studentCourseStatus)
      .put("finalNumber",this.finalNumber)
      .put("performance",this.performance)
      .put("answer",jsonArray);

	  return jsonObject;
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
