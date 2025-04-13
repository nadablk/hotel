/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hotel.ui;

import hotel.classes.Order;
import hotel.databaseOperation.BookingDb;

import hotel.databaseOperation.FoodDb;
import hotel.databaseOperation.ItemDb;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.text.JTextComponent;
import net.proteanit.sql.DbUtils;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;


/**
 *
 * @author Faysal
 */
public class OrderPanel extends javax.swing.JDialog {

    /**
     * Creates new form OrderPanel
     */
    
    Vector<String> bookingList = new Vector();
    BookingDb db = new BookingDb();
    ResultSet result;
    FoodDb foodDb = new FoodDb();
    ItemDb itemDb = new ItemDb();
    public OrderPanel(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.getContentPane().setBackground(new Color(241,241,242));
        searchHelper();
        populateFoodTable();
        populateItemTable();
        AutoCompleteDecorator.decorate(comboBooking);
    }

    // Method to set up auto-suggestion and selection handling for the booking combo box
    public void searchHelper() {
        // Create a DefaultComboBoxModel using the bookingList and assign it to the comboBooking
        final DefaultComboBoxModel model = new DefaultComboBoxModel(bookingList);
        comboBooking.setModel(model);

        // Get the text component used to edit combo box entries
        JTextComponent editor = (JTextComponent) comboBooking.getEditor().getEditorComponent();

        // Add a key listener to respond to key events within the combo box editor
        editor.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent evt) {

                // Handle action when the ENTER key is pressed
                if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
                    // Get the selected or typed item from the combo box
                    String details = (String) comboBooking.getSelectedItem();

                    // Check if the selected item has expected format (contains commas)
                    if (!details.contains(",")) {
                        // If not properly formatted, show a message to the user
                        JOptionPane.showMessageDialog(null, "No booking found, try adding a new booking");
                    } else {
                        // Extract the booking ID (last value after the last comma)
                        int bookinId = Integer.parseInt(details.substring(details.lastIndexOf(",") + 1));

                        // Set the booking ID in the corresponding text field
                        tfBookingId.setText(bookinId + "");
                        // No need for further validation as the string format guarantees a valid booking ID
                    }
                }

                // --- Suggestion generation based on what the user types ---
                String value = "";
                try {
                    // Get the current text from the combo box editor
                    value = comboBooking.getEditor().getItem().toString();
                    // System.out.println(value +" <<<<<<<<<<<<<"); // Debugging line
                } catch (Exception ex) {
                    // Silent catch — can log the error if needed
                }

