package org.zenith.Models;

import java.io.Serializable;

public class FileMetadata implements Serializable {
    private String name;
    private String absolutePath;

    public FileMetadata(String content) {
        content = content.substring(1, content.length() - 1);
        String[] data = content.split(",");

        this.name = data[0];
        this.absolutePath = data[1];
    }

    public FileMetadata(String name, String absolutePath) {
        this.name = name;
        this.absolutePath = absolutePath;
    }

    public String getName() {
        return name;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    @Override
    public String toString() {
        return String.format("{%s,%s}", name, absolutePath);
    }
}
