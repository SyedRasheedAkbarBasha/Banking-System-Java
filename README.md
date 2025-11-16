# Banking Management System

A Java-based Banking Management System that automates core banking operations with secure MySQL database integration using JDBC.

## 📋 Overview

The Banking Management System is a console-based application designed to streamline essential banking operations such as account management, deposits, withdrawals, fund transfers, and balance inquiries. It ensures secure and reliable transaction management through PIN-based authentication and JDBC transaction handling.

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

## 🛠️ Technologies Used

- **Java** - Core programming language
- **JDBC** - Database connectivity
- **MySQL** - Database management system
- **Maven** - Dependency management (if applicable)

## 🗄️ Database Schema

### Account Table


CREATE TABLE account(
    account_number BIGINT PRIMARY KEY,
    full_name VARCHAR(50),
    email VARCHAR(50) UNIQUE KEY,
    balance DECIMAL(10,2),
    security_pin CHAR(4)
);

Users Table

CREATE TABLE users(
    full_name VARCHAR(255),
    email VARCHAR(255) PRIMARY KEY,
    password VARCHAR(255)
);

## Getting Started
Prerequisites
Java JDK 8 or higher

## MySQL Server

MySQL Connector/J

## Installation & Setup

## Clone the repository

git clone https://github.com/yourusername/banking-system-java.git
cd banking-system-java

## Set up the database

CREATE DATABASE banking_system;
USE banking_system;
Execute the SQL schema provided above.

## Configure database connection
Update the database credentials in the Java files:


private static final String url = "jdbc:mysql://localhost:3306/banking_system";
private static final String username = "your_username";
private static final String password = "your_password";

## Run the application

javac BankingApp.java
java BankingApp

## Usage
- Registration: New users can register with name, email, and password

- Login: Registered users can login with credentials

- Account Creation: Create a bank account with initial deposit and security PIN

## Transactions:

- Debit money with PIN verification

- Credit money to account

- Transfer funds to other accounts

- Check current balance

## Security Features
- PIN-based authentication for transactions

- Input validation and exception handling

- SQL injection prevention using prepared statements

- Transaction rollback on failure

## Future Enhancements
- Data Encryption: Secure sensitive data using encryption algorithms

- Mini Statements: View recent transaction history

- Loan Management: Automated loan approval and EMI tracking

## Contributing
- Contributions are welcome! Please feel free to submit pull requests or open issues for suggestions.

