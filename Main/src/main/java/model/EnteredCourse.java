package model;

import controller.Const;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;

public class EnteredCourse {

  //many to one
	private Course course;

	private String startTime;
	private String finishTime;

	private String place;
	private int capacity;
	private String description;

	public EnteredCourse(JsonObject json){

	  this.startTime = json.getString("startTime");
	  this.finishTime = json.getString("finishTime");
	  this.place = json.getString("place");
	  this.capacity = (int)json.getValue("capacity");
	  this.description = json.getString("description");

  }


  public static void enterNewWorkshop(MongoClient client, JsonObject json , Handler<AsyncResult<String>> handler){

	  String courseName = json.getString("course");
	  String startTime = json.getString("startTime");
	  String finishTime = json.getString("finishTime");
	  String place = json.getString("place");
	  String description = json.getString("description");
	  int capacity = json.getInteger("capacity");

	  client.find(Const.course,new JsonObject().put("name",courseName),resSC->{


	    if (resSC.succeeded()){
            JsonObject toInsert = new JsonObject()
              .put("_id", String.valueOf(Const.getEnteredCourseId()))
              .put("course",resSC.result().get(0).getString("name"))
              .put("startTime",startTime)
              .put("finishTime",finishTime)
              .put("place",place)
              .put("capacity",capacity)
              .put("description",description);

            client.insert(Const.enteredCourse,toInsert,resInsert->{

              handler.handle(Future.succeededFuture(toInsert.getString("_id")));

            });


          }else{

            handler.handle(Future.failedFuture("this course name not found"));

          }



    });



  }


  public static void studentRequestForWorkshop(MongoClient client , JsonObject clientJson , JsonObject userJson , Handler<AsyncResult<String>> handler){

    client.find(Const.enteredCourse , clientJson.getJsonObject("enteredCourseId") , res ->{
      if(res.succeeded() && !res.result().isEmpty()){

        JsonObject selectedWorkshop = res.result().get(0);
        JsonObject course = selectedWorkshop.getJsonObject("course");//course of workshop

        //find course in db for pishniaz
        client.find(Const.course , course , resCourse ->{
          if(resCourse.succeeded() && !resCourse.result().isEmpty()){//find course in db
            JsonObject pishniaz = resCourse.result().get(0).getJsonObject("neededCourse");//id haye pishniaz


            User.returnRoles(client,new ArrayList<JsonObject>() , userJson.getJsonArray("role").getList() ,0 , resRoles->{

              ArrayList<JsonObject> roles = resRoles.result();


            } );
          }
          else{//didn't find course in db

          }
        });
      }

    });
  }


  public static void graderRequestForWorkshop(MongoClient client , JsonObject clientJson , Handler<AsyncResult<String>> handler){


  }
}
