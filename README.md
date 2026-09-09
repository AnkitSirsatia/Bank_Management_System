# 🏦 Bank Management System

A **console-based Bank Management System** built using **Java, JDBC, and MySQL**.

The project is designed to demonstrate practical backend development concepts including **Object-Oriented Programming, layered architecture, DAO pattern, Service Layer, JDBC, database transactions, password hashing, input validation, and CRUD operations**.

---

## 🚀 Features

* 👤 Create a new bank account
* 🔐 Secure password hashing using BCrypt
* 🔑 User authentication and login
* 💰 Check account balance
* ➕ Deposit money
* ➖ Withdraw money
* 💸 Transfer money between accounts
* 📋 View transaction history
* 👤 View account details
* ✏️ Update account information
* 📧 Update account email
* 🔑 Change/reset password
* 🗑️ Close bank account
* 🔄 Transaction handling using `COMMIT` and `ROLLBACK`
* 🔒 Database credentials loaded through environment variables
* 📧 Email format validation
* 🆔 Automatic account number generation
* 🧾 Transaction record generation for banking operations

---

# 🛠️ Technologies Used

| Technology | Purpose                 |
| ---------- | ----------------------- |
| **Java**   | Application development |
| **JDBC**   | Database connectivity   |
| **MySQL**  | Relational database     |
| **BCrypt** | Password hashing        |
| **Git**    | Version control         |
| **GitHub** | Source code hosting     |

---

# 🏗️ Architecture

The application follows a **layered architecture** to separate user interaction, business logic, and database operations.

```text
                    ┌─────────────────────┐
                    │        User         │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │      Dashboard      │
                    │      UI Layer       │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │     BankService     │
                    │   Business Logic    │
                    └──────────┬──────────┘
                               │
                 ┌─────────────┴─────────────┐
                 │                           │
                 ▼                           ▼
        ┌─────────────────┐        ┌─────────────────┐
        │   AccountDAO    │        │ TransactionDAO  │
        │   Data Access   │        │   Data Access   │
        └────────┬────────┘        └────────┬────────┘
                 │                          │
                 └────────────┬─────────────┘
                              │
                              ▼
                    ┌─────────────────────┐
                    │        MySQL        │
                    │      Database       │
                    └─────────────────────┘
```

### Architecture Layers

**Dashboard**

* Handles console-based user interaction.
* Displays menus and collects user input.

**Service Layer**

* Contains business logic.
* Validates operations before interacting with the database.

**DAO Layer**

* Handles database operations.
* Uses JDBC to execute SQL queries.

**Model Layer**

* Represents application entities such as accounts and transactions.

**Utility Layer**

* Provides reusable functionality such as database connections, password hashing, account-number generation, and validation.

---

# 📂 Project Structure

```text
src/
│
├── DAO/
│   ├── AccountDAO.java
│   └── TransactionDAO.java
│
├── Dashboard/
│   └── Dashboard.java
│
├── Model/
│   ├── Account.java
│   └── Transaction.java
│
├── Service/
│   └── BankService.java
│
├── Util/
│   ├── ConnectionManager.java
│   ├── NumberGenerator.java
│   └── PasswordUtil.java
│
└── Main.java
```

---

# 💳 Banking Operations

## 👤 Account Management

Users can:

* Create an account
* Login
* View account details
* Update account name
* Update email
* Change password
* Close their account

---

## 💰 Deposit

Users can deposit money into their account.

```text
User
 │
 ▼
Enter Amount
 │
 ▼
Validate Amount
 │
 ▼
Update Account Balance
 │
 ▼
Create Transaction Record
 │
 ▼
Display Updated Balance
```

---

## 💸 Withdrawal

The application validates the withdrawal amount and checks whether the account has sufficient balance.

```text
Enter Amount
     │
     ▼
Validate Amount
     │
     ▼
Check Balance
     │
 ┌───┴────┐
 │        │
Enough   Insufficient
 │        │
 ▼        ▼
Withdraw  Reject
 │
 ▼
Create Transaction Record
```

