package controller;


public class Const {

  public static final String contactPoint = "contactPoint";
  public static final String course = "course";
  public static final String workshop = "enteredCourse";
  public static final String admin = "admin";
  public static final String user = "user";
  public static final String superAdmin = "superAdmin";
  public static final String teacher = "teacher";
  public static final String form = "form";
  private static int workshopNumber = 0 ;


  public static int getWorkshopNumber() {
    workshopNumber++;
    return workshopNumber;
  }
}
