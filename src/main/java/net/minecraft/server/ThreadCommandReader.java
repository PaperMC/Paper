package net.minecraft.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

// CraftBukkit start
import java.util.logging.Level;
import java.util.logging.Logger;
import jline.ConsoleReader;
// CraftBukkit end

public class ThreadCommandReader extends Thread {

    final MinecraftServer server;

    public ThreadCommandReader(MinecraftServer minecraftserver) {
        this.server = minecraftserver;
    }

    public void run() {
        // CraftBukkit
        ConsoleReader bufferedreader = this.server.reader;
        String s = null;

        try {
            // CraftBukkit
            while (!this.server.isStopped && MinecraftServer.isRunning(this.server) && ((s = bufferedreader.readLine(">", null)) != null)) {
                this.server.issueCommand(s, this.server);
            }
        } catch (IOException ioexception) {
            // CraftBukkit
            Logger.getLogger(ThreadCommandReader.class.getName()).log(Level.SEVERE, null, ioexception);
        }
    }
}
