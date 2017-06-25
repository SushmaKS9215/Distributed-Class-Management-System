package Record;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class Student {

	String FirstName;
	String LastName;
	ArrayList<String> CourseRegistered=new ArrayList<String>();
	boolean Status;
	Date StautsDate;
	public String StudentID;
	public static int studentcount=1000;
	
	public Student(){
		StudentID ="SD"+studentcount++;
	}
	
	public String getFirstName() {
		return FirstName;
	}
	public void setFirstName(String firstName) {
		FirstName = firstName;
	}
	public String getLastName() {
		return LastName;
	}
	public void setLastName(String lastName) {
		LastName = lastName;
	}
	public ArrayList<String> getCourseRegistered() {
		return CourseRegistered;
	}
	public void setCourseRegistered(ArrayList<String> courseRegistered) {
		CourseRegistered = courseRegistered;
	}
	public boolean getStatus() {
		return Status;
	}
	public void setStatus(boolean status) {
		Status = status;
	}
	public Date getStautsDate() {
		return StautsDate;
	}
	public void setStautsDate(Date stautsDate) {
		StautsDate = stautsDate;
	}
	
}





