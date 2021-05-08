package mk.vedmak.bitmail.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;

public class EmailTreeItem<String> extends TreeItem<String> {

    private String name;
    private ObservableList<EmailMessage> emailMessages;
    private int unreadMessagesCount;

    public EmailTreeItem(String name) {
        super(name);
        this.name = name;
        this.emailMessages = FXCollections.observableArrayList();
    }



    public void addEmail(Message message) throws MessagingException {
        final EmailMessage emailMessage = fetchMessage(message);
        emailMessages.add(emailMessage);
        //System.out.println("added to " + name + " " + emailMessage.getSubject());
    }

    private EmailMessage fetchMessage(Message message) throws MessagingException {
        final boolean messageIsRead = message.getFlags().contains(Flags.Flag.SEEN);
        final EmailMessage emailMessage = new EmailMessage(message.getSubject(),
                message.getFrom()[0].toString(),
                message.getRecipients(Message.RecipientType.TO)[0].toString(),
                message.getSize(),
                message.getSentDate(),
                messageIsRead,
                message);
        if(!messageIsRead) {
            incrementMessagesCount();
        }
        return emailMessage;
    }

    public void incrementMessagesCount() {
        unreadMessagesCount++;
        updateName();
    }

    public void decrementMessagesCount() {
        unreadMessagesCount--;
        updateName();
    }

    private void updateName() {
        if(unreadMessagesCount > 0) {
            this.setValue((String) (name + "(" + unreadMessagesCount + ")"));
        } else {
            this.setValue(name);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ObservableList<EmailMessage> getEmailMessages() {
        return emailMessages;
    }

    public void setEmailMessages(ObservableList<EmailMessage> emailMessages) {
        this.emailMessages = emailMessages;
    }

    public void addEmailAtTheTop(Message message) throws MessagingException {
        final EmailMessage emailMessage = fetchMessage(message);
        emailMessages.add(0, emailMessage);
    }
}
