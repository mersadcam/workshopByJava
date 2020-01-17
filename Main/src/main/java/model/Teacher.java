package model;

import controller.Const;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.mongo.MongoClientUpdateResult;
import io.vertx.reactivex.ext.unit.Async;

import java.util.ArrayList;

public class Teacher implements Role,FormWriter{


	private ArrayList<Form> forms = new ArrayList<Form>();
	private EnteredCourse myCourse;


  public ArrayList<Form> getForms() {
    return forms;
  }

  public static void addForm(
    MongoClient client ,
    JsonObject jsonTeacher ,
    JsonObject jsonForm,
    Handler<AsyncResult<MongoClientUpdateResult>> handler) {

    Form form = new Form(jsonForm.getJsonObject("formBody"));
    form.addToDB(client,jsonTeacher,resAddToDB ->{

      JsonArray formsId = jsonTeacher.getJsonArray("formsId");
      formsId.add(resAddToDB.result());

      JsonObject teacherId = jsonTeacher.getJsonObject("_id");
      JsonObject toSet = new JsonObject()
        .put("$set",new JsonObject().put("formsId",formsId));

      client.updateCollection(Const.teacher,new JsonObject().put("_id",teacherId),toSet,handler);

    });
  }

}
