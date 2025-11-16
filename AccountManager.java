package BankingManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class AccountManager {
    private Connection con;
    private Scanner sc;

    AccountManager(Connection con , Scanner sc){
        this.con = con;
        this.sc = sc;
    }

    public void debit_money(long account_number) throws SQLException {

        System.out.println("Enter Amount : ");
        long amount;
        try {
            amount = sc.nextLong();
        } catch (InputMismatchException e) {
            System.out.println("Invalid amount entered.");
            sc.nextLine();
            return;
        }
        //  Clear buffer after sc.nextLong()
        sc.nextLine();

        System.out.println("Enter Security Pin : ");
        String security_pin = sc.nextLine();

        try {
            con.setAutoCommit(false);
            String selectQuery = "select balance from account where account_number = ? and security_pin = ? ";

            //  Use try-with-resources for PreparedStatement and nested ResultSet
            try (PreparedStatement selectSmt = con.prepareStatement(selectQuery)) {
                selectSmt.setLong(1,account_number);
                selectSmt.setString(2,security_pin);

                try (ResultSet res = selectSmt.executeQuery()) {
                    if(res.next()){
                        double currentBalance = res.getDouble("balance");
                        if(currentBalance >= amount){
                            String debit_query = "update account set balance = balance - ? where account_number = ?";

                            //  Use separate PreparedStatement (debitSmt) for the update
                            try (PreparedStatement debitSmt = con.prepareStatement(debit_query)) {
                                debitSmt.setDouble(1,amount);
                                debitSmt.setLong(2,account_number);

                                int rowsAffected = debitSmt.executeUpdate();

                                if (rowsAffected > 0){
                                    System.out.println("Rs : "+amount+" debited Successfully");
                                    con.commit();
                                } else {
                                    System.out.println("Debited Failed !!!!");
                                    con.rollback();
                                }
                            }
                        } else {
                            System.out.println("Insufficient Balance !!");
                        }
                    } else {
                        System.out.println("Invalid Account Number or Security Pin.");
                    }
                }
            }
        } catch (SQLException e){
            con.rollback();
            e.printStackTrace();
        } finally {
            // Ensure auto-commit is restored
            con.setAutoCommit(true);
        }
    }

    public void credit_money(long account_number) throws SQLException {

        System.out.println("Enter Amount : ");
        long amount;
        try {
            amount = sc.nextLong();
        } catch (InputMismatchException e) {
            System.out.println("Invalid amount entered.");
            sc.nextLine();
            return;
        }
        //  Clear buffer after sc.nextLong()
        sc.nextLine();

        System.out.println("Enter Security Pin : ");
        String security_pin = sc.nextLine();

        try {
            con.setAutoCommit(false);
            String selectQuery = "select * from account where account_number = ? and security_pin = ? ";

            //  Use try-with-resources for PreparedStatement and nested ResultSet
            try (PreparedStatement selectSmt = con.prepareStatement(selectQuery)) {
                selectSmt.setLong(1,account_number);
                selectSmt.setString(2,security_pin);

                try (ResultSet res = selectSmt.executeQuery()) {
                    if(res.next()){
                        String credit_query = "update account set balance = balance + ? where account_number = ?";

                        //  Use separate PreparedStatement (creditSmt) for the update
                        try (PreparedStatement creditSmt = con.prepareStatement(credit_query)) {
                            creditSmt.setDouble(1,amount);
                            creditSmt.setLong(2,account_number);

                            int rowsAffected = creditSmt.executeUpdate();

                            if (rowsAffected > 0){
                                System.out.println("Rs : "+amount+" Credited Successfully");
                                con.commit();
                            } else {
                                System.out.println("Credited Failed !!!!");
                                con.rollback();
                            }
                        }
                    } else {
                        System.out.println("Invalid Account Number or Security Pin.");
                    }
                }
            }
        } catch (SQLException e){
            con.rollback();
            e.printStackTrace();
        } finally {
            // Ensure auto-commit is restored
            con.setAutoCommit(true);
        }
    }

    public void getBalance (long account_number) {

        System.out.println("Enter your Security Pin : ");
        String security_pin = sc.nextLine();

        try{
            String query = "select balance from account where account_number = ? and security_pin = ?";

            // Use try-with-resources for PreparedStatement and nested ResultSet
            try (PreparedStatement smt = con.prepareStatement(query)) {
                smt.setLong(1,account_number);
                smt.setString(2,security_pin);

                try (ResultSet res = smt.executeQuery()) {
                    if (res.next()){
                        // Changed getInt to getDouble for balance consistency
                        double balance = res.getDouble("balance");
                        System.out.println("Balance : "+balance);
                    } else {
                        System.out.println("Invalid Security Pin.");
                    }
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void transferMoney(long senderAccountNumber){

        System.out.println("Enter Receiver Account Number : ");
        long receiverAccountNumber;
        try {
            receiverAccountNumber = sc.nextLong();
        } catch (InputMismatchException e) {
            System.out.println("Invalid receiver account number entered.");
            sc.nextLine();
            return;
        }
        //  Clear buffer after sc.nextLong()
        sc.nextLine();

        if (!isAccountExists(receiverAccountNumber)) {
            System.out.println("Receiver account " + receiverAccountNumber + " does not exist!");
            return;
        }

        if (senderAccountNumber == receiverAccountNumber) {
            System.out.println("Cannot transfer to your own account!");
            return;
        }

        System.out.println("Enter Amount : ");
        double amount;
        try {
            amount = sc.nextDouble();
        } catch (InputMismatchException e) {
            System.out.println("Invalid amount entered.");
            sc.nextLine();
            return;
        }
        //  Clear buffer after sc.nextDouble()
        sc.nextLine();

        System.out.println("Enter Security Pin : ");
        String security_pin = sc.nextLine();

        try {
            con.setAutoCommit(false);
            if (senderAccountNumber != 0 && receiverAccountNumber != 0) {
                String senderBalanceQuery = "select balance from account where account_number = ? and security_pin = ?";

                //  Use try-with-resources for PreparedStatement and nested ResultSet
                try (PreparedStatement smt = con.prepareStatement(senderBalanceQuery)) {
                    smt.setLong(1, senderAccountNumber);
                    smt.setString(2, security_pin);

                    try (ResultSet res = smt.executeQuery()) {
                        if (res.next()) {
                            double senderCurrentBalance = res.getDouble("balance");
                            if (senderCurrentBalance >= amount) {

                                String debit_query = "update account set balance = balance - ? where account_number = ?";
                                String credit_query = "update account set balance = balance + ? where account_number = ?";

                                //  Use try-with-resources for two PreparedStatements
                                try (PreparedStatement debitSmt = con.prepareStatement(debit_query);
                                     PreparedStatement creditSmt = con.prepareStatement(credit_query)) {

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
                                }
                            } else {
                                System.out.println("Insufficient Balance !!");
                            }
                        } else {
                            System.out.println("Wrong Account Number or Invalid Security Pin !!!!");
                        }
                    }
                }
            } else {
                System.out.println("Invalid Account Number");
            }
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw new RuntimeException(e);
        } finally {
            try {
                // Ensure auto-commit is restored
                con.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    
    private boolean isAccountExists(long accountNumber) {
        String query = "SELECT account_number FROM account WHERE account_number = ?";

        //  Use try-with-resources for PreparedStatement and nested ResultSet
        try (PreparedStatement smt = con.prepareStatement(query)) {
            smt.setLong(1, accountNumber);

            try (ResultSet res = smt.executeQuery()) {
                return res.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
