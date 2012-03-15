package org.bukkit.craftbukkit.help;

import org.bukkit.help.HelpTopic;

import java.util.Comparator;

/**
 * Used to impose a custom total ordering on help topics. All topics are listed in alphabetic order, but topics
 * that start with a slash come after topics that don't.
 */
public class HelpTopicComparator implements Comparator<HelpTopic> {
    private TopicNameComparator tnc = new TopicNameComparator();

    public int compare(HelpTopic lhs, HelpTopic rhs) {
        return tnc.compare(lhs.getName(), rhs.getName());
    }

    public static class TopicNameComparator implements Comparator<String> {
        public int compare(String lhs, String rhs) {
            if (lhs.startsWith("/") && !rhs.startsWith("/")) {
                return 1;
            } else if (!lhs.startsWith("/") && rhs.startsWith("/")) {
                return -1;
            } else {
                return lhs.compareToIgnoreCase(rhs);
            }
        }
    }
}
