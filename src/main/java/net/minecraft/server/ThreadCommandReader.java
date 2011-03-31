package net.minecraft.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import jline.ConsoleReader;

public class ThreadCommandReader extends Thread {

    final MinecraftServer a;

    public ThreadCommandReader(MinecraftServer minecraftserver) {
        this.a = minecraftserver;
    }

    public void run() {
        // Craftbukkit start - whole method, nuked to oblivion! :o

        try {
            ConsoleReader reader = a.reader;
            String line = null;
            while ((!this.a.g) && (MinecraftServer.a(this.a)) && ((line = reader.readLine(">", null)) != null)) {
                this.a.a(line, this.a);
            }
        } catch (IOException ex) {
            Logger.getLogger(ThreadCommandReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Craftbukkit end
    }
}
