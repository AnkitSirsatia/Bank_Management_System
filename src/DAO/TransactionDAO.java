package DAO;

import Model.Transaction;
import Util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    public boolean saveTransaction(Connection connection, int id,int accountId, String type, double amount, String description) {

        String sql = " INSERT INTO transactions (id,account_id, type, amount, description) VALUES (?,?, ?, ?, ?) ";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1,id);
            ps.setInt(2, accountId);
            ps.setString(3, type);
            ps.setDouble(4, amount);
            ps.setString(5, description);

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public List<Transaction> getTransactionsByAccount(Connection connection, int accountId) {

        List<Transaction> transactions = new ArrayList<>();

        String sql = " SELECT * FROM transactions WHERE account_id = ? ORDER BY created_at DESC";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, accountId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Transaction transaction = new Transaction();

                transaction.setId(rs.getInt("id"));
                transaction.setAccountId(rs.getInt("account_id"));
                transaction.setType(rs.getString("type"));
                transaction.setAmount(rs.getDouble("amount"));
                transaction.setDescription(rs.getString("description"));
                transaction.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

                transactions.add(transaction);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactions;
    }


}
