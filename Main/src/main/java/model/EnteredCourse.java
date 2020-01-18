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

  private void setCourse(MongoClient client,String courseName,Handler<AsyncResult<String>> handler){

	  JsonObject query = new JsonObject().put("name",courseName);
	  client.find("course",query,resFind->{

	    if (resFind.result().isEmpty())
	      handler.handle(Future.failedFuture(""));
	    else
	      handler.handle(Future.succeededFuture(resFind.result().get(0).getString("_id")));
    });

  }

  public void enterNewWorkshop(MongoClient client, JsonObject json , Handler<AsyncResult<String>> handler){

	  String courseName = json.getString("course");

	  this.setCourse(client,courseName,resSC->{


	    if (resSC.succeeded()){
            JsonObject toInsert = new JsonObject()
              .put("_id", new ObjectId().toString())
              .put("course",resSC.result())
              .put("startTime",this.startTime)
              .put("finishTime",this.finishTime)
              .put("place",this.place)
              .put("capacity",this.capacity)
              .put("description",this.description)
              .put("workshopNumber",Const.getEnteredCourseId());

            client.insert("enteredCourse",toInsert,handler);


          }else{

            handler.handle(Future.failedFuture("this course name not found"));

          }



    });



  }

}
