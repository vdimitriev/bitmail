package mk.vedmak.bitmail.controller.services;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import mk.vedmak.bitmail.model.EmailTreeItem;
import mk.vedmak.bitmail.view.IconResolver;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.mail.event.MessageCountEvent;
import javax.mail.event.MessageCountListener;
import java.util.List;

public class FetchFolderService extends Service<Void> {
    
    private Store store;
    private EmailTreeItem<String> folderRoot;
    private List<Folder> folderList;
    private IconResolver iconResolver = new IconResolver();

    public FetchFolderService(Store store, EmailTreeItem<String> folderRoot, List<Folder> folderList) {
        this.store = store;
        this.folderRoot = folderRoot;
        this.folderList = folderList;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                fetchFolders();
                return null;
            }
        };
    }

    private void fetchFolders() throws MessagingException {
        final Folder[] folders = store.getDefaultFolder().list();
        handleFolders(folders, folderRoot);
    }

    private void handleFolders(Folder[] folders, EmailTreeItem<String> foldersRoot) throws MessagingException {
        for(Folder folder : folders) {
            folderList.add(folder);
            EmailTreeItem<String> treeItem = new EmailTreeItem<>(folder.getName());
            treeItem.setGraphic(iconResolver.getIconForFolder(folder.getName()));
            foldersRoot.getChildren().add(treeItem);
            foldersRoot.setExpanded(true);
            fetchMessagesOnFolder(folder, treeItem);
            addMessageListenerToFolder(folder, treeItem);
            if(folder.getType() == Folder.HOLDS_FOLDERS) {
                Folder[] subFolders = folder.list();
                handleFolders(subFolders, foldersRoot);
            }
        }
    }

    private void addMessageListenerToFolder(Folder folder, EmailTreeItem<String> emailTreeItem) {
        folder.addMessageCountListener(new MessageCountListener() {
            @Override
            public void messagesAdded(MessageCountEvent e) {
                System.out.println("message added event " + e);
                for(int i = 0; i < e.getMessages().length; i++) {
                    try {
                        Message message = folder.getMessage(folder.getMessageCount() - i);
                        emailTreeItem.addEmailAtTheTop(message);
                    } catch (MessagingException messagingException) {
                        messagingException.printStackTrace();
                    }
                }
            }
            @Override
            public void messagesRemoved(MessageCountEvent e) {
                System.out.println("message removed event" + e);
            }
        });
    }

    private void fetchMessagesOnFolder(Folder folder, EmailTreeItem<String> emailTreeItem) {
        Service fetchMessagesService = new Service() {
            @Override
            protected Task createTask() {
            return new Task() {
                @Override
                protected Object call() throws Exception {
                if(folder.getType() != Folder.HOLDS_FOLDERS) {
                    folder.open(Folder.READ_WRITE);
                    int folderSize = folder.getMessageCount();
                    for(int i = folderSize; i > 0; i-- ) {
                        System.out.println(folder.getMessage(i).getSubject());
                        emailTreeItem.addEmail(folder.getMessage(i));
                    }
                }
                return null;
                }
            };
            }
        };
        fetchMessagesService.start();
    }
}
