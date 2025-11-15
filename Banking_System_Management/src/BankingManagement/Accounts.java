package BankingManagement;

import java.sql.*;
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
        try{
            PreparedStatement smt = con.prepareStatement(query);
            smt.setString(1,email);
            ResultSet res = smt.executeQuery();
            if (res.next()) {
                return true;
            }else{
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    private long generateAccountNumber(){
        try{
            Statement smt = con.createStatement();
            String query ="select account_number from account order by account_number desc limit 1";
            ResultSet res = smt.executeQuery(query);
            if(res.next()){
                long acc = res.getLong("account_number");
                return acc+1;
            }else {
                return 10000100;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 10000100;
    }
    public long getAccountNumber(String email){
        try{
            String query = "select account_number from account where email = ?";
            PreparedStatement smt = con.prepareStatement(query);
            smt.setString(1,email);
            ResultSet res = smt.executeQuery();
            if (res.next()){
                return res.getLong("account_number");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        throw new RuntimeException("Account Doesn't Exist !!!");
    }
    public long open_Account(String email){
        if (!account_exist(email)){
            String query = "insert into account(account_number,full_name,email,balance,security_pin) values (?,?,?,?,?)";
            sc.nextLine();
            System.out.println("Enter full Name : ");
            String full_name = sc.nextLine();
            System.out.println("Enter Initial Amount : ");
            Double balance = sc.nextDouble();
            System.out.println("Enter Security Pin : ");
            String security_pin = sc.nextLine();
            try{
                long account_number = generateAccountNumber();
                PreparedStatement smt = con.prepareStatement(query);
                smt.setLong(1,account_number);
                smt.setString(2,full_name);
                smt.setString(3,email);
                smt.setDouble(4,balance);
                smt.setString(5,security_pin);
                int rowAffected = smt.executeUpdate();
                if (rowAffected>0){
                    return account_number;
                }else {
                    throw new  RuntimeException("Account Creation Failed");
                }
            }catch (SQLException e){
                e.printStackTrace();
            }

        }
        throw new RuntimeException("Account Already Exist !!");
    }
}
