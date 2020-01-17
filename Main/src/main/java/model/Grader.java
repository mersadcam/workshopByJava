package model;

import controller.Const;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

import java.util.Date;
import java.util.List;

public class Grader implements RequestType , FormWriter {

	private Date requestDate;

	public static void graderReport(MongoClient client , JsonObject user , JsonObject clientJson , Handler<AsyncResult<String>> handler){
    System.out.println("in the grader report");

	  JsonObject studentId = new JsonObject()
    .put("requestType",clientJson.getString("studentId"));

    client.find(Const.identity , studentId , res ->{
      if(res.succeeded() && !res.result().isEmpty()){

        FormAnswer answer = new FormAnswer(clientJson.getJsonObject("answerBody"));

        client.insert(Const.answer , clientJson.getJsonObject("answerBody")
        .put("formId",clientJson.getString("formId")) , resInsert->{

          client.find(Const.report , res.result().get(0).getJsonObject("reportId") , resReport ->{
            if(resReport.succeeded() && !resReport.result().isEmpty()){
              JsonArray answersId = resReport.result().get(0).getJsonArray("answersId");
              answersId.add(resInsert.result());
              handler.handle(Future.succeededFuture());
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
