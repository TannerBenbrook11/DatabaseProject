DROP TABLE IF EXISTS DonorEmergencyContacts;
DROP TABLE IF EXISTS VolunteerEmergencyContacts;
DROP TABLE IF EXISTS EmployeeEmergencyContacts;
DROP TABLE IF EXISTS ClientEmergencyContacts;
DROP TABLE IF EXISTS Has;
DROP TABLE IF EXISTS Gives;
DROP TABLE IF EXISTS Leads;
DROP TABLE IF EXISTS Reports;
DROP TABLE IF EXISTS CaresFor;
DROP TABLE IF EXISTS ServesOn;
DROP TABLE IF EXISTS CreditCards;
DROP TABLE IF EXISTS DonationChecks;
DROP TABLE IF EXISTS Donors;
DROP TABLE IF EXISTS Expenses;
DROP TABLE IF EXISTS Employees;
DROP TABLE IF EXISTS Teams;
DROP TABLE IF EXISTS InsurancePolicies;
DROP TABLE IF EXISTS Volunteers;
DROP TABLE IF EXISTS Needs;
DROP TABLE IF EXISTS Clients;

-- Clients Table

CREATE TABLE Clients (
    ssn INT PRIMARY KEY NOT NULL,
    person_name VARCHAR(256) NOT NULL,
    gender VARCHAR(256) NOT NULL,
    profession VARCHAR(256) NOT NULL,
    on_mailing_list BIT NOT NULL,
    mailing_address VARCHAR(256) NOT NULL,
    phone_number VARCHAR(256) NOT NULL,
    email_address VARCHAR(256) NOT NULL,
    assignment_date DATE NOT NULL,
    doctor_name VARCHAR(256) NOT NULL,
    doctor_phone_number VARCHAR(256) NOT NULL
);

-- Volunteers Table

CREATE TABLE Volunteers (
    ssn INT PRIMARY KEY NOT NULL,
    person_name VARCHAR(256) NOT NULL,
    gender VARCHAR(256) NOT NULL,
    profession VARCHAR(256) NOT NULL,
    on_mailing_list BIT NOT NULL,
    mailing_address VARCHAR(256) NOT NULL,
    phone_number VARCHAR(256) NOT NULL,
    email_address VARCHAR(256) NOT NULL,
    date_joined DATE NOT NULL,
    training_date DATE NOT NULL,
    training_location VARCHAR(256) NOT NULL
);

-- Employees Table

CREATE TABLE Employees (
    ssn INT PRIMARY KEY NOT NULL,
    person_name VARCHAR(256) NOT NULL,
    gender VARCHAR(256) NOT NULL,
    profession VARCHAR(256) NOT NULL,
    on_mailing_list BIT NOT NULL,
    mailing_address VARCHAR(256) NOT NULL,
    phone_number VARCHAR(256) NOT NULL,
    email_address VARCHAR(256) NOT NULL,
    salary DECIMAL(10, 2) NOT NULL,
    marital_status VARCHAR(256) NOT NULL,
    hire_date DATE NOT NULL
);

-- Donors Table

CREATE TABLE Donors (
    ssn INT PRIMARY KEY NOT NULL,
    person_name VARCHAR(256) NOT NULL,
    gender VARCHAR(256) NOT NULL,
    profession VARCHAR(256) NOT NULL,
    on_mailing_list BIT NOT NULL,
    mailing_address VARCHAR(256) NOT NULL,
    phone_number VARCHAR(256) NOT NULL,
    email_address VARCHAR(256) NOT NULL,
    is_anon BIT NOT NULL
);

-- ClientEmergencyContacts Table
CREATE TABLE DonorEmergencyContacts (
    donor_ssn INT NOT NULL,
    contact_name VARCHAR(256) NOT NULL,
    contact_phone_number VARCHAR(256) NOT NULL,
    relationship VARCHAR(256) NOT NULL,
    PRIMARY KEY (donor_ssn, contact_name, contact_phone_number),
    FOREIGN KEY (donor_ssn) REFERENCES Donors(ssn)
);

