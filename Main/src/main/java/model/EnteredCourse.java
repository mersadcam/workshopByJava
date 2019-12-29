package model;

import java.util.ArrayList;
import java.util.Date;

public class EnteredCourse {

	private Course course;
	private Date startTime;
	private Date finishTime;

	private String place;
	private int capacity;
	private String description;

	private ArrayList<Section> sections = new ArrayList<Section>();

	enum Section{

		SAT,SUN,MON,TUE,WED,THU,FRI;

		private String time;

		public Section setTime(String time) {
			this.time = time;
			return this;

		}

		public String getTime() {
			return this.time;
		}

	}

}
