import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Random;

/*
 * Class ProgramC represents the local search
 * 	the two instance variables will contain the two forms of desired output once the search is complete
 */
public class ProgramC {
	
	String verbose = "";
	String summary = "";
	/*
	 * Constructor for ProgramC 
	 * 	Runs the local search and instantiates the two instance variables
	 */
	public ProgramC() {
		/*
		 * Math120,Math210,Math215
		 * ICS140,ICS141,ICS232,ICS240,ICS311,ICS340,ICS365,ICS372,ICS440,ICS460,ICS462,ICS490,ICS492,ICS499
		 * LS998,LS999
		*/
		Course MATH120 = new Course("MATH120");
		Course MATH210 = new Course("MATH210");
		Course MATH215 = new Course("Math215");
		Course ICS140 = new Course("ICS140");
		Course ICS141 = new Course("ICS141");
		Course ICS232 = new Course("ICS232");
		Course ICS240 = new Course("ICS240");
		Course ICS311 = new Course("ICS311");
		Course ICS340 = new Course("ICS340");
		Course ICS365 = new Course("ICS365");
		Course ICS372 = new Course("ICS372");
		Course ICS440 = new Course("ICS440");
		Course ICS460 = new Course("ICS460");
		Course ICS462 = new Course("ICS462");
		Course ICS490 = new Course("ICS490");
		Course ICS492 = new Course("ICS492");
		Course ICS499 = new Course("ICS499");
		Course LS998 = new Course("LS998");
		Course LS999 = new Course("LS999");
		// Hardcoded courses course contained within the list in dept/numerical order
		Course[] courseList = {MATH120,MATH210,MATH215,ICS140,ICS141,ICS232,ICS240,ICS311,ICS340,ICS365,ICS372,
				ICS440,ICS460,ICS462,ICS490,ICS492,ICS499,LS998,LS999};
		// Seven semester objects instantiated
		Semester semester1 = new Semester(1, "Summer");
		Semester semester2 = new Semester(2, "Autumn");
		Semester semester3 = new Semester(3, "Spring");
		Semester semester4 = new Semester(4, "Summer");
		Semester semester5 = new Semester(5, "Autumn");
		Semester semester6 = new Semester(6, "Spring");
		Semester semester7 = new Semester(7, "Summer");
		// Seven semester objects stored in a list and placed at the appropriate index
		Semester[] semesterList = {null, semester1,semester2,semester3,semester4,semester5,semester6,semester7};
		/*
		 * Variables used throughout the local search process
		 * assign - The random number generated by Math.random()
		 * eval - Heuristic value that contains the current number of conflicts (initially set to any number != 0)
		 * priorityQueue - heap of courses prioritized by the number of conflicts for each course (Max Heap)
		 * randomWalk - list of courses that will contain all the courses involved in a conflict (random walk stage of search)
		 * anyConflictCourse - the course randomly selected from the randomWalk ArrayList
		 * extract - the course selected from the priority queue (used during the two stage part of the search)
		 * currentSemester - the semester number that a course is currently set to
		 * optimumSemester - the semester number that is the optimum semester for a course to be moved to for that iteration
		 * previouslySetSemester - in order to evaluate which semester is optimum I actually do change the current semester
		 * 		so in order to keep track which semester I have assigned for a course this variable is used (I may want to change
		 * 		it back to the "currentSemester" e.g. in a local optimum)
		 * possibleEval - while evaluating semester changes I store the heuristic value here in order to compare it with the optimum
		 * minimumEval - the optimum heuristic value obtained once a course has been selected
		 * verbose - the beginning two lines of the verbose output
		 */
		int assign;
		int eval = 100;
		MaxHeap priorityQueue;
		ArrayList<Course> randomWalk;
		Course anyConflictCourse = null;
		Course extract;
		int currentSemester;
		int optimumSemester;
		int previouslySetSemester;
		int possibleEval;
		int minimumEval;
		verbose = "120 210 215 140 141 232 240 311 340 365 372 440 460 462 490 492 499 998 999 "
				+ "\n ----------------------------------------------------------------------------\n ";
		/*
		 * The main loop will continue until I've found a solution
		 */
		while (eval != 0){
			// Not useful in the beginning but every random restart resets the semesters
			for (int i = 1; i < 8; i++){
				while(semesterList[i].getList().size() > 0){
					semesterList[i].getList().remove(0);
				}
			}
			// random assignment of semesters given to each course
			for (int i = 0; i < courseList.length; i++){
				assign = (int)(Math.random()*7 + 1);
				semesterList[assign].addCourse(courseList[i]);
				courseList[i].setSemester(assign);
			}
			// get the heuristic value for the random assignment
			eval = evaluate(courseList,semesterList);
			// adds the first line of assignments from the random sample to the verbose output
			for (int i = 0; i < courseList.length; i++){
				verbose = verbose + courseList[i].getSemester() + "    ";
			}
			// concatenates the heuristic value at the end of the line
			verbose = verbose + "Eval: " + eval + "\n ";
			// creates new priority queue
			priorityQueue = new MaxHeap(courseList);
			// instantiates an ArrayList that may be used for the any conflict stage of the search
			randomWalk = new ArrayList<Course>();
			// If problem is not solved within 150 iterations of the random sample I start over
			int count = 0;
			while (count < 150 && eval !=0){
				// heapExtractMax() must be called within a try-catch block
				try {
					/*
					 * Two Stage part of search
					 * 	extract the course with the maximum number of conflicts from the queue and try to find the optimum semester
					 */
					extract = priorityQueue.heapExtractMax();
					currentSemester = extract.getSemester();
					optimumSemester = currentSemester;
					previouslySetSemester = currentSemester;
					minimumEval = eval;
					for (int i = 1; i < 8; i++){
						if (i != currentSemester){
							extract.setSemester(i);
							semesterList[previouslySetSemester].getList().remove(extract);
							semesterList[i].getList().add(extract);
							previouslySetSemester = i;
							possibleEval = evaluateTest(courseList, semesterList);
							if (possibleEval < minimumEval){
								optimumSemester = i;
								minimumEval = possibleEval;
							}
						}
					}
					/*
					 *  previouslySetSemester is always going to equal 7 at this point 
					 *  	because it was the last semester tried for evaluation
					 *  I change it back to the optimum semester found if it is not 7
					 */
					if (previouslySetSemester != optimumSemester){
						extract.setSemester(optimumSemester);
						semesterList[previouslySetSemester].getList().remove(extract);
						semesterList[optimumSemester].getList().add(extract);
					}
					/*
					 * The course with the maximum number of conflicts is already assigned the optimum semester
					 * The search has reached a local minimum and a new strategy is used
					 * This is the Any Conflict part of the course
					 */
					if (currentSemester == optimumSemester){
						// Obtain a list of courses that are involved in at least one conflict
						for(int i = 0; i < courseList.length; i++){
							if (courseList[i].getNumberOfConflicts() > 0){
								randomWalk.add(courseList[i]);
							}
						}
						// generate a random number from 0 to the size of the list - 1
						assign = (int)(Math.random()*randomWalk.size());
						// the number is used to randomly select a course from the array
						anyConflictCourse = randomWalk.get(assign);
						/*
						 * I try to find the optimum semester for that randomly selected course
						 */
						currentSemester = anyConflictCourse.getSemester();
						optimumSemester = currentSemester;
						previouslySetSemester = currentSemester;
						for (int i = 1; i < 8; i++){
							if (i != currentSemester){
								anyConflictCourse.setSemester(i);
								semesterList[previouslySetSemester].getList().remove(anyConflictCourse);
								semesterList[i].getList().add(anyConflictCourse);
								previouslySetSemester = i;
								possibleEval = evaluateTest(courseList, semesterList);
								if (possibleEval < minimumEval){
									optimumSemester = i;
									minimumEval = possibleEval;
								}
							}
						}
						/*
						 *  previouslySetSemester is always going to equal 7 at this point 
						 *  	because it was the last semester tried for evaluation
						 *  I change it back to the optimum semester found if it is not 7
						 */
						if (previouslySetSemester != optimumSemester){
							anyConflictCourse.setSemester(optimumSemester);
							semesterList[previouslySetSemester].getList().remove(anyConflictCourse);
							semesterList[optimumSemester].getList().add(anyConflictCourse);
						}
						/*
						 *  If the randomly selected course is already at its optimum semester 
						 *  	again I use a new strategy
						 *  This time I don't worry about optimizing.  I randomly select a course from the
						 *  	ArrayList of courses involved with any conflict and I randomly assign a semester to it
						 */
						if(currentSemester == optimumSemester){
							assign = (int)(Math.random()*randomWalk.size());
							anyConflictCourse = randomWalk.get(assign);
							currentSemester = anyConflictCourse.getSemester();
							int randomSemester = currentSemester;
							// to make sure randomly assigned semester is not the same as the current
							while (randomSemester == currentSemester){
								randomSemester = (int)(Math.random()*7 + 1);
							}
							anyConflictCourse.setSemester(randomSemester);
							semesterList[currentSemester].getList().remove(anyConflictCourse);
							semesterList[randomSemester].getList().add(anyConflictCourse);
						}
						// reset the ArrayList of any conflict courses
						for (int r = randomWalk.size(); r > 0; r--){
							randomWalk.remove(r-1);
						}
					}
					// The search has made a new assignment and so must be reevaluated - the courses' number of conflicts 
					eval = evaluate(courseList, semesterList);
					/*
					 *  After reevaluating the courses' number of conflicts, I insert the extracted course back into the heap
					 *  	and build the heap
					 */	
					priorityQueue.heapInsert(extract);
					priorityQueue.buildHeap();
					// Add another line to the verbose output after new assignment
					for (int i = 0; i < courseList.length; i++){
						verbose = verbose + courseList[i].getSemester() + "    ";
					}
					verbose = verbose + "Eval: " + eval + "\n ";
				} catch (Exception ex){
					System.out.println(ex.getMessage());
				}
				count ++;
				// end of iteration - goes back up to the second while loop (The Two Stage part)
			}
			// Random Restart
		}
		// Outside the outer while loop so eval = 0 i.e. problem solved so obtain the desired summary output
		summary = createSummary(semesterList);
		// End of ProgramC constructor - solution found and both summary and verbose outputs have been created
	}
	/*
	 * Main method - Program starts by prompting the user to select a choice of output
	 */
	public static void main(String[] args) {
		UI ui = new UI();
	}
	/*
	 * getVerbose() retrieves the verbose output
	 */
	public String getVerbose(){
		return verbose;
	}
	/*
	 * getSummary() retrieves the summary output
	 */
	public String getSummary(){
		return summary;
	}
	/*
	 * evaluate(Course[] courses, Semester[] semesters) takes in as parameter an array of courses and an array of semesters
	 * 	outputs the heuristic value for the assignment of semesters to courses or vice versa)
	 * 	In addition to finding the heuristic value for the total assignment, it also updates the heuristic value for each course
	 */
	public int evaluate(Course[] courses, Semester[] semesters){
		for (int i = 0; i < courses.length; i++){
			courses[i].setNumberOfConflicts(0);
		}
		int eval = 0;
		// Every conditional represents a constraint
		if (courses[0].getSemester() >= courses[1].getSemester()){
			courses[0].increaseNumberOfConflicts();
			courses[1].increaseNumberOfConflicts();
			eval++;
		}
		
		if (courses[0].getSemester() >= courses[2].getSemester()){
			courses[0].increaseNumberOfConflicts();
			courses[2].increaseNumberOfConflicts();
			eval++;
		}
		
		if (courses[0].getSemester() > courses[3].getSemester()){
			courses[0].increaseNumberOfConflicts();
			courses[3].increaseNumberOfConflicts();
			eval++;
		}
		
		if (courses[0].getSemester() >= courses[11].getSemester()){
			courses[0].increaseNumberOfConflicts();
			courses[11].increaseNumberOfConflicts();
			eval++;
		}
		
		if (courses[0].getSemester() >= courses[12].getSemester()){
			courses[0].increaseNumberOfConflicts();
			courses[12].increaseNumberOfConflicts();
			eval++;
		}
		
		if (courses[0].getSemester() >= courses[13].getSemester()){
			courses[0].increaseNumberOfConflicts();
			courses[13].increaseNumberOfConflicts();
			eval++;
		}
		
		if (courses[0].getSemester() >= courses[14].getSemester()){
			courses[0].increaseNumberOfConflicts();
			courses[14].increaseNumberOfConflicts();
			eval++;
		}
		
		if (courses[0].getSemester() >= courses[15].getSemester()){
			courses[0].increaseNumberOfConflicts();
			courses[15].increaseNumberOfConflicts();
			eval++;
		}
		
		if (courses[0].getSemester() >= courses[16].getSemester()){
			courses[0].increaseNumberOfConflicts();
			courses[16].increaseNumberOfConflicts();
			eval++;
		}
		
		if (courses[1].getSemester() >= courses[11].getSemester()){
			courses[1].increaseNumberOfConflicts();
			courses[11].increaseNumberOfConflicts();
			eval++;
		}
		
		if (courses[1].getSemester() >= courses[12].getSemester()){
			courses[1].increaseNumberOfConflicts();
			courses[12].increaseNumberOfConflicts();
			eval++;
		}
		
		if (courses[1].getSemester() >= courses[13].getSemester()){
			courses[1].increaseNumberOfConflicts();
			courses[13].increaseNumberOfConflicts();
			eval++;
		}
		
		if (courses[1].getSemester() >= courses[14].getSemester()){
			courses[1].increaseNumberOfConflicts();
			courses[14].increaseNumberOfConflicts();
			eval++;
		}
		
		if (courses[1].getSemester() >= courses[15].getSemester()){
			courses[1].increaseNumberOfConflicts();
			courses[15].increaseNumberOfConflicts();
			eval++;
		}
		
		if (courses[1].getSemester() >= courses[16].getSemester()){
			courses[1].increaseNumberOfConflicts();
			courses[16].increaseNumberOfConflicts();
			eval++;
		}
		
		if (courses[2].getSemester() > courses[4].getSemester()){
			courses[2].increaseNumberOfConflicts();
			courses[4].increaseNumberOfConflicts();
			eval++;
		}
		
		if (courses[2].getSemester() >= courses[11].getSemester()){
			courses[2].increaseNumberOfConflicts();
			courses[11].increaseNumberOfConflicts();
			eval++;
		}
		
		if (courses[2].getSemester() >= courses[12].getSemester()){
			courses[2].increaseNumberOfConflicts();
			courses[12].increaseNumberOfConflicts();
			eval++;
		}
		
		if (courses[2].getSemester() >= courses[13].getSemester()){
			courses[2].increaseNumberOfConflicts();
			courses[13].increaseNumberOfConflicts();
			eval++;
		}
		
		if (courses[2].getSemester() >= courses[14].getSemester()){
			courses[2].increaseNumberOfConflicts();
			courses[14].increaseNumberOfConflicts();
			eval++;
		}
		
		if (courses[2].getSemester() >= courses[15].getSemester()){
			courses[2].increaseNumberOfConflicts();
			courses[15].increaseNumberOfConflicts();
			eval++;
		}
		
		if (courses[2].getSemester() >= courses[16].getSemester()){
			courses[2].increaseNumberOfConflicts();
			courses[16].increaseNumberOfConflicts();
			eval++;
		}
		
		if (courses[3].getSemester() >= courses[4].getSemester()){
			courses[3].increaseNumberOfConflicts();
			courses[4].increaseNumberOfConflicts();
			eval++;
		}
		
		if (courses[3].getSemester() >= courses[11].getSemester()){
			courses[3].increaseNumberOfConflicts();
			courses[11].increaseNumberOfConflicts();
			eval++;
		}
		
		if (courses[3].getSemester() >= courses[12].getSemester()){
			courses[3].increaseNumberOfConflicts();
			courses[12].increaseNumberOfConflicts();
			eval++;
		}
		
		if (courses[3].getSemester() >= courses[13].getSemester()){
			courses[3].increaseNumberOfConflicts();
			courses[13].increaseNumberOfConflicts();
			eval++;
		}
		
		if (courses[3].getSemester() >= courses[14].getSemester()){
			courses[3].increaseNumberOfConflicts();
			courses[14].increaseNumberOfConflicts();
			eval++;
		}
		
		if (courses[3].getSemester() >= courses[15].getSemester()){
			courses[3].increaseNumberOfConflicts();
			courses[15].increaseNumberOfConflicts();
			eval++;
		}
		
		if (courses[3].getSemester() >= courses[16].getSemester()){
			courses[3].increaseNumberOfConflicts();
			courses[16].increaseNumberOfConflicts();
			eval++;
		}
		
		if (courses[4].getSemester() >= courses[5].getSemester()){
			courses[4].increaseNumberOfConflicts();
			courses[5].increaseNumberOfConflicts();
			eval++;
		}
		
		if (courses[4].getSemester() >= courses[6].getSemester()){
			courses[4].increaseNumberOfConflicts();
			courses[6].increaseNumberOfConflicts();
			eval++;
		}
		
		if (courses[4].getSemester() >= courses[7].getSemester()){
			courses[4].increaseNumberOfConflicts();
			courses[7].increaseNumberOfConflicts();
			eval++;
		}
		
		if (courses[4].getSemester() >= courses[11].getSemester()){
			courses[4].increaseNumberOfConflicts();
			courses[11].increaseNumberOfConflicts();
			eval++;
		}
		
		if (courses[4].getSemester() >= courses[12].getSemester()){
			courses[4].increaseNumberOfConflicts();
			courses[12].increaseNumberOfConflicts();
			eval++;
		}
		
		if (courses[4].getSemester() >= courses[13].getSemester()){
			courses[4].increaseNumberOfConflicts();
			courses[13].increaseNumberOfConflicts();
			eval++;
		}
		
		if (courses[4].getSemester() >= courses[14].getSemester()){
			courses[4].increaseNumberOfConflicts();
			courses[14].increaseNumberOfConflicts();
			eval++;
		}
		
		if (courses[4].getSemester() >= courses[15].getSemester()){
			courses[4].increaseNumberOfConflicts();
			courses[15].increaseNumberOfConflicts();
			eval++;
		}
		
		if (courses[4].getSemester() >= courses[16].getSemester()){
			courses[4].increaseNumberOfConflicts();
			courses[16].increaseNumberOfConflicts();
			eval++;
		}
		
		if (courses[5].getSemester() >= courses[11].getSemester()){
			courses[5].increaseNumberOfConflicts();
			courses[11].increaseNumberOfConflicts();
			eval++;
		}
		
		if (courses[5].getSemester() >= courses[12].getSemester()){
			courses[5].increaseNumberOfConflicts();
			courses[12].increaseNumberOfConflicts();
			eval++;
		}
		
		if (courses[5].getSemester() >= courses[13].getSemester()){
			courses[5].increaseNumberOfConflicts();
			courses[13].increaseNumberOfConflicts();
			eval++;
		}
		
		if (courses[5].getSemester() >= courses[14].getSemester()){
			courses[5].increaseNumberOfConflicts();
			courses[14].increaseNumberOfConflicts();
			eval++;
		}
		
		if (courses[5].getSemester() >= courses[15].getSemester()){
			courses[5].increaseNumberOfConflicts();
			courses[15].increaseNumberOfConflicts();
			eval++;
		}
		
		if (courses[5].getSemester() >= courses[16].getSemester()){
			courses[5].increaseNumberOfConflicts();
			courses[16].increaseNumberOfConflicts();
			eval++;
		}
		
		if (courses[6].getSemester() >= courses[7].getSemester()){
			courses[6].increaseNumberOfConflicts();
			courses[7].increaseNumberOfConflicts();
			eval++;
		}
		
		if (courses[6].getSemester() >= courses[8].getSemester()){
			courses[6].increaseNumberOfConflicts();
			courses[8].increaseNumberOfConflicts();
			eval++;
		}
		
		if (courses[6].getSemester() >= courses[9].getSemester()){
			courses[6].increaseNumberOfConflicts();
			courses[9].increaseNumberOfConflicts();
			eval++;
		}
		
		if (courses[6].getSemester() >= courses[10].getSemester()){
			courses[6].increaseNumberOfConflicts();
			courses[10].increaseNumberOfConflicts();
			eval++;
		}
		
		if (courses[6].getSemester() >= courses[11].getSemester()){
			courses[6].increaseNumberOfConflicts();
			courses[11].increaseNumberOfConflicts();
			eval++;
		}
		
		if (courses[6].getSemester() >= courses[12].getSemester()){
			courses[6].increaseNumberOfConflicts();
			courses[12].increaseNumberOfConflicts();
			eval++;
		}
		
		if (courses[6].getSemester() >= courses[13].getSemester()){
			courses[16].increaseNumberOfConflicts();
			courses[13].increaseNumberOfConflicts();
			eval++;
		}
		
		if (courses[6].getSemester() >= courses[14].getSemester()){
			courses[6].increaseNumberOfConflicts();
			courses[14].increaseNumberOfConflicts();
			eval++;
		}
		
		if (courses[6].getSemester() >= courses[15].getSemester()){
			courses[6].increaseNumberOfConflicts();
			courses[15].increaseNumberOfConflicts();
			eval++;
		}
		
		if (courses[6].getSemester() >= courses[16].getSemester()){
			courses[6].increaseNumberOfConflicts();
			courses[16].increaseNumberOfConflicts();
			eval++;
		}
		
		if (courses[8].getSemester() >= courses[11].getSemester()){
			courses[8].increaseNumberOfConflicts();
			courses[11].increaseNumberOfConflicts();
			eval++;
		}
		
		if (courses[10].getSemester() >= courses[16].getSemester()){
			courses[10].increaseNumberOfConflicts();
			courses[16].increaseNumberOfConflicts();
			eval++;
		}
		// course ICS490 is never offered in the summer
		if (courses[14].getSemester() == 1 || courses[14].getSemester() == 4 || courses[14].getSemester() == 7){
			courses[14].increaseNumberOfConflicts();
			eval++;
		}
		// course ICS492 is only offered in the summer (but it cannot be the 1st summer due to other constraints)
		if (courses[15].getSemester() != 4 && courses[15].getSemester() != 7){
			courses[15].increaseNumberOfConflicts();
			eval++;
		}
		// course ICS499 must be taken in the last semester
		if (courses[16].getSemester() != 7){
			courses[16].increaseNumberOfConflicts();
			eval++;
		}
		// only 3 courses per semester
		ListIterator<Course> it;
		for (int i = 1; i < 8; i++){
			if (semesters[i].getList().size() > 3){
				eval++;
				it = semesters[i].getList().listIterator(0);
				while (it.hasNext()){
					Course temp = it.next();
					temp.increaseNumberOfConflicts();
				}
			}
		}
		return eval;
	}
	/*
	 * evaluateTest(Course[] courses, Semester[] semesters) takes in as parameter an array of courses and an array of semesters
	 * 	outputs the heuristic value for the assignment of semesters to courses or vice versa
	 * 	It only finds the heuristic value for the total assignment, does not update the heuristic value for each course
	 * Used while trying to find the optimum semester (there is no need to update the individual course heuristic value at that time)
	 */
	public int evaluateTest(Course[] courses, Semester[] semesters){
		int eval = 0;
		if (courses[0].getSemester() >= courses[1].getSemester()){
			eval++;
		}
		
		if (courses[0].getSemester() >= courses[2].getSemester()){
			eval++;
		}
		
		if (courses[0].getSemester() > courses[3].getSemester()){
			eval++;
		}
		
		if (courses[0].getSemester() >= courses[11].getSemester()){
			eval++;
		}
		
		if (courses[0].getSemester() >= courses[12].getSemester()){
			eval++;
		}
		
		if (courses[0].getSemester() >= courses[13].getSemester()){
			eval++;
		}
		
		if (courses[0].getSemester() >= courses[14].getSemester()){
			eval++;
		}
		
		if (courses[0].getSemester() >= courses[15].getSemester()){
			eval++;
		}
		
		if (courses[0].getSemester() >= courses[16].getSemester()){
			eval++;
		}
		
		if (courses[1].getSemester() >= courses[11].getSemester()){
			eval++;
		}
		
		if (courses[1].getSemester() >= courses[12].getSemester()){
			eval++;
		}
		
		if (courses[1].getSemester() >= courses[13].getSemester()){
			eval++;
		}
		
		if (courses[1].getSemester() >= courses[14].getSemester()){
			eval++;
		}
		
		if (courses[1].getSemester() >= courses[15].getSemester()){
			eval++;
		}
		
		if (courses[1].getSemester() >= courses[16].getSemester()){
			eval++;
		}
		
		if (courses[2].getSemester() > courses[4].getSemester()){
			eval++;
		}
		
		if (courses[2].getSemester() >= courses[11].getSemester()){
			eval++;
		}
		
		if (courses[2].getSemester() >= courses[12].getSemester()){
			eval++;
		}
		
		if (courses[2].getSemester() >= courses[13].getSemester()){
			eval++;
		}
		
		if (courses[2].getSemester() >= courses[14].getSemester()){
			eval++;
		}
		
		if (courses[2].getSemester() >= courses[15].getSemester()){
			eval++;
		}
		
		if (courses[2].getSemester() >= courses[16].getSemester()){
			eval++;
		}
		
		if (courses[3].getSemester() >= courses[4].getSemester()){
			eval++;
		}
		
		if (courses[3].getSemester() >= courses[11].getSemester()){
			eval++;
		}
		
		if (courses[3].getSemester() >= courses[12].getSemester()){
			eval++;
		}
		
		if (courses[3].getSemester() >= courses[13].getSemester()){
			eval++;
		}
		
		if (courses[3].getSemester() >= courses[14].getSemester()){
			eval++;
		}
		
		if (courses[3].getSemester() >= courses[15].getSemester()){
			eval++;
		}
		
		if (courses[3].getSemester() >= courses[16].getSemester()){
			eval++;
		}
		
		if (courses[4].getSemester() >= courses[5].getSemester()){
			eval++;
		}
		
		if (courses[4].getSemester() >= courses[6].getSemester()){
			eval++;
		}
		
		if (courses[4].getSemester() >= courses[7].getSemester()){
			eval++;
		}
		
		if (courses[4].getSemester() >= courses[11].getSemester()){
			eval++;
		}
		
		if (courses[4].getSemester() >= courses[12].getSemester()){
			eval++;
		}
		
		if (courses[4].getSemester() >= courses[13].getSemester()){
			eval++;
		}
		
		if (courses[4].getSemester() >= courses[14].getSemester()){
			eval++;
		}
		
		if (courses[4].getSemester() >= courses[15].getSemester()){
			eval++;
		}
		
		if (courses[4].getSemester() >= courses[16].getSemester()){
			eval++;
		}
		
		if (courses[5].getSemester() >= courses[11].getSemester()){
			eval++;
		}
		
		if (courses[5].getSemester() >= courses[12].getSemester()){
			eval++;
		}
		
		if (courses[5].getSemester() >= courses[13].getSemester()){
			eval++;
		}
		
		if (courses[5].getSemester() >= courses[14].getSemester()){
			eval++;
		}
		
		if (courses[5].getSemester() >= courses[15].getSemester()){
			eval++;
		}
		
		if (courses[5].getSemester() >= courses[16].getSemester()){
			eval++;
		}
		
		if (courses[6].getSemester() >= courses[7].getSemester()){
			eval++;
		}
		
		if (courses[6].getSemester() >= courses[8].getSemester()){
			eval++;
		}
		
		if (courses[6].getSemester() >= courses[9].getSemester()){
			eval++;
		}
		
		if (courses[6].getSemester() >= courses[10].getSemester()){
			eval++;
		}
		
		if (courses[6].getSemester() >= courses[11].getSemester()){
			eval++;
		}
		
		if (courses[6].getSemester() >= courses[12].getSemester()){
			eval++;
		}
		
		if (courses[6].getSemester() >= courses[13].getSemester()){
			eval++;
		}
		
		if (courses[6].getSemester() >= courses[14].getSemester()){
			eval++;
		}
		
		if (courses[6].getSemester() >= courses[15].getSemester()){
			eval++;
		}
		
		if (courses[6].getSemester() >= courses[16].getSemester()){
			eval++;
		}
		
		if (courses[8].getSemester() >= courses[11].getSemester()){
			eval++;
		}
		
		if (courses[10].getSemester() >= courses[16].getSemester()){
			eval++;
		}
		// course ICS490 is never offered in the summer
		if (courses[14].getSemester() == 1 || courses[14].getSemester() == 4 || courses[14].getSemester() == 7){
			eval++;
		}
		// course ICS492 is only offered in the summer (but it cannot be the 1st summer due to other constraints)
		if (courses[15].getSemester() != 4 && courses[15].getSemester() != 7){
			eval++;
		}
		// course ICS499 must be taken in the last semester
		if (courses[16].getSemester() != 7){
			eval++;
		}
		// only 3 courses per semester
		for (int i = 1; i < 8; i++){
			if (semesters[i].getList().size() > 3){
				eval++;
			}
		}
		return eval;
	}
	/*
	 * createSummary(Semester[] semesters) returns the desired summary output given the list of semesters as an argument
	 */
	public String createSummary(Semester[] semesters){
		String output = "";
		for (int i = 1; i < semesters.length;i++){
			output += "Semester " + i + " " + semesters[i].getSeason() + ":";
			for (int j = 0; j < semesters[i].getList().size(); j++){
					Course c = semesters[i].getList().get(j);
					output += c.getTitle() + " ";
			}
			output += "\n";
		}
		return output;
	}
}

