package org.zenith.Models;

import java.io.Serializable;

public class FileMetadata implements Serializable {
    private final String name;
    private final String absolutePath;

    public FileMetadata(String content) {
        content = content.substring(1, content.length() - 1);
        String[] data = content.split("/");

        this.name = data[data.length - 1];
        this.absolutePath = content;
    }

    public FileMetadata(String name, String absolutePath) {
        this.name = name;
        this.absolutePath = absolutePath;
    }

    public String filenameToHex() {
        // TODO: Returns the current file name as hex
        return "";
    }

    public String getName() {
        return name;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    @Override
    public String toString() {
        return String.format("{%s}", absolutePath);
    }
}
