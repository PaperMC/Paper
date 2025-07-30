package org.bukkit.craftbukkit.scoreboard;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import net.kyori.adventure.audience.Audience;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Team.Visibility;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Team;

final class CraftTeam extends CraftScoreboardComponent implements Team {
    private final PlayerTeam team;

    CraftTeam(CraftScoreboard scoreboard, PlayerTeam team) {
        super(scoreboard);
        this.team = team;
    }

    @Override
    public String getName() {
        this.checkState();

        return this.team.getName();
    }

    @Override
    public net.kyori.adventure.text.Component displayName() throws IllegalStateException {
        this.checkState();
        return io.papermc.paper.adventure.PaperAdventure.asAdventure(this.team.getDisplayName());
    }

    @Override
    public void displayName(net.kyori.adventure.text.Component displayName) throws IllegalStateException, IllegalArgumentException {
        if (displayName == null) displayName = net.kyori.adventure.text.Component.empty();
        this.checkState();
        this.team.setDisplayName(io.papermc.paper.adventure.PaperAdventure.asVanilla(displayName));
    }

    @Override
    public net.kyori.adventure.text.Component prefix() throws IllegalStateException {
        this.checkState();
        return io.papermc.paper.adventure.PaperAdventure.asAdventure(this.team.getPlayerPrefix());
    }

    @Override
    public void prefix(net.kyori.adventure.text.Component prefix) throws IllegalStateException, IllegalArgumentException {
        if (prefix == null) prefix = net.kyori.adventure.text.Component.empty();
        this.checkState();
        this.team.setPlayerPrefix(io.papermc.paper.adventure.PaperAdventure.asVanilla(prefix));
    }

    @Override
    public net.kyori.adventure.text.Component suffix() throws IllegalStateException {
        this.checkState();
        return io.papermc.paper.adventure.PaperAdventure.asAdventure(this.team.getPlayerSuffix());
    }

    @Override
    public void suffix(net.kyori.adventure.text.Component suffix) throws IllegalStateException, IllegalArgumentException {
        if (suffix == null) suffix = net.kyori.adventure.text.Component.empty();
        this.checkState();
        this.team.setPlayerSuffix(io.papermc.paper.adventure.PaperAdventure.asVanilla(suffix));
    }

    @Override
    public boolean hasColor() {
        this.checkState();
        return this.team.getColor().getColor() != null;
    }

    @Override
    public net.kyori.adventure.text.format.TextColor color() throws IllegalStateException {
        Preconditions.checkState(this.team.getColor().getColor() != null, "Team colors must have hex values");
        this.checkState();

        net.kyori.adventure.text.format.TextColor color = net.kyori.adventure.text.format.TextColor.color(this.team.getColor().getColor());
        if (!(color instanceof net.kyori.adventure.text.format.NamedTextColor)) {
            throw new IllegalStateException("Team doesn't have a NamedTextColor");
        }
        return color;
    }

    @Override
    public void color(net.kyori.adventure.text.format.NamedTextColor color) {
        this.checkState();
        this.team.setColor(color == null ? net.minecraft.ChatFormatting.RESET : io.papermc.paper.adventure.PaperAdventure.asVanilla(color));
    }

    @Override
    public String getDisplayName() {
        this.checkState();

        return CraftChatMessage.fromComponent(this.team.getDisplayName());
    }

    @Override
    public void setDisplayName(String displayName) {
        Preconditions.checkArgument(displayName != null, "Display name cannot be null");
        this.checkState();

        this.team.setDisplayName(CraftChatMessage.fromString(displayName)[0]); // SPIGOT-4112: not nullable
    }

    @Override
    public String getPrefix() {
        this.checkState();

        return CraftChatMessage.fromComponent(this.team.getPlayerPrefix());
    }

    @Override
    public void setPrefix(String prefix) {
        Preconditions.checkArgument(prefix != null, "Prefix cannot be null");
        this.checkState();

        this.team.setPlayerPrefix(CraftChatMessage.fromStringOrNull(prefix));
    }

    @Override
    public String getSuffix() {
        this.checkState();

        return CraftChatMessage.fromComponent(this.team.getPlayerSuffix());
    }

    @Override
    public void setSuffix(String suffix) {
        Preconditions.checkArgument(suffix != null, "Suffix cannot be null");
        this.checkState();

        this.team.setPlayerSuffix(CraftChatMessage.fromStringOrNull(suffix));
    }

