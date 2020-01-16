package model;

import io.vertx.core.json.JsonObject;

import java.util.ArrayList;

public class sAdmin extends User {

	private ArrayList<EnteredCourse> enteredCourses = new ArrayList<EnteredCourse>();

  public sAdmin(JsonObject json) {
    super(json);
  }
}
