package Service;

// Business logic-

/*BankService
registerAccount()
login()
getAccountDetails()
getBalance()
deposit()
withdraw()
transfer()
getTransactionHistory()
updateAccount()
closeAccount()

 */


import DAO.AccountDAO;
import Model.Account;
import Util.ConnectionManager;
import Util.NumberGenerator;
import Util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class BankService {
    private final AccountDAO accountDAO = new AccountDAO();
    private final Scanner scanner;
    public BankService(Scanner scanner){
        this.scanner=scanner;
    }

    public boolean registerAccount(String accountHolderName, String email, String password){
        accountHolderName = accountHolderName.toUpperCase();
        email= email.toUpperCase();
        long id = NumberGenerator.generateIdNumber();
        int accountNumber= NumberGenerator.generateAccountNumber();
        if(accountDAO.createAccount(id,accountNumber,accountHolderName,email,password,0.00)){
            Account account = accountDAO.findAccountByNumber(accountNumber);
            System.out.println("Congratulations,"+account.getAccountHolderName()+ " are register successfully!");
            System.out.println("Account Number : "+account.getAccountNumber()+" Account Id : "+account.getId());
            return true;
        }else{
            return false;
        }
    }


    public boolean getAccount(int accountNumber){
        Account account = accountDAO.findAccountByNumber(accountNumber);
        return account != null;
    }

    public boolean getAccountDetails(int accountNumber){
        Account account = accountDAO.findAccountByNumber(accountNumber);
        if( account != null){
            System.out.println("\n====================================");
            System.out.println("          ACCOUNT DETAILS");
            System.out.println("====================================");
            System.out.println("Account ID      : " + account.getId());
            System.out.println("Account Number  : " + account.getAccountNumber());
            System.out.println("Holder Name     : " + account.getAccountHolderName());
            System.out.println("Email           : " + account.getEmail());
            System.out.println("Balance         : ₹" + account.getBalance());
            System.out.println("====================================");
            System.out.println();
            return true;
        }
        return false;
    }

    public double getBalance(int accountNumber){
        String query = "SELECT balance FROM accounts WHERE account_number = ?";
        double current_balance = 0.0;
        try(Connection connection = ConnectionManager.getConnection();
            PreparedStatement ps = connection.prepareStatement(query)){
            ps.setInt(1,accountNumber);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                current_balance=rs.getDouble("balance");
            }
        }catch(SQLException e){
            System.out.println(e);
        }
        return current_balance;
    }

    public boolean withdraw(Account account,double amount,String password ){
        if(!accountDAO.isSufficient(account.getAccountNumber(),amount)){
            System.out.println("Insufficient Bank Balance");
            return false;
        }
        double newAmount = account.getBalance() - amount;
        if(account.getPassword().equals(password)){
            accountDAO.updateBalance(account.getAccountNumber(),newAmount);
            account.setBalance(newAmount);
            return true;
        }
       return false;
    }

    public boolean deposit(Account account,double amount,String password){
        double newAmount = account.getBalance()+ amount;
        if(account.getPassword().equals(password)){
            accountDAO.updateBalance(account.getAccountNumber(),newAmount);
            account.setBalance(newAmount);
            return true;
        }
        return false;
    }

    // money transfer
    public boolean transferMoney(int senderAccountNo ,int receiverAccountNo,double amount){
        String debit ="UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
        String credit ="UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
        try(Connection connection = ConnectionManager.getConnection();
            PreparedStatement sender = connection.prepareStatement(debit);
            PreparedStatement receiver = connection.prepareStatement(credit)
        ){
            connection.setAutoCommit(false);
            if(amount<=0){
                System.out.println("Enter Valid amount");
                return false;
            }
            if(!accountDAO.isSufficient(senderAccountNo, amount)){
                System.out.println("Insufficient Balance");
                connection.rollback();
                return false;
            }

            sender.setDouble(1,amount);
            sender.setInt(2,senderAccountNo);
            receiver.setDouble(1,amount);
            receiver.setInt(2,receiverAccountNo);

            int senderRows = sender.executeUpdate();
            int receiverRows = receiver.executeUpdate();
            if(senderRows==1 && receiverRows==1){
                connection.commit();
                System.out.println("Transaction is Successful");
                return true;
            }else {
                connection.rollback();
                System.out.println("Transaction failed");
            }
        }catch(SQLException e){
            System.out.println("Bank Server is not responding... Try later");

        }
        return false;
    }

    public boolean updateAccountName(int accountNumber,String name){
        name = name.toUpperCase();
        if(accountDAO.updateAccountName(accountNumber,name)){
            System.out.println("Name change successfully");
            return true;
        }else{
            System.out.println("Inputs error");
        }
        return false;
    }
    public boolean updateAccountEmail(int accountNumber,String email,String name){
        email = email.toUpperCase();
        name = name.toUpperCase();
        if(accountDAO.updateAccountEmail(accountNumber,email,name)){
            System.out.println("Name change successfully");
            return true;
        }else{
            System.out.println("Inputs error");
        }
        return false;
    }

    public boolean closeAccount(int accountNumber,String password){
        Account account = accountDAO.findAccountByNumber(accountNumber);
        if(account.getAccountNumber()==accountNumber && PasswordUtil.checkPassword(password,account.getPassword())){
            return accountDAO.deleteAccount(account.getAccountNumber());
        }else{
            System.out.println("Ops! invalid Account Number or Password");
        }
        return false;
    }

    public boolean reSetPassword(int accountNumber,String password,String newPassword){
        Account account = accountDAO.findAccountByNumber(accountNumber);
        if(PasswordUtil.checkPassword(password,account.getPassword())) {
            return accountDAO.reSetPassword(accountNumber, newPassword);
        }
        return false;
    }
    public boolean forgotPassword(int accountNumber,String email){
        Account account = accountDAO.findAccountByNumber(accountNumber);
        email = email.toUpperCase();
        if(account.getEmail().equals(email)){

            int opt = 0000; // generate OTP and SEND TO EAMIL ID

            System.out.println("Enter OTP send to "+account.getEmail()+" this email id : ");
            if(scanner.nextInt()==opt){
                System.out.print("Enter New password : ");
                String newPassword = scanner.next();
                newPassword = PasswordUtil.hashPassword(newPassword);
                return accountDAO.reSetPassword(account.getAccountNumber(),newPassword);
            }
        }else{
            System.out.println("No email found");
        }
        return false;
    }

    // login
    public boolean login(int accountNumber ,String Password){
        try {
            Account account = accountDAO.findAccountByNumber(accountNumber);
            boolean passwd = PasswordUtil.checkPassword(Password, account.getPassword());
            if (accountNumber == account.getAccountNumber()) {
                if (passwd) {
                    System.out.println(account.getAccountHolderName() + ", Successfully Login");
                    return true;
                } else {
                    System.out.println("Invalid Password");
                    System.out.println("Forget Password ?\nPress 1 : Reset Password");
                    if(scanner.nextInt()==1){
                        System.out.println("Enter Account Number: ");
                        int accountNumb = scanner.nextInt();
                        System.out.println("Enter Email Id: ");
                        String email = scanner.next();
                        if(forgotPassword(accountNumb,email)) {
                            System.out.println("Password Re-set successfully");
                        }
                        return false;
                    }else{
                        System.out.println("Enter Valid choice");
                    }
                }
            } else {
                System.out.println("Invalid Account Number");
            }
        }catch (NullPointerException e){
            System.out.println("Data not found");
        }
        return false;
    }

}
