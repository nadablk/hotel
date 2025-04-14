package hotel.databaseOperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

import hotel.classes.Order;

/**
 *
 * @author Faysal
 */


/// ######                       DORKAR NAI EI DB ER , ETA PORE BAD DIYE DIBO
public class OrderDb {

    Connection conn = DataBaseConnection.connectTODB();
    PreparedStatement statement = null;
    ResultSet result = null;

    ppublic void insertOrder(Order order) {
        try {
            String insertOrder = "INSERT INTO orderItem (booking_id, item_food, price, quantity, total) VALUES (?, ?, ?, ?, ?)";

            statement = conn.prepareStatement(insertOrder);
            statement.setInt(1, order.getBookingId());
            statement.setString(2, order.getFoodItem());
            statement.setDouble(3, order.getPrice());
            statement.setInt(4, order.getQuantity());
            statement.setDouble(5, order.getTotal());

            System.out.println("Executing query: " + statement);
            statement.execute();

            JOptionPane.showMessageDialog(null, "Successfully inserted a new Order");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nOrder insertion failed");
        } finally {
            flushStatmentOnly();
        }
    }


}

    public void flushAll() {
        {
            try {
                statement.close();
                result.close();
            } catch (SQLException ex) {
                System.err.print(ex.toString() + " >> CLOSING DB");
            }
        }
    }

    private void flushStatmentOnly() {
        {
            try {
                statement.close();
            } catch (SQLException ex) {
                System.err.print(ex.toString() + " >> CLOSING DB");
            }
        }
    }

}
