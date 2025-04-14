package hotel.databaseOperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

import hotel.classes.Food;

/**
 * Handles database operations related to food items.
 * This includes inserting, retrieving, updating, and deleting records from the 'food' table.
 */
public class FoodDb {

    // Establishes connection with the database
    Connection conn = DataBaseConnection.connectTODB();

    // Used for executing SQL statements
    PreparedStatement statement = null;

    // Used to store the result set returned from a SELECT query
    ResultSet result = null;

    /**
     * Inserts a new food item into the database using a prepared statement.
     * @param food The food item object to insert (must have name and price set)
     */
    public void insertFood(Food food) {
        try {
            String insertFood = "INSERT INTO food (name, price) VALUES (?, ?)";
            statement = conn.prepareStatement(insertFood);
            statement.setString(1, food.getName());
            statement.setDouble(2, food.getPrice());

            statement.execute();
            JOptionPane.showMessageDialog(null, "Successfully inserted a new food item");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nInsert query for food failed");
        } finally {
            flushStatmentOnly();
        }
    }

    /**
     * Retrieves all food items from the database.
     * @return A ResultSet containing all food rows.
     */
    public ResultSet getFoods() {
        try {
            String query = "SELECT * FROM food";
            statement = conn.prepareStatement(query);
            result = statement.executeQuery();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nError retrieving food data");
        }
        return result;
    }

    /**
     * Updates an existing food item in the database using its food_id.
     * @param food The updated food object. Must contain a valid foodId.
     */
    public void updateFood(Food food) {
        try {
            String updateFood = "UPDATE food SET name = ?, price = ? WHERE food_id = ?";
            statement = conn.prepareStatement(updateFood);
            statement.setString(1, food.getName());
            statement.setDouble(2, food.getPrice());
            statement.setInt(3, food.getFoodId());

            statement.execute();
            JOptionPane.showMessageDialog(null, "Successfully updated food");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nUpdate food query failed");
        } finally {
            flushStatmentOnly();
        }
    }

    /**
     * Deletes a food item from the database using its ID.
     * @param foodId The unique ID of the food to be deleted.
     */
    public void deleteFood(int foodId) {
        try {
            String deleteQuery = "DELETE FROM food WHERE food_id = ?";
            statement = conn.prepareStatement(deleteQuery);
            statement.setInt(1, foodId);

            statement.execute();
            JOptionPane.showMessageDialog(null, "Deleted food");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nDelete food query failed");
        } finally {
            flushStatmentOnly();
        }
    }

    /**
     * Closes both PreparedStatement and ResultSet to free database resources.
     * Use this when both are no longer needed.
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
     * Use this when only statements were used (no ResultSet).
     */
    private void flushStatmentOnly() {
        try {
            if (statement != null) statement.close();
        } catch (SQLException ex) {
            System.err.print(ex.toString() + " >> Error closing DB");
        }
    }
}
