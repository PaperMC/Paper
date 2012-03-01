package org.bukkit.craftbukkit.help;

import org.bukkit.Server;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.help.HelpTopic;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

/**
 * HelpYamlReader is responsible for processing the contents of the help.yml file.
 */
public class HelpYamlReader {

    private YamlConfiguration helpYaml;

    public HelpYamlReader(Server server) {
        File helpYamlFile = new File("help.yml");
        
        helpYaml = YamlConfiguration.loadConfiguration(helpYamlFile);
        helpYaml.options().copyDefaults(true);
        helpYaml.setDefaults(YamlConfiguration.loadConfiguration(getClass().getClassLoader().getResourceAsStream("configurations/help.yml")));
        try {
            if (!helpYamlFile.exists()) {
                helpYaml.save(helpYamlFile);
            }
        } catch (IOException ex) {
            server.getLogger().log(Level.SEVERE, "Could not save " + helpYamlFile, ex);
        }
    }

    /**
     * Extracts a list of all general help topics from help.yml
     * @return A list of general topics.
     */
    public List<HelpTopic> getGeneralTopics() {
        List<HelpTopic> topics = new LinkedList<HelpTopic>();
        ConfigurationSection generalTopics = helpYaml.getConfigurationSection("general-topics");
        if (generalTopics != null) {
            for (String topicName : generalTopics.getKeys(false)) {
                ConfigurationSection section = generalTopics.getConfigurationSection(topicName);
                String shortText = section.getString("shortText");
                String fullText = section.getString("fullText");
                String permission = section.getString("permission");
                topics.add(new CustomHelpTopic(topicName, shortText, fullText, permission));
            }
        }
        return topics;
    }

    /**
     * Extracts a list of topic amendments from help.yml
     * @return A list of amendments
     */
    public List<HelpTopicAmendment> getTopicAmendments() {
        List<HelpTopicAmendment> amendments = new LinkedList<HelpTopicAmendment>();
        ConfigurationSection commandTopics = helpYaml.getConfigurationSection("amended-topics");
        if (commandTopics != null) {
            for (String topicName : commandTopics.getKeys(false)) {
                ConfigurationSection section = commandTopics.getConfigurationSection(topicName);
                String description = section.getString("shortText");
                String usage = section.getString("fullText");
                amendments.add(new HelpTopicAmendment(topicName, description, usage));
            }
        }
        return amendments;
    }
}
