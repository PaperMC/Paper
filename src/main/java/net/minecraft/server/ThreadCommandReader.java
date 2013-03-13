package net.minecraft.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.bukkit.craftbukkit.Main.*; // CraftBukkit

class ThreadCommandReader extends Thread {

    final DedicatedServer server;

    ThreadCommandReader(DedicatedServer dedicatedserver) {
        this.server = dedicatedserver;
    }

    public void run() {
        // CraftBukkit start
        if (!useConsole) {
            return;
        }
        // CraftBukkit end

        jline.console.ConsoleReader bufferedreader = this.server.reader; // CraftBukkit
        String s;

        try {
            // CraftBukkit start - JLine disabling compatibility
            while (!this.server.isStopped() && this.server.isRunning()) {
                if (useJline) {
                    s = bufferedreader.readLine(">", null);
                } else {
                    s = bufferedreader.readLine();
                }
                if (s != null) {
                    this.server.issueCommand(s, this.server);
                }
                // CraftBukkit end
            }
        } catch (IOException ioexception) {
            // CraftBukkit
            java.util.logging.Logger.getLogger("").log(java.util.logging.Level.SEVERE, null, ioexception);
        }
    }
}
