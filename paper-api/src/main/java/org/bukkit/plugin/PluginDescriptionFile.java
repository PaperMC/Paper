
package org.bukkit.plugin;

import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

/**
 * Provides access to a Plugins description file, plugin.yaml
 */
public final class PluginDescriptionFile {
    private static final Yaml yaml = new Yaml(new SafeConstructor());
    private String name = null;
    private String main = null;
    private String version = null;
    private Object commands = null;

    @SuppressWarnings("unchecked")
    public PluginDescriptionFile(final InputStream stream) throws InvalidDescriptionException {
        loadMap((Map<String, Object>)yaml.load(stream));
    }

    /**
     * Loads a PluginDescriptionFile from the specified reader
     * @param reader
     */
    @SuppressWarnings("unchecked")
    public PluginDescriptionFile(final Reader reader) throws InvalidDescriptionException {
        loadMap((Map<String, Object>)yaml.load(reader));
    }

    /**
     * Creates a new PluginDescriptionFile with the given detailed
     *
     * @param pluginName Name of this plugin
     * @param mainClass Full location of the main class of this plugin
     */
    public PluginDescriptionFile(final String pluginName, final String pluginVersion, final String mainClass) {
        name = pluginName;
        version = pluginVersion;
        main = mainClass;
    }

    /**
     * Saves this PluginDescriptionFile to the given writer
     *
     * @param writer Writer to output this file to
     */
    public void save(Writer writer) {
        yaml.dump(saveMap(), writer);
    }

    /**
     * Returns the name of a plugin
     *
     * @return String name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the version of a plugin
     *
     * @return String name
     */
    public String getVersion() {
        return version;
    }

    /**
     * Returns the main class for a plugin
     *
     * @return Java classpath
     */
    public String getMain() {
        return main;
    }
    
    public Object getCommands() {
        return commands;
    }

    private void loadMap(Map<String, Object> map) throws InvalidDescriptionException {
        try {
            name = map.get("name").toString();
        } catch (NullPointerException ex) {
            throw new InvalidDescriptionException(ex, "name is not defined");
        } catch (ClassCastException ex) {
            throw new InvalidDescriptionException(ex, "name is of wrong type");
        }

        try {
            version = map.get("version").toString();
        } catch (NullPointerException ex) {
            throw new InvalidDescriptionException(ex, "version is not defined");
        } catch (ClassCastException ex) {
            throw new InvalidDescriptionException(ex, "version is of wrong type");
        }

        try {
            main = map.get("main").toString();
        } catch (NullPointerException ex) {
            throw new InvalidDescriptionException(ex, "main is not defined");
        } catch (ClassCastException ex) {
            throw new InvalidDescriptionException(ex, "main is of wrong type");
        }
        
        try {
            commands = map.get("commands");
        } catch (NullPointerException ex) {
            throw new InvalidDescriptionException(ex, "command is not defined");
        } catch (ClassCastException ex) {
            throw new InvalidDescriptionException(ex, "command is of wrong type");
        }
    }

    private Map<String, Object> saveMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", name);
        map.put("main", main);
        map.put("version", version);
        map.put("command", commands);
        return map;
    }
}
