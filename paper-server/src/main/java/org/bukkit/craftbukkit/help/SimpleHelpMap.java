package org.bukkit.craftbukkit.help;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.MultipleCommandAlias;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.command.VanillaCommandWrapper;
import org.bukkit.help.GenericCommandHelpTopic;
import org.bukkit.help.HelpMap;
import org.bukkit.help.HelpTopic;
import org.bukkit.help.HelpTopicComparator;
import org.bukkit.help.HelpTopicFactory;
import org.bukkit.help.IndexHelpTopic;

/**
 * Standard implementation of {@link HelpMap} for CraftBukkit servers.
 */
public class SimpleHelpMap implements HelpMap {

    private HelpTopic defaultTopic;
    private final Map<String, HelpTopic> helpTopics;
    private final Map<Class, HelpTopicFactory<Command>> topicFactoryMap;
    private final CraftServer server;
    private HelpYamlReader yaml;

    public SimpleHelpMap(CraftServer server) {
        this.helpTopics = new TreeMap<>(HelpTopicComparator.topicNameComparatorInstance()); // Using a TreeMap for its explicit sorting on key
        this.topicFactoryMap = new HashMap<>();
        this.server = server;
        this.yaml = new HelpYamlReader(server);

        Predicate<? super HelpTopic> indexFilter = Predicates.not(Predicates.instanceOf(CommandAliasHelpTopic.class));
        if (!this.yaml.commandTopicsInMasterIndex()) {
            indexFilter = Predicates.and(indexFilter, Predicates.not(new IsCommandTopicPredicate()));
        }

        this.defaultTopic = new IndexHelpTopic("Index", null, null, Collections2.filter(this.helpTopics.values(), indexFilter), "Use /help [n] to get page n of help.");

        this.registerHelpTopicFactory(MultipleCommandAlias.class, new MultipleCommandAliasHelpTopicFactory());
    }

    @Override
    public synchronized HelpTopic getHelpTopic(String topicName) {
        if (topicName.isEmpty()) {
            return this.defaultTopic;
        }

        if (this.helpTopics.containsKey(topicName)) {
            return this.helpTopics.get(topicName);
        }

        return null;
    }

    @Override
    public Collection<HelpTopic> getHelpTopics() {
        return this.helpTopics.values();
    }

    @Override
    public synchronized void addTopic(HelpTopic topic) {
        // Existing topics take priority
        if (!this.helpTopics.containsKey(topic.getName())) {
            this.helpTopics.put(topic.getName(), topic);
        }
    }

    @Override
    public synchronized void clear() {
        this.helpTopics.clear();
    }

    @Override
    public List<String> getIgnoredPlugins() {
        return this.yaml.getIgnoredPlugins();
    }

    /**
     * Reads the general topics from help.yml and adds them to the help index.
     */
    public synchronized void initializeGeneralTopics() {
        this.yaml = new HelpYamlReader(this.server);

        // Initialize general help topics from the help.yml file
        for (HelpTopic topic : this.yaml.getGeneralTopics()) {
            this.addTopic(topic);
        }

        // Initialize index help topics from the help.yml file
        for (HelpTopic topic : this.yaml.getIndexTopics()) {
            if (topic.getName().equals("Default")) {
                this.defaultTopic = topic;
            } else {
                this.addTopic(topic);
            }
        }
    }

