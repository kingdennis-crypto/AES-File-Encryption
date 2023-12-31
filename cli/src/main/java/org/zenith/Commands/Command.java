package org.zenith.Commands;

import org.zenith.Models.FunctionDescription;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Command {
    private final Map<String, FunctionDescription> functions;

    public Command() {
        functions = new HashMap<>();
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
        List<FunctionDescription> functionList = functions.values()
                .stream()
                .sorted(Comparator.comparingInt(o -> o.getUsage().length()))
                .toList();

        for (FunctionDescription function : functionList) {
            System.out.printf("%-20s %s%n", function.getUsage() + ":", function.getDescription());
        }
    }
}
