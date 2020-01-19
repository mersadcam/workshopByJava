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
    this._id = jsonObject.getString("_id");
    //this part????
  }

  //need find or json or update for data base??
  @Override
  public String get_id() {
    return this._id;
  }
}
