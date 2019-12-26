package controller;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import model.User;
import sun.security.provider.SecureRandom;
import sun.security.provider.certpath.Vertex;

import java.lang.invoke.MethodHandle;

public class App extends AbstractVerticle {

  public static void main(String[] args) {

    Vertx vertx = Vertx.vertx();
    HttpServer server = vertx.createHttpServer();
    Router router = Router.router(vertx);
    JsonObject config = new JsonObject()
      .put("host" , "localhost")
      .put("port" , 27017)
      .put("db_name" , "WorkshopDB");
    MongoClient client = MongoClient.createShared( vertx , config );

    Route statics = router.route("/static/*");
    statics.handler( ctx ->{
      HttpServerResponse response = ctx.response();
      response.sendFile("./src/main" + ctx.request().path());
      response.end();
    });


    Route register = router.route(HttpMethod.POST,"/register");
    register.handler(ctx->{

      JsonObject json = ctx.getBodyAsJson();
      User user = new User(json);


    });


    Route login = router.route().path("/login");

    login.handler(ctx ->{
      HttpServerResponse response = ctx.response();
      response.sendFile("index.html");
      if( ctx.request().method().equals(HttpMethod.POST)){
        JsonObject userJson = new JsonObject();
        SecureRandom random = new SecureRandom();


      }
    });



  }
}

