-- Query 1: Insert a new team into the database
DROP PROCEDURE IF EXISTS insertTeam;
GO

CREATE PROCEDURE insertTeam
(
    @team_name VARCHAR(256), -- Team name
    @team_type VARCHAR(256), -- Team type
    @date_formed DATE -- Date formed
)
AS
BEGIN
    INSERT INTO Teams (team_name, team_type, date_formed) VALUES (@team_name, @team_type, @date_formed);
END;
GO

-- Query 2: Insert a new client into the database and associate with one or more teams
DROP PROCEDURE IF EXISTS insertClient;
GO

CREATE PROCEDURE insertClient
(
    @ssn INT, -- Social Security Number
    @person_name VARCHAR(256), -- Person name
    @gender VARCHAR(256), -- gender
    @profession VARCHAR(256), -- profession
    @on_mailing_list BIT, -- on mailing list
    @mailing_address VARCHAR(256), -- mailing address
    @email_address VARCHAR(256), -- email address
    @phone_number VARCHAR(256), -- phone number
    @assignment_date DATE, -- assignment date
    @doctor_name VARCHAR(256), -- doctor name
    @doctor_phone_number VARCHAR(256), -- doctor phone number
    -- @emergency_contact_name VARCHAR(256), -- emergency contact name
    -- @contact_phone_number VARCHAR(256), -- contact phone number
    -- @relationship VARCHAR(256), -- relationship
    @need_type VARCHAR(256), -- need type
    @importance INT, -- importance
    @policy_id VARCHAR(256), -- policy id
    @provider_name VARCHAR(256), -- provider name
    @provider_address VARCHAR(256), -- provider address
    @insurance_type VARCHAR(256) -- insurance type
)
AS
BEGIN

    -- Insert into Clients table
    INSERT INTO Clients (ssn, person_name, gender, profession, on_mailing_list, mailing_address, phone_number, email_address, assignment_date, doctor_name, doctor_phone_number)
    VALUES (@ssn, @person_name, @gender, @profession, @on_mailing_list, @mailing_address, @phone_number, @email_address, @assignment_date, @doctor_name, @doctor_phone_number);

    -- Insert into Needs table
    INSERT INTO Needs (ssn, need_type, importance)
    VALUES (@ssn, @need_type, @importance);

    -- Insert into InsurancePolicies table
    INSERT INTO InsurancePolicies (policy_id, provider_name, provider_address, insurance_type)
    VALUES (@policy_id, @provider_name, @provider_address, @insurance_type);

END;
GO

-- Query 3: Insert a new volunteer into the database and associate with one or more teams
DROP PROCEDURE IF EXISTS insertVolunteer;
GO

CREATE PROCEDURE insertVolunteer
(
    @ssn INT, -- Social Security Number
    @person_name VARCHAR(256), -- Person name
    @gender VARCHAR(256), -- gender
    @profession VARCHAR(256), -- profession
    @on_mailing_list BIT, -- on mailing list
    @mailing_address VARCHAR(256), -- mailing address
    @email_address VARCHAR(256), -- email address
    @phone_number VARCHAR(256), -- phone number
    @date_joined DATE, -- date joined
    @training_date DATE, -- training date
    @training_location VARCHAR(256) -- training location
)
AS
BEGIN

    -- Insert into Volunteers table
    INSERT INTO Volunteers (ssn, person_name, gender, profession, on_mailing_list, mailing_address, phone_number, email_address, date_joined, training_date, training_location)
    VALUES (@ssn, @person_name, @gender, @profession, @on_mailing_list, @mailing_address, @phone_number, @email_address, @date_joined, @training_date, @training_location);

END;
GO

-- Query 4: Insert number of hours a volunteer worked this month for a particular team
DROP PROCEDURE IF EXISTS InsertVolunteerHours;
GO

