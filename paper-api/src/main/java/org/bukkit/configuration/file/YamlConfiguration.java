package org.bukkit.configuration.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.representer.Representer;

/**
 * An implementation of {@link Configuration} which saves all files in Yaml.
 */
public class YamlConfiguration extends FileConfiguration {
    protected static final String COMMENT_PREFIX = "# ";
    protected static final String BLANK_CONFIG = "{}\n";
    private final DumperOptions yamlOptions = new DumperOptions();
    private final Representer yamlRepresenter = new Representer();
    private final Yaml yaml = new Yaml(new SafeConstructor(), yamlRepresenter, yamlOptions);

    @Override
    public String saveToString() {
        Map<String, Object> output = new LinkedHashMap<String, Object>();
        
        yamlOptions.setIndent(options().indent());
        yamlOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        yamlRepresenter.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        
        serializeValues(output, getValues(false));
        
        String header = buildHeader();
        String dump = yaml.dump(output);
        
        if (dump.equals(BLANK_CONFIG)) {
            dump = "";
        }
        
        return header + dump;
    }

    @Override
    public void loadFromString(String contents) throws InvalidConfigurationException {
        if (contents == null) {
            throw new IllegalArgumentException("Contents cannot be null");
        }
        
        Map<String, Object> input;
        try {
            input = (Map<String, Object>)yaml.load(contents);
        } catch (Throwable ex) {
            throw new InvalidConfigurationException("Specified contents is not a valid Configuration", ex);
        }
        
        String header = parseHeader(contents);
        
        if (header.length() > 0) {
            options().header(header);
        }
        
        deserializeValues(input, this);
    }
    
    protected void deserializeValues(Map<String, Object> input, ConfigurationSection section) throws InvalidConfigurationException {
        if (input == null) {
            return;
        }
        
        for (Map.Entry<String, Object> entry : input.entrySet()) {
            Object value = entry.getValue();
            
            if (value instanceof Map) {
                Map<String, Object> subvalues;
                
                try {
                    subvalues = (Map<String, Object>) value;
                } catch (ClassCastException ex) {
                    throw new InvalidConfigurationException("Map found where type is not <String, Object>", ex);
                }
                
                if (subvalues.containsKey(ConfigurationSerialization.SERIALIZED_TYPE_KEY)) {
                    try {
                        ConfigurationSerializable serializable = ConfigurationSerialization.deserializeObject(subvalues);
                        section.set(entry.getKey(), serializable);
                    } catch (IllegalArgumentException ex) {
                        throw new InvalidConfigurationException("Could not deserialize object", ex);
                    }
                } else {
                    ConfigurationSection subsection = section.createSection(entry.getKey());
                    deserializeValues(subvalues, subsection);
                }
            } else {
                section.set(entry.getKey(), entry.getValue());
            }
        }
    }
    
    protected void serializeValues(Map<String, Object> output, Map<String, Object> input) {
        if (input == null) {
            return;
        }
        
        for (Map.Entry<String, Object> entry : input.entrySet()) {
            Object value = entry.getValue();
            
            if (value instanceof ConfigurationSection) {
                ConfigurationSection subsection = (ConfigurationSection)entry.getValue();
                Map<String, Object> subvalues = new LinkedHashMap<String, Object>();
                
                serializeValues(subvalues, subsection.getValues(false));
                value = subvalues;
            } else if (value instanceof ConfigurationSerializable) {
                ConfigurationSerializable serializable = (ConfigurationSerializable)value;
                Map<String, Object> subvalues = new LinkedHashMap<String, Object>();
                subvalues.put(ConfigurationSerialization.SERIALIZED_TYPE_KEY, ConfigurationSerialization.getAlias(serializable.getClass()));
                
                serializeValues(subvalues, serializable.serialize());
                value = subvalues;
            } else if ((!isPrimitiveWrapper(value)) && (!isNaturallyStorable(value))) {
                throw new IllegalStateException("Configuration contains non-serializable values, cannot process");
            }
            
            if (value != null) {
                output.put(entry.getKey(), value);
            }
        }
    }
    
