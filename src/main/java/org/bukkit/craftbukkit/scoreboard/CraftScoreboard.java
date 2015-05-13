package org.bukkit.craftbukkit.scoreboard;

import java.util.Collection;
import net.minecraft.server.Scoreboard;
import net.minecraft.server.ScoreboardObjective;
import net.minecraft.server.ScoreboardTeam;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Team;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;

public final class CraftScoreboard implements org.bukkit.scoreboard.Scoreboard {
    final Scoreboard board;

    CraftScoreboard(Scoreboard board) {
        this.board = board;
    }

    public CraftObjective registerNewObjective(String name, String criteria) throws IllegalArgumentException {
        Validate.notNull(name, "Objective name cannot be null");
        Validate.notNull(criteria, "Criteria cannot be null");
        Validate.isTrue(name.length() <= 16, "The name '" + name + "' is longer than the limit of 16 characters");
        Validate.isTrue(board.getObjective(name) == null, "An objective of name '" + name + "' already exists");

        CraftCriteria craftCriteria = CraftCriteria.getFromBukkit(criteria);
        ScoreboardObjective objective = board.registerObjective(name, craftCriteria.criteria);
        return new CraftObjective(this, objective);
    }

    public Objective getObjective(String name) throws IllegalArgumentException {
        Validate.notNull(name, "Name cannot be null");
        ScoreboardObjective nms = board.getObjective(name);
        return nms == null ? null : new CraftObjective(this, nms);
    }

    public ImmutableSet<Objective> getObjectivesByCriteria(String criteria) throws IllegalArgumentException {
        Validate.notNull(criteria, "Criteria cannot be null");

        ImmutableSet.Builder<Objective> objectives = ImmutableSet.builder();
        for (ScoreboardObjective netObjective : (Collection<ScoreboardObjective>) this.board.getObjectives()) {
            CraftObjective objective = new CraftObjective(this, netObjective);
            if (objective.getCriteria().equals(criteria)) {
                objectives.add(objective);
            }
        }
        return objectives.build();
    }

    public ImmutableSet<Objective> getObjectives() {
        return ImmutableSet.copyOf(Iterables.transform((Collection<ScoreboardObjective>) this.board.getObjectives(), new Function<ScoreboardObjective, Objective>() {

            @Override
            public Objective apply(ScoreboardObjective input) {
                return new CraftObjective(CraftScoreboard.this, input);
            }
        }));
    }

    public Objective getObjective(DisplaySlot slot) throws IllegalArgumentException {
        Validate.notNull(slot, "Display slot cannot be null");
        ScoreboardObjective objective = board.getObjectiveForSlot(CraftScoreboardTranslations.fromBukkitSlot(slot));
        if (objective == null) {
            return null;
        }
        return new CraftObjective(this, objective);
    }

    public ImmutableSet<Score> getScores(OfflinePlayer player) throws IllegalArgumentException {
        Validate.notNull(player, "OfflinePlayer cannot be null");

        return getScores(player.getName());
    }

    public ImmutableSet<Score> getScores(String entry) throws IllegalArgumentException {
        Validate.notNull(entry, "Entry cannot be null");

        ImmutableSet.Builder<Score> scores = ImmutableSet.builder();
        for (ScoreboardObjective objective : (Collection<ScoreboardObjective>) this.board.getObjectives()) {
            scores.add(new CraftScore(new CraftObjective(this, objective), entry));
        }
        return scores.build();
    }

    public void resetScores(OfflinePlayer player) throws IllegalArgumentException {
        Validate.notNull(player, "OfflinePlayer cannot be null");

        resetScores(player.getName());
    }

    public void resetScores(String entry) throws IllegalArgumentException {
        Validate.notNull(entry, "Entry cannot be null");

        for (ScoreboardObjective objective : (Collection<ScoreboardObjective>) this.board.getObjectives()) {
            board.resetPlayerScores(entry, objective);
        }
    }

    public Team getPlayerTeam(OfflinePlayer player) throws IllegalArgumentException {
        Validate.notNull(player, "OfflinePlayer cannot be null");

        ScoreboardTeam team = board.getPlayerTeam(player.getName());
        return team == null ? null : new CraftTeam(this, team);
    }

    public Team getEntryTeam(String entry) throws IllegalArgumentException {
        Validate.notNull(entry, "Entry cannot be null");

        ScoreboardTeam team = board.getPlayerTeam(entry);
        return team == null ? null : new CraftTeam(this, team);
    }

    public Team getTeam(String teamName) throws IllegalArgumentException {
        Validate.notNull(teamName, "Team name cannot be null");

        ScoreboardTeam team = board.getTeam(teamName);
        return team == null ? null : new CraftTeam(this, team);
    }

    public ImmutableSet<Team> getTeams() {
        return ImmutableSet.copyOf(Iterables.transform((Collection<ScoreboardTeam>) this.board.getTeams(), new Function<ScoreboardTeam, Team>() {

            @Override
            public Team apply(ScoreboardTeam input) {
                return new CraftTeam(CraftScoreboard.this, input);
            }
        }));
    }

    public Team registerNewTeam(String name) throws IllegalArgumentException {
        Validate.notNull(name, "Team name cannot be null");
        Validate.isTrue(name.length() <= 16, "Team name '" + name + "' is longer than the limit of 16 characters");
        Validate.isTrue(board.getTeam(name) == null, "Team name '" + name + "' is already in use");

        return new CraftTeam(this, board.createTeam(name));
    }

    public ImmutableSet<OfflinePlayer> getPlayers() {
        ImmutableSet.Builder<OfflinePlayer> players = ImmutableSet.builder();
        for (Object playerName : board.getPlayers()) {
            players.add(Bukkit.getOfflinePlayer(playerName.toString()));
        }
        return players.build();
    }

    public ImmutableSet<String> getEntries() {
        ImmutableSet.Builder<String> entries = ImmutableSet.builder();
        for (Object entry : board.getPlayers()) {
            entries.add(entry.toString());
        }
        return entries.build();
    }

    public void clearSlot(DisplaySlot slot) throws IllegalArgumentException {
        Validate.notNull(slot, "Slot cannot be null");
        board.setDisplaySlot(CraftScoreboardTranslations.fromBukkitSlot(slot), null);
    }

    // CraftBukkit method
    public Scoreboard getHandle() {
        return board;
    }
}
