package org.bukkit.craftbukkit.boss;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.network.protocol.game.ClientboundBossEventPacket;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.entity.Player;

public class CraftBossBar implements BossBar {

    private final ServerBossEvent handle;
    private Map<BarFlag, FlagContainer> flags;

    public CraftBossBar(String title, BarColor color, BarStyle style, BarFlag... flags) {
        this.handle = new ServerBossEvent(
                CraftChatMessage.fromString(title, true)[0],
                this.convertColor(color),
                this.convertStyle(style)
        );

        this.initialize();

        for (BarFlag flag : flags) {
            this.addFlag(flag);
        }

        this.setColor(color);
        this.setStyle(style);
    }

    public CraftBossBar(ServerBossEvent bossBattleServer) {
        this.handle = bossBattleServer;
        this.initialize();
    }

    private void initialize() {
        this.flags = new HashMap<>();
        this.flags.put(BarFlag.DARKEN_SKY, new FlagContainer(this.handle::shouldDarkenScreen, this.handle::setDarkenScreen));
        this.flags.put(BarFlag.PLAY_BOSS_MUSIC, new FlagContainer(this.handle::shouldPlayBossMusic, this.handle::setPlayBossMusic));
        this.flags.put(BarFlag.CREATE_FOG, new FlagContainer(this.handle::shouldCreateWorldFog, this.handle::setCreateWorldFog));
    }

    private BarColor convertColor(BossEvent.BossBarColor color) {
        return BarColor.valueOf(color.name());
    }

    private BossEvent.BossBarColor convertColor(BarColor color) {
        return BossEvent.BossBarColor.valueOf(color.name());
    }

    private BossEvent.BossBarOverlay convertStyle(BarStyle style) {
        return switch (style) {
            case SOLID -> BossEvent.BossBarOverlay.PROGRESS;
            case SEGMENTED_6 -> BossEvent.BossBarOverlay.NOTCHED_6;
            case SEGMENTED_10 -> BossEvent.BossBarOverlay.NOTCHED_10;
            case SEGMENTED_12 -> BossEvent.BossBarOverlay.NOTCHED_12;
            case SEGMENTED_20 -> BossEvent.BossBarOverlay.NOTCHED_20;
        };
    }

    private BarStyle convertStyle(BossEvent.BossBarOverlay style) {
        return switch (style) {
            case PROGRESS -> BarStyle.SOLID;
            case NOTCHED_6 -> BarStyle.SEGMENTED_6;
            case NOTCHED_10 -> BarStyle.SEGMENTED_10;
            case NOTCHED_12 -> BarStyle.SEGMENTED_12;
            case NOTCHED_20 -> BarStyle.SEGMENTED_20;
        };
    }

    @Override
    public String getTitle() {
        return CraftChatMessage.fromComponent(this.handle.name);
    }

    @Override
    public void setTitle(String title) {
        this.handle.name = CraftChatMessage.fromString(title, true)[0];
        this.handle.broadcast(ClientboundBossEventPacket::createUpdateNamePacket);
    }

    @Override
    public BarColor getColor() {
        return this.convertColor(this.handle.color);
    }

    @Override
    public void setColor(BarColor color) {
        this.handle.color = this.convertColor(color);
        this.handle.broadcast(ClientboundBossEventPacket::createUpdateStylePacket);
    }

    @Override
    public BarStyle getStyle() {
        return this.convertStyle(this.handle.overlay);
    }

    @Override
    public void setStyle(BarStyle style) {
        this.handle.overlay = this.convertStyle(style);
        this.handle.broadcast(ClientboundBossEventPacket::createUpdateStylePacket);
    }

    @Override
    public void addFlag(BarFlag flag) {
        FlagContainer flagContainer = this.flags.get(flag);
        if (flagContainer != null) {
            flagContainer.set.accept(true);
        }
    }

    @Override
    public void removeFlag(BarFlag flag) {
        FlagContainer flagContainer = this.flags.get(flag);
        if (flagContainer != null) {
            flagContainer.set.accept(false);
        }
    }

    @Override
    public boolean hasFlag(BarFlag flag) {
        FlagContainer flagContainer = this.flags.get(flag);
        if (flagContainer != null) {
            return flagContainer.get.get();
        }
        return false;
    }

    @Override
    public void setProgress(double progress) {
        Preconditions.checkArgument(progress >= 0.0 && progress <= 1.0, "Progress must be between 0.0 and 1.0 (%s)", progress);
        this.handle.setProgress((float) progress);
    }

    @Override
    public double getProgress() {
        return this.handle.getProgress();
    }

    @Override
    public void addPlayer(Player player) {
        Preconditions.checkArgument(player != null, "player == null");
        Preconditions.checkArgument(((CraftPlayer) player).getHandle().connection != null, "player is not fully connected (wait for PlayerJoinEvent)");

        this.handle.addPlayer(((CraftPlayer) player).getHandle());
    }

    @Override
    public void removePlayer(Player player) {
        Preconditions.checkArgument(player != null, "player == null");

        this.handle.removePlayer(((CraftPlayer) player).getHandle());
    }

    @Override
    public List<Player> getPlayers() {
        ImmutableList.Builder<Player> players = ImmutableList.builder();
        for (ServerPlayer p : this.handle.getPlayers()) {
            players.add(p.getBukkitEntity());
        }
        return players.build();
    }

    @Override
    public void setVisible(boolean visible) {
        this.handle.setVisible(visible);
    }

    @Override
    public boolean isVisible() {
        return this.handle.visible;
    }

    @Override
    public void show() {
        this.handle.setVisible(true);
    }

    @Override
    public void hide() {
        this.handle.setVisible(false);
    }

    @Override
    public void removeAll() {
        for (Player player : this.getPlayers()) {
            this.removePlayer(player);
        }
    }

    private final class FlagContainer {

        private final Supplier<Boolean> get;
        private final Consumer<Boolean> set;

        private FlagContainer(Supplier<Boolean> get, Consumer<Boolean> set) {
            this.get = get;
            this.set = set;
        }
    }

    public ServerBossEvent getHandle() {
        return this.handle;
    }
}
