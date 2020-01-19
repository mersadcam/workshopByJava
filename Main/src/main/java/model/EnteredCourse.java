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
  //id
  //json
  //kolli
  public  EnteredCourse(String _id){
    this._id = _id;
  }

	public EnteredCourse(Course course ,String startTime, String finishTime , String place , String description ,int capacity){
	  this._id = new ObjectId().toString();
	  this.course = course;
	  this.startTime = startTime;
	  this.finishTime = finishTime;
	  this.capacity = capacity;
	  this.description = description;
	  this.place = place;
  }

	public EnteredCourse(JsonObject json){

	  this.startTime = json.getString("startTime");
	  this.finishTime = json.getString("finishTime");
	  this.place = json.getString("place");
	  this.capacity = (int)json.getValue("capacity");
	  this.description = json.getString("description");
	  this._id = json.getString("_id");
    this.course = new Course(json.getString("_id"));
  }

  public String getCourseName() {
    return this.course.getName();
  }

  public void setCourse(Course course){

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


  public static void graderRequestForWorkshop(MongoClient client , JsonObject clientJson , JsonObject userJson , Handler<AsyncResult<String>> handler){

	  JsonObject enteredCourseId = new JsonObject()
      .put("_id",clientJson.getString("enteredCourseId"));

	  client.find(Const.enteredCourse , enteredCourseId , res ->{

	    if(res.succeeded() && !res.result().isEmpty()) {//find workshop in db

	      EnteredCourse workshop = new EnteredCourse(res.result().get(0));
        ObjectId id = new ObjectId();

        JsonObject grader = new JsonObject()
          .put("_id", id.toString())
          .put("requestDate", clientJson.getString("requestDate"))
          .put("roleName", "grader")
          .put("course", workshop.getCourseName());

        client.insert(Const.role, grader, resInsertGrader -> {
          if (resInsertGrader.succeeded()) {
            JsonArray userRoles = userJson.getJsonArray("roles");

            userRoles.add(grader.getString("_id"));

            JsonObject graderNew = new JsonObject().put("roles", userRoles);
            JsonObject userRolesNew = new JsonObject().put("$set", graderNew);

            client.updateCollection(Const.user, new JsonObject().put("token", userJson.getString("token")), userRolesNew, resRolesUpdate -> {
              if (resRolesUpdate.succeeded()) {



                JsonArray workshopGroup = res.result().get(0).getJsonArray("group");

                workshopGroup.add(grader.getString("_id"));

                JsonObject workshopGroupJsonNew = new JsonObject()
                  .put("$set", new JsonObject().put("group", workshopGroup));

                client.updateCollection(Const.enteredCourse, enteredCourseId, workshopGroupJsonNew, resGroupUpdate -> {

                  if (resGroupUpdate.succeeded()) {
                    handler.handle(Future.succeededFuture("succeeded"));
                  } else {//cannot add group to enterd course collection
                    handler.handle(Future.failedFuture("cannot add group to enterd course collection"));
                  }
                });
              } else {//cannot update roles id in the user collection
                handler.handle(Future.failedFuture("cannot update roles id in the user collection"));
              }
            });
          } else {
            handler.handle(Future.failedFuture("cannot add grader to role in db "));
          }
        });
      }
	    else{//didn't find workshop in db
	      handler.handle(Future.failedFuture("cannot find workshop in database"));
      }
    });
	}


}

