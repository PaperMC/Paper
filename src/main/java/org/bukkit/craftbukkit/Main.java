package org.bukkit.craftbukkit;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import net.minecrell.terminalconsole.TerminalConsoleAppender; // Paper

public class Main {
    public static boolean useJline = true;
    public static boolean useConsole = true;

    public static void main(String[] args) {
        // Todo: Installation script
        OptionParser parser = new OptionParser() {
            {
                acceptsAll(asList("?", "help"), "Show the help");

                acceptsAll(asList("c", "config"), "Properties file to use")
                        .withRequiredArg()
                        .ofType(File.class)
                        .defaultsTo(new File("server.properties"))
                        .describedAs("Properties file");

                acceptsAll(asList("P", "plugins"), "Plugin directory to use")
                        .withRequiredArg()
                        .ofType(File.class)
                        .defaultsTo(new File("plugins"))
                        .describedAs("Plugin directory");

                acceptsAll(asList("h", "host", "server-ip"), "Host to listen on")
                        .withRequiredArg()
                        .ofType(String.class)
                        .describedAs("Hostname or IP");

                acceptsAll(asList("W", "world-dir", "universe", "world-container"), "World container")
                        .withRequiredArg()
                        .ofType(File.class)
                        .defaultsTo(new File("."))
                        .describedAs("Directory containing worlds");

                acceptsAll(asList("w", "world", "level-name"), "World name")
                        .withRequiredArg()
                        .ofType(String.class)
                        .describedAs("World name");

                acceptsAll(asList("p", "port", "server-port"), "Port to listen on")
                        .withRequiredArg()
                        .ofType(Integer.class)
                        .describedAs("Port");

                acceptsAll(asList("o", "online-mode"), "Whether to use online authentication")
                        .withRequiredArg()
                        .ofType(Boolean.class)
                        .describedAs("Authentication");

                acceptsAll(asList("s", "size", "max-players"), "Maximum amount of players")
                        .withRequiredArg()
                        .ofType(Integer.class)
                        .describedAs("Server size");

                acceptsAll(asList("d", "date-format"), "Format of the date to display in the console (for log entries)")
                        .withRequiredArg()
                        .ofType(SimpleDateFormat.class)
                        .describedAs("Log date format");

                acceptsAll(asList("log-pattern"), "Specfies the log filename pattern")
                        .withRequiredArg()
                        .ofType(String.class)
                        .defaultsTo("server.log")
                        .describedAs("Log filename");

                acceptsAll(asList("log-limit"), "Limits the maximum size of the log file (0 = unlimited)")
                        .withRequiredArg()
                        .ofType(Integer.class)
                        .defaultsTo(0)
                        .describedAs("Max log size");

                acceptsAll(asList("log-count"), "Specified how many log files to cycle through")
                        .withRequiredArg()
                        .ofType(Integer.class)
                        .defaultsTo(1)
                        .describedAs("Log count");

                acceptsAll(asList("log-append"), "Whether to append to the log file")
                        .withRequiredArg()
                        .ofType(Boolean.class)
                        .defaultsTo(true)
                        .describedAs("Log append");

                acceptsAll(asList("log-strip-color"), "Strips color codes from log file");

                acceptsAll(asList("b", "bukkit-settings"), "File for bukkit settings")
                        .withRequiredArg()
                        .ofType(File.class)
                        .defaultsTo(new File("bukkit.yml"))
                        .describedAs("Yml file");

                acceptsAll(asList("C", "commands-settings"), "File for command settings")
                        .withRequiredArg()
                        .ofType(File.class)
                        .defaultsTo(new File("commands.yml"))
                        .describedAs("Yml file");

                acceptsAll(asList("forceUpgrade"), "Whether to force a world upgrade");
                acceptsAll(asList("eraseCache"), "Whether to force cache erase during world upgrade");
                acceptsAll(asList("nogui"), "Disables the graphical console");

                acceptsAll(asList("nojline"), "Disables jline and emulates the vanilla console");

                acceptsAll(asList("noconsole"), "Disables the console");

                acceptsAll(asList("v", "version"), "Show the CraftBukkit Version");

                acceptsAll(asList("demo"), "Demo mode");

                // Spigot Start
                acceptsAll(asList("S", "spigot-settings"), "File for spigot settings")
                        .withRequiredArg()
                        .ofType(File.class)
                        .defaultsTo(new File("spigot.yml"))
                        .describedAs("Yml file");
                // Spigot End

                // Paper Start
                acceptsAll(asList("paper", "paper-settings"), "File for paper settings")
                        .withRequiredArg()
                        .ofType(File.class)
                        .defaultsTo(new File("paper.yml"))
                        .describedAs("Yml file");
                // Paper end

                // Paper start
                acceptsAll(asList("server-name"), "Name of the server")
                        .withRequiredArg()
                        .ofType(String.class)
                        .defaultsTo("Unknown Server")
                        .describedAs("Name");
                // Paper end
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

            float javaVersion = Float.parseFloat(System.getProperty("java.class.version"));
            if (javaVersion > 59.0) {
                System.err.println("Unsupported Java detected (" + javaVersion + "). Only up to Java 15 is supported.");
                if (!Boolean.getBoolean("Paper.IgnoreJavaVersion")) return; // Paper
            }

            try {
                // Paper start - Handled by TerminalConsoleAppender
                /*
                // This trick bypasses Maven Shade's clever rewriting of our getProperty call when using String literals
                String jline_UnsupportedTerminal = new String(new char[]{'j', 'l', 'i', 'n', 'e', '.', 'U', 'n', 's', 'u', 'p', 'p', 'o', 'r', 't', 'e', 'd', 'T', 'e', 'r', 'm', 'i', 'n', 'a', 'l'});
                String jline_terminal = new String(new char[]{'j', 'l', 'i', 'n', 'e', '.', 't', 'e', 'r', 'm', 'i', 'n', 'a', 'l'});

                useJline = !(jline_UnsupportedTerminal).equals(System.getProperty(jline_terminal));

                if (options.has("nojline")) {
                    System.setProperty("user.language", "en");
                    useJline = false;
                }

                if (useJline) {
                    AnsiConsole.systemInstall();
                } else {
                    // This ensures the terminal literal will always match the jline implementation
                    System.setProperty(jline.TerminalFactory.JLINE_TERMINAL, jline.UnsupportedTerminal.class.getName());
                }
                */

                if (options.has("nojline")) {
                    System.setProperty(TerminalConsoleAppender.JLINE_OVERRIDE_PROPERTY, "false");
                    useJline = false;
                }
                // Paper end

                if (options.has("noconsole")) {
                    useConsole = false;
                    useJline = false; // Paper
                    System.setProperty(TerminalConsoleAppender.JLINE_OVERRIDE_PROPERTY, "false"); // Paper
                }

                if (Main.class.getPackage().getImplementationVendor() != null && System.getProperty("IReallyKnowWhatIAmDoingISwear") == null) {
                    Date buildDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(Main.class.getPackage().getImplementationVendor()); // Paper

                    Calendar deadline = Calendar.getInstance();
                    deadline.add(Calendar.DAY_OF_YEAR, -21);
                    if (buildDate.before(deadline.getTime())) {
                        // Paper start - This is some stupid bullshit
                        System.err.println("*** Warning, you've not updated in a while! ***");
                        System.err.println("*** Please download a new build as per instructions from https://papermc.io/downloads ***"); // Paper
                        //System.err.println("*** Server will start in 20 seconds ***");
                        //Thread.sleep(TimeUnit.SECONDS.toMillis(20));
                        // Paper End
                    }
                }

                // Paper start - Log Java and OS versioning to help with debugging plugin issues
                java.lang.management.RuntimeMXBean runtimeMX = java.lang.management.ManagementFactory.getRuntimeMXBean();
                java.lang.management.OperatingSystemMXBean osMX = java.lang.management.ManagementFactory.getOperatingSystemMXBean();
                if (runtimeMX != null && osMX != null) {
                    String javaInfo = "Java " + runtimeMX.getSpecVersion() + " (" + runtimeMX.getVmName() + " " + runtimeMX.getVmVersion() + ")";
                    String osInfo = "Host: " + osMX.getName() + " " + osMX.getVersion() + " (" + osMX.getArch() + ")";

                    System.out.println("System Info: " + javaInfo + " " + osInfo);
                } else {
                    System.out.println("Unable to read system info");
                }
                // Paper end
                System.setProperty( "library.jansi.version", "Paper" ); // Paper - set meaningless jansi version to prevent git builds from crashing on Windows
                System.out.println("Loading libraries, please wait...");
                net.minecraft.server.Main.main(options);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    private static List<String> asList(String... params) {
        return Arrays.asList(params);
    }
}
