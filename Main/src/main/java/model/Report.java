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
	private ArrayList<FormAnswer> data = new ArrayList<FormAnswer>();

	public Report(Status studentCourseStatus , String finalNumber , Performance performance ){
	  this._id = new ObjectId().toString();
	  this.studentCourseStatus = studentCourseStatus;
	  this.finalNumber = finalNumber;
	  this.performance = performance;
  }
	public Report(JsonObject jsonObject){

	  this._id = jsonObject.getString("_id");
//	  this.studentCourseStatus = jsonObject.getBinary("studentCourseStatus"); mersad check
	  this.finalNumber = jsonObject.getString("finalNumber");
//	  this.performance = new Performance(jsonObject.getString("_id")); mersad check
    JsonArray jsonArray = jsonObject.getJsonArray("data");

    for(int i = 0 ; i < jsonArray.size() ; i++){
      data.add(new FormAnswer(jsonArray.getString(i)));
    }
  }
	public Report(String _id){
    this._id = _id;
  }

  public String get_id() {
    return _id;
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

	public void addData(FormAnswer formAnswer){
	  this.data.add(formAnswer);
  }

  public JsonObject toJson(){
	  JsonArray jsonArray = new JsonArray();
	  JsonObject jsonObject = new JsonObject();

	  for(int i = 0 ; i < data.size() ; i++){
	    jsonArray.add(data.get(i).get_id());
    }

	  jsonObject
      .put("_id",this._id)
      .put("studentStatusCourse",this.studentCourseStatus)
      .put("finalNumber",this.finalNumber)
      .put("performance",this.performance)
      .put("data",jsonArray);

	  return jsonObject;
  }

  public void saveToDB(MongoClient client, Handler<AsyncResult<String>> handler){

    client.insert(Const.report,this.toJson(),handler);

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
