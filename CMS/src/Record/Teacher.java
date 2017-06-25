package Record;

import java.util.concurrent.atomic.AtomicInteger;

public class Teacher {

	String Firstname;
	String Lastname;
	String Address;
	String PhoneNumber;
	String Specialization;
	String Location;
	public String TeacherID;
	static int teachercount=1000;

	public Teacher(){
		TeacherID ="TD"+teachercount++;
		}	
	public String getFiisrtname() {
		return Firstname;
	}
	public void setFiisrtname(String fiisrtname) {
		Firstname = fiisrtname;
	}
	public String getLastname() {
		return Lastname;
	}
	public void setLastname(String lastname) {
		Lastname = lastname;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public String getPhoneNumber() {
		return PhoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		PhoneNumber = phoneNumber;
	}
	public String getSpecialization() {
		return Specialization;
	}
	public void setSpecialization(String specialization) {
		Specialization = specialization;
	}
	public String getLocation() {
		return Location;
	}
	public void setLocation(String location) {
		Location = location;
	}

}
