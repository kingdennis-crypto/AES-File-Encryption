package org.zenith.Models;

import java.util.function.Consumer;

public class FunctionDescription {
    private final String usage;
    private final String description;
    private final Consumer<String[]> function;

    public FunctionDescription(String usage, String description, Consumer<String[]> function) {
        this.usage = usage;
        this.description = description;
        this.function = function;
    }

    public String getUsage() {
        return usage;
    }

    public String getDescription() {
        return description;
    }

    public Consumer<String[]> getFunction() {
        return function;
    }
}
