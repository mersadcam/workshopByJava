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
import io.vertx.ext.web.handler.BodyHandler;
import model.User;
import sun.security.provider.SecureRandom;
import sun.security.provider.certpath.Vertex;

import java.lang.invoke.MethodHandle;

public class App extends AbstractVerticle {

  public static String reRouteTo;
  public static String msg;

  public static void main(String[] args) {

    Vertx vertx = Vertx.vertx();
    HttpServer server = vertx.createHttpServer();
    Router router = Router.router(vertx);
    JsonObject config = new JsonObject()
      .put("host" , "localhost")
      .put("port" , 27017)
      .put("db_name" , "WorkshopDB");
    MongoClient client = MongoClient.createShared( vertx , config );

    Route statics = router.route("/statics/*");
    statics.handler( ctx ->{
      HttpServerResponse response = ctx.response();
      System.out.println("./src/main" + ctx.request().path());
      response.sendFile("./src/main" + ctx.request().path());
      response.end();
    });


    Route register = router.route().path("/register");
    register.handler(BodyHandler.create());
    register.handler(ctx->{


      HttpServerResponse response = ctx.response();

      if(ctx.request().method().equals(HttpMethod.POST)) {

        JsonObject json = ctx.getBodyAsJson();
        User user = new User(json);

        user.register(client,handle->{

          if (handle.succeeded()) {
            System.out.println("register succeed ,token is " + handle.result());
            ctx.request().headers().add("token", handle.result());//now token is in request header
            App.reRouteTo = "/dashboard";
        }
          if (handle.failed()) {

            ctx.response().end("duplicate");
            App.reRouteTo = "/login";
          }

        });


      }

      else
        ctx.reroute("/");

      ctx.reroute(App.reRouteTo);

    });


    Route login = router.route().path("/login");
    login.handler(ctx ->{
      HttpServerResponse response = ctx.response();
      response.sendFile("src/main/statics/fortest/login/index.html");
      if( ctx.request().method().equals(HttpMethod.POST)){
        JsonObject userJson = new JsonObject();




      }
    });

    Route home = router.route().path("/");
    home.handler(ctx->{

      HttpServerResponse response = ctx.response();
      response.end("<h1>HOME</h1>");

    });

    Route dashboard = router.route().path("/dashboard");
    dashboard.handler(ctx->{
      ctx.response().sendFile("src/main/statics/fortest/dashboard/index.html");
      HttpServerResponse response = ctx.response();
      response.write("<h1>your dashboard</h1>");
      response.write("your token is " + ctx.request().headers().get("token"));

    });


    server.requestHandler(router).listen(8000);

  }
}

