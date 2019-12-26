package model;

import java.util.ArrayList;

public class Report {

	private Status studentCourseStatus;
	private String finalNumber;
	private Performance performance;
	private ArrayList<FormAnswer> data = new ArrayList<FormAnswer>();



	enum Status{
		PASSED,
		NOTPASSED
	}

	enum Performance{
		BAD,
		NOTBAD,
		GOOD,
		EXCELLENT
	}

}
