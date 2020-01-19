package controller;


import java.util.Random;

public class Const {


  private static int enteredCourseId = 0;

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





  //Routes
  public static final String userStar = "/user/*";
  public static final String register = "/register";
  public static final String login = "/login" ;
  public static final String signout = "/user/signout";
  public static final String userProfileEdit ="/user/profile/edit";
  public static final String userWorkshops ="/user/workshops";
  public static final String userWorkshopStudentRequest ="/user/workshop/studentRequest";
  public static final String userWorkshopGraderRequest = "/user/workshop/graderRequest";
  public static final String userWorkshop = "/user/workshop";
  public static final String userMakeGroup = "/user/workshop/makeGroup";
  public static final String userWorkshopNewForm ="/user/workshop/newForm";
  public static final String userGraderReport ="/user/graderReport";
  public static final String userFinalReport ="/user/finalReport";
  public static final String adminCreateNewCourse ="/admin/createNewCourse";
  public static final String adminEnterNewWorkshop ="/admin/enterNewWorkshop";
  public static final String superAdminCreateAdmin ="/superAdmin/createAdmin";


  public static int getEnteredCourseId() {
//    enteredCourseId++; for system we should run it
    enteredCourseId = new Random().nextInt();
    enteredCourseId = Math.abs(enteredCourseId);
    return enteredCourseId;
  }

  public static void setEnteredCourseId(int number){
    enteredCourseId = number;
  }
}
