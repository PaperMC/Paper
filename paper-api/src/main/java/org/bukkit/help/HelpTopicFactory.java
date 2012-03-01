package org.bukkit.help;

import org.bukkit.command.Command;

/**
 * A HelpTopicFactory is used to create custom {@link HelpTopic} objects from commands that inherit from a
 * common base class. You can use a custom HelpTopic to change the way all the commands in your plugin display
 * in the help. If your plugin implements a complex permissions system, a custom help topic may also be appropriate.
 *
 * To automatically bind your plugin's commands to your custom HelpTopic implementation, first make sure all your
 * commands derive from a custom base class (it doesn't have to do anything). Next implement a custom HelpTopicFactory
 * for that accepts your custom command base class and instantiates an instance of your custom HelpTopic from it.
 * Finally, register your HelpTopicFactory against your command base class using the {@link HelpMap#registerHelpTopicFactory(Class, HelpTopicFactory)}
 * method.
 *
 * @param <TCommand> The base class for your custom commands.
 */
public interface HelpTopicFactory<TCommand extends Command> {
    /**
     * This method accepts a command deriving from a custom command base class and constructs a custom HelpTopic
     * for it.
     *
     * @param command The custom command to build a help topic for.
     * @return A new custom help topic.
     */
    public HelpTopic createTopic(TCommand command);
}
