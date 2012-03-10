package org.bukkit.help;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * HelpTopic implementations are displayed to the user when the user uses the /help command.
 *
 * Custom implementations of this class can work at two levels. A simple implementation only
 * needs to set the value of {@code name}, {@code shortText}, and {@code fullText} int the
 * constructor. This base class will take care of the rest.
 *
 * Complex implementations can be created by overriding the behavior of all the methods in
 * this class.
 */
public abstract class HelpTopic {
    protected String name;
    protected String shortText;
    protected String fullText;
    protected String amendedPermission;
    
    /**
     * Determines if a {@link Player} is allowed to see this help topic. HelpTopic implementations should take
     * server administrator wishes into account as set by the {@link HelpTopic#amendCanSee(String)} function.
     *
     * @param player The Player in question.
     * @return True of the Player can see this help topic, false otherwise.
     */
    public abstract boolean canSee(CommandSender player);

    /**
     * Allows the server administrator to override the permission required to see a help topic. HelpTopic
     * implementations should take this into account when determining topic visibility on the
     * {@link HelpTopic#canSee(org.bukkit.command.CommandSender)} function.
     * @param amendedPermission The permission node the server administrator wishes to apply to this topic.
     */
    public void amendCanSee(String amendedPermission) {
        this.amendedPermission = amendedPermission;
    }

    /**
     * Returns the name of this help topic.
     * @return The topic name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns a brief description that will be displayed in the topic index.
     * @return A brief topic description.
     */
    public String getShortText() {
        return shortText;
    }

    /**
     * Returns the full description of this help topic that is displayed when the user requests this topic's details.
     * The result will be paginated to properly fit the user's client.
     *
     * @param forWho The player or console requesting the full text. Useful for further security trimming
     *               the command's full text based on sub-permissions in custom implementations.
     *
     * @return A full topic description.
     */
    public String getFullText(CommandSender forWho) {
        return fullText;
    }

    /**
     * Allows the server admin (or another plugin) to add or replace the contents of a help topic. A null in
     * either parameter will leave that part of the topic unchanged. In either amending parameter, the string
     * {@literal <text>} is replaced with the existing contents in the help topic. Use this to append or prepend
     * additional content into an automatically generated help topic.
     *
     * @param amendedShortText The new topic short text to use, or null to leave alone.
     * @param amendedFullText The new topic full text to use, or null to leave alone.
     */
    public void amendTopic(String amendedShortText, String amendedFullText) {
        shortText = applyAmendment(shortText, amendedShortText);
        fullText = applyAmendment(fullText, amendedFullText);
    }

    /**
     * Developers implementing their own custom HelpTopic implementations can use this utility method to ensure
     * their implementations comply with the expected behavior of the {@link HelpTopic#amendTopic(String, String)}
     * method.
     *
     * @param baseText The existing text of the help topic.
     * @param amendment The amending text from the amendTopic() method.
     *
     * @return The application of the amending text to the existing text, according to the expected rules of
     * amendTopic().
     */
    protected String applyAmendment(String baseText, String amendment) {
        if (amendment == null) {
            return baseText;
        } else {
            return amendment.replaceAll("<text>", baseText);
        }
    }
}
