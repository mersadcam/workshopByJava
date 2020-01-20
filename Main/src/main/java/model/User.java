package model;

import controller.Const;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.mongo.MongoClientUpdateResult;
import io.vertx.reactivex.ext.unit.Async;
import org.bson.types.ObjectId;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

//import java.util.List;
//import model.ContactPoint;

public class User {

  private static final SecureRandom secureRandom = new SecureRandom(); //this is for token
  private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //this is for token

  private ArrayList<Role> roles = new ArrayList<Role>();

  //OneToOne relationship :
  private String _id;
  private ContactPoint contactPoint;
  private String username;
  private String password;
  private String token;
  private String userType;

  public User(JsonObject json) {

    this._id = json.getString("_id");
    this.username = json.getString("username");
    this.password = json.getString("password");
    this.token = json.getString("token");
    this.userType = json.getString("userType");
    this.contactPoint = new ContactPoint(json.getString("contactPoint"));

    JsonArray rolesId = json.getJsonArray("identities");

    for (int i = 0 ; i < rolesId.size() ; i++){
      this.roles.add(new Identity(rolesId.getString(i)));
    }

  }

  public User(String _id){
    this._id = _id;
  }

  public User(ContactPoint contactPoint , String username , String password , String token , String userType){
    this._id = new ObjectId().toString();
    this.contactPoint = contactPoint;
    this.username = username;
    this.password = password;
    this.token = token;
    this.userType = userType;
  }

  public void setContactPoint(ContactPoint contactPoint) {
    this.contactPoint = contactPoint;
  }