    /**
     * Processes all the commands registered in the server and creates help topics for them.
     */
    public synchronized void initializeCommands() {
        // ** Load topics from highest to lowest priority order **
        Set<String> ignoredPlugins = new HashSet<String>(this.yaml.getIgnoredPlugins());

        // Don't load any automatic help topics if All is ignored
        if (ignoredPlugins.contains("All")) {
            return;
        }

        // Initialize help topics from the server's command map
        outer: for (Command command : this.server.getCommandMap().getCommands()) {
            if (this.commandInIgnoredPlugin(command, ignoredPlugins)) {
                continue;
            }

            // Register a topic
            for (Class c : this.topicFactoryMap.keySet()) {
                if (c.isAssignableFrom(command.getClass())) {
                    HelpTopic t = this.topicFactoryMap.get(c).createTopic(command);
                    if (t != null) this.addTopic(t);
                    continue outer;
                }
                if (command instanceof PluginCommand && c.isAssignableFrom(((PluginCommand) command).getExecutor().getClass())) {
                    HelpTopic t = this.topicFactoryMap.get(c).createTopic(command);
                    if (t != null) this.addTopic(t);
                    continue outer;
                }
            }
            this.addTopic(new GenericCommandHelpTopic(command));
        }

        // Initialize command alias help topics
        for (Command command : this.server.getCommandMap().getCommands()) {
            if (this.commandInIgnoredPlugin(command, ignoredPlugins)) {
                continue;
            }
            for (String alias : command.getAliases()) {
                // Only register if this command owns the alias
                if (this.server.getCommandMap().getCommand(alias) == command) {
                    this.addTopic(new CommandAliasHelpTopic("/" + alias, "/" + command.getLabel(), this));
                }
            }
        }

        // Add alias sub-index
        Collection<HelpTopic> filteredTopics = Collections2.filter(this.helpTopics.values(), Predicates.instanceOf(CommandAliasHelpTopic.class));
        if (!filteredTopics.isEmpty()) {
            this.addTopic(new IndexHelpTopic("Aliases", "Lists command aliases", null, filteredTopics));
        }

        // Initialize plugin-level sub-topics
        Map<String, Set<HelpTopic>> pluginIndexes = new HashMap<>();
        this.fillPluginIndexes(pluginIndexes, this.server.getCommandMap().getCommands());

        for (Map.Entry<String, Set<HelpTopic>> entry : pluginIndexes.entrySet()) {
            this.addTopic(new IndexHelpTopic(entry.getKey(), "All commands for " + entry.getKey(), null, entry.getValue(), "Below is a list of all " + entry.getKey() + " commands:"));
        }

        // Amend help topics from the help.yml file
        for (HelpTopicAmendment amendment : this.yaml.getTopicAmendments()) {
            if (this.helpTopics.containsKey(amendment.getTopicName())) {
                this.helpTopics.get(amendment.getTopicName()).amendTopic(amendment.getShortText(), amendment.getFullText());
                if (amendment.getPermission() != null) {
                    this.helpTopics.get(amendment.getTopicName()).amendCanSee(amendment.getPermission());
                }
            }
        }
    }

    private void fillPluginIndexes(Map<String, Set<HelpTopic>> pluginIndexes, Collection<? extends Command> commands) {
        for (Command command : commands) {
            String pluginName = this.getCommandPluginName(command);
            if (pluginName != null) {
                HelpTopic topic = this.getHelpTopic("/" + command.getLabel());
                if (topic != null) {
                    if (!pluginIndexes.containsKey(pluginName)) {
                        pluginIndexes.put(pluginName, new TreeSet<>(HelpTopicComparator.helpTopicComparatorInstance())); //keep things in topic order
                    }
                    pluginIndexes.get(pluginName).add(topic);
                }
            }
        }
    }

    private String getCommandPluginName(Command command) {
        if (command instanceof PluginIdentifiableCommand) {
            return ((PluginIdentifiableCommand) command).getPlugin().getName();
        }
        if (command instanceof VanillaCommandWrapper wrapper) {
            return wrapper.helpCommandNamespace;
        }
        if (command instanceof BukkitCommand) {
            return "Bukkit";
        }
        return null;
    }

    private boolean commandInIgnoredPlugin(Command command, Set<String> ignoredPlugins) {
        if ((command instanceof BukkitCommand) && ignoredPlugins.contains("Bukkit")) {
            return true;
        }
        if (command instanceof PluginIdentifiableCommand && ignoredPlugins.contains(((PluginIdentifiableCommand) command).getPlugin().getName())) {
            return true;
        }
        return false;
    }

    @Override
    public void registerHelpTopicFactory(Class commandClass, HelpTopicFactory factory) {
        Preconditions.checkArgument(Command.class.isAssignableFrom(commandClass) || CommandExecutor.class.isAssignableFrom(commandClass), "commandClass (%s) must implement either Command or CommandExecutor", commandClass.getName());
        this.topicFactoryMap.put(commandClass, factory);
    }

    private class IsCommandTopicPredicate implements Predicate<HelpTopic> {

        @Override
        public boolean apply(HelpTopic topic) {
            return topic.getName().charAt(0) == '/';
        }
    }
}
