package org.bukkit.craftbukkit.help;

/**
 * A HelpTopicAmendment represents the contents of a topic amendment from the help.yml
 */
public class HelpTopicAmendment {
    private String topicName;
    private String shortText;
    private String fullText;

    public HelpTopicAmendment(String topicName, String shortText, String fullText) {
        this.fullText = fullText;
        this.shortText = shortText;
        this.topicName = topicName;
    }

    /**
     * Gets the amended full text
     * @return the full text
     */
    public String getFullText() {
        return fullText;
    }

    /**
     * Gets the amended short text
     * @return the short text
     */
    public String getShortText() {
        return shortText;
    }

    /**
     * Gets the name of the topic being amended
     * @return the topic name
     */
    public String getTopicName() {
        return topicName;
    }
}
