package model;

import controller.Const;
import dev.morphia.annotations.Id;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.mongo.MongoClientUpdateResult;
import org.bson.types.ObjectId;

import java.util.ArrayList;

public class Group {

  private String _id;
	private ArrayList<Identity> identities = new ArrayList<Identity>();

  public Group(ArrayList<Identity> identities){
    this._id = new ObjectId().toString();
    this.identities = identities;
  }

  public Group() {
    this._id = new ObjectId().toString();

  }

  public Group( JsonObject jsonObject ){
    this._id = jsonObject.getString("_id");
    JsonArray identitiesId = jsonObject.getJsonArray("identities");

    for (int i = 0 ; i < identitiesId.size() ; i++){

      this.identities.add(new Identity(identitiesId.getString(i)));

    }

  }

  public Group(String _id){

    this._id = _id;

  }

  public ArrayList<Identity> getIdentities() {
    return identities;
  }

  public void addIdentity(Identity identity){
    this.identities.add(identity);
  }

  public String get_id(){
    return this._id;
  }

  public JsonObject toJson(){

    JsonObject json = new JsonObject();
    JsonArray jsonArray = new JsonArray();

    for( int i = 0 ; i < this.identities.size() ; i++){
      jsonArray.add(this.identities.get(i).get_id());
    }

    json.put("_id",this._id)
      .put("identities" , jsonArray);

    return  json;
  }

  public void saveToDB(MongoClient client, Handler<AsyncResult<String>> handler){

    client.insert(Const.group,this.toJson(),handler);

  }

  public void update(MongoClient client, Handler<AsyncResult<MongoClientUpdateResult>> handler){

    JsonObject query = new JsonObject().put("_id",this._id);
    JsonObject update = new JsonObject().put("$set",this.toJson());

    client.updateCollection(Const.group,query,update,handler);


  }

  public void saveToDB(MongoClient client){

    client.insert(Const.group,this.toJson(),handler->{});

  }

  public void update(MongoClient client){

    JsonObject query = new JsonObject().put("_id",this._id);
    JsonObject update = new JsonObject().put("$set",this.toJson());

    client.updateCollection(Const.group,query,update,handler->{});


  }

  public void addIdentity(String string) {
    Identity id = new Identity(string);
    addIdentity(id);
  }
  public static void returnGroupIdentities(MongoClient client,Group group,ne)
}
