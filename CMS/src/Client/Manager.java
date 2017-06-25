package Client;


import RMIInterface.ClassManagementSystemInterface;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Created by Sushma on 04-06-2017.
 */

//Client Class
public class Manager {
	//Main function , invokes the server methods by looking up the regisrty 
    public static void main(String[] args) throws Exception {

        String MangerID=null;
        Scanner scanner = new Scanner(System.in);
        int option;
        String FirstName;
        String LastName;
        String Location = null;
        ArrayList<String> course =null;
        Date date = null;
        String Address;
        String PhoneNumber = null;
        String Specialiazation;
        String Status = null;
        int courseCount;
        String recordId;
        String feildName = null;
        String newValue = null;

        //For Logging
        Logger logger;
        FileHandler file = null;
        SimpleFormatter formatter = new SimpleFormatter();
        logger = Logger.getLogger(ClassManagementSystemInterface.class.getName());


        //Validatiding Manager ID
        boolean validateManagerId=false;
        String managerId=null;
        while(!validateManagerId){
            System.out.println("Enter your ManagerID");
            managerId=scanner.next();
            if(managerId.trim().startsWith("MTL")||managerId.trim().startsWith("LVL")||managerId.trim().startsWith("DDO")){
                MangerID=managerId;
                validateManagerId=true;
                break;
            }
            else{
                System.out.println("Inavlid Manager ID!!Manager Id should start with MTL or LVL or DDO");

            }
        }

        //Creating Registry with Port Number 2999
        Registry registry;
        registry = LocateRegistry.getRegistry(2999);


        //Montreal Manager  
        if (MangerID.startsWith("MTL")) {

            //Creating Log File for Montreal Manager
            file = new FileHandler("C:\\Users\\Sushma\\Desktop\\Distributed Systems\\LogFiles\\Manager_MTL.log");
            logger.addHandler(file);
            logger.setUseParentHandlers(false);
            file.setFormatter(formatter);

            options();
            boolean quit = false;
            while (!quit) {

                System.out.println("Enter Your Choice");
                option = scanner.nextInt();
                scanner.nextLine();
                switch (option) {

                    case 0:
                        options();
                        break;
                    //Create Student Record
                    case 1:

                        System.out.println("Enter the First Name of the Student");
                        FirstName = scanner.next();
                        scanner.nextLine();

                        System.out.println("Enter the Last Name of the Student");
                        LastName = scanner.next();
                        scanner.nextLine();

                        System.out.println("Enter the  Number Of courses");
                        courseCount = scanner.nextInt();
                        scanner.nextLine();

                        course = new ArrayList<String>();
                        System.out.println("Enter " + courseCount + " courses");
                        for (int i = 0; i < courseCount; i++) {
                            course.add(scanner.next());

                        }

                        //Setting Status of Student 
                        boolean statusValidate =false;
                        boolean status =false;
                        while(!statusValidate){
                            System.out.println("Enter the  Status of the Student");
                            Status = scanner.next();

                            if(Status.equalsIgnoreCase("Active")){
                                status=true;
                                statusValidate=true;
                                break;
                            }
                            else if(Status.equalsIgnoreCase("InActive")){
                                status=false;
                                statusValidate=true;
                                break;
                            }
                            else{
                                System.out.println("Status should be Active or Inactive");
                            }
                        }

                        //Setting Status Date of the Student
                        if (Status.equalsIgnoreCase("active")) {
                            System.out.println("Enter the  Date in the format yyyy-mm-dd ,When Student became Active ");
                            DateFormat parser = new SimpleDateFormat("yyyy-mm-dd");
                            try {
                                date =(Date) parser.parse(scanner.next());

                            }
                            catch (ParseException e) {

                                e.printStackTrace();
                            }

                        }
                        else {
                            System.out.println("Enter the  Date in the format yyyy-mm-dd ,When student became Inactive");
                            DateFormat parser = new SimpleDateFormat("yyyy-mm-dd");
                            try {
                                date =(Date) parser.parse(scanner.next());

                            }
                            catch (ParseException e) {

                                e.printStackTrace();
                            }
                        }

                        ClassManagementSystemInterface createstudentrecord = (ClassManagementSystemInterface) registry.lookup("Montreal");
                        boolean create = createstudentrecord.createSReacord(FirstName, LastName, course, status, date);
                        createstudentrecord.printData();

                        if (create) {
                            System.out.println("Student Record Created Successfully");
                            logger.info("Student Record created Succesffully from Manager with ID" + MangerID);

                        } else {
                            System.out.println("Student Record was not created");
                            logger.info("Student Record not created");
                        }

                        break;
                    //Create Teacher record
                    case 2:

                        System.out.println("Enter the First Name of Teacher");
                        FirstName = scanner.next();

                        System.out.println("Enter the Last Name of the Teacher");
                        LastName = scanner.next();

                        System.out.println("Enter the  Address");
                        Address = scanner.next();

                        //Validating Phone Number Format
                        boolean validatePhone=false;
                        while(!validatePhone){
                            System.out.println("Enter the  Phone Number in the format yyy-yyy-yyyy");
                            String phonenumberScanned=scanner.next();
                            if(phonenumberScanned.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")){
                                PhoneNumber = phonenumberScanned;
                                validatePhone=true;
                                break;
                            }
                            else{
                                System.out.println("Phone number  should be in the format yyy-yyy-yyyy");
                            }
                        }

                        System.out.println("Enter the Specialization");
                        Specialiazation = scanner.next();

                        //Validating Location
                        boolean validateLocation=false;
                        while(!validateLocation){
                            System.out.println("Enter the location");
                            String locationScanned = scanner.next();
                            if (locationScanned.equalsIgnoreCase("MTL")||locationScanned.equalsIgnoreCase("LVL")||locationScanned.equalsIgnoreCase("DDO")) {
                                Location=locationScanned;
                                validateLocation=true;
                                break;
                            }
                            else{
                                System.out.println("Location must be MTL ,LVL or DDO");
                            }
                        }

                        ClassManagementSystemInterface createTeacherRecord = (ClassManagementSystemInterface) registry.lookup("Montreal");
                        boolean createdTeacherRecord = createTeacherRecord.createTRecord(FirstName, LastName, Address, PhoneNumber, Specialiazation, Location);
                        createTeacherRecord.printData();

                        if (createdTeacherRecord) {
                            System.out.println("Teacher record created Successfully");
                            logger.info("Teacher Record created Succesffully from Manager with ID" + MangerID);

                        } else {
                            System.out.println("Teacher Record not created");
                            logger.info("Teacher Record not created");
                        }
                        break;
                    //Editing records
                    case 3:
                        System.out.println("Enter the record Id you wish to Edit");
                        recordId = scanner.next();

                        System.out.println("Enter the Feild Name to Edit");
                        feildName= scanner.next();

                        if(!feildName.equalsIgnoreCase("CourseRegistered")){
                            System.out.println("Enter the new value");
                            newValue=scanner.next();
                        }

                        StringBuilder sb = new StringBuilder();
                        if(feildName.equalsIgnoreCase("courseRegistered")){

                            boolean editquit=false;
                            courseEditOption();
                            while(!editquit){
                                System.out.println("Choose an option");
                                int editoption=scanner.nextInt();
                                switch(editoption){
                                    case 0:
                                        courseEditOption();
                                        break;
                                    case 1:

                                        System.out.println("Enter the new Value to add to List");
                                        String newcourseList =scanner.next();
                                        for(String coursesString :course){

                                            sb.append(coursesString);
                                            sb.append(" ");
                                            sb.append(",");
                                        }

                                        sb.append(newcourseList);
                                        course.add(newcourseList);
                                        newValue=sb.toString();
                                        break;
                                    case 2:
                                        System.out.println("Enter the location which you would like to edit");
                                        int position =scanner.nextInt();
                                        System.out.println("Enter the new course");
                                        String newcourse=scanner.next();
                                        String replace=course.set(position, newcourse);
                                        // System.out.println(replace);

                                        for(String coursesString :course){
                                            sb.append(coursesString);
                                            sb.append(" ");
                                            sb.append(",");
                                        }
                                        newValue=sb.toString();
                                        break;
                                    case 3:
                                        editquit=true;
                                        break;
                                }
                            }

                        }

                        ClassManagementSystemInterface editRecord = (ClassManagementSystemInterface) registry.lookup("Montreal");
                        boolean editedRecords = editRecord.editRecord(recordId, feildName, newValue);
                        editRecord.printData();
                        if (editedRecords) {
                            System.out.println("Edited Record Succesffully ");
                            logger.info("Edited Record Succesffully " + MangerID);
                        } else {
                            System.out.println("Could not Edit the Record");
                            logger.info("Could not Edit the Record" + MangerID);
                        }
                        break;
                    //Get Record Count
                    case 4:
                        ClassManagementSystemInterface getRecordCount = (ClassManagementSystemInterface) registry.lookup("Montreal");
                        String result = getRecordCount.getRecordCounts();
                        System.out.println(result);
                        break;

                    case 5:
                        quit = true;
                        break;
                }

            }
        } else if (MangerID.startsWith("LVL")) {

            //Logging File
            file = new FileHandler("C:\\Users\\Sushma\\Desktop\\Distributed Systems\\LogFiles\\Manager_LVL.log");
            logger.addHandler(file);
            logger.setUseParentHandlers(false);
            file.setFormatter(formatter);

            options();
            boolean quit = false;
            while (!quit) {

                System.out.println("Enter Your Choice");
                option = scanner.nextInt();
                scanner.nextLine();
                switch (option) {

                    case 0:
                        options();
                        break;
                    //Create Student Record
                    case 1:

                        System.out.println("Enter the First Name of the Student");
                        FirstName = scanner.next();
                        scanner.nextLine();

                        System.out.println("Enter the Last Name of the Student");
                        LastName = scanner.next();
                        scanner.nextLine();

                        System.out.println("Enter the  Number Of courses");
                        courseCount = scanner.nextInt();
                        scanner.nextLine();
                        course = new ArrayList<String>();
                        System.out.println("Enter " + courseCount + "courses");
                        for (int i = 0; i < courseCount; i++) {
                            course.add(scanner.next());

                        }

                        boolean statusValidate =false;
                        boolean status =false;
                        while(!statusValidate){
                            System.out.println("Enter the  Status of the Student");
                            Status = scanner.next();

                            if(Status.equalsIgnoreCase("Active")){
                                status=true;
                                statusValidate=true;
                                break;
                            }
                            else if(Status.equalsIgnoreCase("InActive")){
                                status=false;
                                statusValidate=true;
                                break;
                            }
                            else{
                                System.out.println("Status should be Active or Inactive");
                            }
                        }


                        if (Status.equalsIgnoreCase("active")) {
                            System.out.println("Enter the  Date When Student became Active ,in the format yyyy-mm-dd ");
                            DateFormat parser = new SimpleDateFormat("yyyy-mm-dd");
                            try {
                                date =(Date) parser.parse(scanner.next());

                            }
                            catch (ParseException e) {

                                e.printStackTrace();
                            }

                        } else {
                            System.out.println("Enter the  Date When student became Inactive ,in the format yyyy-mm-dd ");
                            DateFormat parser = new SimpleDateFormat("yyyy-mm-dd");
                            try {
                                date =(Date) parser.parse(scanner.next());

                            }
                            catch (ParseException e) {

                                e.printStackTrace();
                            }
                        }

                        ClassManagementSystemInterface createstudentrecord = (ClassManagementSystemInterface) registry.lookup("Laval");
                        boolean create = createstudentrecord.createSReacord(FirstName, LastName, course, status, date);
                        createstudentrecord.printData();

                        if (create) {
                            System.out.println("Student Record created Succesffully");

                            logger.info("Student Record created Succesffully from Manager with ID" + MangerID);

                        } else {
                            System.out.println("Student Record not create");
                            logger.info("Student Record not created");
                        }

                        break;
                    case 2:

                        System.out.println("Enter the First Name of Teacher");
                        FirstName = scanner.next();

                        System.out.println("Enter the Last Name of the Teacher");
                        LastName = scanner.next();

                        System.out.println("Enter the  Address");
                        Address = scanner.next();


                        boolean validatePhone=false;
                        while(!validatePhone){
                            System.out.println("Enter the  Phone Number in the format yyy-yyy-yyyy");
                            String phonenumberScanned=scanner.next();
                            if(phonenumberScanned.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")){
                                PhoneNumber = phonenumberScanned;
                                validatePhone=true;
                                break;
                            }
                            else{
                                System.out.println("Phone number  should be in the format yyy-yyy-yyyy");
                            }
                        }

                        System.out.println("Enter the Specialization");
                        Specialiazation = scanner.next();

                        boolean validateLocation=false;
                        while(!validateLocation){
                            System.out.println("Enter the location");
                            String locationScanned = scanner.next();
                            if (locationScanned.equalsIgnoreCase("MTL")||locationScanned.equalsIgnoreCase("LVL")||locationScanned.equalsIgnoreCase("DDO")) {
                                Location=locationScanned;
                                validateLocation=true;
                                break;
                            }
                            else{
                                System.out.println("Location must be MTL ,LVL or DDO");
                            }
                        }

                        ClassManagementSystemInterface createTeacherRecord = (ClassManagementSystemInterface) registry.lookup("Laval");
                        boolean createdTeacherRecord = createTeacherRecord.createTRecord(FirstName, LastName, Address, PhoneNumber, Specialiazation, Location);
                        createTeacherRecord.printData();
                        if (createdTeacherRecord) {
                            System.out.println("Teacher Record created Succesffully");
                            logger.info("Teacher Record created Succesffully from Manager with ID" + MangerID);

                        } else {
                            System.out.println("Teacher Record not created");
                            logger.info("Teacher Record not created");
                        }
                        break;
                    case 3:
                        System.out.println("Enter the record Id you wish to Edit");
                        recordId = scanner.next();

                        System.out.println("Enter the Feild Name to Edit");
                        feildName= scanner.next();

                        if(!feildName.equalsIgnoreCase("CourseRegistered")){
                            System.out.println("Enter the new value");
                            newValue=scanner.next();
                        }

                        StringBuilder sb = new StringBuilder();
                        if(feildName.equalsIgnoreCase("courseRegistered")){

                            boolean editquit=false;
                            courseEditOption();
                            while(!editquit){
                                System.out.println("Choose an option");
                                int editoption=scanner.nextInt();
                                switch(editoption){
                                    case 0:
                                        courseEditOption();
                                        break;
                                    case 1:

                                        System.out.println("Enter the new Value to add to List");
                                        String newcourseList =scanner.next();
                                        for(String coursesString :course){

                                            sb.append(coursesString);
                                            sb.append(" ");
                                            sb.append(",");
                                        }

                                        sb.append(newcourseList);
                                        course.add(newcourseList);
                                        newValue=sb.toString();
                                        break;
                                    case 2:
                                        System.out.println("Enter the location which you would like to edit");
                                        int position =scanner.nextInt();
                                        System.out.println("Enter the new course");
                                        String newcourse=scanner.next();
                                        String replace=course.set(position, newcourse);
                                        // System.out.println(replace);

                                        for(String coursesString :course){
                                            sb.append(coursesString);
                                            sb.append(" ");
                                            sb.append(",");
                                        }
                                        newValue=sb.toString();
                                        break;
                                    case 3:
                                        editquit=true;
                                        break;
                                }
                            }

                        }

                        ClassManagementSystemInterface editRecord = (ClassManagementSystemInterface) registry.lookup("Laval");
                        boolean editedRecords = editRecord.editRecord(recordId, feildName, newValue);
                        editRecord.printData();
                        if (editedRecords) {

                            System.out.println("Edited Record Succesffully ");
                            logger.info("Edited Record Succesffully " + MangerID);
                        } else {
                            System.out.println("Could not Edit the Record");
                            logger.info("Could not Edit the Record" + MangerID);
                        }
                        break;
                    case 4:
                        ClassManagementSystemInterface getRecordCount = (ClassManagementSystemInterface) registry.lookup("Laval");
                        String result = getRecordCount.getRecordCounts();
                        System.out.println(result);

                        break;

                    case 5:
                        quit = true;
                        break;
                }
            }

        }

        else if(MangerID.startsWith("DDO")){

            file = new FileHandler("C:\\Users\\Sushma\\Desktop\\Distributed Systems\\LogFiles\\Manager_DDO.log");
            logger.addHandler(file);
            logger.setUseParentHandlers(false);
            file.setFormatter(formatter);

            options();
            boolean quit = false;
            while (!quit) {

                System.out.println("Enter Your Choice");
                option = scanner.nextInt();
                scanner.nextLine();
                switch (option) {

                    case 0:
                        options();
                        break;
                    case 1:

                        System.out.println("Enter the First Name of the Student");
                        FirstName = scanner.next();
                        scanner.nextLine();

                        System.out.println("Enter the Last Name of the Student");
                        LastName = scanner.next();
                        scanner.nextLine();

                        System.out.println("Enter the  Number Of courses");
                        courseCount = scanner.nextInt();
                        scanner.nextLine();
                        course = new ArrayList<String>();
                        System.out.println("Enter " + courseCount + "courses");
                        for (int i = 0; i < courseCount; i++) {
                            course.add(scanner.next());

                        }

                        System.out.println("Enter the  Status of the Student");
                        Status = scanner.next();

                        boolean status =false;
                        if(Status.equalsIgnoreCase("Active")){
                            status=true;
                        }
                        else if(Status.equalsIgnoreCase("InActive")){
                            status=false;
                        }
                        else{
                            System.out.println("Status should be Active or Inactive");
                        }

                        if (Status.equalsIgnoreCase("active")) {
                            System.out.println("Enter the  Date When Student became Active,in the format yyyy-mm-dd ");
                            DateFormat parser = new SimpleDateFormat("yyyy-mm-dd");
                            try {
                                date =(Date) parser.parse(scanner.next());

                            }
                            catch (ParseException e) {

                                System.out.println(e.getMessage());
                            }
                        } else {
                            System.out.println("Enter the  Date When student became Inactive,in the format yyyy-mm-dd ");
                            DateFormat parser = new SimpleDateFormat("yyyy-mm-dd");
                            try {
                                date =(Date) parser.parse(scanner.next());

                            }
                            catch (ParseException e) {

                                e.printStackTrace();
                            }
                        }

                        ClassManagementSystemInterface createstudentrecord = (ClassManagementSystemInterface) registry.lookup("DDO");
                        boolean create = createstudentrecord.createSReacord(FirstName, LastName, course, status, date);
                        createstudentrecord.printData();
                        //System.out.println(create);

                        if (create) {
                            System.out.println("Student Record created Succesffully");
                            logger.info("Student Record created Succesffully from Manager with ID" + MangerID);

                        } else {
                            System.out.println("Student Record not created");
                            logger.info("Student Record not created");
                        }

                        break;
                    case 2:

                        System.out.println("Enter the First Name of Teacher");
                        FirstName = scanner.next();

                        System.out.println("Enter the Last Name of the Teacher");
                        LastName = scanner.next();

                        System.out.println("Enter the  Address");
                        Address = scanner.next();

                        boolean validatePhone=false;
                        while(!validatePhone){
                            System.out.println("Enter the  Phone Number in the format yyy-yyy-yyyy");
                            String phonenumberScanned=scanner.next();
                            if(phonenumberScanned.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")){
                                PhoneNumber = phonenumberScanned;
                                validatePhone=true;
                                break;
                            }
                            else{
                                System.out.println("Phone number  should be in the format yyy-yyy-yyyy");
                            }
                        }

                        System.out.println("Enter the Specialization");
                        Specialiazation = scanner.next();

                        boolean validateLocation=false;
                        while(!validateLocation){
                            System.out.println("Enter the location");
                            String locationScanned = scanner.next();
                            if (locationScanned.equalsIgnoreCase("MTL")||locationScanned.equalsIgnoreCase("LVL")||locationScanned.equalsIgnoreCase("DDO")) {
                                Location=locationScanned;
                                validateLocation=true;
                                break;
                            }
                            else{
                                System.out.println("Location must be MTL ,LVL or DDO");
                            }
                        }

                        ClassManagementSystemInterface createTeacherRecord = (ClassManagementSystemInterface) registry.lookup("DDO");
                        boolean createdTeacherRecord = createTeacherRecord.createTRecord(FirstName, LastName, Address, PhoneNumber, Specialiazation, Location);
                        createTeacherRecord.printData();
                        if (createdTeacherRecord) {
                            System.out.println("Teacher Record created Succesffully");
                            logger.info("Teacher Record created Succesffully from Manager with ID" + MangerID);

                        } else {
                            System.out.println("Teacher Record not created");
                            logger.info("Teacher Record not created");
                        }
                        break;
                    case 3:
                        System.out.println("Enter the record Id you wish to Edit");
                        recordId = scanner.next();

                        System.out.println("Enter the Feild Name to Edit");
                        feildName= scanner.next();

                        if(!feildName.equalsIgnoreCase("CourseRegistered")){
                            System.out.println("Enter the new value");
                            newValue=scanner.next();
                        }

                        StringBuilder sb = new StringBuilder();
                        if(feildName.equalsIgnoreCase("courseRegistered")){

                            boolean editquit=false;
                            courseEditOption();
                            while(!editquit){
                                System.out.println("Choose an option");
                                int editoption=scanner.nextInt();
                                switch(editoption){
                                    case 0:
                                        courseEditOption();
                                        break;
                                    case 1:

                                        System.out.println("Enter the new Value to add to List");
                                        String newcourseList =scanner.next();
                                        for(String coursesString :course){

                                            sb.append(coursesString);
                                            sb.append(" ");
                                            sb.append(",");
                                        }

                                        sb.append(newcourseList);
                                        course.add(newcourseList);
                                        newValue=sb.toString();
                                        break;
                                    case 2:
                                        System.out.println("Enter the location which you would like to edit");
                                        int position =scanner.nextInt();
                                        System.out.println("Enter the new course");
                                        String newcourse=scanner.next();
                                        String replace=course.set(position, newcourse);
                                        // System.out.println(replace);

                                        for(String coursesString :course){
                                            sb.append(coursesString);
                                            sb.append(" ");
                                            sb.append(",");
                                        }
                                        newValue=sb.toString();
                                        break;
                                    case 3:
                                        editquit=true;
                                        break;
                                }
                            }

                        }

                        ClassManagementSystemInterface editRecord = (ClassManagementSystemInterface) registry.lookup("DDO");
                        boolean editedRecords = editRecord.editRecord(recordId, feildName, newValue);
                        editRecord.printData();
                        if (editedRecords) {
                            System.out.println("Edited Record Succesffully ");
                            logger.info("Edited Record Succesffully " + MangerID);
                        } else {
                            System.out.println("Could not Edit the Record" );
                            logger.info("Could not Edit the Record" + MangerID);
                        }
                        break;
                    case 4:
                        ClassManagementSystemInterface getRecordCount = (ClassManagementSystemInterface) registry.lookup("DDO");
                        String result = getRecordCount.getRecordCounts();
                        System.out.println(result);
                        break;

                    case 5:
                        quit = true;
                        break;
                }
            }
        }
    }

    //To print the operation to be performed
    public static void options(){
        System.out.println("Choose from the  following options ");
        System.out.println("0 Print the options again");
        System.out.println("1 Create Student Records");
        System.out.println("2 Create Teacher Records");
        System.out.println("3 Edit the Records");
        System.out.println("4 Get the Record Count");
        System.out.println("5 Exit");
    }

    //Options for editing course regsitered
    public static void courseEditOption(){
        System.out.println("Choose 0 to repeat the option List");
        System.out.println("Choose 1 to add new Course to the List");
        System.out.println("Choose 2 to edit the exisiting List");
        System.out.println("Choose 3 to quit editing");
    }
}
