package model;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Reference;
import io.vertx.core.json.JsonObject;
import org.bson.types.ObjectId;

import java.util.ArrayList;
@Entity
public class Course {

  @Id
  private ObjectId id;
	private String name;
	private String description;

	@Reference
	private CoursesRelationships neededCourses;

	public Course(JsonObject jsonObject){
	  this.name = jsonObject.getString("name");
	  this.description = jsonObject.getString("description");
  }


}