-- VolunteerEmergencyContacts Table
CREATE TABLE VolunteerEmergencyContacts (
    volunteer_ssn INT NOT NULL,
    contact_name VARCHAR(256) NOT NULL,
    contact_phone_number VARCHAR(256) NOT NULL,
    relationship VARCHAR(256) NOT NULL,
    PRIMARY KEY (volunteer_ssn, contact_name, contact_phone_number),
    FOREIGN KEY (volunteer_ssn) REFERENCES Volunteers(ssn)
);

-- EmployeeEmergencyContacts Table
CREATE TABLE EmployeeEmergencyContacts (
    employee_ssn INT NOT NULL,
    contact_name VARCHAR(256) NOT NULL,
    contact_phone_number VARCHAR(256) NOT NULL,
    relationship VARCHAR(256) NOT NULL,
    PRIMARY KEY (employee_ssn, contact_name, contact_phone_number),
    FOREIGN KEY (employee_ssn) REFERENCES Employees(ssn)
);

-- ClientEmergencyContacts Table
CREATE TABLE ClientEmergencyContacts (
    client_ssn INT NOT NULL,
    contact_name VARCHAR(256) NOT NULL,
    contact_phone_number VARCHAR(256) NOT NULL,
    relationship VARCHAR(256) NOT NULL,
    PRIMARY KEY (client_ssn, contact_name, contact_phone_number),
    FOREIGN KEY (client_ssn) REFERENCES Clients(ssn)
);

-- Insurance Policies Table

CREATE TABLE InsurancePolicies (
    policy_id VARCHAR(256)  NOT NULL,
    provider_name VARCHAR(256),
    provider_address VARCHAR(256),
    insurance_type VARCHAR(256),
    PRIMARY KEY (policy_id)
);

-- Teams Table

CREATE TABLE Teams (
    team_name VARCHAR(256) NOT NULL,
    team_type VARCHAR(256),
    date_formed DATE,
    PRIMARY KEY (team_name)
);

-- Expenses Table

CREATE TABLE Expenses (
    ssn INT NOT NULL,
    expense_date DATE NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    expense_description VARCHAR(256) NOT NULL,
    PRIMARY KEY (ssn, expense_date, expense_description),
    FOREIGN KEY (ssn) REFERENCES Employees(ssn)
);

-- DonationChecks Table

CREATE TABLE DonationChecks (
    ssn INT NOT NULL,
    check_number VARCHAR(256) NOT NULL,
    donation_date DATE NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    donation_type VARCHAR(256) NOT NULL,
    campaign_name VARCHAR(256) NOT NULL,
    PRIMARY KEY (ssn, donation_date, amount, donation_type),
    FOREIGN KEY (ssn) REFERENCES Donors(ssn)
);

-- CreditCards Table

CREATE TABLE CreditCards (
    ssn INT NOT NULL,
    card_number VARCHAR(256) NOT NULL,
    card_type VARCHAR(256) NOT NULL,
    expiration_date VARCHAR(256) NOT NULL,
    donation_date DATE NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    donation_type VARCHAR(256) NOT NULL,
    campaign_name VARCHAR(256) NOT NULL,
    PRIMARY KEY (ssn, donation_date, amount, donation_type),
    FOREIGN KEY (ssn) REFERENCES Donors(ssn)
);

-- ServesOn Table

CREATE TABLE ServesOn (
    ssn INT NOT NULL,
    team_name VARCHAR(256) NOT NULL,
    serving_month VARCHAR(256) NOT NULL,
    served_hours INT NOT NULL,
    active BIT NOT NULL,
    PRIMARY KEY (ssn, team_name, serving_month),
    FOREIGN KEY (ssn) REFERENCES Volunteers(ssn),
    FOREIGN KEY (team_name) REFERENCES Teams(team_name)
);

-- CaresFor Table

