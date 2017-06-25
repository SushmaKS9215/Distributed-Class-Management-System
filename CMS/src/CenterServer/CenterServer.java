package CenterServer;
import RMIInterface.ClassManagementSystemInterface;
import Record.Student;
import Record.Teacher;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Created by Sushma on 04-06-2017.
 */
public class CenterServer extends UnicastRemoteObject implements ClassManagementSystemInterface ,Runnable{
	
    private HashMap<String , ArrayList> hashmapRecords;
    private ArrayList record;
    private Logger logger= Logger.getLogger(CenterServer.class.getName());
    private Logger loggerMTL= Logger.getLogger(CenterServer.class.getName());
    private int UDPPort;
    private int serverPort;
    private String location;

    //Constructor to intialize server port ,UDP port, hashmap and arrayList
    public CenterServer(int serverPort,int UDPPort,String location) throws Exception {
        super();
        this.serverPort=serverPort;
        this.UDPPort=UDPPort;
        this.location=location;
        this.hashmapRecords=new HashMap<String,ArrayList>();
        this.record = new ArrayList();
        FileHandler file_MTL =null;
        FileHandler file_LVL =null;
        FileHandler file_DDO =null;
        try {
            if(this.location=="MTL"){
                file_MTL=new FileHandler("C:\\Users\\Sushma\\Desktop\\Distributed Systems\\LogFiles\\Log_File_MTL.log");
                SimpleFormatter formatter=new SimpleFormatter();
                file_MTL.setFormatter(formatter);
                loggerMTL.addHandler(file_MTL);
            }
            else if (this.location=="LVL"){
                file_LVL=new FileHandler("C:\\Users\\Sushma\\Desktop\\Distributed Systems\\LogFiles\\Log_File_LVL.log");
                SimpleFormatter formatter=new SimpleFormatter();
                file_LVL.setFormatter(formatter);
                logger.addHandler(file_LVL);
            }
            else if(this.location=="DDO"){
                file_DDO=new FileHandler("C:\\Users\\Sushma\\Desktop\\Distributed Systems\\LogFiles\\Log_File_DDO.log");
                SimpleFormatter formatter=new SimpleFormatter();
                file_DDO.setFormatter(formatter);
                logger.addHandler(file_DDO);
            }

        }

        catch (SecurityException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
        logger.setUseParentHandlers(false);

    }
    
    //Create Teacher Record
    public boolean createTRecord(String firstName, String lastName, String address, String phone, String specialization,
                                 String location) {

        //Initializing Teacher object data members
        Teacher teacherRecord =new Teacher();
        teacherRecord.setFiisrtname(firstName);
        teacherRecord.setLastname(lastName);
        teacherRecord.setAddress(address);
        teacherRecord.setPhoneNumber(phone);
        teacherRecord.setLocation(location);
        teacherRecord.setSpecialization(specialization);
        boolean status =false;
        FileHandler file =null;

        //Search if hashMap already contatins a vlaue with key as 1st character of last name ,if so add it to the exisiting list
        if(hashmapRecords.containsKey(lastName.charAt(0))){
            ArrayList returned=hashmapRecords.get(Character.toString(lastName.charAt(0)));
            returned.add(teacherRecord);
            hashmapRecords.put(Character.toString(lastName.charAt(0)), returned);
            status=true;
        }

        //Add the object to the array list and push it to hash map with key as 1st character of Last Name
        else{
            record.add(teacherRecord);
            hashmapRecords.put(Character.toString(lastName.charAt(0)), record);
            status=true;
        }

        if(status){
            loggerMTL.info("teacher record created from" + this.location);
        }
        else{
            loggerMTL.info("Teacher record not created " + this.location);
        }

        return status;
    }


    //Create Student Record
    public boolean createSReacord(String firstName, String lastName, ArrayList<String> courseRegistered, boolean status,
                                  Date statusDate)  {

        //Initialzing student object data memeber
        Student studentRecord =new Student();
        studentRecord.setFirstName(firstName);
        studentRecord.setLastName(lastName);
        studentRecord.setCourseRegistered(courseRegistered);
        studentRecord.setStatus(status);
        studentRecord.setStautsDate(statusDate);
        boolean added =false;

        //Search if hashMap already contatins a vlaue with key as 1st character of last name ,if so add it to the exisiting list
        if(hashmapRecords.containsKey(lastName.charAt(0))){
            synchronized(this){
                ArrayList returned=hashmapRecords.get(Character.toString(lastName.charAt(0)));
                returned.add(studentRecord);
                hashmapRecords.put(Character.toString(lastName.charAt(0)), returned);
            }
            logger.info("Created");
            added=true;

        }

        //Add the object to the array list and push it to hash map with key as 1st character of Last Name
        else{
            record.add(studentRecord);
            hashmapRecords.put(Character.toString(lastName.charAt(0)), record);
            added=true;
        }

        return added;
    }

    //Get the record count with respect to particular location , used for getting count from across the servers
    public int recordcount(int port){
        int numberOfRecords = 0;
        if(port==6000){
            ArrayList value = null;
            for (Map.Entry<String, ArrayList> entry : hashmapRecords.entrySet()) {
                value = entry.getValue();
            }

            if(value==null){
                numberOfRecords=0;
            }
            else{
                numberOfRecords=value.size();
            }

        }

        else if(port==6001){
            ArrayList value = null;
            for (Map.Entry<String, ArrayList> entry : hashmapRecords.entrySet()) {
                value = entry.getValue();
            }

            if(value==null){
                numberOfRecords=0;
            }
            else{
                numberOfRecords=value.size();
            }
        }

        else
        {
            ArrayList value = null;
            for (Map.Entry<String, ArrayList> entry : hashmapRecords.entrySet()) {
                value = entry.getValue();
            }

            if(value==null){
                numberOfRecords=0;
            }
            else{
                numberOfRecords=value.size();
            }
        }
        return numberOfRecords;
    }
    
    //Gets the record count of current server and calls UDP to the other two servers to get the count
    public String getRecordCounts()
    {
        int numberOfRecords = 0;
        String result =null;
        ArrayList value = null;
        for (Map.Entry<String, ArrayList> entry : hashmapRecords.entrySet()) {
            value = entry.getValue();
        }

        if(value==null){
            numberOfRecords=0;
        }
        else{
            numberOfRecords=value.size();
        }

        if(this.location=="MTL"){
            result = "MTL " + Integer.toString(numberOfRecords) + " LVL "+ConnectUDP(6001)+" DDO "+ConnectUDP(6002);
        }
        else if(this.location=="LVL"){
            result="MTL " + ConnectUDP(6000) + " LVL " + Integer.toString(numberOfRecords) +" DDO " +ConnectUDP(6002);
        }
        else{
            result="MTL " + ConnectUDP(6000) + " LVL " + ConnectUDP(6001) +" DDO " +Integer.toString(numberOfRecords);
        }
        return result;
    }

    //Edits the given Feild if exists and validates the feild name and the new value before editing
    public  boolean editRecord(String recordID, String fieldName, String newValue)
    {
        String editedrecord=null;
        boolean editStatus=false;
        for (Map.Entry<String, ArrayList> entry : hashmapRecords.entrySet()) {
            synchronized(this){
                ArrayList value = entry.getValue();
                for(int i=0;i<value.size();i++){
                    if(value.get(i).getClass()==Record.Student.class){
                        Student student = (Student) value.get(i);
                        if(student.StudentID.equals(recordID)){
                            if(fieldName.equalsIgnoreCase("courseRegistered")){

                                ArrayList<String> myList = new ArrayList<String>(Arrays.asList(newValue.split(",")));
                                student.setCourseRegistered(myList);
                                editStatus=true;


                            }

                            else if(fieldName.equalsIgnoreCase("Status")){
                                if(newValue.equalsIgnoreCase("Active")){
                                    student.setStatus(true);
                                    editStatus=true;
                                }
                                else if(newValue.equalsIgnoreCase("Inactive")){
                                    student.setStatus(false);
                                    editStatus=true;
                                }
                                else{
                                    System.out.println("Status should be Active or Inactive");
                                    editStatus=false;
                                }

                            }
                            else if(fieldName.equalsIgnoreCase("statusDate")){
                                editStatus=true;
                                DateFormat parser = new SimpleDateFormat("yyyy-mm-dd");
                                try {
                                    Date date =(Date) parser.parse(newValue);
                                    student.setStautsDate(date);
                                }
                                catch (Exception e) {

                                    editStatus=false;
                                    e.getMessage();

                                }

                            }
                            else{
                                System.out.println("Opps !!, Cannot edit any other feilds expect for courseRegistered , Status, Status Date");
                                editStatus=false;
                            }

                        }
                        else{
                            //System.out.println("Invalid Student Id");
                            //editStatus=false;
                        }

                    }

                    else if(value.get(i).getClass()==Record.Teacher.class){
                        Teacher teacher = (Teacher)value.get(i);
                        if(teacher.TeacherID.equals(recordID)){
                            if(fieldName.equalsIgnoreCase("Location")){
                                if(newValue.equalsIgnoreCase("MTL")|| newValue.equalsIgnoreCase("LVL")|| newValue.equalsIgnoreCase("DDO")){

                                    teacher.setLocation(newValue);
                                    editStatus =true;

                                    //System.out.println("Location Edited");      				

                                }
                                else{
                                    System.out.println("Opps !! , you entered invlaid location. Location must be MTL ,LVL or DDO");
                                    editStatus=false;
                                }

                            }
                            else if(fieldName.equalsIgnoreCase("Address")){

                                teacher.setAddress(newValue);
                                editStatus =true;

                                //System.out.println("Address edited");

                            }
                            else if(fieldName.equalsIgnoreCase("PhoneNumber")){
                                if(newValue.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")){

                                    teacher.setPhoneNumber(newValue);
                                    editStatus =true;

                                }
                                else{
                                    System.out.println("Oops!! ,Invalid Phone Number. Please enter the number in the format YYY-YYY-YYYY");
                                    editStatus=false;
                                }
                                //System.out.println("Phone Number edited");

                            }
                            else{
                                System.out.println("Oops !! ,Cannot any other feilds except Location , Address and Phone Number");
                                editStatus=false;
                                //editStatus =false;
                            }
                        }

                        else{
                            System.out.println("Invalid Teacher ID");
                        }
                    }

                    else{
                        System.out.println("Invalid Object");
                    }
                }
            }
        }

        return editStatus;

    }

    
    //Print the hashMap
    @Override
    public void printData() throws RemoteException {
        // TODO Auto-generated method stub

        for (Map.Entry<String, ArrayList> entry : hashmapRecords.entrySet()) {
            String key = entry.getKey().toString();
            //System.out.println("Key : "+ key);
            ArrayList value = entry.getValue();
            for(int i=0;i<value.size();i++){
                if(value.get(i).getClass()==Record.Student.class){
                    Student stuvalues = (Student) value.get(i);
                    System.out.println("First Name is : "+ stuvalues.getFirstName());
                    System.out.println("Last Name is : "+ stuvalues.getLastName());
                    System.out.println("Courses Registered : "+ stuvalues.getCourseRegistered());
                    System.out.println("Status is "+ stuvalues.getStatus());
                    System.out.println("Status date is : "+ stuvalues.getStautsDate());
                    System.out.println("Student Id is : "+ stuvalues.StudentID);
                }
                else if(value.get(i).getClass()==Record.Teacher.class){
                    Teacher teachvalues = (Teacher) value.get(i);
                    System.out.println("First Name is : "+ teachvalues.getFiisrtname());
                    System.out.println("Last Name is : "+ teachvalues.getLastname());
                    System.out.println("Location : "+ teachvalues.getLocation());
                    System.out.println("Specialization is : "+ teachvalues.getSpecialization());
                    System.out.println("Phone Number is : "+ teachvalues.getPhoneNumber());
                    System.out.println("Address is  : " + teachvalues.getAddress());
                }
                else{
                    System.out.println("Invalid obj");
                }

            }

        }
    }


    //Connects to UDP to send and receive the data from Server (Acts as UDP Client)
    public String ConnectUDP(int portNumber)
    {

        System.out.println("Connected at Port" + portNumber);
        String recieved=null;
        DatagramSocket datagramSocket;
        try {
            datagramSocket = new DatagramSocket();

            byte[] bufferReceive =new byte[50];
            //String recieved ;
            InetAddress address =InetAddress.getLocalHost();
            byte[] bufferSend =new byte[50];

            DatagramPacket sendRequestpacket = new DatagramPacket(bufferSend,bufferSend.length,address,portNumber);
            datagramSocket.send(sendRequestpacket);
            System.out.println("Prot number sent to UDP coonect"+portNumber);
            DatagramPacket receiveReplyPacket = new DatagramPacket(bufferReceive,bufferReceive.length) ;
            datagramSocket.receive(receiveReplyPacket);

            recieved =new String(bufferReceive,0,bufferReceive.length);
            datagramSocket.close();

        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return recieved;
    }

    //Run Method acts as UDP server , receives the request from UDP Connect and sends the count back to the UDPConnect via the same port it received a request from
    @Override
    public void run() {
        DatagramSocket datagramSocket=null;
        System.out.println("UDP Server with port number" + this.UDPPort+ "and Located at "+ this.location);

        try{

            datagramSocket =new DatagramSocket(this.UDPPort);
            byte[] bufferReceive = new byte[50];
            byte[] bufferSend = new byte[50];
            while(true){


                DatagramPacket receivedPacket =new DatagramPacket(bufferReceive,bufferReceive.length);
                datagramSocket.receive(receivedPacket);
                System.out.println("Port number at server run , fetched from UDP requuest ") ;
                System.out.println((receivedPacket.getPort()));
                bufferSend = Integer.toString(recordcount(receivedPacket.getPort())).getBytes();

                DatagramPacket sendPackets=new DatagramPacket(bufferSend,bufferSend.length,receivedPacket.getAddress(),receivedPacket.getPort());
                datagramSocket.send(sendPackets);

            }
        }
        catch(SocketException ex){
            System.out.println("Socket "+ex.getMessage());
        }
        catch(IOException e){

            System.out.println("IO :"+e.getMessage());

        }


    }

    //Main
    public static  void main(String[] args) throws RemoteException,AlreadyBoundException{
        try {
            //Create 3 objects of server , one for each location Montreal ,Laval and DDO
            CenterServer montrealObject = new CenterServer(2999,6000,"MTL");
            CenterServer lavalObject = new CenterServer(2998,6001,"LVL");
            CenterServer ddoObject = new CenterServer(2997,6002,"DDO");
            //Create a registry 
            Registry registry= LocateRegistry.createRegistry(2999);

            //Bind the registry to the unique objects of server  and name the registry
            registry.bind("Montreal", montrealObject);
            registry.bind("Laval", lavalObject);
            registry.bind("DDO", ddoObject);

            //Create 3 threads , one for each server to have the UDP connection open for recieving request as soon as the server starts.
            Thread montrealThread=new Thread(montrealObject);
            System.out.println("Montreal Thread"+montrealThread.getId());

            Thread lavalThread=new Thread(lavalObject);
            System.out.println("Laval Thread"+lavalThread.getId());

            Thread ddoThread=new Thread(ddoObject);
            System.out.println("DDO Thread"+ddoThread.getId());

            //Start all the servers thread to have the UDP connection open all the time , meaning available to recieve the request all the time
            montrealThread.start();
            lavalThread.start();
            ddoThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

