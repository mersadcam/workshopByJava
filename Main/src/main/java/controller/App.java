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
    allowedHeaders.add("token");
    allowedHeaders.add("userType");
    allowedHeaders.add("origin");
    allowedHeaders.add("Content-Type");
    allowedHeaders.add("accept");
    /////////////////////////////////////
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

      String fullName = json.getString("fullName");
      String emailAddress = json.getString("emailAddress");
      String username = json.getString("username");
      String password = json.getString("password");

      if( fullName == null ||
          emailAddress == null ||
          username == null)
        ctx.response().end(new JsonObject()
        .put("status","false")
        .put("msg","Please fill the star blanks")
        .toString());

      else{

        client.find(Const.user,new JsonObject().put("username",json.getString("username")),res->{

          if(res.result().isEmpty()){




            ContactPoint cp = new ContactPoint(
              fullName,
              emailAddress
            );

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

            ctx.response().end(new JsonObject().put("status","false").put("msg","This Username has saved before").toString());

          }

        });



      }



    });

    /////////////////////////////////////

    router.post(Const.login)
      .handler(BodyHandler.create())
      .handler(ctx ->{

        JsonObject json = ctx.getBodyAsJson();

        if(json.getString("username") == null ||
            json.getString("password") == null ){

          ctx.response().end(new JsonObject().put("status","false")
          .put("msg","Please fill the blanks").toString());

        }else{

          User.login(client,json,res->{

            if (res.succeeded())
              ctx.response().end(res.result().toString());
            else
              ctx.response().end(new JsonObject().put("status","false")
                .put("msg","There was a problem logging in. Check your email and password or create an account.")
                .toString());

          });

        }



      });

    /////////////////////////////////////

    router.route(Const.info)
      .handler(ctx->{

        JsonObject userJson = ctx.get("userJson");
        client.find(Const.contactPoint,new JsonObject().put("_id",userJson.getString("contactPoint")),res->{

          JsonObject body = new JsonObject().put("user",userJson).put("contactPoint",res.result().get(0));
          ctx.response().end(new JsonObject()
          .put("status","true").put("body",body).toString());

        });

      });


    router.route(Const.profile)
      .handler(ctx->{

        JsonObject userJson = ctx.get("userJson");
        JsonObject clientJson = ctx.get("clientJson");

        String username = clientJson.getString("username");
        client.find(Const.user,new JsonObject().put("username",username),res->{

          EnteredCourse.setRolesOnWorkshops(client, new ArrayList<>(),
            res.result().get(0).getJsonArray("roles").getList(),
            0,resSetRoles->{


            EnteredCourse.myWorkshops(client,new ArrayList<JsonObject>(),resSetRoles.result(),0,resMyWorkshop->{

              EnteredCourse.setTeacherOnMyWorkshops(client,new ArrayList<JsonObject>(),resMyWorkshop.result(),0,resFinal->{

                client.find(Const.contactPoint,new JsonObject()
                  .put("_id",res.result().get(0).getString("contactPoint")),resCP->{

                  JsonObject body = new JsonObject().put("workshops",resFinal.result())
                    .put("user",res.result().get(0))
                    .put("contactPoint",resCP.result().get(0))
                    .put("username",userJson.getString("username"));
                  ctx.response().end(new JsonObject().put("status","true")
                    .put("body",body).toString());

                });



              });

            });

          });


        });



      });


    router.route(Const.userProfileEdit)
      .handler(ctx ->{

        HttpServerResponse response = ctx.response();
        JsonObject toResponse = new JsonObject();
        JsonObject userJson = ctx.get("userJson");
        JsonObject clientJson = ctx.get("clientJson");
        String userType = ctx.get("userType");
        String CP_id = userJson.getString("contactPoint");
        User user = new User(userJson);

        user.editProfile(client , user ,clientJson , handle -> {

          if (handle.succeeded())
            toResponse.put("status", true).put("msg","Profile changed successfully");

          else
            toResponse.put("status", false).put("msg",handle.cause().getMessage());

          response.end(toResponse.toString());

        });

      });


    router.route(Const.userWorkshops)
      .handler(ctx->{

        EnteredCourse.allWorkshopsWithTeacher(client,res->{

          if( res.succeeded() )
            ctx.response().end(new JsonObject().put("status","true")
            .put("body",res.result()).toString());

          else
            ctx.response().end(new JsonObject().put("status","false")
            .put("msg","There are some problems in system").toString());


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
              Identity identity = new Identity(report,student,workshop, "Student");
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

        Grader.writerReport(client , "Grader" , clientJson , res ->{
          if(res.succeeded()){

            ctx.response().end(new JsonObject().put("status","true").put("msg","Your answer have saved successfully").toString());

          }
          else{
            ctx.response().end(new JsonObject().put("status","false").put("msg","There are some problems in your answer").toString());

          }
        });



      });

    //new added
    router.get(Const.userStudentFinalReport)
      .handler(ctx ->{

        JsonObject userJson = ctx.get("userJson");
        JsonObject clientJson = ctx.get("clientJson");

        JsonObject toFindIdentity = new JsonObject()
          .put("roleName","Student")
          .put("_id",clientJson.getString("studentId"));

        client.find(Const.role,toFindIdentity,resIdentity->{


          if( !resIdentity.result().isEmpty()) {

            Identity identity = new Identity(resIdentity.result().get(0));
            client.find(Const.report,new JsonObject().put("_id",identity.getReportId()),resReport->{

              if (!resReport.result().isEmpty()){

                Report report = new Report(resReport.result().get(0));
                report.setFinalNumber(clientJson.getDouble("finalNumber"));
                report.setStudentCourseStatus(clientJson.getString("studentCourseStatus"));
                report.setCompleteNumber(clientJson.getDouble("completeNumber"));
                report.update(client);

                ctx.response().end(new JsonObject()
                  .put("status","true")
                  .put("msg","Report have been entered successfully")
                  .toString());

              }

            });
          }else{

            ctx.response().end(new JsonObject()
              .put("status","false")
              .put("msg","There are some problems in system\nCall to informatic team( phone number : ++++) ")
              .toString());

          }


        });


      });

    //new added (for teacher needed debug)
    router.route(Const.workshopStar)
      .handler(ctx ->{

        JsonObject clientJson = ctx.get("clientJson");
        JsonObject userJson = ctx.get("userJson");
        User user = new User(userJson);
        String roleId = ctx.request().getHeader("roleId");

        //else part have problem because print stack trace
        EnteredCourse.workshopStar(client , clientJson , roleId , user , handler ->{
          if(handler.succeeded() && !handler.result().isEmpty()){
            ctx.put("roleId",handler.result().toString());
            ctx.next();
          }
          else {
            ctx.put("roleId", "");
            ctx.next();
          }
        });

      });

    //new added
    router.route(Const.userWorkshopPage)
      .handler(ctx ->{
        JsonObject clientJson = ctx.get("clientJson");//workshop id is here
        JsonObject userJson = ctx.get("userJson");
        User user = new User(userJson);
        String roleId = ctx.get("roleId");
        JsonObject toResponse = new JsonObject();

        if (roleId == null){//user don't have role in the workshop

          client.find(Const.enteredCourse , new JsonObject().put("_id",clientJson.getString("workshopId")),res ->{

            if (res.succeeded() && !res.result().isEmpty()){

              EnteredCourse.setTeacherOnWorkshop(client, new ArrayList<JsonObject>() , res.result() , 0 , resSetTeacher ->{
                toResponse.put("status","true")
                  .put("body",resSetTeacher.result().get(0).put("role",""));
                ctx.response().end(toResponse.toString());
              });
            }

            else {//we don't find workshop in entered course

              toResponse.put("status","false")
                .put("msg","Workshop doesn't exist in database");
              ctx.response().end(toResponse.toString());

            }
          });
        }

        else {//find role in roles and put roleName

          client.find(Const.enteredCourse, new JsonObject().put("_id", clientJson.getString("workshopId")), res -> {

            if (res.succeeded() && !res.result().isEmpty()) {

              EnteredCourse.setTeacherOnWorkshop(client, new ArrayList<JsonObject>(), res.result(), 0, resSetTeacher -> {
                JsonObject response = new JsonObject();
//                response.put("body", resSetTeacher.result().get(0));

                client.find(Const.role, new JsonObject().put("_id" , roleId) , resFindRole->{
                  if (resFindRole.succeeded() && !resFindRole.result().isEmpty()){
//                    response.put("roleName",resFindRole.result().get(0).getString("roleName"));
                    if ( resFindRole.result().get(0).getString("roleName").equals("Teacher")){
                      Form.findForm(client , new ArrayList<JsonObject>() , resFindRole.result().get(0).getJsonArray("form") , 0 , resFindForm->{
                        response.put("body", resSetTeacher.result().get(0).put("roleName", resFindRole.result().get(0).getString("roleName"))
                          .put("formBody",resFindForm.result().get(0)));
//                      response.put("formBody",resFindForm.result().toString());
                      });
                    }
                    else {
                      response.put("body", resSetTeacher.result().get(0).put("roleName", resFindRole.result().get(0).getString("roleName")));
                    }
                  }
                });
                toResponse.put("status", "true");
                ctx.response().end(toResponse.put("body",response).toString());
              });
            }
            else {//we don't find workshop in entered course
              toResponse.put("status", "false")
                .put("msg", "Workshop doesn't exist in database");
              ctx.response().end(toResponse.toString());
            }
          });
        }

//        ctx.response().end(toResponse.toString());
      });

    //new added
    router.route(Const.userTeacherAcceptGrader)
      .handler(ctx ->{
        JsonObject clientJson = ctx.get("clientJson");
        JsonObject userJson = ctx.get("userJson");
        User user = new User(userJson);
        JsonObject toResponse = new JsonObject();

        Grader.teacherAcceptGrader(client , user , clientJson , handler ->{
          if (handler.succeeded()){
            toResponse
              .put("status",true)
              .put("msg",handler.result());
          }
          else {
            toResponse
              .put("status",false)
              .put("msg",handler.cause().toString());
          }
          ctx.response().end(toResponse.toString());
        });
      });


    router.route(Const.userMessege)
      .handler(ctx->{

        JsonObject clientJson = ctx.get("clientJson");
        JsonObject userJson = ctx.get("userJson");
        String text = clientJson.getString("text");
        User sender = new User(userJson);
        User receiver = new User(clientJson.getString("receiver"));
        String replyId = clientJson.getString("replyId");
        Messege reply = new Messege(replyId);

        Messege messege = new Messege(text,reply);
        MessegeRelation messegeRelation = new MessegeRelation(sender,receiver,messege);
        messegeRelation.saveToDB(client);
        messege.saveToDB(client);

        ctx.response().end(new JsonObject()
        .put("status","true")
        .put("msg","Your messege have been sent successfully")
        .toString());

      });


    router.route(Const.userMakeGroup)
      .handler(ctx ->{
        JsonObject clientJson = ctx.get("clientJson");
        JsonObject userJson = ctx.get("userJson");
        JsonObject toResponse = new JsonObject();
        User user = new User(userJson);

        Teacher.userMakeGroup(client , user , clientJson , handler ->{
          if (handler.succeeded()){
            toResponse
              .put("status","true")
              .put("msg",handler.result());
          }
          else {
            toResponse
              .put("status","false")
              .put("msg",handler.cause().toString());
          }
          ctx.response().end(toResponse.toString());
        });

      });


    router.route(Const.dashboard)
      .handler(ctx ->{
        JsonObject userJson = ctx.get("userJson");
        JsonObject clientJson = ctx.get("clientJson");

        User user = new User(userJson);

          EnteredCourse.setRolesOnWorkshops(client, new ArrayList<>(),
            user.getRolesId(),
            0,resSetRoles->{


              EnteredCourse.myWorkshops(client,new ArrayList<JsonObject>(),resSetRoles.result(),0,resMyWorkshop->{

                ArrayList<JsonObject> someOfMyWorkshop = new ArrayList<>();
                int x;

                if(resMyWorkshop.result().size() > 5)
                  x = 5;
                else
                  x = resMyWorkshop.result().size();

                for (int i = 0 ; i < x ; i++)
                  someOfMyWorkshop.add(resMyWorkshop.result().get(i));


                EnteredCourse.setTeacherOnMyWorkshops(client,new ArrayList<JsonObject>(),someOfMyWorkshop,0,resFinal->{

                  client.find(Const.contactPoint,new JsonObject()
                    .put("_id",user.getContactPointId()),resCP->{

                      client.find(Const.messegeRelation,new JsonObject().put("receiver",user.getUsername()),resReceiver->{

                        client.find(Const.enteredCourse,new JsonObject(),resWorkshops->{

                          EnteredCourse.setTeacherOnWorkshop(client,new ArrayList<JsonObject>(),resWorkshops.result(),0,resResolvedWorkshop->{


                            ArrayList<JsonObject> newWorkshops = new ArrayList<JsonObject>();

                            int limit;

                            if (resWorkshops.result().size() >= 5)
                              limit = 5;

                            else
                              limit = resWorkshops.result().size();

                            for( int i = 0 ; i < limit ; i++)
                              newWorkshops.add(resResolvedWorkshop.result().get(i));

                            JsonObject body = new JsonObject().put("workshops",resFinal.result())
                              .put("user",user.toJson())
                              .put("contactPoint",resCP.result().get(0))
                              .put("username",userJson.getString("username"))
                              .put("newWorkshops",newWorkshops)
                              .put("messages",resReceiver.result());
                            ctx.response().end(new JsonObject().put("status","true")
                              .put("body",body).toString());


                          });



                        });




                      });






                  });



                });

              });

            });


      });
    ////////////////////////////////////


    router.route().path(Const.adminStar)
      .handler(BodyHandler.create())
      .handler(ctx ->{

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

            Course course = new Course(name,description,"");
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

        Course course = new Course(clientJson.getString("name"),clientJson.getString("description"),"");



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
            response.end("Access Denied.");
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



    //////////////////////////////////////



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

