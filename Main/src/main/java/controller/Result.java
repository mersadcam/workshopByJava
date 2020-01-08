package controller;

import io.vertx.core.json.JsonObject;

public class Result {

  public boolean status;
  public Msg msg;
  public JsonObject json;

  public enum Msg{
    UNAVAILABLE,
    TRYAGAIN,
    OK
  }

  public Result(boolean status,JsonObject json){
    this.status = status;
    this.json = json;
  }

  public Result(boolean status){
    this.status = status;
  }

}
