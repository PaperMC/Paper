
package org.bukkit.plugin;

import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
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
    private ArrayList<String> depend = null;
    private String version = null;
    private Object commands = null;
    private String description = null;
    private ArrayList<String> authors = new ArrayList<String>();
    private String website = null;
    private boolean database = false;

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
     * Returns the name of a plugin including the version
     *
     * @return String name
     */
    public String getFullName() {
        return name + " v" + version;
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

    public Object getDepend() {
        return depend;
    }

    /**
     * Gets the description of this plugin
     *
     * return Description of this plugin
     */
    public String getDescription() {
        return description;
    }

    public ArrayList<String> getAuthors() {
        return authors;
    }

    public String getWebsite() {
        return website;
    }

    public boolean isDatabaseEnabled() {
        return database;
    }

    public void setDatabaseEnabled(boolean database) {
        this.database = database;
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
            if (main.startsWith("org.bukkit.")) {
                throw new InvalidDescriptionException("main may not be within the org.bukkit namespace");
            }
        } catch (NullPointerException ex) {
            throw new InvalidDescriptionException(ex, "main is not defined");
        } catch (ClassCastException ex) {
            throw new InvalidDescriptionException(ex, "main is of wrong type");
        }

        if (map.containsKey("commands")) {
            try {
                commands = map.get("commands");
            } catch (ClassCastException ex) {
                throw new InvalidDescriptionException(ex, "commands are of wrong type");
            }
        }

        if (map.containsKey("depend")) {
            try {
                depend = (ArrayList<String>)map.get("depend");
            } catch (ClassCastException ex) {
                throw new InvalidDescriptionException(ex, "depend is of wrong type");
            }
        }

        if (map.containsKey("database")) {
            try {
                database = (Boolean)map.get("database");
            } catch (ClassCastException ex) {
                throw new InvalidDescriptionException(ex, "database is of wrong type");
            }
        }

        if (map.containsKey("website")) {
            try {
                website = (String)map.get("website");
            } catch (ClassCastException ex) {
                throw new InvalidDescriptionException(ex, "website is of wrong type");
            }
        }

        if (map.containsKey("description")) {
            try {
                description = (String)map.get("description");
            } catch (ClassCastException ex) {
                throw new InvalidDescriptionException(ex, "description is of wrong type");
            }
        }

        if (map.containsKey("author")) {
            try {
                String extra = (String)map.get("author");
                authors.add(extra);
            } catch (ClassCastException ex) {
                throw new InvalidDescriptionException(ex, "author is of wrong type");
            }
        }

        if (map.containsKey("authors")) {
            try {
                ArrayList<String> extra = (ArrayList<String>)map.get("authors");
                authors.addAll(extra);
            } catch (ClassCastException ex) {
                throw new InvalidDescriptionException(ex, "authors are of wrong type");
            }
        }
    }

    private Map<String, Object> saveMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", name);
        map.put("main", main);
        map.put("version", version);
        map.put("database", database);

        if (commands != null) map.put("command", commands);
        if (depend != null) map.put("depend", depend);
        if (website != null) map.put("website", website);
        if (description != null) map.put("description", description);

        if (authors.size() == 1) {
            map.put("author", authors.get(0));
        } else if (authors.size() > 1) {
            map.put("authors", authors);
        }

        return map;
    }
}
