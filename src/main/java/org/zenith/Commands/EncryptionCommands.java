package org.zenith.Commands;

import org.zenith.Models.FunctionDescription;
import org.zenith.Models.KeyProperties;
import org.zenith.Utilities.ConfigurationHandler;
import org.zenith.Utilities.EncryptionHandler;
import org.zenith.Utilities.KeyUtils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class EncryptionCommands extends Command {
    private final EncryptionHandler encryptionHandler;
    private final KeyUtils keyUtils;

    public EncryptionCommands() {
        super("encryption", "This command is to handle the encryption");

        ConfigurationHandler configurationHandler = new ConfigurationHandler();
        encryptionHandler = new EncryptionHandler();
        keyUtils = new KeyUtils(configurationHandler.getProperty("keyFolderPath"));

        super.addFunction("test", new FunctionDescription("text", "Encrypts a string", this::encryptText));
    }

    public void encryptText(String[] data) {
        try {
            KeyProperties key = keyUtils.getKey("key1");

            if (data.length > 2) {
                byte[] encrypted = encryptionHandler.encrypt(data[2].getBytes(), key.getSecretKey(), key.getNonce());
                String decrypted = encryptionHandler.decrypt(encrypted, key.getSecretKey(), key.getNonce());

                System.out.println(new String(encrypted, StandardCharsets.UTF_8));
                System.out.println(Arrays.toString(encrypted));
                System.out.println(decrypted);
            } else {
                System.out.println("Missing text to encrypt");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
