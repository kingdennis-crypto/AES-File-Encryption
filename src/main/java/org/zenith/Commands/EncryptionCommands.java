package org.zenith.Commands;

import org.zenith.Models.FunctionDescription;
import org.zenith.Models.KeyProperties;
import org.zenith.Handlers.ConfigurationHandler;
import org.zenith.Handlers.EncryptionHandler;
import org.zenith.Handlers.KeyHandler;

import java.io.IOException;

public class EncryptionCommands extends Command {
    private final EncryptionHandler encryptionHandler;
    private final KeyHandler keyHandler;

    public EncryptionCommands() {
        super("encryption", "This command is to handle the encryption");

        encryptionHandler = new EncryptionHandler();
        keyHandler = new KeyHandler(ConfigurationHandler.getInstance().getProperty("KEY_PATH"));

        super.addFunction("encrypt", new FunctionDescription("encrypt [path]", "Encrypts the file located at the specified path.", this::encryptFile));
        super.addFunction("decrypt", new FunctionDescription("decrypt [path]", "Decrypts the file located at the specified path.", this::decryptFile));
    }

    private void encryptFile(String[] data) {
        try {
            if (data.length > 2) {
                KeyProperties key = keyHandler.getKey(ConfigurationHandler.getInstance().getProperty("SELECTED_KEY"));
                encryptionHandler.encryptFile(data[2], key.getSecretKey(), key.getNonce());

                System.out.println("Successfully encrypted");
            } else {
                System.out.println("No file path was given");
            }
        } catch (NullPointerException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void decryptFile(String[] data) {
        try {
            if (data.length > 2) {
                KeyProperties key = keyHandler.getKey(ConfigurationHandler.getInstance().getProperty("SELECTED_KEY"));
                encryptionHandler.decryptFile(data[2], key.getSecretKey(), key.getNonce());

                System.out.println("Successfully decrypted");
            } else {
                System.out.println("No file path was given");
            }
        } catch (NullPointerException ex) {
            System.err.println(ex.getMessage());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
