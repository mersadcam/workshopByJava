package model;

import java.util.ArrayList;

public class PaymentStatus {

	private Status paymentStatus;
	private String totalValue;
	private String paid;
	private String notPaid;

	private ArrayList<Payment> payments = new ArrayList<Payment>();

	enum Status {
		IN_PAYMENT,
		PAID
	}

}