CREATE PROCEDURE InsertVolunteerHours
(
    @ssn INT, -- ssn
    @team_name VARCHAR(256), -- team_name
    @serving_month VARCHAR(256), -- serving_month
    @served_hours INT, -- served_hours
    @active BIT -- active
)
AS
BEGIN

    -- Update ServeOn table for hours from a volunteer if month is already present
    IF EXISTS (SELECT 1 FROM ServesOn WHERE ssn = @ssn AND team_name = @team_name AND serving_month = @serving_month)
    BEGIN
        UPDATE ServesOn
        SET served_hours = @served_hours
        WHERE ssn = @ssn AND team_name = @team_name AND serving_month = @serving_month;
    END
    ELSE
    BEGIN
        -- Insert into ServesOn table
        INSERT INTO ServesOn (ssn, team_name, serving_month, served_hours, active)
        VALUES (@ssn, @team_name, @serving_month, @served_hours, @active);
    END;
END;
GO


-- Query 5: Insert a new employee into the database and associate with one or more teams
DROP PROCEDURE IF EXISTS insertEmployee;
GO

CREATE PROCEDURE insertEmployee
(
    @ssn INT, -- ssn
    @person_name VARCHAR(256), -- Person name
    @gender VARCHAR(256), -- gender
    @profession VARCHAR(256), -- profession
    @on_mailing_list BIT, -- on mailing list
    @mailing_address VARCHAR(256), -- mailing address
    @phone_number VARCHAR(256), -- phone number
    @email_address VARCHAR(256), -- email address
    @salary DECIMAL(10,2), -- salary
    @marital_status VARCHAR(256), -- marital status
    @hire_date DATE -- hire date

)
AS
BEGIN
    -- Insert into Employees table
    INSERT INTO Employees (ssn, person_name, gender, profession, on_mailing_list, mailing_address, phone_number, email_address, salary, marital_status, hire_date)
    VALUES (@ssn, @person_name, @gender, @profession, @on_mailing_list, @mailing_address, @phone_number, @email_address, @salary, @marital_status, @hire_date);

END;
GO


-- Query 6: Insert an expense charged by an employee
DROP PROCEDURE IF EXISTS insertExpense;
GO

CREATE PROCEDURE insertExpense
(
    @ssn INT, -- ssn
    @expense_date DATE, -- Expense date
    @amount DECIMAL(10, 2), -- Amount
    @expense_description VARCHAR(256) -- Expense description
)
AS
BEGIN
    INSERT INTO Expenses (ssn, expense_date, amount, expense_description)
    VALUES (@ssn, @expense_date, @amount, @expense_description);
END;
GO

-- Query 7: Insert a new donor and associate with multiple donations
DROP PROCEDURE IF EXISTS insertDonor;
GO

CREATE PROCEDURE insertDonor
(
    @ssn INT, -- ssn
    @person_name VARCHAR(256), -- Person name
    @gender VARCHAR(256), -- gender
    @profession VARCHAR(256), -- profession
    @on_mailing_list BIT, -- on mailing list
    @mailing_address VARCHAR(256), -- mailing address
    @phone_number VARCHAR(256), -- phone number
    @email_address VARCHAR(256), -- email address
    @is_anonymous BIT -- is anonymous
)
AS
BEGIN
    -- Insert into Donors table
    INSERT INTO Donors (ssn, person_name, gender, profession, on_mailing_list, mailing_address, phone_number, email_address, is_anon)
    VALUES (@ssn, @person_name, @gender, @profession, @on_mailing_list, @mailing_address, @phone_number, @email_address, @is_anonymous);


END;
GO


-- Query 8: Retrieve the name and phone number of the doctor of a particular client (1/week)
DROP PROCEDURE IF EXISTS retrieveDoctorInfo;
GO

CREATE PROCEDURE retrieveDoctorInfo
(
    @ssn INT -- ssn
)
AS
BEGIN
    -- Retrieve the name and phone number of the doctor of a particular client
    SELECT doctor_name, doctor_phone_number 
    FROM Clients 
    WHERE ssn = @ssn;
