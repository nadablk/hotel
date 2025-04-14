package hotel.databaseOperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

import hotel.classes.Room;
import hotel.classes.RoomFare;

public class RoomDb {
    Connection conn = DataBaseConnection.connectTODB();
    PreparedStatement statement = null;
    ResultSet result = null;

    public void insertRoom(Room room) {
        try {
            String insertQuery = "INSERT INTO room (room_no, bed_number, tv, wifi, gizer, phone, room_class, meal_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            statement = conn.prepareStatement(insertQuery);
            statement.setString(1, room.getRoomNo());
            statement.setInt(2, room.getBedNumber());
            statement.setString(3, boolToString(room.isHasTV()));
            statement.setString(4, boolToString(room.isHasWIFI()));
            statement.setString(5, boolToString(room.isHasGizer()));
            statement.setString(6, boolToString(room.isHasPhone()));
            statement.setString(7, room.getRoomClass().getRoomType());
            statement.setInt(8, room.getMealId());

            statement.execute();
            JOptionPane.showMessageDialog(null, "Successfully inserted a new Room");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nInsert Room failed");
        } finally {
            flushStatmentOnly();
        }
    }

    public ResultSet getRooms() {
        try {
            String query = "SELECT * FROM room";
            statement = conn.prepareStatement(query);
            result = statement.executeQuery();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nError retrieving rooms");
        }
        return result;
    }

    public int getNoOfRooms() {
        int rooms = -1;
        try {
            String query = "SELECT COUNT(room_no) AS noRoom FROM room";
            statement = conn.prepareStatement(query);
            result = statement.executeQuery();
            if (result.next()) {
                rooms = result.getInt("noRoom");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nError counting rooms");
        }
        return rooms;
    }

    public ResultSet getAllRoomNames() {
        try {
            String query = "SELECT room_no FROM room";
            statement = conn.prepareStatement(query);
            result = statement.executeQuery();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nError retrieving room numbers");
        }
        return result;
    }

    public void deleteRoom(int roomId) {
        try {
            String deleteQuery = "DELETE FROM room WHERE room_id = ?";
            statement = conn.prepareStatement(deleteQuery);
            statement.setInt(1, roomId);
            statement.execute();
            JOptionPane.showMessageDialog(null, "Deleted room");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nDelete Room failed");
        } finally {
            flushStatmentOnly();
        }
    }

    public void updateRoom(Room room) {
        try {
            String updateQuery = "UPDATE room SET room_no = ?, bed_number = ?, tv = ?, wifi = ?, gizer = ?, phone = ?, room_class = ?, meal_id = ? WHERE room_id = ?";
            statement = conn.prepareStatement(updateQuery);
            statement.setString(1, room.getRoomNo());
            statement.setInt(2, room.getBedNumber());
            statement.setString(3, boolToString(room.isHasTV()));
            statement.setString(4, boolToString(room.isHasWIFI()));
            statement.setString(5, boolToString(room.isHasGizer()));
            statement.setString(6, boolToString(room.isHasPhone()));
            statement.setString(7, room.getRoomClass().getRoomType());
            statement.setInt(8, room.getMealId());
            statement.setInt(9, room.getRoomId());

            statement.execute();
            JOptionPane.showMessageDialog(null, "Successfully updated the room");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nUpdate Room failed");
        } finally {
            flushStatmentOnly();
        }
    }

    public void insertRoomType(RoomFare roomType) {
        try {
            String insertQuery = "INSERT INTO roomType (type, price) VALUES (?, ?)";
            statement = conn.prepareStatement(insertQuery);
            statement.setString(1, roomType.getRoomType());
            statement.setDouble(2, roomType.getPricePerDay());

            statement.execute();
            JOptionPane.showMessageDialog(null, "Successfully inserted a new Room Type");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nInsert RoomType failed");
        } finally {
            flushStatmentOnly();
        }
    }

    public ResultSet getRoomType() {
        try {
            String query = "SELECT * FROM roomType";
            statement = conn.prepareStatement(query);
            result = statement.executeQuery();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString() + "\nError retrieving Room Types");
        }
