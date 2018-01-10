package org.bukkit.craftbukkit.boss;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import net.minecraft.server.BossBattle;
import net.minecraft.server.BossBattleServer;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.PacketPlayOutBoss;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.entity.Player;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class CraftBossBar implements BossBar {

    private final BossBattleServer handle;
    private final Set<BarFlag> flags;
    private BarColor color;
    private BarStyle style;

    public CraftBossBar(String title, BarColor color, BarStyle style, BarFlag... flags) {
        this.flags = flags.length > 0 ? EnumSet.of(flags[0], flags) : EnumSet.noneOf(BarFlag.class);
        this.color = color;
        this.style = style;

        handle = new BossBattleServer(
                CraftChatMessage.fromString(title, true)[0],
                convertColor(color),
                convertStyle(style)
        );

        updateFlags();
    }

    private BossBattle.BarColor convertColor(BarColor color) {
        BossBattle.BarColor nmsColor = BossBattle.BarColor.valueOf(color.name());
        return (nmsColor == null) ? BossBattle.BarColor.WHITE : nmsColor;
    }

    private BossBattle.BarStyle convertStyle(BarStyle style) {
        switch (style) {
            default:
            case SOLID:
                return BossBattle.BarStyle.PROGRESS;
            case SEGMENTED_6:
                return BossBattle.BarStyle.NOTCHED_6;
            case SEGMENTED_10:
                return BossBattle.BarStyle.NOTCHED_10;
            case SEGMENTED_12:
                return BossBattle.BarStyle.NOTCHED_12;
            case SEGMENTED_20:
                return BossBattle.BarStyle.NOTCHED_20;
        }
    }

    private void updateFlags() {
        handle.a(hasFlag(BarFlag.DARKEN_SKY));
        handle.b(hasFlag(BarFlag.PLAY_BOSS_MUSIC));
        handle.c(hasFlag(BarFlag.CREATE_FOG));
    }

    @Override
    public String getTitle() {
        return CraftChatMessage.fromComponent(handle.e());
    }

    @Override
    public void setTitle(String title) {
        handle.title = CraftChatMessage.fromString(title, true)[0];
        handle.sendUpdate(PacketPlayOutBoss.Action.UPDATE_NAME);
    }

    @Override
    public BarColor getColor() {
        return color;
    }

    @Override
    public void setColor(BarColor color) {
        this.color = color;
        handle.color = convertColor(color);
        handle.sendUpdate(PacketPlayOutBoss.Action.UPDATE_STYLE);
    }

    @Override
    public BarStyle getStyle() {
        return style;
    }

    @Override
    public void setStyle(BarStyle style) {
        this.style = style;
        handle.style = convertStyle(style);
        handle.sendUpdate(PacketPlayOutBoss.Action.UPDATE_STYLE);
    }

    @Override
    public void addFlag(BarFlag flag) {
        flags.add(flag);
        updateFlags();
    }

    @Override
    public void removeFlag(BarFlag flag) {
        flags.remove(flag);
        updateFlags();
    }

    @Override
    public boolean hasFlag(BarFlag flag) {
        return flags.contains(flag);
    }

    @Override
    public void setProgress(double progress) {
    	Preconditions.checkArgument(progress >= 0.0 && progress <= 1.0, "Progress must be between 0.0 and 1.0 (%s)", progress);
        handle.setProgress((float) progress);
    }

    @Override
    public double getProgress() {
        return handle.getProgress();
    }

    @Override
    public void addPlayer(Player player) {
        handle.addPlayer(((CraftPlayer) player).getHandle());
    }

    @Override
    public void removePlayer(Player player) {
        handle.removePlayer(((CraftPlayer) player).getHandle());
    }

    @Override
    public List<Player> getPlayers() {
        ImmutableList.Builder<Player> players = ImmutableList.builder();
        for (EntityPlayer p : handle.getPlayers()) {
            players.add(p.getBukkitEntity());
        }
        return players.build();
    }

    @Override
    public void setVisible(boolean visible) {
        handle.setVisible(visible);
    }

    @Override
    public boolean isVisible() {
        return handle.visible;
    }

    @Override
    public void show() {
        handle.setVisible(true);
    }

    @Override
    public void hide() {
        handle.setVisible(false);
    }

    @Override
    public void removeAll() {
        for (Player player : getPlayers()) {
            removePlayer(player);
        }
    }
}