END;
GO

-- Query 9: Retrieve total expenses charged by each employee for a particular period, sorted by amount (1/month)
DROP PROCEDURE IF EXISTS retrieveTotalExpenses;
GO

CREATE PROCEDURE retrieveTotalExpenses
(
    @start_date DATE, -- Start date
    @end_date DATE -- End date
)
AS
BEGIN
    -- Retrieve total expenses charged by each employee for a particular period, sorted by amount
    SELECT ssn, SUM(amount) AS total_expenses
    FROM Expenses
    WHERE expense_date BETWEEN @start_date AND @end_date
    GROUP BY ssn
    ORDER BY total_expenses DESC;
END;
GO

-- Query 10: Retrieve the list of volunteers that are members of teams that support a particular client (4/year).
DROP PROCEDURE IF EXISTS GetVolunteersForClient;
GO

CREATE PROCEDURE GetVolunteersForClient
(
    @ssn INT -- ssn
)
AS
BEGIN
    SELECT v.person_name
    FROM Volunteers v
    JOIN ServesOn s ON v.ssn = s.ssn
    JOIN CaresFor c ON s.team_name = c.team_name
    WHERE c.ssn = @ssn;
END
GO

-- Query 11: Retrieve the names of all teams founded after a specified date (1/month)
DROP PROCEDURE IF EXISTS getTeamsAfterDate;
GO

CREATE PROCEDURE getTeamsAfterDate
(
    @date_formed DATE -- Date formed
)
AS
BEGIN
    -- Retrieve names of teams founded after a specified date
    SELECT team_name 
    FROM Teams
    WHERE date_formed > @date_formed;
END;
GO

-- Query 12: Retrieve names, SSNs, contact information, and emergency contacts for all people (1/week)
DROP PROCEDURE IF EXISTS getAllPeopleInfo;
GO

CREATE PROCEDURE getAllPeopleInfo
AS
BEGIN
    -- Retrieve names, SSNs, contact information, and emergency contacts for all people
    SELECT e.person_name, e.ssn, e.mailing_address, e.phone_number, e.email_address, ec.contact_name AS emergency_contact_name, ec.contact_phone_number AS emergency_contact_phone, ec.relationship AS emergency_contact_relationship
    FROM Employees e
    LEFT JOIN EmployeeEmergencyContacts ec ON e.ssn = ec.employee_ssn
    UNION ALL
    SELECT v.person_name, v.ssn, v.mailing_address, v.phone_number, v.email_address, vc.contact_name AS emergency_contact_name, vc.contact_phone_number AS emergency_contact_phone, vc.relationship AS emergency_contact_relationship
    FROM Volunteers v
    LEFT JOIN VolunteerEmergencyContacts vc ON v.ssn = vc.volunteer_ssn
    UNION ALL
    SELECT c.person_name, c.ssn, c.mailing_address, c.phone_number, c.email_address, cc.contact_name AS emergency_contact_name, cc.contact_phone_number AS emergency_contact_phone, cc.relationship AS emergency_contact_relationship
    FROM Clients c
    LEFT JOIN ClientEmergencyContacts cc ON c.ssn = cc.client_ssn
    UNION ALL
    SELECT d.person_name, d.ssn, d.mailing_address, d.phone_number, d.email_address, dc.contact_name AS emergency_contact_name, dc.contact_phone_number AS emergency_contact_phone, dc.relationship AS emergency_contact_relationship
    FROM Donors d
    LEFT JOIN DonorEmergencyContacts dc ON d.ssn = dc.donor_ssn;
END;
GO

-- Query 13: Retrieve the name and total donations by donors who are also employees, sorted by amount (1/week)

DROP PROCEDURE IF EXISTS GetEmployeeDonors;
GO

