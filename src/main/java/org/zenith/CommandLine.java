package org.zenith;

import org.zenith.Commands.Command;
import org.zenith.Commands.EncryptionCommands;
import org.zenith.Commands.KeyCommands;

import java.util.*;

public class CommandLine {
    private final Map<String, Command> commands;

    public CommandLine() {
        commands = new HashMap<>();

        commands.put("key", new KeyCommands());
        commands.put("encrypt", new EncryptionCommands());
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter your command (enter 'help' or 'h' for help): ");
            String userInput = scanner.nextLine();

            String[] commandParts = userInput.split(" ");

            if (commandParts.length > 0) {
                String command = commandParts[0];

                if (command.equals("exit")) {
                    System.out.println("Exiting the program.");
                    System.exit(0);
                } else if (command.equals("help") || command.equals("h")) {
                    System.out.println("All available commands (each command has it's own subcommands):\n");

                    if (commandParts.length > 1) {
                        Command cInterface = commands.get(commandParts[1]);
                        cInterface.getHelp();
                    } else {
                        for (Command cInterface : commands.values()) {
                            System.out.printf("%-20s %s%n", cInterface.getUsage() + ":", cInterface.getDescription());
                        }
                    }

                    System.out.println();
                } else if (commands.containsKey(command)) {
                    Command cInterface = commands.get(command);

                    if (commandParts.length > 1) {
                        cInterface.runFunction(commandParts[1], commandParts);
                    } else {
                        System.out.println();
                        cInterface.getHelp();
                        System.out.println();
                    }
                } else {
                    unknownCommand(command);
                }
            }
        }
    }

    private void unknownCommand(String command) {
        System.out.printf("Unknown command: %s%n", command);
    }
}
