package net.minecraft.server;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import java.util.regex.Pattern; // CraftBukkit

class ConsoleLogFormatter extends Formatter {

    private SimpleDateFormat b;

    final ConsoleLogManager a;
    // CraftBukkit start - Add color stripping
    private Pattern pattern = Pattern.compile("\\x1B\\[([0-9]{1,2}(;[0-9]{1,2})*)?[m|K]");
    private boolean strip = false;
    // CraftBukkit end

    private ConsoleLogFormatter(ConsoleLogManager consolelogmanager) {
        this.a = consolelogmanager;
        this.b = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.strip = MinecraftServer.getServer().options.has("log-strip-color"); // CraftBukkit
    }

    public String format(LogRecord logrecord) {
        StringBuilder stringbuilder = new StringBuilder();

        stringbuilder.append(this.b.format(Long.valueOf(logrecord.getMillis())));
        if (ConsoleLogManager.a(this.a) != null) {
            stringbuilder.append(ConsoleLogManager.a(this.a));
        }

        stringbuilder.append(" [").append(logrecord.getLevel().getName()).append("] ");
        stringbuilder.append(this.formatMessage(logrecord));
        stringbuilder.append('\n');
        Throwable throwable = logrecord.getThrown();

        if (throwable != null) {
            StringWriter stringwriter = new StringWriter();

            throwable.printStackTrace(new PrintWriter(stringwriter));
            stringbuilder.append(stringwriter.toString());
        }

        // CraftBukkit start - handle stripping color
        if (this.strip) {
            return this.pattern.matcher(stringbuilder.toString()).replaceAll("");
        } else {
            return stringbuilder.toString();
        }
        // CraftBukkit end
    }

    ConsoleLogFormatter(ConsoleLogManager consolelogmanager, EmptyClass3 emptyclass3) {
        this(consolelogmanager);
    }
}
