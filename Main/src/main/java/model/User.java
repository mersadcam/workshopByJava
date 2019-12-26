package model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

public class User {

	private ArrayList<Role> roles = new ArrayList<Role>();
	private ContactPoint information;
	private String username;
	private String hashPass;

	private ArrayList<LoginLogs> loginLogs = new ArrayList<LoginLogs>();

	public User(JsonObject json) {

    ContactPoint.Gender gender;
    String genderString = json.getString("male");
    if (genderString.equals("male"))
      gender = ContactPoint.Gender.MALE;
    else if(genderString.equals("female"))
      gender = ContactPoint.Gender.FEMALE;
    else
      gender = ContactPoint.Gender.OTHERS;



	  this.username = json.getString("username");
    this.hashPass = json.getString("hashPass");
	  this.information = new ContactPoint(
	    json.getString("firstname"),json.getString("lastName"),
      gender,json.getString("emailAddress")
      );


	}





	public void setHashKey(String hashKey) {
		this.hashPass = hashKey;
	}

	public void setUserName(String username) {
		this.username = username;
	}

	public void setImage(File image){
	  this.information.setImage(image);
  }

	public void register(MongoClient client,Handler<AsyncResult<String>> handler){

	  JsonObject json = new JsonObject()
      .put("username",this.username)
      .put("hashPass",this.hashPass)
      .put("roles","")
      .put("information",this.information);

	  client.find("user",new JsonObject().put("username",this.username),ctx->{

	    if( ctx.failed()){
	      client.insert("user",json,handler);
      }

    });

  }


}
