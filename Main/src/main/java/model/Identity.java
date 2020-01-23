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
	EnteredCourse enteredCourse;
  String roleName;

  public Identity(String _id){
    this._id = _id;
  }

  public Identity(Report report , RequestType type , EnteredCourse enteredCourse , String roleName){
    this._id = new ObjectId().toString();
    this.requestType = type;
    this.report = report;
    this.enteredCourse = enteredCourse;
    this.roleName = roleName;
  }

  public Identity(JsonObject jsonObject){
    this._id = jsonObject.getString("_id");
    this.roleName = jsonObject.getString("roleName");
    this.report = new Report(jsonObject.getString("report"));
    this.enteredCourse = new EnteredCourse(jsonObject.getString("enteredCourse"));
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
      .put("enteredCourse",this.enteredCourse.get_id())
      .put("roleName",this.roleName);

    return json;
  }

  @Override
  public String getRoleName() {
    return roleName;
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


  public String getReportId() {
    return report.get_id();
  }


  public String getRequestTypeId() {
    return requestType.get_id();
  }


//  public static void findRole(MongoClient client , String role){
//    JsonObject result = new JsonObject();
//
//    client.find(Const.role , new JsonObject().put("_id",role) , res->{
//
//      if(res.succeeded() && !res.result().isEmpty()){
//        result.put("roleName",res.result().get(0).getString("roleName"));
//        result.put("_id" ,res.result().get(0).getString("_id"));
//
//        if (!result.getString("roleName").equals("Teacher")){
//          result.put("enteredCourse", res.result().get(0).getString("enteredCourse"));
//        }
//        else {
//          client.find(Const.enteredCourse , new JsonObject().put("_id",res.result().get(0).getString("enteredCourse")) , resFind->{
//            if (resFind.succeeded() && !resFind.result().isEmpty()){
//              result.put("enteredCourse",resFind.result().get(0).getString("enteredCourse"));
//              final JsonObject result1 = result;
//            }
//            else{//if we don't find entered course
//              result.put("enteredCourse","null");
//
//            }
//          });
//        }
//
//      }
//      else {
//        result.put("roleName","null");
//      }
//
//    });
//  }
}

