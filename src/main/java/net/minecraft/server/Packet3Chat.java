package net.minecraft.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.io.IOException; // CraftBukkit

public class Packet3Chat extends Packet {

    public String message;

    public Packet3Chat() {}

    public Packet3Chat(String s) {
        /* CraftBukkit start - handle this later
        if (s.length() > 119) {
            s = s.substring(0, 119);
        }
        // CraftBukkit end */

        this.message = s;
    }

    public void a(DataInputStream datainputstream) throws IOException { // CraftBukkit
        this.message = a(datainputstream, 119);
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
