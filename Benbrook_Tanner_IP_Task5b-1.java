package org.example;
import java.io.*;
import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.logging.Logger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.math.BigDecimal;

public class Benbrook_Tanner_IP_Task5b {
    private static final Logger log; // Logger for the application

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%4$-7s] %5$s %n"); // Set log format
        log = Logger.getLogger(Benbrook_Tanner_IP_Task5b.class.getName()); // Initialize the logger
    }

    public static void main(String[] args) throws Exception {

        Properties properties = new Properties(); // Load database properties
        properties.load(Benbrook_Tanner_IP_Task5b.class.getClassLoader().getResourceAsStream("application.properties")); // Load properties file

        Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties); // Establish connection

        Scanner inputScanner = new Scanner(System.in); // Initialize scanner for user input
        int query = 0; // Initialize query input variable
        System.out.print("WELCOME TO PAN, YOUR OWN PERSONALIZED DATABASE SYSTEM!\n");

        while (query != 18) { // Loop until user selects option 18 (quit)
            System.out.println("""
                    Choose an option:\s
                    1: Insert a Team into the database\s
                    2: Insert a Client into the database\s
                    3: Insert a Volunteer into the database\s
                    4: Insert number of hours volunteer worked this month\s
                    5: Insert an Employee into the database\s
                    6: Insert an Expense associated with an Employee\s
                    7: Insert a Donor into the database\s
                    8: Retrieve Doctor information\s
                    9: Retrieve Employee charges for a particular time period\s
                    10: Retrieve Volunteers that are members of teams that support a particular client\s
                    11: Retrieve names of teams founded after particular dates\s
                    12: Retrieve information of all people in database\s
                    13: Retrieve name and amount donated by donors that are also employees\s
                    14: Increase salary of employees with more than one team reporting to them\s
                    15: Delete Clients who don't have health insurance and value of importance for transportation is less than 5\s
                    16: Import a new team from data file\s
                    17: Export names and mailing address of people on mailing list\s
                    18: Quit\s"""); // Prompt user
            query = inputScanner.nextInt(); // Get user input

            switch (query) { // Handle different query options
                case 1:
                    executeInsertTeamProcedure(connection, inputScanner);
                    break;
                case 2:
                    executeInsertClientProcedure(connection, inputScanner);
                    break;
                case 3:
                    executeInsertVolunteerProcedure(connection, inputScanner);
                    break;
                case 4:
                    executeInsertVolunteerHoursProcedure(connection, inputScanner);
                    break;
                case 5:
                    executeInsertEmployeeProcedure(connection, inputScanner);
                    break;
                case 6:
                    executeInsertExpenseProcedure(connection, inputScanner);
                    break;
                case 7:
                    executeInsertDonorProcedure(connection, inputScanner);
                    break;
                case 8:
                    executeRetrieveDoctorInfoProcedure(connection, inputScanner);
                    break;
                case 9:
                    executeRetrieveTotalExpensesProcedure(connection, inputScanner);
                    break;
                case 10:
                    executeGetVolunteersForClientProcedure(connection, inputScanner);
                    break;
                case 11:
                    executeGetTeamsFoundedAfterDateProcedure(connection, inputScanner);
                    break;
                case 12:
                    executeGetPeopleInfoProcedure(connection, inputScanner);
                    break;
                case 13:
                    executeGetDonorsThatAreEmployeesProcedure(connection, inputScanner);
                    break;
                case 14:
                    executeIncreaseSalaryForEmployeesProcedure(connection, inputScanner);
                    break;
                case 15:
                    executeDeleteClientsProcedure(connection);
                    break;
                case 16:
                    executeImportTeamProcedure(connection, inputScanner);
                    break;
                case 17:
                    executeExportMailingList(connection, inputScanner);
                    break;
                case 18:
                    System.out.println("You have exited PAN. Have a great day!"); // Exit program
                    break;
                default:
                    System.out.println("Invalid option. Please select a valid option (1-18)."); // Handle invalid input
            }
        }

        connection.close(); // Close database connection
    }

    // Execute Query 1 - Insert Team
    private static void executeInsertTeamProcedure(Connection connection, Scanner keyboard) throws SQLException {
        System.out.println("Enter the Team Name: "); // Prompt for team name
        String teamName = keyboard.next(); // Get team name

        System.out.println("Enter the Team Type: "); // Prompt for team type
        String teamType = keyboard.next(); // Get team type

        System.out.println("Enter the date formed (yyyy-MM-dd): "); // Prompt for team formed date
        String dateFormed = keyboard.next();

        // handle date format
        if (!isValidDate(dateFormed)) {
            System.err.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
            return;
        }
        String sql = "{call insertTeam(?, ?, ?)}"; // Prepare the SQL call for the procedure
        CallableStatement stmt = connection.prepareCall(sql); // Create CallableStatement

        stmt.setString(1, teamName); // Set team name parameter
        stmt.setString(2, teamType); // Set team type parameter
        stmt.setString(3, dateFormed); // Set date formed parameter

        stmt.execute(); // Execute the stored procedure
        System.out.println("Team inserted successfully."); // Confirm success
        stmt.close(); // Close the statement
    }

    // Execute Query 2 - Insert Client
    private static void executeInsertClientProcedure(Connection connection, Scanner keyboard) throws SQLException {
        // Prompt for client SSN
        System.out.println("Enter the client ssn:");
        int cSSN = keyboard.nextInt();
        keyboard.nextLine(); // need nextLine when reading Int

        // Prompt for client name
        System.out.println("Enter the client name: ");
        String cName = keyboard.nextLine();

        // Prompt for client gender
        System.out.println("Enter the client gender:");
        String cGender = keyboard.nextLine();

        // Prompt for client profession
        System.out.println("Enter the client profession:");
        String cProfession = keyboard.nextLine();

        // Prompt for client mailing list status
        System.out.println("Is Client on the mailing list? (1 for yes, 0 for no):");
        int clientMailingListStatus = keyboard.nextInt();
        keyboard.nextLine();

        // Prompt for client mailing address
        System.out.println("Enter the client mailing address:");
        String clientMailingAddress = keyboard.nextLine();

        // Prompt for client phone number
        System.out.println("Enter the client phone number: ");
        String cPhoneNumber = keyboard.nextLine();

        // Prompt for client email
        System.out.println("Enter the client email: ");
        String cEmail = keyboard.nextLine();

        // Prompt for client assignment date
        System.out.println("Enter the client assignment date (yyyy-MM-dd): ");
        String cAssignmentDate = keyboard.nextLine();
        if (!isValidDate(cAssignmentDate)) {
            System.err.println("Invalid date format for assignment date. Please enter the date in yyyy-MM-dd format.");
            return;
        }

        // Prompt for client doctor name
        System.out.println("Enter the doctor name of client:");
        String doctorName = keyboard.nextLine();

        // Prompt for client doctor phone number
        System.out.println("Enter the client doctor's phone number:");
        String doctorPhoneNumber = keyboard.nextLine();

        // Collect multiple team names
        List<String> teamNames = new ArrayList<>();
        System.out.println("How many teams care for the client? ");
        int teams = keyboard.nextInt();
        keyboard.nextLine();
        for (int i = 0; i < teams; i++) {
            System.out.println("Enter the team " + (i + 1) + " name:");
            teamNames.add(keyboard.nextLine());
        }

        // Prompt for client need type
        System.out.println("Enter the client's need type: ");
        String cNeedType = keyboard.nextLine();

        // Prompt for client need importance
        System.out.println("Enter the client's need importance (1-10): ");
        int cNeedImportance = keyboard.nextInt();
        keyboard.nextLine();

        // Prompt for client insurance policy id
        System.out.println("Enter the client's insurance policy id: ");
        String cInsurancePolicyId = keyboard.nextLine();

        // Prompt for client insurance provider
        System.out.println("Enter the client's insurance provider: ");
        String cInsuranceProvider = keyboard.nextLine();

        // Prompt for client insurance provider address
        System.out.println("Enter the client's insurance provider's address: ");
        String cInsuranceProviderAddress = keyboard.nextLine();

        // Prompt for client insurance type
        System.out.println("Enter client's insurance type: ");
        String cInsuranceType = keyboard.nextLine();

        // Execute main client insertion procedure
        String sql = "{call insertClient(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        CallableStatement callableStatement = connection.prepareCall(sql);
        callableStatement.setInt(1, cSSN);
        callableStatement.setString(2, cName);
        callableStatement.setString(3, cGender);
        callableStatement.setString(4, cProfession);
        callableStatement.setInt(5, clientMailingListStatus);
        callableStatement.setString(6, clientMailingAddress);
        callableStatement.setString(7, cEmail);
        callableStatement.setString(8, cPhoneNumber);
        callableStatement.setString(9, cAssignmentDate);
        callableStatement.setString(10, doctorName);
        callableStatement.setString(11, doctorPhoneNumber);
        callableStatement.setString(12, cNeedType);
        callableStatement.setInt(13, cNeedImportance);
        callableStatement.setString(14, cInsurancePolicyId);
        callableStatement.setString(15, cInsuranceProvider);
        callableStatement.setString(16, cInsuranceProviderAddress);
        callableStatement.setString(17, cInsuranceType);

        callableStatement.execute();
        System.out.println("Client has been inserted successfully.");
        callableStatement.close();

        // Insert emergency contacts
        String emergencyContactSql = "INSERT INTO ClientEmergencyContacts (client_ssn, contact_name, " +
                "contact_phone_number, relationship) VALUES (?, ?, ?, ?)";
        System.out.println("How many emergency contacts does the client have?");
        int emergencyContacts = keyboard.nextInt();
        keyboard.nextLine();
        for (int i = 0; i < emergencyContacts; i++) {
            System.out.println("Please enter the name of emergency contact " + (i + 1) + ":");
            String ecName = keyboard.nextLine();
            System.out.println("Please enter the phone number of emergency contact " + (i + 1) + ":");
            String ecPhoneNumber = keyboard.nextLine();
            System.out.println("Please enter the relationship of emergency contact " + (i + 1) + ":");
            String ecRelationship = keyboard.nextLine();
            try (CallableStatement ecStmt = connection.prepareCall(emergencyContactSql)) {
                ecStmt.setInt(1, cSSN);
                ecStmt.setString(2, ecName);
                ecStmt.setString(3, ecPhoneNumber);
                ecStmt.setString(4, ecRelationship);
                ecStmt.execute();
            }
        }

        // Insert additional team names into CaresFor table
        String caresFor = "INSERT INTO CaresFor (ssn, team_name, client_active) VALUES (?, ?, ?)";
        for (int i = 0; i < teamNames.size(); i++) { // Start from 1 since the first team was already added
            try (CallableStatement caresForStmt = connection.prepareCall(caresFor)) {
                caresForStmt.setInt(1, cSSN);
                caresForStmt.setString(2, teamNames.get(i));
                // Get client active status
                System.out.println("Is client active for team " + teamNames.get(i) + "? (1 for yes, 0 for no):");
                int active = keyboard.nextInt();
                keyboard.nextLine();
                caresForStmt.setInt(3, active);
                caresForStmt.execute();
            }
        }
        // Insert into Has table
        String has = "INSERT INTO Has (ssn, policy_id) VALUES (?, ?)";
        try (CallableStatement hasStmt = connection.prepareCall(has)) {
            hasStmt.setInt(1, cSSN);
            hasStmt.setString(2, cInsurancePolicyId);
            hasStmt.execute();
        }
    }

    // query 3
    // Method to insert a new volunteer into the database
    private static void executeInsertVolunteerProcedure(Connection connection, Scanner inputScanner) throws SQLException {
        // Prompt for volunteer SSN
        System.out.println("Enter volunteer ssn:");
        int volunteerSSN = inputScanner.nextInt();
        inputScanner.nextLine(); // Consume newline left-over

        // Prompt for volunteer name
        System.out.println("Enter volunteer name: ");
        String name = inputScanner.nextLine();

        // Prompt for volunteer gender
        System.out.println("Enter volunteer gender:");
        String gender = inputScanner.nextLine();

        // Prompt for volunteer profession
        System.out.println("Enter volunteer profession:");
        String profession = inputScanner.nextLine();

        // Prompt for volunteer mailing list status
        System.out.println("Enter volunteer mailing list status (1 for yes, 0 for no):");
        int mailingListStatus = inputScanner.nextInt();
        inputScanner.nextLine(); // Consume newline left-over

        // Prompt for volunteer mailing address
        System.out.println("Enter volunteer mailing address:");
        String mailingAddress = inputScanner.nextLine();

        // Prompt for volunteer phone number
        System.out.println("Enter volunteer phone number:");
        String phoneNumber = inputScanner.nextLine();

        // Prompt for volunteer email
        System.out.println("Enter volunteer email:");
        String email = inputScanner.nextLine();

        // Prompt for volunteer date joined
        System.out.println("Enter volunteer date joined (yyyy-MM-dd):");
        String dateJoined = inputScanner.nextLine();
        if (!isValidDate(dateJoined)) {
            System.err.println("Invalid date format for date joined. Please enter the date in yyyy-MM-dd format.");
            return;
        }

        // Prompt for volunteer training date
        System.out.println("Enter volunteer training date (yyyy-MM-dd):");
        String trainingDate = inputScanner.nextLine();
        if (!isValidDate(trainingDate)) {
            System.err.println("Invalid date format for training date. Please enter the date in yyyy-MM-dd format.");
            return;
        }

        // Prompt for volunteer training location
        System.out.println("Enter volunteer training location:");
        String trainingLocation = inputScanner.nextLine();

        // Prepare the main volunteer insertion procedure with 14 parameters
        String sql = "{call insertVolunteer(?,?,?,?,?,?,?,?,?,?,?)}";
        CallableStatement callableStatement = connection.prepareCall(sql);
        // Set parameters for `insertVolunteer`
        callableStatement.setInt(1, volunteerSSN); // ssn (INT)
        callableStatement.setString(2, name); // person_name (VARCHAR)
        callableStatement.setString(3, gender); // gender (VARCHAR)
        callableStatement.setString(4, profession); // profession (VARCHAR)
        callableStatement.setInt(5, mailingListStatus); // on_mailing_list (BIT/INT)
        callableStatement.setString(6, mailingAddress); // mailing_address (VARCHAR)
        callableStatement.setString(7, email); // email_address (VARCHAR)
        callableStatement.setString(8, phoneNumber); // phone_number (VARCHAR)
        callableStatement.setString(9, dateJoined); // date_joined (DATE as String)
        callableStatement.setString(10, trainingDate); // training_date (DATE as String)
        callableStatement.setString(11, trainingLocation); // training_location (VARCHAR)

        // Execute the stored procedure
        callableStatement.execute();
        System.out.println("Volunteer inserted successfully.");
        callableStatement.close();

        // Insert emergency contacts
        String emergencyContactSql = "INSERT INTO VolunteerEmergencyContacts (volunteer_ssn, contact_name, " +
                "contact_phone_number, relationship) VALUES (?, ?, ?, ?)";
        System.out.println("How many emergency contacts does the client have?");
        int emergencyContacts = inputScanner.nextInt();
        inputScanner.nextLine();
        for (int i = 0; i < emergencyContacts; i++) {
            System.out.println("Please enter the name of emergency contact " + (i + 1) + ":");
            String Name = inputScanner.nextLine();
            System.out.println("Please enter the phone number of emergency contact " + (i + 1) + ":");
            String PhoneNumber = inputScanner.nextLine();
            System.out.println("Please enter the relationship of emergency contact " + (i + 1) + ":");
            String Relationship = inputScanner.nextLine();
            try (CallableStatement ecStmt = connection.prepareCall(emergencyContactSql)) {
                ecStmt.setInt(1, volunteerSSN);
                ecStmt.setString(2, Name);
                ecStmt.setString(3, PhoneNumber);
                ecStmt.setString(4, Relationship);
                ecStmt.execute();
            }
        }

        // Collect team names
        List<String> teamNames = new ArrayList<>();
        System.out.println("How many teams does the volunteer serve on?");
        int numberOfTeams = inputScanner.nextInt();
        inputScanner.nextLine(); // Consume newline left-over
        for (int i = 0; i < numberOfTeams; i++) {
            System.out.println("Enter team name:");
            teamNames.add(inputScanner.nextLine());
        }

        // Insert additional team names into ServesOn table
        for (String teamName : teamNames) {
            String servesOnSql = "INSERT INTO ServesOn (ssn, team_name, serving_month, served_hours, active) VALUES (?, ?, ?, ?, ?)";
            try (CallableStatement servesOnStmt = connection.prepareCall(servesOnSql)) {
                servesOnStmt.setInt(1, volunteerSSN);
                servesOnStmt.setString(2, teamName);

                // Prompt for serving month
                System.out.println("Enter the number of months a volunteer has served on a team " + teamName + ":");
                int monthsServed = inputScanner.nextInt();
                inputScanner.nextLine(); // Consume newline left-over

                // Prompt for active status
                System.out.println("Is the volunteer active for team " + teamName + "? (1 for yes, 0 for no):");
                int active = inputScanner.nextInt();
                inputScanner.nextLine(); // Consume newline left-over
                servesOnStmt.setInt(5, active);

                for (int j = 0; j < monthsServed; ++j) {

                    // Prompt for month served
                    System.out.println("Enter the month served by volunteer on team " + teamName + ":");
                    String servedMonth = inputScanner.nextLine();
                    servesOnStmt.setString(3, servedMonth);

                    // Prompt for hours served
                    System.out.println("Enter the number of hours served by volunteer on team " + teamName + ":");
                    int servedHours = inputScanner.nextInt();
                    inputScanner.nextLine(); // Consume newline left-over
                    servesOnStmt.setInt(4, servedHours);

                    // Execute the insert statement
                    servesOnStmt.execute();


                }
            }
        }

        // Insert additional team leadership information if applicable
        String leadsSql = "INSERT INTO Leads (ssn, team_name, is_leader) VALUES (?, ?, ?)";
        for (String teamName : teamNames) {
            try (CallableStatement leadsStmt = connection.prepareCall(leadsSql)) {
                leadsStmt.setInt(1, volunteerSSN);
                leadsStmt.setString(2, teamName);

                // Prompt for leadership status
                System.out.println("Is the volunteer the team leader for: '" + teamName + "'? (1 for yes, 0 for no):");
                int isLeader = inputScanner.nextInt();
                inputScanner.nextLine(); // Consume newline left-over
                leadsStmt.setInt(3, isLeader);

                // Execute the insert statement
                leadsStmt.execute();
                System.out.println("Assigned volunteer as team leader for: '" + teamName + "'.");
            }
        }
    }

    // Query 4
    private static void executeInsertVolunteerHoursProcedure(Connection connection, Scanner inputScanner) throws SQLException {
        // Prompt for volunteer SSN
        System.out.println("Enter the volunteer ssn:");
        int ssn = inputScanner.nextInt();
        inputScanner.nextLine(); // Consume newline left-over

        // Prompt for team name
        System.out.println("Enter the team name:");
        String teamName = inputScanner.nextLine();

        // Prompt for serving month
        System.out.println("Enter the serving month (e.g., January):");
        String servingMonth = inputScanner.nextLine();

        // Prompt for hours worked
        System.out.println("Enter the hours worked:");
        int hoursWorked = inputScanner.nextInt();
        inputScanner.nextLine(); // Consume newline left-over

        // Prompt for active status
        System.out.println("Is the volunteer currently active? (1 for yes, 0 for no):");
        int isActive = inputScanner.nextInt();
        inputScanner.nextLine(); // Consume newline left-over

        // Prepare the SQL call for the procedure
        String sql = "{call InsertVolunteerHours(?,?,?,?,?)}";
        CallableStatement callableStatement = connection.prepareCall(sql);

        // Set parameters for `insertVolunteerHours`
        callableStatement.setInt(1, ssn); // ssn (INT)
        callableStatement.setString(2, teamName); // team_name (VARCHAR)
        callableStatement.setString(3, servingMonth); // serving_month (VARCHAR)
        callableStatement.setInt(4, hoursWorked); // hours_worked (INT)
        callableStatement.setInt(5, isActive); // active (BIT/INT)

        // Execute the stored procedure
        callableStatement.execute();
        System.out.println("Volunteer hours updated successfully.");
        callableStatement.close();
    }

    // Query 5
    // Method to insert a new employee into the database
    private static void executeInsertEmployeeProcedure(Connection connection, Scanner inputScanner) throws SQLException {
        // Prompt for employee SSN
        System.out.print("Please enter the employee's SSN: ");
        int employeeSSN = inputScanner.nextInt();
        inputScanner.nextLine(); // Consume newline left-over

        // Prompt for employee name
        System.out.print("Please enter the employee's name: ");
        String employeeName = inputScanner.nextLine();

        // Prompt for employee gender
        System.out.print("Please enter the employee's gender: ");
        String employeeGender = inputScanner.nextLine();

        // Prompt for employee profession
        System.out.print("Please enter the employee's profession: ");
        String employeeProfession = inputScanner.nextLine();

        // Prompt for employee mailing list status
        System.out.print("Is the employee on the mailing list? (1 for yes, 0 for no): ");
        int mailingListStatus = inputScanner.nextInt();
        inputScanner.nextLine(); // Consume newline left-over

        // Prompt for employee mailing address
        System.out.print("Please enter the employee's mailing address: ");
        String mailingAddress = inputScanner.nextLine();

        // Prompt for employee phone number
        System.out.print("Please enter the employee's phone number: ");
        String phoneNumber = inputScanner.nextLine();

        // Prompt for employee email address
        System.out.print("Please enter the employee's email address: ");
        String emailAddress = inputScanner.nextLine();

        // Prompt for employee salary
        System.out.print("Please enter the employee's salary: ");
        BigDecimal salary = inputScanner.nextBigDecimal();
        inputScanner.nextLine(); // Consume newline left-over

        // Prompt for employee marital status
        System.out.print("Please enter the employee's marital status: ");
        String maritalStatus = inputScanner.nextLine();

        // Prompt for employee hire date
        System.out.print("Please enter the employee's hire date (yyyy-MM-dd): ");
        String hireDate = inputScanner.nextLine();

        // Prepare the SQL call for the procedure
        String sql = "{call insertEmployee(?,?,?,?,?,?,?,?,?,?,?)}";
        CallableStatement callableStatement = connection.prepareCall(sql);

        // Set parameters for the stored procedure
        callableStatement.setInt(1, employeeSSN); // Set the SSN parameter
        callableStatement.setString(2, employeeName); // Set the name parameter
        callableStatement.setString(3, employeeGender); // Set the gender parameter
        callableStatement.setString(4, employeeProfession); // Set the profession parameter
        callableStatement.setInt(5, mailingListStatus); // Set the mailing list status parameter
        callableStatement.setString(6, mailingAddress); // Set the mailing address parameter
        callableStatement.setString(7, phoneNumber); // Set the phone number parameter
        callableStatement.setString(8, emailAddress); // Set the email address parameter
        callableStatement.setBigDecimal(9, salary); // Set the salary parameter
        callableStatement.setString(10, maritalStatus); // Set the marital status parameter
        callableStatement.setString(11, hireDate); // Set the hire date parameter

        // Execute the stored procedure
        callableStatement.execute();
        System.out.println("Employee inserted successfully.");
        callableStatement.close();

        // Insert emergency contacts
        String emergencyContactSql = "INSERT INTO EmployeeEmergencyContacts (employee_ssn, contact_name, " +
                "contact_phone_number, relationship) VALUES (?, ?, ?, ?)";
        System.out.println("How many emergency contacts does the client have?");
        int emergencyContacts = inputScanner.nextInt();
        inputScanner.nextLine();
        for (int i = 0; i < emergencyContacts; i++) {
            System.out.println("Please enter the name of emergency contact " + (i + 1) + ":");
            String Name = inputScanner.nextLine();
            System.out.println("Please enter the phone number of emergency contact " + (i + 1) + ":");
            String PhoneNumber = inputScanner.nextLine();
            System.out.println("Please enter the relationship of emergency contact " + (i + 1) + ":");
            String Relationship = inputScanner.nextLine();
            try (CallableStatement ecStmt = connection.prepareCall(emergencyContactSql)) {
                ecStmt.setInt(1, employeeSSN);
                ecStmt.setString(2, Name);
                ecStmt.setString(3, PhoneNumber);
                ecStmt.setString(4, Relationship);
                ecStmt.execute();
            }
        }

        // Collect team names
        List<String> teamNames = new ArrayList<>();
        System.out.print("How many teams does the employee belong to? ");
        int numberOfTeams = inputScanner.nextInt();
        inputScanner.nextLine(); // Consume newline left-over
        for (int i = 0; i < numberOfTeams; i++) {
            System.out.print("Please enter the name of team " + (i + 1) + ": ");
            teamNames.add(inputScanner.nextLine());
        }

        // Insert additional team names into Reports table
        String reportsSql = "INSERT INTO Reports (ssn, team_name, report_date, report_description) VALUES (?, ?, ?, ?)";
        for (String teamName : teamNames) {
            try (CallableStatement reportsStmt = connection.prepareCall(reportsSql)) {
                reportsStmt.setInt(1, employeeSSN);
                reportsStmt.setString(2, teamName);

                // Prompt for report date
                System.out.print("Please enter the report date (yyyy-MM-dd) for team " + teamName + ": ");
                String reportDate = inputScanner.nextLine();
                reportsStmt.setString(3, reportDate);

                // Prompt for report description
                System.out.print("Please enter the report description for team " + teamName + ": ");
                String reportDescription = inputScanner.nextLine();
                reportsStmt.setString(4, reportDescription);

                // Execute the insert statement
                reportsStmt.execute();
            }
        }
    }

    // Query 6
    // Method to insert a new expense into the database
    private static void executeInsertExpenseProcedure(Connection connection, Scanner inputScanner) throws SQLException {
        // Prompt for employee SSN
        System.out.print("Please enter the employee's SSN: ");
        int employeeSSN = inputScanner.nextInt();
        inputScanner.nextLine(); // Consume newline left-over

        // Prompt for expense date
        System.out.print("Please enter the expense date (yyyy-MM-dd): ");
        String expenseDate = inputScanner.next();

        // Handle date format
        if (!isValidDate(expenseDate)) {
            System.err.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
            return;
        }

        Date sqlExpenseDate = null;
        BigDecimal expenseAmount = null;

        // Prompt for expense amount
        System.out.print("Please enter the expense amount: ");
        String expenseAmountInput = inputScanner.next();

        // Handle decimal parsing
        try {
            expenseAmount = new BigDecimal(expenseAmountInput);
        } catch (NumberFormatException e) {
            System.err.println("Invalid decimal format. Please enter a valid decimal number.");
            e.printStackTrace();
            return; // Exit the method if decimal parsing fails
        }

        // Prompt for expense description
        System.out.print("Please enter the expense description: ");
        String expenseDescription = inputScanner.next();

        // Prepare the SQL call for the procedure
        String sql = "{call insertExpense(?, ?, ?, ?)}";
        CallableStatement callableStatement = connection.prepareCall(sql);

        // Set parameters for the stored procedure
        callableStatement.setInt(1, employeeSSN); // Set the SSN parameter
        callableStatement.setString(2, expenseDate); // Set the expense date parameter
        callableStatement.setBigDecimal(3, expenseAmount); // Set the expense amount parameter
        callableStatement.setString(4, expenseDescription); // Set the expense description parameter

        // Execute the stored procedure
        callableStatement.execute();
        System.out.println("Expense inserted successfully.");

        callableStatement.close();
    }

    // Query 7
    private static void executeInsertDonorProcedure(Connection connection, Scanner inputScanner) throws SQLException {
        // Prompt for donor SSN
        System.out.print("Please enter the donor's SSN: ");
        int donorSSN = inputScanner.nextInt();
        inputScanner.nextLine(); // Consume newline left-over

        // Prompt for donor name
        System.out.print("Please enter the donor's name: ");
        String donorName = inputScanner.nextLine();

        // Prompt for donor gender
        System.out.print("Please enter the donor's gender: ");
        String donorGender = inputScanner.nextLine();

        // Prompt for donor profession
        System.out.print("Please enter the donor's profession: ");
        String donorProfession = inputScanner.nextLine();

        // Prompt for donor mailing list status
        System.out.print("Is the donor on the mailing list? (1 for yes, 0 for no): ");
        int mailingListStatus = inputScanner.nextInt();
        inputScanner.nextLine(); // Consume newline left-over

        // Prompt for donor mailing address
        System.out.print("Please enter the donor's mailing address: ");
        String mailingAddress = inputScanner.nextLine();

        // Prompt for donor phone number
        System.out.print("Please enter the donor's phone number: ");
        String phoneNumber = inputScanner.nextLine();

        // Prompt for donor email
        System.out.print("Please enter the donor's email address: ");
        String emailAddress = inputScanner.nextLine();

        // Prompt for donor anonymity status
        System.out.print("Is the donor anonymous? (1 for yes, 0 for no): ");
        int isAnonymous = inputScanner.nextInt();
        inputScanner.nextLine(); // Consume newline left-over

        // Prepare the SQL call for the procedure
        String sql = "{call insertDonor(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        CallableStatement callableStatement = connection.prepareCall(sql);

        // Set parameters for the stored procedure
        callableStatement.setInt(1, donorSSN); // Set the SSN parameter
        callableStatement.setString(2, donorName); // Set the name parameter
        callableStatement.setString(3, donorGender); // Set the gender parameter
        callableStatement.setString(4, donorProfession); // Set the profession parameter
        callableStatement.setInt(5, mailingListStatus); // Set the mailing list status parameter
        callableStatement.setString(6, mailingAddress); // Set the mailing address parameter
        callableStatement.setString(7, emailAddress); // Set the email address parameter
        callableStatement.setString(8, phoneNumber); // Set the phone number parameter
        callableStatement.setInt(9, isAnonymous); // Set the anonymous status parameter

        // Execute the stored procedure
        callableStatement.execute();
        System.out.println("Donor inserted successfully.");
        callableStatement.close();

        String emergencyContactSql = "INSERT INTO DonorEmergencyContacts (donor_ssn, contact_name, " +
                "contact_phone_number, relationship) VALUES (?, ?, ?, ?)";
        System.out.println("How many emergency contacts does the client have?");
        int emergencyContacts = inputScanner.nextInt();
        inputScanner.nextLine();
        for (int i = 0; i < emergencyContacts; i++) {
            System.out.println("Please enter the name of emergency contact " + (i + 1) + ":");
            String Name = inputScanner.nextLine();
            System.out.println("Please enter the phone number of emergency contact " + (i + 1) + ":");
            String PhoneNumber = inputScanner.nextLine();
            System.out.println("Please enter the relationship of emergency contact " + (i + 1) + ":");
            String Relationship = inputScanner.nextLine();
            try (CallableStatement ecStmt = connection.prepareCall(emergencyContactSql)) {
                ecStmt.setInt(1, donorSSN);
                ecStmt.setString(2, Name);
                ecStmt.setString(3, PhoneNumber);
                ecStmt.setString(4, Relationship);
                ecStmt.execute();
            }
        }

        // Ask for number of donations
        System.out.print("How many donations does the donor wish to make? ");
        int numberOfDonations = inputScanner.nextInt();
        inputScanner.nextLine(); // Consume newline left-over

        // Insert additional donation information based on credit card or check
        for (int i = 0; i < numberOfDonations; i++) {
            // Prompt for donation date
            System.out.print("Please enter the donation date (yyyy-MM-dd): ");
            String donationDate = inputScanner.nextLine();

            // Prompt for donation amount
            System.out.print("Please enter the donation amount: ");
            BigDecimal donationAmount = inputScanner.nextBigDecimal();
            inputScanner.nextLine(); // Consume newline left-over

            // Prompt for donation type
            System.out.print("Please enter the donation type (credit card or check): ");
            String donationType = inputScanner.nextLine();

            // Prompt for campaign name
            System.out.print("Please enter the campaign name: ");
            String campaignName = inputScanner.nextLine();

            if (donationType.equalsIgnoreCase("credit card")) {
                // Prompt for card number
                System.out.print("Please enter the card number: ");
                String cardNumber = inputScanner.nextLine();

                // Prompt for card type
                System.out.print("Please enter the card type: ");
                String cardType = inputScanner.nextLine();

                // Prompt for card expiration date
                System.out.print("Please enter the card expiration date (yyyy-MM-dd): ");
                String expirationDate = inputScanner.nextLine();

                // Prepare the SQL call for the procedure
                String creditCardSql = "INSERT INTO CreditCards (ssn, card_number, card_type, expiration_date, donation_date, amount, donation_type, campaign_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                try (CallableStatement creditCardStmt = connection.prepareCall(creditCardSql)) {
                    creditCardStmt.setInt(1, donorSSN);
                    creditCardStmt.setString(2, cardNumber);
                    creditCardStmt.setString(3, cardType);
                    creditCardStmt.setString(4, expirationDate);
                    creditCardStmt.setString(5, donationDate);
                    creditCardStmt.setBigDecimal(6, donationAmount);
                    creditCardStmt.setString(7, donationType);
                    creditCardStmt.setString(8, campaignName);
                    creditCardStmt.execute();
                }

            } else if (donationType.equalsIgnoreCase("check")) {
                // Prompt for check number
                System.out.print("Please enter the check number: ");
                String checkNumber = inputScanner.nextLine();

                // Prepare the SQL call for the procedure
                String checkSql = "INSERT INTO DonationChecks (ssn, check_number, donation_date, amount, donation_type, campaign_name) VALUES (?, ?, ?, ?, ?, ?)";
                try (CallableStatement checkStmt = connection.prepareCall(checkSql)) {
                    checkStmt.setInt(1, donorSSN);
                    checkStmt.setString(2, checkNumber);
                    checkStmt.setString(3, donationDate);
                    checkStmt.setBigDecimal(4, donationAmount);
                    checkStmt.setString(5, donationType);
                    checkStmt.setString(6, campaignName);
                    checkStmt.execute();
                }

            } else {
                System.out.println("Invalid donation type. Please enter either 'credit card' or 'check'.");
            }
        }
    }

    //Query 8
    private static void executeRetrieveDoctorInfoProcedure(Connection connection, Scanner inputScanner) throws SQLException {
        // Prompt for client SSN
        System.out.print("Please enter the client's SSN: ");
        int clientSSN = inputScanner.nextInt();
        inputScanner.nextLine(); // Consume newline left-over

        // Prepare the SQL call for the procedure
        String sql = "{call retrieveDoctorInfo(?)}";
        try (CallableStatement callableStatement = connection.prepareCall(sql)) {
            // Set the SSN parameter
            callableStatement.setInt(1, clientSSN);

            // Execute the query and process the result set
            try (ResultSet resultSet = callableStatement.executeQuery()) {
                while (resultSet.next()) {
                    // Retrieve and print doctor information
                    System.out.println("Doctor's Name: " + resultSet.getString("doctor_name"));
                    System.out.println("Doctor's Phone Number: " + resultSet.getString("doctor_phone_number"));
                }
            }
        }
    }

    //Query 9
    private static void executeRetrieveTotalExpensesProcedure(Connection connection, Scanner inputScanner) throws SQLException {
        // Prompt for start date
        System.out.print("Please enter the start date (YYYY-MM-DD): ");
        String startDate = inputScanner.next();

        // Prompt for end date
        System.out.print("Please enter the end date (YYYY-MM-DD): ");
        String endDate = inputScanner.next();

        // Prepare the SQL call for the procedure
        String sql = "{call retrieveTotalExpenses(?, ?)}";
        try (CallableStatement callableStatement = connection.prepareCall(sql)) {
            // Set the start and end date parameters
            callableStatement.setString(1, startDate);
            callableStatement.setString(2, endDate);

            // Execute the query and process the result set
            try (ResultSet resultSet = callableStatement.executeQuery()) {
                while (resultSet.next()) {
                    // Retrieve and print employee SSN and total expenses
                    System.out.println("Employee SSN: " + resultSet.getInt("ssn"));
                    System.out.println("Total Expenses: " + resultSet.getDouble("total_expenses"));
                }
            }
        }
    }

    // query 10
    private static void executeGetVolunteersForClientProcedure(Connection connection, Scanner inputScanner) throws SQLException {
        // Prompt for client SSN
        System.out.println("Enter the SSN of the client:");
        // Read the client SSN from the user
        int clientSSN = inputScanner.nextInt();
        inputScanner.nextLine();  // Consume newline left-over
        String sql = "{call GetVolunteersForClient(?)}";

        // Execute the stored procedure
        try (CallableStatement stmt = connection.prepareCall(sql)) {
            stmt.setInt(1, clientSSN);
            // Execute the query and process the result set
            try (ResultSet rs = stmt.executeQuery()) {
                boolean hasResults = false;
                System.out.println("Volunteers supporting client with SSN " + clientSSN + ":");
                // Iterate over the result set and print the volunteer names
                while (rs.next()) {
                    hasResults = true;
                    String volunteerName = rs.getString("person_name");
                    System.out.println("- " + volunteerName);
                }

                // Print message if no volunteers found
                if (!hasResults) {
                    System.out.println("No volunteers found for this client.");
                }
            }
        } catch (SQLException e) {
            System.err.println("An error occurred while retrieving volunteer information: " + e.getMessage());
            throw e;  // Rethrow exception if further handling is needed
        }
    }

    // Helper method to validate date format
    private static boolean isValidDate(String dateString) {
        // Define date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // Set strict parsing for the date input into the console
        dateFormat.setLenient(false); // Strict date parsing
        try {
            dateFormat.parse(dateString); // Attempt to parse the date
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    // query 11
    private static void executeGetTeamsFoundedAfterDateProcedure(Connection connection, Scanner inputScanner) throws SQLException {
        // Prompt for the date to retrieve teams founded after
        System.out.println("Enter the date to retrieve teams founded after (YYYY-MM-DD):");
        // Read the date from the user
        String dateFormed = inputScanner.next();
        // Prepare the SQL call for the procedure
        String sql = "{call getTeamsAfterDate(?)}";
        // Execute the stored procedure
        try (CallableStatement stmt = connection.prepareCall(sql)) {
            stmt.setString(1, dateFormed);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println("Team name: " + rs.getString("team_name"));
                }
            }
        }
    }

    // query 12
    private static void executeGetPeopleInfoProcedure(Connection connection, Scanner inputScanner) throws SQLException {
        // Prepare the SQL call for the procedure
        String sql = "{call getAllPeopleInfo()}";
        // Execute the stored procedure
        try (CallableStatement stmt = connection.prepareCall(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println("Name: " + rs.getString("person_name"));
                    System.out.println("SSN: " + rs.getInt("ssn"));
                    System.out.println("Mailing Address: " + rs.getString("mailing_address"));
                    System.out.println("Phone Number: " + rs.getString("phone_number"));
                    System.out.println("Email Address: " + rs.getString("email_address"));
                    System.out.println("Emergency Contact Name: " + rs.getString("emergency_contact_name"));
                    System.out.println("Emergency Contact Phone: " + rs.getString("emergency_contact_phone"));
                    System.out.println("Emergency Contact Relationship: " + rs.getString("emergency_contact_relationship"));
                }
            }
        }
    }

    // query 13
    private static void executeGetDonorsThatAreEmployeesProcedure(Connection connection, Scanner inputScanner) throws SQLException {
        // Prepare the SQL call for the procedure
        String sql = "{call getEmployeeDonors()}";
        // Execute the stored procedure
        try (CallableStatement stmt = connection.prepareCall(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println("Name: " + rs.getString("person_name"));
                    System.out.println("Is the donor anonymous?: " + rs.getString("is_anon"));
                    System.out.println("Total Donations: " + rs.getDouble("total_donations"));
                }
            }
        }
    }

    // query 14
    private static void executeIncreaseSalaryForEmployeesProcedure(Connection connection, Scanner inputScanner) throws SQLException {
        // Prepare the SQL call for the procedure
        String sql = "{call increaseSalaryForMultiTeamEmployees()}";
        try (CallableStatement stmt = connection.prepareCall(sql)) {
            stmt.execute();
        }
    }

    // query 15
    private static void executeDeleteClientsProcedure(Connection connection) throws SQLException {
        // Prepare the SQL call for the procedure
        String sql = "{call deleteClients()}";
        try (CallableStatement stmt = connection.prepareCall(sql)) {
            stmt.execute();
            System.out.println("Successfully deleted uninsured clients with low transportation importance.");
        } catch (SQLException e) {
            System.err.println("Error executing delete procedure: " + e.getMessage());
            throw e;
        }
    }

    // query 16
    private static void executeImportTeamProcedure(Connection connection, Scanner keyboard) throws SQLException, IOException {
        System.out.print("Enter the input file name (e.g., teams.txt): ");
        String fileName = keyboard.next();

        // Get the directory path of the current class
        String basePath = new java.io.File(".").getCanonicalPath();
        String PathToFile = basePath + java.io.File.separator + fileName;

        // Read the file line by line and insert team details
        try (BufferedReader reader = new BufferedReader(new FileReader(PathToFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Parse the line to extract team details
                String[] teamData = line.split(",");
                if (teamData.length != 3) {
                    System.out.println("Invalid data format in line: " + line);
                    continue;
                }

                // Extract team details
                String teamName = teamData[0].trim();
                String teamType = teamData[1].trim();
                Date dateOfCreation = Date.valueOf(teamData[2].trim()); // Assumes format yyyy-MM-dd

                // Call the InsertTeam stored procedure
                String query = "{CALL InsertTeam(?, ?, ?)}";
                try (CallableStatement callableStatement = connection.prepareCall(query)) {
                    callableStatement.setString(1, teamName);
                    callableStatement.setString(2, teamType);
                    callableStatement.setDate(3, dateOfCreation);

                    // Execute the stored procedure
                    callableStatement.executeUpdate();
                    System.out.println("Inserted team: " + teamName);
                } catch (SQLException e) {
                    System.out.println("Error inserting team '" + teamName + "': " + e.getMessage());
                }
            }
            // Close the reader
            System.out.println("Team import completed from file: " + PathToFile);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + PathToFile);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    // Query 17: Export: Retrieve names and mailing addresses of all people on the mailing list and output them to a data file instead of screen (the user must be asked to enter the output file name).
    private static void executeExportMailingList(Connection connection, Scanner keyboard) throws SQLException, IOException {
        System.out.print("Enter the output file name (e.g., output.txt): ");
        String nameOfFile = keyboard.next();

        // Get the directory path of the current class
        String base = new java.io.File(".").getCanonicalPath();
        String file = base + java.io.File.separator + nameOfFile;

        // Prepare the SQL call for the procedure
        String query = "{CALL executeExportMailingList()}";
        try (CallableStatement callableStatement = connection.prepareCall(query);
             ResultSet resultSet = callableStatement.executeQuery()) {

            try (FileWriter fWriter = new FileWriter(file)) {
                while (resultSet.next()) {
                    String name = resultSet.getString("person_name");
                    String address = resultSet.getString("mailing_address");

                    fWriter.write("Name: " + name + ", Address: " + address + "\n");
                }
                System.out.println("Data successfully exported to " + file);
            } catch (IOException e) {
                System.out.println("Error writing to file: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error executing stored procedure: " + e.getMessage());
        }
    }
}