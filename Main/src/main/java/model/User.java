package model;

import java.io.File;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
//import java.util.List;
//import model.ContactPoint;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

public class User {

  private static final SecureRandom secureRandom = new SecureRandom(); //this is for token
  private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //this is for token

  private ArrayList<Role> roles = new ArrayList<Role>();
  private ContactPoint information;
  private String username;
  private String hashPass;
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
    this.hashPass = json.getString("hashPass");
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


  public String register(MongoClient client, Handler<AsyncResult<String>> handler) {

    JsonObject json = new JsonObject()
      .put("username", this.username)
      .put("hashPass", this.hashPass)
      .put("roles", "")
      .put("information", "this.information")
      .put("token", this.token);

    client.find("user", new JsonObject().put("username", this.username), ctx -> {
      if (ctx.result().isEmpty()) {
        client.insert("user", json, handler);
      }
      else{
        handler.handle(Future.failedFuture("Username is duplicate"));
      }
    });

    return this.token;

  }


  public static String generateNewToken() {
    byte[] randomBytes = new byte[24];
    secureRandom.nextBytes(randomBytes);
    return base64Encoder.encodeToString(randomBytes);
  }

  public void setToken() {
    this.token = generateNewToken();
  }


}
