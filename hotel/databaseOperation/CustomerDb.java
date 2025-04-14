package hotel.databaseOperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

import hotel.classes.UserInfo;

public class CustomerDb {
    Connection conn;
    PreparedStatement statement = null;
    ResultSet result = null;

    public CustomerDb() {
        conn = DataBaseConnection.connectTODB();
    }

    public void insertCustomer(UserInfo user) {
        try {
            String insertQuery = "INSERT INTO userInfo (name, address, phone, type) VALUES (?, ?, ?, ?)";
            statement = conn.prepareStatement(insertQuery);
            statement.setString(1, user.getName());
            statement.setString(2, user.getAddress());
            statement.setString(3, user.getPhoneNo());
            statement.setString(4, user.getType());

            statement.execute();
            JOptionPane.showMessageDialog(null, "Successfully inserted new Customer");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nInsertQuery Failed");
        } finally {
            flushStatementOnly();
        }
    }

    public void updateCustomer(UserInfo user) {
        try {
            String updateQuery = "UPDATE userInfo SET name = ?, address = ?, phone = ?, type = ? WHERE user_id = ?";
            statement = conn.prepareStatement(updateQuery);
            statement.setString(1, user.getName());
            statement.setString(2, user.getAddress());
            statement.setString(3, user.getPhoneNo());
            statement.setString(4, user.getType());
            statement.setInt(5, user.getCustomerId());

            statement.execute();
            JOptionPane.showMessageDialog(null, "Successfully updated Customer");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nUpdate query Failed");
        } finally {
            flushStatementOnly();
        }
    }

    public void deleteCustomer(int userId) {
        try {
            String deleteQuery = "DELETE FROM userInfo WHERE user_id = ?";
            statement = conn.prepareStatement(deleteQuery);
            statement.setInt(1, userId);

            statement.execute();
            JOptionPane.showMessageDialog(null, "Deleted user");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nDelete query Failed");
        } finally {
            flushStatementOnly();
        }
    }

    public ResultSet getAllCustomer() {
        try {
            String query = "SELECT * FROM userInfo";
            statement = conn.prepareStatement(query);
            result = statement.executeQuery();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nError retrieving customers");
        }
        return result;
    }

    private void flushStatementOnly() {
        try {
            if (statement != null) statement.close();
        } catch (SQLException ex) {
            System.err.print(ex.toString() + " >> CLOSING DB");
        }
    }

    public void flushAll() {
        try {
            if (statement != null) statement.close();
            if (result != null) result.close();
        } catch (SQLException ex) {
            System.err.print(ex.toString() + " >> CLOSING DB");
        }
    }
}
