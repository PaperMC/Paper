package org.bukkit.craftbukkit.help;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.help.HelpMap;
import org.bukkit.help.HelpTopic;
import org.bukkit.help.IndexHelpTopic;

/**
 */
public class CustomIndexHelpTopic extends IndexHelpTopic {
    private List<String> futureTopics;
    private final HelpMap helpMap;

    public CustomIndexHelpTopic(HelpMap helpMap, String name, String shortText, String permission, List<String> futureTopics, String preamble) {
        super(name, shortText, permission, new HashSet<HelpTopic>(), preamble);
        this.helpMap = helpMap;
        this.futureTopics = futureTopics;
    }

    @Override
    public String getFullText(CommandSender sender) {
        if (futureTopics != null) {
            List<HelpTopic> topics = new LinkedList<HelpTopic>();
            for (String futureTopic : futureTopics) {
                HelpTopic topic = helpMap.getHelpTopic(futureTopic);
                if (topic != null) {
                    topics.add(topic);
                }
            }
            setTopicsCollection(topics);
            futureTopics = null;
        }

        return super.getFullText(sender);
    }
}
