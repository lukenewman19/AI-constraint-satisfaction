/*
 * Class MaxHeap is an implementation of the Priority Queue for the Local Search program
 * 	Courses are prioritized by increasing number of conflicts
 */
public class MaxHeap {
	
	private Course[] heapArray;
	private int heapSize;
	/*
	 * constructor for MaxHeap takes as an argument an array of courses and builds the heap
	 */
	public MaxHeap(Course[] courseList){
		heapArray = new Course[courseList.length + 1];
		System.arraycopy(courseList, 0, heapArray, 1, courseList.length);
		buildHeap();
	}
	/*
	 * left(int i) returns the left child's index of the course indexed at i
	 */
	public int left(int i){
		return 2*i;
	}
	/*
	 * right(int i) returns the right child's index of the course indexed at i
	 */
	public int right(int i){
		return 2*i + 1;
	}
	/*
	 * parent(int i) returns the parent's index of the course indexed at i
	 */
	public int parent(int i){
		return i/2;
	}
	/*
	 * buildHeap() builds a heap with the current heapArray instance variable
	 */
	public void buildHeap(){
		heapSize = heapArray.length - 1;
		for (int i = heapSize/2; i >= 1; i--){
			heapify(i);
		}
	}
	/*
	 * heapify(int i) will maintain the properties of the heap from the course indexed at i and down that branch of the heap
	 */
	public void heapify(int i){
		int leftChild = left(i);
		int rightChild = right(i);
		int largest = i;
		if ( leftChild <= heapSize && heapArray[leftChild].getNumberOfConflicts() > heapArray[i].getNumberOfConflicts()){
			largest = leftChild;
		}
		if ( rightChild <= heapSize && heapArray[rightChild].getNumberOfConflicts() > heapArray[largest].getNumberOfConflicts()){
			largest = rightChild;
		}
		if (largest != i){
			Course temp = heapArray[largest];
			heapArray[largest] = heapArray[i];
			heapArray[i] = temp;
			heapify(largest);
		}
	}
	/*
	 * getHeapSize() returns the size of the heap
	 */
	public int getHeapSize(){
		return heapSize;
	}
	/*
	 * getHeapArray() returns the heapArray
	 */
	public Course[] getHeapArray(){
		return heapArray;
	}
	/*
	 * heapInsert(Course course) inserts its argument into the heap at the bottom 
	 * 	and then moves it up to its proper place in the heap if it needs to move
	 */
	public void heapInsert(Course course){
		heapSize = heapSize + 1;
		if (heapSize >= heapArray.length){
			Course[] newArray = new Course[heapSize*2+1];
			System.arraycopy( heapArray, 0, newArray, 0, heapSize - 1);
			heapArray = newArray;
			heapArray[heapSize] = course;
			try{
				heapIncreaseKey(heapSize, course);
			} catch (Exception ex){
				System.out.println(ex.getMessage());
			}
		}
		else {
			heapArray[heapSize] = course;
			try{
				heapIncreaseKey(heapSize, course);
			} catch (Exception ex){
				System.out.println(ex.getMessage());
			}
		}
	}
	/*
	 * heapIncreaseKey(int index, Course key) changes the value of the Course indexed at the argument passed in as index
	 * 	the value is changed to key and then moved up the heap for as long as its parent is smaller 
	 */
	public void heapIncreaseKey(int index, Course key) throws Exception{
		if ( key.getNumberOfConflicts() < heapArray[index].getNumberOfConflicts()){
			throw new Exception("New key is less than current key!");
		}
		heapArray[index] = key;
		int i = index;
		while (i > 1 && heapArray[parent(i)].getNumberOfConflicts() < heapArray[i].getNumberOfConflicts()){
			Course temp = heapArray[i];
			heapArray[i] = heapArray[parent(i)];
			heapArray[parent(i)] = temp;
			i = parent(i);
		}
	}
	/*
	 * heapExtractMax() extracts the root of the heap and calls heapify(1) to maintain heap properties
	 * 	precondition - the heap must be a proper max heap
	 */
	public Course heapExtractMax() throws Exception{
		if (heapSize < 1){
			throw new Exception("Heap Underflow");
		}
		else{
			Course max = heapArray[1];
			heapArray[1] = heapArray[heapSize];
			heapSize = heapSize - 1;
			heapify(1);
			return max;
		}
	}
}
