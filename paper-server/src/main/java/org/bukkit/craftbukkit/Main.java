package org.bukkit.craftbukkit;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.util.PathConverter;

import static java.util.Arrays.asList;

public class Main {
    public static final java.time.Instant BOOT_TIME = java.time.Instant.now(); // Paper - track initial start time
    public static boolean useJline = true;
    public static boolean useConsole = true;

    // Paper start - Reset loggers after shutdown
    static {
        System.setProperty("java.util.logging.manager", "io.papermc.paper.log.CustomLogManager");
    }
    // Paper end - Reset loggers after shutdown

    public static void main(String[] args) {
        if (System.getProperty("jdk.nio.maxCachedBufferSize") == null) System.setProperty("jdk.nio.maxCachedBufferSize", "262144"); // Paper - cap per-thread NIO cache size; https://www.evanjones.ca/java-bytebuffer-leak.html
        OptionParser parser = new OptionParser() {
            {
                this.acceptsAll(asList("?", "help"), "Show the help");

                this.acceptsAll(asList("c", "config"), "Properties file to use")
                        .withRequiredArg()
                        .ofType(File.class)
                        .defaultsTo(new File("server.properties"))
                        .describedAs("Properties file");

                this.acceptsAll(asList("P", "plugins"), "Plugin directory to use")
                        .withRequiredArg()
                        .ofType(File.class)
                        .defaultsTo(new File("plugins"))
                        .describedAs("Plugin directory");

                this.acceptsAll(asList("h", "host", "server-ip"), "Host to listen on")
                        .withRequiredArg()
                        .ofType(String.class)
                        .describedAs("Hostname or IP");

                this.acceptsAll(asList("W", "world-dir", "universe", "world-container"), "World container")
                        .withRequiredArg()
                        .ofType(File.class)
                        .defaultsTo(new File("."))
                        .describedAs("Directory containing worlds");

                this.acceptsAll(asList("w", "world", "level-name"), "World name")
                        .withRequiredArg()
                        .ofType(String.class)
                        .describedAs("World name");

                this.acceptsAll(asList("p", "port", "server-port"), "Port to listen on")
                        .withRequiredArg()
                        .ofType(Integer.class)
                        .describedAs("Port");

                this.accepts("serverId", "Server ID")
                        .withRequiredArg();

                this.accepts("jfrProfile", "Enable JFR profiling");

                this.accepts("pidFile", "pid File")
                        .withRequiredArg()
                        .withValuesConvertedBy(new PathConverter());

                this.acceptsAll(asList("o", "online-mode"), "Whether to use online authentication")
                        .withRequiredArg()
                        .ofType(Boolean.class)
                        .describedAs("Authentication");

                this.acceptsAll(asList("s", "size", "max-players"), "Maximum amount of players")
                        .withRequiredArg()
                        .ofType(Integer.class)
                        .describedAs("Server size");

                this.acceptsAll(asList("d", "date-format"), "Format of the date to display in the console (for log entries)")
                        .withRequiredArg()
                        .ofType(SimpleDateFormat.class)
                        .describedAs("Log date format");

                this.accepts("log-pattern", "Specifies the log filename pattern")
                        .withRequiredArg()
                        .ofType(String.class)
                        .defaultsTo("server.log")
                        .describedAs("Log filename");

                this.accepts("log-limit", "Limits the maximum size of the log file (0 = unlimited)")
                        .withRequiredArg()
                        .ofType(Integer.class)
                        .defaultsTo(0)
                        .describedAs("Max log size");

                this.accepts("log-count", "Specified how many log files to cycle through")
                        .withRequiredArg()
                        .ofType(Integer.class)
                        .defaultsTo(1)
                        .describedAs("Log count");

                this.accepts("log-append", "Whether to append to the log file")
                        .withRequiredArg()
                        .ofType(Boolean.class)
                        .defaultsTo(true)
                        .describedAs("Log append");

                this.accepts("log-strip-color", "Strips color codes from log file");

                this.acceptsAll(asList("b", "bukkit-settings"), "File for bukkit settings")
                        .withRequiredArg()
                        .ofType(File.class)
                        .defaultsTo(new File("bukkit.yml"))
                        .describedAs("Yml file");

                this.acceptsAll(asList("C", "commands-settings"), "File for command settings")
                        .withRequiredArg()
                        .ofType(File.class)
                        .defaultsTo(new File("commands.yml"))
                        .describedAs("Yml file");

                this.accepts("forceUpgrade", "Whether to force a world upgrade");
                this.accepts("eraseCache", "Whether to force cache erase during world upgrade");
                this.accepts("recreateRegionFiles", "Whether to recreate region files during world upgrade");
                this.accepts("safeMode", "Loads level with vanilla datapack only"); // Paper
                this.accepts("nogui", "Disables the graphical console");

                this.accepts("nojline", "Disables jline and emulates the vanilla console");

                this.accepts("noconsole", "Disables the console");

                this.acceptsAll(asList("v", "version"), "Show the CraftBukkit Version");

                this.accepts("demo", "Demo mode");

                this.accepts("initSettings", "Only create configuration files and then exit"); // SPIGOT-5761: Add initSettings option

                this.acceptsAll(asList("S", "spigot-settings"), "File for spigot settings")
                        .withRequiredArg()
                        .ofType(File.class)
                        .defaultsTo(new File("spigot.yml"))
                        .describedAs("Yml file");

                this.acceptsAll(asList("paper-dir", "paper-settings-directory"), "Directory for Paper settings")
                    .withRequiredArg()
                    .ofType(File.class)
                    .defaultsTo(new File(io.papermc.paper.configuration.PaperConfigurations.CONFIG_DIR))
                    .describedAs("Config directory");
                this.acceptsAll(asList("paper", "paper-settings"), "File for Paper settings")
                        .withRequiredArg()
                        .ofType(File.class)
                        .defaultsTo(new File("paper.yml"))
                        .describedAs("Yml file");

                this.acceptsAll(asList("add-plugin", "add-extra-plugin-jar"), "Specify paths to extra plugin jars to be loaded in addition to those in the plugins folder. This argument can be specified multiple times, once for each extra plugin jar path.")
                        .withRequiredArg()
                        .ofType(File.class)
                        .defaultsTo(new File[] {})
                        .describedAs("Jar file");

                this.accepts("server-name", "Name of the server")
                        .withRequiredArg()
                        .ofType(String.class)
                        .defaultsTo("Unknown Server")
                        .describedAs("Name");
            }
        };

        OptionSet options = null;

        try {
            options = parser.parse(args);
        } catch (joptsimple.OptionException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, ex.getLocalizedMessage());
        }

