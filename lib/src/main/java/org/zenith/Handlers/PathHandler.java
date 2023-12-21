package org.zenith.Handlers;

public class PathHandler {
    /**
     * Constructs and returns the full path for a folder within the user data directory.
     * @param folderName The name of the folder for which the path is to be constructed.
     * @return A string representing the full path for the specified folder.
     */
    public static String getFolderPath(String folderName) {
        return String.format("%s/%s/", getUserDataDirectory(), folderName);
    }

    /**
     * Retrieves the user data directory based on operating system
     *
     * @return A String representing the user data directory
     */
    private static String getUserDataDirectory() {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            // Windows
            return System.getenv("APPDATA");
        } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
            // Unix/Linux/Mac
            return System.getProperty("user.home");
        } else {
            // Unknown operating system
            return null;
        }
    }
}
