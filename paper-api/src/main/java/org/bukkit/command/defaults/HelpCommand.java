package org.bukkit.command.defaults;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.help.HelpMap;
import org.bukkit.help.HelpTopic;
import org.bukkit.help.HelpTopicComparator;
import org.bukkit.help.IndexHelpTopic;
import org.bukkit.util.ChatPaginator;

import java.util.*;

public class HelpCommand extends VanillaCommand {
    public HelpCommand() {
        super("help");
        this.description = "Shows the help menu";
        this.usageMessage = "/help <pageNumber>\n/help <topic>\n/help <topic> <pageNumber>";
        this.setPermission("bukkit.command.help");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) return true;

        String command;
        int pageNumber;
        int pageHeight;
        int pageWidth;

        if (args.length == 0) {
            command = "";
            pageNumber = 1;
        } else if (NumberUtils.isDigits(args[args.length - 1])) {
            command = StringUtils.join(ArrayUtils.subarray(args, 0, args.length - 1), " ");
            try {
                pageNumber = NumberUtils.createInteger(args[args.length - 1]);
            } catch (NumberFormatException exception) {
                pageNumber = 1;
            }
            if (pageNumber <= 0) {
                pageNumber = 1;
            }
        } else {
            command = StringUtils.join(args, " ");
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
            sender.sendMessage(ChatColor.RED + "No help for " + command);
            return true;
        }

        ChatPaginator.ChatPage page = ChatPaginator.paginate(topic.getFullText(sender), pageNumber, pageWidth, pageHeight);

        StringBuilder header = new StringBuilder();
        header.append(ChatColor.YELLOW);
        header.append("--------- ");
        header.append(ChatColor.WHITE);
        header.append("Help: ");
        header.append(topic.getName());
        header.append(" ");
        if (page.getTotalPages() > 1) {
            header.append("(");
            header.append(page.getPageNumber());
            header.append("/");
            header.append(page.getTotalPages());
            header.append(") ");
        }
        header.append(ChatColor.YELLOW);
        for (int i = header.length(); i < ChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH; i++) {
            header.append("-");
        }
        sender.sendMessage(header.toString());

        sender.sendMessage(page.getLines());

        return true;
    }

    @Override
    public boolean matches(String input) {
        return input.equalsIgnoreCase("help") || input.equalsIgnoreCase("?");
    }

    protected HelpTopic findPossibleMatches(String searchString) {
        int maxDistance = (searchString.length() / 5) + 3;
        Set<HelpTopic> possibleMatches = new TreeSet<HelpTopic>(HelpTopicComparator.helpTopicComparatorInstance());

        if (searchString.startsWith("/")) {
            searchString = searchString.substring(1);
        }

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
     * from the algorithm at <a href="http://en.wikipedia.org/wiki/Damerau–Levenshtein_distance">Wikipedia: Damerau–Levenshtein distance</a>
     *
     * @param s1 The first string being compared.
     * @param s2 The second string being compared.
     * @return The number of substitutions, deletions, insertions, and
     * transpositions required to get from s1 to s2.
     */
    protected static int damerauLevenshteinDistance(String s1, String s2) {
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
