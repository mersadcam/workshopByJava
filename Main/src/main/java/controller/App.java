package controller;


import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.query.Query;
import dev.morphia.query.UpdateOperations;
import dev.morphia.query.UpdateOperator;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import model.Course;
import model.User;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
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
//    JsonObject config = new JsonObject()
//      .put("host" , "localhost")
//      .put("port" , 27017)
//      .put("db_name" , "WorkshopDB");
//    MongoClient client = MongoClient.createShared( vertx , config );
    ///////////////////////////////////// this part for odm(morphia)
    final Morphia morphia = new Morphia();
    morphia.mapPackage("dev.morphia.example");

    final Datastore datastore = morphia.createDatastore( new com.mongodb.MongoClient()
      , "WorkshopDB");
    datastore.ensureIndexes();
    /////////////////////////////////////test morphia
    Course c = new Course(new JsonObject()
      .put("name","osol")
      .put("description","eee"));
    datastore.save(c);
    Course d = new Course(new JsonObject()
    .put("name","pishrafte")
    .put("description","ea"));
    datastore.save(d);

    Query<Course> query = datastore.createQuery(Course.class);
    @Deprecated
    List<Course> courses = query.asList();

    UpdateOperations<Course> updateOperations = datastore.createUpdateOperations(Course.class)
      .push("description","rr");

    

    /////////////////////////////////////
    Route statics = router.route("/statics/*");
    statics.handler( ctx ->{
      HttpServerResponse response = ctx.response();
      response.sendFile("./src/main" + ctx.request().path());
      response.end();
    });

    /////////////////////////////////////

    router.post("/register")
      .handler(BodyHandler.create())
      .handler(ctx->{

      HttpServerResponse response = ctx.response();
      JsonObject json = ctx.getBodyAsJson();
      User user = new User(json);
      Result status = user.register(datastore);

      if (status.status){

        response.end(status.json.toString());

      }

      response.end("401");

    });
    /////////////////////////////////////

    router.post("/login")
      .handler(BodyHandler.create())
      .handler(ctx ->{

        HttpServerResponse response = ctx.response();
        JsonObject json = ctx.getBodyAsJson();
        User user = new User(json);
        Result result = user.login(datastore);

        if (result.status){
          response.end(result.json.toString());
        }

        response.end("403");

      });

    /////////////////////////////////////
    router.get("/")
      .handler(ctx ->{
        HttpServerResponse response = ctx.response();
        response.sendFile("src/main/statics/fortest/login/index.html");
        response.end();
      });

    /////////////////////////////////////
//    router.get("/dashboard")
//      .handler(ctx->{
//      String token = ctx.request().getHeader("token");
//      HttpServerResponse response = ctx.response();
//      User.checkToken(client,token,res ->{
//        if(!res.result().isEmpty()){
//          response.sendFile("src/main/statics/fortest/dashboard/index.html");
//
//        }
//        else {
//          response.setStatusCode(302);
//          response.putHeader("location","/");
//          response.end();  //unauthorized
//        }
//      });
//
//
//    });

    /////////////////////////////////////
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

