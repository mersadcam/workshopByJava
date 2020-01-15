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


  private String _id;
	private String name;
	private String description;

	private ArrayList<String> neededCourses = new ArrayList<String>();

	private void setCourses(MongoClient client, JsonArray jsonArray){

	  List<String> list = jsonArray.getList();
	  String courseName;

	  for (int i = 0 ; i < list.size() ; i++ ){
	    courseName = list.get(i);

	    client.find("course",new JsonObject().put("name",courseName),res->{

	      if(!res.result().isEmpty())
	        this.neededCourses.add(res.result().get(0).getString("_id"));

      });

    }

  }

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
        this.setCourses(client,jsonArray);

        JsonObject forInsert = new JsonObject()
          .put("_id",new ObjectId().toString())
          .put("name",this.name)
          .put("description",this.description)
          .put("neededCourses",this.neededCourses);

        client.insert("course",forInsert,resInsert->{

          handler.handle(Future.succeededFuture(""));

        });


      }else
	      handler.handle(Future.failedFuture("duplicated"));



    });

  }



}
