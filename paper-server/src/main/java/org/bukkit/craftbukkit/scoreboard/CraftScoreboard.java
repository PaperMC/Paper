package org.bukkit.craftbukkit.scoreboard;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.ScoreHolder;
import net.minecraft.world.scores.Scoreboard;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Team;

public final class CraftScoreboard implements org.bukkit.scoreboard.Scoreboard {
    final Scoreboard board;

    CraftScoreboard(Scoreboard board) {
        this.board = board;
    }

    @Override
    public CraftObjective registerNewObjective(String name, String criteria) {
        return this.registerNewObjective(name, criteria, name);
    }
    // Paper start - Adventure
    @Override
    public CraftObjective registerNewObjective(String name, String criteria, net.kyori.adventure.text.Component displayName) {
        return this.registerNewObjective(name, CraftCriteria.getFromBukkit(criteria), displayName, RenderType.INTEGER);
    }
    @Override
    public CraftObjective registerNewObjective(String name, String criteria, net.kyori.adventure.text.Component displayName, RenderType renderType) {
        return this.registerNewObjective(name, CraftCriteria.getFromBukkit(criteria), displayName, renderType);
    }
    @Override
    public CraftObjective registerNewObjective(String name, Criteria criteria, net.kyori.adventure.text.Component displayName) throws IllegalArgumentException {
        return this.registerNewObjective(name, criteria, displayName, RenderType.INTEGER);
    }
    @Override
    public CraftObjective registerNewObjective(String name, Criteria criteria, net.kyori.adventure.text.Component displayName, RenderType renderType) throws IllegalArgumentException {
        if (displayName == null) {
            displayName = net.kyori.adventure.text.Component.empty();
        }
        Preconditions.checkArgument(name != null, "Objective name cannot be null");
        Preconditions.checkArgument(criteria != null, "Criteria cannot be null");
        Preconditions.checkArgument(renderType != null, "RenderType cannot be null");
        Preconditions.checkArgument(name.length() <= Short.MAX_VALUE, "The name '%s' is longer than the limit of 32767 characters (%s)", name, name.length());
        Preconditions.checkArgument(this.board.getObjective(name) == null, "An objective of name '%s' already exists", name);
        net.minecraft.world.scores.Objective objective = this.board.addObjective(name, ((CraftCriteria) criteria).criteria, io.papermc.paper.adventure.PaperAdventure.asVanilla(displayName), CraftScoreboardTranslations.fromBukkitRender(renderType), true, null);
        return new CraftObjective(this, objective);
    }
    // Paper end - Adventure

    @Override
    public CraftObjective registerNewObjective(String name, String criteria, String displayName) {
        return this.registerNewObjective(name, CraftCriteria.getFromBukkit(criteria), displayName, RenderType.INTEGER);
    }

    @Override
    public CraftObjective registerNewObjective(String name, String criteria, String displayName, RenderType renderType) {
        return this.registerNewObjective(name, CraftCriteria.getFromBukkit(criteria), displayName, renderType);
    }

    @Override
    public CraftObjective registerNewObjective(String name, Criteria criteria, String displayName) {
        return this.registerNewObjective(name, criteria, displayName, RenderType.INTEGER);
    }

    @Override
    public CraftObjective registerNewObjective(String name, Criteria criteria, String displayName, RenderType renderType) {
        return this.registerNewObjective(name, criteria, net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().deserialize(displayName), renderType); // Paper - Adventure
    }

    @Override
    public Objective getObjective(String name) {
        Preconditions.checkArgument(name != null, "Objective name cannot be null");
        net.minecraft.world.scores.Objective nms = this.board.getObjective(name);
        return nms == null ? null : new CraftObjective(this, nms);
    }

    @Override
    public ImmutableSet<Objective> getObjectivesByCriteria(String criteria) {
        Preconditions.checkArgument(criteria != null, "Criteria name cannot be null");

        ImmutableSet.Builder<Objective> objectives = ImmutableSet.builder();
        for (net.minecraft.world.scores.Objective netObjective : this.board.getObjectives()) {
            CraftObjective objective = new CraftObjective(this, netObjective);
            if (objective.getCriteria().equals(criteria)) {
                objectives.add(objective);
            }
        }
        return objectives.build();
    }

    @Override
    public ImmutableSet<Objective> getObjectivesByCriteria(Criteria criteria) {
        Preconditions.checkArgument(criteria != null, "Criteria cannot be null");

        ImmutableSet.Builder<Objective> objectives = ImmutableSet.builder();
        for (net.minecraft.world.scores.Objective netObjective : this.board.getObjectives()) {
            CraftObjective objective = new CraftObjective(this, netObjective);
            if (objective.getTrackedCriteria().equals(criteria)) {
                objectives.add(objective);
            }
        }

        return objectives.build();
    }

