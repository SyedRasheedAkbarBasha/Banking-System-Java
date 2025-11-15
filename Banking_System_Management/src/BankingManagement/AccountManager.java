package BankingManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {
    private Connection con;
    private Scanner sc;

    AccountManager(Connection con , Scanner sc){
        this.con = con;
        this.sc = sc;
    }
    public void debit_money(long account_number) throws SQLException {
        sc.nextLine();
        System.out.println("Enter Amount : ");
        long amount = sc.nextLong();
        System.out.println("Enter Security Pin : ");
        String security_pin = sc.nextLine();

        try{
            con.setAutoCommit(false);
            String query = "select * from account where account_number = ? and security_pin = ? ";
            PreparedStatement smt = con.prepareStatement(query);
            smt.setLong(1,account_number);
            smt.setString(2,security_pin);
            ResultSet res = smt.executeQuery();
            if(res.next()){
                double currentBalance = res.getDouble("balance");
                if(currentBalance>=amount){
                    String debit_query = "update account set balance = balance - ? where account_number = ?";
                    PreparedStatement debitSmt = con.prepareStatement(debit_query);
                    smt.setDouble(1,amount);
                    smt.setLong(2,account_number);
                    int rowsAffected = debitSmt.executeUpdate();
                    if (rowsAffected>0){
                        System.out.println("Rs : "+amount+"debited Successfully");
                        con.commit();
                        con.setAutoCommit(true);
                    }
                    else {
                        System.out.println("Debited Failed !!!!");
                        con.rollback();
                    }
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void credit_money(long account_number) throws SQLException {
        sc.nextLine();
        System.out.println("Enter Amount : ");
        long amount = sc.nextLong();
        System.out.println("Enter Security Pin : ");
        String security_pin = sc.nextLine();

        try{
            con.setAutoCommit(false);
            String query = "select * from account where account_number = ? and security_pin = ? ";
            PreparedStatement smt = con.prepareStatement(query);
            smt.setLong(1,account_number);
            smt.setString(2,security_pin);
            ResultSet res = smt.executeQuery();
            if(res.next()){
                double currentBalance = res.getDouble("balance");

                    String credit_query = "update account set balance = balance + ? where account_number = ?";
                    smt.setDouble(1,amount);
                    smt.setLong(2,account_number);
                    int rowsAffected = smt.executeUpdate();
                    if (rowsAffected>0){
                        System.out.println("Rs : "+amount+"Credited Successfully");
                        con.commit();
                        con.setAutoCommit(true);
                    }
                    else {
                        System.out.println("Credited Failed !!!!");
                        con.rollback();
                    }
                }

        }catch (SQLException e){
            e.printStackTrace();
        }


    }

    public void getBalance (long account_number) throws SQLException{

        sc.nextLine();
        System.out.println("Enter your Security Pin : ");
        String security_pin = sc.nextLine();

        try{
            String query = "select balance from account where account_number = ? and security_pin = ?";
            PreparedStatement smt = con.prepareStatement(query);
            smt.setLong(1,account_number);
            smt.setString(2,security_pin);
            ResultSet res = smt.executeQuery();
            if (res.next()){
                int balance = res.getInt("balance");
                System.out.println("Balance : "+balance);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

    }
    public void transferMoney(long senderAccountNumber){
        sc.nextLine();
        System.out.println("Enter Receiver Account Number : ");
        long receiverAccountNumber = sc.nextLong();

        // ERROR 6 FIX: Check if receiver account exists
        if (!isAccountExists(receiverAccountNumber)) {
            System.out.println("Receiver account " + receiverAccountNumber + " does not exist!");
            return;
        }

        // Additional safety check: prevent self-transfer
        if (senderAccountNumber == receiverAccountNumber) {
            System.out.println("Cannot transfer to your own account!");
            return;
        }

        System.out.println("Enter Amount : ");
        double amount = sc.nextDouble();
        sc.nextLine(); // Important: consume the newline character

        System.out.println("Enter Security Pin : ");
        String security_pin = sc.nextLine();

        try {
            con.setAutoCommit(false);
            if (senderAccountNumber != 0 && receiverAccountNumber != 0) {
                String senderBalanceQuery = "select * from account where account_number = ? and security_pin = ?";
                PreparedStatement smt = con.prepareStatement(senderBalanceQuery);
                smt.setLong(1, senderAccountNumber);
                smt.setString(2, security_pin);
                ResultSet res = smt.executeQuery();

                if (res.next()) {
                    double senderCurrentBalance = res.getDouble("balance");
                    if (senderCurrentBalance >= amount) {
                        String debit_query = "update account set balance = balance - ? where account_number = ?";
                        String credit_query = "update account set balance = balance + ? where account_number = ?";

                        PreparedStatement debitSmt = con.prepareStatement(debit_query);
                        PreparedStatement creditSmt = con.prepareStatement(credit_query);

                        debitSmt.setDouble(1, amount);
                        debitSmt.setLong(2, senderAccountNumber);
                        creditSmt.setDouble(1, amount);
                        creditSmt.setLong(2, receiverAccountNumber);

                        int rowAffected1 = debitSmt.executeUpdate();
                        int rowAffected2 = creditSmt.executeUpdate();

                        if (rowAffected1 > 0 && rowAffected2 > 0) {
                            System.out.println("Rs: " + amount + " Transferred Successfully");
                            System.out.println("Transaction Successful !!");
                            con.commit();
                        } else {
                            System.out.println("Transaction Failed !!!");
                            con.rollback();
                        }
                        con.setAutoCommit(true);

                        // Close statements
                        debitSmt.close();
                        creditSmt.close();
                    } else {
                        System.out.println("Insufficient Balance !!");
                        con.setAutoCommit(true);
                    }
                } else {
                    System.out.println("Wrong Account Number or Invalid Security Pin !!!!");
                    con.setAutoCommit(true);
                }
                smt.close();
            } else {
                System.out.println("Invalid Account Number");
            }
        } catch (SQLException e) {
            try {
                con.rollback();
                con.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw new RuntimeException(e);
        }
    }

    // ADD THIS HELPER METHOD TO CHECK IF ACCOUNT EXISTS
    private boolean isAccountExists(long accountNumber) {
        String query = "SELECT account_number FROM account WHERE account_number = ?";
        try {
            PreparedStatement smt = con.prepareStatement(query);
            smt.setLong(1, accountNumber);
            ResultSet res = smt.executeQuery();
            return res.next(); // Returns true if account exists
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
