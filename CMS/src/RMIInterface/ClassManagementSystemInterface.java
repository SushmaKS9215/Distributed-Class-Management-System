package RMIInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public interface ClassManagementSystemInterface extends Remote {

	
	public boolean createTRecord(String firstName, String lastName,String address,String phone,String specialization,String location) throws RemoteException;
	public boolean createSReacord(String firstName,String lastName,ArrayList<String> courseRegistered,boolean status,Date statusDate) throws RemoteException;
	public String getRecordCounts() throws RemoteException;
	public boolean editRecord(String recordID,String fieldName,String newValue) throws RemoteException;
	public void printData() throws RemoteException;

}
