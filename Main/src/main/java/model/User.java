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
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.mongo.MongoClientUpdateResult;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.bson.types.ObjectId;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {

  private static final SecureRandom secureRandom = new SecureRandom(); //this is for token
  private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //this is for token

  @Id
  private ObjectId id;

  @Reference
  private ArrayList<Role> roles = new ArrayList<Role>();
  @Reference
  private ContactPoint information;

  private String username;
  private String password;
  private String token;


  public User(JsonObject json) {

    ContactPoint.Gender gender;
    String genderString = json.getString("gender");

    if (genderString == null)
      gender = ContactPoint.Gender.NOTSET;
    else if (genderString.equals("male"))
      gender = ContactPoint.Gender.MALE;
    else if (genderString.equals("female"))
      gender = ContactPoint.Gender.FEMALE;
    else {
      gender = ContactPoint.Gender.OTHERS;
    }




    this.username = json.getString("username");
    this.password = json.getString("password");
    this.token = generateNewToken();
    this.information = new ContactPoint(
      json.getString("firstname"), json.getString("lastName"),
      gender, json.getString("emailAddress")
    );


  }

  public String getToken() {
    return token;
  }

  public void setImage(File image) {
    this.information.setImage(image);
  }


  public Result register(Datastore client ) {


    try {

      List<User> query = client.createQuery(User.class)
        .field("username").equal(this.username)
        .find().toList();

    }catch (Exception e){

      return new Result(false);

    }


    this.setToken();
    client.save(this);

    return new Result(true,
      new JsonObject().put("token",this.getToken()));

  }


  public static String generateNewToken() {
    byte[] randomBytes = new byte[24];
    secureRandom.nextBytes(randomBytes);
    return base64Encoder.encodeToString(randomBytes);
  }

  public void setToken() {
    this.token = generateNewToken();
  }

  public Result login(Datastore client){

    Query<User> query = client.createQuery(User.class)
      .field("username").equal(this.username)
      .field("password").equal(this.password);


    try {
        query.find().toList();

    }catch (Exception e){

      this.setToken();
      //update and set new token to database

      UpdateOperations<User> ops = client.createUpdateOperations(User.class);
      ops.set("token",this.token);

      client.update(query, ops);


      return new Result(true,new JsonObject()
        .put("token",this.getToken()));

    }


    return new Result(false);


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




}
