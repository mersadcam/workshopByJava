package controller;


public class Const {

  public static final String contactPoint = "contactPoint";
  public static final String course = "course";
  public static final String workshop = "enteredCourse";
  public static final String admin = "admin";
  public static final String user = "user";
  public static final String superAdmin = "superAdmin";
  public static final String role = "role";
  public static final String form = "form";
  public static final String requestType = "requestType";//collection for grader and student
  public static final String identity = "identity";
  public static final String report = "report";
  public static final String answer = "answer";
  private static int workshopNumber = 0 ;


  public static int getWorkshopNumber() {
    workshopNumber++;
    return workshopNumber;
  }
}