---

# 🔄 Money Transfer

Money transfers are handled using a **database transaction**.

The transfer consists of two operations:

1. Debit money from the sender.
2. Credit money to the receiver.

Both operations must succeed.

```text
             Start Transaction
                    │
                    ▼
             Validate Receiver
                    │
                    ▼
              Check Balance
                    │
                    ▼
             Debit Sender
                    │
                    ▼
            Credit Receiver
                    │
                    ▼
          Create Transactions
                    │
              ┌─────┴─────┐
              │           │
           Success       Failure
              │           │
              ▼           ▼
           COMMIT      ROLLBACK
```

The application uses:

```java
connection.setAutoCommit(false);
```

and completes the operation using:

```java
connection.commit();
```

If an error occurs:

```java
connection.rollback();
```

This prevents an inconsistent state where money is deducted from the sender but not credited to the receiver.

---

# 📋 Transaction History

The application maintains transaction records for banking operations.

A transaction contains information such as:

```text
Transaction ID
Transaction Type
Description
Date / Time
Amount
```

Example:

```text
ID          TYPE       DESCRIPTION              DATE                    AMOUNT
------------------------------------------------------------------------------------------------
566204981   CREDIT     ATM DEPOSIT              2026-09-09T23:32:42     1000.00
688460976   DEBIT      MONEY TRANSFER           2026-09-09T23:23:21       10.00
```

Transaction records are associated with the corresponding bank account, allowing users to view their own transaction history.

---

# 🔐 Authentication & Password Security

Passwords are **not stored as plain text**.

The application uses **BCrypt hashing** to securely store passwords.

```text
User Password
      │
      ▼
 BCrypt Hashing
      │
      ▼
Stored Password Hash
      │
      ▼
    MySQL
```

During login, the entered password is verified against the stored BCrypt hash.

---

# 📧 Email Validation

The application validates the email address provided during account creation or email updates.

Invalid email formats are rejected before the account information is stored.

---

# 🔒 Environment Variables

Database credentials are kept outside the source code using environment variables.

The application uses:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
```

Example:

```text
DB_URL=jdbc:mysql://localhost:3306/bank_management
DB_USERNAME=root
DB_PASSWORD=your_password
```

> ⚠️ Never commit real database credentials or `.env` files containing secrets to GitHub.

---

# 🗄️ Database

The application uses **MySQL** as its relational database.

The database stores information related to:

```text
Accounts
Transactions
```

Typical account information includes:

```text
Account ID
Account Number
Account Holder Name
Email
Password Hash
Balance
```

Transaction information includes:

```text
Transaction ID
Account ID
Transaction Type
Amount
Description
Created At
```

---

# 📦 Model Layer

## `Account.java`

Represents a bank account and contains account-related information.

```text
Account
├── ID
├── Account Number
├── Account Holder Name
├── Email
├── Password Hash
└── Balance
```

## `Transaction.java`

Represents a banking transaction.

```text
Transaction
├── Transaction ID
├── Account ID
├── Type
├── Amount
├── Description
└── Created At
```

---

# 🗄️ DAO Layer

The DAO layer is responsible for communication with the MySQL database.

### `AccountDAO.java`

Handles account-related database operations such as:

```text
Create Account
Find Account
Update Account
Update Balance
Change Password
Delete Account
```

### `TransactionDAO.java`

Handles transaction-related database operations such as:

```text
Create Transaction
Fetch Transaction History
Store Transaction Details
```

Using DAO classes keeps database logic separate from business logic.

---

# ⚙️ Service Layer

### `BankService.java`

The Service Layer contains the application's core business logic.

Examples include:

```text
Register Account
Login
Deposit
Withdraw
Transfer Money
View Account Details
Update Account
Change Password
Close Account
View Transaction History
```

The Service Layer acts as the bridge between the Dashboard and DAO layer.

---

# 🔧 Utility Classes

### `ConnectionManager.java`

Creates and manages JDBC connections to the MySQL database.

Database credentials are loaded from environment variables.

### `PasswordUtil.java`

Provides password hashing and password verification using BCrypt.

### `NumberGenerator.java`

Generates account numbers for newly created accounts.

---

# 📸 Screenshots

### 👤 Account Creation

<img src="screenshots/account-creation.png" width="700">

### 🏦 Dashboard

<img src="screenshots/dashboard.png" width="700">

### 📋 Account Details

<img src="screenshots/account-details.png" width="700">

### 💰 Deposit

<img src="screenshots/deposit.png" width="700">

---

# 🧠 Concepts Demonstrated

## Java

* Object-Oriented Programming
* Encapsulation
* Classes & Objects
* Constructors
* Methods
* Interfaces
* Exception Handling
* Collections

## JDBC

* `Connection`
* `PreparedStatement`
* `ResultSet`
* `executeQuery()`
* `executeUpdate()`
* JDBC connection management
* Parameterized SQL queries

## MySQL

* Relational database design
* CRUD operations
* SQL queries
* Primary keys
* Foreign keys
* Database constraints
* Transactions
* `COMMIT`
* `ROLLBACK`

## Backend Architecture

* Layered Architecture
* DAO Pattern
* Service Layer
* Model Layer
* Separation of Concerns

## Security

* BCrypt password hashing
* Prepared statements
* Environment variables
* Input validation

---

# ▶️ How to Run

## 1. Clone the Repository

```bash
git clone https://github.com/AnkitSirsatia/Bank_Management_System.git
```

```bash
cd Bank_Management_System
```

## 2. Configure MySQL

Make sure MySQL Server is installed and running.

Create the database:

```sql
CREATE DATABASE bank_management;
```

Select it:

```sql
USE bank_management;
```

Create the required tables using the SQL structure required by the project.

---

## 3. Configure Environment Variables

Set the following environment variables:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
```

