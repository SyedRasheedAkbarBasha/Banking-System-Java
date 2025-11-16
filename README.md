# Banking Management System

A Java-based Banking Management System that automates core banking operations with secure MySQL database integration using JDBC.

---

## 📋 Overview

The Banking Management System is a console-based application designed to streamline essential banking operations such as account management, deposits, withdrawals, fund transfers, and balance inquiries. It ensures secure and reliable transaction management through PIN-based authentication and JDBC transaction handling.

---

## ✨ Features

- **User Authentication**: Secure registration and login system  
- **Account Management**: Create and manage bank accounts with unique account numbers  
- **Transaction Handling**:
  - Debit money (withdraw)  
  - Credit money (deposit)  
  - Transfer funds between accounts  
  - Check account balance  
- **Security**: PIN-based authentication for all transactions  
- **Data Integrity**: JDBC transaction handling with commit/rollback  
- **Error Handling**: Comprehensive exception management  

---

## 🛠️ Technologies Used

- **Java** – Core programming language  
- **JDBC** – Database connectivity  
- **MySQL** – Database management system  
- **Maven** – Dependency management (if applicable)  

---

## 🗄️ Database Schema

### 🏦 Account Table

```sql
CREATE TABLE account (
    account_number BIGINT PRIMARY KEY,
    full_name VARCHAR(50),
    email VARCHAR(50) UNIQUE KEY,
    balance DECIMAL(10,2),
    security_pin CHAR(4)
);
```

### 👤 Users Table

```sql
CREATE TABLE users (
    full_name VARCHAR(255),
    email VARCHAR(255) PRIMARY KEY,
    password VARCHAR(255)
);
```

---

## 🚀 Getting Started

### 📌 Prerequisites
- Java JDK 8 or higher  
- MySQL Server  
- MySQL Connector/J  

---

## ⚙️ Installation & Setup

### 1️⃣ Clone the repository

```bash
git clone https://github.com/yourusername/banking-system-java.git
cd banking-system-java
```

### 2️⃣ Set up the database

```sql
CREATE DATABASE banking_system;
USE banking_system;
```

Execute the SQL schema provided above.

---

### 3️⃣ Configure database connection

Update credentials in your Java file:

```java
private static final String url = "jdbc:mysql://localhost:3306/banking_system";
private static final String username = "your_username";
private static final String password = "your_password";
```

---

### 4️⃣ Run the application

```bash
javac BankingApp.java
java BankingApp
```

---

## 🧑‍💻 Usage

### 🔐 Registration
New users can register using full name, email, and password.

### 🔑 Login
Registered users can log in using their credentials.

### 🏦 Account Creation
Users can create a bank account with:
- Initial deposit  
- Security PIN  

### 💸 Transactions

- **Debit Money** (with PIN verification)  
- **Credit Money**  
- **Transfer Funds** between accounts  
- **Check Balance**  

---

## 🔒 Security Features

- PIN-based transaction authentication  
- Input validation & exception handling  
- SQL injection protection (Prepared Statements)  
- Transaction rollback on failures  

---

## 🔮 Future Enhancements

- **Data Encryption** for sensitive details  
- **Mini Statements** showing recent history  
- **Loan Management System** with EMI automation  

---

## 🤝 Contributing

Contributions are welcome!  
Please open issues or submit pull requests for improvements.

