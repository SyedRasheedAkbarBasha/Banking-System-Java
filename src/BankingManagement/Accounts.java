package BankingManagement;

import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Accounts {

    private  Connection con;
    private Scanner sc;

    public Accounts(Connection con, Scanner sc){
        this.con = con;
        this.sc = sc;
    }

    public boolean account_exist(String email){
        String query = "select account_number from account where email = ? ";

        // 🛠️ FIX 1: Use try-with-resources for PreparedStatement and nested ResultSet
        try (PreparedStatement smt = con.prepareStatement(query)) {
            smt.setString(1,email);

            try (ResultSet res = smt.executeQuery()) {
                return res.next();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    private long generateAccountNumber(){
        // 🛠️ FIX 1: Use try-with-resources for Statement and nested ResultSet
        try (Statement smt = con.createStatement()) {
            String query ="select account_number from account order by account_number desc limit 1";

            try (ResultSet res = smt.executeQuery(query)) {
                if(res.next()){
                    long acc = res.getLong("account_number");
                    return acc+1;
                } else {
                    return 10000100; // Starting account number
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return 10000100;
    }

    public long getAccountNumber(String email){
        String query = "select account_number from account where email = ?";

        // 🛠️ FIX 1: Use try-with-resources for PreparedStatement and nested ResultSet
        try (PreparedStatement smt = con.prepareStatement(query)) {
            smt.setString(1,email);

            try (ResultSet res = smt.executeQuery()) {
                if (res.next()){
                    return res.getLong("account_number");
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        // Original logic throws RuntimeException if account doesn't exist
        throw new RuntimeException("Account Doesn't Exist !!!");
    }

    public long open_Account(String email){
        if (!account_exist(email)){
            String query = "insert into account(account_number,full_name,email,balance,security_pin) values (?,?,?,?,?)";

            // 🛠️ FIX 2: Removed sc.nextLine() here; buffer is cleared in BankingApp before calling register/open.

            System.out.println("Enter full Name : ");
            String full_name = sc.nextLine();

            System.out.println("Enter Initial Amount : ");
            Double balance;
            try {
                balance = sc.nextDouble();
            } catch (InputMismatchException e) {
                System.out.println("Invalid amount entered. Using 0.00 as initial balance.");
                balance = 0.00;
            }
            // 🛠️ FIX 3: Clear buffer after sc.nextDouble() to allow next sc.nextLine() to read the PIN.
            sc.nextLine();

            System.out.println("Enter Security Pin : ");
            String security_pin = sc.nextLine();

            try {
                long account_number = generateAccountNumber();

                // 🛠️ FIX 1: Use try-with-resources for PreparedStatement
                try (PreparedStatement smt = con.prepareStatement(query)) {
                    smt.setLong(1,account_number);
                    smt.setString(2,full_name);
                    smt.setString(3,email);
                    smt.setDouble(4,balance);
                    smt.setString(5,security_pin);

                    int rowAffected = smt.executeUpdate();

                    if (rowAffected > 0){
                        return account_number;
                    } else {
                        throw new  RuntimeException("Account Creation Failed");
                    }
                }
            } catch (SQLException e){
                e.printStackTrace();
            }

        }
        throw new RuntimeException("Account Already Exist !!");
    }
}