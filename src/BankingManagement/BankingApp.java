package BankingManagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class BankingApp {
    private static final String password = "syedrasheed786";
    private static final String username = "root";
    private static final String url = "jdbc:mysql://127.0.0.1:3306/banking_system";

    public static void main(String[] args) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            System.out.println("MySQL JDBC Driver not found.");
            return;
        }

        // 🛠️ FIX 1: Use try-with-resources to ensure Connection and Scanner are closed.
        try (Connection con = DriverManager.getConnection(url, username, password);
             Scanner sc = new Scanner(System.in)) {

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

            int choice = -1;
            try {
                choice = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid number (1, 2, or 3)!");
                sc.nextLine();
                return;
            }
            // 🛠️ FIX 2: Clear buffer after reading choice to allow User.register/login to read string inputs.
            sc.nextLine();

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

                            int accountChoice = -1;
                            try {
                                accountChoice = sc.nextInt();
                            } catch (InputMismatchException e) {
                                System.out.println("Please enter a valid number (1 or 2)!");
                                sc.nextLine();
                                break;
                            }
                            // 🛠️ FIX 3: Clear buffer after reading accountChoice.
                            sc.nextLine();

                            if (accountChoice == 1) {
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
                                // 🛠️ FIX 4: Clear buffer after reading choice1.
                                sc.nextLine();

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
                                        System.out.println("Logging Out...");
                                        break;
                                    default:
                                        System.out.println("Enter Valid Option");
                                        break;
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Please enter a valid number!");
                                sc.nextLine();
                                choice1 = 0;
                            } catch (SQLException e) {
                                System.out.println("An error occurred during transaction: " + e.getMessage());
                            }
                        }

                    } else {
                        System.out.println("Incorrect Email or Password !!");
                    }
                    break; // 🛠️ FIX 5: Added break to prevent fall-through to case 3/default.
                case 3:
                    System.out.println("THANK YOU FOR USING BANKING SYSTEM!!!!");
                    System.out.print("Exiting System");
                    int i = 0;
                    while (i <= 4) {
                        System.out.print(".");
                        Thread.sleep(400);
                        i++;
                    }
                    break;

                default:
                    System.out.println("Enter Valid Option !!!");
                    break;
            }

        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("System exit interrupted", e);
        }
    }
}