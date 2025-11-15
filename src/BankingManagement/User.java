package BankingManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
    private Connection con;
    private Scanner sc;

    public User(Connection con, Scanner sc) {
        this.con = con;
        this.sc = sc;
    }

    // Note: Removed the redundant main method as it serves no purpose here.

    public void register() throws SQLException {
        // 🛠️ FIX 1: Removed sc.nextLine() here; buffer clearing is handled by the caller (BankingApp).

        System.out.println("Enter Full Name : ");
        String full_name = sc.nextLine();

        System.out.println("Enter Email ID : ");
        String email = sc.nextLine();

        System.out.println("Enter Password : ");
        String password = sc.nextLine();

        if(isEmail_exists(email)){
            System.out.println("Email is Already Exists !!! Please Login To Continue....");
            return;
        }

        String query = "insert into users (full_name,email,password) values(?,?,?)";

        // 🛠️ FIX 2: Use try-with-resources for PreparedStatement.
        try (PreparedStatement smt = con.prepareStatement(query)) {
            smt.setString(1,full_name);
            smt.setString(2,email);
            smt.setString(3,password);

            int rowsAffected = smt.executeUpdate();

            if(rowsAffected > 0){
                System.out.println("Registered SuccessFully !!!");
            } else {
                System.out.println("Registration Failed !!!");
            }
        } catch (SQLException e) {
            // Note: Keeping RuntimeException throw as in original logic for SQL failure.
            throw new RuntimeException("SQL Error during registration", e);
        }
    }

    public String login() {
        // 🛠️ FIX 1: Removed sc.nextLine() here; buffer clearing is handled by the caller (BankingApp).

        System.out.println("Enter Email : ");
        String email = sc.nextLine();
        System.out.println("Enter Password : ");
        String password = sc.nextLine();

        String query = "select * from users where email = ? and password = ?";

        // 🛠️ FIX 2: Use try-with-resources for PreparedStatement and nested ResultSet.
        try (PreparedStatement smt = con.prepareStatement(query)) {
            smt.setString(1,email);
            smt.setString(2,password);

            try (ResultSet res = smt.executeQuery()) {
                if (res.next()){
                    return email;
                } else {
                    return null;
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
            return null; // Return null on SQL error
        }
    }

    public boolean isEmail_exists(String email){
        String query = "select * from users where email = ?";

        // 🛠️ FIX 2: Use try-with-resources for PreparedStatement and nested ResultSet.
        try (PreparedStatement smt = con.prepareStatement(query)) {
            smt.setString(1,email);

            try (ResultSet res = smt.executeQuery()) {
                if(res.next()){
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}