    @Override
    public ImmutableSet<Objective> getObjectives() {
        return ImmutableSet.copyOf(Iterables.transform(this.board.getObjectives(), (Function<net.minecraft.world.scores.Objective, Objective>) input -> new CraftObjective(CraftScoreboard.this, input)));
    }

    @Override
    public Objective getObjective(DisplaySlot slot) {
        Preconditions.checkArgument(slot != null, "Display slot cannot be null");
        net.minecraft.world.scores.Objective objective = this.board.getDisplayObjective(CraftScoreboardTranslations.fromBukkitSlot(slot));
        if (objective == null) {
            return null;
        }
        return new CraftObjective(this, objective);
    }

    @Override
    public ImmutableSet<Score> getScores(OfflinePlayer player) {
        return this.getScores(CraftScoreboard.getScoreHolder(player));
    }

    @Override
    public ImmutableSet<Score> getScores(String entry) {
        return this.getScores(CraftScoreboard.getScoreHolder(entry));
    }

    private ImmutableSet<Score> getScores(ScoreHolder entry) {
        Preconditions.checkArgument(entry != null, "Entry cannot be null");

        ImmutableSet.Builder<Score> scores = ImmutableSet.builder();
        for (net.minecraft.world.scores.Objective objective : this.board.getObjectives()) {
            scores.add(new CraftScore(new CraftObjective(this, objective), entry));
        }
        return scores.build();
    }

    @Override
    public void resetScores(OfflinePlayer player) {
        this.resetScores(CraftScoreboard.getScoreHolder(player));
    }

    @Override
    public void resetScores(String entry) {
        this.resetScores(CraftScoreboard.getScoreHolder(entry));
    }

    private void resetScores(ScoreHolder entry) {
        Preconditions.checkArgument(entry != null, "Entry cannot be null");

        for (net.minecraft.world.scores.Objective objective : this.board.getObjectives()) {
            this.board.resetSinglePlayerScore(entry, objective);
        }
    }

    @Override
    public Team getPlayerTeam(OfflinePlayer player) {
        Preconditions.checkArgument(player != null, "OfflinePlayer cannot be null");

        PlayerTeam team = this.board.getPlayersTeam(player.getName());
        return team == null ? null : new CraftTeam(this, team);
    }

    @Override
    public Team getEntryTeam(String entry) {
        Preconditions.checkArgument(entry != null, "Entry cannot be null");

        PlayerTeam team = this.board.getPlayersTeam(entry);
        return team == null ? null : new CraftTeam(this, team);
    }

    @Override
    public Team getTeam(String teamName) {
        Preconditions.checkArgument(teamName != null, "Team name cannot be null");

        PlayerTeam team = this.board.getPlayerTeam(teamName);
        return team == null ? null : new CraftTeam(this, team);
    }

    @Override
    public ImmutableSet<Team> getTeams() {
        return ImmutableSet.copyOf(Iterables.transform(this.board.getPlayerTeams(), (Function<PlayerTeam, Team>) input -> new CraftTeam(CraftScoreboard.this, input)));
    }

    @Override
    public Team registerNewTeam(String name) {
        Preconditions.checkArgument(name != null, "Team name cannot be null");
        Preconditions.checkArgument(name.length() <= Short.MAX_VALUE, "Team name '%s' is longer than the limit of 32767 characters (%s)", name, name.length());
        Preconditions.checkArgument(this.board.getPlayerTeam(name) == null, "Team name '%s' is already in use", name);

        return new CraftTeam(this, this.board.addPlayerTeam(name));
    }

    @Override
    public ImmutableSet<OfflinePlayer> getPlayers() {
        ImmutableSet.Builder<OfflinePlayer> players = ImmutableSet.builder();
        for (ScoreHolder playerName : this.board.getTrackedPlayers()) {
            players.add(Bukkit.getOfflinePlayer(playerName.getScoreboardName()));
        }
        return players.build();
    }

    @Override
    public ImmutableSet<String> getEntries() {
        ImmutableSet.Builder<String> entries = ImmutableSet.builder();
        for (ScoreHolder entry : this.board.getTrackedPlayers()) {
            entries.add(entry.getScoreboardName());
        }
        return entries.build();
    }

    @Override
    public void clearSlot(DisplaySlot slot) {
        Preconditions.checkArgument(slot != null, "Slot cannot be null");
        this.board.setDisplayObjective(CraftScoreboardTranslations.fromBukkitSlot(slot), null);
    }

    // CraftBukkit method
    public Scoreboard getHandle() {
        return this.board;
    }

    static ScoreHolder getScoreHolder(String entry) {
        return () -> entry;
    }

    static ScoreHolder getScoreHolder(OfflinePlayer player) {
        Preconditions.checkArgument(player != null, "OfflinePlayer cannot be null");

        if (player instanceof CraftPlayer craft) {
            return craft.getHandle();
        } else {
            return CraftScoreboard.getScoreHolder(player.getName());
        }
    }
}
