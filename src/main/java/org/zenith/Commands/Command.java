package org.zenith.Commands;

import org.zenith.Models.FunctionDescription;

import java.util.HashMap;
import java.util.Map;

public abstract class Command {
    private final String usage;
    private final String description;
    private final Map<String, FunctionDescription> functions;

    public Command(String usage, String description) {
        this.usage = usage;
        this.description = description;

        functions = new HashMap<>();
    }

    public String getUsage() {
        return usage;
    }

    public String getDescription() {
        return description;
    }

    public void addFunction(String key, FunctionDescription function) {
        functions.put(key, function);
    }

    public void runFunction(String key, String[] data) {
        FunctionDescription command = functions.get(key);

        if (command == null) {
            System.out.println("This function doesn't exist!");
            return;
        }

        command.getFunction().accept(data);
    }

    public void getHelp() {
        for (FunctionDescription function : functions.values()) {
            System.out.printf("%-20s %s%n", function.getUsage() + ":", function.getDescription());
        }
    }
}
