package hotel.databaseOperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

import hotel.classes.Item;

public class ItemDb {
    Connection conn = DataBaseConnection.connectTODB();
    PreparedStatement statement = null;
    ResultSet result = null;

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

    public void flushAll() {
        try {
            if (statement != null) statement.close();
            if (result != null) result.close();
        } catch (SQLException ex) {
            System.err.print(ex.toString() + " >> Error closing DB");
        }
    }

    private void flushStatmentOnly() {
        try {
            if (statement != null) statement.close();
        } catch (SQLException ex) {
            System.err.print(ex.toString() + " >> Error closing DB");
        }
    }
}
