package io.papermc.paper.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import java.util.HashMap;
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
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class PaperHelpCommand {
    public static final String DESCRIPTION = "Shows the help menu";

    // this.usageMessage = "/help <pageNumber>\n/help <topic>\n/help <topic> <pageNumber>";

    public static LiteralCommandNode<CommandSourceStack> create() {
        final PaperHelpCommand command = new PaperHelpCommand();
        return Commands.literal("help")
            .requires(source -> source.getSender().hasPermission("bukkit.command.help"))
            .then(page(command))
            .then(topic(command))
            .executes(context -> {
                command.help(context, "", 1);
                return Command.SINGLE_SUCCESS;
            }).build();
    }

    private static RequiredArgumentBuilder<CommandSourceStack, String> topic(PaperHelpCommand command) {
        return Commands.argument("topic", StringArgumentType.string())
            .suggests((context, builder) -> {
                Bukkit.getServer().getHelpMap().getHelpTopics().stream()
                    .filter(topic -> topic.canSee(context.getSource().getSender()))
                    .map(HelpTopic::getName)
                    .map(name -> name.startsWith("/") ? name.substring(1) : name)
                    .map(StringArgumentType::escapeIfRequired)
                    .filter(name -> name.contains(builder.getRemaining()))
                    .forEach(builder::suggest);
                return builder.buildFuture();
            }).then(Commands.argument("page", IntegerArgumentType.integer())
                .executes(context -> {
                    final String topic = context.getArgument("topic", String.class);
                    final Integer page = context.getArgument("page", Integer.class);
                    command.help(context, topic, page);
                    return Command.SINGLE_SUCCESS;
                }))
            .executes(context -> {
                command.help(context, context.getArgument("topic", String.class), 1);
                return Command.SINGLE_SUCCESS;
            });
    }

    private static RequiredArgumentBuilder<CommandSourceStack, Integer> page(PaperHelpCommand command) {
        return Commands.argument("page", IntegerArgumentType.integer()).executes(context -> {
            command.help(context, "", context.getArgument("page", Integer.class));
            return Command.SINGLE_SUCCESS;
        });
    }

    private void help(CommandContext<CommandSourceStack> context, String query, int pageNumber) {
        CommandSender sender = context.getSource().getSender();

        pageNumber = Math.max(1, pageNumber);
        int pageHeight;
        int pageWidth;

        if (sender instanceof ConsoleCommandSender) {
            pageHeight = ChatPaginator.UNBOUNDED_PAGE_HEIGHT;
            pageWidth = ChatPaginator.UNBOUNDED_PAGE_WIDTH;
        } else {
            pageHeight = ChatPaginator.CLOSED_CHAT_PAGE_HEIGHT - 1;
            pageWidth = ChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH;
        }

        HelpMap helpMap = Bukkit.getServer().getHelpMap();
        HelpTopic topic = helpMap.getHelpTopic(query);

        if (topic == null) topic = helpMap.getHelpTopic("/" + query);
        if (topic == null) topic = findPossibleMatches(query);

        if (topic == null || !topic.canSee(sender)) {
            sender.sendMessage(Component.text("No help for " + query, NamedTextColor.RED));
            return;
        }

        ChatPaginator.ChatPage page = ChatPaginator.paginate(topic.getFullText(sender), pageNumber, pageWidth, pageHeight);

        TextComponent.Builder header = Component.text().append(Component.text("--------- ", NamedTextColor.YELLOW)).append(Component.text("Help: ", NamedTextColor.WHITE)).append(Component.text(topic.getName())).append(Component.space());
        if (page.getTotalPages() > 1) {
            header.append(Component.text("(")).append(Component.text(page.getPageNumber())).append(Component.text("/")).append(Component.text(page.getTotalPages())).append(Component.text(") "));
        }
        final TextComponent headerComponent = header.build();
        final int headerSize = PlainTextComponentSerializer.plainText().serialize(headerComponent).length();
        final int headerEndingCount = Math.max(0, ChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH - headerSize);

        sender.sendMessage(headerComponent.append(Component.text("-".repeat(headerEndingCount), NamedTextColor.YELLOW)));

        sender.sendMessage(page.getLines());
    }

    private static @Nullable HelpTopic findPossibleMatches(String searchString) {
        if (searchString.isEmpty()) return null;
        if (searchString.charAt(0) == '/') searchString = searchString.substring(1);

        int maxDistance = (searchString.length() / 5) + 3;
        Set<HelpTopic> possibleMatches = new TreeSet<>(HelpTopicComparator.helpTopicComparatorInstance());

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

        if (!possibleMatches.isEmpty()) {
            return new IndexHelpTopic("Search", null, null, possibleMatches, "Search for: " + searchString);
        } else {
            return null;
        }
    }

    /**
     * Computes the Damerau-Levenshtein Distance between two strings. Adapted
     * from the algorithm at <a href="http://en.wikipedia.org/wiki/Damerau%E2%80%93Levenshtein_distance">Wikipedia: Damerau–Levenshtein distance</a>
     *
     * @param s1 The first string being compared.
     * @param s2 The second string being compared.
     * @return The number of substitutions, deletions, insertions, and
     * transpositions required to get from s1 to s2.
     */
    private static int damerauLevenshteinDistance(@Nullable String s1, @Nullable String s2) {
        if (s1 == null && s2 == null) {
            return 0;
        } else if (s1 != null && s2 == null) {
            return s1.length();
        } else if (s1 == null) {
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

        Map<Character, Integer> sd = new HashMap<>();
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
