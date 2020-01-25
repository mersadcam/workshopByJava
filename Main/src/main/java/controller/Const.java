package controller;


import java.util.Random;

public class Const {



  private static int enteredCourseId = 0;

  public static final String host = "localhost";
  public static final int port = 8000;

  //Collections
  public static final String contactPoint = "contactPoint";
  public static final String course = "course";
  public static final String enteredCourse = "enteredCourse";
  public static final String user = "user";
  public static final String role = "role";
  public static final String form = "form";
  public static final String report = "report";
  public static final String answer = "answer";
  public static final String group = "group";
  public static final String payment = "payment";
  public static final String paymentStatus = "paymentStatus";
  public static final String formAnswer = "formAnswer";
  public static final String student = "student";
  public static final String grader = "grader";
  public static final String messege = "messege";
  public static final String messegeRelation = "messegeRelation";



  //Routes
  public static final String userStar = "/user/*";
  public static final String adminStar = "/admin/*";
  public static final String register = "/register";
  public static final String login = "/login" ;
  public static final String signout = "/user/signout";
  public static final String dashboard = "/user/dashboard"; //nazadeh
  public static final String workshopStar = "/user/workshop/*"; //nazadeh
  public static final String userProfileEdit ="/user/profile/edit";
  public static final String userProfile ="/user/profile/"; //nazadim
  public static final String userWorkshops ="/user/workshops"; //tamiz it
  public static final String userWorkshopStudentRequest ="/user/workshop/studentRequest";
  public static final String userWorkshopGraderRequest = "/user/workshop/graderRequest";
  public static final String userWorkshopPage = "/user/workshop/page";
  public static final String userMakeGroup = "/user/workshop/makeGroup";
  public static final String userWorkshopNewForm ="/user/workshop/newForm";
  public static final String userGraderReport ="/user/workshop/graderReport";//need test
  public static final String userTeacherAcceptGrader = "/user/workshop/acceptGrader";
  public static final String workshopPage = "/user/workshop/page";
  public static final String userStudentFinalReport ="/user/workshop/StudentfinalReport";//nazadeh
  public static final String adminCreateNewCourse ="/admin/createNewCourse";
  public static final String adminEnterNewWorkshop ="/admin/enterNewWorkshop";
  public static final String superAdminCreateAdmin ="/superAdmin/createAdmin";
  public static final String userMessege = "/user/messege";
  public static final String profile = "/user/profile";
  public static final String info = "/user/info";
  public static final String courses = "/user/course";

  public static final String uploadBackgroundImage = "/img/profile/background";
  public static final String uploadProfileImage = "/img/profile/main";
  public static final String uploadWorkshopImage = "/img/workshop";


  public static String generateWorkshopId() {
//    enteredCourseId++; for system we should run it
    enteredCourseId = new Random().nextInt();
    enteredCourseId = Math.abs(enteredCourseId);
    return String.valueOf(enteredCourseId);
  }

  public static void setEnteredCourseId(int number){
    enteredCourseId = number;
  }
}
