package model;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.reactivex.ext.unit.Async;

import java.util.ArrayList;

public class Teacher implements Role,FormWriter{


	private ArrayList<Form> forms = new ArrayList<Form>();
	private EnteredCourse myCourse;


  public ArrayList<Form> getForms() {
    return forms;
  }

  public static void addForm(MongoClient client , JsonObject jsonTeacher , JsonObject jsonForm, Handler<AsyncResult<String>> handler) {
    Form form = new Form(jsonForm.getJsonObject("formBody"));
    form.addToDB(client,jsonTeacher,handler);
  }
}
