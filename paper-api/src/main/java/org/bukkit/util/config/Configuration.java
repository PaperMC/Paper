package org.bukkit.util.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.reader.UnicodeReader;

/**
 * YAML configuration loader. This class is very far from finished, as it
 * only supports simple property lookup at the moment. Lists and nodes will be
 * supported in the future. For specifying node paths in the various get*()
 * methods, they support SK's path notation, allowing you to select child nodes
 * by using syntax in the form of root.parent.child. All node names are case
 * sensitive. YAML files are loaded safely and Java objects will not
 * be created if they are specified in the YAML file.
 * 
 * @author sk89q
 */
public class Configuration {
    private Yaml yaml = new Yaml(new SafeConstructor());
    private File file;
    private Map<String, Object> root;
    
    public Configuration(File file) {
        this.file = file;
    }
    
    /**
     * Loads the configuration file. All errors are thrown away.
     */
    public void load() {
        FileInputStream stream = null;
        
        try {
            stream = new FileInputStream(file);
            read(yaml.load(new UnicodeReader(stream)));
        } catch (IOException e) {
            root = new HashMap<String, Object>();
        } catch (ConfigurationException e) {
            root = new HashMap<String, Object>();
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException e) {
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    private void read(Object input) throws ConfigurationException {
        try {
            root = (Map<String, Object>)input;
        } catch (ClassCastException e) {
            throw new ConfigurationException("Root document must be an key-value structure");
        }
    }
    
    /**
     * Gets a property at a location. This will either return an Object
     * or null, with null meaning that no configuration value exists at
     * that location. This could potentially return a default value (not yet
     * implemented) as defined by a plugin, if this is a plugin-tied
     * configuration.
     * 
     * @param path SK's dot notation supported
     * @return object or null
     */
    @SuppressWarnings("unchecked")
    public Object getProperty(String path) {
        if (!path.contains(".")) {
            Object val = root.get(path);
            if (val == null) {
                return null;
            }
            return val;
        }
        
        String[] parts = path.split("\\.");
        Map<String, Object> node = root;
        
        for (int i = 0; i < parts.length; i++) {
            Object o = node.get(parts[i]);
            
            if (o == null) {
                return null;
            }
            
            if (i == parts.length - 1) {
                return o;
            }
            
            try {
                node = (Map<String, Object>)o;
            } catch (ClassCastException e) {
                return null;
            }
        }
        
        return null;
    }

    /**
     * Gets a string at a location. This will either return an String
     * or null, with null meaning that no configuration value exists at
     * that location. If the object at the particular location is not actually
     * a string, it will be converted to its string representation.
     * 
     * @param path SK's dot notation supported
     * @return string or null
     */
    public String getString(String path) {
        Object o = getProperty(path);
        if (o == null) {
            return null;
        }
        return o.toString();
    }

    /**
     * Gets a string at a location. This will either return an String
     * or the default value. If the object at the particular location is not
     * actually a string, it will be converted to its string representation.
     * 
     * @param path SK's dot notation supported
     * @param def default value
     * @return string or default
     */
    public String getString(String path, String def) {
        String o = getString(path);
        if (o == null) {
            return def;
        }
        return o;
    }

    /**
     * Gets an integer at a location. This will either return an integer
     * or the default value. If the object at the particular location is not
     * actually a integer, the default value will be returned. However, other
     * number types will be casted to an integer.
     * 
     * @param path SK's dot notation supported
     * @param def default value
     * @return int or default
     */
    public int getInt(String path, int def) {
        Object o = getProperty(path);
        if (o == null) {
            return def;
        } else if (o instanceof Byte) {
            return (Byte)o;
        } else if (o instanceof Integer) {
            return (Integer)o;
        } else if (o instanceof Double) {
            return (int)(double)(Double)o;
        } else if (o instanceof Float) {
            return (int)(float)(Float)o;
        } else if (o instanceof Long) {
            return (int)(long)(Long)o;
        } else {
            return def;
        }
    }

    /**
     * Gets a double at a location. This will either return an double
     * or the default value. If the object at the particular location is not
     * actually a double, the default value will be returned. However, other
     * number types will be casted to an double.
     * 
     * @param path SK's dot notation supported
     * @param def default value
     * @return double or default
     */
    public double getDouble(String path, double def) {
        Object o = getProperty(path);
        if (o == null) {
            return def;
        } else if (o instanceof Float) {
            return (Float)o;
        } else if (o instanceof Double) {
            return (Double)o;
        } else if (o instanceof Byte) {
            return (Byte)o;
        } else if (o instanceof Integer) {
            return (Integer)o;
        } else if (o instanceof Long) {
            return (Long)o;
        } else {
            return def;
        }
    }
}
