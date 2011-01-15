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
 * craftbook:
 *     midi: true
 * sturmeh:
 *     cool: false
 *     lame: true
 *     likes:
 *         cuteasianboys: true
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
    private Yaml yaml = new Yaml(new SafeConstructor());
    private File file;
    
    public Configuration(File file) {
        super(new HashMap<String, Object>());
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
}
