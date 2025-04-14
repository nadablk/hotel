package hotel.databaseOperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

import hotel.classes.Item;

/**
 * This class handles all database operations related to items.
 * It provides methods to insert, update, retrieve, and delete items in the database.
 */
public class ItemDb {

    // Database connection object
    Connection conn = DataBaseConnection.connectTODB();

    // Used to execute parameterized SQL queries
    PreparedStatement statement = null;

    // Used to store the result of SELECT queries
    ResultSet result = null;

    /**
     * Inserts a new item into the database using a parameterized query to prevent SQL injection.
     * @param item The Item object containing name, description, and price
     */
    public void insertItem(Item item) {
        try {
            String insertItem = "INSERT INTO item (name, description, price) VALUES (?, ?, ?)";
            statement = conn.prepareStatement(insertItem);
            statement.setString(1, item.getItemName());
            statement.setString(2, item.getDescription());
            statement.setDouble(3, item.getPrice());

            statement.execute();
            JOptionPane.showMessageDialog(null, "Successfully inserted a new item");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nInsert query for item failed");
        } finally {
            flushStatmentOnly();
        }
    }

    /**
     * Updates an existing item based on the provided item ID.
     * All fields (name, price, description) are updated.
     * @param item The Item object with updated values and valid item ID
     */
    public void updateItem(Item item) {
        try {
            String updateItem = "UPDATE item SET name = ?, price = ?, description = ? WHERE item_id = ?";
            statement = conn.prepareStatement(updateItem);
            statement.setString(1, item.getItemName());
            statement.setDouble(2, item.getPrice());
            statement.setString(3, item.getDescription());
            statement.setInt(4, item.getItemId());

            statement.execute();
            JOptionPane.showMessageDialog(null, "Successfully updated item");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nUpdate item failed");
        } finally {
            flushStatmentOnly();
        }
    }

    /**
     * Retrieves all items from the database.
     * @return ResultSet containing all rows from the 'item' table
     */
    public ResultSet getItems() {
        try {
            String query = "SELECT * FROM item";
            statement = conn.prepareStatement(query);
            result = statement.executeQuery();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nError retrieving items");
        }
        return result;
    }

    /**
     * Deletes an item from the database based on the provided item ID.
     * @param itemId The ID of the item to delete
     */
    public void deleteItem(int itemId) {
        try {
            String deleteQuery = "DELETE FROM item WHERE item_id = ?";
            statement = conn.prepareStatement(deleteQuery);
            statement.setInt(1, itemId);
            statement.execute();

            JOptionPane.showMessageDialog(null, "Deleted item");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nDelete item query failed");
        } finally {
            flushStatmentOnly();
        }
    }

    /**
     * Closes both the PreparedStatement and ResultSet to release database resources.
     * Should be used when both objects are in use.
     */
    public void flushAll() {
        try {
            if (statement != null) statement.close();
            if (result != null) result.close();
        } catch (SQLException ex) {
            System.err.print(ex.toString() + " >> Error closing DB");
        }
    }

    /**
     * Closes only the PreparedStatement object.
     * Use this if no ResultSet is being used.
     */
    private void flushStatmentOnly() {
        try {
            if (statement != null) statement.close();
        } catch (SQLException ex) {
            System.err.print(ex.toString() + " >> Error closing DB");
        }
    }
}
