package net.minecraft.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import jline.ConsoleReader;

public class ThreadCommandReader extends Thread {

    final MinecraftServer server;

    public ThreadCommandReader(MinecraftServer minecraftserver) {
        this.server = minecraftserver;
    }

    public void run() {
        // Craftbukkit start - whole method, nuked to oblivion! :o

        try {
            ConsoleReader reader = this.server.reader;
            String line = null;
            while ((!this.server.isStopped) && (MinecraftServer.isRunning(this.server)) && ((line = reader.readLine(">", null)) != null)) {
                this.server.issueCommand(line, (ICommandListener) this.server);
            }
        } catch (IOException ioexception) {
            Logger.getLogger(ThreadCommandReader.class.getName()).log(Level.SEVERE, null, ioexception);
        }
        // Craftbukkit end
    }
}
