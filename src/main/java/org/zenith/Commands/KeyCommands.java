package org.zenith.Commands;

import org.zenith.Interfaces.ICommand;
import org.zenith.Models.KeyProperties;
import org.zenith.Utilities.ConfigurationHandler;
import org.zenith.Utilities.KeyUtils;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

public class KeyCommands implements ICommand {
    KeyUtils keyUtils;

    public KeyCommands() {
        ConfigurationHandler configurationHandler = new ConfigurationHandler();
        keyUtils = new KeyUtils(configurationHandler.getProperty("keyFolderPath"));
    }

    @Override
    public void runCommand(String[] commandList) {
        try {
            if (commandList.length > 1) {
                String subCommand = commandList[1];

                switch (subCommand) {
                    case "create":
                        if (commandList.length > 2) {
                            keyUtils.createKey(commandList[2]);
                        } else {
                            System.out.println("Missing key name. Usage: key create [keyName]");
                        }

                        break;
                    case "delete":
                        if (commandList.length > 2) {
                            keyUtils.deleteKey(commandList[2]);
                        } else {
                            System.out.println("Missing key name. Usage: key delete [keyName]");
                        }

                        break;
                    case "get":
                        if (commandList.length > 2) {
                            KeyProperties properties = keyUtils.getKey(commandList[2]);

                            System.out.println("Secret key: " + Arrays.toString(properties.getSecretKey().getEncoded()));
                        } else {
                            System.out.println("Missing key name. Usage: key get [keyName]");
                        }

                        break;
                    case "select":
                        // TODO: Create function to select this key and to store it as usable key for the encryption
                        System.out.println("Selecting the key");
                    case "list":
                        List<String> keys = keyUtils.listKeys();
                        StringBuilder stringBuilder = new StringBuilder();

                        for (int i = 0; i < keys.size(); i++) {
                            stringBuilder
                                    .append(String.format("%d. %s", i + 1, keys.get(i)))
                                    .append(System.lineSeparator());
                        }

                        System.out.println(stringBuilder.toString());

                        break;
                    default:
                        System.out.println("Invalid subcommand for 'key'. Try 'create' or 'delete'.");
                }
            }
        } catch (FileAlreadyExistsException ex) {
            System.out.printf("The key with the name: '%s' already exists", commandList[2]);
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Wrong algorithm");
        } catch (IOException ex) {
            System.out.println("Something went wrong when trying to create the file");
        }

    }
}
