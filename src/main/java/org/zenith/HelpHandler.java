package org.zenith;

import java.util.*;

public class HelpHandler {
    Map<String, Map<String, String>> commands = new HashMap<>();

    public HelpHandler() {
        commands.put("key", Map.of("list", "Lists all available keys",
                                   "create [name]", "Creates a key",
                                   "delete [name]", "Deletes a key"));
    }

    public void printCommands(String[] input) {
//        System.out.println(Arrays.toString(input));
        StringBuilder builder = new StringBuilder();

        if (input.length > 1 && commands.containsKey(input[1])) {
            commands.get(input[1])
                    .forEach((command, description) -> builder
                            .append(command)
                            .append(": ")
                            .append(description)
                            .append(System.lineSeparator()));
        } else {
            builder.append("exit: Exit the program").append(System.lineSeparator());
            commands.keySet().forEach(s -> builder.append(s).append(System.lineSeparator()));
        }



        System.out.println(builder.toString());
    }
}
