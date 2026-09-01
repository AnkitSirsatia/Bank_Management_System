package Dashboard;

import DAO.AccountDAO;
import Model.Account;
import Service.BankService;
import Util.PasswordUtil;

import java.util.Scanner;

public class Dashboard {
    private final  BankService bankService;
    private final AccountDAO  accountDAO = new AccountDAO();
    private Account account;
    private final Scanner scanner;

    public Dashboard(BankService bankService,Scanner scanner){
        this.bankService=bankService;
        this.scanner = scanner;
    }


    public boolean login(){
        System.out.print("Enter Account Number : ");
        int loginAccountNumber = scanner.nextInt();
        System.out.print("Enter Password : ");
        String loginPassword = scanner.next();
        if(bankService.login(loginAccountNumber, loginPassword)){
            this.account = accountDAO.findAccountByNumber(loginAccountNumber);
            return true;
        }
        return false;
    }

    public void createAccount(){
        System.out.println("\n========================================");
        System.out.println("          CREATE NEW ACCOUNT");
        System.out.println("========================================");

        System.out.print("Enter Account Holder Name : ");
        String createAccountHolderName = scanner.nextLine();

        System.out.print("Enter Email : ");
        String createEmail = scanner.nextLine();

        System.out.print("Create Password : ");
        String createPassword = scanner.nextLine();

        System.out.println("========================================");
        bankService.registerAccount(createAccountHolderName, createEmail, createPassword);
    }

    public void withdraw(){
        System.out.println("\n========================================");
        System.out.println("          WITHDRAW");
        System.out.println("========================================");
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        if(amount>0) {
            if (bankService.withdraw(account, amount, account.getPassword())) {
                System.out.println("Withdraw " + amount + " successfully ");
            } else {
                System.out.println("Withdraw failed");
            }
        }else {
            System.out.println("Enter Invalid Amount");
        }
    }

    public void deposit(){
        System.out.println("\n========================================");
        System.out.println("          DEPOSIT");
        System.out.println("========================================");
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        if(amount>0) {
            if (bankService.deposit(account, amount, account.getPassword())) {
                System.out.println("Deposit " + amount + " successfully ");
            } else {
                System.out.println("Deposit failed");
            }
        }else {
            System.out.println("Enter Invalid Amount");
        }
    }

    public void showBalance(){
        System.out.println("\n========================================");
        System.out.println("          BALANCE");
        System.out.println("========================================");
        System.out.println("Available Balance "+bankService.getBalance(account.getAccountNumber()));

    }

    public void closeAccount(){
        System.out.println("\n========================================");
        System.out.println("          CLOSED ACCOUNT");
        System.out.println("========================================");
        System.out.println("Verified Account Number: ");
        int accountNo = scanner.nextInt();
        System.out.println("Verified Password: ");
        String passwd = scanner.next();
        if(bankService.closeAccount(accountNo,passwd)){
            account.setId(0);
            account.setAccountNumber(0);
            account.setAccountHolderName(null);
            account.setBalance(0.0);
            account.setEmail(null);
            account.setPassword(null);
            System.out.println("Account Closed Successfully");
        }else{
            System.out.println("Data not Found!");
        }
    }

    public void changePassword(){
        System.out.print("Enter Old Password: ");
        String oldPassword = scanner.next();
        if(PasswordUtil.checkPassword(oldPassword,account.getPassword())){
            String newPassword;String confirmPassword;
            do {
                System.out.println("Enter New Password");
                newPassword = scanner.next();
                System.out.println("Enter Confirm Password");
                confirmPassword = scanner.next();
            }while (!newPassword.equals(confirmPassword));
            String setPasswd = PasswordUtil.hashPassword(newPassword);
            if(bankService.reSetPassword(account.getAccountNumber(),oldPassword,setPasswd)){
                account.setPassword(setPasswd);
                System.out.println("Password Changed Successfully");
            }else{
                System.out.println("Password Not Changed");
            }
        }
    }

    public void transferMoney(){
        System.out.println("\n========================================");
        System.out.println("          TRANSFER MONEY");
        System.out.println("========================================");
        System.out.println("Receivers Account Number: ");
        int receiverAccountNo = scanner.nextInt();
        Account receiverAccount = accountDAO.findAccountByNumber(receiverAccountNo);
        if( receiverAccount != null){
            System.out.println("\n====================================");
            System.out.println("          ACCOUNT DETAILS");
            System.out.println("====================================");
            System.out.println("Account ID      : " + receiverAccount.getId());
            System.out.println("Account Number  : " + receiverAccount.getAccountNumber());
            System.out.println("Holder Name     : " + receiverAccount.getAccountHolderName());
            System.out.println("====================================");
            System.out.println();
            System.out.println("Confirm Account : Press Y/N");
            if(scanner.next().equalsIgnoreCase("Y")){
                System.out.println("Enter amount: ");
                double amount = scanner.nextDouble();
                if(bankService.transferMoney(account.getAccountNumber(),receiverAccountNo,amount)){
                    account.setBalance(account.getBalance()-amount);
                }
            }
        }else {
            System.out.println("Account not found");

        }

    }

    public void accountDetails(){
        if(bankService.getAccountDetails(account.getAccountNumber())){
            System.out.println();
        }else {
            System.out.println("Details not fetched");
        }
    }

    public void showMenu() {

        while (true) {

            System.out.println("\n========================================");
            System.out.println("          🏦 BANK MANAGEMENT");
            System.out.println("========================================");
            System.out.println("Welcome, " + account.getAccountHolderName());
            System.out.println("Account No: " + account.getAccountNumber());
            System.out.println("----------------------------------------");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Closed Account");
            System.out.println("5. Transaction History");
            System.out.println("6. Account Details");
            System.out.println("7. Change Password");
            System.out.println("9. Transfer Money");
            System.out.println("8. Logout");
            System.out.println("========================================");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {

                case 1:
                    showBalance();
                    break;

                case 2:
                    deposit();
                    break;

                case 3:
                    withdraw();
                    break;

                case 4:
                    closeAccount();
                    break;

                case 5:
                    //transactionHistory();
                    break;

                case 6:
                    accountDetails();
                    break;

                case 7:
                    changePassword();
                    break;

                case 9:
                    transferMoney();
                    break;

                case 8:
                    System.out.println("Logged out successfully.");
                    return;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}

