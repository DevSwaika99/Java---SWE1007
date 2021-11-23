package home;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import utils.ConnectionUtil;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private Label lblErrors;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label btnBack;

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnLogin;

    @FXML
    public void handleButtonAction(javafx.scene.input.MouseEvent mouseEvent) {

        if (mouseEvent.getSource() == btnBack) {
            System.exit(0);
        }

        if (mouseEvent.getSource() == btnLogin) {
            //login here
            if (logIn().equals("Success")) {
                try {
                    Node node = (Node) mouseEvent.getSource();
                    Stage stage = (Stage) node.getScene().getWindow();
                    stage.setMaximized(true);
                    stage.close();
                    Scene scene = new Scene(FXMLLoader.load(getClass().getResource("home.fxml")));
                    stage.setScene(scene);
                    stage.show();

                } catch (IOException ex) {
                    System.err.println(ex.getMessage());
                }
            }
        }
    }


    public void initialize(URL url, ResourceBundle rb) {

    }

    public LoginController() {
        con = ConnectionUtil.conDB();
    }

    Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    private String logIn() {
        String email = txtUsername.getText();
        String password = txtPassword.getText();

        String sql = "SELECT * FROM admin Where email = ? and password = ?";

        try {
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                lblErrors.setTextFill(Color.TOMATO);
                lblErrors.setText("Enter Correct Email or Password");
                System.err.println("Wrong credentials :/");
                return "Error";
            } else {
                lblErrors.setTextFill(Color.GREEN);
                lblErrors.setText("Login Successful.. Redirecting..");
                System.out.println("Successful Login");
                return "Success";
            }

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            return "Exception";
        }
    }


}

