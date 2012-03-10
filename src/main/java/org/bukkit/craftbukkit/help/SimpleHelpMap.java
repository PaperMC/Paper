package org.bukkit.craftbukkit.help;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.MultipleCommandAlias;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.defaults.VanillaCommand;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.help.*;

import java.util.*;

/**
 * Standard implementation of {@link HelpMap} for CraftBukkit servers.
 */
public class SimpleHelpMap implements HelpMap {
    
    private HelpTopic defaultTopic;
    private Map<String, HelpTopic> helpTopics;
    private Map<Class, HelpTopicFactory> topicFactoryMap;

    public SimpleHelpMap() {
        helpTopics = new TreeMap<String, HelpTopic>(new HelpTopicComparator()); // Using a TreeMap for its explicit sorting on key
        defaultTopic = new IndexHelpTopic(helpTopics.values());
        topicFactoryMap = new HashMap<Class, HelpTopicFactory>();

        registerHelpTopicFactory(MultipleCommandAlias.class, new MultipleCommandAliasHelpTopicFactory());
    }
    
    public synchronized HelpTopic getHelpTopic(String topicName) {
        if (topicName.equals("")) {
            return defaultTopic;
        }

        if (helpTopics.containsKey(topicName)) {
            return helpTopics.get(topicName);
        }

        return null;
    }

    public synchronized void addTopic(HelpTopic topic) {
        // Existing topics take priority
        if (!helpTopics.containsKey(topic.getName())) {
            helpTopics.put(topic.getName(), topic);
        }
    }

    public synchronized void clear() {
        helpTopics.clear();
    }

    /**
     * Reads the general topics from help.yml and adds them to the help index.
     * @param server A reference to the server.
     */
    public synchronized void initializeGeneralTopics(CraftServer server) {
        HelpYamlReader reader = new HelpYamlReader(server);

        // Initialize general help topics from the help.yml file
        for (HelpTopic topic : reader.getGeneralTopics()) {
            addTopic(topic);
        }
    }

    /**
     * Processes all the commands registered in the server and creates help topics for them.
     * @param server A reference to the server.
     */
    public synchronized void initializeCommands(CraftServer server) {
        // ** Load topics from highest to lowest priority order **

        // Initialize help topics from the server's command map
        outer: for (Command command : server.getCommandMap().getCommands()) {
            for (Class c : topicFactoryMap.keySet()) {
                if (c.isAssignableFrom(command.getClass())) {
                    addTopic(topicFactoryMap.get(c).createTopic(command));
                    continue outer;
                }
                if (command instanceof PluginCommand && c.isAssignableFrom(((PluginCommand)command).getExecutor().getClass())) {
                    addTopic(topicFactoryMap.get(c).createTopic(command));
                    continue outer;
                }
            }
            addTopic(new GenericCommandHelpTopic(command));
        }

        // Initialize help topics from the server's fallback commands
        for (VanillaCommand command : server.getCommandMap().getFallbackCommands()) {
            addTopic(new GenericCommandHelpTopic(command));
        }

        // Amend help topics from the help.yml file
        HelpYamlReader reader = new HelpYamlReader(server);
        for (HelpTopicAmendment amendment : reader.getTopicAmendments()) {
            if (helpTopics.containsKey(amendment.getTopicName())) {
                helpTopics.get(amendment.getTopicName()).amendTopic(amendment.getShortText(), amendment.getFullText());
            }
        }
    }

    public void registerHelpTopicFactory(Class commandClass, HelpTopicFactory factory) {
        if (!Command.class.isAssignableFrom(commandClass) && !CommandExecutor.class.isAssignableFrom(commandClass)) {
            throw new IllegalArgumentException("commandClass must implement either Command or CommandExecutor!");
        }
        topicFactoryMap.put(commandClass, factory);
    }
}
