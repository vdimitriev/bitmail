package mk.vedmak.bitmail.controller.services;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.web.WebEngine;
import mk.vedmak.bitmail.model.EmailMessage;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import java.io.IOException;

public class MessageRendererService extends Service {

    private EmailMessage emailMessage;
    private WebEngine webEngine;
    private StringBuffer stringBuffer;

    public MessageRendererService(WebEngine webEngine) {
        this.webEngine = webEngine;
        stringBuffer = new StringBuffer();
        this.setOnSucceeded(event -> {
            displayMessage();
        });
    }

    public void setEmailMessage(EmailMessage emailMessage) {
        this.emailMessage = emailMessage;
    }

    @Override
    protected Task createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                try {
                    loadMessage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    private void displayMessage() {
        webEngine.loadContent(stringBuffer.toString());
    }

    private void loadMessage() throws MessagingException, IOException {
        stringBuffer.setLength(0); //setting the length of the string buffer to zero clears the string buffer
        Message message = emailMessage.getMessage();
        String contentType = message.getContentType();
        if(isSimpleType(contentType)) {
            stringBuffer.append(message.getContent().toString());
        } else if(isMultipartType(contentType)) {
            Multipart multipart = (Multipart) message.getContent();
            loadMultipart(multipart, stringBuffer);
        }
    }

    private void loadMultipart(Multipart multipart, StringBuffer stringBuffer) throws MessagingException, IOException {
        for(int i = multipart.getCount() - 1; i >= 0; i--) {
            BodyPart bodyPart = multipart.getBodyPart(i);
            String contentType = bodyPart.getContentType();
            if(isSimpleType(contentType)) {
                stringBuffer.append(bodyPart.getContent().toString());
            } else if(isMultipartType(contentType)) {
                Multipart mp = (Multipart) bodyPart.getContent();
                loadMultipart(mp, stringBuffer);
            } else if(!isTextPlain(contentType)) {
                MimeBodyPart mbp = (MimeBodyPart) bodyPart;
                emailMessage.addAttachment(mbp);
            }
        }
    }

    private boolean isTextPlain(String contentType) {
        return contentType.contains("TEXT/PLAIN");
    }

    private boolean isImageType(String contentType) {
        return contentType.contains("IMAGE/PNG");
    }

    private boolean isSimpleType(String contentType) {
        return contentType.contains("TEXT/HTML") ||
            contentType.contains("mixed") ||
            contentType.contains("text");
    }

    private boolean isMultipartType(String contentType) {
        if(contentType.contains("multipart")) {
            return true;
        } else {
            return false;
        }
    }
}