Example:

```text
DB_URL=jdbc:mysql://localhost:3306/bank_management
DB_USERNAME=root
DB_PASSWORD=your_password
```

---

## 4. Add Dependencies

Make sure the project has the required:

* MySQL JDBC Driver
* BCrypt library
* Email Validator

---

## 5. Run the Application

Run:

```text
Main.java
```

The console application will display the main banking menu.

---

# 📋 Application Flow

```text
                 Start Application
                         │
                         ▼
                    Main Menu
                         │
              ┌──────────┴──────────┐
              │                     │
              ▼                     ▼
       Create Account              Login
                                    │
                                    ▼
                           Account Dashboard
                                    │
          ┌──────────┬──────────────┼─────────────┐
          │          │              │             │
          ▼          ▼              ▼             ▼
       Deposit    Withdraw       Transfer    Transaction
                                               History
          │          │              │             │
          └──────────┴──────────────┼─────────────┘
                                    │
                                    ▼
                              MySQL Database
```

---

# 🚧 Future Improvements

* [ ] Convert the application into a Spring Boot REST API
* [ ] Replace JDBC DAO implementation with Spring Data JPA
* [ ] Add JWT-based authentication
* [ ] Add JUnit and integration testing
* [ ] Add structured logging
* [ ] Improve input validation and exception handling
* [ ] Use `BigDecimal` for monetary calculations
* [ ] Add stronger database constraints
* [ ] Add API documentation using Swagger/OpenAPI
* [ ] Build a web frontend
* [ ] Add mobile/web-based banking interface
* [ ] Deploy the backend to the cloud

---

# 🎯 Project Objective

The main objective of this project was to build a practical Java backend application while understanding how different backend components work together.

```text
Java
  ↓
JDBC
  ↓
DAO
  ↓
Service Layer
  ↓
MySQL
```

The project provided hands-on experience with:

* Backend architecture
* Database connectivity
* SQL
* Authentication
* Password security
* CRUD operations
* Database transactions
* Business logic
* Data access patterns

---

# 👨‍💻 Author

**Ankit Sirsatia**

GitHub:
https://github.com/AnkitSirsatia

---

# 📄 License

This project was created for **educational and learning purposes**.
