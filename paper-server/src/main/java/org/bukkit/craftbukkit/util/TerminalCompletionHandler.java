package org.bukkit.craftbukkit.util;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import jline.console.ConsoleReader;
import jline.console.completer.CompletionHandler;

/**
 * SPIGOT-6705: Make sure we print the display line again on tab completion, so that the user does not get stuck on it
 * e.g. The user needs to press y / n to continue
 */
public class TerminalCompletionHandler implements CompletionHandler {

    private final TerminalConsoleWriterThread writerThread;
    private final CompletionHandler delegate;

    public TerminalCompletionHandler(TerminalConsoleWriterThread writerThread, CompletionHandler delegate) {
        this.writerThread = writerThread;
        this.delegate = delegate;
    }

    @Override
    public boolean complete(ConsoleReader reader, List<CharSequence> candidates, int position) throws IOException {
        // First check normal list, so that we do not unnecessarily create a new HashSet if the not distinct list is already lower
        if (candidates.size() <= reader.getAutoprintThreshold()) {
            return delegate.complete(reader, candidates, position);
        }

        Set<CharSequence> distinct = new HashSet<>(candidates);
        if (distinct.size() <= reader.getAutoprintThreshold()) {
            return delegate.complete(reader, candidates, position);
        }

        writerThread.setCompletion(distinct.size());

        // FIXME: Potential concurrency issue, when terminal writer prints the display message before the delegate does it
        // resulting in two display message being present, until a new message gets logged or the user presses y / n
        // But the probability of this happening are probably lower than the effort needed to fix this
        // And seeing the display message at all should be a higher priority than seeing it two times in rare cases.
        boolean result = delegate.complete(reader, candidates, position);

        writerThread.setCompletion(-1);
        // draw line to prevent concurrency issue,
        // where terminal write would print the display message between delegate#complete finished and the completion set back to -1
        // Resulting in the display message being present even after pressing y / n
        reader.drawLine();
        reader.flush();

        return result;
    }
}
