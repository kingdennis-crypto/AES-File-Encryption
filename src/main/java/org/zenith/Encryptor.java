package org.zenith;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Encryptor {
    public Encryptor() {}

    public void run() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to my encryption tool!\n");

        while (true) {
            System.out.print("\nEnter a command (\"h\" for help): ");
            String input = scanner.nextLine();

            if (input.isEmpty()) {
                System.out.println("You haven't provided any command!");
                continue;
            }

            String[] inputList = input.trim().split("\s");
            String command = inputList[0];

            if (command.equals("exit")) {
                break;
            }

            switch (command) {
                case "h":
                    HelpHandler helpHandler = new HelpHandler();
                    helpHandler.printCommands(inputList);
                    break;
                default:
                    unknownCommand(command);
            }
        }
    }

    public void unknownCommand(String command) {
        System.out.printf("Unknown command: %s%n", command);
    }
}
