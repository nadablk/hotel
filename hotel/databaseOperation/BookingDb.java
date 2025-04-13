package hotel.databaseOperation;

import java.sql.*;
import javax.swing.JOptionPane;
import hotel.classes.Booking;
import hotel.classes.Order;

/**
 * BookingDb handles all CRUD operations for booking and order data in the database.
 * Ensures safe interaction with SQL via prepared statements.
 */
public class BookingDb {

    private Connection conn;
    private PreparedStatement statement = null;
    private ResultSet result = null;

    public BookingDb() {
        conn = DataBaseConnection.connectTODB();
    }

    /**
     * Inserts a new booking into the database for each selected room.
     * @param booking Booking object containing customer and room details.
     */
    public void insertBooking(Booking booking) {
        String insertQuery = "INSERT INTO booking " +
                "(customer_id, booking_room, guests, check_in, check_out, booking_type, has_checked_out) " +
                "VALUES (?, ?, ?, ?, ?, ?, 0)";

        for (var room : booking.getRooms()) {
            try {
                statement = conn.prepareStatement(insertQuery);
                statement.setInt(1, booking.getCustomer().getCustomerId());
                statement.setString(2, room.getRoomNo());
                statement.setInt(3, booking.getPerson());
                statement.setLong(4, booking.getCheckInDateTime());
                statement.setLong(5, booking.getCheckOutDateTime());
                statement.setString(6, booking.getBookingType());

                statement.execute();
                JOptionPane.showMessageDialog(null, "Successfully inserted new Booking");

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex + "\nInsertQuery booking Failed");
            } finally {
                flushStatementOnly();
            }
        }
    }

    /**
     * Retrieves all bookings.
     * @return ResultSet containing all bookings.
     */
    public ResultSet getBookingInformation() {
        try {
            statement = conn.prepareStatement("SELECT * FROM booking");
            result = statement.executeQuery();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex + "\nError getting all bookings.");
        }
        return result;
    }

    /**
     * Retrieves a single booking by ID.
     * @param bookingId The booking ID to search for.
     * @return ResultSet containing the booking data.
     */
    public ResultSet getABooking(int bookingId) {
        try {
            statement = conn.prepareStatement("SELECT * FROM booking WHERE booking_id = ?");
            statement.setInt(1, bookingId);
            result = statement.executeQuery();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex + "\nError getting booking by ID.");
        }
        return result;
    }

    /**
     * Retrieves all active bookings for a room name (not checked out).
     * @param roomName Name or partial name of the room.
     * @return ResultSet of matching bookings.
     */
    public ResultSet bookingsReadyForOrder(String roomName) {
        try {
            String query = "SELECT booking_id, booking_room, name " +
                    "FROM booking JOIN userInfo ON booking.customer_id = userInfo.user_id " +
                    "WHERE booking_room LIKE ? AND has_checked_out = 0 " +
                    "ORDER BY booking_id DESC";

            statement = conn.prepareStatement(query);
            statement.setString(1, "%" + roomName + "%");
            result = statement.executeQuery();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex + "\nError getting bookings ready for order.");
        }
        return result;
    }

    /**
     * Marks a booking as checked out and updates its check-out time.
     * @param bookingId ID of the booking to update.
     * @param checkOutTime Epoch time to set for check-out.
     */
    public void updateCheckOut(int bookingId, long checkOutTime) {
        try {
            String update = "UPDATE booking SET has_checked_out = 1, check_out = ? WHERE booking_id = ?";
            statement = conn.prepareStatement(update);
            statement.setLong(1, checkOutTime);
            statement.setInt(2, bookingId);

            statement.execute();
            JOptionPane.showMessageDialog(null, "Successfully updated Check Out");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex + "\nUpdate checkout failed.");
        } finally {
            flushStatementOnly();
        }
    }

    /**
     * Retrieves the room price for a given booking.
     * @param bookingId Booking ID.
     * @return Price of the room.
     */
    public int getRoomPrice(int bookingId) {
        int price = -1;
        try {
            String query = "SELECT price FROM booking " +
                    "JOIN room ON booking_room = room_no " +
                    "JOIN roomType ON type = room_class " +
                    "WHERE booking_id = ?";
            statement = conn.prepareStatement(query);
            statement.setInt(1, bookingId);
            result = statement.executeQuery();

            if (result.next()) {
                price = result.getInt("price");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex + "\nError retrieving room price.");
        } finally {
            flushAll();
        }

        return price;
    }

    /**
     * Inserts a new food order associated with a booking.
     * @param order Order object with booking reference and food details.
     */
    public void insertOrder(Order order) {
        try {
            String insert = "INSERT INTO orderItem (booking_id, item_food, price, quantity, total) " +
                    "VALUES (?, ?, ?, ?, ?)";
            statement = conn.prepareStatement(insert);
            statement.setInt(1, order.getBookingId());
            statement.setString(2, order.getFoodItem());
            statement.setDouble(3, order.getPrice());
            statement.setInt(4, order.getQuantity());
            statement.setDouble(5, order.getTotal());

            statement.execute();
            JOptionPane.showMessageDialog(null, "Successfully inserted a new Order");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex + "\nInsert Order Failed");
        } finally {
            flushStatementOnly();
        }
    }

    /**
     * Retrieves all orders/payments for a specific booking.
     * @param bookingId Booking ID.
     * @return ResultSet of payment info.
     */
    public ResultSet getAllPaymentInfo(int bookingId) {
        try {
            String query = "SELECT * FROM orderItem WHERE booking_id = ?";
            statement = conn.prepareStatement(query);
            statement.setInt(1, bookingId);
            result = statement.executeQuery();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex + "\nError getting payment info.");
        }
        return result;
    }

    /** Safely closes statement and result set. */
    public void flushAll() {
        try {
            if (statement != null) statement.close();
            if (result != null) result.close();
        } catch (SQLException ex) {
            System.err.println(ex + " >> CLOSING DB");
        }
    }

    /** Safely closes statement only. */
    public void flushStatementOnly() {
        try {
            if (statement != null) statement.close();
        } catch (SQLException ex) {
            System.err.println(ex + " >> CLOSING STATEMENT");
        }
    }
}
