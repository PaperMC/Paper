package org.bukkit.craftbukkit.util;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import jline.ConsoleReader;

public class TerminalConsoleHandler extends ConsoleHandler {
    private final ConsoleReader reader;

    public TerminalConsoleHandler(ConsoleReader reader) {
        super();
        this.reader = reader;
    }

    @Override
    public synchronized void flush() {
        super.flush();
        try {
            reader.redrawLine();
        } catch (IOException ex) {
            Logger.getLogger(TerminalConsoleHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
