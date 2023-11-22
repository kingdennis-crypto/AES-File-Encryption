package org.zenith.Commands;

import org.zenith.Models.FunctionDescription;
import org.zenith.Models.KeyProperties;
import org.zenith.Utilities.ConfigurationHandler;
import org.zenith.Utilities.KeyUtils;

import java.util.Arrays;
import java.util.List;

public class KeyCommands extends Command {
    private final KeyUtils keyUtils;

    public KeyCommands() {
        super("key", "This command is to handle the key management");

        ConfigurationHandler configurationHandler = new ConfigurationHandler();
        keyUtils = new KeyUtils(configurationHandler.getProperty("keyFolderPath"));

        super.addFunction("create", new FunctionDescription("create [key]", "Create a key", this::createKey));
        super.addFunction("delete", new FunctionDescription("delete [key]", "Deletes a key", this::deleteKey));
        super.addFunction("get", new FunctionDescription("get [key]", "Gets a key", this::getKey));
        super.addFunction("list", new FunctionDescription("list", "Lists all generated keys", this::listKeys));
    }

    public void createKey(String[] data) {
        try {
            if (data.length > 2) {
                keyUtils.createKey(data[2]);
            } else {
                System.out.println("Missing key name. Usage: key create [keyName]");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void deleteKey(String[] data) {
        try {
            if (data.length > 2) {
                keyUtils.deleteKey(data[2]);
            } else {
                System.out.println("Missing key name. Usage: key delete [keyName]");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getKey(String[] data) {
        try {
            if (data.length > 2) {
                KeyProperties properties = keyUtils.getKey(data[2]);
                System.out.println("Secret key: " + Arrays.toString(properties.getSecretKey().getEncoded()));
            } else {
                System.out.println("Missing key name. Usage: key get [keyName]");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // TODO: Create function to select this key and to store it as usable key for the encryption
    public void selectKey(String[] data) {}

    public void listKeys(String[] data) {
        try {
            List<String> keys = keyUtils.listKeys();
            StringBuilder stringBuilder = new StringBuilder();

            for (int i = 0; i < keys.size(); i++) {
                stringBuilder
                        .append(String.format("%d. %s", i + 1, keys.get(i)))
                        .append(System.lineSeparator());
            }

            System.out.println(stringBuilder);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
