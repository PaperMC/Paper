package net.minecraft.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.event.Event.Type;
import org.bukkit.event.player.PlayerEvent;

public class ServerConfigurationManager {

    public static Logger a = Logger.getLogger("Minecraft");
    public List b = new ArrayList();
    private MinecraftServer c;
    private PlayerManager d;
    private int e;
    private Set<String> f = new HashSet<String>();
    private Set<String> g = new HashSet<String>();
    private Set<String> h = new HashSet<String>();
    private File i;
    private File j;
    private File k;
    private PlayerNBTManager l;
    private CraftServer server; // Craftbukkit

    public ServerConfigurationManager(MinecraftServer paramMinecraftServer) {
        server = paramMinecraftServer.server; // Craftbukkit

        this.c = paramMinecraftServer;
        this.i = paramMinecraftServer.a("banned-players.txt");
        this.j = paramMinecraftServer.a("banned-ips.txt");
        this.k = paramMinecraftServer.a("ops.txt");
        this.d = new PlayerManager(paramMinecraftServer);
        this.e = paramMinecraftServer.d.a("max-players", 20);
        e();
        g();
        i();
        f();
        h();
        j();
    }

    public void a(WorldServer paramWorldServer) {
        this.l = new PlayerNBTManager(new File(paramWorldServer.t, "players"));
    }

    public int a() {
        return this.d.b();
    }

    public void a(EntityPlayerMP paramEntityPlayerMP) {
        this.b.add(paramEntityPlayerMP);
        this.l.b(paramEntityPlayerMP);

        this.c.e.A.d((int) paramEntityPlayerMP.p >> 4, (int) paramEntityPlayerMP.r >> 4);

        while (this.c.e.a(paramEntityPlayerMP, paramEntityPlayerMP.z).size() != 0) {
            paramEntityPlayerMP.a(paramEntityPlayerMP.p, paramEntityPlayerMP.q + 1.0D, paramEntityPlayerMP.r);
        }
        this.c.e.a(paramEntityPlayerMP);
        this.d.a(paramEntityPlayerMP);

        // Craftbukkit
        server.getPluginManager().callEvent(new PlayerEvent(Type.PLAYER_JOIN, server.getPlayer(paramEntityPlayerMP)));
    }

    public void b(EntityPlayerMP paramEntityPlayerMP) {
        this.d.c(paramEntityPlayerMP);
    }

    public void c(EntityPlayerMP paramEntityPlayerMP) {
        this.l.a(paramEntityPlayerMP);
        this.c.e.d(paramEntityPlayerMP);
        this.b.remove(paramEntityPlayerMP);
        this.d.b(paramEntityPlayerMP);

        // Craftbukkit
        server.getPluginManager().callEvent(new PlayerEvent(Type.PLAYER_QUIT, server.getPlayer(paramEntityPlayerMP)));
    }

    public EntityPlayerMP a(NetLoginHandler paramNetLoginHandler, String paramString1, String paramString2) {
        if (this.f.contains(paramString1.trim().toLowerCase())) {
            paramNetLoginHandler.a("You are banned from this server!");
            return null;
        }
        String str = paramNetLoginHandler.b.b().toString();
        str = str.substring(str.indexOf("/") + 1);
        str = str.substring(0, str.indexOf(":"));
        if (this.g.contains(str)) {
            paramNetLoginHandler.a("Your IP address is banned from this server!");
            return null;
        }
        if (this.b.size() >= this.e) {
            paramNetLoginHandler.a("The server is full!");
            return null;
        }
        for (int m = 0; m < this.b.size(); m++) {
            EntityPlayerMP localEntityPlayerMP = (EntityPlayerMP) this.b.get(m);
            if (localEntityPlayerMP.aw.equalsIgnoreCase(paramString1)) {
                localEntityPlayerMP.a.a("You logged in from another location");
            }
        }
        return new EntityPlayerMP(this.c, this.c.e, paramString1, new ItemInWorldManager(this.c.e));
    }

    public EntityPlayerMP d(EntityPlayerMP paramEntityPlayerMP) {
        this.c.k.a(paramEntityPlayerMP);
        this.c.k.b(paramEntityPlayerMP);
        this.d.b(paramEntityPlayerMP);
        this.b.remove(paramEntityPlayerMP);
        this.c.e.e(paramEntityPlayerMP);

        EntityPlayerMP localEntityPlayerMP = new EntityPlayerMP(this.c, this.c.e, paramEntityPlayerMP.aw, new ItemInWorldManager(this.c.e));
        localEntityPlayerMP.g = paramEntityPlayerMP.g;
        localEntityPlayerMP.a = paramEntityPlayerMP.a;

        this.c.e.A.d((int) localEntityPlayerMP.p >> 4, (int) localEntityPlayerMP.r >> 4);

        while (this.c.e.a(localEntityPlayerMP, localEntityPlayerMP.z).size() != 0) {
            localEntityPlayerMP.a(localEntityPlayerMP.p, localEntityPlayerMP.q + 1.0D, localEntityPlayerMP.r);
        }

        localEntityPlayerMP.a.b(new Packet9());
        localEntityPlayerMP.a.a(localEntityPlayerMP.p, localEntityPlayerMP.q, localEntityPlayerMP.r, localEntityPlayerMP.v, localEntityPlayerMP.w);

        this.d.a(localEntityPlayerMP);
        this.c.e.a(localEntityPlayerMP);
        this.b.add(localEntityPlayerMP);

        localEntityPlayerMP.k();
        return localEntityPlayerMP;
    }

    public void b() {
        this.d.a();
    }

    public void a(int paramInt1, int paramInt2, int paramInt3) {
        this.d.a(paramInt1, paramInt2, paramInt3);
    }

