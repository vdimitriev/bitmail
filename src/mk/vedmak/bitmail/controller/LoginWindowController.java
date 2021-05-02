package mk.vedmak.bitmail.controller;

import javafx.fxml.FXML;
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

public class LoginWindowController extends BaseController {

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
            EmailLoginResult emailLoginResult = loginService.login();
            switch(emailLoginResult) {
                case SUCCESS:
                    System.out.println("login succesfull !!" + emailAccount);
                    break;
                default:
                    System.out.println("login not succesfull !!");
            }
        }
        System.out.println("Login button clicked !!");
        viewFactory.showMainWindow();
        final Stage stage = (Stage) errorLabel.getScene().getWindow();
        viewFactory.closeStage(stage);
    }

    private boolean fieldsAreValid() {
        if(emailAddressField.getText().isEmpty()) {
            errorLabel.setText("Please fill email");
            return false;
        }

        if(passwordField.getText().isEmpty()) {
            errorLabel.setText("Please fill email");
            return false;
        }

        return true;
    }

}
