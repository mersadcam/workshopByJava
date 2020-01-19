package model;

import controller.Const;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

import java.util.ArrayList;

public class Report {

	private Status studentCourseStatus;
	private String finalNumber;
	private Performance performance;
	private ArrayList<FormAnswer> answer = new ArrayList<FormAnswer>();


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
