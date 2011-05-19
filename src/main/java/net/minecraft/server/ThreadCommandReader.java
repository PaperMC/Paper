package net.minecraft.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

// CraftBukkit start
import java.util.logging.Level;
import java.util.logging.Logger;
import jline.ConsoleReader;
import org.bukkit.craftbukkit.Main;
// CraftBukkit end

public class ThreadCommandReader extends Thread {

    final MinecraftServer server;

    public ThreadCommandReader(MinecraftServer minecraftserver) {
        this.server = minecraftserver;
    }

    public void run() {
        // CraftBukkit start
        ConsoleReader bufferedreader = this.server.reader;
        String s = null;
        // CraftBukkit end

        try {
            while (!this.server.isStopped && MinecraftServer.isRunning(this.server)) {
                // CraftBukkit start - JLine disabling compatibility
                if (Main.useJline) {
                    s = bufferedreader.readLine(">", null);
                } else {
                    s = bufferedreader.readLine();
                }
                if (s != null) {
                    // CraftBukkit end
                    this.server.issueCommand(s, this.server);
                }
            }
        } catch (IOException ioexception) {
            // CraftBukkit
            Logger.getLogger(ThreadCommandReader.class.getName()).log(Level.SEVERE, null, ioexception);
        }
    }
}
