package model;

import io.vertx.core.json.JsonObject;
import org.bson.types.ObjectId;

public class Course {


  private ObjectId id;
	private String name;
	private String description;


	private CoursesRelationships neededCourses;

	public Course(JsonObject jsonObject){
	  this.name = jsonObject.getString("name");
	  this.description = jsonObject.getString("description");
  }


}
