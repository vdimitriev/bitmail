package mk.vedmak.bitmail.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;
import mk.vedmak.bitmail.EmailManager;
import mk.vedmak.bitmail.controller.services.EmailSenderService;
import mk.vedmak.bitmail.controller.services.EmailSendingResult;
import mk.vedmak.bitmail.model.EmailAccount;
import mk.vedmak.bitmail.view.ViewFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class ComposeMessageController extends BaseController implements Initializable {

    @FXML
    private TextField recipientTextField;

    @FXML
    private TextField subjectTextField;

    @FXML
    private Button sendButton;

    @FXML
    private Label errorLabel;

    @FXML
    private HTMLEditor htmlEditor;

    @FXML
    private ChoiceBox<EmailAccount> emailAccountChoice;


    public ComposeMessageController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    @FXML
    void sendButtonAction() {
        //System.out.println(htmlEditor.getHtmlText());
        EmailSenderService emailSenderService = new EmailSenderService(
            emailAccountChoice.getValue(),
            subjectTextField.getText(),
            recipientTextField.getText(),
            htmlEditor.getHtmlText()
        );
        emailSenderService.start();
        emailSenderService.setOnSucceeded(event -> {
            EmailSendingResult result = emailSenderService.getValue();
            switch(result) {
                case SUCCESS:
                    Stage stage = (Stage)recipientTextField.getScene().getWindow();
                    stage.close();
                    break;
                case FAILED_BY_PROVIDER:
                    errorLabel.setText("Provider error");
                    break;
                case FAILED_BY_UNEXPECTED_ERROR:
                    errorLabel.setText("Unexpected error");
                    break;
                default:
                    errorLabel.setText("Non predicted error");
                    break;
            }
        });
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        emailAccountChoice.setItems(emailManager.getEmailAccounts());
        emailAccountChoice.setValue(emailManager.getEmailAccounts().get(0));
    }
}