                // If the user has typed at least 2 characters, fetch matching booking suggestions
                if (value.length() >= 2) {
                    // Fetch and update combo box suggestions with relevant bookings
                    bookingComboFill(db.bookingsReadyForOrder(value));

                    // Clear database cache after use
                    db.flushAll();
                }
            }
        });
    }


    // Method to fill the booking comboBox list with booking details from a ResultSet
    public void bookingComboFill(ResultSet result) {
        // Clear the existing items from the booking list to avoid duplicates
        bookingList.clear();

        try {
            // Loop through each row in the ResultSet
            while (result.next()) {
                // Format each booking entry as: "Room Name, Guest Name, Booking ID"
                String bookingEntry = result.getString("booking_room") + ", "
                        + result.getString("name") + ","
                        + result.getString("booking_id");

                // Add the formatted string to the booking list
                bookingList.add(bookingEntry);
            }
        } catch (SQLException ex) {
            // Show an error message if something goes wrong while processing the ResultSet
            JOptionPane.showMessageDialog(null, "bookingCombo fill error");
        }
    }


    // Method to populate the food table with data from the food database
    private void populateFoodTable() {
        // Fetch the list of foods from the food database
        result = foodDb.getFoods();

        // Set the table model to display the fetched food data, converting the ResultSet to a table format
        tableFood.setModel(DbUtils.resultSetToTableModel(result));

        // Clear any cached data from the food database after fetching the food items
        foodDb.flushAll();
    }

    // Method to populate the item table with data from the item database
    private void populateItemTable() {
        // Fetch the list of items from the item database
        result = itemDb.getItems();

        // Set the table model to display the fetched item data, converting the ResultSet to a table format
        tableItem.setModel(DbUtils.resultSetToTableModel(result));

        // Clear any cached data from the item database after fetching the items
        itemDb.flushAll();
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tableFood = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableItem = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        tfFoodItem = new javax.swing.JTextField();
        tfQuantity = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        tfPrice = new javax.swing.JTextField();
        tfTotal = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        tfBookingId = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        comboBooking = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        tableFood.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tableFood.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table_foodMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableFood);

        tableItem.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tableItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table_itemMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tableItem);

        jPanel1.setBackground(new java.awt.Color(230, 231, 232));

        tfQuantity.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tf_quantityKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tf_quantityKeyTyped(evt);
            }
        });

        jLabel1.setText("Item/Food");

        jLabel2.setText("Quantity");

        tfPrice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tf_priceKeyTyped(evt);
            }
        });

        jLabel3.setText("Total");

        tfBookingId.setBackground(new java.awt.Color(204, 255, 0));

        jLabel4.setText("Price");

        comboBooking.setEditable(true);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/SaveButton.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(comboBooking, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(18, 18, 18)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(tfBookingId)
                                .addComponent(tfFoodItem)
                                .addComponent(tfQuantity)
                                .addComponent(tfTotal)
                                .addComponent(tfPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(114, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(comboBooking, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(tfBookingId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(tfFoodItem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(tfPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(tfTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(91, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(71, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(66, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void table_foodMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_foodMouseClicked
        int row = tableFood.getSelectedRow();
        displayToTextField(row);
    }//GEN-LAST:event_table_foodMouseClicked

    private void tf_quantityKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tf_quantityKeyReleased
       int price = Integer.parseInt(tfPrice.getText());
        try{
           int quantity = Integer.parseInt(tfQuantity.getText());
            tfTotal.setText(quantity*price+"");
       }catch(Exception ex)
       {
       }
    }//GEN-LAST:event_tf_quantityKeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        db.insertOrder(new Order(
                Integer.parseInt(tfBookingId.getText()),
                tfFoodItem.getText(),
                Integer.parseInt(tfPrice.getText()),
                Integer.parseInt(tfQuantity.getText()),
                Integer.parseInt(tfTotal.getText())
                
        ));
    }//GEN-LAST:event_jButton1ActionPerformed

    private void table_itemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_itemMouseClicked
        int row = tableItem.getSelectedRow();
        displayToTextFieldFromItem(row);
    }//GEN-LAST:event_table_itemMouseClicked

    private void tf_priceKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tf_priceKeyTyped
        char c = evt.getKeyChar();
        
        if(!(Character.isDigit(c) || c== KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE ))
        {
            evt.consume();
        }
    }//GEN-LAST:event_tf_priceKeyTyped

    private void tf_quantityKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tf_quantityKeyTyped
        char c = evt.getKeyChar();
        
        if(!(Character.isDigit(c) || c== KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE ))
        {
            evt.consume();
        }
    }//GEN-LAST:event_tf_quantityKeyTyped

    
     private void displayToTextField(int row) {
        tfFoodItem.setText(tableFood.getModel().getValueAt(row, 1)+"");
        tfPrice.setText(tableFood.getModel().getValueAt(row, 2)+"");
       
    }
    // Method to display selected item details from the item table to the corresponding text fields
    private void displayToTextFieldFromItem(int row) {
        // Set the 'Food Item' text field with the value from column 1 of the selected row
        tfFoodItem.setText(tableItem.getModel().getValueAt(row, 1) + "");

        // Set the 'Price' text field with the value from column 2 of the selected row
        tfPrice.setText(tableItem.getModel().getValueAt(row, 2) + "");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(OrderPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(OrderPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(OrderPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(OrderPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                OrderPanel dialog = new OrderPanel(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox comboBooking;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tableFood;
    private javax.swing.JTable tableItem;
    private javax.swing.JTextField tfBookingId;
    private javax.swing.JTextField tfFoodItem;
    private javax.swing.JTextField tfPrice;
    private javax.swing.JTextField tfQuantity;
    private javax.swing.JTextField tfTotal;
    // End of variables declaration//GEN-END:variables

   
}
