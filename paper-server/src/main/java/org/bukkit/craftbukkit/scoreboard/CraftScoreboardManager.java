package org.bukkit.craftbukkit.scoreboard;

import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.network.protocol.game.ClientboundSetObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.ScoreAccess;
import net.minecraft.world.scores.ScoreHolder;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.util.WeakCollection;
import org.bukkit.scoreboard.ScoreboardManager;

public final class CraftScoreboardManager implements ScoreboardManager {
    private final CraftScoreboard mainScoreboard;
    private final MinecraftServer server;
    private final Collection<CraftScoreboard> scoreboards = new WeakCollection<>();
    private final Map<CraftPlayer, CraftScoreboard> playerBoards = new HashMap<>();

    public CraftScoreboardManager(MinecraftServer minecraftserver, net.minecraft.world.scores.Scoreboard scoreboardServer) {
        this.mainScoreboard = new CraftScoreboard(scoreboardServer);
        this.server = minecraftserver;
        this.scoreboards.add(this.mainScoreboard);
    }

    @Override
    public CraftScoreboard getMainScoreboard() {
        return this.mainScoreboard;
    }

    @Override
    public CraftScoreboard getNewScoreboard() {
        org.spigotmc.AsyncCatcher.catchOp("scoreboard creation"); // Spigot
        CraftScoreboard scoreboard = new CraftScoreboard(new ServerScoreboard(this.server));
        this.scoreboards.add(scoreboard);
        return scoreboard;
    }

    // CraftBukkit method
    public CraftScoreboard getPlayerBoard(CraftPlayer player) {
        CraftScoreboard board = this.playerBoards.get(player);
        return board == null ? this.getMainScoreboard() : board;
    }

    // CraftBukkit method
    public void setPlayerBoard(CraftPlayer player, org.bukkit.scoreboard.Scoreboard bukkitScoreboard) {
        Preconditions.checkArgument(bukkitScoreboard instanceof CraftScoreboard, "Cannot set player scoreboard to an unregistered Scoreboard");

        CraftScoreboard scoreboard = (CraftScoreboard) bukkitScoreboard;
        net.minecraft.world.scores.Scoreboard oldboard = this.getPlayerBoard(player).getHandle();
        net.minecraft.world.scores.Scoreboard newboard = scoreboard.getHandle();
        ServerPlayer entityplayer = player.getHandle();

        if (oldboard == newboard) {
            return;
        }

        if (scoreboard == this.mainScoreboard) {
            this.playerBoards.remove(player);
        } else {
            this.playerBoards.put(player, scoreboard);
        }

        // Old objective tracking
        HashSet<Objective> removed = new HashSet<>();
        for (int i = 0; i < 3; ++i) {
            Objective scoreboardobjective = oldboard.getDisplayObjective(net.minecraft.world.scores.DisplaySlot.BY_ID.apply(i));
            if (scoreboardobjective != null && !removed.contains(scoreboardobjective)) {
                entityplayer.connection.send(new ClientboundSetObjectivePacket(scoreboardobjective, 1));
                removed.add(scoreboardobjective);
            }
        }

        // Old team tracking
        Iterator<?> iterator = oldboard.getPlayerTeams().iterator();
        while (iterator.hasNext()) {
            PlayerTeam scoreboardteam = (PlayerTeam) iterator.next();
            entityplayer.connection.send(ClientboundSetPlayerTeamPacket.createRemovePacket(scoreboardteam));
        }

        // The above is the reverse of the below method.
        this.server.getPlayerList().updateEntireScoreboard((ServerScoreboard) newboard, player.getHandle());
    }

    // CraftBukkit method
    public void removePlayer(CraftPlayer player) {
        this.playerBoards.remove(player);
    }

    // CraftBukkit method
    public void forAllObjectives(ObjectiveCriteria criteria, ScoreHolder holder, Consumer<ScoreAccess> consumer) {
        for (CraftScoreboard scoreboard : this.scoreboards) {
            Scoreboard board = scoreboard.board;
            board.forAllObjectives(criteria, holder, (score) -> consumer.accept(score));
        }
    }
}
