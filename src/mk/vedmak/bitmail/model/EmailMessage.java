package mk.vedmak.bitmail.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import javax.mail.Message;
import java.util.Date;

public class EmailMessage {

    private SimpleStringProperty subject;
    private SimpleStringProperty sender;
    private SimpleStringProperty recipient;
    private SimpleObjectProperty size;
    private SimpleObjectProperty<Date> date;
    private boolean isRead;
    private Message message;

    public EmailMessage(String subject,
                        String sender,
                        String recipient,
                        int size,
                        Date date,
                        boolean isRead,
                        Message message) {
        this.subject = new SimpleStringProperty(subject);
        this.sender = new SimpleStringProperty(sender);
        this.recipient = new SimpleStringProperty(recipient);
        this.size = new SimpleObjectProperty<>(new SizeInteger(size));
        this.date = new SimpleObjectProperty<>(date);
        this.isRead = isRead;
        this.message = message;
    }

    public String getSubject() {
        return subject.get();
    }

    public String getSender() {
        return sender.get();
    }

    public String getRecipient() {
        return recipient.get();
    }

    public SizeInteger getSize() {
        return (SizeInteger) size.get();
    }

    public Date getDate() {
        return date.get();
    }

    public Boolean isRead() {
        return isRead;
    }

    public Message getMessage() {
        return message;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
