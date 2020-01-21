package controller;


import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import model.*;

import java.io.File;
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



      client.find(Const.user,new JsonObject().put("username",json.getString("username")),res->{

        if(res.result().isEmpty()){

          String firstName = json.getString("firstName");
          String lastName = json.getString("lastName");
          String emailAddress = json.getString("emailAddress");
          String gender  = json.getString("gender");


          ContactPoint cp = new ContactPoint(
            firstName,
            lastName,
            emailAddress,
            gender
          );



          String username  = json.getString("username");
          String password = json.getString("password");
          String userType = "user";


          User user = new User( cp , username , password , userType);
          user.setToken();
          cp.saveToDB(client);
          user.saveToDB(client);

          ctx.response().end(new JsonObject()
            .put("status","true")
            .put("body",
              new JsonObject()
                .put("token",user.getToken())
                .put("userType",user.getUserType())
                ).toString());

        }

        else{

          ctx.response().end(new JsonObject().put("status","false").put("msg","this User have saved before").toString());

        }

      });



    });

    /////////////////////////////////////

    router.post(Const.login)
      .handler(BodyHandler.create())
      .handler(ctx ->{

        JsonObject json = ctx.getBodyAsJson();


        User.login(client,json,res->{

          if (res.succeeded())
            ctx.response().end(res.result().toString());
          else
            ctx.response().end(new JsonObject().put("status","false").put("msg","username or password is wrong").toString());

        });

      });

    /////////////////////////////////////

    router.route(Const.userProfileEdit)
      .handler(ctx ->{

        HttpServerResponse response = ctx.response();
        JsonObject toResponse = new JsonObject();
        JsonObject userJson = ctx.get("userJson");
        JsonObject clientJson = ctx.get("clientJson");
        String userType = ctx.get("userType");
        String CP_id = userJson.getString("contactPoint");
        User user = new User(userJson);

        user.editProfile(client , user ,clientJson , CP_id , handle -> {

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
        User user = new User(userJson);

        EnteredCourse.graderRequestForWorkshop(client , clientJson , user , handler ->{
          if(handler.succeeded()){
            toResponse
              .put("status","true")
              .put("msg",handler.cause());

          }
          else {
            toResponse
              .put("status","false")
              .put("msg",handler.cause());

          }
          ctx.response().end(toResponse.toString());
        });
      });

    router.route(Const.userWorkshopStudentRequest)
      .handler(ctx->{

      JsonObject clientJson = ctx.get("clientJson");
      JsonObject userJson = ctx.get("userJson");
      User user = new User(userJson);

      //find workshop
      //pishniaz ro check kon
      //payment in data base


      EnteredCourse.studentRequestStatus(client , clientJson,user , res ->{
        if(res.succeeded()) {

          client.find(Const.enteredCourse, new JsonObject().put("_id", clientJson.getString("workshopId")), resFind -> {

            if (!resFind.result().isEmpty()) {

              EnteredCourse workshop = new EnteredCourse(resFind.result().get(0));
              JsonObject paymentParts = workshop.getPaymentParts();
              int workshopValue = workshop.getValue();

              Payment payment ;
              PaymentStatus paymentStatus = new PaymentStatus(workshopValue);

              if (clientJson.getString("paymentType").equals("cash")) {

                String paymentTime = paymentParts.getJsonObject("cash").getString("time");


                payment = new Payment("",
                  paymentTime,
                  workshopValue);

                paymentStatus.addPayment(payment);

                payment.saveToDB(client);
                paymentStatus.saveToDB(client);

              } else {

                JsonArray payments = workshop.getPaymentParts().getJsonArray("installment");

                  for (int i = 0; i < payments.size(); i++) {

                    payment = new Payment(payments.getJsonObject(i).getString("name"),
                      payments.getJsonObject(i).getString("time"), payments.getJsonObject(i).getInteger("value"));
                    payment.saveToDB(client);
                    paymentStatus.addPayment(payment);

                  }
                  paymentStatus.saveToDB(client);

              }

              Report report = new Report();
              report.saveToDB(client);
              Student student = new Student(paymentStatus);
              student.saveToDB(client);
              Identity identity = new Identity(report,student,new Course(workshop.getCourseName()), "Student");
              user.addRole(identity);
              user.update(client);
              identity.saveToDB(client);

              client.find(Const.group,new JsonObject().put("_id",workshop.getGroups().get(0).get_id()),resFindGroup->{

                Group group = new Group(resFindGroup.result().get(0));
                group.addIdentity(identity);
                group.update(client,resUpdateGroup->{

                  if(resUpdateGroup.succeeded())
                    ctx.response().end(new JsonObject().put("status","true").put("msg","you are added in this course").toString());

                  else
                    ctx.response().end(new JsonObject().put("status","false").put("msg","there is a problem").toString());

                });

              });




            } else {

              ctx.response().end("Access Denied");

            }
          });
        }

        });


      });

    //new added
    router.route(Const.userWorkshopNewForm)
      .handler(ctx ->{

        JsonObject userJson = ctx.get("userJson");   // user info in db
        JsonObject clientJson = ctx.get("clientJson"); //sent from user
        User user = new User(userJson);
        JsonObject toResponse = new JsonObject();
        User.returnRoles(client,new ArrayList<JsonObject>(),user.getRolesId(),0,res->{

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

        JsonObject userJson = ctx.get("userJson");
        JsonObject clientJson = ctx.get("clientJson");
        String roleName = ctx.get("roleName");

        Grader.writerReport(client , roleName , clientJson , res ->{
          if(res.succeeded()){
            System.out.println("succed we win");
          }
          else{
            System.out.println(res.cause());
          }
        });



      });

    //new added
    router.get(Const.userFinalReport)
      .handler(ctx ->{

      });

    //new added
    router.route(Const.workshopStar)
      .handler(ctx ->{

        JsonObject clientJson = ctx.get("clientJson");
        JsonObject userJson = ctx.get("userJson");
        User user = new User(userJson);
        JsonObject toResponse = new JsonObject();
        String roleId = ctx.request().getHeader("roleId");

        EnteredCourse.workshopStar(client , clientJson , roleId , user , handler ->{
          if(handler.succeeded()){
            toResponse
              .put("status","true")
              .put("msg",handler.cause().toString());
          }
          else{
            toResponse
              .put("status","false")
              .put("msg",handler.cause().toString());
          }
          //what should i do ( ctx.next or ctx.end )
          ctx.response().end(toResponse.toString());
        });

      });

    ////////////////////////////////////


    router.route().path(Const.adminStar).handler(BodyHandler.create()).handler(ctx ->{

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
      .handler(ctx ->{

        JsonObject clientJson = ctx.get("clientJson");
        String name = clientJson.getString("name").toUpperCase();
        String description = clientJson.getString("description");
        client.find(Const.course,new JsonObject().put("name",name),resFind->{

          if(resFind.result().isEmpty()){

            Course course = new Course(name,description);
            course.saveToDB(client);

            ctx.response().end(new JsonObject()
            .put("status","true")
            .put("msg","New course added successfully")
            .put("body",course.toJson()).toString());

          }else{

            ctx.response().end(new JsonObject()
              .put("status","false")
              .put("msg","This course has already saved")
              .toString());

          }

        });

        Course course = new Course(clientJson.getString("name"),clientJson.getString("description"));



      });


    router.get(Const.adminEnterNewWorkshop)
      .handler(ctx ->{

        HttpServerResponse response = ctx.response();
        JsonObject toResponse = new JsonObject();
        JsonObject userJson = ctx.get("userJson");
        JsonObject clientJson = ctx.get("clientJson");
        String username = clientJson.getString("teacher");

        EnteredCourse.enterNewWorkshop(client, clientJson,res-> {
          if (res.failed()){

            toResponse.put("status", "fasle").put("msg",res.cause().getMessage());
            response.end(toResponse.toString());

          }else{

            ctx.response().end(new JsonObject()
              .put("status","true")
              .put("body",res.result())
              .put("msg","Workshop is created successfully")
            .toString());
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
        JsonObject userJson = ctx.get("userJson");

        User.signout(client , userJson.getString("token") , res ->{
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


    router.route(Const.uploadProfileImage)
      .handler(BodyHandler.create())
      .handler(ctx->{

        Set<FileUpload> files = ctx.fileUploads();

        for (FileUpload file : files){

          File up = new File(file.uploadedFileName());
          System.out.println(up.toString());

        }


      });

    server.requestHandler(router).listen(Const.port);

  }
}

