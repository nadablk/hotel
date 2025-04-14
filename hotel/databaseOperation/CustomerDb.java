package hotel.databaseOperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

import hotel.classes.UserInfo;

/**
 * Handles the database operations related to customer information.
 * This class includes methods for inserting, updating, deleting, and retrieving customer data.
 */
public class CustomerDb {

    // Database connection object
    private Connection conn;

    // PreparedStatement and ResultSet objects for executing SQL queries and storing results
    private PreparedStatement statement = null;
    private ResultSet result = null;

    /**
     * Constructor that initializes the database connection.
     */
    public CustomerDb() {
        conn = DataBaseConnection.connectTODB();  // Establish connection to the database
    }

    /**
     * Inserts a new customer into the userInfo table.
     * @param user The customer data to be inserted into the database.
     */
    public void insertCustomer(UserInfo user) {
        try {
            // SQL query to insert a new customer
            String insertQuery = "INSERT INTO userInfo (name, address, phone, type) VALUES (?, ?, ?, ?)";
            statement = conn.prepareStatement(insertQuery);

            // Setting the customer data into the query
            statement.setString(1, user.getName());
            statement.setString(2, user.getAddress());
            statement.setString(3, user.getPhoneNo());
            statement.setString(4, user.getType());

            // Executing the insert operation
            statement.execute();
            JOptionPane.showMessageDialog(null, "Successfully inserted new Customer");

        } catch (SQLException ex) {
            // Handling any exceptions that may occur during the insert
            JOptionPane.showMessageDialog(null, ex.toString() + "\nInsertQuery Failed");
        } finally {
            // Ensure statement is closed after execution
            flushStatementOnly();
        }
    }

    /**
     * Updates an existing customer in the userInfo table.
     * @param user The updated customer data (including the customer ID).
     */
    public void updateCustomer(UserInfo user) {
        try {
            // SQL query to update an existing customer by their ID
            String updateQuery = "UPDATE userInfo SET name = ?, address = ?, phone = ?, type = ? WHERE user_id = ?";
            statement = conn.prepareStatement(updateQuery);

            // Setting the customer data into the query
            statement.setString(1, user.getName());
            statement.setString(2, user.getAddress());
            statement.setString(3, user.getPhoneNo());
            statement.setString(4, user.getType());
            statement.setInt(5, user.getCustomerId()); // Customer ID is used to identify the record

            // Executing the update operation
            statement.execute();
            JOptionPane.showMessageDialog(null, "Successfully updated Customer");
        } catch (SQLException ex) {
            // Handling any exceptions that may occur during the update
            JOptionPane.showMessageDialog(null, ex.toString() + "\nUpdate query
