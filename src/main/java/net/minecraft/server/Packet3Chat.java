package net.minecraft.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet3Chat extends Packet {

    public String a;

    public Packet3Chat() {}

    public Packet3Chat(String s) {
        // CraftBukkit start - handle this later
        //if (s.length() > 119) {
        //    s = s.substring(0, 119);
        //}
        // CraftBukkit end

        this.a = s;
    }

    public void a(DataInputStream datainputstream) throws IOException { // CraftBukkit
        this.a = a(datainputstream, 119);
    }

    public void a(DataOutputStream dataoutputstream) throws IOException { // CraftBukkit
        a(this.a, dataoutputstream);
    }

    public void a(NetHandler nethandler) {
        nethandler.a(this);
    }

    public int a() {
        return this.a.length();
    }
}
