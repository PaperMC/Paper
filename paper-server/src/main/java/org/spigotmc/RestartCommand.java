package org.spigotmc;

import java.io.File;
import java.util.Locale;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.util.CraftChatMessage;

public class RestartCommand extends Command {

    public RestartCommand(String name) {
        super(name);
        this.description = "Restarts the server";
        this.usageMessage = "/restart";
        this.setPermission("bukkit.command.restart");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (this.testPermission(sender)) {
            MinecraftServer.getServer().processQueue.add(RestartCommand::restart);
        }
        return true;
    }

    public static void restart() {
        RestartCommand.restart(SpigotConfig.restartScript);
    }

    private static void restart(final String restartScript) {
        try {
            // Paper - extract method and cleanup
            boolean isRestarting = addShutdownHook(restartScript);
            if (isRestarting) {
                System.out.println("Attempting to restart with " + SpigotConfig.restartScript);
            } else {
                System.out.println("Startup script '" + SpigotConfig.restartScript + "' does not exist! Stopping server.");
            }
            // Stop the watchdog
            WatchdogThread.doStop();

            shutdownServer(isRestarting);
            // Paper end
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Paper start - sync copied from above with minor changes, async added
    private static void shutdownServer(boolean isRestarting) {
        if (MinecraftServer.getServer().isSameThread()) {
            // Kick all players
            for (ServerPlayer p : com.google.common.collect.ImmutableList.copyOf(MinecraftServer.getServer().getPlayerList().players)) {
                p.connection.disconnect(CraftChatMessage.fromStringOrEmpty(SpigotConfig.restartMessage, true), org.bukkit.event.player.PlayerKickEvent.Cause.RESTART_COMMAND); // Paper - kick event reason (cause is never used)
            }
            // Give the socket a chance to send the packets
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
            }

            closeSocket();

            // Actually shutdown
            try {
                MinecraftServer.getServer().close(); // calls stop()
            } catch (Throwable t) {
            }

            // Actually stop the JVM
            System.exit(0);
        } else {
            // Mark the server to shutdown at the end of the tick
            MinecraftServer.getServer().safeShutdown(false, isRestarting);

            // wait 10 seconds to see if we're actually going to try shutdown
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ignored) {}

            // Check if we've actually hit a state where the server is going to safely shutdown
            // if we have, let the server stop as usual
            if (MinecraftServer.getServer().isStopped()) return;

            // If the server hasn't stopped by now, assume worse case and kill
            closeSocket();
            System.exit(0);
        }
    }
    // Paper end

    // Paper - Split from moved code
    private static void closeSocket() {
        // Close the socket so we can rebind with the new process
        MinecraftServer.getServer().getConnection().stop();

        // Give time for it to kick in
        try {
            Thread.sleep(100);
        } catch (InterruptedException ignored) {}
    }
    // Paper end

    // Paper start - copied from above and modified to return if the hook registered
    public static boolean addShutdownHook(String restartScript) {
        String[] split = restartScript.split(" ");
        if (split.length > 0 && new File(split[0]).isFile()) {
            Thread shutdownHook = new Thread(() -> {
                try {
                    String os = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
                    if (os.contains("win")) {
                        Runtime.getRuntime().exec("cmd /c start " + restartScript);
                    } else {
                        Runtime.getRuntime().exec("sh " + restartScript);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            shutdownHook.setDaemon(true);
            Runtime.getRuntime().addShutdownHook(shutdownHook);
            return true;
        } else {
            return false;
        }
    }
    // Paper end
}
