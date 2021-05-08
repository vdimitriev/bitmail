package mk.vedmak.bitmail.controller.persistence;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PersistenceAccess {

    //private String VALID_ACCOUNTS_LOCATION = System.getenv("APPDATA") + File.separator + "validAccounts.ser";
    private String VALID_ACCOUNTS_LOCATION = System.getProperty("user.home") + File.separator + "validAccounts.ser"; //.ser stands for serializable
    private Encoder encoder = new Encoder();

    public List<ValidAccount> loadFromPersistence() {
        List<ValidAccount> resultList = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(VALID_ACCOUNTS_LOCATION);
            ObjectInputStream ois = new ObjectInputStream(fis);
            List<ValidAccount> persistedList = (List<ValidAccount>)ois.readObject();
            decodePasswords(persistedList);
            resultList.addAll(persistedList);
            ois.close();
            fis.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }

    public void saveToPersistence(List<ValidAccount> validAccounts) {
        try {
            encodePasswords(validAccounts);
            File file = new File(VALID_ACCOUNTS_LOCATION);
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(validAccounts);
            oos.close();
            fos.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void encodePasswords(List<ValidAccount> validAccounts) {
        for(ValidAccount validAccount : validAccounts) {
            String originalPassword = validAccount.getPassword();
            validAccount.setPassword(encoder.encode(originalPassword));
        }
    }

    private void decodePasswords(List<ValidAccount> persistedList) {
        for(ValidAccount validAccount : persistedList) {
            String originalPassword = validAccount.getPassword();
            validAccount.setPassword(encoder.decode(originalPassword));
        }
    }
}
