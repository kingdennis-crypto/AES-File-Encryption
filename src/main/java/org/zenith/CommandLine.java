package org.zenith;

import org.zenith.Commands.KeyCommands;
import org.zenith.Interfaces.ICommand;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CommandLine {
    private Map<String, ICommand> commands;

    public CommandLine() {
        commands = new HashMap<>();

        commands.put("key", new KeyCommands());
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter your command: ");
            String userInput = scanner.nextLine();

            String[] commandParts = userInput.split(" ");
            System.out.println(Arrays.toString(commandParts));

            if (commandParts.length > 0) {
                String command = commandParts[0];

                if (command.equals("exit")) {
                    System.out.println("Exiting the program.");
                    System.exit(0);
                }

                if (commands.containsKey(command)) {
                    commands.get(command).runCommand(commandParts);
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