    @Override
    public ChatColor getColor() {
        this.checkState();

        return CraftChatMessage.getColor(this.team.getColor());
    }

    @Override
    public void setColor(ChatColor color) {
        Preconditions.checkArgument(color != null, "Color cannot be null");
        Preconditions.checkArgument(!color.isFormat(), "Color must be a color not a format");
        this.checkState();

        this.team.setColor(CraftChatMessage.getColor(color));
    }

    @Override
    public boolean allowFriendlyFire() {
        this.checkState();

        return this.team.isAllowFriendlyFire();
    }

    @Override
    public void setAllowFriendlyFire(boolean enabled) {
        this.checkState();

        this.team.setAllowFriendlyFire(enabled);
    }

    @Override
    public boolean canSeeFriendlyInvisibles() {
        this.checkState();

        return this.team.canSeeFriendlyInvisibles();
    }

    @Override
    public void setCanSeeFriendlyInvisibles(boolean enabled) {
        this.checkState();

        this.team.setSeeFriendlyInvisibles(enabled);
    }

    @Override
    public NameTagVisibility getNameTagVisibility() throws IllegalArgumentException {
        this.checkState();

        return CraftTeam.notchToBukkit(this.team.getNameTagVisibility());
    }

    @Override
    public void setNameTagVisibility(NameTagVisibility visibility) throws IllegalArgumentException {
        this.checkState();

        this.team.setNameTagVisibility(CraftTeam.bukkitToNotch(visibility));
    }

    @Override
    public Set<OfflinePlayer> getPlayers() {
        this.checkState();

        ImmutableSet.Builder<OfflinePlayer> players = ImmutableSet.builder();
        for (String playerName : this.team.getPlayers()) {
            players.add(Bukkit.getOfflinePlayer(playerName));
        }
        return players.build();
    }

    @Override
    public Set<String> getEntries() {
        this.checkState();

        ImmutableSet.Builder<String> entries = ImmutableSet.builder();
        for (String playerName : this.team.getPlayers()) {
            entries.add(playerName);
        }
        return entries.build();
    }

    @Override
    public int getSize() {
        this.checkState();

        return this.team.getPlayers().size();
    }

    @Override
    public void addPlayer(OfflinePlayer player) {
        Preconditions.checkArgument(player != null, "OfflinePlayer cannot be null");
        this.addEntry(player.getName());
    }

    @Override
    public void addEntry(String entry) {
        Preconditions.checkArgument(entry != null, "Entry cannot be null");
        this.checkState();

        this.getScoreboard().getHandle().addPlayerToTeam(entry, this.team);
    }

    @Override
    public void addEntities(java.util.Collection<org.bukkit.entity.Entity> entities) throws IllegalStateException, IllegalArgumentException {
        this.addEntries(entities.stream().map(entity -> ((org.bukkit.craftbukkit.entity.CraftEntity) entity).getHandle().getScoreboardName()).toList());
    }

    @Override
    public void addEntries(java.util.Collection<String> entries) throws IllegalStateException, IllegalArgumentException {
        Preconditions.checkArgument(entries != null, "Entries cannot be null");
        this.checkState();

        ((net.minecraft.server.ServerScoreboard) this.getScoreboard().getHandle()).addPlayersToTeam(entries, this.team);
    }

    @Override
    public boolean removePlayer(OfflinePlayer player) {
        Preconditions.checkArgument(player != null, "OfflinePlayer cannot be null");
        return this.removeEntry(player.getName());
    }

    @Override
    public boolean removeEntry(String entry) {
        Preconditions.checkArgument(entry != null, "Entry cannot be null");
        this.checkState();

        if (!this.team.getPlayers().contains(entry)) {
            return false;
        }

        this.getScoreboard().getHandle().removePlayerFromTeam(entry, this.team);
        return true;
    }

    @Override
    public boolean removeEntities(java.util.Collection<org.bukkit.entity.Entity> entities) throws IllegalStateException, IllegalArgumentException {
        return this.removeEntries(entities.stream().map(entity -> ((org.bukkit.craftbukkit.entity.CraftEntity) entity).getHandle().getScoreboardName()).toList());
    }

