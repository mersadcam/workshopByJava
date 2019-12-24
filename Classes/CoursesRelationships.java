package school;

import java.util.ArrayList;

public class CoursesRelationships {
	
	private RelationshipType relationshipType = RelationshipType.NEEDED;
	private ArrayList<Course> neededCourses = new ArrayList<Course>();
	
	enum RelationshipType{
		NEEDED
	}

}
