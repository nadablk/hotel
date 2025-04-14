package hotel.databaseOperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

import I3.DatabaseOperation.DataBaseConnection;
import hotel.classes.UserInfo;

public class DatabaseOperation {

    Connection conn = DataBaseConnection.connectTODB();
    PreparedStatement statement = null;
    ResultSet result = null;

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
            flushStatmentOnly();
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
            flushStatmentOnly();
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
            flushStatmentOnly();
        }
    }

    public ResultSet getAllCustomer() {
        try {
            String query = "SELECT * FROM userInfo";
            statement = conn.prepareStatement(query);
            result = statement.executeQuery();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nError coming from getAllCustomer");
        }
        return result;
    }

    public ResultSet searchUser(String user) {
        try {
            String query = "SELECT user_id, name, address FROM userInfo WHERE name LIKE ?";
            statement = conn.prepareStatement(query);
            statement.setString(1, "%" + user + "%");
            result = statement.executeQuery();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nError in searchUser");
        }
        return result;
    }

    public ResultSet searchAnUser(int id) {
        try {
            String query = "SELECT * FROM userInfo WHERE user_id = ?";
            statement = conn.prepareStatement(query);
            statement.setInt(1, id);
            result = statement.executeQuery();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nError in searchAnUser");
        }
        return result;
    }

    public ResultSet getAvailableRooms(long checkInTime) {
        try {
            String query = "SELECT room_no FROM room LEFT OUTER JOIN booking ON room.room_no = booking.booking_room " +
                    "WHERE booking.booking_room IS NULL OR ? < booking.check_in OR booking.check_out < ? " +
                    "GROUP BY room.room_no ORDER BY room_no";
            statement = conn.prepareStatement(query);
            statement.setLong(1, checkInTime);
            statement.setLong(2, checkInTime);
            result = statement.executeQuery();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nError in getAvailableRooms");
        }
        return result;
    }

    public ResultSet getBookingInfo(long startDate, long endDate, String roomNo) {
        try {
            String query = "SELECT * FROM booking WHERE booking_room = ? AND (" +
                    "(check_in <= ? AND (check_out = 0 OR check_out <= ?)) OR " +
                    "(check_in > ? AND check_out < ?) OR " +
                    "(check_in <= ? AND (check_out = 0 OR check_out > ?)))";
            statement = conn.prepareStatement(query);
            statement.setString(1, roomNo);
            statement.setLong(2, startDate);
            statement.setLong(3, endDate);
            statement.setLong(4, startDate);
            statement.setLong(5, endDate);
            statement.setLong(6, endDate);
            statement.setLong(7, endDate);
            result = statement.executeQuery();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nError in getBookingInfo");
        }
        return result;
    }

    public int getCustomerId(UserInfo user) {
        int id = -1;
        try {
            String query = "SELECT user_id FROM userInfo WHERE name = ? AND phone = ?";
            statement = conn.prepareStatement(query);
            statement.setString(1, user.getName());
            statement.setString(2, user.getPhoneNo());
            result = statement.executeQuery();

            if (result.next()) {
                id = result.getInt("user_id");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nError in getCustomerId");
        } finally {
            flushAll();
        }
        return id;
    }

    public void flushAll() {
        try {
            if (statement != null) statement.close();
            if (result != null) result.close();
        } catch (SQLException ex) {
            System.err.print(ex.toString() + " >> CLOSING DB");
        }
    }

    private void flushStatmentOnly() {
        try {
            if (statement != null) statement.close();
        } catch (SQLException ex) {
            System.err.print(ex.toString() + " >> CLOSING DB");
        }
    }
}
