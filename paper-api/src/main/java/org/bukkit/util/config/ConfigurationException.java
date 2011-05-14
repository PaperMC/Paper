package org.bukkit.util.config;

/**
 * Configuration exception.
 *
 * @author sk89q
 */
public class ConfigurationException extends Exception {
    private static final long serialVersionUID = -2442886939908724203L;

    public ConfigurationException() {
        super();
    }

    public ConfigurationException(String msg) {
        super(msg);
    }
}
