package org.bukkit.craftbukkit.boss;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import io.papermc.paper.math.Position;
import io.papermc.paper.util.MCUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.dimension.end.DragonRespawnStage;
import net.minecraft.world.level.dimension.end.EnderDragonFight;
import org.bukkit.Location;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.DragonBattle;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftEnderCrystal;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EnderDragon;

public class CraftDragonBattle implements DragonBattle {

    private final EnderDragonFight handle;

    public CraftDragonBattle(EnderDragonFight handle) {
        this.handle = handle;
    }

    @Override
    public EnderDragon getEnderDragon() {
        Entity entity = this.handle.level.getEntity(this.handle.dragonUUID);
        return (entity != null) ? (EnderDragon) entity.getBukkitEntity() : null;
    }

    @Override
    public BossBar getBossBar() {
        return new CraftBossBar(this.handle.dragonEvent);
    }

    @Override
    public Location getEndPortalLocation() {
        if (this.handle.exitPortalLocation == null) {
            return null;
        }

        return CraftLocation.toBukkit(this.handle.exitPortalLocation, this.handle.level);
    }

    @Override
    public boolean generateEndPortal(boolean withPortals) {
        if (this.handle.exitPortalLocation != null || this.handle.findExitPortal() != null) {
            return false;
        }

        this.handle.spawnExitPortal(withPortals);
        return true;
    }

    @Override
    public boolean hasBeenPreviouslyKilled() {
        return this.handle.hasPreviouslyKilledDragon();
    }

    @Override
    public void setPreviouslyKilled(boolean previouslyKilled) {
        this.handle.hasPreviouslyKilledDragon = previouslyKilled;
    }

    @Override
    public void initiateRespawn() {
        this.handle.tryRespawn();
    }

    @Override
    public boolean initiateRespawn(Collection<EnderCrystal> crystals) { // todo doesn't seems to work without crystals
        if (this.hasBeenPreviouslyKilled() && this.getRespawnPhase() == RespawnPhase.NONE) {
            // Copy from EnderDragonFight#tryRespawn for generate exit portal if not exists
            if (this.handle.exitPortalLocation == null) {
                BlockPattern.BlockPatternMatch match = this.handle.findExitPortal();
                if (match == null) {
                    this.handle.spawnExitPortal(true);
                }
            }

            List<EnderCrystal> filteredCrystals = crystals != null ? new ArrayList<>(crystals) : new ArrayList<>();
            filteredCrystals.removeIf(crystal -> {
                if (crystal == null) {
                    return true;
                }
                return !((CraftWorld) crystal.getWorld()).getHandle().equals(this.handle.level);
            });

            return this.handle.respawnDragon(
                Lists.transform(filteredCrystals, crystal -> ((CraftEnderCrystal) crystal).getHandle())
            );
        }
        return false;
    }

    @Override
    public RespawnPhase getRespawnPhase() {
        return this.toBukkitRespawnPhase(this.handle.respawnStage);
    }

    @Override
    public boolean setRespawnPhase(RespawnPhase phase) {
        Preconditions.checkArgument(phase != null && phase != RespawnPhase.NONE, "Invalid respawn phase provided: %s", phase);

        if (this.handle.respawnStage == null) {
            return false;
        }

        this.handle.setRespawnStage(this.toNMSRespawnPhase(phase));
        return true;
    }

    @Override
    public void resetCrystals() {
        this.handle.resetSpikeCrystals();
    }

    @Override
    public int hashCode() {
        return this.handle.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CraftDragonBattle && ((CraftDragonBattle) obj).handle == this.handle;
    }

    private RespawnPhase toBukkitRespawnPhase(DragonRespawnStage phase) {
        return (phase != null) ? RespawnPhase.values()[phase.ordinal()] : RespawnPhase.NONE;
    }

    private DragonRespawnStage toNMSRespawnPhase(RespawnPhase phase) {
        return (phase != RespawnPhase.NONE) ? DragonRespawnStage.values()[phase.ordinal()] : null;
    }

    @Override
    public int getGatewayCount() {
        return EnderDragonFight.GATEWAY_COUNT - this.handle.gateways.size();
    }

    @Override
    public boolean spawnNewGateway() {
        return this.handle.spawnNewGateway();
    }

    @Override
    public void spawnNewGateway(final Position position) {
        this.handle.spawnNewGateway(MCUtil.toBlockPos(position));
    }

    @Override
    public List<EnderCrystal> getRespawnCrystals() {
        if (this.handle.respawnCrystals.isEmpty()) {
            return Collections.emptyList();
        }

        final List<EnderCrystal> crystals = new ArrayList<>();
        for (final net.minecraft.world.entity.EntityReference<EndCrystal> ref : this.handle.respawnCrystals) {
            final EndCrystal crystal = ref.getEntity(this.handle.level, EndCrystal.class);
            if (crystal != null && !crystal.isRemoved() && crystal.isAlive() && crystal.valid) {
                crystals.add(((EnderCrystal) crystal.getBukkitEntity()));
            }
        }
        return Collections.unmodifiableList(crystals);
    }

    @Override
    public List<EnderCrystal> getHealingCrystals() {
        final List<EnderCrystal> enderCrystals = new ArrayList<>();
        for (final EndCrystal crystal : this.handle.getSpikeCrystals()) {
            if (!crystal.isRemoved() && crystal.isAlive() && crystal.valid) {
                enderCrystals.add(((EnderCrystal) crystal.getBukkitEntity()));
            }
        }
        return Collections.unmodifiableList(enderCrystals);
    }
}
