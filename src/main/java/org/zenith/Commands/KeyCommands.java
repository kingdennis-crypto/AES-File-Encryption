package org.zenith.Commands;

import org.zenith.Interfaces.ICommand;
import org.zenith.Utilities.KeyUtils;

public class KeyCommands implements ICommand {
    KeyUtils keyUtils;

    public KeyCommands() {
        keyUtils = new KeyUtils();
    }

    @Override
    public void runCommand(String[] commandList) {
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
                case "list":
                    System.out.println(keyUtils.listKeys());
                    break;
                default:
                    System.out.println("Invalid subcommand for 'key'. Try 'create' or 'delete'.");
            }
        }
    }
}
