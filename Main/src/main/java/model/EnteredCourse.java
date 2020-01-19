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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EnteredCourse {

  //many to one
	private Course course;
  private String _id;
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
	  this._id = String.valueOf(Const.getEnteredCourseId());

  }

  public void addCourse(Course course){

	  this.course =course;

  }

  public String get_id() {
    return _id;
  }

  public JsonObject toJson(){

	  JsonObject json = new JsonObject()
      .put("_id",this._id)
      .put("startTime",this.startTime)
      .put("finishTime",this.finishTime)
      .put("place",this.place)
      .put("capacity",this.capacity)
      .put("description",this.description)
      .put("course",this.course.getName());

	  return json;
  }

  public void saveToDB(MongoClient client, Handler<AsyncResult<String>> handler){

    client.insert(Const.enteredCourse,this.toJson(),handler);

  }

  public void update(MongoClient client, Handler<AsyncResult<MongoClientUpdateResult>> handler){

    JsonObject query = new JsonObject().put("_id",this._id);
    JsonObject update = new JsonObject().put("$set",this.toJson());

    client.updateCollection(Const.enteredCourse,query,update,handler);

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


  public static void studentRequestStatus(
    MongoClient client ,
    JsonObject clientJson ,
    JsonObject userJson ,
    Handler<AsyncResult<String>> handler){

	  String courseName = clientJson.getString("course");

	  client.find(Const.course,new JsonObject().put("name",courseName),resFind->{

	    JsonObject course = resFind.result().get(0);
        JsonArray courseList = course.getJsonArray("neededCourse");

        User.passedCourses(client,userJson,resPassedCourses->{

          if (User.preCoursesPassed(courseList,resPassedCourses.result()))
              handler.handle(Future.succeededFuture(""));
          else
            handler.handle(Future.failedFuture("your need pass some courses"));



        });






    });


  }

  public static void returnCourses(
    MongoClient client,
    ArrayList<JsonObject> arr,
    List neededCourse ,
    int counter ,
    Handler<AsyncResult<ArrayList<JsonObject>>> handler){


	  if( counter == neededCourse.size())
	    handler.handle(Future.succeededFuture(arr));

	  else{

	    client.find(Const.course,new JsonObject().put("name",neededCourse.get(counter)),res->{

	      arr.add(res.result().get(0));
	      returnCourses(client,arr,neededCourse,counter+1,handler);

      });

    }


  }


  public static void graderRequestForWorkshop(MongoClient client , JsonObject clientJson , Handler<AsyncResult<String>> handler){


  }
}
