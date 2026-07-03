import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.*;
import com.toedter.calendar.JDateChooser;
import java.sql.*;



public class Main {
    static void main(String[] args) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "tiger"
        );



        Statement stmt = conn.createStatement();
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS expenses (" +
                "id SERIAL PRIMARY KEY," +
                "amount DOUBLE PRECISION," +
                "category VARCHAR(50)," +
                "date VARCHAR(20)," +
                "note VARCHAR(200))");
        

        JFrame frame = new JFrame("Expense Tracker");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Input panel (labels + fields)
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));

        JTextField amountField = new JTextField(10);
        String[] categories = {"Food", "Travel", "Rent", "Shopping", "Other"};
        JComboBox<String> categoryField = new JComboBox<>(categories);
        JTextField noteField = new JTextField(10);
        //DATE FUNCTION USING DATE JAR FILE
        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd/MM/yyyy");

        inputPanel.add(new JLabel("Amount"));
        inputPanel.add(amountField);
        inputPanel.add(new JLabel("Category"));
        inputPanel.add(categoryField);
        inputPanel.add(new JLabel("Note"));
        inputPanel.add(noteField);
        inputPanel.add(new JLabel("Date"));
        inputPanel.add(dateChooser);

        // Button panel
        JButton addButton = new JButton("Add expense");
        JLabel totalLabel = new JLabel("Total: ₹0");
        JButton clearButton = new JButton("Clear expenses");


        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JPanel buttonPanel = new JPanel();
        bottomPanel.add(addButton);
        bottomPanel.add(totalLabel);
        bottomPanel.add(clearButton);


        // Table setup
        String[] columns = {"Amount", "Category", "Date", "Note"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Add everything to frame
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setVisible(true);


        //loading data at startup
        ResultSet rs = stmt.executeQuery("SELECT * FROM expenses");
        while (rs.next()) {
            tableModel.addRow(new Object[]{
                    rs.getDouble("amount"),
                    rs.getString("category"),
                    rs.getString("date"),
                    rs.getString("note")
            });
        }

        // Button action
        addButton.addActionListener(new ActionListener() {
            double total = 0.0;

            public void actionPerformed(ActionEvent e) {

                String amountText = amountField.getText();
                String categoryText = categoryField.getSelectedItem().toString();
                //Getting date
                String datetext = ((JTextField) dateChooser.getDateEditor().getUiComponent()).getText();
                String noteText = noteField.getText();

                // Add row
                tableModel.addRow(new Object[]{"₹" + amountText, categoryText, datetext, noteText});

                //adding data in database
                try {
                    PreparedStatement ps = conn.prepareStatement(
                            "INSERT INTO expenses(amount, category, date, note) VALUES(?, ?, ?, ?)"
                    );
                    ps.setDouble(1, Double.parseDouble(amountField.getText()));
                    ps.setString(2, categoryField.getSelectedItem().toString());
                    ps.setString(3, ((JTextField) dateChooser.getDateEditor().getUiComponent()).getText());
                    ps.setString(4, noteField.getText());

                    ps.executeUpdate();

                } catch (SQLException except) {
                    except.printStackTrace();
                }



                //total amount updation
                try{
                    double amount = Double.parseDouble(amountField.getText());
                    total = total + amount;
                    totalLabel.setText("Total: ₹" + total);
                }catch(NumberFormatException ex){
                    JOptionPane.showMessageDialog(frame, "Please enter a valid number for Amount");
                    return;
                }



                // Clear fields
                amountField.setText("");
                categoryField.setSelectedIndex(0);
                noteField.setText("");
                dateChooser.setDate(null);

                JOptionPane.showMessageDialog(frame, "Entry added");

                clearButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {

                    }
                });


            }
        });
        conn.close();
    }
}
