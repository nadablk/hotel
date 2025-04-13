package hotel.tableModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

import hotel.databaseOperation.DatabaseOperation;
import hotel.databaseOperation.RoomDb;

/**
 *
 * @author Faysal Ahmed
 */
public class BookingTableModel extends AbstractTableModel {

    private String[] columnNames;
    private Date date;
    private Object[][] data;

    public BookingTableModel(long start ,long end) {
        iniColNames();
        fetchDataFromDB(start, end);
        
       
    }

    public void iniColNames() {

        date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("d");
        // -1 , because date starts with 0
        int today = ( Integer.parseInt(ft.format(date))-1 )%getMonthLimit(date);
        columnNames = new String[11];
        columnNames[0] = "#";
        for(int i =1;i<11;i++)
        {
            
            today = today%getMonthLimit(date);
            today ++;
          //  System.out.println(today+" , loop today");
            columnNames[i] = today+"";
        }
    }

    public int getMonthLimit(Date x)
    {
        SimpleDateFormat ft = new SimpleDateFormat("M");
        int y = Integer.parseInt(ft.format(x));
        if(y ==2)
            return 28;
        else if (y ==1|| y ==3|| y ==5|| y ==7|| y ==8|| y ==10 || y== 12)
            return 31;
        else return 30;
    }
    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public void fetchDataFromDB(long start, long end) {
        RoomDb roomDb = new RoomDb();
        DatabaseOperation dbOperation = new DatabaseOperation();

        try {
            int rows = roomDb.getNoOfRooms();
            data = new Object[rows][11];
            initializeDataArray(data);

            ResultSet roomNames = roomDb.getAllRoomNames();

            for (int i = 0; i < rows && roomNames.next(); i++) {
                String roomName = roomNames.getString("room_no");
                data[i][0] = roomName;

                ResultSet result = dbOperation.getBookingInfo(start, end, roomName);

                while (result.next()) {
                    long checkIn = Long.parseLong(result.getString("check_in"));
                    long checkOut = Long.parseLong(result.getString("check_out"));

                    handleBookingLogic(data, i, checkIn, checkOut, start, end);
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "From Booking table model class\n" + ex.toString());
        }
    }

    // Initialize the data array with empty strings
    private void initializeDataArray(Object[][] data) {
        for (int i = 0; i < data.length; i++) {
            Arrays.fill(data[i], "");
        }
    }

    // Handle the logic of populating data array based on check-in/out times
    private void handleBookingLogic(Object[][] data, int row, long checkIn, long checkOut, long start, long end) {
        int dayStart = getDay(start);
        int checkInDay = getDay(checkIn);
        int checkOutDay = checkOut != 0 ? getDay(checkOut) : 0;

        // Case 1: Booking started before or at start and ends within or open
        if (checkIn <= start && (checkOut == 0 || checkOut <= end)) {
            data[row][1] = "<<";
        }

        // Case 2: Booking starts and ends within the range
        else if (checkIn > start && checkOut < end) {
            data[row][(checkInDay - dayStart) + 1] = ">";
            data[row][(checkOutDay - dayStart) + 1] = "<";
        }

        // Case 3: Booking started before end and ends after or is still ongoing
        else if (checkIn <= end && (checkOut == 0 || checkOut > end)) {
            data[row][(checkInDay - dayStart) + 1] = ">>";
        }
    }

    // Helper to extract day-of-month from Unix timestamp (in seconds)
    private int getDay(long unixTimeInSeconds) {
        return Integer.parseInt(new SimpleDateFormat("d").format(new Date(unixTimeInSeconds * 1000)));
    }

}
