package DAO;

import Model.Account;
import Util.ConnectionManager;
import Util.PasswordUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDAO {

    // Create the account in database
    public boolean createAccount(long id , int accountNumber, String accountHolderName, String email, String password, double balance){
        String query = "INSERT INTO accounts (id,account_number, account_holder_name, email, password_hash, balance) VALUES (?,?,?,?,?,?)";
        try(Connection connection = ConnectionManager.getConnection(); PreparedStatement ps = connection.prepareStatement(query)){
            ps.setLong(1,id);
            ps.setInt(2,accountNumber);
            ps.setString(3,accountHolderName);
            ps.setString(4,email);
            String hashedPassword = PasswordUtil.hashPassword(password);
            ps.setString(5,hashedPassword);
            ps.setDouble(6,balance);
            ps.executeUpdate();
            return true;
        }catch(SQLException e){
            System.out.println(e);
        }
        return false;
    }

    // get accounts by account number from database
    public Account findAccountByNumber(int accountNumber){
        String query = "SELECT * FROM accounts WHERE account_number = ?";
        try(Connection connection = ConnectionManager.getConnection(); PreparedStatement ps = connection.prepareStatement(query)){
            ps.setInt(1,accountNumber);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                Account ac = new Account();
                ac.setId(rs.getLong("id"));
                ac.setAccountNumber(rs.getInt("account_number"));
                ac.setAccountHolderName(rs.getString("account_Holder_name"));
                ac.setEmail(rs.getString("email"));
                ac.setPassword(rs.getString("password_hash"));
                ac.setBalance(rs.getDouble("balance"));
                return ac;
            }
        }catch(SQLException e){
            System.out.println(e);
        }
        return null;
    }

    // Check balance isSufficient
    public boolean isSufficient(int account_number , double amount){
        String query = "SELECT balance FROM accounts WHERE account_number = ?";
        try(Connection connection = ConnectionManager.getConnection();
            PreparedStatement ps = connection.prepareStatement(query)){
            ps.setInt(1,account_number);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                double current_balance = rs.getDouble("balance");
                if(current_balance>=amount) {
                    return true;
                }
            }
        }catch(SQLException e){
            System.out.println(e);
        }
        return false;
    }


    // update balance
    public void updateBalance(int accountNumber,double balance){
        String updateBalance = "UPDATE accounts SET balance = ? WHERE account_number=?";
        try(Connection connection = ConnectionManager.getConnection();
            PreparedStatement ps = connection.prepareStatement(updateBalance)){
            connection.setAutoCommit(false);
            ps.setInt(2,accountNumber);
            ps.setDouble(1,balance);
            if((ps.executeUpdate()==1)){
                connection.commit();
            }else{
                connection.rollback();
            }
        }catch(SQLException e){
            System.out.println(e);
        }


    }
    // update account
    public boolean updateAccountName(int accountNumber,String name){
        String updateAccountHolderName = "UPDATE accounts SET account_holder_name = ? WHERE account_number=?";
        try(Connection connection = ConnectionManager.getConnection();
            PreparedStatement accName = connection.prepareStatement(updateAccountHolderName)){
            connection.setAutoCommit(false);
           accName.setString(1,name);
           accName.setInt(2,accountNumber);
            if((accName.executeUpdate()==1)){
                connection.commit();
                return true;
            }else{
                connection.rollback();
            }
        }catch(SQLException e){
            System.out.println(e);
        }
        return false;

    }
    // update account
    public boolean updateAccountEmail(int accountNumber,String email,String name){
        String updateAccountHolderName = "UPDATE accounts SET email = ? WHERE account_number=? AND account_holder_name = ?";
        try(Connection connection = ConnectionManager.getConnection();
            PreparedStatement accEmail = connection.prepareStatement(updateAccountHolderName)){
            connection.setAutoCommit(false);
            accEmail.setString(1,email);
            accEmail.setInt(2,accountNumber);
            accEmail.setString(3,name);
            if((accEmail.executeUpdate()==1)){
                connection.commit();
                return true;
            }else{
                connection.rollback();
            }
        }catch(SQLException e){
            System.out.println(e);
        }
        return false;
    }
    // delete account
    public boolean deleteAccount(int accountNumber){
        String deleteAccount= "DELETE FROM accounts WHERE account_number=? ";
        try(Connection connection = ConnectionManager.getConnection();
            PreparedStatement accDelete = connection.prepareStatement(deleteAccount)){
            connection.setAutoCommit(false);
            accDelete.setInt(1,accountNumber);
            if((accDelete.executeUpdate()==1)){
                connection.commit();
                return true;
            }else{
                connection.rollback();
            }
        }catch(SQLException e){
            System.out.println(e);
        }
        return false;
    }

    public boolean reSetPassword(int accountNumber,String password){
        String reSetPassword = "UPDATE accounts SET password_hash = ? WHERE account_number=?";
        try(Connection connection = ConnectionManager.getConnection();
            PreparedStatement reSetpasswd = connection.prepareStatement(reSetPassword)){
            connection.setAutoCommit(false);
            reSetpasswd.setString(1,password);
            reSetpasswd.setInt(2,accountNumber);
            if((reSetpasswd.executeUpdate()==1)){
                connection.commit();
                return true;
            }else{
                connection.rollback();
            }
        }catch(SQLException e){
            System.out.println(e);
        }
        return false;

    }

















}
