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

    JsonArray rolesId = json.getJsonArray("roles");

    for (int i = 0 ; i < rolesId.size() ; i++){
      this.roles.add(new Identity(rolesId.getString(i)));
    }

  }


  public User(String _id){
    this._id = _id;
  }

  public User(ContactPoint contactPoint , String username , String password , String userType){
    this._id = new ObjectId().toString();
    this.contactPoint = contactPoint;
    this.username = username;
    this.password = password;
    this.userType = userType;
  }

  public void setContactPoint(ContactPoint contactPoint) {
    this.contactPoint = contactPoint;
  }

  public void addRole(Role role){

    this.roles.add(role);

  }

  public String getUserType() {
    return userType;
  }

  public String getUsername() {
    return username;
  }

  public String get_id(){
    return this._id;
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

  public String getContactPointId() {
    return contactPoint.get_id();
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

  public static void login(MongoClient client , JsonObject json , Handler<AsyncResult<JsonObject>> handler){


    String username = json.getString("username");
    String password = json.getString("password");


    JsonObject query = new JsonObject()
      .put("username",username)
      .put("password",password);

    client.find(Const.user,query,res->{

      if (!res.result().isEmpty()){

        String token = generateNewToken();

        client.updateCollection(Const.user , query , new JsonObject().put("$set",new JsonObject()
        .put("token",token)),result->{

          JsonObject toResponse = new JsonObject()
            .put("token",token)
            .put("userType",res.result().get(0).getString("userType"));

          handler.handle(Future.succeededFuture(new JsonObject().put("body",toResponse).put("status","true")));

        });



      }else{

        handler.handle(Future.failedFuture(""));


      }


    });

  }

  public static void editProfile(MongoClient client,User user , JsonObject editJson , String contactPoint_id , Handler<AsyncResult<MongoClientUpdateResult>> handler){

    String username,gender,emailAddress,firstName,lastName,biography;

    username = editJson.getString("username");
    firstName = editJson.getString("firstName");
    lastName = editJson.getString("lastName");
    gender = editJson.getString("gender");
    emailAddress = editJson.getString("emailAddress");
    biography = editJson.getString("biography");


    if ( username.equals("") ||
    firstName.equals("") ||
    lastName.equals("") ||
    emailAddress.equals("") ){
      handler.handle(Future.failedFuture(""));
    }else {

      client.find(Const.user , new JsonObject().put("username", username) , resDup -> {

        if (user.getUsername().equals(username) || resDup.result().isEmpty()) {

          user.setUsername(username);
          user.update(client,res->{

            client.find(Const.contactPoint,new JsonObject().put("_id",user.getContactPointId()),resFind->{

              ContactPoint cp = new ContactPoint(resFind.result().get(0));
              cp.setFirstName(firstName);
              cp.setEmailAddress(emailAddress);
              cp.setLastName(lastName);
              cp.setGender(gender);
              cp.update(client,handler);

            });

          });

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

      System.out.println(rolesId);
      client.find(Const.role,new JsonObject().put("_id",rolesId.get(counter)), res->{

        if (!res.result().isEmpty())
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

  public static void searchInRoleId(User user , String roleId , Handler<AsyncResult<String>> handler){
    boolean isExist = false;
    for(int i = 0 ; i < user.getRoles().size() ; i++){
      if(user.getRoles().get(i).equals(roleId)){
        isExist = true;
      }
    }

    if (isExist){
      handler.handle(Future.succeededFuture("This role id is exist in user roles."));
    }
    else {
      handler.handle(Future.failedFuture("Don't find role id in user roles."));
    }
  }

  public static void findRoleId(MongoClient client, User user, EnteredCourse enteredCourse, Handler<AsyncResult<String>> handler){
    //should debug it function
    //should return role id
    //don't debug this part for teacher
    client.find(Const.role , new JsonObject().put("roleName","teacher").put("_id",enteredCourse.getGroups().get(0).get_id()) , res->{
      if(res.succeeded() && !res.result().isEmpty()){
        JsonArray teachers = res.result().get(0).getJsonArray("teacher");//you should know how save teachers

        String teacherRoleId = null;
        teacherRoleId = isIdentityInRole(teachers,user.getRoles());
        if(teacherRoleId != null){
          handler.handle(Future.succeededFuture(teacherRoleId));
        }
        else {
          handler.handle(Future.failedFuture("You are not teacher of the workshop."));
        }
      }
      else {
        client.find(Const.group,new JsonObject().put("_id",enteredCourse.getGroups().get(0).get_id()), resFind ->{

          if(resFind.succeeded() && !resFind.result().isEmpty()){
            JsonArray identities = resFind.result().get(0).getJsonArray("identities");
            String  roleId = null;
            roleId = isIdentityInRole(identities , user.getRoles() );
            if(roleId != null){
              handler.handle(Future.succeededFuture(roleId));//output
            }
            else {
              handler.handle(Future.failedFuture("You are not in the workshop."));//output
            }
          }
          else {
            handler.handle(Future.failedFuture("workshop don't have group."));//output
          }
        });
      }
    });

  }

  public static String isIdentityInRole(JsonArray identities, ArrayList<Role> roles){
    if(identities == null)
      return null;

    for (int i = 0 ; i < identities.size() ; i++){
      for (int j = 0 ; j < roles.size() ; j++){
        if(identities.getString(i).equals(roles.get(j).get_id()))
          return identities.getString(i);
      }
    }
   return null;
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
