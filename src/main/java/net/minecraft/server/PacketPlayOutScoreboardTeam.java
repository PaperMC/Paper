package net.minecraft.server;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

public class PacketPlayOutScoreboardTeam implements Packet<PacketListenerPlayOut> {

    private String a = "";
    private IChatBaseComponent b;
    private IChatBaseComponent c;
    private IChatBaseComponent d;
    private String e;
    private String f;
    private EnumChatFormat g;
    private final Collection<String> h;
    private int i;
    private int j;

    public PacketPlayOutScoreboardTeam() {
        this.b = ChatComponentText.d;
        this.c = ChatComponentText.d;
        this.d = ChatComponentText.d;
        this.e = ScoreboardTeamBase.EnumNameTagVisibility.ALWAYS.e;
        this.f = ScoreboardTeamBase.EnumTeamPush.ALWAYS.e;
        this.g = EnumChatFormat.RESET;
        this.h = Lists.newArrayList();
    }

    public PacketPlayOutScoreboardTeam(ScoreboardTeam scoreboardteam, int i) {
        this.b = ChatComponentText.d;
        this.c = ChatComponentText.d;
        this.d = ChatComponentText.d;
        this.e = ScoreboardTeamBase.EnumNameTagVisibility.ALWAYS.e;
        this.f = ScoreboardTeamBase.EnumTeamPush.ALWAYS.e;
        this.g = EnumChatFormat.RESET;
        this.h = Lists.newArrayList();
        this.a = scoreboardteam.getName();
        this.i = i;
        if (i == 0 || i == 2) {
            this.b = scoreboardteam.getDisplayName();
            this.j = scoreboardteam.packOptionData();
            this.e = scoreboardteam.getNameTagVisibility().e;
            this.f = scoreboardteam.getCollisionRule().e;
            this.g = scoreboardteam.getColor();
            this.c = scoreboardteam.getPrefix();
            this.d = scoreboardteam.getSuffix();
        }

        if (i == 0) {
            this.h.addAll(scoreboardteam.getPlayerNameSet());
        }

    }

    public PacketPlayOutScoreboardTeam(ScoreboardTeam scoreboardteam, Collection<String> collection, int i) {
        this.b = ChatComponentText.d;
        this.c = ChatComponentText.d;
        this.d = ChatComponentText.d;
        this.e = ScoreboardTeamBase.EnumNameTagVisibility.ALWAYS.e;
        this.f = ScoreboardTeamBase.EnumTeamPush.ALWAYS.e;
        this.g = EnumChatFormat.RESET;
        this.h = Lists.newArrayList();
        if (i != 3 && i != 4) {
            throw new IllegalArgumentException("Method must be join or leave for player constructor");
        } else if (collection != null && !collection.isEmpty()) {
            this.i = i;
            this.a = scoreboardteam.getName();
            this.h.addAll(collection);
        } else {
            throw new IllegalArgumentException("Players cannot be null/empty");
        }
    }

    @Override
    public void a(PacketDataSerializer packetdataserializer) throws IOException {
        this.a = packetdataserializer.e(16);
        this.i = packetdataserializer.readByte();
        if (this.i == 0 || this.i == 2) {
            this.b = packetdataserializer.h();
            this.j = packetdataserializer.readByte();
            this.e = packetdataserializer.e(40);
            this.f = packetdataserializer.e(40);
            this.g = (EnumChatFormat) packetdataserializer.a(EnumChatFormat.class);
            this.c = packetdataserializer.h();
            this.d = packetdataserializer.h();
        }

        if (this.i == 0 || this.i == 3 || this.i == 4) {
            int i = packetdataserializer.i();

            for (int j = 0; j < i; ++j) {
                this.h.add(packetdataserializer.e(40));
            }
        }

    }

    @Override
    public void b(PacketDataSerializer packetdataserializer) throws IOException {
        packetdataserializer.a(this.a);
        packetdataserializer.writeByte(this.i);
        if (this.i == 0 || this.i == 2) {
            packetdataserializer.a(this.b);
            packetdataserializer.writeByte(this.j);
            packetdataserializer.a(this.e);
            packetdataserializer.a(this.f);
            packetdataserializer.a((Enum) this.g);
            packetdataserializer.a(this.c);
            packetdataserializer.a(this.d);
        }

        if (this.i == 0 || this.i == 3 || this.i == 4) {
            packetdataserializer.d(this.h.size());
            Iterator iterator = this.h.iterator();

            while (iterator.hasNext()) {
                String s = (String) iterator.next();

                packetdataserializer.a(s);
            }
        }

    }

    public void a(PacketListenerPlayOut packetlistenerplayout) {
        packetlistenerplayout.a(this);
    }
}
