package net.minecraft.server;

import java.io.*;

public class ThreadCommandReader extends Thread {

    final MinecraftServer a; /* synthetic field */

    public ThreadCommandReader(MinecraftServer minecraftserver) {
        super();
        a = minecraftserver;
    }

    public void run() {
        BufferedReader bufferedreader = new BufferedReader(((java.io.Reader) (new InputStreamReader(System.in))));
        String s = null;

        try {
            while (!a.g && MinecraftServer.a(a) && (s = bufferedreader.readLine()) != null) { 
                a.a("#" + s, ((ICommandListener) (a)));
            }
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
        }
    }
}