    protected String parseHeader(String input) {
        String[] lines = input.split("\r?\n", -1);
        StringBuilder result = new StringBuilder();
        boolean readingHeader = true;
        
        for (int i = 0; (i < lines.length) && (readingHeader); i++) {
            String line = lines[i];
            
            if (line.startsWith(COMMENT_PREFIX)) {
                if (i > 0) {
                    result.append("\n");
                }
                
                if (line.length() > COMMENT_PREFIX.length()) {
                    result.append(line.substring(COMMENT_PREFIX.length()));
                }
            } else if (line.length() == 0) {
                result.append("\n");
            } else {
                readingHeader = false;
            }
        }
        
        return result.toString();
    }
    
    protected String buildHeader() {
        String header = options().header();
        
        if (options().copyHeader()) {
            Configuration def = getDefaults();
            
            if ((def != null) && (def instanceof FileConfiguration)) {
                FileConfiguration filedefaults = (FileConfiguration)def;
                String defaultsHeader = filedefaults.buildHeader();
                
                if ((defaultsHeader != null) && (defaultsHeader.length() > 0)) {
                    return defaultsHeader;
                }
            }
        }
        
        if (header == null) {
            return "";
        }
        
        StringBuilder builder = new StringBuilder();
        String[] lines = header.split("\r?\n", -1);
        boolean startedHeader = false;
        
        for (int i = lines.length - 1; i >= 0; i--) {
            builder.insert(0, "\n");
            
            if ((startedHeader) || (lines[i].length() != 0)) {
                builder.insert(0, lines[i]);
                builder.insert(0, COMMENT_PREFIX);
                startedHeader = true;
            }
        }
        
        return builder.toString();
    }

    @Override
    public YamlConfigurationOptions options() {
        if (options == null) {
            options = new YamlConfigurationOptions(this);
        }
        
        return (YamlConfigurationOptions)options;
    }
    
    /**
     * Creates a new {@link YamlConfiguration}, loading from the given file.
     * <p>
     * Any errors loading the Configuration will be logged and then ignored.
     * If the specified input is not a valid config, a blank config will be returned.
     * 
     * @param file Input file
     * @return Resulting configuration
     * @throws IllegalArgumentException Thrown is file is null
     */
    public static YamlConfiguration loadConfiguration(File file) {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null");
        }
        
        YamlConfiguration config = new YamlConfiguration();
        
        try {
            config.load(file);
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + file, ex);
        } catch (InvalidConfigurationException ex) {
            if (ex.getCause() instanceof YAMLException) {
                Bukkit.getLogger().severe("Config file " + file + " isn't valid! " + ex.getCause());
            } else if ((ex.getCause() == null) || (ex.getCause() instanceof ClassCastException)) {
                Bukkit.getLogger().severe("Config file " + file + " isn't valid!");
            } else {
                Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + file + ": " + ex.getCause().getClass(), ex);
            }
        }
        
        return config;
    }
    
    /**
     * Creates a new {@link YamlConfiguration}, loading from the given stream.
     * <p>
     * Any errors loading the Configuration will be logged and then ignored.
     * If the specified input is not a valid config, a blank config will be returned.
     * 
     * @param stream Input stream
     * @return Resulting configuration
     * @throws IllegalArgumentException Thrown is stream is null
     */
    public static YamlConfiguration loadConfiguration(InputStream stream) {
        if (stream == null) {
            throw new IllegalArgumentException("Stream cannot be null");
        }
        
        YamlConfiguration config = new YamlConfiguration();
        
        try {
            config.load(stream);
        } catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load configuration", ex);
        } catch (InvalidConfigurationException ex) {
            if (ex.getCause() instanceof YAMLException) {
                Bukkit.getLogger().severe("Config file isn't valid! " + ex.getCause());
            } else if ((ex.getCause() == null) || (ex.getCause() instanceof ClassCastException)) {
                Bukkit.getLogger().severe("Config file isn't valid!");
            } else {
                Bukkit.getLogger().log(Level.SEVERE, "Cannot load configuration: " + ex.getCause().getClass(), ex);
            }
        }
        
        return config;
    }
}