  public void addRole(Role role){

    this.roles.add(role);

  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getToken() {
    return token;
  }

  public static String generateNewToken() {
    byte[] randomBytes = new byte[24];
    secureRandom.nextBytes(randomBytes);
    return base64Encoder.encodeToString(randomBytes);
  }

  public void setToken() {
    this.token = generateNewToken();
  }

  public JsonObject toJson(){

    JsonObject json = new JsonObject();
    JsonArray roleJsonArray = new JsonArray();

    for (int i = 0 ; i < this.roles.size() ; i++ ){
      roleJsonArray.add(this.roles.get(i).get_id());
    }



    json.put("username",this.username)
      .put("password",this.password)
      .put("token",this.token)
      .put("userType",this.userType)
      .put("roles",roleJsonArray)
      .put("contactPoint",this.contactPoint.get_id());

    return json;

  }

  public void saveToDB(MongoClient client, Handler<AsyncResult<String>> handler){

    client.insert(Const.user,this.toJson(),handler);

  }

  public void update(MongoClient client, Handler<AsyncResult<MongoClientUpdateResult>> handler){

    JsonObject query = new JsonObject().put("token",this.token);
    JsonObject update = new JsonObject().put("$set",this.toJson());

    client.updateCollection(Const.user,query,update,handler);

  }
  public void saveToDB(MongoClient client){

    client.insert(Const.user,this.toJson(),handler->{});

  }

  public void update(MongoClient client){

    JsonObject query = new JsonObject().put("token",this.token);
    JsonObject update = new JsonObject().put("$set",this.toJson());

    client.updateCollection(Const.user,query,update,handler->{});

  }

  public ArrayList<Role> getRoles() {
    return roles;
  }


  public ArrayList<String> getRolesId(){

    ArrayList<String> ids = new ArrayList<String>();

    for (int i = 0 ; i < this.roles.size() ; i++ )
      ids.add(this.roles.get(i).get_id());

    return ids;

  }

  public void login(MongoClient client , Handler<AsyncResult<String>> handler){

    JsonObject query = new JsonObject()
      .put("username",this.username)
      .put("password",this.password);

    client.find(Const.user,query,res->{

      if (!res.result().isEmpty()){
        this.setToken();
        JsonObject token =  new JsonObject()
          .put("token", this.token)
          .put("userType",res.result().get(0).getString("userType"));

        client.updateCollection(Const.user , query , new JsonObject().put("$set",new JsonObject()
        .put("token",this.token)),result->{

          handler.handle(Future.succeededFuture(token.toString()));

        });



      }else{

        handler.handle(Future.failedFuture(""));


      }


    });

  }

  public void register(MongoClient client,String contactPointId , Handler<AsyncResult<String>> handler ) {



    //startTransaction :

    client.find(Const.user,new JsonObject()
    .put("username",this.username),res->{

      if(res.result().isEmpty()){
        this.setToken();
        JsonObject json = new JsonObject()
          .put("username",this.username)
          .put("password",this.password)
          .put("token",this.token)
          .put("contactPoint",contactPointId)
          .put("userType","user");

        client.insert("user",json,ctx->{

          JsonObject token = new JsonObject()
            .put("token",this.token);
          handler.handle(Future.succeededFuture(token.toString()));

        });

      }else{

        handler.handle(Future.failedFuture(""));

      }


    });

    //commitTransaction

  }

  public void editProfile(MongoClient client,JsonObject editJson,String collection ,String contactPoint_id,Handler<AsyncResult<MongoClientUpdateResult>> handler){

    String username,gender,emailAddress,firstName,lastName;

    username = editJson.getString("username");
    firstName = editJson.getString("firstName");
    lastName = editJson.getString("lastName");
    gender = editJson.getString("gender");
    emailAddress = editJson.getString("emailAddress");

    if ( username == null ||
    firstName == null ||
    lastName == null ||
    gender == null ||
    emailAddress == null ){
      handler.handle(Future.failedFuture(""));
    }else {

      client.find(collection , new JsonObject().put("username", username), resDup -> {

        if (this.username.equals(username) || resDup.result().isEmpty()) {

          JsonObject toSet = new JsonObject().put("username",username);
          JsonObject update = new JsonObject().put("$set", toSet);

          client.updateCollection(collection,new JsonObject().put("token",this.token), update , resUp->{



            ContactPoint.editContactPoint(client,editJson,contactPoint_id, handler);

          } );

        }
        else{

          handler.handle(Future.failedFuture("duplicate Username"));

        }

      });

    }

  }



  public static void signout(MongoClient client , String token , Handler<AsyncResult<MongoClientUpdateResult>> handler){

    JsonObject query = new JsonObject()
      .put("token", token);

    JsonObject update = new JsonObject()
      .put("$set",new JsonObject()
        .put("token",""));

    client.updateCollection(Const.user,query,update,handler);

  }

  public static void returnRoles(
    MongoClient client,
    ArrayList<JsonObject> arr,
    ArrayList<String> rolesId,
    int counter,
    Handler<AsyncResult<ArrayList<JsonObject>>> handler){

    if( counter == rolesId.size())
      handler.handle(Future.succeededFuture(arr));

    else{


      client.find(Const.role,new JsonObject().put("_id",rolesId.get(counter)), res->{

        arr.add(res.result().get(0));
        returnRoles(client,arr,rolesId,counter+1,handler);

      });


    }


  }

  public static JsonObject isTeacherInWorkshop(ArrayList<JsonObject> roles,String workshopId){

    JsonObject role;

    for ( int i = 0 ; i < roles.size() ; i++ ){
      role = roles.get(i);

      if(role.getString("roleName").equals("Teacher") && role.getString("enteredCourse").equals(workshopId))
        return role;

    }
    return null;

  }

  public static void passedCourses(
    MongoClient client,
    User user,
    Handler<AsyncResult<ArrayList<String>>> handler){

    ArrayList<String> rolesId = new ArrayList<>();

    for(int i = 0 ; i < user.getRoles().size() ; i++ ){
      rolesId.add(user.getRoles().get(i).get_id());
    }


    User.returnRoles(client,new ArrayList<JsonObject>(),rolesId,0,res->{

      ArrayList<JsonObject> studentRoles = filterByRoleName("Student",res.result());

      passedStudent(client,new ArrayList<JsonObject>(),studentRoles,0,resPassedStudent->{

        ArrayList<JsonObject> passedStudents = resPassedStudent.result();
        ArrayList<String> passedCourses = new ArrayList<>();

        passedStudents.forEach(student->{

          passedCourses.add(student.getString("course"));

        });

        handler.handle(Future.succeededFuture(passedCourses));

      });


    });

  }

  public static ArrayList<JsonObject> filterByRoleName(String roleName,ArrayList<JsonObject> arrayList){

    ArrayList<JsonObject> toReturn = new ArrayList<JsonObject>();

    arrayList.forEach(json->{

      if(json.getString("roleName").equals(roleName))
        toReturn.add(json);

    });

    return toReturn;

  }

  public static void passedStudent(
    MongoClient client,
    ArrayList<JsonObject> arr,
    ArrayList<JsonObject> students,
    int counter,
    Handler<AsyncResult<ArrayList<JsonObject>>> handler){

      if (counter == students.size()){

        handler.handle(Future.succeededFuture(arr));

      }

      else{

        client.find(Const.report,new JsonObject().put("report",students.get(counter).getString("report")),res->{

          if(res.result().get(0).getString("studentCourseStatus").equals("PASSED"))
            arr.add(students.get(counter));

          passedStudent(client,arr,students,counter+1,handler);

        });

      }


  }

  public static boolean preCoursesPassed(JsonArray neededCourses,ArrayList<String> passedCourses){

    //#Delete
    if( neededCourses == null)
      return true;

    for( int i = 0 ; i < neededCourses.size() ; i++){

      if( !passedCourses.contains(neededCourses.getString(i)))
        return false;
    }
    return true;

  }



  //  public void roleInWorkshop(MongoClient client,JsonObject clientJson,Handler<AsyncResult<List<JsonObject>>> handler){
//
//    String roleName = clientJson.getString("roleName");
//    String workshopId = clientJson.getString("workshopId");
//
//    JsonObject toFind = new JsonObject()
//      .put("roleName",roleName)
//      .put("userId",this._id)
//      .put("workshopId",workshopId);
//
//    client.find(Const.role, toFind,handler);
//
//  }

}