CREATE PROCEDURE GetEmployeeDonors
AS
BEGIN
    -- Retrieve total donations by donors who are also employees, sorted by amount
    SELECT 
        d.person_name,
        CASE 
            WHEN d.is_anon = 1 THEN 'Yes'
            ELSE 'No'
        END AS is_anon,
        SUM(all_don.amount) AS total_donations
    FROM Donors d
    JOIN (
        -- Combine DonationCard and DonationCheck
        SELECT ssn, amount
        FROM CreditCards
        UNION ALL
        SELECT ssn, amount
        FROM DonationChecks
    ) all_don ON d.ssn = all_don.ssn
    JOIN Employees e ON d.ssn = e.ssn
    GROUP BY d.person_name, d.is_anon
    ORDER BY total_donations DESC;
END;
GO

-- Query 14: Increase salary by 10% for employees with reports from more than one team (1/year)
DROP PROCEDURE IF EXISTS increaseSalaryForMultiTeamEmployees;
GO

CREATE PROCEDURE increaseSalaryForMultiTeamEmployees
AS
BEGIN
    -- Increase salary by 10% for employees reporting on multiple teams
    UPDATE Employees
    SET salary = salary * 1.1
    WHERE ssn IN (
        SELECT ssn
        FROM Reports
        GROUP BY ssn
        HAVING COUNT(DISTINCT team_name) > 1
    );
END;
GO

-- Query 15: Delete all clients who do not have health insurance and whose value of importance for transportation is less than 5 (4/year).

DROP PROCEDURE IF EXISTS deleteClients;
GO

CREATE PROCEDURE deleteClients
AS
BEGIN
    DECLARE @deleteClients TABLE (ssn INT); -- Temporary table to store SSNs of clients to delete
    
    -- Insert the SSNs of clients who meet the criteria for deletion
    -- Insert the SSNs of clients who meet the criteria for deletion
    INSERT INTO @deleteClients (ssn)
    SELECT c.ssn
    FROM Clients c 
    JOIN Has h ON c.ssn = h.ssn
    JOIN InsurancePolicies ip ON h.policy_id = ip.policy_id
    WHERE ip.insurance_type != 'Health' 
    AND c.ssn IN (
        SELECT ssn 
        FROM Needs 
        WHERE need_type = 'Transportation'
        AND importance < 5
    );

    -- Delete from the 'Needs' table
    DELETE FROM Needs 
    WHERE ssn IN (SELECT ssn FROM @deleteClients);

    -- Delete from the 'Has' table
    DELETE FROM Has 
    WHERE ssn IN (SELECT ssn FROM @deleteClients);

    -- Delete from the 'InsurancePolicies' table if not referenced in the 'Has' table
    DELETE FROM InsurancePolicies
    WHERE policy_id NOT IN (SELECT policy_id FROM Has);

    -- Delete from 'ClientEmergencyContacts' table
    DELETE FROM ClientEmergencyContacts
    WHERE client_ssn IN (SELECT ssn FROM @deleteClients);

    -- Finally, delete from 'Clients' table
    DELETE FROM Clients 
    WHERE ssn IN (SELECT ssn FROM @deleteClients);
END
GO

-- Query 17: Retrieve names and mailing addresses of all people on the mailing list
DROP PROCEDURE IF EXISTS executeExportMailingList;
GO

CREATE PROCEDURE executeExportMailingList
AS
BEGIN
    -- Retrieve names and mailing addresses of all people on the mailing list
    SELECT person_name, mailing_address
    FROM Employees
    WHERE on_mailing_list = 1
    UNION ALL
    SELECT person_name, mailing_address
    FROM Volunteers
    WHERE on_mailing_list = 1
    UNION ALL
    SELECT person_name, mailing_address
    FROM Clients
    WHERE on_mailing_list = 1
    UNION ALL
    SELECT person_name, mailing_address
    FROM Donors
    WHERE on_mailing_list = 1;
END;