package model;

import controller.Const;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Reference;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.mongo.MongoClientUpdateResult;
import org.bson.types.ObjectId;
import java.util.ArrayList;
import java.util.List;

public class Course {

	private String name;
	private String category;
	private String description;
	private ArrayList<Course> neededCourses = new ArrayList<Course>();


	public Course(String name , String description ,String category){
	  this.name = name.toUpperCase();
	  this.category = category.toUpperCase();
	  this.description = description;
  }

  public Course(String name){
	  this.name = name.toUpperCase();
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public void setName(String name){
	  this.name = name;
  }

  public Course(JsonObject json){

	  this.name = json.getString("name").toUpperCase();
	  this.description = json.getString("description");
	  this.category = json.getString("category");
	  if(this.category == null)
	    this.category = "";

	  JsonArray coursesName = json.getJsonArray("neededCourses");

	  for( int i = 0 ; i < coursesName.size() ; i++ )
	    addCourse(new Course(coursesName.getString(i)));


  }

  public void addCourse(Course course){

	  this.neededCourses.add(course);

  }


  public void addToNeededCourses(Course course){

	  this.neededCourses.add(course);

  }

  public String getName() {

	  return name.toUpperCase();

  }

  public JsonObject toJson(){


    JsonObject json =  new JsonObject();
    JsonArray CoursesName = new JsonArray();

    for (int i = 0 ; i < this.neededCourses.size() ; i++ ){
      CoursesName.add(this.neededCourses.get(i).getName().toUpperCase());
    }

    json.put("name",this.getName())
      .put("description",description)
      .put("neededCourses",CoursesName)
      .put("category",this.category.toUpperCase());

    return json;

  }

  public void saveToDB(MongoClient client, Handler<AsyncResult<String>> handler){

    client.insert(Const.course,this.toJson(),handler);

  }

  public void update(MongoClient client, Handler<AsyncResult<MongoClientUpdateResult>> handler){

    JsonObject query = new JsonObject().put("name",this.name);
    JsonObject update = new JsonObject().put("$set",this.toJson());

    client.updateCollection(Const.course,query,update,handler);

  }

  public void saveToDB(MongoClient client){

    client.insert(Const.course,this.toJson(),handler->{});

  }

  public void update(MongoClient client){

    JsonObject query = new JsonObject().put("name",this.name);
    JsonObject update = new JsonObject().put("$set",this.toJson());

    client.updateCollection(Const.course,query,update,handler->{});

  }


}
