package model;

import controller.Const;
import dev.morphia.annotations.Id;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.mongo.MongoClientUpdateResult;
import org.bson.types.ObjectId;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SimpleTimeZone;

public class EnteredCourse {

  //many to one
	private Course course;
  private String _id;
	private String startTime;
	private String finishTime;
  private JsonArray paymentParts;
  private int value;
	private String place;
	private int capacity;
	private String description;
	private ArrayList<Group> groups = new ArrayList<Group>();

  public  EnteredCourse(String _id){
    this._id = _id;
  }

	public EnteredCourse(
	  Course course ,
    String startTime,
    String finishTime ,
    String place ,
    String description ,
    int capacity,
    int value ,
    JsonArray paymentParts){

    this.value = value;
	  this._id = new ObjectId().toString();
	  this.course = course;
	  this.startTime = startTime;
	  this.finishTime = finishTime;
	  this.capacity = capacity;
	  this.description = description;
	  this.paymentParts = paymentParts;
	  this.place = place;

  }

	public EnteredCourse(JsonObject json){

	  this.startTime = json.getString("startTime");
	  this.finishTime = json.getString("finishTime");
	  this.place = json.getString("place");
	  this.capacity = (int)json.getValue("capacity");
	  this.description = json.getString("description");
	  this.value = json.getInteger("value");
	  this.paymentParts = json.getJsonArray("paymentParts");
	  this._id = json.getString("_id");
    this.course = new Course(json.getString("_id"));

    JsonArray groupsId = json.getJsonArray("groups");

    for (int i = 0 ; i < groupsId.size() ; i++){
      this.groups.add(new Group(groupsId.getString(i)));
    }


  }

  public void addGroup(Group group){
    this.groups.add(group);
  }

  public ArrayList<Group> getGroups(){
    return this.groups;
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
    JsonArray jsonArray = new JsonArray();

    for (int i = 0 ; i < this.groups.size() ; i++ ){
      jsonArray.add(this.groups.get(i).get_id());
    }

	  JsonObject json = new JsonObject()
      .put("_id",this._id)
      .put("startTime",this.startTime)
      .put("finishTime",this.finishTime)
      .put("place",this.place)
      .put("capacity",this.capacity)
      .put("description",this.description)
      .put("value",this.value)
      .put("paymentParts",this.paymentParts)
      .put("course",this.course.getName())
      .put("groups",jsonArray);

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

  public void saveToDB(MongoClient client){

    client.insert(Const.enteredCourse,this.toJson(),handler->{});

  }

  public void update(MongoClient client){

    JsonObject query = new JsonObject().put("_id",this._id);
    JsonObject update = new JsonObject().put("$set",this.toJson());

    client.updateCollection(Const.enteredCourse,query,update,handler->{});

  }


  public static void enterNewWorkshop(MongoClient client, JsonObject json , Handler<AsyncResult<String>> handler){



	  int value = json.getInteger("value");
	  JsonArray paymentParts = json.getJsonArray("paymentParts");
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
              .put("description",description)
              .put("value",value)
              .put("paymentParts",paymentParts);

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
    User user ,
    Handler<AsyncResult<String>> handler){

	  String courseName = clientJson.getString("course");

	  client.find(Const.course,new JsonObject().put("name",courseName),resFind->{

	    JsonObject course = resFind.result().get(0);
        JsonArray courseList = course.getJsonArray("neededCourse");

        User.passedCourses(client,user,resPassedCourses->{

          if (User.preCoursesPassed(courseList,resPassedCourses.result()))
              handler.handle(Future.succeededFuture(""));
          else
            handler.handle(Future.failedFuture("you need pass some courses"));

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


  public static void graderRequestForWorkshop(MongoClient client , JsonObject clientJson , User user , Handler<AsyncResult<String>> handler){

	  JsonObject enteredCourseId = new JsonObject()
      .put("_id",clientJson.getString("enteredCourseId"));

	  client.find(Const.enteredCourse , enteredCourseId , res ->{

	    if(res.succeeded() && !res.result().isEmpty()) {//find workshop in db

        EnteredCourse workshop = new EnteredCourse(res.result().get(0));

        String time = new SimpleDateFormat("HH-dd-MM-yyyy").format(new java.util.Date());
        Grader grader = new Grader( time );
        grader.saveToDB(client);
        Report report = new Report();
        report.saveToDB(client);
        Identity identity = new Identity(report ,grader , new Course(workshop.getCourseName()),"Grader");
        identity.saveToDB(client);
        user.addRole(identity);
        user.update(client);

        client.find(Const.group , new JsonObject().put("_id",workshop.getGroups().get(0).get_id()) , resFindGroup ->{
          Group group = new Group(resFindGroup.result().get(0));
          group.addIdentity(identity);
          group.update(client , resUpdate->{
            handler.handle(Future.succeededFuture());
          });
        });
      }
	    else{//didn't find workshop in db
	      handler.handle(Future.failedFuture("cannot find workshop in database"));
      }
    });
	}
}

