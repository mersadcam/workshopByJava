package model;

import controller.Const;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.mongo.MongoClientUpdateResult;
import org.bson.types.ObjectId;

public class Identity implements Role {

  String _id ;
  Report report;
	RequestType requestType;
	Course course;
  String roleName;

  public Identity(String _id){
    this._id = _id;
  }

  public Identity(Report report , RequestType type , Course course , String roleName){
    this._id = new ObjectId().toString();
    this.requestType = type;
    this.report = report;
    this.course = course;
    this.roleName = roleName;
  }

  public Identity(JsonObject jsonObject){
    this._id = jsonObject.getString("_id");
    this.roleName = jsonObject.getString("roleName");
    this.report = new Report(jsonObject.getString("report"));
    this.course = new Course(jsonObject.getString("course"));
  }

  public void setType(RequestType type) {
    this.requestType = type;
  }

  //need find or json or update for data base??
  @Override
  public String get_id() {
    return this._id;
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject()
      .put("_id",this._id)
      .put("report",this.report.get_id())
      .put("requestType",this.requestType.get_id())
      .put("course",this.course.getName())
      .put("roleName",this.roleName);

    return json;
  }


  public void saveToDB(MongoClient client, Handler<AsyncResult<String>> handler){

    client.insert(Const.role, this.toJson() ,handler);

  }

  public void saveToDB(MongoClient client){

    client.insert(Const.role, this.toJson() ,handler->{});

  }

  public void update(MongoClient client, Handler<AsyncResult<MongoClientUpdateResult>> handler){

    JsonObject query = new JsonObject().put("_id",this._id);
    JsonObject update = new JsonObject().put("$set",this.toJson());

    client.updateCollection(Const.role,query,update,handler);

  }

  public void update(MongoClient client){

    JsonObject query = new JsonObject().put("_id",this._id);
    JsonObject update = new JsonObject().put("$set",this.toJson());

    client.updateCollection(Const.role,query,update,handler->{});

  }
}

