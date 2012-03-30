package net.minecraft.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.io.IOException; // CraftBukkit

public class Packet3Chat extends Packet {

    public static int b = 119;
    public String message;

    public Packet3Chat() {}

    public Packet3Chat(String s) {
        /* CraftBukkit start - handle this later
        if (s.length() > b) {
            s = s.substring(0, b);
        }
        // CraftBukkit end */

        this.message = s;
    }

    public void a(DataInputStream datainputstream) throws IOException { // CraftBukkit
        this.message = a(datainputstream, b);
    }

    public void a(DataOutputStream dataoutputstream) throws IOException { // CraftBukkit
        a(this.message, dataoutputstream);
    }

    public void handle(NetHandler nethandler) {
        nethandler.a(this);
    }

    public int a() {
        return 2 + this.message.length() * 2;
    }
}
