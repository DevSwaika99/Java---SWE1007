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

public class HomeController implements Initializable {

    @FXML
    private TextField txtFirstname;

    @FXML
    private TextField txtLastname;

    @FXML
    private TextField txtEmail;

    @FXML
    private ComboBox<String> txtGender;

    @FXML
    private DatePicker txtDOB;

    @FXML
    private Button btnSave;

    @FXML
    private DatePicker txtDOB1;

    @FXML
    private Button btnReset;

    @FXML
    private Button btnDelete;

    @FXML
    private Label lblStatus;

    @FXML
    private TableView<ObservableList> tblData;



    PreparedStatement preparedStatement;
    Connection connection;

    public HomeController() {
        connection = (Connection) ConnectionUtil.conDB();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        ObservableList<String> list = FXCollections.observableArrayList("Male", "Female", "Other");
        txtGender.setItems(list);
        fetColumnList();
        fetRowList();
    }

    @FXML
    private void HandleEvents(MouseEvent event) {
        if (event.getSource() == btnSave) {
            if (txtEmail.getText().isEmpty() || txtFirstname.getText().isEmpty() || txtLastname.getText().isEmpty() ||
                    txtDOB.getValue().equals(null)) {
                lblStatus.setTextFill(Color.TOMATO);
                lblStatus.setText("Enter all details");
            } else if (txtFirstname.getText().matches("[a-z A-Z]+") && txtLastname.getText().matches("[a-z A-Z]+")
                    && txtEmail.getText().matches("[a-zA-z0-9._%+-]+@[a-zA-z0-9._%+-]+")) {
                saveData();
            } else {
                lblStatus.setTextFill(Color.TOMATO);
                lblStatus.setText("Incorrect format of firstname/ lastname/ email");
            }
        }
        if(event.getSource() == btnDelete){
            deleteRow();
            lblStatus.setTextFill(Color.TOMATO);
            lblStatus.setText("Entry Deleted");
        }
        if(event.getSource() == btnReset){
            clearFields();
            lblStatus.setTextFill(Color.GREEN);
            lblStatus.setText("Input Fields Reset");
        }
    }

    private void clearFields() {
        txtFirstname.clear();
        txtLastname.clear();
        txtEmail.clear();
        txtDOB.getEditor().clear();
        txtDOB1.getEditor().clear();
        txtGender.getEditor().clear();
    }

    private void deleteRow() {
        tblData.getColumns().clear();
    }


    private String saveData() {

        try {
            String st = "INSERT INTO emps ( firstname, lastname, email, gender, leavedate, rejoindate) VALUES (?,?,?,?,?,?)";
            preparedStatement = (PreparedStatement) connection.prepareStatement(st);
            preparedStatement.setString(1, txtFirstname.getText());
            preparedStatement.setString(2, txtLastname.getText());
            preparedStatement.setString(3, txtEmail.getText());
            preparedStatement.setString(4, txtGender.getValue().toString());
            preparedStatement.setString(5, txtDOB.getValue().toString());
            preparedStatement.setString(6, txtDOB1.getValue().toString());

            preparedStatement.executeUpdate();
            lblStatus.setTextFill(Color.GREEN);
            lblStatus.setText("Added Successfully");

            fetRowList();
            //clear fields
            clearFields();
            return "Success";

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            lblStatus.setTextFill(Color.TOMATO);
            lblStatus.setText(ex.getMessage());
            return "Exception";
        }
    }

    private ObservableList<ObservableList> data;
    String SQL = "SELECT * from emps";

    //only fetch columns
    private void fetColumnList() {

        try {
            ResultSet rs = connection.createStatement().executeQuery(SQL);

            //SQL FOR SELECTING ALL OF CUSTOMER
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1).toUpperCase());
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                tblData.getColumns().removeAll(col);
                tblData.getColumns().addAll(col);

                System.out.println("Column [" + i + "] ");

            }

        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());

        }
    }

    //fetches rows and data from the list
    private void fetRowList() {
        data = FXCollections.observableArrayList();
        ResultSet rs;
        try {
            rs = connection.createStatement().executeQuery(SQL);

            while (rs.next()) {
                //Iterate Row
                ObservableList row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs.getString(i));
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

