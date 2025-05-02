package org.bukkit.command.defaults;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.help.HelpMap;
import org.bukkit.help.HelpTopic;
import org.bukkit.help.HelpTopicComparator;
import org.bukkit.help.IndexHelpTopic;
import org.bukkit.util.ChatPaginator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HelpCommand extends BukkitCommand {
    public HelpCommand() {
        super("help");
        this.description = "Shows the help menu";
        this.usageMessage = "/help <pageNumber>\n/help <topic>\n/help <topic> <pageNumber>";
        this.setAliases(Arrays.asList(new String[]{"?"}));
        this.setPermission("bukkit.command.help");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String currentAlias, @NotNull String[] args) {
        if (!testPermission(sender)) return true;

        String command;
        int pageNumber;
        int pageHeight;
        int pageWidth;

        if (args.length == 0) {
            command = "";
            pageNumber = 1;
        } else if (args[args.length - 1].chars().allMatch(Character::isDigit)) {
            command = Joiner.on(" ").join(Arrays.copyOfRange(args, 0, args.length - 1));
            try {
                pageNumber = Integer.decode(args[args.length - 1]);
            } catch (NumberFormatException exception) {
                pageNumber = 1;
            }
            if (pageNumber <= 0) {
                pageNumber = 1;
            }
        } else {
            command = Joiner.on(" ").join(args);
            pageNumber = 1;
        }

        if (sender instanceof ConsoleCommandSender) {
            pageHeight = ChatPaginator.UNBOUNDED_PAGE_HEIGHT;
            pageWidth = ChatPaginator.UNBOUNDED_PAGE_WIDTH;
        } else {
            pageHeight = ChatPaginator.CLOSED_CHAT_PAGE_HEIGHT - 1;
            pageWidth = ChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH;
        }

        HelpMap helpMap = Bukkit.getServer().getHelpMap();
        HelpTopic topic = helpMap.getHelpTopic(command);

        if (topic == null) {
            topic = helpMap.getHelpTopic("/" + command);
        }

        if (topic == null) {
            topic = findPossibleMatches(command);
        }

        if (topic == null || !topic.canSee(sender)) {
            sender.sendMessage(Component.text("No help for " + command, NamedTextColor.RED));
            return true;
        }

        ChatPaginator.ChatPage page = ChatPaginator.paginate(topic.getFullText(sender), pageNumber, pageWidth, pageHeight);

        TextComponent.Builder header = Component.text()
            .append(Component.text("--------- ", NamedTextColor.YELLOW))
            .append(Component.text("Help: ", NamedTextColor.WHITE))
            .append(Component.text(topic.getName()))
            .append(Component.space());
        if (page.getTotalPages() > 1) {
            header.append(Component.text("("))
                .append(Component.text(page.getPageNumber()))
                .append(Component.text("/"))
                .append(Component.text(page.getTotalPages()))
                .append(Component.text(") "));
        }
        final TextComponent headerComponent = header.build();
        final int headerSize = PlainTextComponentSerializer.plainText().serialize(headerComponent).length();
        final int headerEndingCount = Math.max(0, ChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH - headerSize);

        sender.sendMessage(headerComponent.append(Component.text("-".repeat(headerEndingCount), NamedTextColor.YELLOW)));

        sender.sendMessage(page.getLines());

        return true;
    }

    @NotNull
    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        Preconditions.checkArgument(sender != null, "Sender cannot be null");
        Preconditions.checkArgument(args != null, "Arguments cannot be null");
        Preconditions.checkArgument(alias != null, "Alias cannot be null");

        if (args.length == 1) {
            List<String> matchedTopics = new ArrayList<String>();
            String searchString = args[0];
            for (HelpTopic topic : Bukkit.getServer().getHelpMap().getHelpTopics()) {
                if (!topic.canSee(sender)) {
                    continue;
                }
                String trimmedTopic = topic.getName().startsWith("/") ? topic.getName().substring(1) : topic.getName();

                if (trimmedTopic.startsWith(searchString)) {
                    matchedTopics.add(trimmedTopic);
                }
            }
            return matchedTopics;
        }
        return ImmutableList.of();
    }

    @Nullable
    protected HelpTopic findPossibleMatches(@NotNull String searchString) {
        int maxDistance = (searchString.length() / 5) + 3;
        Set<HelpTopic> possibleMatches = new TreeSet<HelpTopic>(HelpTopicComparator.helpTopicComparatorInstance());

        if (searchString.startsWith("/")) {
            searchString = searchString.substring(1);
        }

        if (searchString.isEmpty()) return null; // Paper - prevent index out of bounds - nothing matches an empty search string, should have been special cased to defaultTopic earlier, just return null.
        for (HelpTopic topic : Bukkit.getServer().getHelpMap().getHelpTopics()) {
            String trimmedTopic = topic.getName().startsWith("/") ? topic.getName().substring(1) : topic.getName();

            if (trimmedTopic.length() < searchString.length()) {
                continue;
            }

            if (Character.toLowerCase(trimmedTopic.charAt(0)) != Character.toLowerCase(searchString.charAt(0))) {
                continue;
            }

            if (damerauLevenshteinDistance(searchString, trimmedTopic.substring(0, searchString.length())) < maxDistance) {
                possibleMatches.add(topic);
            }
        }

        if (possibleMatches.size() > 0) {
            return new IndexHelpTopic("Search", null, null, possibleMatches, "Search for: " + searchString);
        } else {
            return null;
        }
    }

    /**
     * Computes the Dameraur-Levenshtein Distance between two strings. Adapted
     * from the algorithm at <a href="http://en.wikipedia.org/wiki/Damerau%E2%80%93Levenshtein_distance">Wikipedia: Damerau–Levenshtein distance</a>
     *
     * @param s1 The first string being compared.
     * @param s2 The second string being compared.
     * @return The number of substitutions, deletions, insertions, and
     * transpositions required to get from s1 to s2.
     */
    protected static int damerauLevenshteinDistance(@Nullable String s1, @Nullable String s2) {
        if (s1 == null && s2 == null) {
            return 0;
        }
        if (s1 != null && s2 == null) {
            return s1.length();
        }
        if (s1 == null && s2 != null) {
            return s2.length();
        }

        int s1Len = s1.length();
        int s2Len = s2.length();
        int[][] H = new int[s1Len + 2][s2Len + 2];

        int INF = s1Len + s2Len;
        H[0][0] = INF;
        for (int i = 0; i <= s1Len; i++) {
            H[i + 1][1] = i;
            H[i + 1][0] = INF;
        }
        for (int j = 0; j <= s2Len; j++) {
            H[1][j + 1] = j;
            H[0][j + 1] = INF;
        }

        Map<Character, Integer> sd = new HashMap<Character, Integer>();
        for (char Letter : (s1 + s2).toCharArray()) {
            if (!sd.containsKey(Letter)) {
                sd.put(Letter, 0);
            }
        }

        for (int i = 1; i <= s1Len; i++) {
            int DB = 0;
            for (int j = 1; j <= s2Len; j++) {
                int i1 = sd.get(s2.charAt(j - 1));
                int j1 = DB;

                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    H[i + 1][j + 1] = H[i][j];
                    DB = j;
                } else {
                    H[i + 1][j + 1] = Math.min(H[i][j], Math.min(H[i + 1][j], H[i][j + 1])) + 1;
                }

                H[i + 1][j + 1] = Math.min(H[i + 1][j + 1], H[i1][j1] + (i - i1 - 1) + 1 + (j - j1 - 1));
            }
            sd.put(s1.charAt(i - 1), i);
        }

        return H[s1Len + 1][s2Len + 1];
    }
}
