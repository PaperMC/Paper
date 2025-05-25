package org.bukkit.craftbukkit.scoreboard;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
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
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.util.WeakCollection;
import org.bukkit.scoreboard.ScoreboardManager;

public final class CraftScoreboardManager implements ScoreboardManager {
    private final CraftScoreboard mainScoreboard;
    private final MinecraftServer server;
    private final Collection<CraftScoreboard> scoreboards = new WeakCollection<>();
    private final Map<CraftPlayer, CraftScoreboard> playerBoards = new HashMap<>();

    public CraftScoreboardManager(MinecraftServer server, net.minecraft.world.scores.Scoreboard scoreboard) {
        this.mainScoreboard = new CraftScoreboard(scoreboard);
        this.mainScoreboard.registeredGlobally = true;
        this.server = server;
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
        if (io.papermc.paper.configuration.GlobalConfiguration.get().scoreboards.trackPluginScoreboards) {
            scoreboard.registeredGlobally = true;
            this.scoreboards.add(scoreboard);
        }
        return scoreboard;
    }

    public void registerScoreboardForVanilla(CraftScoreboard scoreboard) {
        org.spigotmc.AsyncCatcher.catchOp("scoreboard registration");
        this.scoreboards.add(scoreboard);
    }

    public CraftScoreboard getPlayerBoard(CraftPlayer player) {
        CraftScoreboard board = this.playerBoards.get(player);
        return board == null ? this.getMainScoreboard() : board;
    }

    public void setPlayerBoard(CraftPlayer player, CraftScoreboard scoreboard) {
        net.minecraft.world.scores.Scoreboard oldBoard = this.getPlayerBoard(player).getHandle();
        net.minecraft.world.scores.Scoreboard newBoard = scoreboard.getHandle();
        if (oldBoard == newBoard) {
            return;
        }

        if (scoreboard == this.mainScoreboard) {
            this.playerBoards.remove(player);
        } else {
            this.playerBoards.put(player, scoreboard);
        }

        ServerPlayer serverPlayer = player.getHandle();

        // Old objective tracking
        HashSet<Objective> removed = new HashSet<>();
        for (net.minecraft.world.scores.DisplaySlot displaySlot : net.minecraft.world.scores.DisplaySlot.values()) { // Paper - clear all display slots
            Objective objective = oldBoard.getDisplayObjective(displaySlot); // Paper - clear all display slots
            if (objective != null && !removed.contains(objective)) {
                serverPlayer.connection.send(new ClientboundSetObjectivePacket(objective, 1));
                removed.add(objective);
            }
        }

        // Old team tracking
        for (final PlayerTeam team : oldBoard.getPlayerTeams()) {
            serverPlayer.connection.send(ClientboundSetPlayerTeamPacket.createRemovePacket(team));
        }

        // The above is the reverse of the below method.
        this.server.getPlayerList().updateEntireScoreboard((ServerScoreboard) newBoard, player.getHandle());
    }

    // CraftBukkit method
    public void removePlayer(CraftPlayer player) {
        this.playerBoards.remove(player);
    }

    // CraftBukkit method
    public void forAllObjectives(ObjectiveCriteria criteria, ScoreHolder holder, Consumer<ScoreAccess> consumer) {
        for (CraftScoreboard scoreboard : this.scoreboards) {
            scoreboard.getHandle().forAllObjectives(criteria, holder, consumer);
        }
    }
}
