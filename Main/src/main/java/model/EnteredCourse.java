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
import io.vertx.reactivex.ext.unit.Async;
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
	private String name;
	private String category;
  private String _id;
	private String startTime;
	private String finishTime;
  private JsonObject paymentParts;
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
    String name,
    String startTime,
    String finishTime ,
    String place ,
    String description ,
    int capacity,
    int value ,
    JsonObject paymentParts){

    this.value = value;
	  this._id = Const.generateWorkshopId();
	  this.course = course;
	  this.name = name;
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
	  this.name = json.getString("name");
	  this.place = json.getString("place");
	  this.capacity = (int)json.getValue("capacity");
	  this.description = json.getString("description");
	  this.value = json.getInteger("value");
	  this.paymentParts = json.getJsonObject("paymentParts");
	  this._id = json.getString("_id");
    this.course = new Course(json.getString("course").toUpperCase());

    JsonArray groupsId = json.getJsonArray("groups");

    for (int i = 0 ; i < groupsId.size() ; i++){
      this.groups.add(new Group(groupsId.getString(i)));
    }


  }

  public void addGroup(Group group){
    this.groups.add(group);
  }

  public String getCourseName() {
    return this.course.getName().toUpperCase();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setCourse(Course course){

	  this.course =course;

  }

  public int getValue() {
    return value;
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
      .put("name",this.name)
      .put("startTime",this.startTime)
      .put("finishTime",this.finishTime)
      .put("place",this.place)
      .put("capacity",this.capacity)
      .put("description",this.description)
      .put("value",this.value)
      .put("paymentParts",this.paymentParts)
      .put("course",this.course.getName().toUpperCase())
      .put("groups",jsonArray);

	  return json;
  }

  public JsonObject getPaymentParts() {
    return paymentParts;
  }

  public ArrayList<Group> getGroups() {
    return groups;
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

  public static void enterNewWorkshop(MongoClient client, JsonObject json , Handler<AsyncResult<JsonObject>> handler){

	  int value = json.getInteger("value");
	  JsonObject paymentParts = json.getJsonObject("paymentParts");
	  String courseName = json.getString("course").toUpperCase();
	  String startTime = json.getString("startTime");
	  String finishTime = json.getString("finishTime");
	  String place = json.getString("place");
	  String name = json.getString("name");
	  String description = json.getString("description");
	  int capacity = json.getInteger("capacity");

	  client.find(Const.course,new JsonObject().put("name",courseName),resSC->{


	    if (!resSC.result().isEmpty()){
	      Course course = new Course(resSC.result().get(0));
            EnteredCourse newWorkshop = new EnteredCourse(course ,
              name,
              startTime,
              finishTime ,
              place ,
              description ,
              capacity,
              value ,
              paymentParts);

              Group gp = new Group();
              Teacher teacher = new Teacher();
              newWorkshop.addGroup(gp);
              teacher.setEnteredCourse(newWorkshop);

        client.find(Const.user,new JsonObject().put("username",json.getString("teacher")) , res->{

              if(!res.result().isEmpty()) {

                User user = new User(res.result().get(0));
                gp.saveToDB(client);
                newWorkshop.saveToDB(client);
                teacher.saveToDB(client);
                user.addRole(teacher);
                user.update(client);
                handler.handle(Future.succeededFuture(newWorkshop.toJson()));

              }
              else{

                handler.handle(Future.failedFuture("This user not found"));

              }

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

	  String courseName = clientJson.getString("course").toUpperCase();

	  client.find(Const.course,new JsonObject().put("name",courseName),resFind->{

	    JsonObject course = resFind.result().get(0);
        JsonArray courseList = course.getJsonArray("neededCourses");

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


  public static void graderRequestForWorkshop(
    MongoClient client ,
    JsonObject clientJson ,
    User user ,
    Handler<AsyncResult<String>> handler){

	  JsonObject enteredCourseId = new JsonObject()
      .put("_id",clientJson.getString("enteredCourseId"));

	  client.find(Const.enteredCourse , enteredCourseId , res ->{

	    if(res.succeeded() && !res.result().isEmpty()) {//find workshop in db

        EnteredCourse workshop = new EnteredCourse(res.result().get(0));

        Grader grader = new Grader();
        grader.saveToDB(client);
        Report report = new Report();
        report.saveToDB(client);
        Identity identity = new Identity(report ,grader , workshop,"Grader");
        identity.saveToDB(client);
        user.addRole(identity);
        user.update(client);

        client.find(Const.group , new JsonObject().put("_id",workshop.getGroups().get(0).get_id()) , resFindGroup ->{
          Group group = new Group(resFindGroup.result().get(0));
          group.addIdentity(identity);
          group.update(client , resUpdate->{
            handler.handle(Future.succeededFuture("succeeded."));
          });
        });
      }
	    else{//didn't find workshop in db
	      handler.handle(Future.failedFuture("cannot find workshop in database."));
      }
    });
	}

	//in this function we find person role in one workshop
  public static void workshopStar(MongoClient client , JsonObject clientJson , String roleId ,
                                  User user , Handler<AsyncResult<String>> handler){

    EnteredCourse workshopId = new EnteredCourse(clientJson.getString("enteredCourseId"));
    JsonObject searchWorkshop = new JsonObject().put("_id" , workshopId.get_id());

    client.find(Const.enteredCourse , searchWorkshop , resSearchWorkshop ->{
      if(resSearchWorkshop.succeeded() && !resSearchWorkshop.result().isEmpty()){//if we find workshop

        EnteredCourse findedWorkshop = new EnteredCourse(resSearchWorkshop.result().get(0));

        if(roleId.equals("")){ //we don't have role id
          //should find it
          User.findRoleId(client, user , findedWorkshop , handler);

        }
        else{ //we have role id
           User.searchInRoleId(user ,roleId , handler);
          //check and search between user roles if exist do nothing if don't exist send response
        }
      }
      else{ //if we don't find workshop
        handler.handle(Future.failedFuture("Don't find workshop."));
      }
    });
  }

  public static void myWorkshops(
    MongoClient client,
    ArrayList<JsonObject> workshops,
    List rolesId,
    int counter,
    Handler<AsyncResult<ArrayList<JsonObject>>> handler){

    if (counter == rolesId.size()){

      handler.handle(Future.succeededFuture(workshops));

    }

    else {

      client.find(Const.role,new JsonObject().put("_id",rolesId.get(counter)),resRole->{

        client.find(Const.enteredCourse,new JsonObject().put("_id",resRole.result().get(0).getString("enteredCourse")),resWork->{
          if (resWork.succeeded()){
            JsonObject jsonObject = new JsonObject().put("workshop",resWork.result().get(0));

            if (resRole.result().get(0).getString("roleName").equals("Teacher")){

              Teacher teacher = new Teacher(resRole.result().get(0));
              jsonObject.put("role",teacher.toJson());

            }
            else{

              Identity identity = new Identity(resWork.result().get(0));
              jsonObject.put("role",identity.toJson().put("status",identity.report.getStudentCourseStatus()));

            }

            workshops.add(jsonObject);
            myWorkshops(client,workshops,rolesId,counter+1,handler);

          }

        });

      });

    }

  }

  public static void allWorkshopsWithTeacher(MongoClient client , Handler<AsyncResult<JsonObject>> handler){

    JsonObject id = new JsonObject();

    client.find(Const.enteredCourse , id , resFind ->{
      if (resFind.succeeded() && !resFind.result().isEmpty()) {
        EnteredCourse.findTeacherRecursive(client,resFind.result().size() ,resFind.result() , new ArrayList<JsonObject>(), resWorkshops->{
          ArrayList<JsonObject> toSend = resWorkshops.result();
          handler.handle(Future.succeededFuture(new JsonObject().put("body" , toSend.toString())));
        });
      }
    });

  }

  public static void findTeacherRecursive(MongoClient client , int counter , List<JsonObject> result , ArrayList<JsonObject> workshops ,
                                          Handler<AsyncResult<ArrayList<JsonObject>>> handler){
    if (counter == -1){
      handler.handle(Future.succeededFuture(workshops));
    }
    else {
      JsonObject json = new JsonObject().put("_id",result.get(counter).getString("_id"));

      client.find(Const.enteredCourse , json , resFind ->{
        JsonObject workshop = new JsonObject();

        if (resFind.succeeded() && !resFind.result().isEmpty()){
          client.find(Const.role , new JsonObject().put("enteredCourse",result.get(counter).getString("_id"))
            .put("roleName","Teacher") , resFindRole ->{

            if (resFindRole.succeeded() && !resFindRole.result().isEmpty()){
              workshop.put("enteredCourse",result.get(counter).getString("_id"))
                .put("Teacher",resFindRole.result().get(0).getString("roleName"))
                .put("course",resFind.result().get(0).getString("name"));
            }
            workshops.add(workshop);
            findTeacherRecursive(client,counter-1,result,workshops , handler);
          });
        }
        else {
          workshop.put("workshop","not found");
          workshops.add(workshop);
          findTeacherRecursive(client,counter - 1 , result , workshops , handler);
        }
      });
    }
  }

  public static void getTeacherUser(MongoClient client , String workshopId , Handler<AsyncResult<String>> handler){

    JsonObject workShopId = new JsonObject().put("_id",workshopId)
      .put("roleName","Teacher");
    client.find(Const.role , workShopId , res->{
      if (res.succeeded() && !res.result().isEmpty()){

      }
    });
  }

}