    @Override
    public boolean removeEntries(java.util.Collection<String> entries) throws IllegalStateException, IllegalArgumentException {
        Preconditions.checkArgument(entries != null, "Entry cannot be null");
        this.checkState();

        for (String entry : entries) {
            if (this.team.getPlayers().contains(entry)) {
                ((net.minecraft.server.ServerScoreboard) this.getScoreboard().getHandle()).removePlayersFromTeam(entries, this.team);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean hasPlayer(OfflinePlayer player) throws IllegalArgumentException, IllegalStateException {
        Preconditions.checkArgument(player != null, "OfflinePlayer cannot be null");
        return this.hasEntry(player.getName());
    }

    @Override
    public boolean hasEntry(String entry) throws IllegalArgumentException, IllegalStateException {
        Preconditions.checkArgument(entry != null, "Entry cannot be null");
        this.checkState();

        return this.team.getPlayers().contains(entry);
    }

    @Override
    public void unregister() {
        this.checkState();
        this.getScoreboard().getHandle().removePlayerTeam(this.team);
    }

    @Override
    public OptionStatus getOption(Option option) {
        this.checkState();

        Enum<?> value = switch (option) {
            case NAME_TAG_VISIBILITY -> this.team.getNameTagVisibility();
            case DEATH_MESSAGE_VISIBILITY -> this.team.getDeathMessageVisibility();
            case COLLISION_RULE -> this.team.getCollisionRule();
        };
        return OptionStatus.values()[value.ordinal()];
    }

    @Override
    public void setOption(Option option, OptionStatus status) {
        this.checkState();

        switch (option) {
            case NAME_TAG_VISIBILITY -> this.team.setNameTagVisibility(Visibility.values()[status.ordinal()]);
            case DEATH_MESSAGE_VISIBILITY -> this.team.setDeathMessageVisibility(Visibility.values()[status.ordinal()]);
            case COLLISION_RULE -> this.team.setCollisionRule(net.minecraft.world.scores.Team.CollisionRule.values()[status.ordinal()]);
            default -> throw new IllegalArgumentException("Unrecognised option " + option);
        }
    }

    @Override
    public void addEntity(org.bukkit.entity.Entity entity) throws IllegalStateException, IllegalArgumentException {
        Preconditions.checkArgument(entity != null, "Entity cannot be null");
        this.addEntry(((org.bukkit.craftbukkit.entity.CraftEntity) entity).getHandle().getScoreboardName());
    }

    @Override
    public boolean removeEntity(org.bukkit.entity.Entity entity) throws IllegalStateException, IllegalArgumentException {
        Preconditions.checkArgument(entity != null, "Entity cannot be null");
        return this.removeEntry(((org.bukkit.craftbukkit.entity.CraftEntity) entity).getHandle().getScoreboardName());
    }

    @Override
    public boolean hasEntity(org.bukkit.entity.Entity entity) throws IllegalStateException, IllegalArgumentException {
        Preconditions.checkArgument(entity != null, "Entity cannot be null");
        return this.hasEntry(((org.bukkit.craftbukkit.entity.CraftEntity) entity).getHandle().getScoreboardName());
    }

    @Override
    public Iterable<? extends Audience> audiences() {
        this.checkState();
        List<Audience> audiences = new ArrayList<>();
        for (String playerName : this.team.getPlayers()) {
            org.bukkit.entity.Player player = Bukkit.getPlayerExact(playerName);
            if (player != null) {
                audiences.add(player);
            }
        }

        return audiences;
    }

    @Deprecated
    public static Visibility bukkitToNotch(NameTagVisibility visibility) {
        return switch (visibility) {
            case ALWAYS -> Visibility.ALWAYS;
            case NEVER -> Visibility.NEVER;
            case HIDE_FOR_OTHER_TEAMS -> Visibility.HIDE_FOR_OTHER_TEAMS;
            case HIDE_FOR_OWN_TEAM -> Visibility.HIDE_FOR_OWN_TEAM;
        };
    }

    @Deprecated
    public static NameTagVisibility notchToBukkit(Visibility visibility) {
        return switch (visibility) {
            case ALWAYS -> NameTagVisibility.ALWAYS;
            case NEVER -> NameTagVisibility.NEVER;
            case HIDE_FOR_OTHER_TEAMS -> NameTagVisibility.HIDE_FOR_OTHER_TEAMS;
            case HIDE_FOR_OWN_TEAM -> NameTagVisibility.HIDE_FOR_OWN_TEAM;
        };
    }

    @Override
    void checkState() {
        Preconditions.checkState(this.getScoreboard().getHandle().getPlayerTeam(this.team.getName()) != null, "Unregistered scoreboard component");
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + (this.team != null ? this.team.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final CraftTeam other = (CraftTeam) obj;
        return Objects.equals(this.team, other.team);
    }
}
