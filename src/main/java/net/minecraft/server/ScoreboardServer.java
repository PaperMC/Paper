package net.minecraft.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ScoreboardServer extends Scoreboard {

    private final MinecraftServer a;
    private final Set b = new HashSet();
    private ScoreboardSaveData c;

    public ScoreboardServer(MinecraftServer minecraftserver) {
        this.a = minecraftserver;
    }

    public void handleScoreChanged(ScoreboardScore scoreboardscore) {
        super.handleScoreChanged(scoreboardscore);
        if (this.b.contains(scoreboardscore.getObjective())) {
            this.sendAll(new Packet207SetScoreboardScore(scoreboardscore, 0)); // CraftBukkit - Internal packet method
        }

        this.b();
    }

    public void handlePlayerRemoved(String s) {
        super.handlePlayerRemoved(s);
        this.sendAll(new Packet207SetScoreboardScore(s)); // CraftBukkit - Internal packet method
        this.b();
    }

    public void setDisplaySlot(int i, ScoreboardObjective scoreboardobjective) {
        ScoreboardObjective scoreboardobjective1 = this.getObjectiveForSlot(i);

        super.setDisplaySlot(i, scoreboardobjective);
        if (scoreboardobjective1 != scoreboardobjective && scoreboardobjective1 != null) {
            if (this.h(scoreboardobjective1) > 0) {
                this.sendAll(new Packet208SetScoreboardDisplayObjective(i, scoreboardobjective)); // CraftBukkit - Internal packet method
            } else {
                this.g(scoreboardobjective1);
            }
        }

        if (scoreboardobjective != null) {
            if (this.b.contains(scoreboardobjective)) {
                this.sendAll(new Packet208SetScoreboardDisplayObjective(i, scoreboardobjective)); // CraftBukkit - Internal packet method
            } else {
                this.e(scoreboardobjective);
            }
        }

        this.b();
    }

    public void addPlayerToTeam(String s, ScoreboardTeam scoreboardteam) {
        super.addPlayerToTeam(s, scoreboardteam);
        this.sendAll(new Packet209SetScoreboardTeam(scoreboardteam, Arrays.asList(new String[] { s}), 3)); // CraftBukkit - Internal packet method
        this.b();
    }

    public void removePlayerFromTeam(String s, ScoreboardTeam scoreboardteam) {
        super.removePlayerFromTeam(s, scoreboardteam);
        this.sendAll(new Packet209SetScoreboardTeam(scoreboardteam, Arrays.asList(new String[] { s}), 4)); // CraftBukkit - Internal packet method
        this.b();
    }

    public void handleObjectiveAdded(ScoreboardObjective scoreboardobjective) {
        super.handleObjectiveAdded(scoreboardobjective);
        this.b();
    }

    public void handleObjectiveChanged(ScoreboardObjective scoreboardobjective) {
        super.handleObjectiveChanged(scoreboardobjective);
        if (this.b.contains(scoreboardobjective)) {
            this.sendAll(new Packet206SetScoreboardObjective(scoreboardobjective, 2)); // CraftBukkit - Internal packet method
        }

        this.b();
    }

    public void handleObjectiveRemoved(ScoreboardObjective scoreboardobjective) {
        super.handleObjectiveRemoved(scoreboardobjective);
        if (this.b.contains(scoreboardobjective)) {
            this.g(scoreboardobjective);
        }

        this.b();
    }

    public void handleTeamAdded(ScoreboardTeam scoreboardteam) {
        super.handleTeamAdded(scoreboardteam);
        this.sendAll(new Packet209SetScoreboardTeam(scoreboardteam, 0)); // CraftBukkit - Internal packet method
        this.b();
    }

    public void handleTeamChanged(ScoreboardTeam scoreboardteam) {
        super.handleTeamChanged(scoreboardteam);
        this.sendAll(new Packet209SetScoreboardTeam(scoreboardteam, 2)); // CraftBukkit - Internal packet method
        this.b();
    }

    public void handleTeamRemoved(ScoreboardTeam scoreboardteam) {
        super.handleTeamRemoved(scoreboardteam);
        this.sendAll(new Packet209SetScoreboardTeam(scoreboardteam, 1)); // CraftBukkit - Internal packet method
        this.b();
    }

    public void a(ScoreboardSaveData scoreboardsavedata) {
        this.c = scoreboardsavedata;
    }

    protected void b() {
        if (this.c != null) {
            this.c.c();
        }
    }

    public List getScoreboardScorePacketsForObjective(ScoreboardObjective scoreboardobjective) {
        ArrayList arraylist = new ArrayList();

        arraylist.add(new Packet206SetScoreboardObjective(scoreboardobjective, 0));

        for (int i = 0; i < 3; ++i) {
            if (this.getObjectiveForSlot(i) == scoreboardobjective) {
                arraylist.add(new Packet208SetScoreboardDisplayObjective(i, scoreboardobjective));
            }
        }

        Iterator iterator = this.getScoresForObjective(scoreboardobjective).iterator();

        while (iterator.hasNext()) {
            ScoreboardScore scoreboardscore = (ScoreboardScore) iterator.next();

            arraylist.add(new Packet207SetScoreboardScore(scoreboardscore, 0));
        }

        return arraylist;
    }

    public void e(ScoreboardObjective scoreboardobjective) {
        List list = this.getScoreboardScorePacketsForObjective(scoreboardobjective);
        Iterator iterator = this.a.getPlayerList().players.iterator();

        while (iterator.hasNext()) {
            EntityPlayer entityplayer = (EntityPlayer) iterator.next();
            if (entityplayer.getBukkitEntity().getScoreboard().getHandle() != this) continue; // CraftBukkit - Only players on this board
            Iterator iterator1 = list.iterator();

            while (iterator1.hasNext()) {
                Packet packet = (Packet) iterator1.next();

                entityplayer.playerConnection.sendPacket(packet);
            }
        }

        this.b.add(scoreboardobjective);
    }

    public List f(ScoreboardObjective scoreboardobjective) {
        ArrayList arraylist = new ArrayList();

        arraylist.add(new Packet206SetScoreboardObjective(scoreboardobjective, 1));

        for (int i = 0; i < 3; ++i) {
            if (this.getObjectiveForSlot(i) == scoreboardobjective) {
                arraylist.add(new Packet208SetScoreboardDisplayObjective(i, scoreboardobjective));
            }
        }

        return arraylist;
    }

    public void g(ScoreboardObjective scoreboardobjective) {
        List list = this.f(scoreboardobjective);
        Iterator iterator = this.a.getPlayerList().players.iterator();

        while (iterator.hasNext()) {
            EntityPlayer entityplayer = (EntityPlayer) iterator.next();
            if (entityplayer.getBukkitEntity().getScoreboard().getHandle() != this) continue; // CraftBukkit - Only players on this board
            Iterator iterator1 = list.iterator();

            while (iterator1.hasNext()) {
                Packet packet = (Packet) iterator1.next();

                entityplayer.playerConnection.sendPacket(packet);
            }
        }

        this.b.remove(scoreboardobjective);
    }

    public int h(ScoreboardObjective scoreboardobjective) {
        int i = 0;

        for (int j = 0; j < 3; ++j) {
            if (this.getObjectiveForSlot(j) == scoreboardobjective) {
                ++i;
            }
        }

        return i;
    }

    // CraftBukkit start - Send to players
    private void sendAll(Packet packet) {
        for (EntityPlayer entityplayer : (List<EntityPlayer>) this.a.getPlayerList().players) {
            if (entityplayer.getBukkitEntity().getScoreboard().getHandle() == this) {
                entityplayer.playerConnection.sendPacket(packet);
            }
        }
    }
    // CraftBukkit end
}
