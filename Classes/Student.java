package school;

public class Student implements RequestType{
	
	private PaymentStatus paymentStatus;
	private Status neededCourseStatus;
	
	enum Status{
		PASSED,
		NOTPASSED
	}
	
}
