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

    

    public void register() throws SQLException {
        

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
            
            throw new RuntimeException("SQL Error during registration", e);
        }
    }

    public String login() {
        

        System.out.println("Enter Email : ");
        String email = sc.nextLine();
        System.out.println("Enter Password : ");
        String password = sc.nextLine();

        String query = "select * from users where email = ? and password = ?";

        
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
