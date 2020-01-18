package controller;


public class Const {

  public static final String contactPoint = "contactPoint";
  public static final String course = "course";
  public static final String enteredCourse = "enteredCourse";
  public static final String admin = "admin";
  public static final String user = "user";
  public static final String superAdmin = "superAdmin";
  public static final String role = "role";
  public static final String form = "form";
  public static final String report = "report";
  public static final String answer = "answer";
  public static final String group = "group";
  private static int enteredCourseId = 0 ;


  public static int getEnteredCourseId() {
    enteredCourseId++;
    return enteredCourseId;
  }
}
