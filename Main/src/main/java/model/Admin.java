package model;

import io.vertx.core.json.JsonObject;

import java.util.ArrayList;

public class Admin extends User {

	private ArrayList<EnteredCourse> enteredCourses = new ArrayList<EnteredCourse>();

  public Admin(JsonObject json) {
    super(json);
  }
}
