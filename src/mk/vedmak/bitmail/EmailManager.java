package mk.vedmak.bitmail;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import mk.vedmak.bitmail.controller.services.FetchFolderService;
import mk.vedmak.bitmail.controller.services.FolderUpdaterService;
import mk.vedmak.bitmail.model.EmailAccount;
import mk.vedmak.bitmail.model.EmailMessage;
import mk.vedmak.bitmail.model.EmailTreeItem;
import mk.vedmak.bitmail.view.IconResolver;

import javax.mail.Flags;
import javax.mail.Folder;
import java.util.ArrayList;
import java.util.List;

public class EmailManager {

    private EmailMessage selectedMessage;
    private EmailTreeItem<String> selectedFolder;
    private ObservableList<EmailAccount> emailAccounts = FXCollections.observableArrayList();
    private IconResolver iconResolver = new IconResolver();

    //Folder handling:
    private EmailTreeItem<String> foldersRoot = new EmailTreeItem<>("");

    public EmailTreeItem<String> getFoldersRoot() {
        return foldersRoot;
    }

    private List<Folder> folderList = new ArrayList<>();

    private FolderUpdaterService folderUpdaterService;

    public EmailManager() {
        folderUpdaterService = new FolderUpdaterService(folderList);
        folderUpdaterService.start();
    }

    public void addEmailAccount(EmailAccount emailAccount) {
        emailAccounts.add(emailAccount);
        final EmailTreeItem<String> treeItem = new EmailTreeItem<>(emailAccount.getAddress());
        treeItem.setGraphic(iconResolver.getIconForFolder(emailAccount.getAddress()));
        FetchFolderService fetchFolderService = new FetchFolderService(emailAccount.getStore(), treeItem, folderList);
        fetchFolderService.start();
        foldersRoot.getChildren().add(treeItem);
    }

    public List<Folder> getFolderList() {
        return folderList;
    }

    public EmailMessage getSelectedMessage() {
        return selectedMessage;
    }

    public void setSelectedMessage(EmailMessage selectedMessage) {
        this.selectedMessage = selectedMessage;
    }

    public EmailTreeItem<String> getSelectedFolder() {
        return selectedFolder;
    }

    public void setSelectedFolder(EmailTreeItem<String> selectedFolder) {
        this.selectedFolder = selectedFolder;
    }

    public void setFoldersRoot(EmailTreeItem<String> foldersRoot) {
        this.foldersRoot = foldersRoot;
    }

    public void setFolderList(List<Folder> folderList) {
        this.folderList = folderList;
    }

    public FolderUpdaterService getFolderUpdaterService() {
        return folderUpdaterService;
    }

    public void setFolderUpdaterService(FolderUpdaterService folderUpdaterService) {
        this.folderUpdaterService = folderUpdaterService;
    }

    public void setRead() {
        try {
            selectedMessage.setRead(true);
            selectedMessage.getMessage().setFlag(Flags.Flag.SEEN, true);
            selectedFolder.decrementMessagesCount();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void setUnRead() {
        try {
            selectedMessage.setRead(false);
            selectedMessage.getMessage().setFlag(Flags.Flag.SEEN, false);
            selectedFolder.incrementMessagesCount();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteSelectedMessage() {
        try {
            selectedMessage.getMessage().setFlag(Flags.Flag.DELETED, true);
            selectedFolder.getEmailMessages().remove(selectedMessage);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public ObservableList<EmailAccount> getEmailAccounts() {
        return emailAccounts;
    }
}