        if ((options == null) || (options.has("?"))) {
            try {
                parser.printHelpOn(System.out);
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (options.has("v")) {
            System.out.println(CraftServer.class.getPackage().getImplementationVersion());
        } else {
            // Do you love Java using + and ! as string based identifiers? I sure do!
            String path = new File(".").getAbsolutePath();
            if (path.contains("!") || path.contains("+")) {
                System.err.println("Cannot run server in a directory with ! or + in the pathname. Please rename the affected folders and try again.");
                return;
            }

            // Paper start - Improve java version check
            boolean skip = Boolean.getBoolean("Paper.IgnoreJavaVersion");
            String javaVersionName = System.getProperty("java.version");
            // J2SE SDK/JRE Version String Naming Convention
            boolean isPreRelease = javaVersionName.contains("-");
            if (isPreRelease) {
                if (!skip) {
                    System.err.println("Unsupported Java detected (" + javaVersionName + "). You are running an unsupported, non official, version. Only general availability versions of Java are supported. Please update your Java version. See https://docs.papermc.io/paper/faq#unsupported-java-detected-what-do-i-do for more information.");
                    return;
                }

                System.err.println("Unsupported Java detected ("+ javaVersionName + "), but the check was skipped. Proceed with caution! ");
            }
            // Paper end - Improve java version check

            try {
                if (options.has("nojline")) {
                    System.setProperty(net.minecrell.terminalconsole.TerminalConsoleAppender.JLINE_OVERRIDE_PROPERTY, "false");
                    useJline = false;
                }

                if (options.has("noconsole")) {
                    Main.useConsole = false;
                    useJline = false; // Paper
                    System.setProperty(net.minecrell.terminalconsole.TerminalConsoleAppender.JLINE_OVERRIDE_PROPERTY, "false"); // Paper
                }

                if (Main.class.getPackage().getImplementationVendor() != null && System.getProperty("IReallyKnowWhatIAmDoingISwear") == null) {
                    Date buildDate = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z").parse(Main.class.getPackage().getImplementationVendor()); // Paper

                    Calendar deadline = Calendar.getInstance();
                    deadline.add(Calendar.DAY_OF_YEAR, -14);
                    if (buildDate.before(deadline.getTime())) {
                        // Paper start - This is some stupid bullshit
                        System.err.println("*** Warning, you've not updated in a while! ***");
                        System.err.println("*** Please download a new build from https://papermc.io/downloads/paper ***");
                        // Paper end
                    }
                }

                System.setProperty("library.jansi.version", "Paper"); // Paper - set meaningless jansi version to prevent git builds from crashing on Windows
                System.setProperty("jdk.console", "java.base"); // Paper - revert default console provider back to java.base so we can have our own jline

                io.papermc.paper.PaperBootstrap.boot(options);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }
}
