package hotel.databaseOperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

import I3.DatabaseOperation.DataBaseConnection;
import hotel.classes.UserInfo;

/**
 * Handles database operations for customer information and related functionalities.
 * This class provides methods to insert, update, delete, and retrieve customer data.
 */
public class DatabaseOperation {

    // Establishing the connection to the database
    Connection conn = DataBaseConnection.connectTODB();

    // PreparedStatement to execute SQL queries
    PreparedStatement statement = null;

    // ResultSet to store the results from SELECT queries
    ResultSet result = null;

    /**
     * Inserts a new customer record into the database.
     * @param user The user information to insert into the database (name, address, phone, type).
     */
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

    /**
     * Updates an existing customer record based on the customer ID.
     * @param user The updated user information (must contain a valid customerId).
     */
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

    /**
     * Deletes a customer record based on the user ID.
     * @param userId The ID of the user to delete.
     */
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

    /**
     * Retrieves all customer records from the database.
     * @return A ResultSet containing all customer data.
     */
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

    /**
     * Searches for customers by name.
     * @param user The partial or full name of the user to search for.
     * @return A ResultSet containing matching customer records.
     */
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

    /**
     * Searches for a specific customer by their unique ID.
     * @param id The ID of the customer to search for.
     * @return A ResultSet containing the matching customer record.
     */
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

    /**
     * Retrieves available rooms based on the check-in time.
     * @param checkInTime The check-in time to compare with room booking times.
     * @return A ResultSet containing available room numbers.
     */
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

    /**
     * Retrieves booking information for a specific room and date range.
     * @param startDate The start date of the booking range.
     * @param endDate The end date of the booking range.
     * @param roomNo The room number to check booking status for.
     * @return A ResultSet containing booking information.
     */
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

    /**
     * Retrieves the customer ID based on the name and phone number of the user.
     * @param user The user object containing the name and phone number to search.
     * @return The customer ID if found, or -1 if no matching customer is found.
     */
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

    /**
     * Closes the PreparedStatement and ResultSet to free up database resources.
     * Use this when you're done using the database resources.
     */
    public void flushAll() {
        try {
            if (statement != null) statement.close();
            if (result != null) result.close();
        } catch (SQLException ex) {
            System.err.print(ex.toString() + " >> CLOSING DB");
        }
    }

    /**
     * Closes the PreparedStatement only, used when no ResultSet is involved.
     */
    private void flushStatmentOnly() {
        try {
            if (statement != null) statement.close();
        } catch (SQLException ex) {
            System.err.print(ex.toString() + " >> CLOSING DB");
        }
    }
}
