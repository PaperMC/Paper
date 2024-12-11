package io.papermc.paper.adventure;

import com.google.common.collect.Collections2;
import java.util.Set;
import java.util.function.Function;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.bossbar.BossBarImplementation;
import net.kyori.adventure.bossbar.BossBarViewer;
import net.kyori.adventure.text.Component;
import net.minecraft.network.protocol.game.ClientboundBossEventPacket;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public final class BossBarImplementationImpl implements BossBar.Listener, BossBarImplementation {
    private final BossBar bar;
    private ServerBossEvent vanilla;

    public BossBarImplementationImpl(final BossBar bar) {
        this.bar = bar;
    }

    public void playerShow(final CraftPlayer player) {
        if (this.vanilla == null) {
            this.vanilla = new ServerBossEvent(
                PaperAdventure.asVanilla(this.bar.name()),
                PaperAdventure.asVanilla(this.bar.color()),
                PaperAdventure.asVanilla(this.bar.overlay())
            );
            this.vanilla.adventure = this.bar;
            this.bar.addListener(this);
        }
        this.vanilla.addPlayer(player.getHandle());
    }

    public void playerHide(final CraftPlayer player) {
        if (this.vanilla != null) {
            this.vanilla.removePlayer(player.getHandle());
            if (this.vanilla.getPlayers().isEmpty()) {
                this.bar.removeListener(this);
                this.vanilla = null;
            }
        }
    }

    @Override
    public void bossBarNameChanged(final @NonNull BossBar bar, final @NonNull Component oldName, final @NonNull Component newName) {
        this.maybeBroadcast(ClientboundBossEventPacket::createUpdateNamePacket);
    }

    @Override
    public void bossBarProgressChanged(final @NonNull BossBar bar, final float oldProgress, final float newProgress) {
        this.maybeBroadcast(ClientboundBossEventPacket::createUpdateProgressPacket);
    }

    @Override
    public void bossBarColorChanged(final @NonNull BossBar bar, final BossBar.@NonNull Color oldColor, final BossBar.@NonNull Color newColor) {
        this.maybeBroadcast(ClientboundBossEventPacket::createUpdateStylePacket);
    }

    @Override
    public void bossBarOverlayChanged(final @NonNull BossBar bar, final BossBar.@NonNull Overlay oldOverlay, final BossBar.@NonNull Overlay newOverlay) {
        this.maybeBroadcast(ClientboundBossEventPacket::createUpdateStylePacket);
    }

    @Override
    public void bossBarFlagsChanged(final @NonNull BossBar bar, final @NonNull Set<BossBar.Flag> flagsAdded, final @NonNull Set<BossBar.Flag> flagsRemoved) {
        this.maybeBroadcast(ClientboundBossEventPacket::createUpdatePropertiesPacket);
    }

    @Override
    public @NotNull Iterable<? extends BossBarViewer> viewers() {
        return this.vanilla == null ? Set.of() : Collections2.transform(this.vanilla.getPlayers(), ServerPlayer::getBukkitEntity);
    }

    private void maybeBroadcast(final Function<BossEvent, ClientboundBossEventPacket> fn) {
        if (this.vanilla != null) {
            this.vanilla.broadcast(fn);
        }
    }
}
