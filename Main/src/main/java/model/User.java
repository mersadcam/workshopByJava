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

	public User(String userName, String hashKey, String firstName, String lastName, String emailAddress, ContactPoint.Gender gender,
			File url) {
		// not completed
	}

	public void setHashKey(String hashKey) {
		this.hashPass = hashKey;
	}

	public void setUserName(String username) {
		this.username = username;
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
