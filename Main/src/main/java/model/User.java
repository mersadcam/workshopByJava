package model;

import java.io.File;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
//import java.util.List;
//import model.ContactPoint;

import com.mongodb.client.model.UpdateOneModel;
import controller.Result;
import dev.morphia.Datastore;
import dev.morphia.UpdateOptions;
import dev.morphia.query.*;
import dev.morphia.query.internal.MorphiaCursor;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.mongo.MongoClientUpdateResult;
import jdk.internal.org.objectweb.asm.Handle;
import jdk.nashorn.internal.ir.annotations.Reference;


public class User {

  private static final SecureRandom secureRandom = new SecureRandom(); //this is for token
  private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //this is for token


  private String _id;


  private ArrayList<Role> roles = new ArrayList<Role>();

  //OneToOne relationship :
  private String contactPoint;
  private String username;
  private String password;
  private String token;


  public User(JsonObject json) {

    this.username = json.getString("username");
    this.password = json.getString("password");
    this.contactPoint = json.getString("contactPoint");
    this._id = json.getString("_id");
    this.token = json.getString("token");

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
  public void returnContactPoint(MongoClient client, Handler<AsyncResult<List<JsonObject>>> handler){

    JsonObject query = new JsonObject().put("_id",this.contactPoint);
    client.find("contactPoint",query,handler);

  }
  public void login(MongoClient client , Handler<AsyncResult<String>> handler){

    JsonObject query = new JsonObject()
      .put("username",this.username)
      .put("password",this.password);
    client.find("user",query,res->{

      if (!res.result().isEmpty()){
        this.setToken();
        JsonObject token =  new JsonObject()
          .put("token", this.token);

        client.updateCollection("user",query,new JsonObject().put("$set",new JsonObject()
        .put("token",this.token)),result->{

          handler.handle(Future.succeededFuture(token.toString()));

        });



      }else{

        handler.handle(Future.failedFuture(""));


      }


    });

  }
  public void register(MongoClient client , Handler<AsyncResult<String>> handler ) {



    //startTransaction :

    client.find("user",new JsonObject()
    .put("username",this.username),res->{

      if(res.result().isEmpty()){

        JsonObject json = new JsonObject()
          .put("username",this.username)
          .put("password",this.password)
          .put("contactPoint",this.contactPoint);

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
  public void editProfile(MongoClient client,JsonObject editJson,Handler<AsyncResult<MongoClientUpdateResult>> handler){

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

      client.find("user", new JsonObject().put("username", username), resDup -> {

        if (this.username == username || resDup.result().isEmpty()) {

          JsonObject toSet = new JsonObject().put("username",username);
          JsonObject update = new JsonObject().put("$set", toSet);

          client.updateCollection("user",new JsonObject().put("token",this.token), update , resUp->{

            ContactPoint.editContactPoint(client,editJson,this.contactPoint, handler);

          } );

        }
        else{

          handler.handle(Future.failedFuture("duplicate Username"));

        }

      });

    }

  }
  public void signout(MongoClient client , String token , Handler<AsyncResult<MongoClientUpdateResult>> handler){

    JsonObject query = new JsonObject()
      .put("token", token);

    JsonObject update = new JsonObject()
      .put("$set",new JsonObject()
        .put("token",""));

    client.updateCollection("user",query,update,handler);

  }
  public static void checkToken(MongoClient client, String token,Handler<AsyncResult<List<JsonObject>>> handler){

    client.find("user",new JsonObject().put("token",token),handler);

  }
  public void setContactPointId(String _id){

    this.contactPoint = _id;

  }


}
