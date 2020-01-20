package model;

import controller.Const;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.mongo.MongoClientUpdateResult;
import io.vertx.reactivex.ext.unit.Async;
import org.bson.types.ObjectId;

import java.util.ArrayList;

public class Teacher implements Role,FormWriter{

  private String _id;
  private String roleName;
	private ArrayList<Form> forms = new ArrayList<Form>();
	private EnteredCourse enteredCourse;

	public Teacher(EnteredCourse enteredCourse){

	  this.enteredCourse = enteredCourse;
	  this.roleName = "Teacher";
	  this._id = new ObjectId().toString();

  }
  public Teacher(){}

  public Teacher(String _id){
	  this._id = _id;
  }

  public Teacher(JsonObject jsonObject){
	  this._id = jsonObject.getString("_id");
	  this.roleName = jsonObject.getString("roleName");
	  this.enteredCourse = new EnteredCourse(jsonObject.getString("enteredCourse"));

	  JsonArray formsArray = jsonObject.getJsonArray("forms");

	  for(int i = 0 ; i < formsArray.size() ; i++){
	    this.forms.add(new Form(formsArray.getString(i)));
    }
  }

  public void addForm(Form form){

	  this.forms.add(form);

  }
  public ArrayList<Form> getForms() {
    return forms;
  }

  public JsonObject toJson(){

	  JsonArray formsTeacher = new JsonArray();

    for (int i = 0 ; i < this.forms.size() ; i++ ){
      formsTeacher.add(this.forms.get(i).get_id());
    }

	  JsonObject json = new JsonObject()
      .put("roleName",this.roleName)
      .put("_id",this._id)
      .put("form", formsTeacher)
      .put("enteredCourse",enteredCourse.get_id());

	  return json;

  }

  public void saveToDB(MongoClient client, Handler<AsyncResult<String>> handler){

    client.insert(Const.role,this.toJson(),handler);

  }

  public void update(MongoClient client, Handler<AsyncResult<MongoClientUpdateResult>> handler){

    JsonObject query = new JsonObject().put("_id",this._id);
    JsonObject update = new JsonObject().put("$set",this.toJson());

    client.updateCollection(Const.role,query,update,handler);

  }


  public static void addNewForm(
    MongoClient client ,
    JsonObject jsonTeacher ,
    JsonObject jsonForm,
    Handler<AsyncResult<MongoClientUpdateResult>> handler) {

    Form form = new Form(jsonForm.getJsonObject("formBody"));
    form.addToDB(client,resAddToDB ->{

      String formId = resAddToDB.result();
      JsonArray formsId = jsonTeacher.getJsonArray("formsId");

      if( formsId == null)
        formsId = new JsonArray();

      formsId.add(formId);

      String teacherId = jsonTeacher.getString("_id");
      JsonObject toSet = new JsonObject()
        .put("$set",new JsonObject().put("form",formsId));

      client.updateCollection(Const.role,new JsonObject().put("_id",teacherId),toSet,handler);

    });
  }

  @Override
  public String get_id() {
    return this._id;
  }
}
