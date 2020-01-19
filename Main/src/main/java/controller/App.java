package controller;


import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import model.*;
import org.bson.types.ObjectId;

import javax.swing.text.html.parser.Entity;
import java.io.IOException;
import java.util.ArrayList;
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

    JsonObject config = new JsonObject()
      .put("host" , "localhost")
      .put("port" , 27017)
      .put("db_name" , "workshop");
    MongoClient client = MongoClient.createShared( vertx , config );

    /////////////////////////////////////


    router.route()
      .path(Const.userStar)
      .handler(BodyHandler.create()).handler(ctx ->{

        String userType = ctx.request().getHeader("userType");
        String token = ctx.request().getHeader("token");
        JsonObject clientJson = ctx.getBodyAsJson();//client json

        if( token == null){
          ctx.response().setStatusCode(503).end(new JsonObject().put("error","Access Denied").toString());
        }

         client.find(Const.user,new JsonObject().put("token",token).put("userType",userType),res->{

          if(!res.result().isEmpty()){

            JsonObject user = res.result().get(0);
            ctx.put("userType",userType);
            ctx.put("userJson",user);
            ctx.put("clientJson",clientJson);
            ctx.next();

          }
          else
            ctx.response().setStatusCode(503).end(new JsonObject().put("error","Access Denied").toString());


        });


    });

    /////////////////////////////////////

    router.post(Const.register)
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

    router.post(Const.login)
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

    router.route(Const.userProfileEdit)
      .handler(ctx ->{

        HttpServerResponse response = ctx.response();
        JsonObject toResponse = new JsonObject();
        JsonObject userJson = ctx.get("userJson");
        JsonObject json = ctx.get("clientJson");
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


    router.route(Const.userWorkshops)
      .handler(ctx->{

      JsonObject toResponse = new JsonObject();

      client.find(Const.enteredCourse,new JsonObject(),res->{

        List workshopList = res.result();

        if( res.succeeded())
          toResponse.put("status","true")
          .put("body",workshopList);

        else
          toResponse.put("status","false");

        ctx.response().end( toResponse.toString() );


      });




    });

    router.route(Const.userWorkshopGraderRequest)
      .handler(ctx ->{

        JsonObject toResponse = new JsonObject();
        JsonObject clientJson = ctx.get("clientJson");
        JsonObject userJson = ctx.get("userJson");

        EnteredCourse.graderRequestForWorkshop(client , clientJson , userJson , handler ->{
          if(handler.succeeded()){
            toResponse
              .put("status","true");

          }
          else {
            toResponse
              .put("status","false")
              .put("msg","cannot add grader request");

          }
          ctx.response().end(toResponse.toString());
        });
      });

    router.route(Const.userWorkshopStudentRequest)
      .handler(ctx->{

      JsonObject toResponse = new JsonObject();
      JsonObject clientJson = ctx.get("clientJson");
      JsonObject userJson = ctx.get("userJson");

      //find workshop
      //pishniaz ro check kon
      //payment in data base


      EnteredCourse.studentRequestStatus(client , clientJson,userJson , res ->{
        if(res.succeeded()) {

          client.find(Const.enteredCourse, new JsonObject().put("_id", clientJson.getString("workshopId")), resFind -> {

            if (!resFind.result().isEmpty()) {

              JsonObject workshop = resFind.result().get(0);
              JsonArray payments = workshop.getJsonArray("paymentParts");
              int workshopValue = workshop.getInteger("value");

              PaymentStatus paymentStatus = new PaymentStatus(workshopValue);

              if (clientJson.getString("paymentType").equals("cash")) {

                Payment payment = new Payment(payments.getJsonObject(0).getString("name"),
                  payments.getJsonObject(0).getString("time"),
                  workshopValue);

                payment.saveToDB(client, resSavePayment -> {

                  paymentStatus.addPayment(payment);
                  paymentStatus.saveToDB(client, resSavePaymentStatus -> {

                    Student student = new Student(new Course(workshop.getString("course"), ""), paymentStatus);
                    student.saveToDB(client, resSaveStudent -> {

                      ctx.response().end(new JsonObject()
                        .put("status", "true")
                        .put("msg", "your request entered successfully")
                        .toString());

                    });


                  });

                });


              } else {


                JsonArray listOfJson = workshop.getJsonArray("paymentParts");
                for (int i = 0; i < listOfJson.size(); i++)
                  paymentStatus.addPayment(new Payment(listOfJson.getJsonObject(i).getString("name"),
                    listOfJson.getJsonObject(i).getString("time"), listOfJson.getJsonObject(i).getInteger("value")));

                Student student = new Student(new Course(workshop.getString("course"), ""), paymentStatus);
                student.saveToDB(client, resSaveStudent -> {

                  ctx.response().end(new JsonObject()
                    .put("status", "true")
                    .put("msg", "your request entered successfully")
                    .toString());

                });

              }


            } else {

              ctx.response().end("Access Denied");

            }
          });
        }

        });

//      EnteredCourse.studentRequestForWorkshop(client , clientJson , handler ->{
//        if(){
//
//        }
//        else{
//          toResponse
//            .put("status","false")
//            .put("msg","workshop not found.");
//          }
//        });

      });

    //new added
    router.route(Const.userWorkshopNewForm)
      .handler(ctx ->{

        JsonObject userJson = ctx.get("userJson");   // user info in db
        JsonObject clientJson = ctx.get("clientJson"); //sent from user
        JsonObject toResponse = new JsonObject();

        List rolesId;

        try {
          rolesId = userJson.getJsonArray("rolesId").getList();
        }catch (Exception e){

          rolesId = null;

        }

        if (rolesId == null)
          ctx.response().end(new JsonObject()
          .put("status","false")
          .put("msg","you dont have any roles").toString());


        User user = new User(userJson);
        user.returnRoles(client,new ArrayList<JsonObject>(),rolesId,0,res->{

          JsonObject teacherJson = User.isTeacherInWorkshop(res.result(),clientJson.getString("workshopId"));

          if( teacherJson != null ){

            Teacher.addNewForm(client,teacherJson,clientJson,resAddForm->{

              if( resAddForm.succeeded() )
                toResponse
                  .put("status","true")
                  .put("msg","added successfully");

              else
                toResponse
                  .put("status","false")
                  .put("msg","there is problem in your input");


              ctx.response().end(toResponse.toString());

            });

          }else{

            ctx.response().end(new JsonObject()
            .put("status","false")
            .put("msg","Access Denied").toString());
          }

        });

      });


    router.route(Const.userGraderReport)
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



      });

    //new added
    router.get(Const.userFinalReport)
      .handler(ctx ->{

      });

    ////////////////////////////////////


    router.route().path("/admin/*").handler(BodyHandler.create()).handler(ctx ->{

      String userType = ctx.request().getHeader("userType");
      String token = ctx.request().getHeader("token");
      JsonObject clientJson = ctx.getBodyAsJson();//client json

      if (userType.equals("user") )
        ctx.response().setStatusCode(503).end(
          new JsonObject()
          .put("status","false")
          .put("msg","Access Denied")
          .toString());


      if( token == null){
        ctx.response().setStatusCode(503).end(new JsonObject().put("error","Access Denied").toString());
      }

      client.find(Const.user,new JsonObject().put("token",token).put("userType",userType),res->{

        if(!res.result().isEmpty()){

          JsonObject user = res.result().get(0);
          ctx.put("userType",userType);
          ctx.put("userJson",user);
          ctx.put("clientJson",clientJson);
          ctx.next();

        }
        else
          ctx.response().setStatusCode(503).end(new JsonObject().put("error","Access Denied").toString());


      });


    });



    router.get(Const.adminCreateNewCourse)
      .handler(BodyHandler.create())
      .handler(ctx ->{

        HttpServerResponse response = ctx.response();
        JsonObject json = ctx.getBodyAsJson();
        JsonObject toResponse = new JsonObject();

        Course course = new Course(json.getString("name"),json.getString("description"));

        course.createNewCourse(client,json,resCreate->{

          if(resCreate.succeeded())
            toResponse.put("status","true");
          else
            toResponse.put("status","false");

          response.end(toResponse.toString());

        });


      });


    router.get(Const.adminEnterNewWorkshop)
      .handler(BodyHandler.create())
      .handler(ctx ->{

        HttpServerResponse response = ctx.response();
        JsonObject toResponse = new JsonObject();
        JsonObject clientJson = ctx.get("clientJson");
        String username = clientJson.getString("teacher");
        EnteredCourse.enterNewWorkshop(client,clientJson,res->{

          if (res.succeeded())
            client.find(Const.user,new JsonObject().put("username",username),resFindUser->{

              JsonObject jsonToCreateTeacher = new JsonObject();
              jsonToCreateTeacher.put("roleName","Teacher")
                .put("_id",new ObjectId().toString())
                .put("enteredCourse",res.result())
                .put("form",new JsonArray());


              client.insert(Const.role,jsonToCreateTeacher,resInsert->{

                JsonArray lastUserRoles = resFindUser.result().get(0).getJsonArray("role");
                JsonArray newUserRoles = lastUserRoles.add(resInsert.result());

                client.updateCollection(Const.user,new JsonObject().put("username",username),
                  new JsonObject().put("$set",new JsonObject().put("role",newUserRoles)),resUpdate->{

                    if (resUpdate.succeeded()) {
                      toResponse
                        .put("status", "true")
                        .put("msg", "Workshop Created Successfully");

                    }

                    else
                      toResponse.put("status","false").put("msg","Error");

                    response.end(toResponse.toString());

                  });

              });

            });




          else {

            toResponse.put("status", "fasle");
            response.end(toResponse.toString());

          }

        });

      });


    router.route(Const.superAdminCreateAdmin)
      .handler(BodyHandler.create())
      .handler(ctx ->{
        HttpServerResponse response = ctx.response();

        JsonObject toResponse = new JsonObject();
        JsonObject clientJson = ctx.getBodyAsJson();
        String token = ctx.request().getHeader("token");

        client.find(Const.user , new JsonObject().put("token",token).put("userType","superAdmin") , res->{
          if(!res.result().isEmpty()){

            clientJson.put("userType","admin");
            client.insert(Const.user , clientJson , resInsert ->{
              if(!resInsert.result().isEmpty()){
                toResponse
                  .put("status", "true");

              }
              else{
                toResponse
                  .put("msg","not added super admin");
              }
            });
          }
          else{
            response.end("access denied.");
          }
        });



      });


    router.route(Const.signout)
      .handler(ctx ->{

        JsonObject toResponse = new JsonObject();
        JsonObject user = ctx.get("userJson");
        User userSignOut = new User(user);

        userSignOut.signout(client , userSignOut.getToken() , res ->{
          if(res.succeeded()){
            toResponse
              .put("status","true")
              .put("msg","user signed out.");

          }
          else{
            toResponse
              .put("status","false")
              .put("msg","you cannot signout.");

          }
          ctx.response().end();
        });

      });

    server.requestHandler(router).listen(8000);

  }
}

