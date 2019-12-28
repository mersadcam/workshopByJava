package controller;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
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
      User user = new User(json);

      user.register(client,handle->{
        if (handle.succeeded()) {

          response.end( new JsonObject()
            .put("token",user.getToken()).toString());

        }
        else if (handle.failed())
          response.setStatusCode(401).end();

      });
    });

    router.post("/login")
      .handler(BodyHandler.create())
      .handler(ctx ->{

        HttpServerResponse response = ctx.response();
        JsonObject json = ctx.getBodyAsJson();
        User user = new User(json);
        user.login(client, res ->{
          if (res.succeeded()) {
            response.end(new JsonObject().put("token",user.getToken()).toString());
          }

          else {
            response.setStatusCode(401);
            response.end();
          }

        });
      });

    router.get("/")
      .handler(ctx ->{
        HttpServerResponse response = ctx.response();
        response.sendFile("src/main/statics/fortest/login/index.html");
        response.end();
      });


    router.get("/dashboard")
      .handler(ctx->{
      String token = ctx.request().getHeader("token");
      HttpServerResponse response = ctx.response();
      User.checkToken(client,token,res ->{
        if(!res.result().isEmpty()){
          response.sendFile("src/main/statics/fortest/dashboard/index.html");

        }
        else {
          response.end("not found");  //unauthorized
        }
      });


    });




    server.requestHandler(router).listen(8000);

  }
}

