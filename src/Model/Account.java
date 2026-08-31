package Model;

import DAO.AccountDAO;

import java.math.BigDecimal;

public class Account{
    private long id;
    private int accountNumber ;
    private String accountHolderName;
    private String email;
    private String password;
    private double  balance;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
