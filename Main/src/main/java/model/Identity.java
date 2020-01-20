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
	RequestType type;
	Course course;
  String roleName;

  public Identity(String _id){
    this._id = _id;
  }

  public Identity(Report report , RequestType type , Course course , String roleName){
    this._id = new ObjectId().toString();
    this.type = type;
    this.report = report;
    this.course = course;
    this.roleName = roleName;
  }

  public Identity(JsonObject jsonObject){
    // inja baadesh namidanam che konam ???
    this._id = jsonObject.getString("_id");
    this.roleName = jsonObject.getString("roleName");
    this.report = new Report(jsonObject.getString("report"));
    this.course = new Course(jsonObject.getString("course"));
  }

  public void setType(RequestType type) {
    this.type = type;
  }

  //need find or json or update for data base??
  @Override
  public String get_id() {
    return this._id;
  }
}
