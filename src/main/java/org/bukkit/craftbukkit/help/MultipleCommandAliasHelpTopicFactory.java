package org.bukkit.craftbukkit.help;

import org.bukkit.command.MultipleCommandAlias;
import org.bukkit.help.HelpTopic;
import org.bukkit.help.HelpTopicFactory;

/**
 * This class creates {@link MultipleCommandAliasHelpTopic} help topics from {@link MultipleCommandAlias} commands.
 */
public class MultipleCommandAliasHelpTopicFactory implements HelpTopicFactory<MultipleCommandAlias> {

    @Override
    public HelpTopic createTopic(MultipleCommandAlias multipleCommandAlias) {
        return new MultipleCommandAliasHelpTopic(multipleCommandAlias);
    }
}
