package org.bukkit.util.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.reader.UnicodeReader;
import org.yaml.snakeyaml.representer.Representer;

/**
 * YAML configuration loader. To use this class, construct it with path to
 * a file and call its load() method. For specifying node paths in the
 * various get*() methods, they support SK's path notation, allowing you to
 * select child nodes by delimiting node names with periods.
 * 
 * <p>
 * For example, given the following configuration file:</p>
 * 
 * <pre>members:
 *     - Hollie
 *     - Jason
 *     - Bobo
 *     - Aya
 *     - Tetsu
 * worldguard:
 *     fire:
 *         spread: false
 *         blocks: [cloth, rock, glass]
 * sturmeh:
 *     cool: false
 *     eats:
 *         babies: true</pre>
 * 
 * <p>Calling code could access sturmeh's baby eating state by using
 * <code>getBoolean("sturmeh.eats.babies", false)</code>. For lists, there are
 * methods such as <code>getStringList</code> that will return a type safe list.
 * 
 * <p>This class is currently incomplete. It is not yet possible to get a node.
 * </p>
 * 
 * @author sk89q
 */
public class Configuration extends ConfigurationNode {
    private Yaml yaml;
    private File file;
    
    public Configuration(File file) {
        super(new HashMap<String, Object>());
        
        DumperOptions options = new DumperOptions();
        options.setIndent(4);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        yaml = new Yaml(new SafeConstructor(), new Representer(), options);
        
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
    
    /**
     * Saves the configuration to disk. All errors are clobbered.
     * 
     * @return true if it was successful        
     */
    public boolean save() {
        FileOutputStream stream = null;
        
        File parent = file.getParentFile();
        if (parent != null) {
            parent.mkdirs();
        }
        
        try {
            stream = new FileOutputStream(file);
            yaml.dump(root, new OutputStreamWriter(stream, "UTF-8"));
            return true;
        } catch (IOException e) {
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException e) {
            }
        }
        
        return false;
    }
    
    @SuppressWarnings("unchecked")
    private void read(Object input) throws ConfigurationException {
        try {
            if ( null == input ) {
                root = new HashMap<String, Object>();
            } else {
                root = (Map<String, Object>)input;
            }
        } catch (ClassCastException e) {
            throw new ConfigurationException("Root document must be an key-value structure");
        }
    }
    
    /**
     * This method returns an empty ConfigurationNode for using as a 
     * default in methods that select a node from a node list.
     * @return
     */
    public static ConfigurationNode getEmptyNode() {
        return new ConfigurationNode(new HashMap<String, Object>());
    }
}
