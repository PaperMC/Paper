package org.bukkit.craftbukkit.help;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.help.HelpTopic;

/**
 * HelpYamlReader is responsible for processing the contents of the help.yml file.
 */
public class HelpYamlReader {

    private YamlConfiguration helpYaml;
    private final char ALT_COLOR_CODE = '&';
    private final Server server;

    public HelpYamlReader(Server server) {
        this.server = server;

        File helpYamlFile = new File("help.yml");
        YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("configurations/help.yml"), StandardCharsets.UTF_8));

        try {
            this.helpYaml = YamlConfiguration.loadConfiguration(helpYamlFile);
            this.helpYaml.options().copyDefaults(true);
            this.helpYaml.setDefaults(defaultConfig);
            this.helpYaml.options().setHeader(defaultConfig.options().getHeader());

            try {
                this.helpYaml.save(helpYamlFile);
            } catch (IOException ex) {
                server.getLogger().log(Level.SEVERE, "Could not save " + helpYamlFile, ex);
            }
        } catch (Exception ex) {
            server.getLogger().severe("Failed to load help.yml. Verify the yaml indentation is correct. Reverting to default help.yml.");
            this.helpYaml = defaultConfig;
        }
    }

    /**
     * Extracts a list of all general help topics from help.yml
     *
     * @return A list of general topics.
     */
    public List<HelpTopic> getGeneralTopics() {
        List<HelpTopic> topics = new LinkedList<>();
        ConfigurationSection generalTopics = this.helpYaml.getConfigurationSection("general-topics");
        if (generalTopics != null) {
            for (String topicName : generalTopics.getKeys(false)) {
                ConfigurationSection section = generalTopics.getConfigurationSection(topicName);
                String shortText = ChatColor.translateAlternateColorCodes(this.ALT_COLOR_CODE, section.getString("shortText", ""));
                String fullText = ChatColor.translateAlternateColorCodes(this.ALT_COLOR_CODE, section.getString("fullText", ""));
                String permission = section.getString("permission", "");
                topics.add(new CustomHelpTopic(topicName, shortText, fullText, permission));
            }
        }
        return topics;
    }

    /**
     * Extracts a list of all index topics from help.yml
     *
     * @return A list of index topics.
     */
    public List<HelpTopic> getIndexTopics() {
        List<HelpTopic> topics = new LinkedList<>();
        ConfigurationSection indexTopics = this.helpYaml.getConfigurationSection("index-topics");
        if (indexTopics != null) {
            for (String topicName : indexTopics.getKeys(false)) {
                ConfigurationSection section = indexTopics.getConfigurationSection(topicName);
                String shortText = ChatColor.translateAlternateColorCodes(this.ALT_COLOR_CODE, section.getString("shortText", ""));
                String preamble = ChatColor.translateAlternateColorCodes(this.ALT_COLOR_CODE, section.getString("preamble", ""));
                String permission = section.getString("permission", "");
                List<String> commands = section.getStringList("commands");
                topics.add(new CustomIndexHelpTopic(this.server.getHelpMap(), topicName, shortText, permission, commands, preamble));
            }
        }
        return topics;
    }

    /**
     * Extracts a list of topic amendments from help.yml
     *
     * @return A list of amendments.
     */
    public List<HelpTopicAmendment> getTopicAmendments() {
        List<HelpTopicAmendment> amendments = new LinkedList<>();
        ConfigurationSection commandTopics = this.helpYaml.getConfigurationSection("amended-topics");
        if (commandTopics != null) {
            for (String topicName : commandTopics.getKeys(false)) {
                ConfigurationSection section = commandTopics.getConfigurationSection(topicName);
                String description = ChatColor.translateAlternateColorCodes(this.ALT_COLOR_CODE, section.getString("shortText", ""));
                String usage = ChatColor.translateAlternateColorCodes(this.ALT_COLOR_CODE, section.getString("fullText", ""));
                String permission = section.getString("permission", "");
                amendments.add(new HelpTopicAmendment(topicName, description, usage, permission));
            }
        }
        return amendments;
    }

    public List<String> getIgnoredPlugins() {
        return this.helpYaml.getStringList("ignore-plugins");
    }

    public boolean commandTopicsInMasterIndex() {
        return this.helpYaml.getBoolean("command-topics-in-master-index", true);
    }
}
