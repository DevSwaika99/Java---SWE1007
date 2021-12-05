package home;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import utils.ConnectionUtil;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Home implements Initializable {

    @FXML
    private TextField txtFirstname;

    @FXML
    private TextField txtLastname;

    @FXML
    private TextField txtEmail;

    @FXML
    private ComboBox<String> txtGender;

    @FXML
    private DatePicker txtleave;

    @FXML
    private Button btnSave;

    @FXML
    private DatePicker txtrejoin;

    @FXML
    private Button btnReset;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnShowAll;

    @FXML
    private Label lblStatus;

    @FXML
    private TableView<ObservableList> tblData;

    PreparedStatement preparedStatement;
    Connection connection;

    public Home() {
        connection = ConnectionUtil.conDB();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> list = FXCollections.observableArrayList("Male", "Female", "Other");
        txtGender.setItems(list);
        fetColumnList();
        fetRowList();
    }

    @FXML
    private void HandleEvents(MouseEvent event) {
        if (event.getSource() == btnSave) {
            if (txtEmail.getText().isEmpty() || txtFirstname.getText().isEmpty() || txtLastname.getText().isEmpty()) {
                lblStatus.setTextFill(Color.TOMATO);
                lblStatus.setText("Enter all details");
            } else {
                txtleave.getValue();
                if (txtEmail.getText().matches("[a-zA-z0-9._%+-]+@[a-zA-z0-9._%+-]+")) {
                    if (txtFirstname.getText().matches("[a-z A-Z]+") && txtLastname.getText().matches("[a-z A-Z]+")) {
                        saveData();
                    } else {
                        lblStatus.setTextFill(Color.TOMATO);
                        lblStatus.setText("Incorrect format of firstname/ lastname/ email");
                    }
                } else {
                    lblStatus.setTextFill(Color.TOMATO);
                    lblStatus.setText("Incorrect format of firstname/ lastname/ email");
                }
            }
        }
        if (event.getSource() == btnDelete) {
            deleteRow();
            lblStatus.setTextFill(Color.TOMATO);
            lblStatus.setText("Entry Deleted");
        }
        if (event.getSource() == btnReset) {
            clearFields();
            lblStatus.setTextFill(Color.GREEN);
            lblStatus.setText("Input Fields Reset");
        }
        if (event.getSource() == btnShowAll) {
            fetColumnList();
            lblStatus.setTextFill(Color.TOMATO);
            lblStatus.setText("All data printed");
        }
    }

    String SQL = "SELECT * from emps";

    private void clearFields() {
        txtFirstname.clear();
        txtLastname.clear();
        txtEmail.clear();
        txtleave.getEditor().clear();
        txtrejoin.getEditor().clear();
        txtGender.getEditor().clear();

    }

    private void deleteRow() {
        tblData.getColumns().clear();
    }

    private void saveData() {

        try {
            String st = "INSERT INTO emps ( firstname, lastname, email, gender, leavedate, rejoindate, ndays) VALUES (?,?,?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(st);
            preparedStatement.setString(1, txtFirstname.getText());
            preparedStatement.setString(2, txtLastname.getText());
            preparedStatement.setString(3, txtEmail.getText());
            preparedStatement.setString(4, txtGender.getValue());
            preparedStatement.setString(5, txtleave.getValue().toString());
            preparedStatement.setString(6, txtrejoin.getValue().toString());
            long days = txtrejoin.getValue().toEpochDay() - txtleave.getValue().toEpochDay();
            preparedStatement.setString(7, String.valueOf(days));
            System.out.println(days);
            preparedStatement.executeUpdate();
            lblStatus.setTextFill(Color.GREEN);
            lblStatus.setText("Added Successfully");

            fetRowList();
            // clear fields
            clearFields();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            lblStatus.setTextFill(Color.TOMATO);
            lblStatus.setText(ex.getMessage());
        }
    }

    // only fetch columns
    private void fetColumnList() {

        try {
            ResultSet rs = connection.createStatement().executeQuery(SQL);

            // SQL FOR SELECTING ALL OF EMPLOYEES
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                // We are using non property style for making dynamic table
                final int j = i;
                var col = new TableColumn(rs.getMetaData().getColumnName(i + 1).toUpperCase());
                col.setCellValueFactory(
                        (Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(
                                param.getValue().get(j).toString()));

                tblData.getColumns().removeAll(col);
                tblData.getColumns().addAll(col);

                System.out.println("Column [" + i + "] ");

            }

        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());

        }
    }

    // fetches rows and data from the list
    private void fetRowList() {
        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        ResultSet rs;
        try {
            rs = connection.createStatement().executeQuery(SQL);

            while (rs.next()) {
                // Iterate Row
                var row = FXCollections.observableArrayList();
                int i = 1;
                while (i <= rs.getMetaData().getColumnCount()) {
                    // Iterate Column
                    row.add(rs.getString(i));
                    i++;
                }
                System.out.println("Row [1] added " + row);
                data.add(row);
            }
            tblData.setItems(data);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
