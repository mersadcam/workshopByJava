package school;

import java.util.Date;

public class Payment {
	
	private String paymentName;
	private Date time;
	private String value;
	private Status paymentStatus;
	
	enum Status {
		PAID,
		NOTPAID
	}
	

}
