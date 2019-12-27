package controller;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import model.User;


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

    Route statics = router.route("/statics/*");
    statics.handler( ctx ->{
      HttpServerResponse response = ctx.response();
      response.sendFile("./src/main" + ctx.request().path());
      response.end();
    });



    router.post("/register")
      .handler(BodyHandler.create())
      .handler(ctx->{

      HttpServerResponse response = ctx.response();
      JsonObject json = ctx.getBodyAsJson();
      System.out.println(json);
      User user = new User(json);

      user.register(client,handle->{
        if (handle.succeeded()) {

          ctx.response().end( new JsonObject()
            .put("token",handle.result())
            .toString());

          response.end();

        }
        else if (handle.failed())
          ctx.response().end("403");

      });

    });

    router.post("/login")
      .handler(BodyHandler.create())
      .handler(ctx ->{
        HttpServerResponse response = ctx.response();
        JsonObject userJson = ctx.getBodyAsJson();
        client.find("username",userJson, res ->{
          if ( res.succeeded() ){

          }
          else {

          }

        });
      });

    router.get("/login")
      .handler(ctx ->{
        HttpServerResponse response = ctx.response();
        response.end("login page");
      });


    router.get("/")
      .handler(ctx->{
      HttpServerResponse response = ctx.response();
      response.end("<h1>HOME</h1>");

    });


    router.get("/dashboard")
      .handler(ctx->{
      ctx.response().sendFile("src/main/statics/fortest/dashboard/index.html");
      HttpServerResponse response = ctx.response();
      response.write("<h1>your dashboard</h1>");
      response.write("your token is " + ctx.request().headers().get("token"));
      response.end();

    });


    server.requestHandler(router).listen(8000);

  }
}

