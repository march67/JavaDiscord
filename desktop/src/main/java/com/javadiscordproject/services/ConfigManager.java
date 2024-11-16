// ConfigManager.java

package com.javadiscordproject.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The ConfigManager class is responsible for loading and managing configuration properties from
 * the "config.properties" file, allowing other classes to access configuration values.
 */
public class ConfigManager {
    private static final Properties properties = new Properties();
    private static final Logger logger = Logger.getLogger(ConfigManager.class.getName());

    // Static initializer block to load properties from the configuration file at runtime.
    static {
        try (InputStream input = ConfigManager.class.getResourceAsStream("/config/config.properties")) {
            if (input != null) {
                properties.load(input);
            } else {
                logger.warning("Configuration file not found. Using default values.");
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Failed to load configuration file", ex);
        }
    }

    /**
     * Retrieves the property value associated with the given key.
     *
     * @param key The key for the desired property.
     * @return The property value as a String, or null if the key is not found.
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Retrieves an integer property value associated with the given key.
     * If the property is not found or cannot be converted to an integer, it returns the specified default value.
     *
     * @param key The key for the desired integer property.
     * @param defaultValue The default value to return if the property is missing or not an integer.
     * @return The integer property value or the default value if parsing fails.
     */
    public static int getIntProperty(String key, int defaultValue) {
        String value = properties.getProperty(key);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException | NullPointerException e) {
            logger.log(Level.WARNING, "Invalid integer for key: " + key + ", returning default value: " + defaultValue, e);
            return defaultValue;
        }
    }
}
