package BankingManagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class BankingApp {
    private static final String password = "syedrasheed786";
    private static final String username = "root";
    private static final String url = "jdbc:mysql://127.0.0.1:3306/banking_system";

    public static void main(String[] args) throws ClassNotFoundException {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }

        try {
            Connection con = DriverManager.getConnection(url, username, password);
            Scanner sc = new Scanner(System.in);
            User user = new User(con, sc);
            Accounts accounts = new Accounts(con, sc);
            AccountManager accountManager = new AccountManager(con, sc);
            long account_number;
            String email;
            System.out.println("---- WELCOME TO BANKING SYSTEM ----");
            System.out.println();
            System.out.println("1 . Register");
            System.out.println("2 . Login ");
            System.out.println("3 . Exit");
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    user.register();
                    break;
                case 2:
                    email = user.login();
                    if (email != null) {
                        System.out.println();
                        System.out.println("User Logged In");
                        if (!accounts.account_exist(email)) {
                            System.out.println();
                            System.out.println("1 . Open a new Bank Account ");
                            System.out.println("2 . Exit");
                            if (sc.nextInt() == 1) {
                                account_number = accounts.open_Account(email);
                                System.out.println("Account Opened Successfully");
                                System.out.println("Your Account Number is " + account_number);
                            } else {
                                break;
                            }
                        }
                        account_number = accounts.getAccountNumber(email);
                        int choice1 = 0;
                        while (choice1 != 5) {
                            System.out.println();
                            System.out.println("1. Debit Money");
                            System.out.println("2. Credit Money");
                            System.out.println("3. Transfer Money");
                            System.out.println("4. Check Balance");
                            System.out.println("5. Log Out");

                            try {
                                choice1 = sc.nextInt();
                                switch (choice1) {
                                    case 1:
                                        accountManager.debit_money(account_number);
                                        break;
                                    case 2:
                                        accountManager.credit_money(account_number);
                                        break;
                                    case 3:
                                        accountManager.transferMoney(account_number);
                                        break;
                                    case 4:
                                        accountManager.getBalance(account_number);
                                        break;
                                    case 5:
                                        break;
                                    default:
                                        System.out.println("Enter Valid Option");
                                        break;
                                }
                            }catch (Exception e) {
                                System.out.println("Please enter a valid number!");
                                sc.nextLine();
                                choice1 = 0;
                            }
                        }


                        }else{
                            System.out.println("Incorrect Email or Password !!");
                        }
                        break;
                        case 3:
                            System.out.println("THANK YOU FOR USING BANKING SYSTEM!!!!");
                            System.out.println("Exiting System");
                            int i = 0;
                            while (i <= 4) {
                                System.out.print(".");
                                Thread.sleep(400);
                                i++;
                            }

                        default:
                            System.out.println("Enter Valid Option !!!");
                            break;

                    }

            }
        catch(SQLException e){
                System.out.println(e.getErrorCode());
            } catch(InterruptedException e){
                throw new RuntimeException(e);
            }
        }
    }
