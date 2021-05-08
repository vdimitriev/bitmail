package mk.vedmak.bitmail.controller;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;
import mk.vedmak.bitmail.EmailManager;
import mk.vedmak.bitmail.controller.services.MessageRendererService;
import mk.vedmak.bitmail.model.EmailMessage;
import mk.vedmak.bitmail.view.ViewFactory;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class EmailDetailsController extends BaseController implements Initializable {

    private String LOCATION_OF_DOWNLOADS = System.getProperty("user.home") + "/Downloads/";
    @FXML
    private WebView webView;

    @FXML
    private Label subjectLabel;

    @FXML
    private Label senderLabel;

    @FXML
    private Label attachmentsLabel;

    @FXML
    private HBox hBoxDownloads;

    public EmailDetailsController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        EmailMessage emailMessage = emailManager.getSelectedMessage();
        subjectLabel.setText(emailMessage.getSubject());
        senderLabel.setText(emailMessage.getSender());
        try {
            loadAttachments(emailMessage);
        }catch(MessagingException e) {
            e.printStackTrace();
        }
        MessageRendererService mrs = new MessageRendererService(webView.getEngine());
        mrs.setEmailMessage(emailMessage);
        mrs.restart();
    }

    private void loadAttachments(EmailMessage emailMessage) throws MessagingException {
        if(emailMessage.hasAttachments()) {
            for(MimeBodyPart mbp : emailMessage.getAttachmentList()) {
                try {
                    AttachmentButton button = new AttachmentButton(mbp);
                    hBoxDownloads.getChildren().add(button);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            attachmentsLabel.setText("");
        }
    }

    private class AttachmentButton extends Button {
        private MimeBodyPart mbp;
        private String downloadedFilePath;
        public AttachmentButton(MimeBodyPart mbp) throws MessagingException {
            this.mbp = mbp;
            this.setText(mbp.getFileName());
            this.downloadedFilePath = LOCATION_OF_DOWNLOADS + mbp.getFileName();
            this.setOnAction(event -> downloadAttachment());
        }

        private void downloadAttachment() {
            colorBlue();
            Service service = new Service() {
                @Override
                protected Task createTask() {
                    return new Task() {
                        @Override
                        protected Object call() throws Exception {
                            mbp.saveFile(downloadedFilePath);
                            return null;
                        }
                    };
                }
            };
            service.restart();
            service.setOnSucceeded(event -> {
                colorGreen();
                this.setOnAction(evt -> {
//                    This only works for windows
//
//                    File file = new File(downloadedFilePath);
//
//                    Desktop desktop = Desktop.getDesktop();
//                    if(file.exists()) {
//                        try {
//                            desktop.open(file);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
                });
            });
        }

        private void colorBlue() {
            this.setStyle("-fx-background-color: blue");
        }

        private void colorGreen() {
            this.setStyle("-fx-background-color: green");
        }
    }
}
