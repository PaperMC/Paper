package org.bukkit.craftbukkit.help;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.help.HelpMap;
import org.bukkit.help.HelpTopic;
import org.bukkit.help.IndexHelpTopic;
import org.jetbrains.annotations.NotNull;

public class CustomIndexHelpTopic extends IndexHelpTopic {
    private List<String> futureTopics;
    private final HelpMap helpMap;

    public CustomIndexHelpTopic(HelpMap helpMap, String name, String shortText, String permission, List<String> futureTopics, String preamble) {
        super(name, shortText, permission, new HashSet<>(), preamble);
        this.helpMap = helpMap;
        this.futureTopics = futureTopics;
    }

    @Override
    public boolean canSee(@NotNull final CommandSender sender) {
        this.computeTopics();

        return super.canSee(sender);
    }

    @Override
    public String getFullText(CommandSender sender) {
        this.computeTopics();

        return super.getFullText(sender);
    }

    private void computeTopics() {
        if (this.futureTopics != null) {
            List<HelpTopic> topics = new LinkedList<>();
            for (String futureTopic : this.futureTopics) {
                HelpTopic topic = this.helpMap.getHelpTopic(futureTopic);
                if (topic != null) {
                    topics.add(topic);
                }
            }
            this.setTopicsCollection(topics);
            this.futureTopics = null;
        }
    }
}
