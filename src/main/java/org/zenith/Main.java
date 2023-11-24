package org.zenith;

import org.zenith.Handlers.ConfigurationHandler;

public class Main {
    public static void main(String[] args) {
        ConfigurationHandler.getInstance();

        CommandLine cmd = new CommandLine();
        cmd.start();
    }
}