    public void a(Packet paramPacket) {
        for (int m = 0; m < this.b.size(); m++) {
            EntityPlayerMP localEntityPlayerMP = (EntityPlayerMP) this.b.get(m);
            localEntityPlayerMP.a.b(paramPacket);
        }
    }

    public String c() {
        String str = "";
        for (int m = 0; m < this.b.size(); m++) {
            if (m > 0) {
                str = str + ", ";
            }
            str = str + ((EntityPlayerMP) this.b.get(m)).aw;
        }
        return str;
    }

    public void a(String paramString) {
        this.f.add(paramString.toLowerCase());
        f();
    }

    public void b(String paramString) {
        this.f.remove(paramString.toLowerCase());
        f();
    }

    private void e() {
        try {
            this.f.clear();
            BufferedReader localBufferedReader = new BufferedReader(new FileReader(this.i));
            String str = "";
            while ((str = localBufferedReader.readLine()) != null) {
                this.f.add(str.trim().toLowerCase());
            }
            localBufferedReader.close();
        } catch (Exception localException) {
            a.warning("Failed to load ban list: " + localException);
        }
    }

    private void f() {
        try {
            PrintWriter localPrintWriter = new PrintWriter(new FileWriter(this.i, false));
            for (String str : this.f) {
                localPrintWriter.println(str);
            }
            localPrintWriter.close();
        } catch (Exception localException) {
            a.warning("Failed to save ban list: " + localException);
        }
    }

    public void c(String paramString) {
        this.g.add(paramString.toLowerCase());
        h();
    }

    public void d(String paramString) {
        this.g.remove(paramString.toLowerCase());
        h();
    }

    private void g() {
        try {
            this.g.clear();
            BufferedReader localBufferedReader = new BufferedReader(new FileReader(this.j));
            String str = "";
            while ((str = localBufferedReader.readLine()) != null) {
                this.g.add(str.trim().toLowerCase());
            }
            localBufferedReader.close();
        } catch (Exception localException) {
            a.warning("Failed to load ip ban list: " + localException);
        }
    }

    private void h() {
        try {
            PrintWriter localPrintWriter = new PrintWriter(new FileWriter(this.j, false));
            for (String str : this.g) {
                localPrintWriter.println(str);
            }
            localPrintWriter.close();
        } catch (Exception localException) {
            a.warning("Failed to save ip ban list: " + localException);
        }
    }

    public void e(String paramString) {
        this.h.add(paramString.toLowerCase());
        j();
    }

    public void f(String paramString) {
        this.h.remove(paramString.toLowerCase());
        j();
    }

    private void i() {
        try {
            this.h.clear();
            BufferedReader localBufferedReader = new BufferedReader(new FileReader(this.k));
            String str = "";
            while ((str = localBufferedReader.readLine()) != null) {
                this.h.add(str.trim().toLowerCase());
            }
            localBufferedReader.close();
        } catch (Exception localException) {
            a.warning("Failed to load ip ban list: " + localException);
        }
    }

    private void j() {
        try {
            PrintWriter localPrintWriter = new PrintWriter(new FileWriter(this.k, false));
            for (String str : this.h) {
                localPrintWriter.println(str);
            }
            localPrintWriter.close();
        } catch (Exception localException) {
            a.warning("Failed to save ip ban list: " + localException);
        }
    }

    public boolean g(String paramString) {
        return this.h.contains(paramString.trim().toLowerCase());
    }

    public EntityPlayerMP h(String paramString) {
        for (int m = 0; m < this.b.size(); m++) {
            EntityPlayerMP localEntityPlayerMP = (EntityPlayerMP) this.b.get(m);
            if (localEntityPlayerMP.aw.equalsIgnoreCase(paramString)) {
                return localEntityPlayerMP;
            }
        }
        return null;
    }

    public void a(String paramString1, String paramString2) {
        EntityPlayerMP localEntityPlayerMP = h(paramString1);
        if (localEntityPlayerMP != null) {
            localEntityPlayerMP.a.b(new Packet3Chat(paramString2));
        }
    }

    public void a(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, Packet paramPacket) {
        for (int m = 0; m < this.b.size(); m++) {
            EntityPlayerMP localEntityPlayerMP = (EntityPlayerMP) this.b.get(m);
            double d1 = paramDouble1 - localEntityPlayerMP.p;
            double d2 = paramDouble2 - localEntityPlayerMP.q;
            double d3 = paramDouble3 - localEntityPlayerMP.r;
            if (d1 * d1 + d2 * d2 + d3 * d3 < paramDouble4 * paramDouble4) {
                localEntityPlayerMP.a.b(paramPacket);
            }
        }
    }

    public void i(String paramString) {
        Packet3Chat localPacket3Chat = new Packet3Chat(paramString);
        for (int m = 0; m < this.b.size(); m++) {
            EntityPlayerMP localEntityPlayerMP = (EntityPlayerMP) this.b.get(m);
            if (g(localEntityPlayerMP.aw)) {
                localEntityPlayerMP.a.b(localPacket3Chat);
            }
        }
    }

    public boolean a(String paramString, Packet paramPacket) {
        EntityPlayerMP localEntityPlayerMP = h(paramString);
        if (localEntityPlayerMP != null) {
            localEntityPlayerMP.a.b(paramPacket);
            return true;
        }
        return false;
    }

    public void d() {
        for (int m = 0; m < this.b.size(); m++) {
            this.l.a((EntityPlayerMP) this.b.get(m));
        }
    }

    public void a(int paramInt1, int paramInt2, int paramInt3, TileEntity paramTileEntity) {
    }
}
