package mk.vedmak.bitmail.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mk.vedmak.bitmail.EmailManager;
import mk.vedmak.bitmail.controller.services.EmailLoginResult;
import mk.vedmak.bitmail.controller.services.LoginService;
import mk.vedmak.bitmail.model.EmailAccount;
import mk.vedmak.bitmail.view.ViewFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginWindowController extends BaseController implements Initializable {

    @FXML
    private Button loginButton;

    @FXML
    private TextField emailAddressField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    public LoginWindowController(EmailManager emailManager,
                                 ViewFactory viewFactory,
                                 String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    @FXML
    void loginButtonAction() {
        if(fieldsAreValid()) {
            EmailAccount emailAccount = new EmailAccount(emailAddressField.getText(), passwordField.getText());
            LoginService loginService = new LoginService(emailAccount, emailManager);
            loginService.start();
            loginService.setOnSucceeded(event -> {
                EmailLoginResult emailLoginResult = loginService.getValue();
                switch(emailLoginResult) {
                    case SUCCESS:
                        System.out.println("login succesfull !!" + emailAccount);
                        if(!viewFactory.isMainViewInitialized()) {
                            viewFactory.showMainWindow();
                        }
                        final Stage stage = (Stage) errorLabel.getScene().getWindow();
                        viewFactory.closeStage(stage);
                        return;
                    case FAILED_BY_CREDENTIALS:
                        System.out.println("Invalid credentials");
                        return;
                    case FAILED_BY_NETWORK:
                        System.out.println("Network error");
                        return;
                    case FAILED_BY_UNEXPECTED_ERROR:
                        System.out.println("Unexpected error");
                        return;
                    default:
                        System.out.println("login not succesfull !!");
                        return;
                }
            });
        }
    }

    private boolean fieldsAreValid() {
        if(emailAddressField.getText().isBlank()) {
            errorLabel.setText("Please fill email");
            return false;
        }

        if(passwordField.getText().isBlank()) {
            errorLabel.setText("Please fill password");
            return false;
        }

        return true;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        emailAddressField.setText("slomir2021@gmail.com");
    }
}
