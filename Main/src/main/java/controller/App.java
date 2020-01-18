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


    router.route().path("/user/*").handler(BodyHandler.create()).handler(ctx ->{

        String userType = ctx.request().getHeader("userType");
        String token = ctx.request().getHeader("token");
        JsonObject clientJson = ctx.getBodyAsJson();//client json

        if( token == null ){
          ctx.response().setStatusCode(503).end(new JsonObject().put("error","Access Denied").toString());
        }

        User.checkUserToken(client,userType,token,res->{

          if(!res.result().isEmpty()){

            JsonObject user = res.result().get(0);
            ctx.put("userType",userType);
            ctx.put("user",user);
            ctx.put("json",clientJson);
            ctx.next();

          }
          else
            ctx.response().setStatusCode(503).end(new JsonObject().put("error","Access Denied").toString());


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

        user.register(client,res.result(),res1->{

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

    router.route("/user/profile/edit")
      .handler(ctx ->{

        HttpServerResponse response = ctx.response();
        JsonObject toResponse = new JsonObject();
        JsonObject userJson = ctx.get("user");
        JsonObject json = ctx.get("json");
        String userType = ctx.get("userType");
        String CP_id = userJson.getString("contactPoint");
        User user = new User(userJson);

        user.editProfile(client , json,userType,CP_id, handle -> {

          if (handle.succeeded())
            toResponse.put("status", "true");

          else
            toResponse.put("status", "false");

          response.end(toResponse.toString());

        });

      });

    /////////////////////////////////////
    //first check token

    //new added
    router.route("/user/newForm")
      .handler(ctx ->{

        JsonObject userJson = ctx.get("user");   // user info in db
        JsonObject clientJson = ctx.get("json"); //sent from user

        User user = new User(userJson);
        user.roleInWorkshop(client,clientJson,res->{

          if( res.succeeded() ){

            Teacher.addForm(client,res.result().get(0),clientJson,resAddForm->{
              ctx.response().end("ok");
            });

          }else{

            ctx.response().end("not ok");
          }

        });

      });

    //new added
    router.route("/user/graderReport")
      .handler(ctx ->{

        HttpServerResponse response = ctx.response();
        JsonObject user = ctx.get("user");
        JsonObject clientJson = ctx.get("json");
        Grader.graderReport(client , user , clientJson , res ->{
          if(res.succeeded()){
            System.out.println("succed we win");
          }
          else{
            System.out.println(res.result());
          }
        });

        //find student
        //find report id in student class
        //create answer class
        //insert answer id to report class (answers array)
        //

      });

    //new added
    router.get("/user/final_report")
      .handler(ctx ->{
        String token = ctx.request().getHeader("token");
        String userType = ctx.request().getHeader("userType");

        HttpServerResponse response = ctx.response();

        User.checkUserToken(client ,userType, token , res ->{
          if(!res.result().isEmpty()){

          }
          else{

          }
        });
      });

    //check admin token is needed:
    router.get("/user/createNewCourse")
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
    router.get("/user/enterNewWorkshop")
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


    server.requestHandler(router).listen(8000);

  }
}

