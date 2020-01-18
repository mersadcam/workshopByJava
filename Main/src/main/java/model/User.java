package model;

import controller.Const;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.mongo.MongoClientUpdateResult;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

//import java.util.List;
//import model.ContactPoint;

public class User {

  private static final SecureRandom secureRandom = new SecureRandom(); //this is for token
  private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //this is for token


  private String _id;

  private ArrayList<Role> roles = new ArrayList<Role>();

  //OneToOne relationship :
  private ContactPoint contactPoint;
  private String username;
  private String password;
  private String token;
  private String userType;

  public User(JsonObject json) {

    this.username = json.getString("username");
    this.password = json.getString("password");
    this._id = json.getString("_id");
    this.token = json.getString("token");
    this.userType = json.getString("userType");


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

  public void roleInWorkshop(MongoClient client,JsonObject clientJson,Handler<AsyncResult<List<JsonObject>>> handler){

    String roleName = clientJson.getString("roleName");
    String workshopId = clientJson.getString("workshopId");

    JsonObject toFind = new JsonObject()
      .put("roleName",roleName)
      .put("userId",this._id)
      .put("workshopId",workshopId);

    client.find(Const.role, toFind,handler);

  }

  public void signout(MongoClient client , String token , Handler<AsyncResult<MongoClientUpdateResult>> handler){

    JsonObject query = new JsonObject()
      .put("token", token);

    JsonObject update = new JsonObject()
      .put("$set",new JsonObject()
        .put("token",""));

    client.updateCollection(Const.user,query,update,handler);

  }

  public static void checkUserToken(MongoClient client, String collection ,String token,Handler<AsyncResult<List<JsonObject>>> handler){

    if( collection.equals("user") || collection.equals("admin") || collection.equals("superAdmin") )
      client.find(collection,new JsonObject().put("token",token),handler);

    else
      handler.handle(Future.failedFuture(""));

  }


  public static void returnRoles(
    MongoClient client,
    ArrayList<JsonObject> arr,
    List rolesId,
    int counter,
    Handler<AsyncResult<ArrayList>> handler){

    if( counter == rolesId.size())
      handler.handle(Future.succeededFuture(arr));

    else{


      client.find(Const.role,new JsonObject().put("_id",new JsonObject().put("$oid",rolesId.get(counter))), res->{

        arr.add(res.result().get(0));
        returnRoles(client,arr,rolesId,counter+1,handler);

      });


    }


  }

  public static JsonObject isTeacherInWorkshop(ArrayList<JsonObject> roles,String workshopId){

    JsonObject role;

    for ( int i = 0 ; i < roles.size() ; i++ ){
      role = roles.get(i);

      if(role.getString("roleName").equals("Teacher") && role.getString("workshopId").equals(workshopId))
        return role;

    }
    return null;

  }

}
