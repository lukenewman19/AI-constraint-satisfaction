import java.util.ArrayList;
/*
 * Class Semester represents a semester for the local search program
 * 	contains a list of courses taken for the semester, and a season description
 */
public class Semester {
	
	private int number;
	private String season;
	ArrayList<Course> list;
	/*
	 * constructor for Semester takes in as arguments a number and a season
	 * 	instance variables are instantiated, the list of courses is initially empty
	 */
	public Semester(int number, String season){
		this.number = number;
		this.season = season;
		this.list = new ArrayList<Course>();
	}
	/*
	 * addCourse(Course course) assigns a Course to this semester
	 */
	public void addCourse(Course course){
		list.add(course);
	}
	/*
	 * getList() returns the ArrayList of courses assigned to this semester
	 */
	public ArrayList<Course> getList(){
		return list;
	}
	/*
	 * getSeason() returns the season of the semester
	 */
	public String getSeason(){
		return season;
	}
}
