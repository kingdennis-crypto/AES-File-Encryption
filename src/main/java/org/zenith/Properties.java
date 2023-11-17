package org.zenith;

import java.io.FileInputStream;
import java.io.IOException;

public class Properties {
    public static String getProperty(String key) {
        try {
            String configPath = Properties.class.getClassLoader().getResource("config.properties").getPath();

            java.util.Properties config = new java.util.Properties();
            config.load(new FileInputStream(configPath));

            return config.getProperty(key);
        } catch (IOException ex) {
            return null;
        }
    }
}
