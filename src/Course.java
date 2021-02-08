/*
 * Class Course represents a course in the Local Search program
 * 	contains a title, number of constraint violations it is involved with, and a proposed semester
 */
public class Course {
	
	private String title;
	private int numberOfConflicts;
	private int semesterNumber;
	/*
	 * constructor for Course takes in as an argument a title for the course
	 * 	numberOfConflicts is initially set to 0
	 */
	public Course(String title){
		this.title = title;
		this.numberOfConflicts = 0;
	}
	/*
	 * getNumberOfConflicts() returns the number of conflicts a course is involved in
	 */
	public int getNumberOfConflicts(){
		return numberOfConflicts;
	}
	/*
	 * setNumberOfConflicts(int i) sets the number of conflicts a course is involved in
	 */
	public void setNumberOfConflicts(int i){
		numberOfConflicts = i;
	}
	/*
	 * getTitle() returns the course title
	 */
	public String getTitle(){
		return title;
	}
	/*
	 * setTitle(String title) sets the course title
	 */
	public void setTitle(String title){
		this.title = title;
	}
	/*
	 * increaseNumberOfConflicts() increments the numberOfConflicts the course is associated with
	 */
	public void increaseNumberOfConflicts(){
		numberOfConflicts++;
	}
	/*
	 * getSemester() returns the semester assigned to the course
	 */
	public int getSemester(){
		return semesterNumber;
	}
	/*
	 * setSemester(int i) assigns a semster to the course
	 */
	public void setSemester(int i){
		semesterNumber = i;
	}

}
