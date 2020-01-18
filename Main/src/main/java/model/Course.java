package model;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Reference;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import org.bson.types.ObjectId;
import java.util.ArrayList;
import java.util.List;

public class Course {


	private String name;
	private String description;

	private ArrayList<Course> neededCourses = new ArrayList<Course>();

	public Course(JsonObject jsonObject){
	  this.name = jsonObject.getString("name");
	  this.description = jsonObject.getString("description");
  }

  public void createNewCourse(MongoClient client,JsonObject json, Handler<AsyncResult<String>> handler){

	  JsonObject toFind = new JsonObject()
      .put("name",this.name);

	  client.find("course",toFind,resFind ->{

	    if(resFind.result().isEmpty()){

        JsonArray jsonArray = json.getJsonArray("neededCourses");

        JsonObject forInsert = new JsonObject()
          .put("_id",new ObjectId().toString())
          .put("name",json.getString("name"))
          .put("description",json.getString("description"))
          .put("neededCourses",json.getJsonArray("neededCourse"));

        client.insert("course",forInsert,resInsert->{

          handler.handle(Future.succeededFuture(""));

        });


      }else
	      handler.handle(Future.failedFuture("duplicated"));



    });

  }



}
