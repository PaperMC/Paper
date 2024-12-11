package io.papermc.paper.console;

import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.context.ParsedCommandNode;
import com.mojang.brigadier.context.StringRange;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.dedicated.DedicatedServer;
import org.jline.reader.ParsedLine;
import org.jline.reader.Parser;
import org.jline.reader.SyntaxError;

public class BrigadierConsoleParser implements Parser {

    private final DedicatedServer server;

    public BrigadierConsoleParser(DedicatedServer server) {
        this.server = server;
    }

    @Override
    public ParsedLine parse(final String line, final int cursor, final ParseContext context) throws SyntaxError {
        final ParseResults<CommandSourceStack> results = this.server.getCommands().getDispatcher().parse(new StringReader(line), this.server.createCommandSourceStack());
        final ImmutableStringReader reader = results.getReader();
        final List<String> words = new ArrayList<>();
        CommandContextBuilder<CommandSourceStack> currentContext = results.getContext();
        int currentWordIdx = -1;
        int wordIdx = -1;
        int inWordCursor = -1;
        if (currentContext.getRange().getLength() > 0) {
            do {
                for (final ParsedCommandNode<CommandSourceStack> node : currentContext.getNodes()) {
                    final StringRange nodeRange = node.getRange();
                    String current = nodeRange.get(reader);
                    words.add(current);
                    currentWordIdx++;
                    if (wordIdx == -1 && nodeRange.getStart() <= cursor && nodeRange.getEnd() >= cursor) {
                        // if cursor is in the middle of a parsed word/node
                        wordIdx = currentWordIdx;
                        inWordCursor = cursor - nodeRange.getStart();
                    }
                }
                currentContext = currentContext.getChild();
            } while (currentContext != null);
        }
        final String leftovers = reader.getRemaining();
        if (!leftovers.isEmpty() && leftovers.isBlank()) {
            // if brig didn't consume the whole line, and everything else is blank, add a new empty word
            currentWordIdx++;
            words.add("");
            if (wordIdx == -1) {
                wordIdx = currentWordIdx;
                inWordCursor = 0;
            }
        } else if (!leftovers.isEmpty()) {
            // if there are unparsed leftovers, add a new word with the remaining input
            currentWordIdx++;
            words.add(leftovers);
            if (wordIdx == -1) {
                wordIdx = currentWordIdx;
                inWordCursor = cursor - reader.getCursor();
            }
        }
        if (wordIdx == -1) {
            currentWordIdx++;
            words.add("");
            wordIdx = currentWordIdx;
            inWordCursor = 0;
        }
        return new BrigadierParsedLine(words.get(wordIdx), inWordCursor, wordIdx, words, line, cursor);
    }

    record BrigadierParsedLine(String word, int wordCursor, int wordIndex, List<String> words, String line, int cursor) implements ParsedLine {
    }
}