CREATE TABLE CaresFor (
    ssn INT NOT NULL,
    team_name VARCHAR(256) NOT NULL,
    client_active BIT NOT NULL,
    PRIMARY KEY (ssn, team_name),
    FOREIGN KEY (ssn) REFERENCES Clients(ssn) ON DELETE CASCADE,
    FOREIGN KEY (team_name) REFERENCES Teams(team_name)
);

-- Reports Table

CREATE TABLE Reports (
    ssn INT NOT NULL,
    team_name VARCHAR(256) NOT NULL,
    report_date DATE NOT NULL,
    report_description VARCHAR(512) NOT NULL,
    PRIMARY KEY (ssn, team_name),
    FOREIGN KEY (ssn) REFERENCES Employees(ssn),
    FOREIGN KEY (team_name) REFERENCES Teams(team_name)
);

-- Leads Table

CREATE TABLE Leads (
    ssn INT NOT NULL,
    team_name VARCHAR(256) NOT NULL,
    is_leader BIT NOT NULL,
    PRIMARY KEY (ssn, team_name),
    FOREIGN KEY (ssn) REFERENCES Volunteers(ssn),
    FOREIGN KEY (team_name) REFERENCES Teams(team_name)
);

-- Has Table

CREATE TABLE Has (
    ssn INT NOT NULL,
    policy_id VARCHAR(256) NOT NULL,
    PRIMARY KEY (ssn, policy_id),
    FOREIGN KEY (ssn) REFERENCES Clients(ssn),
    FOREIGN KEY (policy_id) REFERENCES InsurancePolicies(policy_id)
);

-- Needs Table

CREATE TABLE Needs (
    ssn INT NOT NULL,
    need_type VARCHAR(256) NOT NULL,
    importance INT NOT NULL,
    PRIMARY KEY (ssn, need_type),
    FOREIGN KEY (ssn) REFERENCES Clients(ssn),
    CHECK (importance >= 1 AND importance <= 10)
);

-- --Creating indexes
--Clients index
DROP INDEX IF EXISTS Clients.C_ssn;
CREATE INDEX C_ssn ON Clients(ssn);
--Volunteers index
DROP INDEX IF EXISTS Volunteers.V_ssn;
CREATE INDEX V_ssn ON Volunteers(ssn);
--Employees index
DROP INDEX IF EXISTS Employees.E_ssn;
CREATE INDEX E_ssn ON Employees(ssn);
--Donors index
DROP INDEX IF EXISTS Donors.D_ssn;
CREATE INDEX D_ssn ON Donors(ssn);
--ClientEmergencyContacts index
DROP INDEX IF EXISTS ClientEmergencyContacts.CEM_ssn;
CREATE INDEX CEM_ssn ON ClientEmergencyContacts(client_ssn);
--VolunteerEmergencyContacts index
DROP INDEX IF EXISTS VolunteerEmergencyContacts.VEM_ssn;
CREATE INDEX VEM_ssn ON VolunteerEmergencyContacts(volunteer_ssn);
--EmployeeEmergencyContacts index
DROP INDEX IF EXISTS EmployeeEmergencyContacts.EEM_ssn;
CREATE INDEX EEM_ssn ON EmployeeEmergencyContacts(employee_ssn);
--DonorsEmergencyContacts index
DROP INDEX IF EXISTS DonorEmergencyContacts.CEM_ssn;
CREATE INDEX CEM_ssn ON DonorEmergencyContacts(donor_ssn);
--Teams index
DROP INDEX IF EXISTS Teams.T_name;
CREATE INDEX T_name ON Teams(team_name);
--Expenses index
DROP INDEX IF EXISTS Expenses.E_amount;
CREATE INDEX E_amount ON Expenses(amount); 
--DonationChecks index
DROP INDEX IF EXISTS DonationChecks.DC_amount; 
CREATE INDEX DC_amount ON DonationChecks(amount);
--CreditCards index 
DROP INDEX IF EXISTS CreditCards.CC_amount;
CREATE INDEX CC_amount ON CreditCards(amount);