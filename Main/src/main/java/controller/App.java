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
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import model.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


public class App extends AbstractVerticle {


  public static void main(String[] args) throws IOException {

    Vertx vertx = Vertx.vertx();

    HttpServer server = vertx.createHttpServer();
    Router router = Router.router(vertx);

    ///////////////////////////////////// by pass cross origin
    Set<String> allowedHeaders = new HashSet<>();
    allowedHeaders.add("x-requested-with");
    allowedHeaders.add("Access-Control-Allow-Origin");
    allowedHeaders.add("origin");
    allowedHeaders.add("Content-Type");
    allowedHeaders.add("accept");

    Set<HttpMethod> allowedMethods = new HashSet<>();
    allowedMethods.add(HttpMethod.GET);
    allowedMethods.add(HttpMethod.POST);
    allowedMethods.add(HttpMethod.DELETE);
    allowedMethods.add(HttpMethod.PATCH);
    allowedMethods.add(HttpMethod.OPTIONS);
    router.route().handler(CorsHandler.create("*")
      .allowedHeaders(allowedHeaders)
      .allowedMethods(allowedMethods));
    /////////////////////////////////////

    JsonObject config = new JsonObject()
      .put("host" , "localhost")
      .put("port" , 27017)
      .put("db_name" , "workshop");
    MongoClient client = MongoClient.createShared( vertx , config );

    /////////////////////////////////////

    router.route("/checkUserToken").handler(ctx -> {

      String token = ctx.request().getHeader("token");
      HttpServerResponse response = ctx.response();
      JsonObject toResponse = new JsonObject();
      User.checkUserToken(client,token,res->{

        if(!res.result().isEmpty()){

          toResponse.put("status","true");

        }else{

          toResponse.put("status","false");

        }

        response.end(toResponse.toString());

      });


    });

    /////////////////////////////////////

    router.post("/register")
      .handler(BodyHandler.create())
      .handler(ctx->{

      HttpServerResponse response = ctx.response();
      JsonObject json = ctx.getBodyAsJson();
      User user = new User(json);
      ContactPoint cp = new ContactPoint(json);

      cp.addCPToDB(client,res->{

        user.setContactPointId(res.result());

        user.register(client,res1->{

          if( res1.succeeded() ) {
            response.end(res1.result());
          }else{

            response.setStatusCode(401);
            response.end(res.result());

          }


        });

      });

    });

    /////////////////////////////////////

    router.post("/login")
      .handler(BodyHandler.create())
      .handler(ctx ->{

        HttpServerResponse response = ctx.response();
        JsonObject json = ctx.getBodyAsJson();
        User user = new User(json);

        user.login(client,res->{

            response.end(res.result());

        });

      });

    /////////////////////////////////////

    router.get("/profile/edit")
      .handler(BodyHandler.create())
      .handler(ctx ->{

        String token = ctx.request().getHeader("token");
        HttpServerResponse response = ctx.response();
        JsonObject json = ctx.getBodyAsJson();
        JsonObject toResponse = new JsonObject();
        User.checkUserToken(client,token,res->{


          if(!res.result().isEmpty()) {

              User user = new User(res.result().get(0));

              user.editProfile(client, json, handle -> {

                if (handle.succeeded())
                  toResponse.put("status", "true");

                else
                  toResponse.put("status", "false");

                response.end(toResponse.toString());

              });

          }else{

            toResponse
              .put("status","false")
              .put("code","403");
            response.setStatusCode(403).end(toResponse.toString());

          }

        });

      });

    /////////////////////////////////////

    //check admin token is needed:
    router.get("/createNewCourse")
      .handler(BodyHandler.create())
      .handler(ctx ->{

        String token = ctx.request().getHeader("token");
        HttpServerResponse response = ctx.response();
        JsonObject json = ctx.getBodyAsJson();
        JsonObject toResponse = new JsonObject();

        //check "Admin" token before :

        Course course = new Course(json);

        course.createNewCourse(client,json,resCreate->{

          if(resCreate.succeeded())
            toResponse.put("status","true");
          else
            toResponse.put("status","false");

          response.end(toResponse.toString());

        });


      });

    ////////////////////////////////////

    //check admin token is needed:
    router.get("/enterNewWorkshop")
      .handler(BodyHandler.create())
      .handler(ctx ->{

        HttpServerResponse response = ctx.response();
        JsonObject json = ctx.getBodyAsJson();
        String token = ctx.request().getHeader("token");
        JsonObject toResponse = new JsonObject();

        EnteredCourse EC = new EnteredCourse(json);
        EC.enterNewWorkshop(client,json,resNewWorkshop->{

          if (resNewWorkshop.succeeded())
            toResponse.put("status","true");
          else
            toResponse.put("status","fasle");

          response.end(toResponse.toString());

        });

      });

    ///////////////////////////////////

    router.get("/*")
      .handler(ctx ->{
        HttpServerResponse response = ctx.response();
        response.setStatusCode(302);
        response.putHeader("location","/");
        response.end();
      });


    server.requestHandler(router).listen(8000);

  }
}

